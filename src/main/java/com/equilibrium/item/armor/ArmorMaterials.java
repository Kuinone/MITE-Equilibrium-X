package com.equilibrium.item.armor;

import com.equilibrium.OnServerInitialize;
import com.equilibrium.item.material.MaterialItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, OnServerInitialize.MOD_ID);

    // ---- 普通盔甲 ----
    public static final Holder<ArmorMaterial> COPPER = register(
            "copper",
            mapOf(3, 5, 4, 2),  // 顺序：头盔, 胸甲, 护腿, 靴子
            16,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            0.0f, 0.0f,
            () -> Ingredient.of(MaterialItems.COPPER_NUGGET.get())
    );

    public static final Holder<ArmorMaterial> SILVER = register(
            "silver",
            mapOf(3, 5, 4, 2),
            16,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            0.0f, 0.0f,
            () -> Ingredient.of(MaterialItems.SILVER_NUGGET.get())
    );

    public static final Holder<ArmorMaterial> ANCIENT_METAL = register(
            "ancient_metal",
            mapOf(3, 5, 5, 3),
            16,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            0.0f, 0.0f,
            () -> Ingredient.of(MaterialItems.ANCIENT_METAL_NUGGET.get())
    );

    public static final Holder<ArmorMaterial> MITHRIL = register(
            "mithril",
            mapOf(4, 6, 5, 3),
            24,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            2.0f, 0.0f,
            () -> Ingredient.of(MaterialItems.MITHRIL_NUGGET.get())
    );

    public static final Holder<ArmorMaterial> ADAMANTIUM = register(
            "adamantium",
            mapOf(4, 7, 6, 3),
            24,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            3.0f, 0.0f,
            () -> Ingredient.of(MaterialItems.ADAMANTIUM_NUGGET.get())
    );

    // ---- 链甲（轻量化） ----
    public static final Holder<ArmorMaterial> COPPER_CHAIN = register(
            "copper_chain",
            mapOf(2, 3, 3, 2),
            16,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            0.0f, 0.0f,
            () -> Ingredient.of(MaterialItems.COPPER_NUGGET.get())
    );

    public static final Holder<ArmorMaterial> SILVER_CHAIN = register(
            "silver_chain",
            mapOf(2, 3, 3, 2),
            16,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            0.0f, 0.0f,
            () -> Ingredient.of(MaterialItems.SILVER_NUGGET.get())
    );

    public static final Holder<ArmorMaterial> ANCIENT_METAL_CHAIN = register(
            "ancient_metal_chain",
            mapOf(3, 4, 4, 2),
            24,
            SoundEvents.ARMOR_EQUIP_CHAIN,
            0.0f, 0.0f,
            () -> Ingredient.of(MaterialItems.ANCIENT_METAL_NUGGET.get())
    );

    public static final Holder<ArmorMaterial> MITHRIL_CHAIN = register(
            "mithril_chain",
            mapOf(3, 5, 4, 2),
            24,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            2.0f, 0.0f,
            () -> Ingredient.of(MaterialItems.MITHRIL_NUGGET.get())
    );

    public static final Holder<ArmorMaterial> ADAMANTIUM_CHAIN = register(
            "adamantium_chain",
            mapOf(3, 5, 5, 3),
            24,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            3.0f, 0.0f,
            () -> Ingredient.of(MaterialItems.ADAMANTIUM_NUGGET.get())
    );

    // ---------- 辅助方法 ----------
    /**
     * 创建一个防御值映射，参数顺序为：头盔, 胸甲, 护腿, 靴子（从头到脚）
     */
    private static EnumMap<ArmorItem.Type, Integer> mapOf(int helmet, int chestplate, int leggings, int boots) {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.HELMET, helmet);
        map.put(ArmorItem.Type.CHESTPLATE, chestplate);
        map.put(ArmorItem.Type.LEGGINGS, leggings);
        map.put(ArmorItem.Type.BOOTS, boots);
        map.put(ArmorItem.Type.BODY, 0); // 1.21 新增的 BODY 槽，暂不使用
        return map;
    }

    private static Holder<ArmorMaterial> register(
            String name,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            Holder<net.minecraft.sounds.SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        ResourceLocation textureLoc = ResourceLocation.fromNamespaceAndPath(OnServerInitialize.MOD_ID, name);
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(textureLoc));

        return ARMOR_MATERIALS.register(name, () -> new ArmorMaterial(
                defense,
                enchantability,
                equipSound,
                repairIngredient,
                layers,
                toughness,
                knockbackResistance
        ));
    }
}