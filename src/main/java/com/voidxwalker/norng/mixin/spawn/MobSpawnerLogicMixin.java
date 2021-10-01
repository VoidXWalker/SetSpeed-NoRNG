package com.voidxwalker.norng.mixin.spawn;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.MobSpawnerEntry;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(MobSpawnerLogic.class)
public abstract class MobSpawnerLogicMixin {
    @Shadow private int spawnDelay;
    @Shadow private int spawnCount;
    @Shadow private int spawnRange;
    @Shadow private int maxNearbyEntities;
    @Shadow private MobSpawnerEntry spawnEntry;
    @Shadow private double field_9159;
    @Shadow private double field_9161 ;
    @Shadow public abstract void spawnEntity(Entity entity);
    @Shadow public abstract World getWorld();
    @Shadow public abstract BlockPos getPos();
    @Shadow public abstract boolean isPlayerInRange();
    @Shadow  public abstract void updateSpawns();
    /**
     * @author VoidXWalker
     * @reason buffs blaze spawns
     */
    @Overwrite
    public void update(){
        if (!this.isPlayerInRange()) {
            this.field_9159 = this.field_9161;
        } else {
            World world = this.getWorld();
            BlockPos blockPos = this.getPos();
            if (world.isClient) {
                double d = (double)blockPos.getX() + world.random.nextDouble();
                double e = (double)blockPos.getY() + world.random.nextDouble();
                double f = (double)blockPos.getZ() + world.random.nextDouble();
                world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0D, 0.0D, 0.0D);
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                }
                this.field_9159 = this.field_9161;
                this.field_9161 = (this.field_9161 + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0D;
            } else {
                if (this.spawnDelay == -1) {
                    this.updateSpawns();
                }
                if (this.spawnDelay > 0) {
                    --this.spawnDelay;
                    return;
                }
                boolean bl = false;
                int i = 0;
                this.spawnCount=4;
                while(true) {
                    if (i >= this.spawnCount) {
                        if (bl) {
                            this.updateSpawns();
                        }
                        break;
                    }
                    CompoundTag compoundTag = this.spawnEntry.getEntityTag();
                    Optional<EntityType<?>> optional = EntityType.fromTag(compoundTag);
                    if (!optional.isPresent()) {
                        this.updateSpawns();
                        return;
                    }
                    ListTag listTag = compoundTag.getList("Pos", 6);
                    int j = listTag.size();
                    double g = j >= 1 ? listTag.getDouble(0) : (double)blockPos.getX() + (world.random.nextDouble() - world.random.nextDouble()) *( (double)this.spawnRange -1.0D);
                    double h = j >= 2 ? listTag.getDouble(1) : (double)(blockPos.getY() + world.random.nextInt(2) );
                    double k = j >= 3 ? listTag.getDouble(2) : (double)blockPos.getZ() + (world.random.nextDouble() - world.random.nextDouble()) * ((double)this.spawnRange -1.0D);
                    if (true) {
                        label97: {
                            Entity entity = EntityType.loadEntityWithPassengers(compoundTag, world, (entityx) -> {
                                entityx.refreshPositionAndAngles(g, h, k, entityx.yaw, entityx.pitch);
                                return entityx;
                            });
                            if (entity == null) {
                                this.updateSpawns();
                                return;
                            }
                            int l = world.getNonSpectatingEntities(entity.getClass(), (new Box((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), (double)(blockPos.getX() + 1), (double)(blockPos.getY() + 1), (double)(blockPos.getZ() + 1))).expand((double)this.spawnRange)).size();
                            if (l >= this.maxNearbyEntities) {
                                this.updateSpawns();
                                return;
                            }
                            entity.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), world.random.nextFloat() * 360.0F, 0.0F);
                            if (entity instanceof MobEntity) {
                                MobEntity mobEntity = (MobEntity)entity;
                                if (!mobEntity.canSpawn(world, SpawnReason.SPAWNER) || !mobEntity.canSpawn(world)) {
                                    break label97;
                                }
                                if (this.spawnEntry.getEntityTag().getSize() == 1 && this.spawnEntry.getEntityTag().contains("id", 8)) {
                                    ((MobEntity)entity).initialize(world, world.getLocalDifficulty(entity.getBlockPos()), SpawnReason.SPAWNER, (EntityData)null, (CompoundTag)null);
                                }
                            }
                            this.spawnEntity(entity);
                            world.syncWorldEvent(2004, blockPos, 0);
                            if (entity instanceof MobEntity) {
                                ((MobEntity)entity).playSpawnEffects();
                            }
                            bl = true;
                        }
                    }

                    ++i;
                }
            }

        }
    }
}
