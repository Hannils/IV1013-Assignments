import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Scanner;

public class PasswordCrack {
    public static class MyThread extends Thread {
        ArrayList<String> dictionaryList;
        String salt;
        String encryptedPassword;
        String firstname;
        String lastname;
        boolean cracked = false;

        public MyThread(ArrayList<String> dictionaryList, String salt, String encryptedPassword,
                String firstname, String lastname) {
            this.dictionaryList = dictionaryList;
            this.salt = salt;
            this.encryptedPassword = encryptedPassword;
            this.firstname = firstname;
            this.lastname = lastname;
        }

        public void run() {
            for (String word : dictionaryList) {
                //System.out.println("Testing: " + word);
                if (crack(salt, word, encryptedPassword) != null) {
                    System.out.println(word);
                    cracked = true;
                    return;
                }
            }
        }

        public boolean getCracked() {
            return cracked;
        }
    }

    public static void main(String[] args) {
        File dictionary = new File("testing.txt");//args[0]);
        File passwd = new File("passwd2.txt");//args[1]);

        try {
            Scanner passwdScanner = new Scanner(passwd);
            Scanner dictionaryScanner = new Scanner(dictionary);
            ArrayList<String> wordList = new ArrayList<>() {
                {
                    add("123456");
                    add("123456789");
                    add("111111");
                    add("qwerty");
                }
            };
            ArrayList<String> userNames = new ArrayList<>();
            ArrayList<ArrayList<String>> userList = new ArrayList<>();
            ArrayList<String> used = new ArrayList<>();
            ArrayList<MyThread> threadList = new ArrayList<>();

            while (passwdScanner.hasNextLine()) {
                var passwdLine = parsePasswd(passwdScanner.nextLine());
                userList.add(passwdLine);
                userNames.add(passwdLine.get(2));
                userNames.add(passwdLine.get(3));

            }
            for (var name : userNames) {
                wordList.add(name);
            }
            while (dictionaryScanner.hasNextLine()) {
                wordList.add(dictionaryScanner.nextLine());
            }
            ArrayList<Mangle> dictionaryList = new ArrayList<>();
            dictionaryList.add((String s) -> deleteFirst(s));
            dictionaryList.add((String s) -> deleteLast(s));
            dictionaryList.add((String s) -> reverse(s));
            //dictionaryList.add((String s) -> duplicate(s));
            //dictionaryList.add((String s) -> reflect(s));
            //dictionaryList.add((String s) -> (s.toUpperCase()));
            //dictionaryList.add((String s) -> capitalize(s));
            //dictionaryList.add((String s) -> nCapitalize(s));
            //dictionaryList.add((String s) -> prepend(s));
            //dictionaryList.add((String s) -> deleteFirst(s));

            //dictionaryList.add(wordList);
            //dictionaryList.add(deleteFirstDictionary(wordList));
            //dictionaryList.add(deleteLastDictionary(wordList));
            //dictionaryList.add(reverseDictionary(wordList));
            //dictionaryList.add(duplicateDictionary(wordList));
            //dictionaryList.add(reflectDictionary(wordList));
            //dictionaryList.add(uppercaseDictionary(wordList));
            //dictionaryList.add(capitalizeDictionary(wordList));
            //dictionaryList.add(nCapitalizeDictionary(wordList));
            //dictionaryList.add(prependDictionary(wordList));
            //dictionaryList.add(appendDictionary(wordList));
            //dictionaryList.add(toggleCaseDictionary(wordList));
            ArrayList<Boolean> cracked = new ArrayList<>();
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < userList.size(); i++) {
                cracked.set(i, false);
            }
            cracked.set(0, false);
            for (var dict : dictionaryList) {
                temp.clear();
                for (var word : wordList) {
                    temp.add(dict.mangle(word));
                }
                for (int i = 0; i < crac; i++) {
                    if (cracked[i] == false) {
                        var user = userList.get(i);
                        MyThread t = new MyThread(temp, user.get(0), user.get(1), user.get(2), user.get(3));
                        threadList.add(t);
                        t.start();
                    }
                }
                for (int i = 0; i < cracked.length; i++) {
                    threadList.get(i).join();
                    cracked[i] = threadList.get(i).getCracked();
                }
            }

        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

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

    public static String crack(String salt, String str, String hashedPassword) {
        if (jcrypt.crypt(salt, str).equals(hashedPassword))
            return str;
        return null;
    }

    public static ArrayList<String> prependDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            for (int i = 0; i < 25; i++) {
                if (i < 10)
                    list.add(prepend(word, Integer.toString(i)));
                list.add(prepend(word, Character.toString(97 + i)));
                list.add(prepend(word, Character.toString(65 + i)));
            }
        }
        return list;
    }

    public static ArrayList<String> appendDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            for (int i = 0; i < 25; i++) {
                if (i < 10)
                    list.add(append(word, Integer.toString(i)));
                list.add(append(word, Character.toString(97 + i)));
                list.add(append(word, Character.toString(65 + i)));
            }
        }
        return list;
    }

    public static ArrayList<String> deleteLastDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            if (word.length() <= 8)
                list.add(deleteLast(word));
        }
        return list;
    }

    public static ArrayList<String> reverseDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(reverse(word));
        }
        return list;
    }

    public static ArrayList<String> duplicateDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            if (word.length() < 8)
                list.add(duplicate(word));
        }
        return list;
    }

    public static ArrayList<String> reflectDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(reflect(word));
            list.add(reflectReverse(word));
        }
        return list;
    }

    public static ArrayList<String> uppercaseDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(toUpperCase(word));
        }
        return list;
    }

    public static ArrayList<String> capitalizeDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(capitalize(word));
        }
        return list;
    }

    public static ArrayList<String> nCapitalizeDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(nCapitalize(word));
        }
        return list;
    }

    public static ArrayList<String> toggleCaseDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            var tmpStr1 = word;
            var tmpStr2 = word;
            for (int i = 0; i < word.length(); i++) {
                if (i % 2 == 0) {
                    tmpStr1 = tmpStr1.substring(0, i) + Character.toUpperCase(tmpStr1.charAt(i))
                            + tmpStr1.substring(i + 1);
                } else {
                    tmpStr2 = tmpStr2.substring(0, i) + Character.toUpperCase(tmpStr2.charAt(i))
                            + tmpStr2.substring(i + 1);
                }
            }
            list.add(tmpStr1);
            list.add(tmpStr2);
        }
        return list;
    }

    public static ArrayList<String> nCapitalizeAndDeleteFirst(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(nCapitalize(deleteFirst(word)));
        }
        return list;
    }

    public static ArrayList<String> reflectAndUppercase(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(reflect(toUpperCase(word)));
            list.add(reflectReverse(toUpperCase(word)));
        }
        return list;
    }

    public static ArrayList<String> reverseAndCapitalize(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(reverse(capitalize(word)));
        }
        return list;
    }

    public static String toggleCase(String word) {
        var tmpStr1 = word;
        for (int i = 0; i < word.length(); i++) {
            if (i % 2 == 0) {
                tmpStr1 = tmpStr1.substring(0, i) + Character.toUpperCase(tmpStr1.charAt(i))
                        + tmpStr1.substring(i + 1);
            }
        }
        return tmpStr1;
    }

    public static String toggleCaseReverse(String word) {
        var tmpStr2 = word;
        for (int i = 0; i < word.length(); i++) {
            if (i % 2 != 0) {
                tmpStr2 = tmpStr2.substring(0, i) + Character.toUpperCase(tmpStr2.charAt(i))
                        + tmpStr2.substring(i + 1);
            }
        }
        return tmpStr2;
    }

    public static String toUpperCase(String word) {
        return word.toUpperCase();
    }

    public static String duplicate(String word) {
        return word + word;
    }

    public static String reverse(String word) {
        return new StringBuilder(word).reverse().toString();
    }

    public static String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static String nCapitalize(String word) {
        return word.substring(0, 1) + word.substring(1).toUpperCase();
    }

    public static String reflect(String word) {
        return word + reverse(word);
    }

    public static String reflectReverse(String word) {
        return reverse(word) + word;
    }

    public static String deleteFirst(String word) {
        return word.substring(1);
    }

    public static String deleteLast(String word) {
        return word.substring(0, word.length() - 1);
    }

    public static String append(String word, String app) {
        return word + app;
    }

    public static String prepend(String word, String pre) {
        return pre + word;
    }
}