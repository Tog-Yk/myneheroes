package net.togyk.myneheroes.client.render.upgrade;

import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.upgrade.Upgrade;
import net.togyk.myneheroes.upgrade.Upgrades;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class UpgradeModelRegistry {
    private static final Map<Identifier, Function<EntityModelLoader, UpgradeModel>> MODELS = new HashMap<>();


    private static void registerAbilityRenderer(Upgrade upgrade, Function<EntityModelLoader, UpgradeModel> model) {
        if (!MODELS.containsKey(upgrade.getId())) {
            MODELS.put(upgrade.getId(), model);
        } else {
            MyneHeroes.LOGGER.error("There's already a model linked to the upgrade with the id of {}", upgrade.getId());
        }
    }

    static {
        registerAbilityRenderer(Upgrades.TOOLBELT_3, ToolbeltModel::new);
        registerAbilityRenderer(Upgrades.TOOLBELT_4, ToolbeltModel::new);
        registerAbilityRenderer(Upgrades.TOOLBELT_6, ToolbeltModel::new);
        registerAbilityRenderer(Upgrades.TOOLBELT_8, ToolbeltModel::new);
    }

    public static Function<EntityModelLoader, UpgradeModel> get(Upgrade upgrade) {
        return MODELS.getOrDefault(upgrade.getId(), null);
    }

    public static UpgradeModel get(Upgrade upgrade, EntityModelLoader loader) {
        Function<EntityModelLoader, UpgradeModel> modelProvider = MODELS.getOrDefault(upgrade.getId(), null);
        if (modelProvider != null) {
            return modelProvider.apply(loader);
        }
        return null;
    }
}
