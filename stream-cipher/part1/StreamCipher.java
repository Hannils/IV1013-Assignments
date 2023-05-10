import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
public class StreamCipher {
    public static void main(String[] args) {

        try {
            if (args.length != 3) throw new ArrayIndexOutOfBoundsException("Missing parameter from command line");
            long key = Long.parseLong(args[0]);
            Path inputFile = Paths.get(args[1]);
            File outputFile = new File(args[2]);
            FileOutputStream writer = new FileOutputStream(outputFile);
            Random prng = new Random(key);
            byte[] input = Files.readAllBytes(inputFile);
            for (var c : input) {
                writer.write(c ^ prng.nextInt(256));
            }
            writer.close();

        }catch (NumberFormatException e) {
            System.out.println("NumberFormatException thrown, invalid parameter <key>");
            System.exit(1);
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