package be.isach.musicalmobs.nms;

import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

public interface IEntityUtil {


    void randomWalk(Sheep sheep);

    void sendTitle(Player player, String title, String subtitle);

    void sendActionBar(Player player, String message);
}
