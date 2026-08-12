package com.equilibrium.server_and_client.server.event;

import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;

public class CraftingMetalPickAxeListener {

    @SubscribeEvent
    public void onCraftMetalPickAxe(CraftingMetalPickAxeEvent event) {
        // 获取事件上下文
        var player = event.getPlayer();
        var world = event.getWorld();

        // 你的业务逻辑，例如根据玩家状态决定是否允许合成或消耗经验
        if (player.isCreative()) {
            event.setResult(InteractionResult.SUCCESS); // 设置结果为成功
        } else if (player.experienceLevel < 10) {
            event.setResult(InteractionResult.FAIL);   // 设置结果为失败
        }
        // 默认不设置则保持 PASS
    }
}