package net.togyk.myneheroes.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import net.togyk.myneheroes.MyneHeroes;
import net.togyk.myneheroes.power.Power;
import net.togyk.myneheroes.util.PowerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerMixin {
    @ModifyReturnValue(method = "getSkinTextures", at = @At("RETURN"))
    private SkinTextures changeSkin(SkinTextures original) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        for (Power power : PowerData.getPowers(player)) {
            if (power.overridesBaseSkin()) {
                return new SkinTextures(
                        Identifier.of(MyneHeroes.MOD_ID, "textures/entity/skins/empty.png"),
                        null, null, null, SkinTextures.Model.WIDE, false);
            }
        }
        return original;
    }
}
