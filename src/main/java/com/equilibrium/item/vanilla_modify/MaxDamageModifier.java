package com.equilibrium.item.vanilla_modify;

import com.equilibrium.OnServerInitialize;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.lang.reflect.Field;

@EventBusSubscriber(modid = OnServerInitialize.MOD_ID)
public class MaxDamageModifier {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            setMaxDamage(Items.FISHING_ROD, 16);
            setMaxDamage(Items.WOODEN_SHOVEL, 240);
        });
    }

    private static void setMaxDamage(net.minecraft.world.item.Item item, int newMaxDamage) {
        try {
            Field field = net.minecraft.world.item.Item.class.getDeclaredField("maxDamage");
            field.setAccessible(true);
            field.setInt(item, newMaxDamage);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}