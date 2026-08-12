package com.equilibrium.structure;

import com.equilibrium.OnServerInitialize;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class StructureRegister {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, OnServerInitialize.MOD_ID);

    public static final ResourceLocation MOD_MONSTER_ROOM_ID =
            ResourceLocation.fromNamespaceAndPath(OnServerInitialize.MOD_ID, "monster_room");

    // 显式声明类型：Supplier<Feature<?>>，注意注册的是 ModDungeonFeature，它继承 Feature<NoneFeatureConfiguration>
    public static final Supplier<ModDungeonFeature> MOD_DUNGEON_FEATURE =
            FEATURES.register("monster_room", () -> new ModDungeonFeature(NoneFeatureConfiguration.CODEC));
}