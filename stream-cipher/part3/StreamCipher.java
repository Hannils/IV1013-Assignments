import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class StreamCipher {
    public static void main(String[] args) {
        try {
            if (args.length != 3) throw new ArrayIndexOutOfBoundsException("Missing parameter from command line");
            File inputFile = new File(args[1]);
            File outputFile = new File(args[2]);
            BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            MyRandom prng = new MyRandom(args[0].getBytes());

            while (fileReader.ready()) {
                int value  = prng.next(8);
                int res = fileReader.read() ^ value;
                writer.write(res);
            }
            writer.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException thrown, invalid parameter <inputfile>");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOException thrown, invalid parameter <output> or internal error with FileWriter");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}