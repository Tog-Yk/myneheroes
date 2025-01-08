package net.togyk.myneheroes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.togyk.myneheroes.Item.custom.DyeableAdvancedArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
    //change the color of a armorMaterial layer based on the items NBT and if it extends the DyeableAdvancedArmorItem from this mod
    //only works on dyeable layers so check your armorMaterial
    @ModifyVariable(
            method = "renderArmor",
            at = @At("STORE"),
            ordinal = 2
    )
    private int modifyColor(int i, @Local ItemStack itemStack, @Local ArmorMaterial.Layer layer) {
        if (layer.isDyeable() && itemStack.getItem() instanceof DyeableAdvancedArmorItem armorItem) {
            List<ArmorMaterial.Layer> layers = armorItem.getMaterial().value().layers();
            return armorItem.getColor(itemStack,layers.indexOf(layer));
        } else {
            return i;
        }
    }
}
