import java.util.ArrayList;

public class CrackingThread extends Thread {
    private ArrayList<String> dictionaryList;
    private String salt;
    private String encryptedPassword;
    private boolean cracked = false;

    public CrackingThread(ArrayList<String> dictionaryList, String salt, String encryptedPassword) {
        this.dictionaryList = dictionaryList;
        this.salt = salt;
        this.encryptedPassword = encryptedPassword;
    }

    public void run() {
        for (String word : dictionaryList) {
            if (crack(salt, word, encryptedPassword) != null) {
                System.out.println(word);
                cracked = true;
                break;
            }
        }
    }

    public boolean isCracked() {
        return cracked;
    }

    public static String crack(String salt, String str, String encryptedPassword) {
        if (jcrypt.crypt(salt, str).equals(encryptedPassword))
            return str;
        return null;
    }
}
