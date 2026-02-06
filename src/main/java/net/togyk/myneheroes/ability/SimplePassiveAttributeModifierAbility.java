package net.togyk.myneheroes.ability;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Function;

public class SimplePassiveAttributeModifierAbility extends Ability implements AttributeModifierAbility, PassiveAbility {
    private final List<AttributeModifier> attributeModifiers;

    public SimplePassiveAttributeModifierAbility(Identifier id, Settings settings, List<AttributeModifier> attributeModifiers) {
        super(id, 0, settings, null);
        this.attributeModifiers = attributeModifiers;
    }

    public SimplePassiveAttributeModifierAbility(Identifier id, Settings settings, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier.Operation operation, Function<SimplePassiveAttributeModifierAbility, Double> valueSupplier) {
        this(id, settings, List.of(new AttributeModifier(attribute, operation, valueSupplier)));
    }

    @Override
    public attributeModifiers getAttributeModifiers() {
        attributeModifiers attributeModifiers = new attributeModifiers();
        for (AttributeModifier attributeModifier : this.attributeModifiers) {
            attributeModifier.add(attributeModifiers, this);
        }
        return attributeModifiers;
    }

    static class AttributeModifier {
        private final RegistryEntry<EntityAttribute> attribute;
        private final EntityAttributeModifier.Operation operation;
        private final Function<SimplePassiveAttributeModifierAbility, Double> valueSupplier;

        AttributeModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier.Operation operation, Function<SimplePassiveAttributeModifierAbility, Double> valueSupplier) {
            this.attribute = attribute;
            this.operation = operation;
            this.valueSupplier = valueSupplier;
        }

        private void add(attributeModifiers attributeModifiers, SimplePassiveAttributeModifierAbility ability) {
            Identifier id = ability.getId();
            attributeModifiers.addAttributeModifier(attribute, Identifier.of(id.getNamespace(), id.getPath()), operation, () -> valueSupplier.apply(ability));
        }
    }

    @Override
    public SimplePassiveAttributeModifierAbility copy() {
        return new SimplePassiveAttributeModifierAbility(this.id, this.settings, this.attributeModifiers);
    }
}