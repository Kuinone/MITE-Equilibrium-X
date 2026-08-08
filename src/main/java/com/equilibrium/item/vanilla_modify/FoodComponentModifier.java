package com.equilibrium.item.vanilla_modify;

import com.equilibrium.OnServerInitialize;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.lang.reflect.Field;
import java.util.Map;

@EventBusSubscriber(modid = OnServerInitialize.MOD_ID)
public class FoodComponentModifier {

    public static final FoodProperties GOLDEN_APPLE_FOOD_COMPONENT = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(1.2F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION, 400, 0), 1.0F)
            .alwaysEdible()
            .build();

    public static final FoodProperties ENCHANTING_GOLDEN_APPLE_FOOD_COMPONENT = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(1.2F)
            .effect(new MobEffectInstance(MobEffects.REGENERATION, 800, 1), 1.0F)
            .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0), 1.0F)
            .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 800, 1), 1.0F)
            .alwaysEdible()
            .build();

    // 定义要修改的物品和对应的食物属性
    private static final Map<Item, FoodProperties> FOOD_MODIFICATIONS = Map.of(
            Items.GOLDEN_APPLE, GOLDEN_APPLE_FOOD_COMPONENT,
            Items.ENCHANTED_GOLDEN_APPLE, ENCHANTING_GOLDEN_APPLE_FOOD_COMPONENT,
            Items.BREAD, createFood(2, 8F),
            Items.PUMPKIN_PIE, createFood(10, 12F),
            Items.MELON_SLICE, createFood(1, 0F),
            Items.BAKED_POTATO, createFood(2, 6F),
            Items.WHEAT_SEEDS, createFood(0, 1F),
            Items.PUMPKIN_SEEDS, createFood(3, 3F),
            Items.MELON_SEEDS, createFood(0, 1F),
            Items.SUGAR, createFood(1, 1F)
    );

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        // 确保在同步工作队列中执行，避免并发问题
        event.enqueueWork(() -> {
            for (Map.Entry<Item, FoodProperties> entry : FOOD_MODIFICATIONS.entrySet()) {
                setFoodProperties(entry.getKey(), entry.getValue());
            }
        });
    }

    private static FoodProperties createFood(int nutrition, float saturation) {
        return new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation)
                .build();
    }

    /**
     * 通过反射设置物品的食物属性。
     * NeoForge 1.21.1 官方映射中字段名为 "foodProperties"。
     */
    private static void setFoodProperties(Item item, FoodProperties food) {
        try {
            Field field = Item.class.getDeclaredField("foodProperties");
            field.setAccessible(true);
            field.set(item, food);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // 建议使用日志记录，此处简单打印堆栈
            e.printStackTrace();
        }
    }
}