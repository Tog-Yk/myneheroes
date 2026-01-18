package net.togyk.myneheroes.mixin;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityJumpAccessor {
    @Accessor("jumping")
    boolean myneheroes$jumping();
}
