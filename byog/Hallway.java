package byog;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Hallway {
    Posit corner;
    Posit start;
    Posit end;
    int key;

    public Hallway(Posit c, Posit s, Posit e, int k) {
        this.corner = c;
        this.start = s;
        this.end = e;
        this.key = k;
    }

    public static Posit addVerticalHallway(TETile[][] world, Posit p, int h) {
        for (int y = 0; y < h; y += 1) {
            world[p.xPos() - 1][p.yPos() + y] = Tileset.WALL;
            world[p.xPos() + 1][p.yPos() + y] = Tileset.WALL;
        }
        return new Posit(p.xPos(), p.yPos() + h);
    }

    public static Posit addHorizontalHallway(TETile[][] world, Posit p, int w) {
        for (int x = 0; x < w; x += 1) {
            //System.out.println("world: "+world.length+" w: "+w+" p.x: "+p.xPos+" p.y: "+p.yPos);
            world[p.xPos() + x][p.yPos() - 1] = Tileset.WALL;
            world[p.xPos() + x][p.yPos() + 1] = Tileset.WALL;
        }
        return new Posit(p.xPos() + w, p.yPos());
    }

    private static void fillVerticalHallway(TETile[][] world, Posit p, int h) {
        for (int y = 0; y < h; y += 1) {
            world[p.xPos()    ][p.yPos() + y] = Tileset.FLOOR;
        }
    }

    private static void fillHorizontalHallway(TETile[][] world, Posit p, int w) {
        for (int x = 0; x < w; x += 1) {
            world[p.xPos() + x][p.yPos()    ] = Tileset.FLOOR;
        }
    }

    private static void fillLHallway(TETile[][] world, Hallway hw) {
        switch (hw.key) {
            case 0: {
                fillHorizontalHallway(world, hw.start, hw.corner.xPos() - hw.start.xPos() + 1);
                fillVerticalHallway(world, Posit.smallerY(hw.corner, hw.end),
                        Math.abs(hw.corner.yPos() - hw.end.yPos()) + 1);
                break;
            }
            case 1: {
                fillVerticalHallway(world, hw.start, hw.corner.yPos() - hw.start.yPos() + 1);
                fillHorizontalHallway(world, Posit.smallerX(hw.corner, hw.end),
                        Math.abs(hw.corner.xPos() - hw.end.xPos()) + 1);
                break;
            }
            default: break;
        }
    }

    public static void fillLHallwayList(TETile[][] world, ArrayList<Hallway> hallwayList) {
        for (int i = 0; i < hallwayList.size(); i += 1) {
            fillLHallway(world, hallwayList.get(i));
        }
    }
}
