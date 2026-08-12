package com.equilibrium;

import com.equilibrium.block.CraftingDifficultyHelper;
import com.equilibrium.block.UseBlockActionUtil;
import com.equilibrium.block.anvil.AnvilBlocks;
import com.equilibrium.block.crafting_table.CraftingTableBlocks;
import com.equilibrium.block.enchanting_table.EnchantingTableBlocks;
import com.equilibrium.block.furnace.FurnaceBlocks;
import com.equilibrium.block.furnace.FurnaceEntityRegistry;
import com.equilibrium.block.material.MaterialBlocks;
import com.equilibrium.block.miscellaneous.MiscellaneousBlocks;
import com.equilibrium.block.ore.OreBlocks;
import com.equilibrium.entity.goal.BreakBlockGoal;
import com.equilibrium.item.armor.ArmorItems;
import com.equilibrium.item.coin.CoinItems;
import com.equilibrium.item.food.FoodItems;
import com.equilibrium.item.material.MaterialItems;
import com.equilibrium.item.ModItemGroups;
import com.equilibrium.item.miscellaneous.MiscellaneousItems;
import com.equilibrium.item.tool.ToolItems;
import com.equilibrium.network.*;
import com.equilibrium.server_and_client.server.SoundEventRegistry;
import com.equilibrium.server_and_client.server.command.ServerCommands;
import com.equilibrium.server_and_client.server.event.*;
import com.equilibrium.server_and_client.server.persistent_state.MapNbtSerializer;
import com.equilibrium.server_and_client.server.persistent_state.StateSaverAndLoader;
import com.equilibrium.status.RegisterStatusEffect;
import com.equilibrium.structure.StructureRegister;
import com.equilibrium.tags.ModBlockTags;
import com.equilibrium.tags.ModEntityTags;
import com.equilibrium.tags.ModItemTags;
import com.equilibrium.util.AdvancementRemover;
import com.equilibrium.util.BooleanStorageUtil;
import com.equilibrium.util.XpHashMap;
import net.minecraft.SharedConstants;
import net.minecraft.WorldVersion;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.DataVersion;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.equilibrium.difficulty_entry.DifficultyEntryGetter.isAnyExtraEntryExisting;
import static com.equilibrium.difficulty_entry.DifficultyEntryRegister.initGameRules;
import static com.equilibrium.server_and_client.server.event.CropIllnessEvent.updateCropBlockPos;
import static com.equilibrium.server_and_client.server.moonphase_tasks.MoonPhaseEvent.moonPhaseEvent;
import static com.equilibrium.util.BooleanStorageUtil.loadWorldInformation;

@Mod(OnServerInitialize.MOD_ID)
public class OnServerInitialize {

    public static final String MOD_ID = "miteequilibrium";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final BooleanProperty FERTILIZED = BooleanProperty.create("fertilized");
    public static final IntegerProperty GRASSBLOCK_POLLUTED = IntegerProperty.create("grassblock_polluted", 0, 7);
    public static final BooleanProperty CROP_IS_ILLNESS = BooleanProperty.create("crop_illness");

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private StateSaverAndLoader serverState;
    private int tickCount = 0;
    private static final int TICK_INTERVAL = 500;

    public OnServerInitialize(IEventBus modEventBus, ModContainer modContainer) {
        // 初始化游戏规则（必须在早期调用）
        initGameRules();

        // 注册网络包（服务端）
        S2CStockChangeGrassColorPacket.registerOnServer();
        S2CIllnessTextureBooleanPacket.registerOnServer();
        S2CGameRuleSyncPayloadForBooleanPacket.registerOnServer();

        // 注册所有 DeferredRegister
        registerDeferredRegisters(modEventBus);

        // 注册结构 FEATURES 到模组事件总线（已注释，防止重复注册）
        // StructureRegister.FEATURES.register(modEventBus);

        // 注册熔炉实体
        FurnaceEntityRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);

        // 注册自定义世界版本
        SharedConstants.CURRENT_VERSION = new CustomWorldVersion();

        // 注册模组生命周期事件（FMLCommonSetupEvent 属于模组总线）
        modEventBus.addListener(this::onCommonSetup);

        // 注意：游戏事件监听器（NeoForge.EVENT_BUS）的注册已移至 onCommonSetup 中，
        // 以确保注册表完全加载后再绑定，避免 DeferredHolder 过早访问。

        // 启动定时清理任务（不影响注册表）
        scheduler.scheduleAtFixedRate(() -> {
            synchronized (BreakBlockGoal.blockBreakProgressMap) {
                BreakBlockGoal.blockBreakProgressMap.clear();
                LOGGER.debug("Progress map cleared.");
            }
        }, 240, 240, TimeUnit.SECONDS);

        // 初始化经验映射（静态操作，安全）
        initXpMap();
    }

    private void registerDeferredRegisters(IEventBus modEventBus) {
        // 物品
        MaterialItems.ITEMS.register(modEventBus);
        FoodItems.ITEMS.register(modEventBus);
        ArmorItems.ITEMS.register(modEventBus);
        ToolItems.ITEMS.register(modEventBus);
        CoinItems.ITEMS.register(modEventBus);
        MiscellaneousItems.ITEMS.register(modEventBus);

        // 方块及对应物品
        AnvilBlocks.ITEMS.register(modEventBus);
        CraftingTableBlocks.ITEMS.register(modEventBus);
        EnchantingTableBlocks.ITEMS.register(modEventBus);
        FurnaceBlocks.ITEMS.register(modEventBus);
        MaterialBlocks.ITEMS.register(modEventBus);
        MiscellaneousBlocks.ITEMS.register(modEventBus);
        OreBlocks.ITEMS.register(modEventBus);

        AnvilBlocks.BLOCKS.register(modEventBus);
        CraftingTableBlocks.BLOCKS.register(modEventBus);
        EnchantingTableBlocks.BLOCKS.register(modEventBus);
        FurnaceBlocks.BLOCKS.register(modEventBus);
        MaterialBlocks.BLOCKS.register(modEventBus);
        MiscellaneousBlocks.BLOCKS.register(modEventBus);
        OreBlocks.BLOCKS.register(modEventBus);

        // 创造模式物品栏
        ModItemGroups.TABS.register(modEventBus);

        // 声音事件
        SoundEventRegistry.SOUND_EVENTS.register(modEventBus);

        // 状态效果
        RegisterStatusEffect.MOB_EFFECTS.register(modEventBus);


        StructureRegister.FEATURES.register(modEventBus);

    }

    // 原 onServerAboutToStart 改为监听 ServerStartingEvent（或 ServerStartedEvent）
    @Deprecated
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // 注册结构到生物群系
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        var server = event.getServer();

        // 成就删除
        AdvancementRemover.removeAllMinecraftAdvancements(server.getAdvancements().tree());

        // 锁定难度
        server.setDifficultyLocked(true);

        // 读取持久状态
        serverState = StateSaverAndLoader.getServerState(server);

        // 读取农作物疾病位置
        CropIllnessEvent.CROP_BLOCK_POS = MapNbtSerializer.fromNbt(
                serverState.mapNbt2,
                dis -> {
                    try {
                        return new BlockPos(dis.readInt(), dis.readInt(), dis.readInt());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                dis -> {
                    try {
                        return dis.readBoolean();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                ConcurrentHashMap::new
        );

        // 读取土地污染map
        S2CStockChangeGrassColorPacket.BLOCK_POS_INTEGER_CONCURRENT_HASH_MAP = MapNbtSerializer.fromNbt(
                serverState.mapNbt1,
                dis -> {
                    try {
                        return new BlockPos(dis.readInt(), dis.readInt(), dis.readInt());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                dis -> {
                    try {
                        return dis.readInt();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                },
                ConcurrentHashMap::new
        );
    }

    // 服务器 Tick 事件
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        var server = event.getServer();
        if (server == null) return;

        tickCount++;

        if (tickCount % (TICK_INTERVAL / 10) == 0) {
            // 获取世界
            ServerLevel overWorld = server.getLevel(ServerLevel.OVERWORLD);
            if (overWorld != null) {
                // 月相事件
                moonPhaseEvent(server);
                // 更新农作物
                updateCropBlockPos(overWorld);
            }

            // 更新玩家护甲
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                UpdateArmorEvent.updatePlayerArmor(player);
            }

            // 保存持久状态（每周期保存）
            if (serverState != null) {
                serverState.saveMapNbtToBuffer1();
                serverState.saveMapNbtToBuffer2();
            }

            checkAndHandleInvalidWorld(server);
        }

        if (tickCount >= TICK_INTERVAL) {
            tickCount = 0;
        }
    }

    private void checkAndHandleInvalidWorld(net.minecraft.server.MinecraftServer server) {
        boolean isGrandStageClear = false;
        Path path = server.getWorldPath(LevelResource.ROOT).normalize().resolve("WorldInformationRecorder.dat");
        BooleanStorageUtil.WorldInformationRecorder recorder = loadWorldInformation(path.toString());
        if (recorder != null && recorder.getIsGrandStageClear()) {
            isGrandStageClear = true;
        }

        if (isAnyExtraEntryExisting(server, null) && !isGrandStageClear) {
            server.setDifficulty(Difficulty.HARD, true);
            boolean allowCommands = server.getWorldData().isAllowCommands();
            List<ServerPlayer> players = server.getPlayerList().getPlayers();
            boolean anyCreativeOrSpectator = players.stream().anyMatch(p -> p.isCreative() || p.isSpectator());
            boolean hasPlayers = !players.isEmpty();

            if (hasPlayers && (allowCommands || anyCreativeOrSpectator)) {
                players.forEach(p -> p.displayClientMessage(
                        Component.literal("检测到错误的世界设置,服务器将在不久后强制清除玩家"), true
                ));
                new Thread(() -> {
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    if (server.isRunning()) {
                        server.execute(() -> {
                            if (server.isRunning()) {
                                server.getPlayerList().removeAll();
                            }
                        });
                    }
                }).start();
            }
        }
    }

    // 注册命令事件
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ServerCommands.registerCommands(event.getDispatcher());
    }

    @SubscribeEvent
    public void onItemUse(PlayerInteractEvent.RightClickItem event) {
        OnItemUseEvent.onUseItem(event.getEntity(), event.getLevel(), event.getHand());
    }

    @SubscribeEvent
    public void onBlockUse(PlayerInteractEvent.RightClickBlock event) {
        UseBlockActionUtil.canUseVanillaCraftingTable(event.getEntity(), event.getLevel(), event.getHitVec());
    }

    // 模组生命周期事件：此时所有 DeferredRegister 已绑定
    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CraftingDifficultyHelper.initCraftingDifficulties();
            ModBlockTags.registerModBlockTags();
            ModEntityTags.registerModEntityTags();
            ModItemTags.registerModItemTags();
            GlobalModConfig.initConfig();
            initXpMap();
        });

        // 注册表已完全加载，此时安全创建并注册事件监听器
        NeoForge.EVENT_BUS.register(new CraftingMetalPickAxeListener());
        NeoForge.EVENT_BUS.register(this);

        // 手动注册 BreakBlockEvent（现在其静态初始化已安全）
        NeoForge.EVENT_BUS.register(new BreakBlockEvent());
    }

    public static void initXpMap() {
        XpHashMap.setXpForLevel(1, 10);
        XpHashMap.setXpForLevel(2, 50);
        XpHashMap.setXpForLevel(3, 100);
        XpHashMap.setXpForLevel(4, 200);
        XpHashMap.setXpForLevel(5, 500);
    }

    private static class CustomWorldVersion implements WorldVersion {
        @Override
        public @NotNull DataVersion getDataVersion() {
            return new DataVersion(110111, "MITE:Equilibrium-NeoForge-1.7X-Alpha");
        }

        @Override
        public @NotNull String getId() {
            return "108109";
        }

        @Override
        public @NotNull String getName() {
            return "MITE:Equilibrium NeoForge 1.7X Alpha";
        }

        @Override
        public int getProtocolVersion() {
            return 108109;
        }

        @Override
        public int getPackVersion(@NotNull PackType packType) {
            return 34;
        }

        @Override
        public @NotNull Date getBuildTime() {
            return new Date();
        }

        @Override
        public boolean isStable() {
            return true;
        }
    }
}