package com.voidxwalker.norng.mixin.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ElderGuardianEntity;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Iterator;
import java.util.List;


@Mixin(ElderGuardianEntity.class)
public class ElderGuardianEntityMixin extends GuardianEntity {
    public ElderGuardianEntityMixin(EntityType<? extends GuardianEntity> entityType, World world) {
        super(entityType, world);
    }
    /**
     * @author VoidXWalker
     * @reason no elder guardian dong
     */
    @Overwrite
    public void mobTick(){
        super.mobTick();
        int i = 1;
        if ((this.age -30) % 1200 == 0) {
            StatusEffect statusEffect = StatusEffects.MINING_FATIGUE;
            List<ServerPlayerEntity> list = ((ServerWorld) this.world).getPlayers((serverPlayerEntityx) -> {
                return this.squaredDistanceTo(serverPlayerEntityx) < 2500.0D && serverPlayerEntityx.interactionManager.isSurvivalLike();
            });
            int j = 1;
            int k = 1;
            int l = 1;
            Iterator var7 = list.iterator();
            label33:
            while (true) {
                ServerPlayerEntity serverPlayerEntity;
                do {
                    if (!var7.hasNext()) {
                        break label33;
                    }

                    serverPlayerEntity = (ServerPlayerEntity) var7.next();
                } while (serverPlayerEntity.hasStatusEffect(statusEffect) && serverPlayerEntity.getStatusEffect(statusEffect).getAmplifier() >= 2 && serverPlayerEntity.getStatusEffect(statusEffect).getDuration() >= 1200);
                if( serverPlayerEntity.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE)&&serverPlayerEntity.hasStatusEffect(StatusEffects.BAD_OMEN)) {
                    serverPlayerEntity.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.ELDER_GUARDIAN_EFFECT, this.isSilent() ? 0.0F : 1.0F));
                    serverPlayerEntity.addStatusEffect(new StatusEffectInstance(statusEffect, 6000, 2));
                }

            }
        }
    }

}
