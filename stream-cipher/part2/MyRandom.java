import java.util.Random;

public class MyRandom extends Random {
    long seed;
    long m = 117970101840314287L;
    int a = 7;
    int b = 3;

    public MyRandom() {
        super();
    }

    public MyRandom(long seed) {
        super(seed);
    }

    @Override
    public int next(int bits) {
        long range = ((long) Math.pow(2, bits));
        this.seed = Math.abs((a * seed + b)) % m;
        return (int) (this.seed % range);
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }
}
