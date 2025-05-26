package net.togyk.myneheroes.client.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.client.screen.handeler.ArmorDyeingScreenHandler;
import net.togyk.myneheroes.client.screen.handeler.ArmorLightLevelerScreenHandler;
import net.togyk.myneheroes.client.screen.handeler.SelectionAbilityScreenHandler;
import net.togyk.myneheroes.client.screen.handeler.ToolbeltAbilityScreenHandler;
import net.togyk.myneheroes.networking.BlockPosPayload;
import net.togyk.myneheroes.networking.SelectionScreenAbilityPayload;
import net.togyk.myneheroes.networking.ToolBeltScreenAbilityPayload;

public class ModScreenHandlerTypes {
    public static final ScreenHandlerType<ArmorDyeingScreenHandler> ARMOR_DYEING =
            register("armor_dyeing", ArmorDyeingScreenHandler::new, BlockPosPayload.PACKET_CODEC);
    public static final ScreenHandlerType<ArmorLightLevelerScreenHandler> ARMOR_LIGHT_LEVELER =
            register("armor_light_leveler", ArmorLightLevelerScreenHandler::new, BlockPosPayload.PACKET_CODEC);

    public static final ScreenHandlerType<SelectionAbilityScreenHandler> SELECTION_ABILITY =
            register("selection_ability", SelectionAbilityScreenHandler::new, SelectionScreenAbilityPayload.CODEC);

    public static final ScreenHandlerType<ToolbeltAbilityScreenHandler> TOOLBELT_ABILITY =
            register("toolbelt_ability", ToolbeltAbilityScreenHandler::new, ToolBeltScreenAbilityPayload.CODEC);


    public static <T extends ScreenHandler, D extends CustomPayload> ExtendedScreenHandlerType<T, D>
        register(String name, ExtendedScreenHandlerType.ExtendedFactory<T, D> factory, PacketCodec<? super RegistryByteBuf, D> codec) {
            return Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MyneHeroes.MOD_ID, name), new ExtendedScreenHandlerType<>(factory, codec));
    }

    public static void registerModScreenHandlerTypes() {
        MyneHeroes.LOGGER.info("Registering Screen Handler Types for " + MyneHeroes.MOD_ID);
    }
}
