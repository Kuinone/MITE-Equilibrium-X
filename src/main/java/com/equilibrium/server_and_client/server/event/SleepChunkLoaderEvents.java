package com.equilibrium.server_and_client.server.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.CanPlayerSleepEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.equilibrium.OnServerInitialize.MOD_ID;

@EventBusSubscriber(modid = MOD_ID)
public class SleepChunkLoaderEvents {

    public static final int RADIUS = 8;

    private static final TicketType<BlockPos> SLEEP_TICKET = TicketType.create(
            MOD_ID + "sleep_ticket", (a, b) -> 0
    );

    public static final ConcurrentHashMap<UUID, BlockPos> mapForEachPlayerSleepPos = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<UUID, Set<ChunkPos>> mapForEachPlayerDemandToLoadChunks = new ConcurrentHashMap<>();
    public static Set<ChunkPos> allPlayersDemandToLoadChunks = Collections.synchronizedSet(new HashSet<>());

    // ============ 玩家睡觉事件 ============
    @SubscribeEvent
    public static void onPlayerSleep(CanPlayerSleepEvent event) {
        // event.getPlayer() 返回 Player，需强转为 ServerPlayer（服务端事件）
        ServerPlayer player = event.getEntity();
        BlockPos bedPos = event.getPos();

        // 若已有记录，先清除
        if (mapForEachPlayerSleepPos.containsKey(player.getUUID())) {
            chunksOutOfTheWorld(mapForEachPlayerSleepPos.get(player.getUUID()), player);
            mapForEachPlayerSleepPos.remove(player.getUUID());
            mapForEachPlayerDemandToLoadChunks.remove(player.getUUID());
        }

        chunksJoinInTheWorld(bedPos, player);
        player.displayClientMessage(Component.literal("睡眠区域已加载"), false);

        mapForEachPlayerSleepPos.put(player.getUUID(), bedPos);
        Set<ChunkPos> chunkPosSet = getLoadChunkSet(bedPos, RADIUS);
        mapForEachPlayerDemandToLoadChunks.put(player.getUUID(), chunkPosSet);
        allPlayersDemandToLoadChunks = mergeAllPlayerChunkSets(mapForEachPlayerDemandToLoadChunks);
    }

    // ============ 玩家退出事件 ============
    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID uuid = player.getUUID();

        if (mapForEachPlayerSleepPos.containsKey(uuid)) {
            chunksOutOfTheWorld(mapForEachPlayerSleepPos.get(uuid), player);
        }
        mapForEachPlayerSleepPos.remove(uuid);
        mapForEachPlayerDemandToLoadChunks.remove(uuid);
        allPlayersDemandToLoadChunks = mergeAllPlayerChunkSets(mapForEachPlayerDemandToLoadChunks);
    }

    // ============ 区块加载辅助 ============
    private static void chunksJoinInTheWorld(BlockPos bedPos, ServerPlayer player) {
        // player.serverLevel() 返回 ServerLevel，其 getChunkSource() 返回 ServerChunkCache
        ServerChunkCache manager = player.serverLevel().getChunkSource();
        manager.addRegionTicket(
                SLEEP_TICKET,
                new ChunkPos(bedPos),
                RADIUS,
                bedPos
        );
    }

    private static void chunksOutOfTheWorld(BlockPos bedPos, ServerPlayer player) {
        ServerChunkCache manager = player.serverLevel().getChunkSource();
        manager.removeRegionTicket(
                SLEEP_TICKET,
                new ChunkPos(bedPos),
                RADIUS,
                bedPos
        );
    }

    public static Set<ChunkPos> getLoadChunkSet(BlockPos centerBlockPos, int radius) {
        ChunkPos centerChunk = new ChunkPos(centerBlockPos);
        Set<ChunkPos> actualLoadChunks = new HashSet<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                actualLoadChunks.add(new ChunkPos(centerChunk.x + dx, centerChunk.z + dz));
            }
        }
        return actualLoadChunks;
    }

    public static Set<ChunkPos> mergeAllPlayerChunkSets(ConcurrentHashMap<UUID, Set<ChunkPos>> shouldTickTheseChunk) {
        return shouldTickTheseChunk.values().stream()
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}