package net.togyk.myneheroes.power;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AttributeModifierAbility;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class TransformationPower extends Power implements VariableLinkedPower {
    private int transformationTime = 0;
    protected final int maxTransformationTime;
    private boolean transforming = true;

    protected final transformationAttributeModifiers attributeModifiers;

    public TransformationPower(Identifier id, int maxTransformationTime, int color, List<Ability> abilities, Settings settings, transformationAttributeModifiers attributeModifiers) {
        super(id, color, abilities, settings, new attributeModifiers());
        this.maxTransformationTime = maxTransformationTime;
        this.attributeModifiers = attributeModifiers;
    }

    @Override
    public void tick(PlayerEntity player) {
        if (this.isTransforming()) {
            if (this.getTransformationTime() < this.getMaxTransformationTime()) {
                this.setTransformationTime(this.getTransformationTime() + 1);
            } else {
                this.setTransformationTime(this.getMaxTransformationTime());
            }
        } else {
            if (this.getTransformationTime() > 0) {
                this.setTransformationTime(this.getTransformationTime() - 1);
            } else {
                this.setTransformationTime(0);
            }
        }
        super.tick(player);
    }

    public void setTransformationTime(int transformationTime) {
        this.transformationTime = transformationTime;
    }

    public int getTransformationTime() {
        return transformationTime;
    }

    public int getMaxTransformationTime() {
        return maxTransformationTime;
    }

    public double getTransformationProgress() {
        return (double) getTransformationTime() / getMaxTransformationTime();
    }

    @Override
    public transformationAttributeModifiers getAttributeModifiers() {
        return attributeModifiers
                .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, Identifier.of(MyneHeroes.MOD_ID, "power.armor"), EntityAttributeModifier.Operation.ADD_VALUE, transformationProgress -> this.getArmor() * transformationProgress);
    }

    public boolean isTransforming() {
        return transforming;
    }

    public void setTransforming(boolean transforming) {
        this.transforming = transforming;
    }

    @Override
    public boolean overridesBaseSkin() {
        return super.overridesBaseSkin() && this.getTransformationProgress() == 1.0D;
    }

    @Override
    public boolean appliesSkin() {
        return super.appliesSkin() && this.getTransformationProgress() != 0.0D;
    }

    @Override
    public int getSkinOpacity() {
        return (int) (0xFF * this.getTransformationProgress());
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putBoolean("transforming", this.isTransforming());
        nbt.putInt("transformation_time", this.getTransformationTime());
        return super.writeNbt(nbt);
    }

    @Override
    public Object get(String name) {
        if (name.equals("transforming")) {
            return this.isTransforming();
        }
        return null;
    }

    @Override
    public boolean canSet(String name, Object variable) {
        return name.equals("transforming") && variable instanceof Boolean;
    }

    @Override
    public void set(String name, Object variable) {
        if (name.equals("transforming") && variable instanceof Boolean transforming) {
            this.setTransforming(transforming);
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("transforming")) {
            this.setTransforming(nbt.getBoolean("transforming"));
        }

        if (nbt.contains("transformation_time")) {
            this.setTransformationTime(nbt.getInt("transformation_time"));
        }
    }

    public static class transformationAttributeModifiers extends Power.attributeModifiers {
        private final Map<RegistryEntry<EntityAttribute>, PowerAttributeModifierCreator> modifiers = new Object2ObjectOpenHashMap();


        public transformationAttributeModifiers addAttributeModifier(RegistryEntry<EntityAttribute> attribute, Identifier id, EntityAttributeModifier.Operation operation, Function<Double, Double> valueSupplier) {
            this.modifiers.put(attribute, new PowerAttributeModifierCreator(id, operation, valueSupplier));
            return this;
        }

        public transformationAttributeModifiers addAttributeModifier(RegistryEntry<EntityAttribute> attribute, Identifier id, EntityAttributeModifier.Operation operation, Double notTransformed, Double transformed) {
            this.modifiers.put(attribute, new PowerAttributeModifierCreator(id, operation, transformationProgress -> notTransformed + (transformed - notTransformed) * transformationProgress));
            return this;
        }

        @Override
        public transformationAttributeModifiers addAttributeModifier(RegistryEntry<EntityAttribute> attribute, Identifier id, EntityAttributeModifier.Operation operation, Supplier<Double> valueSupplier) {
            this.modifiers.put(attribute, new PowerAttributeModifierCreator(id, operation, transformationProgress -> valueSupplier.get() * transformationProgress));
            return this;
        }

        public transformationAttributeModifiers() {
        }
    }

    @Override
    public void applyAttributes(AttributeContainer attributeContainer) {
        for(Map.Entry<RegistryEntry<EntityAttribute>, PowerAttributeModifierCreator> entry : this.getAttributeModifiers().modifiers.entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
            if (entityAttributeInstance != null) {
                entityAttributeInstance.removeModifier((entry.getValue()).id());
                entityAttributeInstance.addPersistentModifier((entry.getValue()).createAttributeModifier(this));
            }
        }
        //also apply attribute modifiers from abilities
        for(Ability ability : this.getAbilities()) {
            if (ability instanceof AttributeModifierAbility modifierAbility) {
                for(Map.Entry<RegistryEntry<EntityAttribute>, AttributeModifierAbility.AbilityAttributeModifierCreator> entry : modifierAbility.getAttributeModifiers().modifiers.entrySet()) {
                    EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
                    if (entityAttributeInstance != null) {
                        entityAttributeInstance.removeModifier((entry.getValue()).id());
                        entityAttributeInstance.addPersistentModifier((entry.getValue()).createAttributeModifier());
                    }
                }
            }
        }
    }

    @Override
    public void removeAttributes(AttributeContainer attributeContainer) {
        for(Map.Entry<RegistryEntry<EntityAttribute>, PowerAttributeModifierCreator> entry : this.getAttributeModifiers().modifiers.entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
            if (entityAttributeInstance != null) {
                entityAttributeInstance.removeModifier((entry.getValue()).id());
            }
        }
        //also apply attribute modifiers from abilities
        for(Ability ability : this.getAbilities()) {
            if (ability instanceof AttributeModifierAbility modifierAbility) {
                for(Map.Entry<RegistryEntry<EntityAttribute>, AttributeModifierAbility.AbilityAttributeModifierCreator> entry : modifierAbility.getAttributeModifiers().modifiers.entrySet()) {
                    EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
                    if (entityAttributeInstance != null) {
                        entityAttributeInstance.removeModifier((entry.getValue()).id());
                    }
                }
            }
        }
    }

    record PowerAttributeModifierCreator(Identifier id, EntityAttributeModifier.Operation operation, Function<Double, Double> valueSupplier) {
        public EntityAttributeModifier createAttributeModifier(TransformationPower power) {
            return new EntityAttributeModifier(this.id, valueSupplier.apply(power.getTransformationProgress()), this.operation);
        }
    }

    @Override
    public TransformationPower copy() {
        return new TransformationPower(id, maxTransformationTime, color, List.copyOf(this.abilities), settings, attributeModifiers);
    }
}
