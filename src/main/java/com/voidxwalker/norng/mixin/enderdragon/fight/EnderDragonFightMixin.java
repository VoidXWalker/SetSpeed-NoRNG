//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.voidxwalker.norng.mixin.enderdragon.fight;

import com.voidxwalker.norng.Main;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EnderDragonFight.class})
class EnderDragonFightMixin {
    EnderDragonFightMixin() {
    }
    public boolean i;
    /**
     * @author VoidXWalker
     * @reason Implements the custom, faster end fight
     */
    @Inject(
            at = {@At("RETURN")},
            method = {"createDragon"}
    )
    private void createDragon(CallbackInfoReturnable<EnderDragonEntity> cir) {
        Main.ready=false;
        Main.enderDragon = cir.getReturnValue();
        Main.waitingForPerch = true;
        i = false;
    }@Inject(
            at = {@At("HEAD")},
            method = {"tick"}
    )
    private void tick(CallbackInfo ci) {
        if(i == false&&Main.enderDragon!=null) {
            Main.enderDragon.getPhaseManager().setPhase(PhaseType.LANDING_APPROACH);
            i = true;
        }
        if (Main.waitingForPerch) {
            PlayerEntity playerEntity = Main.enderDragon.world.getClosestPlayer(0, 0, 0, 2000, false);
            if(playerEntity!=null){
                if ((playerEntity.getZ()>=10 ||playerEntity.getZ()<=-10)&&playerEntity.getX()<=10&&playerEntity.getX()>=-10&&playerEntity.getY()<=70) {
                    Main.ready = true;
                    Main.waitingForPerch = false;
                }
            }
        }

    }
}
