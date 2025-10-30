package net.togyk.myneheroes.resourcepack;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

import java.util.Collection;

public class ModResourcePacks {
    public static boolean SHIBASHIS_SHENANIGANS = false;
    public static final Identifier SHIBASHIS_SHENANIGANS_ID = Identifier.of(MyneHeroes.MOD_ID, "shibashis_shenanigans");

    public static void registerResourcePacks() {
        ModContainer container = FabricLoader.getInstance().getModContainer(MyneHeroes.MOD_ID).orElseThrow();

        ResourceManagerHelper.registerBuiltinResourcePack(
                SHIBASHIS_SHENANIGANS_ID,
                container,
                Text.translatable("dataPack.myneheroes.shibashis_shenanigans"),
                ResourcePackActivationType.NORMAL
        );

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Collection<String> enabled = server.getDataPackManager().getEnabledIds();
            SHIBASHIS_SHENANIGANS = enabled.contains(SHIBASHIS_SHENANIGANS_ID.toString());
        });
    }
}
