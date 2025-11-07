import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;

public class Day3 {
    public static void main(String[] args) {
        
        String filePath = "input.txt"; 

        try {            
            String brokenString = readFile(filePath);
            List<Integer> results = extractAndCalculate(brokenString);
            
            int sumPart1 = results.stream().reduce(0, Integer::sum); 
            int sumPart2 = results.size(); 
            
            System.out.println("Result for part 1: " + sumPart1);
            System.out.println("Result for part 2: " + sumPart2);

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

    
    private static List<Integer> extractAndCalculate(String input) {
        List<Integer> results = new ArrayList<>();
        Pattern pattern = Pattern.compile("mul\\((\\d+),\\s*(\\d+)\\)");
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1)); 
            int y = Integer.parseInt(matcher.group(2)); 
            results.add(x * y); 
        }

        return results;
    }
}