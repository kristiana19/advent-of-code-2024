import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Day7 {

    
    public static long calculate(String equation) {
        String[] parts = equation.split(": ");
        long target = Long.parseLong(parts[0]);
        String[] numbers = parts[1].split(" ");
        
        List<Long> results = new ArrayList<>();
        results.add(Long.parseLong(numbers[0]));
        
        for (int i = 1; i < numbers.length; i++) {
            List<Long> newResults = new ArrayList<>();
            long num = Long.parseLong(numbers[i]);
            
            for (long res : results) {
                newResults.add(res + num);
                newResults.add(res * num);
            }
            results = newResults;
        }
        
        return results.contains(target) ? target : 0;  
    }

    public static long readAndProcessFile(String fileName) {
        long totalCalibrationResult = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                totalCalibrationResult += calculate(line); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return totalCalibrationResult;
    }

    public static void main(String[] args) {
        String fileName = "index.txt";
        
        long totalCalibrationResult = readAndProcessFile(fileName);
        
        System.out.println("Total calibration result: " + totalCalibrationResult);
    }
}