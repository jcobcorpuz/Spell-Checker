import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpellChecker {
    public static boolean spellCheck(String input, String[] dic){
        String currentCheck = "";
        boolean noErrors = true;
        Scanner spellChecker = new Scanner(input);
        spellChecker.useDelimiter("\\s+"); //space
        if(!grammarCheck(input, input.length())){
            noErrors = false;
        }
        while(spellChecker.hasNext()){
            currentCheck = spellChecker.next();
            if(!isSpecial(currentCheck)){
                if(!checkWord(currentCheck, dic)){
                    System.out.println(currentCheck + " is spelt incorrectly");
                    noErrors = false;
                }
            }
        }
        return noErrors;
    }

    public static boolean isSpecial(String input){
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(input);
        return match.find();
    }

    public static boolean checkWord(String input, String[] dic){
        boolean valid = false;
        int length = dic.length;
        int i = 0;
        while(!valid && i < length){
            if(input.trim().equalsIgnoreCase((dic[i]))){
                valid = true;
                if (input.trim().equals("I")){
                    valid = true;
                }
                else if(input.trim().equals("i")){
                    valid = false;
                }
            }
            i++;
        }
        return valid;
    }

    public static boolean grammarCheck(String input, int length){
         boolean validGrammar = true;
         int lastCharacter = length - 1;
         if (input.charAt(lastCharacter) != '.'){
             System.out.println("Missing fullstop at the end of the sentence");
             validGrammar = false;
         }
         if (!Character.isUpperCase(input.charAt(0))){
             System.out.println("Must start with an uppercase character or number");
             validGrammar = false;
         }
         return validGrammar;
    }

    public static String[] readDictionary(String filepath){
        ArrayList<String> records = new ArrayList<>();
        try{
            Scanner scan;
            scan = new Scanner(new File(filepath));
            scan.useDelimiter("[,\n]");

            while(scan.hasNext()){
                records.add(scan.next());
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        String recordsArray[] = new String[records.size()];
        recordsArray = records.toArray(recordsArray);
        return recordsArray;
    }

    public static void main(String[] args){

        String[] wordList = readDictionary("words.txt");


        String input = "Greetins it is Jacob here nd I am greeting you.";

        if(spellCheck(input, wordList)){
            System.out.println("No errors");
        }
        else{
            System.out.println("Errors");
        }
    }
}
