package com.equilibrium.difficulty_entry;

import com.equilibrium.OnServerInitialize;
import com.equilibrium.network.S2CGameRuleSyncPayloadForBooleanPacket;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;

import static com.equilibrium.difficulty_entry.DifficultyEntryRegister.GET_ALL_ENTRY_KEY;

public class DifficultyEntryUtil {

    /**
     * 游戏规则变更回调（布尔型规则），向所有在线玩家同步单条规则。
     */
    public static void onGameRuleChangedForBoolean(MinecraftServer server, GameRules.BooleanValue booleanRule, String ruleId) {
        S2CGameRuleSyncPayloadForBooleanPacket.S2CGameRuleSyncPayload payload =
                new S2CGameRuleSyncPayloadForBooleanPacket.S2CGameRuleSyncPayload(ruleId, booleanRule.get());

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            PacketDistributor.sendToPlayer(player, payload);
        }

        OnServerInitialize.LOGGER.info("GameRule changed callback: " + ruleId);
    }

    /**
     * 玩家登录时同步所有布尔型游戏规则。
     */
    public static void onPlayerConnectSynchronizingGameRulesForBoolean(ServerPlayer serverPlayerEntity) {
        for (String ruleId : GET_ALL_ENTRY_KEY.keySet()) {
            boolean value = serverPlayerEntity.level()
                    .getGameRules()
                    .getRule(GET_ALL_ENTRY_KEY.get(ruleId))
                    .get();

            S2CGameRuleSyncPayloadForBooleanPacket.S2CGameRuleSyncPayload payload =
                    new S2CGameRuleSyncPayloadForBooleanPacket.S2CGameRuleSyncPayload(ruleId, value);

            PacketDistributor.sendToPlayer(serverPlayerEntity, payload);
        }

        OnServerInitialize.LOGGER.info("A player is connecting, synchronizing all game rules.");
    }
}