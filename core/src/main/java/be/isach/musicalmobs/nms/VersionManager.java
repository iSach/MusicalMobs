package be.isach.musicalmobs.nms;

import be.isach.musicalmobs.MusicalMobs;
import be.isach.musicalmobs.nms.*;

public class VersionManager {
    /**
     * If the version of Bukkit/Spigot is 1.13.
     */
    // public static boolean IS_VERSION_1_13 = Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14");
    private final String PACKAGE = "be.isach.musicalmobs";
    private IModule module;
    private final ServerVersion serverVersion;
    private IEntityUtil entityUtil;

    public VersionManager(ServerVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    @SuppressWarnings("unchecked")
    public void load() throws ReflectiveOperationException {
        module = loadModule("Module");
        entityUtil = loadModule("EntityUtil");
    }

    @SuppressWarnings("unchecked")
    private <T> T loadModule(String name) throws ReflectiveOperationException {
        return (T) ReflectionUtils
                .instantiateObject(Class.forName(PACKAGE + "." + serverVersion + "." + name));
    }

    public IEntityUtil getEntityUtil() {
        return entityUtil;
    }

    public IModule getModule() {
        return module;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }
}

