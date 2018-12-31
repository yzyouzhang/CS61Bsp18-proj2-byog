package byog;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class LockDoor {

    public static Posit fillLockedDoor(TETile[][] world,
                                      ArrayList<Room> roomList, WorldGenerateParam wgp) {
        Posit p = chooseLockedDoor(roomList, wgp);
        world[p.xPos()][p.yPos()] = Tileset.LOCKED_DOOR;
        return p;
    }

    public static Posit chooseLockedDoor(ArrayList<Room> roomList, WorldGenerateParam wgp) {
        Random rand = new Random(wgp.seed());
        Posit door;
        if (!filterRoomList(roomList).isEmpty()) {
            door = setLockedDooer(filterRoomList(roomList).
                    get(rand.nextInt(filterRoomList(roomList).size())), wgp);
        } else {
            door = setLockedDooer(roomList.get(Room.smallestRoom(roomList)), wgp);
        }
        return door;
    }

    private static Posit setLockedDooer(Room r, WorldGenerateParam wgp) {
        Random rand = new Random(wgp.seed());
        return new Posit(r.posit().xPos() + rand.nextInt(r.width() - 2) + 1,
                r.posit().yPos());
    }

    private static ArrayList<Room> filterRoomList(ArrayList<Room> roomList) {
        ArrayList<Room> filtered = new ArrayList<>();
        for (int i = 0; i < roomList.size(); i += 1) {
            if (roomList.get(i).posit().yPos() <= 3) {
                filtered.addLast(roomList.get(i));
            }
        }
        return filtered;
    }
}
