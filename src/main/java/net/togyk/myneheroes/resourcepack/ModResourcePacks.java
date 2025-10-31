package net.togyk.myneheroes.resourcepack;

import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModResourcePacks {
    public static final ResourcePackInfo SHIBASHIS_SHENANIGANS = new ResourcePackInfo(Identifier.of(MyneHeroes.MOD_ID, "shibashis_shenanigans"), ResourcePackActivationType.NORMAL);

    public static void registerResourcePacks() {
        SHIBASHIS_SHENANIGANS.register();
    }
}
