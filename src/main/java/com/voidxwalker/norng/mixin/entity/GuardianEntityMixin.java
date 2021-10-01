package com.voidxwalker.norng.mixin.entity;

import net.minecraft.entity.mob.GuardianEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuardianEntity.class)
public abstract class GuardianEntityMixin {
    @Shadow public abstract void setSpikesRetracted(boolean retracted);
    /**
     * @author VoidXWalker
     * @reason no guardian spikes
     */
    @Inject(
            method = {"tickMovement"},
            at = {@At("TAIL")}
    )
    public void tick(CallbackInfo ci){
        setSpikesRetracted(true);
    }
}
