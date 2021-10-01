package com.voidxwalker.norng.mixin;

import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({DebugHud.class})
public class DebugHudMixin {
    @Inject(
            method = {"getRightText"},
            at = {@At("RETURN")}
    )
    private void getRightText(CallbackInfoReturnable<List<String>> info) {
            List<String> returnValue = (List) info.getReturnValue();
            returnValue.add("Using Void_X_Walker's Official SetSpeed No RNG Mod");
    }
}
