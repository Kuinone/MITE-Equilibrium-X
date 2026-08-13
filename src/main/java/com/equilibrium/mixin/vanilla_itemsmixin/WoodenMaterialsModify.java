package com.equilibrium.mixin.vanilla_itemsmixin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Supplier;

@Mixin(Tiers.class)
public class WoodenMaterialsModify {

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/Tiers;<init>(Ljava/lang/String;ILnet/minecraft/tags/TagKey;IFFILjava/util/function/Supplier;)V",
                    ordinal = 0   // 假设 WOOD 是第 3 个枚举常量（索引 2），请根据实际情况调整
            ),
            index = 4         // 修改第 5 个参数，即 speed（浮点数）
    )
    private static float modifyWoodParameters(float speed) {
        return 0.5F;      // 将木质工具挖掘速度改为 0.5
    }
}