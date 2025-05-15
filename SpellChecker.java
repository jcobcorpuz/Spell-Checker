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
        spellChecker.useDelimiter("\\s+"); // Split input by whitespace
        if(!grammarCheck(input, input.length())){
            noErrors = false;
        }
        while(spellChecker.hasNext()){
            currentCheck = spellChecker.next();
            if(!isSpecial(currentCheck)){
                if(!checkWord(currentCheck, dic)){
                    noErrors = false;
                }
            }
        }
        spellChecker.close();
        return noErrors;
    }

    public static boolean isSpecial(String input){
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher match = pattern.matcher(input);
        return match.find();
    }

    public static boolean checkWord(String input, String[] dic){
        boolean valid = false;
        int i = 0;
        while(i < dic.length && !valid){
            if(input.trim().equalsIgnoreCase((dic[i]))){
                valid = true;
            }
            i++;
        }

        if (valid) {
            return true;
        }
        System.out.println(input + " is spelt incorrectly. Did you mean: ");
        ArrayList suggestions = new ArrayList();
        i = 0;
        while(i < dic.length){
            int distance = levenshteinDistance(input.toLowerCase(), dic[i].toLowerCase());
            if(distance <= 2){
                suggestions.add(dic[i]);
            }
            i++;
        }

        if(suggestions.size() == 0){
            System.out.println("No suggestions.");
        }
        else{
            int j = 0;
            while(j < suggestions.size()){
                System.out.print(suggestions.get(j));
                if(j != suggestions.size() - 1){
                    System.out.print(", ");
                }
                else{
                    System.out.println();
                }
                j++;
            }
        }
        return false;
    }

    public static int levenshteinDistance(String a, String b){
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for(int i = 0; i <= a.length(); i++){
            for (int j = 0; j <= b.length(); j++){
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if(j ==0){
                    dp[i][j] = i;
                }
                else{
                    int cost;
                    if(a.charAt(i - 1) == b.charAt(j - 1)){
                        cost = 0;
                    }
                    else{
                        cost = 1;
                    }
                    int deletion = dp[i - 1][j] + 1;
                    int insertion = dp[i][j - 1] + 1;
                    int substitution = dp[i - 1][j - 1] + cost;
                    dp[i][j] = Math.min(Math.min(deletion, insertion), substitution);

                }
            }
        }
        return dp[a.length()][b.length()];
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
            scan.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
        String recordsArray[] = new String[records.size()];
        recordsArray = records.toArray(recordsArray);
        return recordsArray;
    }

    public static String readEssay(String filepath){
        StringBuilder sb = new StringBuilder();
        try{
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNextLine()){
                sb.append(scanner.nextLine()).append(" ");
            }
            scanner.close();
        }
        catch (Exception e){
            System.out.println("Error reading essay; " + e.getMessage());
        }
        return sb.toString().trim();
    }

    public static void main(String[] args){

        String[] wordList = readDictionary("words.txt");
        String input = readEssay("essay.txt");

        if(spellCheck(input, wordList)){
            System.out.println("No errors");
        }
        else{
            System.out.println("Errors found.");
        }
    }
}
