package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byog.Core.Game class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Game game = new Game();
            TERenderer ter = new TERenderer();
            TETile[][] world = game.playWithInputString(args[0]);
            ter.initialize(80, 30);
            ter.renderFrame(world);
            System.out.println(game.toString());
        } else {
            Game game = new Game();
            game.playWithKeyboard();
        }
    }
}
