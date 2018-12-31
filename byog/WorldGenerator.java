package byog;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class WorldGenerator {
    public static final int MAXROOM = 36;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static void addCorner(TETile[][] world, Posit corner) {
        for (int i = corner.xPos() - 1; i <= corner.xPos() + 1; i += 1) {
            for (int j = corner.yPos() - 1; j <= corner.yPos() + 1; j += 1) {
                world[i][j] = Tileset.WALL;
            }
        }
        world[corner.xPos()][corner.yPos()] = Tileset.FLOOR;
    }

    public static ArrayList<Room> drawRoom(TETile[][] tiles,
                                           int roomNumber, WorldGenerateParam wgp) {
        Random rand = new Random(wgp.seed() + 10);
        int maxW = 4;
        int maxH = 5;

        ArrayList<Room> roomList = new ArrayList<>();
        for (int i = 0; i < roomNumber; i += 1) {
            int roomWidth = rand.nextInt(maxW) + 3;
            int roomHeight = rand.nextInt(maxH) + 3;
            int roomPx = rand.nextInt(WIDTH - roomWidth);
            int roomPy = rand.nextInt(HEIGHT - roomHeight);
            Posit startP = new Posit(roomPx, roomPy);
            Room.addRoom(tiles, startP, roomWidth, roomHeight);
            Room newRoom = new Room(startP, roomWidth, roomHeight);
            roomList.addLast(newRoom);
        }
        return roomList;
    }

    public static Hallway drawLWay(TETile[][] tiles, Room r1, Room r2, WorldGenerateParam wgp) {
        Random rand = new Random(wgp.seed() + 100);

        Posit p1 = Room.innerRand(r1, wgp);
        Posit p2 = Room.innerRand(r2, wgp);
        //System.out.println("add one："+p2.xPos+", "+p1.xPos);
        int key = rand.nextInt(2);
        switch (key) {
            case 0: {  //draw horizontal way first.
                Posit horizontalStart = Posit.smallerX(p1, p2);
                Posit horCornerPt = Hallway.addHorizontalHallway(tiles, horizontalStart,
                        Math.abs(p2.xPos() - p1.xPos()));
                Posit verticalStart = Posit.smallerY(horCornerPt, Posit.largerX(p1, p2));
                Hallway.addVerticalHallway(tiles, verticalStart, Math.abs(p2.yPos() - p1.yPos()));
                addCorner(tiles, horCornerPt);
                return new Hallway(horCornerPt, horizontalStart, Posit.largerX(p1, p2), 0);
            }
            case 1: { //draw vertical way first.
                Posit verticalStart = Posit.smallerY(p1, p2);
                Posit verCornerPt = Hallway.addVerticalHallway(tiles, verticalStart,
                        Math.abs(p2.yPos() - p1.yPos()));
                Posit horizontalStart = Posit.smallerX(verCornerPt, Posit.largerY(p1, p2));
                Hallway.addHorizontalHallway(tiles, horizontalStart,
                        Math.abs(p2.xPos() - p1.xPos()));
                addCorner(tiles, verCornerPt);
                return new Hallway(verCornerPt, verticalStart, Posit.largerY(p1, p2), 1);
            }
            default: {
                return null;
            }
        }
    }

    public static Posit fillPlayer(TETile[][] world,
                                      ArrayList<Room> roomList, WorldGenerateParam wgp) {
        Random rand = new Random(wgp.seed() + 1234);
        Posit p = Room.innerRand(roomList.get(roomList.size()
                - rand.nextInt(roomList.size() - 1) - 1), wgp);
        if (p.xPos() == LockDoor.chooseLockedDoor(roomList, wgp).xPos()) {
            p = Room.innerRand(roomList.get(rand.nextInt(roomList.size() - 3) + 3), wgp);
        }
        world[p.xPos()][p.yPos()] = Tileset.PLAYER;
        return p;
    }

    public static CrazyWorld generate(WorldGenerateParam wgp) {
        long se = wgp.seed();
        Random rand = new Random(se + 1584);

        TETile[][] randomTiles = new TETile[WIDTH][HEIGHT];
        // initialize tiles
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                randomTiles[x][y] = Tileset.NOTHING;
            }
        }

        int roomNumber = rand.nextInt(MAXROOM) + 8;
        ArrayList<Room> roomList = drawRoom(randomTiles, roomNumber, wgp);
        roomList = Room.sortRoomList(roomList);

        ArrayList<Hallway> hallwayList = new ArrayList<>();
        for (int i = 0; i < roomList.size() - 1; i += 1) {
            hallwayList.addLast(drawLWay(randomTiles, roomList.get(i), roomList.get(i + 1), wgp));
        }
        Room.fillRoomList(randomTiles, roomList);
        Hallway.fillLHallwayList(randomTiles, hallwayList);
        Posit lockedDoor = LockDoor.fillLockedDoor(randomTiles, roomList, wgp);
        Posit player = fillPlayer(randomTiles, roomList, wgp);
        Flower.fillFlower(randomTiles, hallwayList, wgp);
        Sand.fillSand(randomTiles, roomList, wgp);
        CrazyWorld cw = new CrazyWorld(lockedDoor, player, randomTiles);
        return cw;
    }
}
