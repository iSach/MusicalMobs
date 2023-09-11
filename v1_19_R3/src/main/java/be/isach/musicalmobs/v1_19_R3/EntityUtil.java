package be.isach.musicalmobs.v1_19_R3;

import be.isach.musicalmobs.nms.IEntityUtil;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftSheep;
import org.bukkit.entity.Sheep;

public class EntityUtil implements IEntityUtil {

    @Override
    public void randomWalk(Sheep sheep) {
        net.minecraft.world.entity.animal.Sheep entitySheep = ((CraftSheep) sheep).getHandle();

        /*
        try {
            Field bField = GoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = GoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(entitySheep.goalSelector, new UnsafeList<GoalSelector>());
            bField.set(entitySheep.targetSelector, new UnsafeList<GoalSelector>());
            cField.set(entitySheep.goalSelector, new UnsafeList<GoalSelector>());
            cField.set(entitySheep.targetSelector, new UnsafeList<GoalSelector>());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
         */
        entitySheep.goalSelector.removeAllGoals((goal) -> true);
        entitySheep.targetSelector.removeAllGoals((goal) -> true);

        entitySheep.goalSelector.addGoal(0, new FloatGoal(entitySheep));
        entitySheep.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(entitySheep, 1.0D));
        entitySheep.goalSelector.addGoal(6, new MoveThroughVillageGoal(entitySheep, 1.0D, false, 0, () -> false));
        entitySheep.goalSelector.addGoal(7, new RandomStrollGoal(entitySheep, 1.0D));
        entitySheep.goalSelector.addGoal(8, new LookAtPlayerGoal(entitySheep, Player.class, 8.0F));
        entitySheep.goalSelector.addGoal(8, new RandomLookAroundGoal(entitySheep));
        entitySheep.targetSelector.addGoal(1, new HurtByTargetGoal(entitySheep));

        entitySheep.goalSelector.addGoal(3, new SheepPanicGoal(entitySheep, 0.4d));

        entitySheep.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.25D);
    }

    @Override
    public void sendActionBar(org.bukkit.entity.Player player, String message) {
        // Send action bar msg
        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message));
    }

    @Override
    public void sendTitle(org.bukkit.entity.Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle, 10, 70, 20);
    }
}
