package com.equilibrium.network;

import com.equilibrium.OnServerInitialize;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.equilibrium.OnServerInitialize.MOD_ID;
import static com.equilibrium.difficulty_entry.DifficultyEntryRegister.GET_ALL_ENTRY_KEY;

@Mod(value = OnServerInitialize.MOD_ID)
@EventBusSubscriber(modid = OnServerInitialize.MOD_ID)
public class S2CGameRuleSyncPayloadForBooleanPacket {

    public static final CustomPacketPayload.Type<S2CGameRuleSyncPayload> ID =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MOD_ID, "game_rule_sync"));

    // ==================== 注册（NeoForge 事件） ====================
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                S2CGameRuleSyncPayload.TYPE,
                S2CGameRuleSyncPayload.STREAM_CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        // 正确获取 ClientLevel
                        ClientLevel clientLevel = (ClientLevel) context.player().level();
                        if (clientLevel == null) return;

                        String ruleId = payload.rulesId;
                        if (!GET_ALL_ENTRY_KEY.containsKey(ruleId)) {
                            OnServerInitialize.LOGGER.error("This GameRule can not be changed");
                            return;
                        }

                        GameRules.Key<GameRules.BooleanValue> booleanRuleKey = GET_ALL_ENTRY_KEY.get(ruleId);
                        clientLevel.getGameRules().getRule(booleanRuleKey).set(payload.gameRuleBooleanValue, null);
                    });
                }
        );
    }

    // ==================== Payload 定义 ====================
    public static class S2CGameRuleSyncPayload implements CustomPacketPayload {
        public static final Type<S2CGameRuleSyncPayload> TYPE = ID;

        public final String rulesId;
        public final Boolean gameRuleBooleanValue;

        public S2CGameRuleSyncPayload(String rulesId, Boolean gameRuleValue) {
            this.rulesId = rulesId;
            this.gameRuleBooleanValue = gameRuleValue;
        }

        public static final StreamCodec<FriendlyByteBuf, S2CGameRuleSyncPayload> STREAM_CODEC =
                StreamCodec.ofMember(
                        (payload, buf) -> {
                            buf.writeUtf(payload.rulesId);
                            buf.writeBoolean(payload.gameRuleBooleanValue);
                        },
                        buf -> {
                            String rulesId = buf.readUtf();
                            Boolean value = buf.readBoolean();
                            return new S2CGameRuleSyncPayload(rulesId, value);
                        }
                );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    // ==================== 服务端发送方法 ====================
    public static void sendToPlayer(ServerPlayer player, S2CGameRuleSyncPayload payload) {
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