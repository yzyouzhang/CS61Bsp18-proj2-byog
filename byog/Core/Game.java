package byog.Core;

import byog.CrazyWorld;
import byog.Posit;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.WorldGenerateParam;
import byog.WorldGenerator;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Game {
    private TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private static long SEED;
    private static final int MENUW = 40;
    private static final int MENUH = 60;
    private boolean gameOver;
    private int health;
    private int sandNumber;
    private String s;
    private int timeCounter;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        showBlank();
        showMenu();
        StdDraw.text(MENUW / 2, MENUH / 4, "Press your choice please: ");
        StdDraw.show();
        health = 5;
        sandNumber = 0;
        timeCounter = 60;
        StdDraw.enableDoubleBuffering();
        while (true) {
            s = "";
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            s += key;
            StdDraw.enableDoubleBuffering();
            StdDraw.clear(Color.BLACK);
            showMenu();
            StdDraw.text(MENUW / 2, MENUH / 4, "Press your choice please: " + s);
            StdDraw.show();
            switch (key) {
                case ('n'):
                case ('N'): {
                    String sd = "";
                    String trueSeed = "";
                    char c = 'l';
                    StdDraw.clear(Color.BLACK);
                    showMenu();
                    StdDraw.text(MENUW / 2, MENUH / 4,
                            "Now please input a seed, then press 's' to start the game.");
                    StdDraw.show();
                    do {
                        if (!StdDraw.hasNextKeyTyped()) {
                            continue;
                        }
                        c = StdDraw.nextKeyTyped();
                        if (c >= 48 && c <= 57) {
                            trueSeed += String.valueOf(c);
                        }
                        sd += String.valueOf(c);
                        if (c != 's') {
                            StdDraw.clear(Color.BLACK);
                            showMenu();
                            StdDraw.text(MENUW / 2, MENUH / 4, "Your seed is: " + sd);
                            StdDraw.show();
                        }
                    } while (c != 's');

                    SEED = getStringtoNum(trueSeed);
                    StdDraw.pause(500);
                    System.out.println("## Game final SEED: " + SEED);

                    WorldGenerateParam wgp = new WorldGenerateParam(80, 30, SEED);
                    ter.initialize(wgp.width(), wgp.height());
                    CrazyWorld cw = WorldGenerator.generate(wgp);
                    ter.renderFrame(cw.world());
                    playGame(cw);
                    break;
                }
                case ('l'):
                case ('L'): {
                    CrazyWorld cw = loadCrazyWorld();
                    ter.initialize(80, 30);
                    ter.renderFrame(cw.world());
                    gameOver = false;
                    playGame(cw);
                    break;
                }
                case ('q'):
                case ('Q'): {
                    gameOver = true;
                    System.exit(0);
                    break;
                }
                default:
            }
        }
    }

    private void playGame(CrazyWorld cw) {

        new Thread(() -> {
            while (timeCounter > 0) {
                StdDraw.enableDoubleBuffering();

                timeCounter--;
                //long hh = timeCounter / 60 / 60 % 60;
                //long mm = timeCounter / 60 % 60;
                //long ss = timeCounter % 60;
                //System.out.println("left + hh + "hours" + mm + "minutes" + ss + "seconds");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        char key;
        String record = "";
        while (!gameOver) {
            mousePointer(cw);
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            key = StdDraw.nextKeyTyped();
            record += key;
            if (health == 0 || timeCounter == 0) {
                gameOver = true;
                health = 5;
                sandNumber = 0;
                //System.out.println("You lose!");
                showBlank();
                drawFrame("Sorry! You lose!");
                StdDraw.pause(5000);
                break;
            } else if ((cw.world()[cw.player().xPos()][cw.player().yPos() - 1].equals(Tileset
                    .LOCKED_DOOR)) && health >= 5 && sandNumber >= 2 && timeCounter > 0) {
                gameOver = true;
                health = 5;
                sandNumber = 5;
                showBlank();
                drawFrame("Congratulation! You win!");
                StdDraw.pause(5500);
                break;
            }
            //System.out.println(record);
            for (int i = 0; i < record.length() - 1; i += 1) {
                if ((record.charAt(i) == ':' && record.charAt(i + 1) == 'q')
                        || (record.charAt(i) == ':' && record.charAt(i + 1) == 'Q')) {
                    saveCrazyWorld(cw);
                    showBlank();
                    drawFrame("Your game has been saved!");
                    StdDraw.pause(3000);
                    gameOver = true;
                }
            }
            cw = move(cw, key);
        }
        showBlank();
        drawFrame("Have an another try next time!");
        StdDraw.pause(5000);
    }

    private void mousePointer(CrazyWorld cw) {
        int mx = (int) StdDraw.mouseX();
        int my = (int) StdDraw.mouseY();
        if (cw.world()[mx][my].equals(Tileset.LOCKED_DOOR)) {
            ter.renderFrame(cw.world());
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "This is a door "
                    + "where you can escape from this crazy world!");
        } else if (cw.world()[mx][my].equals(Tileset.WALL)) {
            ter.renderFrame(cw.world());
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "This is a wall where you can't go, "
                    + "otherwise you'll lose one life!");
        } else  if (cw.world()[mx][my].equals(Tileset.PLAYER)) {
            ter.renderFrame(cw.world());
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "You, the player!");
        } else if (cw.world()[mx][my].equals(Tileset.FLOOR)) {
            ter.renderFrame(cw.world());
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "Floor!");
        } else if (cw.world()[mx][my].equals(Tileset.FLOWER)) {
            ter.renderFrame(cw.world());
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "Flower! You can eat it to add health value!");
        } else if (cw.world()[mx][my].equals(Tileset.SAND)) {
            ter.renderFrame(cw.world());
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "Sand! You need to clean it to win the game!");
        } else {
            ter.renderFrame(cw.world());
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH / 2, 1, "Nothing!");
        }
        StdDraw.text(WIDTH  * 4 / 5, HEIGHT - 1,
                "Your Health Value: " + Integer.toString(health));
        StdDraw.text(WIDTH / 2, HEIGHT - 1,
                "Time Left: " + Integer.toString(timeCounter));
        StdDraw.text(WIDTH  * 1 / 5, HEIGHT  - 1,
                "Sand You have cleaned: " + Integer.toString(sandNumber));
        //StdDraw.text(WIDTH  * 1 / 5, HEIGHT * 3 / 5, s);
        StdDraw.show();
    }

    public void drawFrame(String str) {
        int midWidth = MENUW / 2;
        int midHeight = MENUH / 2;
        StdDraw.clear();
        StdDraw.clear(Color.black);
        // Draw the actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, str);
        StdDraw.show();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        char playMode = input.charAt(0);
        System.out.println("MODE：" + playMode);
        return enterGame(playMode, input);
    }

    private CrazyWorld move(CrazyWorld cw, char key) {
        TETile upper = cw.world()[cw.player().xPos()][cw.player().yPos() + 1];
        TETile lower = cw.world()[cw.player().xPos()][cw.player().yPos() - 1];
        TETile right = cw.world()[cw.player().xPos() + 1][cw.player().yPos()];
        TETile left = cw.world()[cw.player().xPos() - 1][cw.player().yPos()];
        switch (key) {
            case ('w'):
            case ('W'): {
                if (upper.equals(Tileset.WALL)) {
                    health -= 1;
                    return cw;
                } else {
                    if (upper.equals(Tileset.FLOWER)) {
                        health += 1;
                    } else if (upper.equals(Tileset.SAND)) {
                        sandNumber += 1;
                    }
                    cw.world()[cw.player().xPos()][cw.player().yPos() + 1] = Tileset.PLAYER;
                    cw.world()[cw.player().xPos()][cw.player().yPos()] = Tileset.FLOOR;
                    Posit newPlayer = new Posit(cw.player().xPos(), cw.player().yPos() + 1);
                    return new CrazyWorld(cw.lockedDoor(), newPlayer, cw.world());
                }
            }
            case ('s'):
            case ('S'): {
                if (lower.equals(Tileset.WALL)) {
                    health -= 1;
                    return cw;
                } else if (lower.equals(Tileset.LOCKED_DOOR)) {
                    gameOver = true;
                    return cw;
                } else {
                    if (lower.equals(Tileset.FLOWER)) {
                        health += 1;
                    } else if (lower.equals(Tileset.SAND)) {
                        sandNumber += 1;
                    }
                    cw.world()[cw.player().xPos()][cw.player().yPos() - 1] = Tileset.PLAYER;
                    cw.world()[cw.player().xPos()][cw.player().yPos()] = Tileset.FLOOR;
                    Posit newPlayer = new Posit(cw.player().xPos(), cw.player().yPos() - 1);
                    return new CrazyWorld(cw.lockedDoor(), newPlayer, cw.world());
                }
            }
            case ('a'):
            case ('A'): {
                if (left.equals(Tileset.WALL)) {
                    health -= 1;
                    return cw;
                } else {
                    if (left.equals(Tileset.FLOWER)) {
                        health += 1;
                    } else if (left.equals(Tileset.SAND)) {
                        sandNumber += 1;
                    }
                    cw.world()[cw.player().xPos() - 1][cw.player().yPos()] = Tileset.PLAYER;
                    cw.world()[cw.player().xPos()][cw.player().yPos()] = Tileset.FLOOR;
                    Posit newPlayer = new Posit(cw.player().xPos() - 1, cw.player().yPos());
                    return new CrazyWorld(cw.lockedDoor(), newPlayer, cw.world());
                }
            }
            case ('d'):
            case ('D'): {
                if (right.equals(Tileset.WALL)) {
                    health -= 1;
                    return cw;
                } else {
                    if (right.equals(Tileset.FLOWER)) {
                        health += 1;
                    } else if (right.equals(Tileset.SAND)) {
                        sandNumber += 1;
                    }
                    cw.world()[cw.player().xPos() + 1][cw.player().yPos()] = Tileset.PLAYER;
                    cw.world()[cw.player().xPos()][cw.player().yPos()] = Tileset.FLOOR;
                    Posit newPlayer = new Posit(cw.player().xPos() + 1, cw.player().yPos());
                    return new CrazyWorld(cw.lockedDoor(), newPlayer, cw.world());
                }
            } default: return cw;
        }
    }

    private TETile[][] enterGame(char mode, String input) {
        //pick the initial character to be playMode choice.
        switch (mode) {
            case ('n'):
            case ('N'): {
                SEED = getStringtoNum(input);
                WorldGenerateParam wgp = new WorldGenerateParam(WIDTH, HEIGHT, SEED);
                //ter.initialize(wgp.width(), wgp.height());
                CrazyWorld cw = WorldGenerator.generate(wgp);
                //ter.renderFrame(cw.world());

                int start = 1;
                for (int i = 0; i < input.length(); i += 1) {
                    if (input.charAt(i) == 's' || input.charAt(i) == 'S') {
                        start = i + 1;
                        break;
                    }
                }
                for (int i = start; i < input.length(); i += 1) {
                    cw = move(cw, input.charAt(i));
                    if ((input.charAt(i) == ':' && input.charAt(i + 1) == 'q')
                        || (input.charAt(i) == ':' && input.charAt(i + 1) == 'Q')) {
                        gameOver = true;
                        saveCrazyWorld(cw);
                        System.out.println("Saved");
                        break;
                    }
                }
                return cw.world();
            }
            case ('l'):
            case ('L'): {
                //load game.
                CrazyWorld cw = loadCrazyWorld();
                int start = 1;
                for (int i = 0; i < input.length(); i += 1) {
                    if (input.charAt(i) == 's' || input.charAt(i) == 'S') {
                        start = i + 1;
                        break;
                    }
                }
                for (int i = start; i < input.length(); i += 1) {
                    if ((input.charAt(i) == ':' && input.charAt(i + 1) == 'q')
                            || (input.charAt(i) == ':' && input.charAt(i + 1) == 'Q')) {
                        gameOver = true;
                        saveCrazyWorld(cw);
                        System.out.println("Saved");
                        break;
                    }
                    cw = move(cw, input.charAt(i));
                }
                return cw.world();
            }
            case ('q'):
            case ('Q'): {
                gameOver = true;
                TETile[][] world = new TETile[80][30];
                for (TETile[] x : world) {
                    for (TETile y : x) {
                        y = Tileset.NOTHING;
                    }
                }
                return world;
            } default: {
                gameOver = true;
                TETile[][] world = new TETile[80][30];
                for (TETile[] x : world) {
                    for (TETile y : x) {
                        y = Tileset.NOTHING;
                    }
                }
                return world;
            }
        }
    }

    private void showBlank() {
        gameOver = false; //initialize the settings

        StdDraw.clear();
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(MENUW * 16, MENUH * 16);
        Font font = new Font("Monaco", Font.BOLD, 100);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, MENUW);
        StdDraw.setYscale(0, MENUH);
        StdDraw.clear(Color.BLACK);
    }

    private void showMenu() {
        // Draw the GUI
        Font title = new Font("Monaco", Font.BOLD, 25);
        Font mainMenu = new Font("Monaco", Font.PLAIN, 16);
        StdDraw.setFont(title);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(MENUW / 2, MENUH * 2 / 3, "==== CS61B proj2: Cool Game! ====");
        StdDraw.setFont(mainMenu);
        StdDraw.text(MENUW / 2, MENUH * 5.5 / 10, "New Game (n / N)");
        StdDraw.text(MENUW / 2, MENUH * 4.5 / 10, "Load Game (l / L)");
        StdDraw.text(MENUW / 2, MENUH * 3.5 / 10, "Quit (q / Q)");
        //StdDraw.show();
    }

    /* abstract the number in Strings */
    private long getStringtoNum(String str) {
        str = str.trim();
        String str2 = "";
        if (!"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                    str2 = str2 + str.charAt(i);
                }
            }
        }
        return Long.parseLong(str2);
    }

    private static CrazyWorld loadCrazyWorld() {
        File f = new File("./crazyWorld.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (CrazyWorld) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        /* In the case no World has been saved yet, we return a new one. */
        return WorldGenerator.generate(new WorldGenerateParam(80, 30, 567));
    }

    private static void saveCrazyWorld(CrazyWorld cw) {
        File f = new File("./crazyWorld.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(cw);
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }
}
