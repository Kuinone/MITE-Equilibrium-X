package com.equilibrium.network;

import com.equilibrium.OnServerInitialize;
import net.minecraft.core.BlockPos;
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
public class S2CStockChangeGrassColorPacket {

    public static final ResourceLocation STOCK_CHANGE_GRASS_COLOR_PACKET_PACKET_ID =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "grass_color");

    public static Map<BlockPos, Integer> BLOCK_POS_INTEGER_CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    // ==================== 注册（NeoForge 事件） ====================
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                GrassColorPayload.TYPE,
                GrassColorPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        // 更新客户端缓存
                        BLOCK_POS_INTEGER_CONCURRENT_HASH_MAP.put(payload.pos, payload.polluteLevel);
                    });
                }
        );
    }

    // ==================== Payload 定义 ====================
    public static class GrassColorPayload implements CustomPacketPayload {
        public static final Type<GrassColorPayload> TYPE =
                new Type<>(STOCK_CHANGE_GRASS_COLOR_PACKET_PACKET_ID);

        public final BlockPos pos;
        public final int polluteLevel;

        public GrassColorPayload(BlockPos pos, int polluteLevel) {
            this.pos = pos;
            this.polluteLevel = polluteLevel;
        }

        public static final StreamCodec<FriendlyByteBuf, GrassColorPayload> STREAM_CODEC =
                StreamCodec.ofMember(
                        (payload, buf) -> {
                            buf.writeBlockPos(payload.pos);
                            buf.writeVarInt(payload.polluteLevel);
                        },
                        buf -> new GrassColorPayload(buf.readBlockPos(), buf.readVarInt())
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        // 客户端查询工具
        public static int getPolluteLevel(BlockPos pos) {
            return BLOCK_POS_INTEGER_CONCURRENT_HASH_MAP.getOrDefault(pos, 0);
        }
    }

    // ==================== 服务端发送方法 ====================
    public static void sendToPlayer(ServerPlayer player, GrassColorPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    // ==================== 兼容旧调用（可选） ====================
    @Deprecated
    public static void registerOnServer() {
        // 已自动注册
    }

    @Deprecated
    public static void registerOnClient() {
        // 已自动注册
    }
}