package net.togyk.myneheroes.power;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class SpiderTobyPower extends Power{
    public boolean mayclimb = false;
    public SpiderTobyPower(int resistance, int strength) {
        super(resistance, strength);
    }
    public void ClimbLogic(PlayerEntity player) {
        if (this.mayclimb && this.enabled) {
            // logic based on a climb effect by SameDifferent
            //https://github.com/samedifferent/TrickorTreat/blob/master/LICENSE
            //MIT LICENSE!
            if (player.horizontalCollision) {
                Vec3d initialVec = player.getVelocity();
                Vec3d climbVec = new Vec3d(initialVec.x, 0.2D, initialVec.z);
                player.setVelocity(climbVec.multiply(0.96D));
            }
        }
    }
    public void ToggleClimb() {
        this.mayclimb = !this.mayclimb;
    }
    public void ShootWeb() {

    }
}
