package com.equilibrium.mixin.vanilla_blocksmixin;

import com.equilibrium.network.S2CStockChangeGrassColorPacket;
import com.equilibrium.tags.ModEntityTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.lighting.LightEngine;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.equilibrium.OnServerInitialize.*;
import static com.equilibrium.network.S2CStockChangeGrassColorPacket.BLOCK_POS_INTEGER_CONCURRENT_HASH_MAP;
import static com.equilibrium.server_and_client.server.moonphase_tasks.WorldMoonPhasesSelector.calculateMoonType;

@Mixin(GrassBlock.class)
public abstract class GrassBlockMixin extends SpreadingSnowyDirtBlock implements BonemealableBlock {

    public GrassBlockMixin(Properties settings) {
        super(settings);
    }

    @Unique
    private static boolean canBeGrass(BlockState state, LevelReader world, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.is(Blocks.SNOW) && (Integer) blockState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockState.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(world, state, pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(world, blockPos));
            return i < world.getMaxLightLevel();
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (!canBeGrass(state, world, pos)) {
            world.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
            return;
        }
        super.randomTick(state, world, pos, random);

        int polluteLevel = world.getBlockState(pos).getValue(GRASSBLOCK_POLLUTED);
        if (world.getRandom().nextInt(64) == 0 && polluteLevel >= 1) {
            int finalPolluteLevel = Math.clamp(polluteLevel - 1, 0, 7);
            world.setBlock(pos, state.setValue(GRASSBLOCK_POLLUTED, finalPolluteLevel), Block.UPDATE_ALL);
            BLOCK_POS_INTEGER_CONCURRENT_HASH_MAP.put(pos, finalPolluteLevel);
            ServerToClientUpdateGrassBlockState(world, pos, finalPolluteLevel);
        }

        if (world.getRandom().nextInt(8) == 0) {
            BLOCK_POS_INTEGER_CONCURRENT_HASH_MAP.forEach((blockPos, mapPolluteLevel) -> {
                if (mapPolluteLevel == 0)
                    BLOCK_POS_INTEGER_CONCURRENT_HASH_MAP.remove(blockPos);
            });
            ServerToClientUpdateGrassBlockState(world, pos, polluteLevel);
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        super.onRemove(state, world, pos, newState, moved);
        BLOCK_POS_INTEGER_CONCURRENT_HASH_MAP.remove(pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(GRASSBLOCK_POLLUTED);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void GrassBlock(Properties settings, CallbackInfo ci) {
        this.stateDefinition.any().setValue(GRASSBLOCK_POLLUTED, 0);
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(world, pos, state, entity);
        if (!this.defaultBlockState().hasProperty(GRASSBLOCK_POLLUTED))
            return;
        boolean isStock = entity.getType().is(ModEntityTags.STOCKS);
        boolean b1 = (calculateMoonType(world).equals("bloodMoon"));
        boolean b2 = world.canSeeSky(pos.above());
        boolean b3 = entity.getType().is(ModEntityTags.STOCKS);
        if (isStock && entity.getRandom().nextInt(128) == 0 && b1 && b2 && b3) {
            if (world.isLoaded(pos.above()) && world.getBlockState(pos.above()).isAir())
                world.setBlockAndUpdate(pos.above(), Blocks.WITHER_ROSE.defaultBlockState());
        }
        if (isStock && entity.getRandom().nextInt(128) == 0) {
            int polluteLevel = state.getValue(GRASSBLOCK_POLLUTED);
            world.setBlock(pos, state.setValue(GRASSBLOCK_POLLUTED, Math.clamp(polluteLevel + 1, 0, 7)), Block.UPDATE_ALL);
            ServerToClientUpdateGrassBlockState(world, pos, polluteLevel);
        }
    }

    @Unique
    private static void ServerToClientUpdateGrassBlockState(Level world, BlockPos pos, int polluteLevel) {if (!world.isClientSide()) {
            if (world.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 128, false) instanceof ServerPlayer player) {
                // NeoForge 网络发送
                PacketDistributor.sendToPlayer(
                        player,
                        new S2CStockChangeGrassColorPacket.GrassColorPayload(pos, polluteLevel)
                );
            }
        }
    }
}