import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

public class Day3_2 {
    public static void main(String[] args) {
        
        String filePath = "input.txt"; 

        try {            
            String brokenString = readFile(filePath);            
            int total = processInstructions(brokenString);
            System.out.println("Result: " + total);

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    
    private static int processInstructions(String input) {
        Pattern mulPattern = Pattern.compile("mul\\((\\d+),\\s*(\\d+)\\)");
        Pattern doPattern = Pattern.compile("do\\(\\)");
        Pattern dontPattern = Pattern.compile("don't\\(\\)");

        Matcher matcher;
        boolean enabled = true; 
        int totalSum = 0;

       
        for (int i = 0; i < input.length(); ) {
            String subInput = input.substring(i);

            matcher = doPattern.matcher(subInput);
            if (matcher.find() && matcher.start() == 0) {
                enabled = true;
                i += matcher.end();
                continue;
            }

            matcher = dontPattern.matcher(subInput);
            if (matcher.find() && matcher.start() == 0) {
                enabled = false;
                i += matcher.end();
                continue;
            }
            
            matcher = mulPattern.matcher(subInput);
            if (matcher.find() && matcher.start() == 0) {
                if (enabled) {
                    int x = Integer.parseInt(matcher.group(1));
                    int y = Integer.parseInt(matcher.group(2));
                    totalSum += x * y;
                }
                i += matcher.end();
                continue;
            }
            i++;
        }
        return totalSum;
    }
}