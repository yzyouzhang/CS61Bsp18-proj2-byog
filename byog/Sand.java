package byog;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Sand {
    private Posit posit;

    public Sand(Posit p) {
        this.posit = p;
    }

    public static void fillSand(TETile[][] world,
                                               ArrayList<Room> roomList, WorldGenerateParam wgp) {
        int number = 5;
        Random rand = new Random(wgp.seed() + 150);
        for (int i = 0; i < number; i += 1) {
            int location = rand.nextInt(roomList.size() - 1);
            Posit p = setSand(world, roomList.get(location), wgp);
            world[p.xPos()][p.yPos()] = Tileset.SAND;
        }
    }

    public static Posit setSand(TETile[][] world, Room r, WorldGenerateParam wgp) {
        Random rand = new Random(wgp.seed() + 195);
        return new Posit(r.posit().xPos() + rand.nextInt(r.width() - 2) + 1,
                r.posit().yPos() + rand.nextInt(r.height() - 2) + 1);
    }
}
