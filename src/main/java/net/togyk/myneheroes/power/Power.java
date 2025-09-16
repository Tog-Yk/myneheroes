package net.togyk.myneheroes.power;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.ability.AttributeModifierAbility;
import net.togyk.myneheroes.util.AbilityUtil;
import net.togyk.myneheroes.util.PowerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Power {
    public static final Codec<Power> CODEC = Codec.PASSTHROUGH
            .xmap(
                    dynamic -> PowerUtil.nbtToPower((NbtCompound) dynamic.convert(NbtOps.INSTANCE).getValue()),
                    power -> new Dynamic<>(NbtOps.INSTANCE, power.writeNbt(new NbtCompound()))
            );

    public static final PacketCodec<ByteBuf, Power> PACKET_CODEC = new PacketCodec<>() {
        public Power decode(ByteBuf buf) {
            return PowerUtil.nbtToPower(PacketCodecs.NBT_COMPOUND.decode(buf));
        }

        public void encode(ByteBuf buf, Power power) {
            PacketCodecs.NBT_COMPOUND.encode(buf, power.writeNbt(new NbtCompound()));
        }
    };

    public final Identifier id;
    protected final Settings settings;
    public List<Ability> abilities;

    private boolean isDampened = false;
    protected int color;

    private PlayerEntity holder;

    private final Identifier background;
    private final Identifier disabledBackground;

    protected final attributeModifiers attributeModifiers;

    public Power(Identifier id, int color, List<Ability> abilities, Settings settings, attributeModifiers attributeModifiers) {
        this.id = id;
        this.color = color;
        this.abilities = abilities.stream().map(Ability::copy).toList();
        this.background = Identifier.of(id.getNamespace(),"textures/power/"+id.getPath()+"_background.png");
        this.disabledBackground = Identifier.of(id.getNamespace(),"textures/power/"+id.getPath()+"_background_disabled.png");

        this.settings = settings;
        this.attributeModifiers = attributeModifiers;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("id", this.id.toString());

        NbtList abilitiesNbt = new NbtList();
        for (Ability ability : this.abilities) {
            if (ability != null) {
                ability.setHolder(this);
                abilitiesNbt.add(ability.writeNbt(new NbtCompound()));
            }
        }

        nbt.put("abilities", abilitiesNbt);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        NbtList abilitiesNbt = new NbtList();
        if (nbt.contains("abilities")) {
            abilitiesNbt = nbt.getList("abilities", NbtElement.COMPOUND_TYPE);
        }

        List<Ability> abilitiesList = new ArrayList<>();
        for (NbtElement nbtElement : abilitiesNbt) {
            if (nbtElement instanceof NbtCompound nbtCompound) {
                Ability ability = AbilityUtil.nbtToAbility(nbtCompound);
                abilitiesList.add(ability);
            }
        }
        this.abilities = abilitiesList;
    }


    public Identifier getId() {
        return id;
    }

    public boolean isDampened() {
        return isDampened;
    }

    public void setIsDampened(boolean dampened) {
        isDampened = dampened;
    }

    public boolean canStandOnWater() {
        return false;
    }

    public boolean isPhasing() {
        return false;
    }

    public boolean canWallClimb() {
        return settings.canWallCrawl && this.holder != null && !this.holder.isOnGround();
    }

    public double getDamageMultiplier() {
        return this.settings.damageMultiplier;
    }

    public double getArmor() {
        return this.settings.armor;
    }

    public boolean allowFlying(PlayerEntity player) {
        return settings.canFly;
    }

    public int getColor() {
        return color;
    }

    public Identifier getBackground() {
        if (this.isDampened) {
            return disabledBackground;
        } else {
            return background;
        }
    }

    public void tick(PlayerEntity player) {
    }

    public void setHolder(@Nullable PlayerEntity newHolder) {
        holder = newHolder;
    }

    public PlayerEntity getHolder() {
        return holder;
    }

    public void saveAbility(Ability ability) {
    }

    public List<Ability> getAbilities() {
        if (!this.isDampened()) {
            return this.abilities;
        }
        return new ArrayList<>();
    }

    public boolean isDampenedByKryptonite() {
        return settings.isDampenedByKryptonite;
    }

    public static class Settings{
        public double damageMultiplier = 1.00;
        public double armor = 1.00;

        public boolean canFly = false;
        public double flyingUnlocksAt = 0.05;

        public boolean canWallCrawl = false;

        public double textureInterval = 1.00;

        public boolean isDampenedByKryptonite = false;
        public boolean isDampenedByRunes = false;
        public boolean isDampenedByMetaDampeners = false;

        public Settings() {
        }

        public Power.Settings damageMultiplier(double dbl) {
            this.damageMultiplier = dbl;
            return this;
        }

        public Power.Settings armor(double dbl) {
            this.armor = dbl;
            return this;
        }

        public Power.Settings canFly() {
            this.canFly = true;
            return this;
        }

        public Power.Settings flyingUnlocksAt(double dbl) {
            this.canFly = true;
            this.flyingUnlocksAt = dbl;
            return this;
        }

        public Power.Settings textureInterval(double dbl) {
            this.textureInterval = dbl;
            return this;
        }

        public Power.Settings dampenedByKryptonite() {
            this.isDampenedByKryptonite = true;
            return this;
        }

        public Power.Settings dampenedByRunes() {
            this.isDampenedByRunes = true;
            return this;
        }

        public Power.Settings dampenedByMetaDampeners() {
            this.isDampenedByMetaDampeners = true;
            return this;
        }

        public Power.Settings canWallCrawl() {
            this.canWallCrawl = true;
            return this;
        }
    }

    @Override
    public String toString() {
        return this.id.toString() + "{\ndamageMultiplier: " + this.getDamageMultiplier() +
                ",\narmor: " + this.getArmor() +
                ",\nabilities:" + this.abilities.toString();
    }

    public static class attributeModifiers {
        private final Map<RegistryEntry<EntityAttribute>, PowerAttributeModifierCreator> modifiers = new Object2ObjectOpenHashMap();


        public Power.attributeModifiers addAttributeModifier(RegistryEntry<EntityAttribute> attribute, Identifier id, EntityAttributeModifier.Operation operation, Supplier<Double> valueSupplier) {
            this.modifiers.put(attribute, new PowerAttributeModifierCreator(id, operation, valueSupplier));
            return this;
        }

        public attributeModifiers() {
        }
    }

    public attributeModifiers getAttributeModifiers() {
        return attributeModifiers
                .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, Identifier.of(MyneHeroes.MOD_ID, "power.armor"), EntityAttributeModifier.Operation.ADD_VALUE, this::getArmor);
    }

    public void updateAttributes(AttributeContainer attributeContainer) {
        removeAttributes(attributeContainer);
        applyAttributes(attributeContainer);
    }

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

    public void applyAttributes(AttributeContainer attributeContainer) {
        for(Map.Entry<RegistryEntry<EntityAttribute>, PowerAttributeModifierCreator> entry : this.getAttributeModifiers().modifiers.entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
            if (entityAttributeInstance != null) {
                entityAttributeInstance.removeModifier((entry.getValue()).id());
                entityAttributeInstance.addPersistentModifier((entry.getValue()).createAttributeModifier());
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


    record PowerAttributeModifierCreator(Identifier id, EntityAttributeModifier.Operation operation, Supplier<Double> valueSupplier) {
        public EntityAttributeModifier createAttributeModifier() {
                return new EntityAttributeModifier(this.id, valueSupplier.get(), this.operation);
        }
    }

    public Power copy() {
        return new Power(this.id, color, List.copyOf(this.abilities), settings, attributeModifiers);
    }
}