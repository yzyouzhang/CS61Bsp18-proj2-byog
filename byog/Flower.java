package byog;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Flower {
    private Posit posit;

    public Flower(Posit p) {
        this.posit = p;
    }

    public static void fillFlower(TETile[][] world,
                                      ArrayList<Hallway> hallwayList, WorldGenerateParam wgp) {
        int number = 3;
        Random rand = new Random(wgp.seed());
        for (int i = 0; i < number; i += 1) {
            int location = rand.nextInt(hallwayList.size() - 1);
            Posit p = setFlower(hallwayList.get(location), wgp);
            world[p.xPos()][p.yPos()] = Tileset.FLOWER;
        }
    }

    public static Posit setFlower(Hallway h, WorldGenerateParam wgp) {
        Random rand = new Random(wgp.seed());
        if (h.start.xPos() == h.corner.xPos()) {
            return new Posit(h.start.xPos(), h.start.yPos()
                    + rand.nextInt(h.corner.yPos() - h.start.yPos() - 1) + 1);
        } else {
            return new Posit(h.start.xPos()
                    + rand.nextInt(h.corner.xPos() - h.start.xPos() - 1) + 1, h.start.yPos());
        }
    }
}
