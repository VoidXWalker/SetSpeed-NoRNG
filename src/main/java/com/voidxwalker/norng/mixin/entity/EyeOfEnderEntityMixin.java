package com.voidxwalker.norng.mixin.entity;

import net.minecraft.entity.EyeOfEnderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EyeOfEnderEntity.class)
public class EyeOfEnderEntityMixin {
    @Shadow public boolean dropsItem;
    /**
     * @author VoidXWalker
     * @reason no eye of ender breaks
     */
    @Inject(
            method = {"moveTowards"},
            at = {@At("TAIL")}
    )
    public void moveTowards(CallbackInfo ci){
        this.dropsItem = true;
    }
}
