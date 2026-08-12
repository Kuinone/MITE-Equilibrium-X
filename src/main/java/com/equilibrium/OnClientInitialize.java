package com.equilibrium;

import com.equilibrium.block.ModBlockScreenTypesRegister;
import com.equilibrium.block.anvil.iron_anvil_block.IronAnvilScreen;
import com.equilibrium.block.miscellaneous.MiscellaneousBlocks;
import com.equilibrium.block.anvil.adamantium_anvil_block.AdamantiumAnvilScreen;
import com.equilibrium.block.anvil.copper_anvil_block.CopperAnvilScreen;
import com.equilibrium.block.anvil.mithril_anvil_block.MithrilAnvilScreen;
import com.equilibrium.block.enchanting_table.ModBlockEntityTypes;
import com.equilibrium.block.enchanting_table.ModEnchantmentScreen;
import com.equilibrium.block.enchanting_table.diamond.DiamondEnchantingTableBlockEntityRenderer;
import com.equilibrium.block.enchanting_table.emerald.EmeraldEnchantingTableBlockEntityRenderer;
import com.equilibrium.item.armor.ArmorItems;
import com.equilibrium.network.S2CGameRuleSyncPayloadForBooleanPacket;
import com.equilibrium.network.S2CIllnessTextureBooleanPacket;
import com.equilibrium.network.S2CStockChangeGrassColorPacket;
import com.equilibrium.server_and_client.client.render.entity.model.BaseEarthElementalEntityModel;
import com.equilibrium.server_and_client.client.render.entity.renderer.*;
import com.equilibrium.server_and_client.client.render.entity.renderer.elemental.EndRockElementalEntityRenderer;
import com.equilibrium.server_and_client.client.render.entity.renderer.elemental.NetherrackElementalEntityRenderer;
import com.equilibrium.server_and_client.client.render.entity.renderer.elemental.ObsidianElementalEntityRenderer;
import com.equilibrium.server_and_client.client.render.entity.renderer.elemental.StoneElementalEntityRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

import static com.equilibrium.entity.ModEntities.*;
import static com.equilibrium.util.RenderBeaconBeam.RenderBeaconInit;

@Mod(value = OnServerInitialize.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = OnServerInitialize.MOD_ID, value = Dist.CLIENT)
public class OnClientInitialize {

    public OnClientInitialize(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        S2CStockChangeGrassColorPacket.registerOnClient();
        S2CIllnessTextureBooleanPacket.registerOnClient();
        S2CGameRuleSyncPayloadForBooleanPacket.registerOnClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // 绑定菜单与屏幕
            MenuScreens.register(ModBlockScreenTypesRegister.EMERALD_ENCHANTING_TABLE, ModEnchantmentScreen::new);
            MenuScreens.register(ModBlockScreenTypesRegister.DIAMOND_ENCHANTING_TABLE, ModEnchantmentScreen::new);
            MenuScreens.register(ModBlockScreenTypesRegister.COPPER_ANVIL_SCREEN_TYPE, CopperAnvilScreen::new);
            MenuScreens.register(ModBlockScreenTypesRegister.IRON_ANVIL_SCREEN_TYPE, IronAnvilScreen::new);
            MenuScreens.register(ModBlockScreenTypesRegister.MITHRIL_ANVIL_SCREEN_TYPE, MithrilAnvilScreen::new);
            MenuScreens.register(ModBlockScreenTypesRegister.ADAMANTIUM_ANVIL_SCREEN_TYPE, AdamantiumAnvilScreen::new);

            // 注册方块实体渲染器
            BlockEntityRenderers.register(ModBlockEntityTypes.EMERALD_ENCHANTING_TABLE_BLOCK_ENTITY_TYPE,
                    EmeraldEnchantingTableBlockEntityRenderer::new);
            BlockEntityRenderers.register(ModBlockEntityTypes.DIAMOND_ENCHANTING_TABLE_BLOCK_ENTITY_TYPE,
                    DiamondEnchantingTableBlockEntityRenderer::new);

            // 注册方块渲染类型 —— 洋葱方块使用 cutout 层
            ItemBlockRenderTypes.setRenderLayer(MiscellaneousBlocks.ONION_BLOCK.get(), RenderType.cutout());

            // 自定义信标光束渲染
            RenderBeaconInit();
        });
    }

    @SubscribeEvent
    public static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // 注册实体渲染器
        event.registerEntityRenderer(INVISIBLE_STALKER, InvisibleStalkerEntityRendererTransparent::new);
        event.registerEntityRenderer(GHOUL, GhoulEntityRenderer::new);
        event.registerEntityRenderer(SHADOW, ShadowEntityRenderer::new);
        event.registerEntityRenderer(WIGHT, WightEntityRenderer::new);
        event.registerEntityRenderer(REVENANT, RevenantEntityRenderer::new);
        event.registerEntityRenderer(LONG_DEAD, LongDeadEntityRenderer::new);
        event.registerEntityRenderer(PUDDING, PuddingSlimeEntityRenderer::new);
        event.registerEntityRenderer(BONE_LORD, BoneLordEntityRenderer::new);
        event.registerEntityRenderer(WOODEN_SPIDER, WoodenSpiderRenderer::new);
        event.registerEntityRenderer(FIRE_ELEMENTAL, FireElementalEntityRendererTransparent::new);

        event.registerEntityRenderer(STONE_ELEMENTAL, (context) -> {
            ModelPart modelPart = context.bakeLayer(ModelLayers.ZOMBIE);
            return new StoneElementalEntityRenderer(context, new BaseEarthElementalEntityModel<>(modelPart), 0.5f);
        });
        event.registerEntityRenderer(END_ROCK_ELEMENTAL, (context) -> {
            ModelPart modelPart = context.bakeLayer(ModelLayers.ZOMBIE);
            return new EndRockElementalEntityRenderer(context, new BaseEarthElementalEntityModel<>(modelPart), 0.5f);
        });
        event.registerEntityRenderer(NETHERROCK_ELEMENTAL, (context) -> {
            ModelPart modelPart = context.bakeLayer(ModelLayers.ZOMBIE);
            return new NetherrackElementalEntityRenderer(context, new BaseEarthElementalEntityModel<>(modelPart), 0.5f);
        });
        event.registerEntityRenderer(OBSIDIAN_ELEMENTAL, (context) -> {
            ModelPart modelPart = context.bakeLayer(ModelLayers.ZOMBIE);
            return new ObsidianElementalEntityRenderer(context, new BaseEarthElementalEntityModel<>(modelPart), 0.5f);
        });
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> lines = event.getToolTip();

        if (stack.getItem() == Items.LAPIS_LAZULI) {
            lines.add(Component.literal("25XP").withStyle(ChatFormatting.DARK_GRAY));
        }
        if (stack.getItem() == Items.QUARTZ) {
            lines.add(Component.literal("50XP").withStyle(ChatFormatting.DARK_GRAY));
        }
        if (stack.getItem() == Items.DIAMOND) {
            lines.add(Component.literal("500XP").withStyle(ChatFormatting.DARK_GRAY));
        }
        if (stack.getItem() == Items.EMERALD) {
            lines.add(Component.literal("250XP").withStyle(ChatFormatting.DARK_GRAY));
        }
        if (stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
            lines.add(Component.literal("Regeneration II（00:40）").withStyle(ChatFormatting.BLUE));
            lines.add(Component.literal("Resistance II（00:40）").withStyle(ChatFormatting.BLUE));
            lines.add(Component.literal("Fire Resistance（00:40）").withStyle(ChatFormatting.BLUE));
        }
        if (stack.getItem() == Items.GOLDEN_APPLE) {
            lines.add(Component.literal("Regeneration I（00:20）").withStyle(ChatFormatting.BLUE));
        }
        if (stack.getItem() == ArmorItems.MITHRIL_CHEST_PLATE.get()) {
            lines.add(Component.literal("Regeneration: Doubles the natural health recovery rate")
                    .withStyle(ChatFormatting.BLUE));
        }
    }
}