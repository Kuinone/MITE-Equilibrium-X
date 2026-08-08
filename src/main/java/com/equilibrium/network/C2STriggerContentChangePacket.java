package com.equilibrium.network;

import com.equilibrium.OnServerInitialize;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.equilibrium.OnServerInitialize.MOD_ID;

@EventBusSubscriber(modid = OnServerInitialize.MOD_ID)
public class C2STriggerContentChangePacket {

    public static final ResourceLocation TRIGGER_CONTENT_CHANGE_PACKET_ID =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "trigger_content_change");

    // ==================== 注册处理器 ====================
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                TriggerContentChangePayload.TYPE,
                TriggerContentChangePayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        ServerPlayer player = (ServerPlayer) context.player();
                        triggerContentChange(player);
                    });
                }
        );
    }

    // ==================== Payload 定义（record） ====================
    public record TriggerContentChangePayload() implements CustomPacketPayload {
        public static final Type<TriggerContentChangePayload> TYPE =
                new Type<>(TRIGGER_CONTENT_CHANGE_PACKET_ID);

        public static final StreamCodec<FriendlyByteBuf, TriggerContentChangePayload> STREAM_CODEC =
                StreamCodec.ofMember(
                        (payload, buf) -> { /* 无数据需要写入 */ },
                        buf -> new TriggerContentChangePayload()
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    // ==================== 服务端业务逻辑（保持不变） ====================
    private static void triggerContentChange(ServerPlayer player) {
        AbstractContainerMenu screenHandler = player.containerMenu;
        if (screenHandler instanceof CraftingMenu craftingHandler) {
            craftingHandler.slotsChanged(craftingHandler.craftSlots);
        }
        if (screenHandler instanceof InventoryMenu playerScreenHandler) {
            playerScreenHandler.slotsChanged(null);
        }
    }

    // ==================== 客户端发送方法 ====================
    public static void sendTrigger() {
        PacketDistributor.sendToServer(new TriggerContentChangePayload());
    }
}