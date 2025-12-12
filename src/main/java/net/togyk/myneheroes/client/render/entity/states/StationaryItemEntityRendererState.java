package net.togyk.myneheroes.client.render.entity.states;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class StationaryItemEntityRendererState extends LivingEntityRenderState {
    public final ItemRenderState itemRenderState = new ItemRenderState();


    public void update(Entity entity, ItemStack stack, ItemModelManager itemModelManager) {
        itemModelManager.updateForNonLivingEntity(this.itemRenderState, stack, ItemDisplayContext.GROUND, entity);
    }
}
