package byog;

import java.io.Serializable;

public class Posit implements Serializable {
    private int xPos;
    private int yPos;

    public Posit(int xP, int yP) {
        this.xPos = xP;
        this.yPos = yP;
    }

    public int xPos() {
        return this.xPos;
    }

    public int yPos() {
        return this.yPos;
    }

    public static Posit smallerX(Posit p1, Posit p2) {
        if (p1.xPos < p2.xPos) {
            return p1;
        } else {
            return p2;
        }
    }

    public static Posit largerX(Posit p1, Posit p2) {
        if (p1.xPos < p2.xPos) {
            return p2;
        } else {
            return p1;
        }
    }

    public static Posit smallerY(Posit p1, Posit p2) {
        if (p1.yPos < p2.yPos) {
            return p1;
        } else {
            return p2;
        }
    }

    public static Posit largerY(Posit p1, Posit p2) {
        if (p1.yPos < p2.yPos) {
            return p2;
        } else {
            return p1;
        }
    }
}
