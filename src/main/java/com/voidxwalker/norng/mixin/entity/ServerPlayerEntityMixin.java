package com.voidxwalker.norng.mixin.entity;

import com.mojang.authlib.GameProfile;
import com.voidxwalker.norng.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    @Shadow public abstract ServerWorld getServerWorld();

    public ServerPlayerEntityMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, gameProfile);
    }
    /**
     * @author VoidXWalker
     * @reason initializes Mod
     */
    @Inject(
            method = {"<init>"},
            at = {@At("TAIL")}
    )
    public void init(CallbackInfo ci) {
        Main.random = new Random();
        Main.random.setSeed(getServerWorld().getSeed());
        Main.pearlTrades=0;
        Main.initializeBarterRates();
    }
}
