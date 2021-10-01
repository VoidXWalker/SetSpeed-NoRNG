package com.voidxwalker.norng.mixin.spawn;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(SpawnHelper.class)
public class SpawnHelperMixin {
    @Shadow private static  boolean isAcceptableSpawnPosition(ServerWorld world, Chunk chunk, BlockPos.Mutable pos, double squaredDistance){
        throw new IllegalStateException();
    }
    @Shadow private static boolean canSpawn(ServerWorld world, SpawnGroup group, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Biome.SpawnEntry spawnEntry, BlockPos.Mutable pos, double squaredDistance) {
        throw new IllegalStateException();
    }
    @Shadow private static MobEntity createMob(ServerWorld world, EntityType<?> type) {
        throw new IllegalStateException();
    }
    @Shadow private static boolean isValidSpawn(ServerWorld world, MobEntity entity, double squaredDistance) {
        throw new IllegalStateException();
    }
    @Shadow private static Biome.SpawnEntry pickRandomSpawnEntry(ServerWorld serverWorld, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, SpawnGroup spawnGroup, Random random, BlockPos blockPos) {
        throw new IllegalStateException();
    }
    /**
     * @author VoidXWalker
     * @reason manipulates dolphin spawns
     */
    @Overwrite
    public static void spawnEntitiesInChunk(SpawnGroup group, ServerWorld world, Chunk chunk, BlockPos pos, SpawnHelper.Checker checker, SpawnHelper.Runner runner){
        StructureAccessor structureAccessor = world.getStructureAccessor();
        ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
        int i = pos.getY();
        BlockState blockState = chunk.getBlockState(pos);
        if (!blockState.isSolidBlock(chunk, pos)) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            int j = 0;
            for(int k = 0; k < 3; ++k) {
                int l = pos.getX();
                int m = pos.getZ();
                int n = 1;
               Biome.SpawnEntry spawnEntry = null;
                EntityData entityData = null;
                int o = MathHelper.ceil(world.random.nextFloat() * 4.0F);
                int p = 0;
                boolean gate=false;
                boolean gate2=false;
                for(int q = 0; q < o; ++q) {
                    l += world.random.nextInt(6) - world.random.nextInt(6);
                    m += world.random.nextInt(6) - world.random.nextInt(6);
                    mutable.set(l, i, m);
                    double d = (double)l + 0.5D;
                    double e = (double)m + 0.5D;
                    PlayerEntity playerEntity = world.getClosestPlayer(d, (double)i, e, -1.0D, false);
                    if (playerEntity != null) {
                        double f = playerEntity.squaredDistanceTo(d, (double)i, e);
                        if (isAcceptableSpawnPosition(world, chunk, mutable, f)) {
                            if (spawnEntry == null) {
                                spawnEntry = pickRandomSpawnEntry(world, structureAccessor, chunkGenerator, group, world.random, pos);
                                if (spawnEntry == null) {
                                    break;
                                }
                                o = spawnEntry.minGroupSize + world.random.nextInt(1 + spawnEntry.maxGroupSize - spawnEntry.minGroupSize);
                            }
                            if (canSpawn(world, group, structureAccessor, chunkGenerator, spawnEntry, mutable, f) && checker.test(spawnEntry.type, mutable, chunk)) {
                                MobEntity mobEntity = createMob(world, spawnEntry.type);
                                String id = ("" + EntityType.getId(mobEntity.getType()));
                                if (id.equals("minecraft:cod") || id.equals("minecraft:salmon") || id.equals("minecraft:pufferfish") || id.equals("minecraft:tropical_fish") || id.equals("minecraft:squid")) {
                                    if (world.random.nextInt(10) == 9) {
                                        mobEntity = createMob(world, EntityType.DOLPHIN);
                                        gate = true;
                                    }
                                }

                                if (mobEntity == null) {
                                    return;
                                }

                                mobEntity.refreshPositionAndAngles(d, (double) i, e, world.random.nextFloat() * 360.0F, 0.0F);
                                if (!gate2) {
                                    if (gate || isValidSpawn(world, mobEntity, f)) {
                                        entityData = mobEntity.initialize(world, world.getLocalDifficulty(mobEntity.getBlockPos()), SpawnReason.NATURAL, entityData, (CompoundTag) null);

                                        ++j;
                                        ++p;
                                        world.spawnEntity(mobEntity);
                                        gate2=true;
                                        runner.run(mobEntity, chunk);
                                        if (j >= mobEntity.getLimitPerChunk()) {
                                            return;
                                        }

                                        if (mobEntity.spawnsTooManyForEachTry(p)) {
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
