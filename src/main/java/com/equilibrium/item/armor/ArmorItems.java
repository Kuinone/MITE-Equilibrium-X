package com.equilibrium.item.armor;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.equilibrium.OnServerInitialize.MOD_ID;

public class ArmorItems {

    // 使用 DeferredRegister.Items 创建物品注册器（专门用于 Item）
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    // 铜质护甲
    public static final DeferredItem<ArmorItem> COPPER_HELMET = ITEMS.register("copper_helmet",
            () -> new ArmorItem(ArmorMaterials.COPPER, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(5*8)));

    public static final DeferredItem<ArmorItem> COPPER_CHEST_PLATE = ITEMS.register("copper_chest_plate",
            () -> new ArmorItem(ArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8*8)));

    public static final DeferredItem<ArmorItem> COPPER_LEGGINGS = ITEMS.register("copper_leggings",
            () -> new ArmorItem(ArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7*8)));

    public static final DeferredItem<ArmorItem> COPPER_BOOTS = ITEMS.register("copper_boots",
            () -> new ArmorItem(ArmorMaterials.COPPER, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(4*8)));

    public static final DeferredItem<ArmorItem> COPPER_CHAINMAIL_HELMET = ITEMS.register("copper_chainmail_helmet",
            () -> new ArmorItem(ArmorMaterials.COPPER_CHAIN, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(5*4)));

    public static final DeferredItem<ArmorItem> COPPER_CHAINMAIL_CHEST_PLATE = ITEMS.register("copper_chainmail_chest_plate",
            () -> new ArmorItem(ArmorMaterials.COPPER_CHAIN, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8*4)));

    public static final DeferredItem<ArmorItem> COPPER_CHAINMAIL_LEGGINGS = ITEMS.register("copper_chainmail_leggings",
            () -> new ArmorItem(ArmorMaterials.COPPER_CHAIN, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7*4)));

    public static final DeferredItem<ArmorItem> COPPER_CHAINMAIL_BOOTS = ITEMS.register("copper_chainmail_boots",
            () -> new ArmorItem(ArmorMaterials.COPPER_CHAIN, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(4*4)));

    public static final DeferredItem<ArmorItem> SILVER_HELMET = ITEMS.register("silver_helmet",
            () -> new ArmorItem(ArmorMaterials.SILVER, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(5*8)));

    public static final DeferredItem<ArmorItem> SILVER_CHEST_PLATE = ITEMS.register("silver_chest_plate",
            () -> new ArmorItem(ArmorMaterials.SILVER, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8*8)));

    public static final DeferredItem<ArmorItem> SILVER_LEGGINGS = ITEMS.register("silver_leggings",
            () -> new ArmorItem(ArmorMaterials.SILVER, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7*8)));

    public static final DeferredItem<ArmorItem> SILVER_BOOTS = ITEMS.register("silver_boots",
            () -> new ArmorItem(ArmorMaterials.SILVER, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(4*8)));

    public static final DeferredItem<ArmorItem> SILVER_CHAINMAIL_HELMET = ITEMS.register("silver_chainmail_helmet",
            () -> new ArmorItem(ArmorMaterials.SILVER_CHAIN, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(5*4)));

    public static final DeferredItem<ArmorItem> SILVER_CHAINMAIL_CHEST_PLATE = ITEMS.register("silver_chainmail_chest_plate",
            () -> new ArmorItem(ArmorMaterials.SILVER_CHAIN, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8*4)));

    public static final DeferredItem<ArmorItem> SILVER_CHAINMAIL_LEGGINGS = ITEMS.register("silver_chainmail_leggings",
            () -> new ArmorItem(ArmorMaterials.SILVER_CHAIN, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7*4)));

    public static final DeferredItem<ArmorItem> SILVER_CHAINMAIL_BOOTS = ITEMS.register("silver_chainmail_boots",
            () -> new ArmorItem(ArmorMaterials.SILVER_CHAIN, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(4*4)));

    // 秘银护甲
    public static final DeferredItem<ArmorItem> MITHRIL_HELMET = ITEMS.register("mithril_helmet",
            () -> new ArmorItem(ArmorMaterials.MITHRIL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(5*128)));

    public static final DeferredItem<ArmorItem> MITHRIL_CHEST_PLATE = ITEMS.register("mithril_chest_plate",
            () -> new ArmorItem(ArmorMaterials.MITHRIL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8 * 128)));

    public static final DeferredItem<ArmorItem> MITHRIL_LEGGINGS = ITEMS.register("mithril_leggings",
            () -> new ArmorItem(ArmorMaterials.MITHRIL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7 * 128)));

    public static final DeferredItem<ArmorItem> MITHRIL_BOOTS = ITEMS.register("mithril_boots",
            () -> new ArmorItem(ArmorMaterials.MITHRIL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(4 * 128)));

    public static final DeferredItem<ArmorItem> MITHRIL_CHAINMAIL_HELMET = ITEMS.register("mithril_chainmail_helmet",
            () -> new ArmorItem(ArmorMaterials.MITHRIL_CHAIN, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(5*64)));

    public static final DeferredItem<ArmorItem> MITHRIL_CHAINMAIL_CHEST_PLATE = ITEMS.register("mithril_chainmail_chest_plate",
            () -> new ArmorItem(ArmorMaterials.MITHRIL_CHAIN, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8*64)));

    public static final DeferredItem<ArmorItem> MITHRIL_CHAINMAIL_LEGGINGS = ITEMS.register("mithril_chainmail_leggings",
            () -> new ArmorItem(ArmorMaterials.MITHRIL_CHAIN, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7*64)));

    public static final DeferredItem<ArmorItem> MITHRIL_CHAINMAIL_BOOTS = ITEMS.register("mithril_chainmail_boots",
            () -> new ArmorItem(ArmorMaterials.MITHRIL_CHAIN, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(4*64)));

    public static final DeferredItem<ArmorItem> ANCIENT_METAL_HELMET = ITEMS.register("ancient_metal_helmet",
            () -> new ArmorItem(ArmorMaterials.ANCIENT_METAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(5 * 32)));

    public static final DeferredItem<ArmorItem> ANCIENT_METAL_CHEST_PLATE = ITEMS.register("ancient_metal_chest_plate",
            () -> new ArmorItem(ArmorMaterials.ANCIENT_METAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8 * 32)));

    public static final DeferredItem<ArmorItem> ANCIENT_METAL_LEGGINGS = ITEMS.register("ancient_metal_leggings",
            () -> new ArmorItem(ArmorMaterials.ANCIENT_METAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7 * 32)));

    public static final DeferredItem<ArmorItem> ANCIENT_METAL_BOOTS = ITEMS.register("ancient_metal_boots",
            () -> new ArmorItem(ArmorMaterials.ANCIENT_METAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(4 * 32)));
    // 远古金属链甲
    public static final DeferredItem<ArmorItem> ANCIENT_METAL_CHAINMAIL_HELMET = ITEMS.register("ancient_metal_chainmail_helmet",
            () -> new ArmorItem(ArmorMaterials.ANCIENT_METAL_CHAIN, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(5 * 16)));

    public static final DeferredItem<ArmorItem> ANCIENT_METAL_CHAINMAIL_CHEST_PLATE = ITEMS.register("ancient_metal_chainmail_chest_plate",
            () -> new ArmorItem(ArmorMaterials.ANCIENT_METAL_CHAIN, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8 * 16)));

    public static final DeferredItem<ArmorItem> ANCIENT_METAL_CHAINMAIL_LEGGINGS = ITEMS.register("ancient_metal_chainmail_leggings",
            () -> new ArmorItem(ArmorMaterials.ANCIENT_METAL_CHAIN, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7 * 16)));

    public static final DeferredItem<ArmorItem> ANCIENT_METAL_CHAINMAIL_BOOTS = ITEMS.register("ancient_metal_chainmail_boots",
            () -> new ArmorItem(ArmorMaterials.ANCIENT_METAL_CHAIN, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(4 * 16)));

    public static final DeferredItem<ArmorItem> ADAMANTIUM_HELMET = ITEMS.register("adamantium_helmet",
            () -> new ArmorItem(ArmorMaterials.ADAMANTIUM, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(5*512)));

    public static final DeferredItem<ArmorItem> ADAMANTIUM_CHEST_PLATE = ITEMS.register("adamantium_chest_plate",
            () -> new ArmorItem(ArmorMaterials.ADAMANTIUM, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8*512)));

    public static final DeferredItem<ArmorItem> ADAMANTIUM_LEGGINGS = ITEMS.register("adamantium_leggings",
            () -> new ArmorItem(ArmorMaterials.ADAMANTIUM, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7*512)));

    public static final DeferredItem<ArmorItem> ADAMANTIUM_BOOTS = ITEMS.register("adamantium_boots",
            () -> new ArmorItem(ArmorMaterials.ADAMANTIUM, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(4*512)));

    public static final DeferredItem<ArmorItem> ADAMANTIUM_CHAINMAIL_HELMET = ITEMS.register("adamantium_chainmail_helmet",
            () -> new ArmorItem(ArmorMaterials.ADAMANTIUM_CHAIN, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(5*256)));

    public static final DeferredItem<ArmorItem> ADAMANTIUM_CHAINMAIL_CHEST_PLATE = ITEMS.register("adamantium_chainmail_chest_plate",
            () -> new ArmorItem(ArmorMaterials.ADAMANTIUM_CHAIN, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(8*256)));

    public static final DeferredItem<ArmorItem> ADAMANTIUM_CHAINMAIL_LEGGINGS = ITEMS.register("adamantium_chainmail_leggings",
            () -> new ArmorItem(ArmorMaterials.ADAMANTIUM_CHAIN, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(7*256)));

    public static final DeferredItem<ArmorItem> ADAMANTIUM_CHAINMAIL_BOOTS = ITEMS.register("adamantium_chainmail_boots",
            () -> new ArmorItem(ArmorMaterials.ADAMANTIUM_CHAIN, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(4 * 256)));
}