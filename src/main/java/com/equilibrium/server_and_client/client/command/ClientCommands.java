package com.equilibrium.server_and_client.client.command;

import com.equilibrium.OnServerInitialize;
import com.equilibrium.entity.path_finder.AStarCanGoTo;
import com.equilibrium.entity.path_finder.AStarCanGoToAndReturn;
import com.equilibrium.entity.path_finder.AStarSimplePathfinder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

import java.util.List;

import static com.equilibrium.server_and_client.server.moonphase_tasks.MoonPhaseEvent.getMoonType;
import static com.equilibrium.server_and_client.server.event.UpdateArmorEvent.updatePlayerArmor;

@EventBusSubscriber(modid = OnServerInitialize.MOD_ID, value = Dist.CLIENT)
public class ClientCommands {

    @SubscribeEvent
    public static void registerClientCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        registerClientAllCommands(dispatcher);
    }

    private static void registerClientAllCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        // 1. moonType 命令
        dispatcher.register(Commands.literal("moonType")
                .executes(context -> {
                    ClientLevel world = Minecraft.getInstance().level;
                    if (world == null) {
                        context.getSource().sendFailure(Component.literal("无法获取当前世界。"));
                    } else {
                        String moon = getMoonType(world);
                        context.getSource().sendSuccess(() -> Component.literal("当前月相是: " + moon), false);
                    }
                    return 1;
                })
        );

        // 2. fastFlySpeed 命令（需要权限2）
        dispatcher.register(Commands.literal("fastFlySpeed")
                .then(Commands.argument("speed", FloatArgumentType.floatArg(0.1F, 0.5F))
                        .requires(source -> source.hasPermission(2)) // 权限检查
                        .executes(context -> {
                            float speed = FloatArgumentType.getFloat(context, "speed");
                            if (context.getSource().getEntity() instanceof LocalPlayer clientPlayerEntity) {
                                clientPlayerEntity.getAbilities().setFlyingSpeed(speed);
                                clientPlayerEntity.sendSystemMessage(Component.literal("飞行速度已设为: " + speed));
                            } else {
                                OnServerInitialize.LOGGER.error("此命令只能由客户端玩家执行");
                            }
                            return 1;
                        })
                )
        );

        // 3. day 命令
        dispatcher.register(Commands.literal("day")
                .executes(context -> {
                    ClientLevel world = Minecraft.getInstance().level;
                    if (world == null) {
                        context.getSource().sendFailure(Component.literal("无法获取当前世界。"));
                    } else {
                        long time = world.getDayTime();
                        int day = (int) (time / 24000L);
                        context.getSource().sendSuccess(() -> Component.literal("当前是第 " + day + " 天"), false);
                    }
                    return 1;
                })
        );

        // 4. protection 命令
        dispatcher.register(Commands.literal("protection")
                .executes(context -> {
                    Player player = context.getSource().getPlayer();
                    if (player == null) {
                        context.getSource().sendFailure(Component.literal("无法获取玩家"));
                        return 0;
                    }
                    Component text = updatePlayerArmor(player);
                    player.sendSystemMessage(text);
                    return 1;
                })
        );

        // 5. AStarFindPath 命令
        dispatcher.register(Commands.literal("AStarFindPath")
                .then(Commands.argument("x1", IntegerArgumentType.integer())
                        .then(Commands.argument("y1", IntegerArgumentType.integer())
                                .then(Commands.argument("z1", IntegerArgumentType.integer())
                                        .then(Commands.argument("x2", IntegerArgumentType.integer())
                                                .then(Commands.argument("y2", IntegerArgumentType.integer())
                                                        .then(Commands.argument("z2", IntegerArgumentType.integer())
                                                                .executes(context -> {
                                                                    int x1 = IntegerArgumentType.getInteger(context, "x1");
                                                                    int y1 = IntegerArgumentType.getInteger(context, "y1");
                                                                    int z1 = IntegerArgumentType.getInteger(context, "z1");
                                                                    int x2 = IntegerArgumentType.getInteger(context, "x2");
                                                                    int y2 = IntegerArgumentType.getInteger(context, "y2");
                                                                    int z2 = IntegerArgumentType.getInteger(context, "z2");
                                                                    BlockPos start = new BlockPos(x1, y1, z1);
                                                                    BlockPos goal = new BlockPos(x2, y2, z2);
                                                                    ClientLevel world = Minecraft.getInstance().level;
                                                                    if (world == null) {
                                                                        context.getSource().sendFailure(Component.literal("世界为空"));
                                                                        return 0;
                                                                    }
                                                                    List<BlockPos> path = AStarSimplePathfinder.findPath(world, start, goal);
                                                                    Player player = context.getSource().getPlayer();
                                                                    if (player != null) {
                                                                        player.sendSystemMessage(Component.literal(path != null ? "找到路径" : "没有找到路径"));
                                                                    }
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        // 6. AStarFindPathCanGoTo 命令 我的天哪
        dispatcher.register(Commands.literal("AStarFindPathCanGoTo")
                .then(Commands.argument("x1", IntegerArgumentType.integer())
                        .then(Commands.argument("y1", IntegerArgumentType.integer())
                                .then(Commands.argument("z1", IntegerArgumentType.integer())
                                        .then(Commands.argument("x2", IntegerArgumentType.integer())
                                                .then(Commands.argument("y2", IntegerArgumentType.integer())
                                                        .then(Commands.argument("z2", IntegerArgumentType.integer())
                                                                .executes(context -> {
                                                                    int x1 = IntegerArgumentType.getInteger(context, "x1");
                                                                    int y1 = IntegerArgumentType.getInteger(context, "y1");
                                                                    int z1 = IntegerArgumentType.getInteger(context, "z1");
                                                                    int x2 = IntegerArgumentType.getInteger(context, "x2");
                                                                    int y2 = IntegerArgumentType.getInteger(context, "y2");
                                                                    int z2 = IntegerArgumentType.getInteger(context, "z2");
                                                                    BlockPos start = new BlockPos(x1, y1, z1);
                                                                    BlockPos goal = new BlockPos(x2, y2, z2);
                                                                    ClientLevel world = Minecraft.getInstance().level;
                                                                    if (world == null) {
                                                                        context.getSource().sendFailure(Component.literal("世界为空"));
                                                                        return 0;
                                                                    }
                                                                    boolean hasPath = AStarCanGoTo.hasPath(world, start, goal);
                                                                    Player player = context.getSource().getPlayer();
                                                                    if (player != null) {
                                                                        player.sendSystemMessage(Component.literal(hasPath ? "找到路径" : "没有找到路径"));
                                                                    }
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        // 7. AStarFindPathCanGoToAndReturn 命令
        dispatcher.register(Commands.literal("AStarFindPathCanGoToAndReturn")
                .then(Commands.argument("x1", IntegerArgumentType.integer())
                        .then(Commands.argument("y1", IntegerArgumentType.integer())
                                .then(Commands.argument("z1", IntegerArgumentType.integer())
                                        .then(Commands.argument("x2", IntegerArgumentType.integer())
                                                .then(Commands.argument("y2", IntegerArgumentType.integer())
                                                        .then(Commands.argument("z2", IntegerArgumentType.integer())
                                                                .executes(context -> {
                                                                    int x1 = IntegerArgumentType.getInteger(context, "x1");
                                                                    int y1 = IntegerArgumentType.getInteger(context, "y1");
                                                                    int z1 = IntegerArgumentType.getInteger(context, "z1");
                                                                    int x2 = IntegerArgumentType.getInteger(context, "x2");
                                                                    int y2 = IntegerArgumentType.getInteger(context, "y2");
                                                                    int z2 = IntegerArgumentType.getInteger(context, "z2");
                                                                    BlockPos start = new BlockPos(x1, y1, z1);
                                                                    BlockPos goal = new BlockPos(x2, y2, z2);
                                                                    ClientLevel world = Minecraft.getInstance().level;
                                                                    if (world == null) {
                                                                        context.getSource().sendFailure(Component.literal("世界为空"));
                                                                        return 0;
                                                                    }
                                                                    List<BlockPos> path = AStarCanGoToAndReturn.findSimplePath(world, start, goal);
                                                                    Player player = context.getSource().getPlayer();
                                                                    if (player != null) {
                                                                        player.sendSystemMessage(Component.literal(path != null ? "找到路径" : "没有找到路径"));
                                                                    }
                                                                    return 1;
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
    }
}