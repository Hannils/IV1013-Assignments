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


    public static void main(String[] args) {
        var prng = new Random(Long.parseLong("123123123123123"));
        long startTime = System.nanoTime();
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                prng.nextInt(2);
            }
        }
        long endTime = System.nanoTime();
        System.out.println(endTime - startTime);
    }
}
