package net.togyk.myneheroes.client.render.entity.states;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class ThrownItemEntityRendererState extends ProjectileEntityRenderState {
    public final ItemRenderState itemRenderState = new ItemRenderState();
    public ItemStack displayStack = ItemStack.EMPTY;
    public Vec3d velocity = new Vec3d(0, 0, 0);
    public boolean landed = false;
    public Vector3f groundedOffset = new Vector3f();
    public BlockPos blockPos = new BlockPos(0, 0, 0);
    public int light = 0;


    public void update(Entity entity, ItemStack stack, ItemModelManager itemModelManager) {
        this.displayStack = stack;
        itemModelManager.updateForNonLivingEntity(this.itemRenderState, stack, ItemDisplayContext.GROUND, entity);
    }
}
