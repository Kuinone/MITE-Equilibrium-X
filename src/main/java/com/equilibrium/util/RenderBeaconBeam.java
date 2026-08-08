package com.equilibrium.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import static com.equilibrium.OnServerInitialize.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class RenderBeaconBeam {

    private static Vec3 beamPos = null;
    private static long startTick = 0;

    public static void show(Vec3 pos, long worldTime) {
        beamPos = pos;
        startTick = worldTime;
    }

    public static void RenderBeaconInit() {
        // 由事件总线自动注册，无需操作
    }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        if (beamPos == null) return;

        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;

        if (client.level.getDayTime() - startTick > 24000) {
            beamPos = null;
            return;
        }

        MultiBufferSource bufferSource = client.renderBuffers().bufferSource();
        renderBeam(event.getPoseStack(), bufferSource, event.getCamera().getPosition(), client);
    }

    private static void renderBeam(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPos, Minecraft client) {
        double x = beamPos.x + 0.5 - cameraPos.x;
        double y = beamPos.y + 1.0 - cameraPos.y;
        double z = beamPos.z + 0.5 - cameraPos.z;

        poseStack.pushPose();
        poseStack.translate(x, y, z);

        float tickDelta;
        try {
            tickDelta = client.getTimer().getGameTimeDeltaPartialTick(false);
        } catch (Exception e) {
            tickDelta = 1.0f;
        }

        BeaconRenderer.renderBeaconBeam(
                poseStack,
                bufferSource,
                BeaconRenderer.BEAM_LOCATION,
                tickDelta,
                1.0f,
                client.level.getGameTime(),
                0,
                1024,
                0xf9fffe,
                0.2f,
                0.25f
        );

        poseStack.popPose();
    }

    public static void hide() {
        beamPos = null;
    }
}