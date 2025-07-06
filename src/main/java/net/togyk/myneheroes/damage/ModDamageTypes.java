package net.togyk.myneheroes.damage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.togyk.myneheroes.MyneHeroes;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> POWERFUL_PUNCH_TYPE_KEY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(MyneHeroes.MOD_ID, "powered_punch"));
    public static final RegistryKey<DamageType> LASER_TYPE_KEY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(MyneHeroes.MOD_ID, "laser"));


    public static DamageSource of(World world, RegistryKey<DamageType> key, LivingEntity attacker) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key), attacker);
    }
}
