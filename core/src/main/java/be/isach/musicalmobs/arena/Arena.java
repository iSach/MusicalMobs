package be.isach.musicalmobs.arena;

import be.isach.musicalmobs.config.SettingsManager;
import be.isach.musicalmobs.MusicalMobs;
import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;
import com.xxmicloxx.NoteBlockAPI.SongPlayer;
import be.isach.musicalmobs.config.MessageManager;
import be.isach.musicalmobs.listeners.SignsManager;
import be.isach.musicalmobs.util.*;
import org.bukkit.*;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Random;

/**
 * Created by sacha on 21/07/15.
 */
public class Arena {

    // Arena States.
    public enum ArenaState {

        DISABLED("§4§lDISABLED"),

        WAITING(MessageManager.getMessage("GameState.WAITING")),

        COUNTING_DOWN(MessageManager.getMessage("GameState.COUNTDOWN")),

        STARTED(MessageManager.getMessage("GameState.STARTED"));

        private String displayName;

        ArenaState(String displayName) {
            this.displayName = displayName;
        }

        public String toString() {
            return displayName;
        }
    }

    // Arena ID.
    private int id;

    // Players Data.
    private ArrayList<PlayerData> data;

    // Location where players spawn.
    private Location playerSpawn;

    // Location where sheeps spawn (center).
    private Location sheepSpawn;

    // Lobby Signs.
    private ArrayList<Sign> signs;

    // Minimum amount of players.
    private int minPlayers;

    // Maximum amount of players.
    private int maxPlayers;

    // Current amount of players.
    private int currentPlayers;

    // Lobby countdown.
    private int lobbyCountdown;

    // Arena name.
    private String displayName;

    // Arena Current State.
    protected ArenaState state = ArenaState.DISABLED;

    // Current round.
    private int currentRound = 1;

    /**
     * Arena Constructor
     *
     * @param id The {@see be.isach.musicalmobs.arena.Arena} ID.
     */
    protected Arena(int id) {

        // Declaring Arena Infos.
        this.id = id;
        this.data = new ArrayList<>();
        this.minPlayers = SettingsManager.getArenas().get("arenas." + id + ".minPlayers");
        this.maxPlayers = SettingsManager.getArenas().get("arenas." + id + ".maxPlayers");
        this.displayName = SettingsManager.getArenas().get("arenas." + id + ".displayName");
        this.lobbyCountdown = SettingsManager.getArenas().get("arenas." + id + ".lobbyCountdown");
        ConfigurationSection sheepSpawnSection = SettingsManager.getArenas().get("arenas." + id + ".sheepSpawn");
        this.sheepSpawn = ConfigUtils.getLocationFromConfig(sheepSpawnSection, true);
        ConfigurationSection playerSpawnSection = SettingsManager.getArenas().get("arenas." + id + ".playerSpawn");
        this.playerSpawn = ConfigUtils.getLocationFromConfig(playerSpawnSection, true);

        state = ArenaState.WAITING;

        // Load the signs.
        this.signs = SignsManager.getSigns(this);
    }

    /**
     * Gets the arena ID.
     *
     * @return the arena ID.
     */
    public int getID() {
        return id;
    }

    /**
     * Gets the data of a given {@see org.bukkit.entity.Player}.
     *
     * @param player The {@see org.bukkit.entity.Player}.
     * @return the data of {@see org.bukkit.entity.Player} p.
     */
    public PlayerData getPlayerData(Player player) {
        for (PlayerData d : data) if (d.belongsTo(player)) return d;
        return null;
    }

    /**
     * Adds the data of a given player.
     *
     * @param player The {@see org.bukkit.entity.Player}.
     */
    public void addPlayer(Player player) {
        if (currentPlayers >= maxPlayers) {
            player.sendMessage(MessageManager.getMessage("Arena-Full"));
            return;
        }

        if (this.playerSpawn == null || this.sheepSpawn == null) {
            PlayerUtils.sendMessage(player, "Spawn-Points-Not-Set");
            return;
        }

        if (state == ArenaState.STARTED) {
            PlayerUtils.sendMessage(player, "Already-In-Game");
            return;
        }

        File f = new File(MusicalMobs.instance.getDataFolder().getPath() + "/songs/" + getSong() + ".nbs");
        if (!f.exists()) {
            player.sendMessage(MessageManager.getMessage("Song-File-Doesnt-Exist").replaceAll("%song%", getSong()));
            return;
        }

        data.add(new PlayerData(player));
        player.getInventory().clear();
        player.teleport(getPlayerSpawn());
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(false);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);

        currentPlayers++;

        broadcast(MessageManager.getMessage("Joined-Arena").replaceAll("%player%", player.getName()).replaceAll("%current%", getCurrentPlayers() + "").replaceAll("%max%", getMaxPlayers() + ""));

        if (currentPlayers >= (int) SettingsManager.getArenas().get("arenas." + getID() + ".minPlayers") && state == ArenaState.WAITING) {
            start();
        }

        MusicalMobs.reloadSigns();
    }

    /**
     * Removes the data of a given {@see org.bukkit.entity.Player}.
     *
     * @param player The {@see org.bukkit.entity.Player}.
     */
    public void removePlayer(Player player, boolean broadcastMessage) {

        player.removePotionEffect(PotionEffectType.SPEED);

        getPlayerData(player).restore();
        data.remove(getPlayerData(player));

        currentPlayers--;



        for (Sign s : signs) {
            s.setLine(2, "§8" + currentPlayers + "/" + getMaxPlayers());
            s.update();
        }

        if (state == ArenaState.STARTED) {
            if (currentPlayers == 1) {
                stop(data.get(0).getPlayer());
            } else if (currentPlayers == 0) {
                stop();
            }
        } else {
            if (state == ArenaState.COUNTING_DOWN) {
                if (currentPlayers < (int) SettingsManager.getArenas().get("arenas." + getID() + ".minPlayers")) {
                    broadcast(MessageManager.getMessage("Countdown-Stopped"));
                    countdown.cancel();
                    this.state = ArenaState.WAITING;
                } else if (currentPlayers == 0) {
                    countdown.cancel();
                    this.state = ArenaState.WAITING;
                    stop();
                }
            }
        }
        if (broadcastMessage) {
            broadcast(MessageManager.getMessage("Left-Arena").replaceAll("%player%", player.getName()).replaceAll("%current%", getCurrentPlayers() + "").replaceAll("%max%", getMaxPlayers() + ""));
            PlayerUtils.sendMessage(player, "You-Left-Arena");
        }

        MusicalMobs.reloadSigns();

    }

    public Countdown countdown;

    /**
     * Starts the countdown (which starts after the game).
     */
    public void start() {
        this.state = ArenaState.COUNTING_DOWN;
        for (Sign s : signs) {
            s.setLine(3, getState().toString());
            s.update();
        }

        countdown = new Countdown(
                getLobbyCountdown(),
                MessageManager.getMessage("Game-Starting-In").replaceAll("%sec%", "%t"),
                this,
                30,
                20,
                10,
                5,
                4,
                3,
                2,
                1
        );
        countdown.runTaskTimer(MusicalMobs.instance, 0, 20);
    }

    /**
     * Stops the game with a winner.
     *
     * @param winner The winner of the game.
     */
    public void stop(Player winner) {
        this.currentRound = 1;
        this.state = ArenaState.WAITING;
        if (winner != null)
            Bukkit.broadcastMessage(MessageManager.getMessage("Broadcast-Win-On-End.message").replaceAll("%player%", winner.getName()).replaceAll("%id%", getID() + ""));
        for (Player p : getPlayers())
            removePlayer(p, false);
        this.state = ArenaState.WAITING;
        for (Sheep sheep : round.sheeps)
            sheep.remove();
        round.sp.setPlaying(false);
        round.cancel();
        MusicalMobs.reloadSigns();
    }

    /**
     * Stops the game without a winner.
     */
    public void stop() {
        this.state = ArenaState.WAITING;
        this.currentRound = 1;
        broadcast(MessageManager.getMessage("Arena-Ended").replaceAll("%id%", getID() + ""));
        try {
            for (Player p : getPlayers())
                removePlayer(p, false);
        } catch (ConcurrentModificationException exception) {
            //TODO: Fix that if reported again.
        }
        for (Sheep sheep : round.sheeps)
            sheep.remove();
        round.sp.setPlaying(false);
        round.cancel();
        MusicalMobs.reloadSigns();

    }

    /**
     * The countdown runnable.
     */
    private class Countdown extends BukkitRunnable {

        private int timer;
        private String msg;
        private Arena a;
        private ArrayList<Integer> countingNums;

        public Countdown(int start, String msg, Arena a, int... countingNums) {
            this.timer = start;
            this.msg = msg;
            this.a = a;
            this.countingNums = new ArrayList<Integer>();
            for (int i : countingNums) this.countingNums.add(i);
        }

        public void run() {
            if (timer == 0) {
                for (PlayerData pd : data) {
                    pd.getPlayer().teleport(getPlayerSpawn());
                }
                broadcast(MessageManager.getMessage("Game-Started"));
                a.state = ArenaState.STARTED;
                round = new Round();
                round.runTask(MusicalMobs.instance);
                for (Sign s : signs) {
                    s.setLine(3, getState().toString());
                    s.update();
                }
                countdown = null;
                cancel();
            }

            if (countingNums.contains(timer)) {
                if (timer <= 5)
                    for (PlayerData pd : data)
                        pd.getPlayer().playSound(pd.getPlayer().getLocation(), XSound.BLOCK_NOTE_BLOCK_HAT.parseSound(), 1, 1);
                broadcast(msg.replace("%t", timer + ""));
            }

            timer--;
        }
    }

    /**
     * Eliminate a player.
     *
     * @param player The {@see org.bukkit.entity.Player}.
     */
    public void eliminate(Player player) {
        broadcast(MessageManager.getMessage("Player-Eliminated").replaceAll("%left%", getCurrentPlayers() - 1 + "").replaceAll("%max%", getMaxPlayers() + "").replaceAll("%player%", player.getName()));
        removePlayer(player, false);
    }

    public Round round;

    /**
     * The round runnable.
     */
    public class Round extends BukkitRunnable {

        public ArrayList<Sheep> sheeps = new ArrayList<>();
        public SongPlayer sp;

        @Override
        public void run() {
            int sheepsAmount = getCurrentPlayers() - 1;
            for (int i = 0; i < sheepsAmount; i++) {
                Location center = getSheepSpawn().clone();
                center.add(0, 0.5, 0);

                Sheep sheep = (Sheep) center.getWorld().spawnEntity(center, EntityType.SHEEP);

                MusicalMobs.instance.getVersionManager().getEntityUtil().randomWalk(sheep);

                sheeps.add(sheep);
            }

            for (Player p : getPlayers())
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10000000, 1, false, false));

            File f = new File("/plugins/musicalmobs/songs/");
            if (!f.exists())
                f.mkdirs();
            Song s = NBSDecoder.parse(new File(MusicalMobs.instance.getDataFolder(), "songs/" + getSong() + ".nbs"));
            if (s == null) {
                broadcast(MessageManager.getMessage("Song-File-Doesnt-Exist").replaceAll("%song%", getSong()));
                stop();
                cancel();
                return;
            }
            sp = new RadioSongPlayer(s);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (ArenaManager.getInstance().getArena(p) != null && ArenaManager.getInstance().getArena(p).getID() == getID()) {
                    sp.addPlayer(p);
                }
            }
            sp.setPlaying(true);
            Random r = new Random();
            long randomTime = MathsUtils.randInt(200, 400);
            final int A = Bukkit.getScheduler().runTaskTimer(MusicalMobs.instance, new Runnable() {
                @Override
                public void run() {
                    for (Sheep s : sheeps) {
                        // TODO s.getWorld().spigot().playEffect(s.getLocation(), Effect.NOTE, 0, MathsUtils.randInt(1, 24), 1.3f, 0.6f, 1.3f, .1f, 5, 32);
                        // TODO s.setColor(DyeColor.getByData((byte) MathsUtils.randInt(1, 15)));
                        //s.setColor(DyeColor.getByColor(Color.fromBGR(MathUtils.randInt(0, 255), MathUtils.randInt(0, 255), MathUtils.randInt(0, 255))));
                        s.setColor(DyeColor.BLUE);
                    }
                }
            }, 0, 3).getTaskId();
            Bukkit.getScheduler().runTaskLater(MusicalMobs.instance, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getScheduler().cancelTask(A);
                    for (Player players : getPlayers()) {
                        players.playSound(players.getLocation(), XSound.ENTITY_VILLAGER_NO.parseSound(), 3, 1);
                        MusicalMobs.instance.getVersionManager().getEntityUtil().sendTitle(players, "§4", MessageManager.getMessage("Get-On-A-Sheep"));
                    }
                    sp.setPlaying(false);
                    final int B = Bukkit.getScheduler().runTaskTimer(MusicalMobs.instance, new Runnable() {
                        double i = 10;

                        @Override
                        public void run() {
                            for (Player p : getPlayers()) {
                                MusicalMobs.instance.getVersionManager().getEntityUtil().sendActionBar(p, MessageManager.getMessage("Elimination-In").replaceAll("%t%", new DecimalFormat("00.0").format(i) + ""));
                            }
                            i -= 0.1;
                        }
                    }, 0, 2).getTaskId();
                    Bukkit.getScheduler().runTaskLater(MusicalMobs.instance, new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.getScheduler().cancelTask(B);
                            boolean stopNow = true;
                            for (Sheep cs : sheeps) {
                                if (cs.getPassenger() != null)
                                    stopNow = false;
                            }
                            if (stopNow) {
                                broadcast(MessageManager.getMessage("No-One-on-Sheep"));
                                stop();
                                cancel();
                                return;
                            }
                            for (Player p : getPlayers()) {
                                MusicalMobs.instance.getVersionManager().getEntityUtil().sendActionBar(p, MessageManager.getMessage("Eliminating"));
                            }
                            for (Player p : getPlayers()) {
                                if (p.getVehicle() == null && state == ArenaState.STARTED) {
                                    eliminate(p);
                                }
                            }
                            for (Sheep sheep : sheeps) {
                                getWorld().playEffect(sheep.getLocation(), Effect.LAVA_INTERACT, 0);
                                sheep.remove();
                            }
                            cancel();
                            if (currentPlayers > 1) {
                                round = new Round();
                                currentRound++;
                                for (Player p : getPlayers()) {
                                    MusicalMobs.instance.getVersionManager().getEntityUtil().sendTitle(p, MessageManager.getMessage("Round").replaceAll("%round%", currentRound + ""), "");
                                }
                                round.runTask(MusicalMobs.instance);
                            }
                        }
                    }, 10 * 20);
                }
            }, randomTime);


        }

    }

    /**
     * Gets the world of the playerspawn.
     *
     * @return the world of the playerspawn location.
     */
    public World getWorld() {
        return Bukkit.getWorld((String) SettingsManager.getArenas().get("arenas." + getID() + ".playerSpawn.world"));
    }

    /**
     * Gets the song.
     *
     * @return the song.
     */
    public String getSong() {
        return SettingsManager.getArenas().get("arenas." + getID() + ".song");
    }

    /**
     * Gets a list of all the current players.
     *
     * @return a list of all the current players.
     */
    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (PlayerData pd : data) players.add(pd.getPlayer());
        return players;
    }

    /**
     * Gets the lobby countdown.
     *
     * @return The lobby countdown.
     */
    public int getLobbyCountdown() {
        return SettingsManager.getArenas().get("arenas." + getID() + ".lobbyCountdown");
    }

    /**
     * Broadcast a message in the arena.
     *
     * @param message The message to send.
     */
    public void broadcast(String message) {
        for (PlayerData d : data) d.getPlayer().sendMessage(message);
    }

    /**
     * Checks if the arena contains a given player.
     *
     * @param player The {@see org.bukkit.entity.Player}.
     * @return {@code true} if the arena contains the given player, {@code false} otherwise.
     */
    public boolean containsPlayer(Player player) {
        return getPlayerData(player) != null;
    }

    /**
     * Gets the arena name.
     *
     * @return the arena name.
     */
    public String getDisplayName() {
        return SettingsManager.getArenas().get("arenas." + getID() + ".displayName");
    }

    /**
     * Sets the name of the arena.
     *
     * @param displayName The display name.
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the maximum amount of players.
     *
     * @return the maximum amount of players.
     */
    public int getMaxPlayers() {
        return SettingsManager.getArenas().get("arenas." + getID() + ".maxPlayers");
    }

    /**
     * Sets the maximum amount of players.
     *
     * @param maxPlayers the maximum amount of players.
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * Gets the minimum amount of players.
     *
     * @return the minimum amount of players.
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Sets the minimum amount of players.
     *
     * @param minPlayers the minimum amount of players.
     */
    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    /**
     * Gets the sheep spawning center.
     *
     * @return the sheep spawning center.
     */
    public Location getSheepSpawn() {
        return sheepSpawn;
    }

    /**
     * Sets the sheep spawning center.
     *
     * @param sheepSpawn the sheep spawn radius.
     */
    public void setSheepSpawn(Location sheepSpawn) {
        this.sheepSpawn = sheepSpawn;
    }

    /**
     * Gets the players spawn.
     *
     * @return the players spawn.
     */
    public Location getPlayerSpawn() {
        return playerSpawn;
    }

    /**
     * Sets teh players spawn.
     *
     * @param playerSpawn
     */
    public void setPlayerSpawn(Location playerSpawn) {
        this.playerSpawn = playerSpawn;
    }

    /**
     * Gets the current amount of players.
     *
     * @return the current amount of players.
     */
    public int getCurrentPlayers() {
        return this.currentPlayers;
    }

    /**
     * Gets the current arena state.
     *
     * @return the current arena state.
     */
    public ArenaState getState() {
        return this.state;
    }

    /**
     * Checks if the arena exists.
     *
     * @return {@code true} if the arena existzs, {@code false} otherwise.
     */
    public boolean exists() {
        return this != null;
    }

    /**
     * PlayerData class.
     */
    class PlayerData {

        // Player name.
        private String name;

        // Player gamemode.
        private GameMode mode;

        // Flying?
        private Boolean flying;

        // Inventory.
        private ItemStack[] contents, armorContents;

        // Location.
        private Location location;

        // Food level.
        private int foodLevel;

        // Health.
        private double health;

        // Max Health.
        private double maxHealth;

        // List of current Potion Effects.
        private List<PotionEffect> effects = new ArrayList<>();

        /**
         * PlayerData constructor.
         *
         * @param p the player. The data owner.
         */
        protected PlayerData(Player p) {
            this.name = p.getName();
            this.contents = p.getInventory().getContents();
            this.armorContents = p.getInventory().getArmorContents();
            this.location = p.getLocation();
            this.mode = p.getGameMode();
            this.flying = p.isFlying();
            this.foodLevel = p.getFoodLevel();
            this.maxHealth = p.getMaxHealth();
            this.health = p.getHealth();
            for (PotionEffect effect : p.getActivePotionEffects()) {
                effects.add(effect);
                p.removePotionEffect(effect.getType());
            }
        }

        /**
         * Restores player's data.
         */
        protected void restore() {
            Player p = Bukkit.getPlayer(name);

            p.getInventory().setContents(contents);
            p.getInventory().setArmorContents(armorContents);
            p.teleport(location);
            p.setGameMode(mode);
            p.setMaxHealth(maxHealth);
            p.setHealth(health);
            p.setFoodLevel(foodLevel);
            for (PotionEffect effect : effects) {
                p.addPotionEffect(effect);
            }
            if (flying) {
                p.setAllowFlight(true);
                p.setFlying(true);
            }
        }

        /**
         * Checks if the data belongs to the Player p.
         *
         * @param p
         * @return {@code true} if the data belongs to Player p, {@code false} otherwise
         */
        protected boolean belongsTo(Player p) {
            return p.getName().equals(name);
        }

        /**
         * Get the player owning this data.
         *
         * @return the player owning this data.
         */
        public Player getPlayer() {
            for (Player p : Bukkit.getOnlinePlayers()) if (belongsTo(p)) return p;
            return null;
        }

    }


}
