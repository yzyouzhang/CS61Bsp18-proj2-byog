package byog;

public class WorldGenerateParam {
    private int width;
    private int height;
    private long seed;

    public WorldGenerateParam() {
        this.width = 80;
        this.height = 30;
        this.seed = 587667;
    }

    public WorldGenerateParam(int w, int h, long seed) {
        this.width = w;
        this.height = h;
        this.seed = seed;
    }

    public int width() {
        return this.width;
    }

    public int height() {
        return this.height;
    }

    public long seed() {
        return this.seed;
    }
    public WorldGenerateParam setWorldGenerateParam(int w, int h, long s) {
        WorldGenerateParam wgp = new WorldGenerateParam();
        wgp.width = w;
        wgp.height = h;
        wgp.seed = s;
        return wgp;
    }
}
