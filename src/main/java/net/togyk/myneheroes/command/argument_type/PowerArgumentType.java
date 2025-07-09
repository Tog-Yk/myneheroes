package net.togyk.myneheroes.command.argument_type;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.Powers;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PowerArgumentType implements ArgumentType<Identifier> {
    public static PowerArgumentType identifier() {
        return new PowerArgumentType();
    }

    public static Identifier getIdentifier(CommandContext<ServerCommandSource> context, String name) {
        return context.getArgument(name, Identifier.class);
    }

    @Override
    public Identifier parse(StringReader reader) throws CommandSyntaxException {
        return Identifier.fromCommandInput(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Identifier id : Powers.getIds()) {
            String idString = id.toString();
            if (idString.startsWith(builder.getInput())) {
                builder.suggest(idString);
            }
        }

        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("modid:power", Powers.SPEEDSTER.getId().toString());
    }

    public static void register() {
        ArgumentTypeRegistry.registerArgumentType(Identifier.of(MyneHeroes.MOD_ID, "power"), PowerArgumentType.class, ConstantArgumentSerializer.of(PowerArgumentType::identifier));
    }
}
