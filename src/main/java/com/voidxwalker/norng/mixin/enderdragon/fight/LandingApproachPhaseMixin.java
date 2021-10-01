package com.voidxwalker.norng.mixin.enderdragon.fight;

import com.google.common.collect.Lists;
import com.voidxwalker.norng.Main;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.LandingApproachPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.EndPortalFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LandingApproachPhase.class)
public abstract class LandingApproachPhaseMixin extends AbstractPhase {
    public LandingApproachPhaseMixin(EnderDragonEntity dragon) {
        super(dragon);
    }
    public PathNode[] pathNodes = new PathNode[24];
    @Shadow
    public Path field_7047;
    private static final TargetPredicate PLAYERS_IN_RANGE_PREDICATE = (new TargetPredicate()).setBaseMaxDistance(128.0D);
    @Shadow private Vec3d target;
    @Inject( method = {"serverTick"}, at = {@At("HEAD")}, cancellable = true)
    public void serverTick(CallbackInfo ci) {
        this.method_6844();
        ci.cancel();
    }
    /**
     * @author Void_X_Walker
     * @reason Implements the custom, faster end fight
     */
    @Overwrite
    private void method_6844() {
        if (this.field_7047 == null || this.field_7047.isFinished()) {
            if (this.field_7047 == null || this.field_7047.isFinished()) {
                int i = this.dragon.getNearestPathNodeIndex();
                BlockPos blockPos = this.dragon.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPortalFeature.ORIGIN);
                PlayerEntity playerEntity = this.dragon.world.getClosestPlayer(PLAYERS_IN_RANGE_PREDICATE, (double) blockPos.getX(), (double) blockPos.getY(), (double) blockPos.getZ());
                int k;
                if (playerEntity != null) {
                    Vec3d vec3d = (new Vec3d(playerEntity.getX(), 0.0D, playerEntity.getZ())).normalize();
                    k = this.dragon.getNearestPathNodeIndex(-vec3d.x * 40.0D, 105.0D, -vec3d.z * 40.0D);
                } else {
                    k = this.dragon.getNearestPathNodeIndex(40.0D, (double) blockPos.getY(), 0.0D);
                }

                PathNode pathNode = new PathNode(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                this.field_7047 = this.dragon.findPath(i, k, pathNode);
                if (this.field_7047 != null) {
                    this.field_7047.next();
                }
            }

            this.method_6845();
            if (Main.ready) {
                this.dragon.getPhaseManager().setPhase(PhaseType.LANDING);
            }

        }
    }
    /**
     * @author Void_X_Walker
     * @reason Implements the custom, faster end fight
     */
    @Overwrite
        public void method_6845(){
        for(int i = 0; i < 24; ++i) {
            int j = 5;
            int n;
            int o;
            if (i < 12) {
                n = MathHelper.floor(60.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.2617994F * (float)i)));
                o = MathHelper.floor(60.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.2617994F * (float)i)));
            } else {
                int k;
                if (i < 20) {
                    k = i - 12;
                    n = MathHelper.floor(40.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.3926991F * (float)k)));
                    o = MathHelper.floor(40.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.3926991F * (float)k)));
                    j += 10;
                } else {
                    k = i - 20;
                    n = MathHelper.floor(20.0F * MathHelper.cos(2.0F * (-3.1415927F + 0.7853982F * (float)k)));
                    o = MathHelper.floor(20.0F * MathHelper.sin(2.0F * (-3.1415927F + 0.7853982F * (float)k)));
                }
            }

            int r = Math.max(Main.enderDragon.world.getSeaLevel() + 10,Main.enderDragon.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(n, 0, o)).getY() + j);
            this.pathNodes[i] = new PathNode(n, r, o);
        }
        List<PathNode> list = Lists.newArrayList();
        PathNode pathNode;
        if(Main.enderDragon.getZ()>0) {
            pathNode = this.pathNodes[Main.enderDragon.getNearestPathNodeIndex(0, 80, 30)];
        }
        else{
            pathNode = this.pathNodes[Main.enderDragon.getNearestPathNodeIndex(0, 80, -30)];
        }
        list.add(0, pathNode);

        this.field_7047 = new Path(list, new BlockPos(pathNode.x, pathNode.y, pathNode.z), true);
        if (this.field_7047 != null && !this.field_7047.isFinished()) {
            Vec3i vec3i = this.field_7047.getCurrentPosition();
            this.field_7047.next();
            double d = (double)vec3i.getX();
            double e = (double)vec3i.getZ();

            double f;
            do {
                f = (double)((float)vec3i.getY());
            } while(f < (double)vec3i.getY());

            this.target = new Vec3d(d, f, e);
        }

    }

}
