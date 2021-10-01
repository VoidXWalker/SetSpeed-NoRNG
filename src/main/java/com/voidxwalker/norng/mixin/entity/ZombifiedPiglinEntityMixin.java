//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.voidxwalker.norng.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ZombifiedPiglinEntity.class})
public abstract class ZombifiedPiglinEntityMixin extends Entity {
    public ZombifiedPiglinEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    /**
     * @author VoidXWalker
     * @reason no zombiefied piglin spawns in bastions
     */
    @Inject(
            method = {"canSpawn(Lnet/minecraft/world/WorldView;)Z"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void canSpawn(WorldView world, CallbackInfoReturnable<Boolean> cir) {
       World entityWorld = this.getEntityWorld();
        if (entityWorld instanceof ServerWorld && ((ServerWorld)entityWorld).getStructureAccessor().method_28388(this.getBlockPos(), true, StructureFeature.BASTION_REMNANT).hasChildren()) {
            cir.setReturnValue(false);
        }
    }
}
