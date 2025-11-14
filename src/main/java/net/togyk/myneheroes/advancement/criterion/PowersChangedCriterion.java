package net.togyk.myneheroes.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.power.Powers;
import net.togyk.myneheroes.registry.ModRegistryKeys;

import java.util.Optional;

public class PowersChangedCriterion extends AbstractCriterion<PowersChangedCriterion.Conditions> {
    public static final Identifier ID = Identifier.of(MyneHeroes.MOD_ID, "powers_changed");

    public PowersChangedCriterion() {}


    public Codec<Conditions> getConditionsCodec() {
        return PowersChangedCriterion.Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, Identifier identifier) {
        this.trigger(player, (conditions) -> conditions.matches(identifier));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<Identifier> id, Optional<TagKey<Power>> tag)
            implements AbstractCriterion.Conditions {

        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player),
                        Identifier.CODEC.optionalFieldOf("id").forGetter(Conditions::id),
                        Identifier.CODEC.optionalFieldOf("tag")
                                .xmap(
                                        optId -> optId.map(id ->
                                                TagKey.of(ModRegistryKeys.POWER, id)
                                        ),
                                        optTag -> optTag.map(TagKey::id)
                                ).forGetter(Conditions::tag)
                ).apply(instance, Conditions::new)
        );

        public static AdvancementCriterion<Conditions> any() {
            return ModCriteria.POWERS_CHANGED_CRITERION.create(new Conditions(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        public boolean matches(Identifier powerId) {
            if (id.isPresent() && id.get().equals(powerId)) {
                return true;
            }

            // Match tag
            if (tag.isPresent()) {
                Power power = Powers.get(powerId);
                if (power != null) {
                    return power.isIn(tag.get());
                }
            }

            return false;
        }
    }
}
