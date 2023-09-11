package be.isach.musicalmobs.nms;

/**
 * Created by Sacha on 6/03/16.
 *
 * Accepted MC NMS Versions.
 */
public enum ServerVersion {

    v1_8_R3("1.8.8"),
    v1_19_R1("1.19.1"),
    v1_19_R2("1.19.3"),
    v1_19_R3("1.19.4"),
    v1_20_R1("1.20.1");

    String name;

    ServerVersion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
