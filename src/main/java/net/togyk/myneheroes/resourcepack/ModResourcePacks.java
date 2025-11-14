package net.togyk.myneheroes.resourcepack;

import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModResourcePacks {
    public static final ResourcePackInfo SHIBASHIS_SHENANIGANS = new ResourcePackInfo(Identifier.of(MyneHeroes.MOD_ID, "shibashis_shenanigans"), ResourcePackActivationType.NORMAL);
    public static final ResourcePackInfo WHITER_ADAMANTIUM = new ResourcePackInfo(Identifier.of(MyneHeroes.MOD_ID, "whiter_adamantium"), ResourcePackActivationType.NORMAL);

    public static void registerResourcePacks() {
        SHIBASHIS_SHENANIGANS.register();
        //TODO access if it the resource pack should be the default textures
        //if the textures get swaped the new name should be: Distinct/Alternative/Exotic adamantium
        WHITER_ADAMANTIUM.register();
    }
}
