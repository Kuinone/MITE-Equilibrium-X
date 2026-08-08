package com.equilibrium.network;

import com.equilibrium.OnServerInitialize;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;  // ← 正确的导入
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.equilibrium.OnServerInitialize.MOD_ID;


@EventBusSubscriber(modid = OnServerInitialize.MOD_ID)
public class C2SClickTimesPacket {

    public static final ResourceLocation CLICK_TIMES_PAYLOAD_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "right_click_times");
    public static final Map<UUID, Integer> playerClickTimes = new ConcurrentHashMap<>();

    // ==================== 注册处理器 ====================
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");  // 版本号，我不到啊
        registrar.playToServer(
                ClickTimesPayload.TYPE,
                ClickTimesPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        Player player = context.player();
                        if (player instanceof ServerPlayer serverPlayer) {
                            playerClickTimes.put(serverPlayer.getUUID(), payload.times());
                        }
                    });
                }
        );
    }

    // ==================== Payload 定义 ====================
    public record ClickTimesPayload(int times) implements CustomPacketPayload {
        public static final Type<ClickTimesPayload> TYPE =
                new Type<>(CLICK_TIMES_PAYLOAD_ID);

        public static final StreamCodec<FriendlyByteBuf, ClickTimesPayload> STREAM_CODEC =
                StreamCodec.ofMember(
                        (payload, buf) -> buf.writeVarInt(payload.times()),
                        buf -> new ClickTimesPayload(buf.readVarInt())
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    // ==================== 客户端发送 ====================
    public static void sendClickTimes(int times) {
        PacketDistributor.sendToServer(new ClickTimesPayload(times));
    }

    // ==================== 服务端数据操作 ====================
    public static int getClickTimes(Player player) {
        return playerClickTimes.getOrDefault(player.getUUID(), 0);
    }

    public static void removePlayerData(Player player) {
        playerClickTimes.remove(player.getUUID());
    }

    public static void setClickTimes(Player player, int times) {
        playerClickTimes.put(player.getUUID(), times);
    } //哦不不不这方法咋没用啊
}