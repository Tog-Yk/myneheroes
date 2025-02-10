package net.togyk.myneheroes.Item;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModPredicates {
    public static void registerModPredicates() {
        MyneHeroes.LOGGER.info("Registering Predicates for " + MyneHeroes.MOD_ID);

        ModelPredicateProviderRegistry.register(ModItems.VIBRANIUM_SHIELD, Identifier.of(MyneHeroes.MOD_ID,"blocking"),
                (stack, world, entity, seed) -> {
                    // Check if the entity is using the shield.
                    return stack.getItem() instanceof ShieldItem && entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
                });
    }
}
