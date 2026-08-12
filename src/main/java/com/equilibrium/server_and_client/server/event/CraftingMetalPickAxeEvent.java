package com.equilibrium.server_and_client.server.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;

/**
 * NeoForge 事件，用于在合成金属镐时触发。
 * 监听器可通过订阅此事件并调用 {@link #setResult(InteractionResult)} 返回处理结果。
 * 多个监听器按注册顺序依次执行，后执行的会覆盖之前设置的结果，最终结果由最后一个非 PASS 的值决定。
 *
 * <p>使用示例：
 * <pre>{@code
 * // 触发事件
 * CraftingMetalPickAxeEvent event = new CraftingMetalPickAxeEvent(world, player);
 * NeoForge.EVENT_BUS.post(event);
 * InteractionResult result = event.getResult();
 *
 * // 监听事件
 * @SubscribeEvent
 * public void onCraft(CraftingMetalPickAxeEvent event) {
 *     if (condition) {
 *         event.setResult(InteractionResult.SUCCESS);
 *     }
 * }
 * }</pre>
 */
public class CraftingMetalPickAxeEvent extends Event {
    private final Level world;
    private final Player player;
    private InteractionResult result = InteractionResult.PASS;

    public CraftingMetalPickAxeEvent(Level world, Player player) {
        this.world = world;
        this.player = player;
    }

    public Level getWorld() {
        return world;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * 获取当前事件处理结果，默认为 {@link InteractionResult#PASS}。
     */
    public InteractionResult getResult() {
        return result;
    }

    /**
     * 设置事件处理结果。
     * 当多个监听器修改此值时，最终值由最后设置的监听器决定。
     */
    public void setResult(InteractionResult result) {
        this.result = result;
    }
}