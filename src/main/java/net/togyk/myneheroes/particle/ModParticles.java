package net.togyk.myneheroes.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;

public class ModParticles {
    public static void registerParticles() {
        MyneHeroes.LOGGER.info("Registering Particles for " + MyneHeroes.MOD_ID);
    }

    public static final SimpleParticleType ELECTRICITY_PARTICLE =
            registerParticle("electricity_particle", FabricParticleTypes.simple());

    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MyneHeroes.MOD_ID, name), particleType);
    }
}