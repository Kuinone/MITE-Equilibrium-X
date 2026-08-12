package com.equilibrium.item.armor;

import com.equilibrium.OnServerInitialize;
import com.equilibrium.item.material.MaterialItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorMaterials {


    public static final Holder<ArmorMaterial> COPPER = register("copper", Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 1);
        map.put(ArmorItem.Type.LEGGINGS, 4);
        map.put(ArmorItem.Type.CHESTPLATE, 4);
        map.put(ArmorItem.Type.HELMET, 2);
        map.put(ArmorItem.Type.BODY, 4);
    }), 16, SoundEvents.ARMOR_EQUIP_CHAIN, 0F, 0.0F, () -> Ingredient.of(MaterialItems.COPPER_NUGGET.get()));

    public static final Holder<ArmorMaterial> SILVER = register("silver", Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 1);
        map.put(ArmorItem.Type.LEGGINGS, 4);
        map.put(ArmorItem.Type.CHESTPLATE, 4);
        map.put(ArmorItem.Type.HELMET, 2);
        map.put(ArmorItem.Type.BODY, 4);
    }), 16, SoundEvents.ARMOR_EQUIP_CHAIN, 0F, 0.0F, () -> Ingredient.of(MaterialItems.SILVER_NUGGET.get()));

    public static final Holder<ArmorMaterial> ANCIENT_METAL = register("ancient_metal", Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 8);
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.BODY, 11);
    }), 16, SoundEvents.ARMOR_EQUIP_CHAIN, 0F, 0.0F, () -> Ingredient.of(MaterialItems.ANCIENT_METAL_NUGGET.get()));

    public static final Holder<ArmorMaterial> MITHRIL = register("mithril", Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 8);
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.BODY, 11);
    }), 24, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> Ingredient.of(MaterialItems.MITHRIL_NUGGET.get()));

    public static final Holder<ArmorMaterial> ADAMANTIUM = register("adamantium", Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 6);
        map.put(ArmorItem.Type.LEGGINGS, 12);
        map.put(ArmorItem.Type.CHESTPLATE, 16);
        map.put(ArmorItem.Type.HELMET, 6);
        map.put(ArmorItem.Type.BODY, 22);
    }), 24, SoundEvents.ARMOR_EQUIP_DIAMOND, 4.0F, 0.0F, () -> Ingredient.of(MaterialItems.ADAMANTIUM_NUGGET.get()));

    public static final Holder<ArmorMaterial> COPPER_CHAIN = register("copper_chain", Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 1);
        map.put(ArmorItem.Type.LEGGINGS, 4);
        map.put(ArmorItem.Type.CHESTPLATE, 4);
        map.put(ArmorItem.Type.HELMET, 2);
        map.put(ArmorItem.Type.BODY, 4);
    }), 16, SoundEvents.ARMOR_EQUIP_CHAIN, 0F, 0.0F, () -> Ingredient.of(MaterialItems.COPPER_NUGGET.get()));

    public static final Holder<ArmorMaterial> SILVER_CHAIN = register("silver", Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 1);
        map.put(ArmorItem.Type.LEGGINGS, 4);
        map.put(ArmorItem.Type.CHESTPLATE, 4);
        map.put(ArmorItem.Type.HELMET, 2);
        map.put(ArmorItem.Type.BODY, 4);
    }), 16, SoundEvents.ARMOR_EQUIP_CHAIN, 0F, 0.0F, () -> Ingredient.of(MaterialItems.SILVER_NUGGET.get()));

    public static final Holder<ArmorMaterial> ANCIENT_METAL_CHAIN = register("ancient_metal_chainmail", Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 1);
        map.put(ArmorItem.Type.LEGGINGS, 3);
        map.put(ArmorItem.Type.CHESTPLATE, 4);
        map.put(ArmorItem.Type.HELMET, 2);
        map.put(ArmorItem.Type.BODY, 4);
    }), 24, SoundEvents.ARMOR_EQUIP_DIAMOND, 0F, 0F, () -> Ingredient.of());

    public static final Holder<ArmorMaterial> MITHRIL_CHAIN = register("mithril", Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 8);
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.BODY, 11);
    }), 24, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> Ingredient.of(MaterialItems.MITHRIL_NUGGET.get()));

    public static final Holder<ArmorMaterial> ADAMANTIUM_CHAIN = register("adamantium", Util.make(new EnumMap(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 6);
        map.put(ArmorItem.Type.LEGGINGS, 12);
        map.put(ArmorItem.Type.CHESTPLATE, 16);
        map.put(ArmorItem.Type.HELMET, 6);
        map.put(ArmorItem.Type.BODY, 22);
    }), 24, SoundEvents.ARMOR_EQUIP_DIAMOND, 4.0F, 0.0F, () -> Ingredient.of(MaterialItems.ADAMANTIUM_NUGGET.get()));

















    private static Holder<ArmorMaterial> register(
            String id,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(OnServerInitialize.MOD_ID,id)));
        return register(id, defense, enchantability, equipSound, toughness, knockbackResistance, repairIngredient, list);
    }

    private static Holder<ArmorMaterial> register(
            String id,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantability,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient,
            List<ArmorMaterial.Layer> layers
    ) {
        EnumMap<ArmorItem.Type, Integer> enumMap = new EnumMap(ArmorItem.Type.class);

        for (ArmorItem.Type type : ArmorItem.Type.values()) {
            enumMap.put(type, (Integer)defense.get(type));
        }

        return Registry.registerForHolder(
                BuiltInRegistries.ARMOR_MATERIAL,
                ResourceLocation.withDefaultNamespace(id),
                new ArmorMaterial(enumMap, enchantability, equipSound, repairIngredient, layers, toughness, knockbackResistance)
        );
    }
}
