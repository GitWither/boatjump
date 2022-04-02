package daniel.boatjump.mixin;

import daniel.boatjump.BoatJump;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity {

    @Shadow private float velocityDecay;

    @Shadow public @Nullable abstract Entity getPrimaryPassenger();

    public BoatEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "updateVelocity", at = @At(value = "INVOKE", target = "net/minecraft/entity/vehicle/BoatEntity.getPrimaryPassenger()Lnet/minecraft/entity/Entity;"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    public void injectHorizontalCollisionCheck(CallbackInfo cb, double d, double e, double f) {
        if (BoatJump.CAN_BE_USED) {
            Vec3d currentVel = this.getVelocity();
            this.setVelocity(currentVel.x * this.velocityDecay, horizontalCollision ? 0.303 : currentVel.y + e, currentVel.z * this.velocityDecay);
        }
    }
}
