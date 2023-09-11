package be.isach.musicalmobs.v1_20_R1;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * Created by sacha on 25/07/15.
 */
public class SheepPanicGoal extends Goal {

    private Mob mob;

    // speed
    protected double speed;

    // random PosX
    private double randomX;

    // random PosY
    private double randomY;

    // random PosZ
    private double randomZ;

    public SheepPanicGoal(Mob mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        //this.a(1);
    }

    @Override
    public boolean canUse() {
        BlockPos pos = generateRandomPos(mob, 5, 4, null);
        this.randomX = pos.getX();
        this.randomY = pos.getY();
        this.randomZ = pos.getZ();
        return true;
    }

    private static BlockPos generateRandomPos(Mob mob, int var1, int var2, BlockPos blockPos) {
        RandomSource var4 = mob.getRandom();
        boolean var5 = false;
        int var6 = 0;
        int var7 = 0;
        int var8 = 0;
        float var9 = -99999.0F;
        boolean var14;
        if (mob.hasRestriction()) {
            double var10 = mob.getRestrictCenter().distToLowCornerSqr(Math.floor(mob.getX()), Math.floor(mob.getY()), Math.floor(mob.getZ())) + 4.0;
            double var12 = (double) (mob.getRestrictRadius() + (float) var1);
            var14 = var10 < var12 * var12;
        } else {
            var14 = false;
        }

        for (int var15 = 0; var15 < 10; ++var15) {
            int var16 = var4.nextInt(2 * var1 + 1) - var1;
            int var17 = var4.nextInt(2 * var2 + 1) - var2;
            int var18 = var4.nextInt(2 * var1 + 1) - var1;
            if (blockPos == null || !((double) var16 * blockPos.getX() + (double) var18 * blockPos.getZ() < 0.0)) {
                BlockPos var19;
                if (mob.hasRestriction() && var1 > 1) {
                    var19 = mob.getRestrictCenter();
                    if (mob.getX() > (double) var19.getX()) {
                        var16 -= var4.nextInt(var1 / 2);
                    } else {
                        var16 += var4.nextInt(var1 / 2);
                    }

                    if (mob.getZ() > (double) var19.getZ()) {
                        var18 -= var4.nextInt(var1 / 2);
                    } else {
                        var18 += var4.nextInt(var1 / 2);
                    }
                }

                var16 += (int) Math.floor(mob.getX());
                var17 += (int) Math.floor(mob.getY());
                var18 += (int) Math.floor(mob.getZ());
                var19 = new BlockPos(var16, var17, var18);
                if (!var14 || mob.isWithinRestriction(var19)) {
                   // float var20 = mob.a(var19);
                    float var20 = 0.0F;
                    if (var20 > var9) {
                        var9 = var20;
                        var6 = var16;
                        var7 = var17;
                        var8 = var18;
                        var5 = true;
                    }
                }
            }
        }

        if (var5) {
            return new BlockPos(var6, var7, var8);
        } else {
            return null;
        }
    }

    @Override
    public void tick() {
        this.mob.getNavigation().moveTo(this.randomX, this.randomY, this.randomZ, this.speed);
    }

    @Override
    public boolean canContinueToUse() {
        // CraftBukkit start - introduce a temporary timeout hack until this is fixed properly
        /*if ((this.mob.tickCount - this.mob.hurtTime) > 100) {
            this.mob.hurt(null, 0);
            return false;
        }*/
        // CraftBukkit end
        return !this.mob.getNavigation().isDone();
    }
}
