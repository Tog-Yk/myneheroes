package net.togyk.myneheroes.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.command.argument_type.PowerArgumentType;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.util.PowerData;
import net.togyk.myneheroes.util.PowerUtil;

import java.util.List;

public class ModCommands {
    public static void registerModCommands() {
        MyneHeroes.LOGGER.info("registering commands for " + MyneHeroes.MOD_ID);

        PowerArgumentType.register();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    // /myneheroes$addPower <power>
                    CommandManager.literal("power").requires((source) -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("parent", EntityArgumentType.players())
                            .then(CommandManager.literal("add")
                            .then(CommandManager.argument("power", new PowerArgumentType())
                                    .executes(ctx -> {
                                        if (Powers.get(ctx.getArgument("power", Identifier.class)) instanceof Power power) {
                                            PowerData.addPower(EntityArgumentType.getPlayer(ctx, "parent"), power);
                                        }
                                        return 1;
                                    })
                            ))
                            .then(CommandManager.literal("remove")
                            .then(CommandManager.argument("power", new PowerArgumentType())
                                .executes(ctx -> {
                                    PlayerEntity player = EntityArgumentType.getPlayer(ctx, "parent");
                                    List<Power> powers = PowerData.getPowers(player);
                                    Power power = PowerUtil.getPowerMatchingId(powers, ctx.getArgument("power", Identifier.class));
                                    if (power != null) {
                                        PowerData.removePower(player, power);
                                    }
                                    return 1;
                                })
                            ))
                            .then(CommandManager.literal("clear")
                                    .executes(ctx -> {
                                        PowerData.setPowers(EntityArgumentType.getPlayer(ctx, "parent"), List.of());
                                        return 1;
                                    })
                            )
                        )
            );
        });
    }
}
