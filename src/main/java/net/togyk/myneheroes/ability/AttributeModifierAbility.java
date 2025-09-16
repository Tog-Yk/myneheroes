package net.togyk.myneheroes.ability;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.function.Supplier;

public interface AttributeModifierAbility {
    attributeModifiers getAttributeModifiers();


    class attributeModifiers {
        public final Map<RegistryEntry<EntityAttribute>, AbilityAttributeModifierCreator> modifiers = new Object2ObjectOpenHashMap();


        public attributeModifiers addAttributeModifier(RegistryEntry<EntityAttribute> attribute, Identifier id, EntityAttributeModifier.Operation operation, Supplier<Double> valueSupplier) {
            this.modifiers.put(attribute, new AbilityAttributeModifierCreator(id, operation, valueSupplier));
            return this;
        }

        public attributeModifiers() {
        }
    }


    record AbilityAttributeModifierCreator(Identifier id, EntityAttributeModifier.Operation operation, Supplier<Double> valueSupplier) {
        public EntityAttributeModifier createAttributeModifier() {
            return new EntityAttributeModifier(this.id, valueSupplier.get(), this.operation);
        }
    }
}
