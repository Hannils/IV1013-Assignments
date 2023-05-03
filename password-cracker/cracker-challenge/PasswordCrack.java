import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PasswordCrack {
    private static ArrayList<String> userNames;
    private static ArrayList<ArrayList<String>> userList;
    private static ArrayList<String> temp;
    private static CrackingThread[] threadList;
    private static boolean[] cracked;
    private static ArrayList<IMangle> manglerList;
    private static Scanner passwdScanner;
    private static Scanner dictionaryScanner;
    private static ArrayList<String> wordList;

    public static void main(String[] args) {
        try {
            File dictionary = new File(args[0]);
            File passwd = new File(args[1]);
            userNames = new ArrayList<>();
            userList = new ArrayList<>();
            temp = new ArrayList<>();
            manglerList = new ArrayList<>();
            passwdScanner = new Scanner(passwd);
            dictionaryScanner = new Scanner(dictionary);
            wordList = new ArrayList<>();

            readPasswdFile();
            populateWordList();
            cracked = new boolean[userList.size()];
            threadList = new CrackingThread[userList.size()];
            populateMangleList();

            for (var mangler : manglerList) {
                temp.clear();
                for (var word : wordList) {
                    temp.add(mangler.mangle(word));
                }
                for (int i = 0; i < cracked.length; i++) {
                    if (cracked[i] == false) {
                        var user = userList.get(i);
                        CrackingThread t = new CrackingThread(temp, user.get(0), user.get(1), user.get(2), user.get(3));
                        threadList[i] = t;
                        t.start();
                    }
                }
                for (int i = 0; i < threadList.length; i++) {
                    threadList[i].join();
                    cracked[i] = threadList[i].isCracked();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("toString(): " + e.toString());
            System.out.println("getMessage(): " + e.getMessage());
            System.out.println("StackTrace(): ");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function for parsing the input retrieved from the entered passwd file
     * 
     * @param line - Entire line read from passwd
     * @return ArrayList of {Salt, Hash, Firstname, Lastname}
     */
    public static ArrayList<String> parsePasswd(String line) {
        var lines = line.split(":");
        var splitUserInfo = lines[4].split(" ");
        return new ArrayList<String>() {
            {
                add(lines[1].substring(0, 2));
                add(lines[1]);
                add(lines[0]);
                add(splitUserInfo[splitUserInfo.length - 1].toLowerCase());
            }
        };
    }

    /**
     * Function to fill the wordList used for password cracking
     */
    public static void populateWordList() {
        wordList.add("123456");
        wordList.add("1234567");
        wordList.add("111111");
        wordList.add("123456789");
        wordList.add("1234567890");
        for (var name : userNames) {
            wordList.add(name);
        }
        readDictionary();
    }

    /**
     * Function to read input from entered dictionary
     */
    public static void readDictionary() {
        while (dictionaryScanner.hasNextLine()) {
            wordList.add(dictionaryScanner.nextLine());
        }
    }

    public static void readPasswdFile() {
        while (passwdScanner.hasNextLine()) {
            var passwdLine = parsePasswd(passwdScanner.nextLine());
            userList.add(passwdLine);
            userNames.add(passwdLine.get(2));
            userNames.add(passwdLine.get(3));
        }
    }

    public static void populateMangleList() {
        
        manglerList.add((String word) -> word); // Normal
        manglerList.add((String word) -> word.substring(1)); // Delete first
        manglerList.add((String word) -> word.substring(0, word.length() - 1)); // Delete last
        manglerList.add((String word) -> new StringBuilder(word).reverse().toString()); // Reverse the string
        manglerList.add((String word) -> (word + word)); // Duplicate the string
        manglerList.add((String word) -> word + new StringBuilder(word).reverse().toString()); // Reflect the string
        manglerList.add((String word) -> (new StringBuilder(word).reverse().toString() + word)); // Reflect reverse the string
        manglerList.add((String word) -> (word.toUpperCase())); // Uppercase the string
        manglerList.add((String word) -> word.substring(0, 1).toUpperCase() + word.substring(1)); // Capitalize the string
        manglerList.add((String word) -> word.substring(0, 1) + word.substring(1).toUpperCase()); // nCapitalize the string
        manglerList.add((String word) -> toggleCase(word)); // Togglecase even
        manglerList.add((String word) -> toggleCaseReverse(word)); // Togglecase odd

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                for (int k = 0; k < 12; k++) {
                    if (i != j && k != j && k != i) {
                        manglerList.add(new MangleCombiner(new IMangle[]{manglerList.get(i), manglerList.get(j), manglerList.get(k)}));
                    }
                }
            }
        }

        //for (int i = 0; i < 10; i++) {
        //    manglerList.add(new ManglePrepend(Integer.toString(i))); // Prepend all numbers
        //    manglerList.add(new MangleAppend(Integer.toString(i))); // Append all number
        //}
        //for (int i = 0; i < 25; i++) {
        //    manglerList.add(new MangleAppend(Character.toString(97 + i))); // Append all lowercase
        //    manglerList.add(new MangleAppend(Character.toString(65 + i))); // Append all uppercase
        //    manglerList.add(new ManglePrepend(Character.toString(97 + i))); // Prepend all lowercase
        //    manglerList.add(new ManglePrepend(Character.toString(65 + i))); // Prepend all uppercase
        //}




    
        






    }

    public static String toggleCase(String word) {
        var tmpStr = word;
        for (int i = 0; i < word.length(); i++) {
            if (i % 2 == 0) {
                tmpStr = tmpStr.substring(0, i) + Character.toUpperCase(tmpStr.charAt(i))
                        + tmpStr.substring(i + 1);
            }
        }
        return tmpStr;
    }

    public static String toggleCaseReverse(String word) {
        var tmpStr = word;
        for (int i = 0; i < word.length(); i++) {
            if (i % 2 != 0) {
                tmpStr = tmpStr.substring(0, i) + Character.toUpperCase(tmpStr.charAt(i))
                        + tmpStr.substring(i + 1);
            }
        }
        return tmpStr;
    }
}