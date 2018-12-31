package byog;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class CrazyWorld implements Serializable {
    private Posit lockedDoor;
    private Posit player;
    private TETile[][] world;

    public CrazyWorld(Posit l, Posit p, TETile[][] w) {
        this.lockedDoor = l;
        this.player = p;
        this.world = w;
    }

    public Posit lockedDoor() {
        return this.lockedDoor;
    }

    public Posit player() {
        return this.player;
    }

    public TETile[][] world() {
        return this.world;
    }
}
