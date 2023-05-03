import java.io.File;
import java.io.FileNotFoundException;
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
                if (crack(salt, word, encryptedPassword) != null) {
                    System.out.println(word + " for " + firstname + " " + lastname);
                    cracked = true;
                    break;
                }
            }
        }

        public boolean isCracked() {
            return cracked;
        }
    }

    public static void main(String[] args) {
        File dictionary = new File(args[0]);
        File passwd = new File(args[1]);

        try {
            Scanner passwdScanner = new Scanner(passwd);
            Scanner dictionaryScanner = new Scanner(dictionary);
            ArrayList<String> wordList = new ArrayList<>()
            {
                {
                    add("123456");
                    add("1234567");
                    add("111111");
                    add("123456789");
                    add("1234567890");
                }
            };
            ArrayList<String> userNames = new ArrayList<>();
            ArrayList<ArrayList<String>> userList = new ArrayList<>();
            MyThread[] threadList;
            boolean[] cracked;

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
            ArrayList<ArrayList<String>> dictionaryList = new ArrayList<>();
            dictionaryList.add(wordList);
            dictionaryList.add(deleteFirstDictionary(wordList));
            dictionaryList.add(deleteLastDictionary(wordList));
            dictionaryList.add(reverseDictionary(wordList));
            dictionaryList.add(duplicateDictionary(wordList));
            dictionaryList.add(reflectDictionary(wordList));
            dictionaryList.add(uppercaseDictionary(wordList));
            dictionaryList.add(capitalizeDictionary(wordList));
            dictionaryList.add(nCapitalizeDictionary(wordList));
            dictionaryList.add(toggleCaseDictionary(wordList));
            dictionaryList.add(prependDictionary(wordList));
            dictionaryList.add(appendDictionary(wordList));
            cracked = new boolean[userList.size()];
            threadList = new MyThread[userList.size()];
            for (int i = 0; i < cracked.length; i++) {
                cracked[i] = false;
            }

            for (var dict : dictionaryList) {
                for (int i = 0; i < cracked.length; i++) {
                    if (cracked[i] == false) {
                        var user = userList.get(i);
                        MyThread t = new MyThread(dict, user.get(0), user.get(1), user.get(2), user.get(3));
                        threadList[i] = t;
                        t.start();
                    }
                }

                for (int i = 0; i < threadList.length; i++) {
                    threadList[i].join();
                    cracked[i] = threadList[i].isCracked();
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

    public static String crack(String salt, String str, String encryptedPassword) {
        if (jcrypt.crypt(salt, str).equals(encryptedPassword))
            return str;
        return null;
    }

    public static ArrayList<String> prependDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            for (int i = 0; i < 10; i++)
                list.add(Integer.toString(i) + word);
            for (int i = 0; i < 25; i++) {
                var lower = Character.toString(97 + i);
                var upper = Character.toString(65 + i);
                list.add(lower + word);
                list.add(upper + word);
            }
        }
        return list;
    }

    public static ArrayList<String> appendDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            for (int i = 0; i < 10; i++)
                list.add(word + Integer.toString(i));
            for (int i = 0; i < 25; i++) {
                var lower = Character.toString(97 + i);
                var upper = Character.toString(65 + i);
                list.add(word + lower);
                list.add(word + upper);
            }
        }
        return list;
    }

    public static ArrayList<String> deleteFirstDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(word.substring(1, word.length()));
        }
        return list;
    }

    public static ArrayList<String> deleteLastDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(word.substring(0, word.length() - 1));
        }
        return list;
    }

    public static ArrayList<String> reverseDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(new StringBuilder(word).reverse().toString());
        }
        return list;
    }

    public static ArrayList<String> duplicateDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(word + word);
        }
        return list;
    }

    public static ArrayList<String> reflectDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(word + new StringBuilder(word).reverse().toString());
            list.add(new StringBuilder(word).reverse().toString() + word);
        }
        return list;
    }

    public static ArrayList<String> uppercaseDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(word.toUpperCase());
        }
        return list;
    }

    public static ArrayList<String> capitalizeDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(word.substring(0, 1).toUpperCase() + word.substring(1));
        }
        return list;
    }

    public static ArrayList<String> nCapitalizeDictionary(ArrayList<String> dictionary) {
        var list = new ArrayList<String>();
        for (String word : dictionary) {
            list.add(word.substring(0, 1) + word.substring(1).toUpperCase());
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