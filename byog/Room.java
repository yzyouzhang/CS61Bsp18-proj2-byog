package byog;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class Room {
    private Posit posit;
    private int width;
    private int height;

    public Room(Posit p, int w, int h) {
        this.posit = p;
        this.width = w;
        this.height = h;
    }

    public Posit posit() {
        return this.posit;
    }

    public int height() {
        return this.height;
    }

    public int width() {
        return this.width;
    }

    public static void addRoom(TETile[][] world, Posit p, int w, int h) {
        for (int x = 0; x < w; x += 1) {
            //System.out.println("addRoomFunction: xPos:"+p.xPos+",ypos:"+p.yPos);
            world[p.xPos() + x][p.yPos()] = Tileset.WALL;
            world[p.xPos() + x][p.yPos() + h - 1] = Tileset.WALL;
        }
        for (int y = 0; y < h; y += 1) {
            world[p.xPos()][p.yPos() + y] = Tileset.WALL;
            world[p.xPos() + w - 1][p.yPos() + y] = Tileset.WALL;
        }
    }

    public static Posit innerRand(Room r, WorldGenerateParam wgp) {
        Random rand = new Random(wgp.seed());
        int innerX = rand.nextInt(r.width - 2) + r.posit.xPos() + 1;
        int innerY = rand.nextInt(r.height - 2) + r.posit.yPos() + 1;
        Posit innerPosit = new Posit(innerX, innerY);
        return innerPosit;
    }

    private static void fillRoom(TETile[][] world, Room r) {
        for (int x = 1; x < r.width - 1; x += 1) {
            for (int y = 1; y < r.height - 1; y += 1) {
                world[r.posit.xPos() + x][r.posit.yPos() + y] = Tileset.FLOOR;
            }
        }
    }

    public static void fillRoomList(TETile[][] world, ArrayList<Room> roomList) {
        for (int i = 0; i < roomList.size(); i += 1) {
            fillRoom(world, roomList.get(i));
        }
    }

    public static ArrayList<Room> sortRoomList(ArrayList<Room> roomList) {
        ArrayList<Room> newRoomList = new ArrayList<>();
        int roomListSize = roomList.size();
        for (int i = 0; i < roomListSize; i += 1) {
            int minRoom = smallestRoom(roomList);
            newRoomList.addLast(roomList.remove(minRoom));
        }
        return newRoomList;
    }

    public static int smallestRoom(ArrayList<Room> roomList) {
        int min = 110; int minIndex = 0;
        for (int i = 0; i < roomList.size(); i += 1) {
            int positSum = roomList.get(i).posit.xPos() + roomList.get(i).posit.yPos();
            if (positSum < min) {
                min = positSum;
                minIndex = i;
            }
        }
        return minIndex;
    }

}
