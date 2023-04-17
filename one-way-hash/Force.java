import java.math.BigInteger;
import java.security.*;

public class Force {
    public static void main(String[] args) {
        String[] strs = new String[] { "IV1013 security", "Security is fun", "Yes, indeed", "Secure IV1013", "No way" };
        // String[] strs = new String[] { "IV1013 security" };

        String digestAlgorithm = "SHA-256";
        String textEncoding = "UTF-8";
        try {
            for (int i = 0; i < strs.length; i++) {
                System.out.println("Now brute forcing " + strs[i]);
                int count = 0;
                MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
                byte[] inputBytes = strs[i].getBytes(textEncoding);
                md.update(inputBytes);
                byte[] digest = md.digest();
                while (true) {
                    count++;
                    md.update(Integer.toString(count).getBytes());
                    byte[] brute = md.digest();
                    if (brute[0] == digest[0] && brute[1] == digest[1] && brute[2] == digest[2]) {
                        break;
                    }
                }

                System.out.println("Number of trials to break " + strs[i] + ": " + count);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algorithm \"" + digestAlgorithm + "\" is not available");
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }
    }
}