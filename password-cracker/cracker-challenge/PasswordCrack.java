import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class PasswordCrack {
    public static void main(String[] args) {
        File dictionary = new File(args[0]);
        File passwd = new File(args[1]);

        try {
            Scanner passwdScanner = new Scanner(passwd);
            Scanner dictionaryScanner = new Scanner(dictionary);
            ArrayList<String> wordList = new ArrayList<>();
            ArrayList<String> userNames = new ArrayList<>();
            ArrayList<ArrayList<String>> userList = new ArrayList<>();

            while (passwdScanner.hasNextLine()) {
                var passwdLine = parsePasswd(passwdScanner.nextLine());
                userList.add(passwdLine);
                userNames.add(passwdLine.get(2));
                userNames.add(passwdLine.get(3));

            }
            while (dictionaryScanner.hasNextLine()) {
                wordList.add(dictionaryScanner.nextLine());
            }
            for (var name : userNames) {
                wordList.add(name);
            }
            ArrayList<ArrayList<String>> dictionaryList = new ArrayList<>();
            dictionaryList.add(wordList);
            dictionaryList.add(prependDictionary(wordList));
            dictionaryList.add(appendDictionary(wordList));
            dictionaryList.add(deleteFirstDictionary(wordList));
            dictionaryList.add(deleteLastDictionary(wordList));
            dictionaryList.add(reverseDictionary(wordList));
            dictionaryList.add(duplicateDictionary(wordList));
            dictionaryList.add(reflectDictionary(wordList));
            dictionaryList.add(uppercaseDictionary(wordList));
            dictionaryList.add(capitalizeDictionary(wordList));
            dictionaryList.add(nCapitalizeDictionary(wordList));
            dictionaryList.add(toggleCaseDictionary(wordList));

            //dictionaryList.add(prependDictionary(dictionaryList.get(0)));
            //dictionaryList.add(appendDictionary(dictionaryList.get(0)));
            //dictionaryList.add(deleteFirstDictionary(dictionaryList.get(0)));
            //dictionaryList.add(deleteLastDictionary(dictionaryList.get(0)));
            //dictionaryList.add(reverseDictionary(dictionaryList.get(0)));
            //dictionaryList.add(duplicateDictionary(dictionaryList.get(0)));
            //dictionaryList.add(reflectDictionary(dictionaryList.get(0)));
            //dictionaryList.add(uppercaseDictionary(dictionaryList.get(0)));
            //dictionaryList.add(capitalizeDictionary(dictionaryList.get(0)));
            //dictionaryList.add(nCapitalizeDictionary(dictionaryList.get(0)));
            //dictionaryList.add(toggleCaseDictionary(dictionaryList.get(0)));
//
            //dictionaryList.add(prependDictionary(dictionaryList.get(1)));
            //dictionaryList.add(appendDictionary(dictionaryList.get(1)));
            //dictionaryList.add(deleteFirstDictionary(dictionaryList.get(1)));
            //dictionaryList.add(deleteLastDictionary(dictionaryList.get(1)));
            //dictionaryList.add(reverseDictionary(dictionaryList.get(1)));
            //dictionaryList.add(duplicateDictionary(dictionaryList.get(1)));
            //dictionaryList.add(reflectDictionary(dictionaryList.get(1)));
            //dictionaryList.add(uppercaseDictionary(dictionaryList.get(1)));
            //dictionaryList.add(capitalizeDictionary(dictionaryList.get(1)));
            //dictionaryList.add(nCapitalizeDictionary(dictionaryList.get(1)));
            //dictionaryList.add(toggleCaseDictionary(dictionaryList.get(1)));


            for (var user : userList) {
                for (var dic : dictionaryList) {
                    for (String word : dic) {
                        if (crack(user.get(0), word, user.get(1)) != null) {
                            System.out.println("Password found: " + word + " for user: " + user.get(2) + " " + user.get(3) );
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> parsePasswd(String line) {
        var lines = line.split(":");
        var encrypted = lines[1];
        var splitUserInfo = lines[4].split(" ");
        return new ArrayList<String>() { 
            { 
                add(encrypted.substring(0, 2)); 
                add(encrypted);
                add(lines[0]);
                add(splitUserInfo[splitUserInfo.length - 1]);
            } 
        };
    }


    public static String crack(String salt, String str, String encryptedPassword) {
            //System.out.println("Testing: " + str);
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
            list.add(word.substring(0,1).toUpperCase() + word.substring(1));
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
                    tmpStr1 = tmpStr1.substring(0, i) + Character.toUpperCase(tmpStr1.charAt(i)) + tmpStr1.substring(i+1);
                } else {
                    tmpStr2 = tmpStr2.substring(0, i) + Character.toUpperCase(tmpStr2.charAt(i)) + tmpStr2.substring(i+1);
                }
            }
            list.add(tmpStr1);
            list.add(tmpStr2);
        }
        return list;
    }
}