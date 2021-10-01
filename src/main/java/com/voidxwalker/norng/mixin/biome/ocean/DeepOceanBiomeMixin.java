package com.voidxwalker.norng.mixin.biome.ocean;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DeepOceanBiome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeepOceanBiome.class)
public class DeepOceanBiomeMixin extends Biome {
    protected DeepOceanBiomeMixin(Settings settings) {
        super(settings);
    }

    @Inject(
            method = {"<init>"},
            at = {@At("RETURN")}
    )
    public void manipulateSpawns(CallbackInfo ci){
        this.addSpawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.SQUID, 10, 1, 2));

        this.addSpawn(SpawnGroup.WATER_CREATURE, new SpawnEntry(EntityType.DOLPHIN, 2000, 2, 2));
    }
}
