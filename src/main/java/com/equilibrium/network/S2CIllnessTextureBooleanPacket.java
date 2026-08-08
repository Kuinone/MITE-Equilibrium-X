package com.equilibrium.network;

import com.equilibrium.OnServerInitialize;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.equilibrium.OnServerInitialize.MOD_ID;

@Mod(value = OnServerInitialize.MOD_ID)
@EventBusSubscriber(modid = OnServerInitialize.MOD_ID)
public class S2CIllnessTextureBooleanPacket {

    public static final ResourceLocation ILLNESS_APPEARANCE_PAYLOAD_ID =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "illness_appearance");

    public static final Map<Integer, Boolean> SICK_ENTITY = new ConcurrentHashMap<>();

    // ==================== 注册（NeoForge 事件） ====================
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                IllnessAppearancePayload.TYPE,
                IllnessAppearancePayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        // 直接更新客户端静态缓存
                        if (payload.isIllness) {
                            SICK_ENTITY.put(payload.entityId, true);
                        } else {
                            SICK_ENTITY.remove(payload.entityId);
                        }
                    });
                }
        );
    }

    // ==================== Payload 定义（保持 class 结构以兼容原代码） ====================
    public static class IllnessAppearancePayload implements CustomPacketPayload {
        public static final Type<IllnessAppearancePayload> TYPE =
                new Type<>(ILLNESS_APPEARANCE_PAYLOAD_ID);

        public final int entityId;
        public final boolean isIllness;

        public IllnessAppearancePayload(int entityId, boolean isIllness) {
            this.entityId = entityId;
            this.isIllness = isIllness;
        }

        public static final StreamCodec<FriendlyByteBuf, IllnessAppearancePayload> STREAM_CODEC =
                StreamCodec.ofMember(
                        (payload, buf) -> {
                            buf.writeVarInt(payload.entityId);
                            buf.writeBoolean(payload.isIllness);
                        },
                        buf -> new IllnessAppearancePayload(buf.readVarInt(), buf.readBoolean())
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        // 客户端查询工具方法
        public static boolean isIllness(int entityId) {
            return SICK_ENTITY.getOrDefault(entityId, false);
        }
    }

    // ==================== 服务端发送方法 ====================
    public static void sendToPlayer(ServerPlayer player, IllnessAppearancePayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    @Deprecated
    public static void registerOnServer() {
        // 由事件总线自动注册，无需操作
    }

    @Deprecated
    public static void registerOnClient() {
        // 由事件总线自动注册，无需操作
    }
}