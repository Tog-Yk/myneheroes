package net.togyk.myneheroes.resourcepack;

import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModResourcePacks {
    public static final ResourcePackInfo SHIBASHIS_SHENANIGANS = new ResourcePackInfo(Identifier.of(MyneHeroes.MOD_ID, "shibashis_shenanigans"), ResourcePackActivationType.NORMAL);
    public static final ResourcePackInfo ALTERNATIVE_ADAMANTIUM = new ResourcePackInfo(Identifier.of(MyneHeroes.MOD_ID, "alternative_adamantium"), ResourcePackActivationType.NORMAL);
    public static final ResourcePackInfo DISTINCT_ADAMANTIUM = new ResourcePackInfo(Identifier.of(MyneHeroes.MOD_ID, "distinct_adamantium"), ResourcePackActivationType.NORMAL);

    public static void registerResourcePacks() {
        SHIBASHIS_SHENANIGANS.register();
        //TODO access if any of the resource packs should be the default textures
        //if the textures get swaped the new name should be: Distinct/Alternative/Exotic adamantium
        ALTERNATIVE_ADAMANTIUM.register();
        DISTINCT_ADAMANTIUM.register();
    }
}
