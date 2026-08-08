package com.equilibrium.item.vanilla_modify;

import com.equilibrium.OnServerInitialize;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.lang.reflect.Field;
import java.util.Map;

import static com.equilibrium.item.vanilla_modify.ItemMaxStackSize.ITEM_MAX_STACK_SIZE;
import static com.equilibrium.item.vanilla_modify.ItemMaxStackSize.itemMaxStackSizeInit;

@EventBusSubscriber(modid = OnServerInitialize.MOD_ID)
public class MaxStackSizeModifier {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            itemMaxStackSizeInit();

            for (Map.Entry<Item, Integer> entry : ITEM_MAX_STACK_SIZE.entrySet()) {
                Item item = entry.getKey();
                int maxStackSize = entry.getValue();
                if (item.getDefaultMaxStackSize() >= 8) {
                    setMaxStackSize(item, maxStackSize);
                }
            }

            setMaxStackSize(Items.TORCH, 32);
            setMaxStackSize(Items.WHEAT_SEEDS, 64);
            setMaxStackSize(Items.NETHER_WART, 32);
            setMaxStackSize(Items.MELON_SEEDS, 64);
            setMaxStackSize(Items.PUMPKIN_SEEDS, 64);
            setMaxStackSize(Items.DANDELION, 32);
        });
    }

    private static void setMaxStackSize(Item item, int newSize) {
        try {
            Field field = Item.class.getDeclaredField("maxStackSize");
            field.setAccessible(true);
            field.setInt(item, newSize);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}