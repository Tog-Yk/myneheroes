package net.togyk.myneheroes.client.render.entity.states;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.math.Vec3d;
import net.togyk.myneheroes.entity.MeteorVariant;

@Environment(EnvType.CLIENT)
public class MeteorEntityRendererState extends ProjectileEntityRenderState {
    public Vec3d velocity;
    public float size;
    public MeteorVariant variant;
}
