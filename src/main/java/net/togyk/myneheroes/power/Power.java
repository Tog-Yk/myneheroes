package net.togyk.myneheroes.power;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.ability.Ability;
import net.togyk.myneheroes.util.AbilityUtil;
import net.togyk.myneheroes.util.PowerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
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
        this.abilities = abilities;
        this.background = Identifier.of(id.getNamespace(),"textures/power/"+id.getPath()+"_background.png");
        this.disabledBackground = Identifier.of(id.getNamespace(),"textures/power/"+id.getPath()+"_background_disabled.png");

        this.settings = settings;
        this.attributeModifiers = attributeModifiers;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("id", this.id.toString());
        nbt.putBoolean("is_dampened", this.isDampened);

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
        if (nbt.contains("is_dampened")) {
            this.isDampened = nbt.getBoolean("is_dampened");
        }

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

    public double getDamageMultiplier() {
        return this.settings.damageMultiplier;
    }

    public double getResistance() {
        return this.settings.resistance;
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
        List<Identifier> identifiers = this.abilities.stream().filter(Predicate.not(Predicate.isEqual(null))).map(Ability::getId).toList();
        Identifier id = ability.getId();
    }

    public List<Ability> getAbilities() {
        return this.abilities;
    }

    public static class Settings{
        public double damageMultiplier = 1.00;
        public double resistance = 1.00;

        public boolean canFly = false;
        public double flyingUnlocksAt = 0.05;

        public double textureInterval = 1.00;

        public Settings() {
        }

        public Power.Settings damageMultiplier(double dbl) {
            this.damageMultiplier = dbl;
            return this;
        }

        public Power.Settings resistance(double dbl) {
            this.resistance = dbl;
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
    }

    @Override
    public String toString() {
        return this.id.toString() + "{\ndamageMultiplier: " + this.getDamageMultiplier() +
                ",\nresistance: " + this.getResistance() +
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
        return attributeModifiers;
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
    }

    public void applyAttributes(AttributeContainer attributeContainer) {
        for(Map.Entry<RegistryEntry<EntityAttribute>, PowerAttributeModifierCreator> entry : this.getAttributeModifiers().modifiers.entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
            if (entityAttributeInstance != null) {
                entityAttributeInstance.removeModifier((entry.getValue()).id());
                entityAttributeInstance.addPersistentModifier((entry.getValue()).createAttributeModifier());
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