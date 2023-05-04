import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.MalformedInputException;
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
            File dictionary = new File(args[0]);
            File passwd = new File(args[1]);
            userNames = new ArrayList<>();
            userList = new ArrayList<>();
            temp = new ArrayList<>();
            manglerList = new ArrayList<>();
            try {
                passwdScanner = new Scanner(passwd);
            } catch (FileNotFoundException e) {
                System.out.println();
                System.out.println("<passwd>: No such file or directory");
                System.out.println("File entered: " + args[1]);
                System.out.println();
                System.exit(0);
            }
            try {
                dictionaryScanner = new Scanner(dictionary);
            } catch (FileNotFoundException e) {
                System.out.println();
                System.out.println("<dictionary>: No such file or directory");
                System.out.println("File entered: " + args[0]);
                System.out.println();

                System.exit(0);
            }
            wordList = new ArrayList<>();
            try {
                readPasswdFile();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println();
                System.out.println("Malformed passwd file:");
                System.out.println("    Something went wrong with parsing the passwd file: " + args[1]);
                System.out.println("    Make sure the file follows the following format: account:encrypted password data:uid:gid:GCOS-field:homedir:shell");
                System.out.println();
                System.exit(0);
            } catch (IllegalArgumentException e) {
                System.out.println();
                System.out.println("Malformed passwd file:");
                System.out.println("    Something went wrong with parsing the passwd file: " + args[1]);
                System.out.println("    Make sure the file follows the following format: account:encrypted password data:uid:gid:GCOS-field:homedir:shell");
                System.out.println("    " + e.getMessage());
                System.out.println();
                System.exit(0);
            } catch (OutOfMemoryError e) {
                System.out.println();
                System.out.println("<passwd>: Input file too large");
                System.out.println();
            }
            prePopulateWordList();
            try {
                readDictionary();
            } catch (OutOfMemoryError e) {
                System.out.println();
                System.out.println("<dictionary>: Input file too large");
                System.out.println();
            }

            cracked = new boolean[userList.size()];
            threadList = new CrackingThread[userList.size()];
            populateMangleList();

            for (var mangler : manglerList) {
                temp.clear();
                for (var word : wordList) {
                    if (word.length() == 0) continue;
                    try {
                        temp.add(mangler.mangle(word));
                    } catch (OutOfMemoryError e) {
                        System.out.println();
                        System.out.println("Memory ran out during cracking. Please try smaller files.");
                        System.out.println();
                    }
                }
                for (int i = 0; i < cracked.length; i++) {
                    if (cracked[i] == false) {
                        var user = userList.get(i);
                        CrackingThread t = new CrackingThread(temp, user.get(0), user.get(1));
                        threadList[i] = t;
                        t.start();
                    }
                }
                for (int i = 0; i < threadList.length; i++) {
                    try {
                        threadList[i].join();
                    } catch (InterruptedException e) {
                        System.out.println("Thread interruption occurred");
                        System.exit(0);
                    }
                    cracked[i] = threadList[i].isCracked();
                }
            }
    }

    /**
     * Function for parsing the input retrieved from the entered passwd file
     * 
     * @param line - Entire line read from passwd
     * @return ArrayList of {Salt, Hash, Firstname, Lastname}
     * @throws MalformedInputException
     */
    public static ArrayList<String> parsePasswd(String line) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        var lines = line.split(":");
        if (lines.length != 7) throw new IllegalArgumentException("Wrong number of fields on line: " + line);
        if (lines[1].length() != 13) throw new IllegalArgumentException("Length of hash did not correspond to 13 characters on line: " + line); 
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
    public static void prePopulateWordList() {
        wordList.add("123456");
        wordList.add("1234567");
        wordList.add("111111");
        wordList.add("123456789");
        wordList.add("1234567890");
        for (var name : userNames) {
            wordList.add(name);
        }
    }

    /**
     * Function to read input from entered dictionary
     */
    public static void readDictionary() {
        while (dictionaryScanner.hasNextLine()) {
            wordList.add(dictionaryScanner.nextLine());
        }
    }

    public static void readPasswdFile() throws ArrayIndexOutOfBoundsException {
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
        manglerList.add((String word) -> replaceWithZero(word));

        for (int i = 0; i < 10; i++) {
            manglerList.add(new ManglePrepend(Integer.toString(i))); // Prepend all numbers
            manglerList.add(new MangleAppend(Integer.toString(i))); // Append all number
        }
        for (int i = 0; i < 25; i++) {
            manglerList.add(new MangleAppend(Character.toString(97 + i))); // Append all lowercase
            manglerList.add(new MangleAppend(Character.toString(65 + i))); // Append all uppercase
            manglerList.add(new ManglePrepend(Character.toString(97 + i))); // Prepend all lowercase
            manglerList.add(new ManglePrepend(Character.toString(65 + i))); // Prepend all uppercase
        }

        int numberOfManglers = manglerList.size();
        
        // Perform all possible combinations of 2 manglers
        for (int i = 1; i < numberOfManglers; i++) {
            for (int j = 1; j < numberOfManglers; j++) {
                if (i != j) {
                    manglerList.add(new MangleCombiner(
                            new IMangle[] { manglerList.get(i), manglerList.get(j) }));
                }
            }
        }

        // Perform all possible combinations of 3 manglers
        for (int i = 1; i < numberOfManglers; i++) {
            for (int j = 1; j < numberOfManglers; j++) {
                for (int k = 1; k < numberOfManglers; k++) {
                    if (i != j && k != j && k != i) {
                        manglerList.add(new MangleCombiner(
                                new IMangle[] { manglerList.get(i), manglerList.get(j), manglerList.get(k) }));
                    }
                }
            }
        }
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

    public static String replaceWithZero(String word) {
                word = word.replace('o', '0');
                word = word.replace('O', '0');
                return word;
    }
}