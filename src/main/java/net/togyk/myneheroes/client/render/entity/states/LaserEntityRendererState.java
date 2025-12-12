package net.togyk.myneheroes.client.render.entity.states;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class LaserEntityRendererState extends ProjectileEntityRenderState {
    public Vec3d velocity = new Vec3d(0, 0, 0);
    public int innerColor;
    public int color;
}