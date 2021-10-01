package com.voidxwalker.norng.mixin.biome.ocean;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DeepLukewarmOceanBiome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeepLukewarmOceanBiome.class)
public class DeepLukewarmOceanBiomeMixin extends Biome {
    protected DeepLukewarmOceanBiomeMixin(Settings settings) {
        super(settings);
    }

    @Inject(
            method = {"<init>"},
            at = {@At("RETURN")}
    )
    public void manipulateSpawns(CallbackInfo ci){
        this.addSpawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.SQUID, 10, 1, 2));
        this.addSpawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.COD, 1500, 6, 6));
        this.addSpawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.PUFFERFISH, 1000, 3, 3));
        this.addSpawn(SpawnGroup.WATER_AMBIENT, new SpawnEntry(EntityType.TROPICAL_FISH, 2500, 8, 8));
        this.addSpawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.DOLPHIN, 2000, 2, 2));
    }
}
