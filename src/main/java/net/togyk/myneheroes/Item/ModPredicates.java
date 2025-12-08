package net.togyk.myneheroes.Item;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.util.ModTags;

public class ModPredicates {
    private static void registerShieldBlocking(Item item) {
        ModelPredicateProviderRegistry.register(item, Identifier.of(MyneHeroes.MOD_ID,"blocking"),
                (stack, world, entity, seed) -> {
                    // Check if the entity is using the shield.
                    return stack.isIn(ModTags.Items.SHIELDS) && entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F;
                });

    }

    public static void registerModPredicates() {
        MyneHeroes.LOGGER.info("Registering Predicates for " + MyneHeroes.MOD_ID);

        registerShieldBlocking(ModItems.VIBRANIUM_SHIELD);
        registerShieldBlocking(ModItems.A_SYMBOLS_SHIELD);
        registerShieldBlocking(ModItems.COSMIC_SHIELD);
        registerShieldBlocking(ModItems.CARTERS_SHIELD);

        registerShieldBlocking(ModItems.WOODEN_KATANA);
        registerShieldBlocking(ModItems.STONE_KATANA);
        registerShieldBlocking(ModItems.IRON_KATANA);
        registerShieldBlocking(ModItems.GOLD_KATANA);
        registerShieldBlocking(ModItems.DIAMOND_KATANA);
        registerShieldBlocking(ModItems.NETHERITE_KATANA);
        registerShieldBlocking(ModItems.ADAMANTIUM_KATANA);
    }
}
