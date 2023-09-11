package be.isach.musicalmobs.v1_8_R3;

import be.isach.musicalmobs.nms.IEntityUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSheep;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.*;

import java.lang.reflect.Field;

public class EntityUtil implements IEntityUtil {

    @Override
    public void randomWalk(Sheep sheep) {
        EntitySheep entitySheep = ((CraftSheep) sheep).getHandle();

        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(entitySheep.goalSelector, new UnsafeList<PathfinderGoalSelector>());
            bField.set(entitySheep.targetSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(entitySheep.goalSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(entitySheep.targetSelector, new UnsafeList<PathfinderGoalSelector>());
        } catch (Exception exc) {
            exc.printStackTrace();
        }


        entitySheep.goalSelector.a(0, new PathfinderGoalFloat(entitySheep));
        entitySheep.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(entitySheep, 1.0D));
        entitySheep.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(entitySheep, 1.0D, false));
        entitySheep.goalSelector.a(7, new PathfinderGoalRandomStroll(entitySheep, 1.0D));
        entitySheep.goalSelector.a(8, new PathfinderGoalLookAtPlayer(entitySheep, EntityHuman.class, 8.0F));
        entitySheep.goalSelector.a(8, new PathfinderGoalRandomLookaround(entitySheep));
        entitySheep.targetSelector.a(1, new PathfinderGoalHurtByTarget(entitySheep, true));

        entitySheep.goalSelector.a(3, new SheepPanicGoal(entitySheep, 0.4d));

        entitySheep.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(1.25D);
    }

    @Override
    public void sendActionBar(org.bukkit.entity.Player player, String message) {
        // Send action bar msg
        ActionBarAPI.send(player, message);
    }

    @Override
    public void sendTitle(org.bukkit.entity.Player player, String title, String subtitle) {
        Title t = new Title(title, subtitle);
        t.send(player);
    }
}
