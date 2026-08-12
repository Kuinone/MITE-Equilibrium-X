package com.equilibrium.server_and_client.server.event;

import com.equilibrium.item.material.MaterialItems;
import com.equilibrium.tags.ModBlockTags;
import com.equilibrium.util.BlockToItemConverter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Random;

import static com.equilibrium.block.reference.BlocksHardnessList.BLOCKS_HARDNESS_HASHMAP;
import static com.equilibrium.block.reference.BlocksHardnessList.getStandardBlockName;

// 移除 @EventBusSubscriber，改为手动注册实例
public class BreakBlockEvent {

    private final BlockToItemConverter blockToItemConverter;  // 改为实例字段
    private static int guarantee = 0;

    public BreakBlockEvent() {
        // 在构造函数中初始化，此时注册表已完全绑定
        this.blockToItemConverter = new BlockToItemConverter();
    }

    // 移除 static，改为实例方法
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player.isCreative()) return;

        Level world = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        ItemStack itemStack = player.getMainHandItem();
        // 工具耐久消耗
        itemStack.hurtAndBreak(
                BLOCKS_HARDNESS_HASHMAP.getOrDefault(getStandardBlockName(state.getBlock()), 0),
                player,
                EquipmentSlot.MAINHAND
        );

        // 仅处理树叶、沙砾、矿石
        if (!(state.is(BlockTags.LEAVES) ||
                state.getBlock() == Blocks.GRAVEL ||
                state.is(ModBlockTags.ORE))) {
            return;
        }

        Random random = new Random();

        var enchantmentLookup = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        int fortuneLevel = itemStack.getEnchantmentLevel(enchantmentLookup.getOrThrow(Enchantments.FORTUNE));
        int silkTouchLevel = itemStack.getEnchantmentLevel(enchantmentLookup.getOrThrow(Enchantments.SILK_TOUCH));

        // ---- 树叶 ----
        if (state.is(BlockTags.LEAVES)) {
            int chance = random.nextInt(100 - fortuneLevel * 30);
            if (chance <= 10) {
                world.addFreshEntity(new ItemEntity(
                        world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                        new ItemStack(Items.STICK)
                ));
            }
        }

        // ---- 沙砾 ----
        if (state.getBlock() == Blocks.GRAVEL) {
            if (silkTouchLevel == 1) {
                world.addFreshEntity(new ItemEntity(
                        world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                        new ItemStack(Items.GRAVEL)
                ));
                return;
            }

            int gravelDropChance = 75 - fortuneLevel * 15;
            if (random.nextInt(100) < gravelDropChance && guarantee < 12) {
                guarantee++;
                world.addFreshEntity(new ItemEntity(
                        world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                        new ItemStack(Blocks.GRAVEL)
                ));
                return;
            } else {
                guarantee = 0;
            }

            int extra = random.nextInt(1000);
            ItemStack dropStack;
            if (extra == 0) {
                dropStack = new ItemStack(Items.REDSTONE);
            } else if (extra <= 100) {
                dropStack = new ItemStack(MaterialItems.SILVER_NUGGET.get());
            } else if (extra <= 240) {
                dropStack = new ItemStack(Items.FLINT);
            } else if (extra <= 400) {
                dropStack = new ItemStack(MaterialItems.COPPER_NUGGET.get());
            } else {
                dropStack = new ItemStack(MaterialItems.FLINT.get());
            }
            world.addFreshEntity(new ItemEntity(
                    world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                    dropStack
            ));
        }

        // ---- 矿石 ----
        if (state.is(ModBlockTags.ORE)) {
            if (silkTouchLevel == 1) {
                Item item = state.getBlock().asItem();
                world.addFreshEntity(new ItemEntity(
                        world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                        new ItemStack(item)
                ));
                return;
            }

            int dropCount = 1;
            Item dropItem = blockToItemConverter.convertBlockToItem(state.getBlock());

            if (dropItem == Items.LAPIS_LAZULI ||
                    dropItem == Items.REDSTONE ||
                    dropItem == Items.GOLD_NUGGET) {
                dropCount = 4 + random.nextInt(4);
            }

            if (random.nextInt(10) >= (10 - fortuneLevel)) {
                dropCount *= 2;
            }

            for (int i = 0; i < dropCount; i++) {
                world.addFreshEntity(new ItemEntity(
                        world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                        new ItemStack(dropItem)
                ));
            }
        }
    }
}