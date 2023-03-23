import java.util.Random;

public class MyRandom extends Random {
    int[] S = new int[256];
    int i = 0;
    int j = 0;

    public MyRandom(byte[] seed) {
        setSeed(seed);
    }

    @Override
    /**
     * Code recieved by lecture: SymmetricKeyEncryption Iv5.pdf p.48
     */
    public int next(int bits) {
        this.i = (this.i + 1) % 256;
        this.j = (this.j + S[this.i]) % 256;
        swapValues(S, i, j);
        return S[(S[this.i] + S[this.j]) % 256];
    }

    /**
     * Code recieved by lecture: SymmetricKeyEncryption Iv5.pdf p.47
     */
    public void setSeed(byte[] seed) {
        for (int i = 0; i < 256; i++) S[i] = i;
        for (int i = 0, j = 0; i < 256; i++) {
            j = (j + S[i] + seed[i % seed.length]) % 256;
            swapValues(S, i, j);
        }
        this.i = 0;
        this.j = 0;
    }

    public void swapValues(int[] arr, int x, int y) {
        int temp = S[x];
        S[x] = S[y];
        S[y] = temp;
    }

    public static void main(String[] args) {
        var prng = new MyRandom("Hello World!".getBytes());
        for (int i = 0; i < 30; i++) {
            System.out.println(prng.next(8));
        }
    }
}
