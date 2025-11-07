import java.util.*;

public class Day2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter numbers in one line (separated by spaces), type 'konec' to finish.");

        int safeReportsCount = 0;

        while (true) {
            String report = scanner.nextLine();

            if (report.equalsIgnoreCase("konec")) {
                break;
            }

            if (isSafe(report)) {
                safeReportsCount++;
            }
        }
        System.out.println("The number of safe reports is: " + safeReportsCount);
        scanner.close();
    }

    public static boolean isSafe(String report) {
       
        String[] levelStrings = report.split(" ");
        int[] levels = new int[levelStrings.length];

       
        for (int i = 0; i < levelStrings.length; i++) {
            levels[i] = Integer.parseInt(levelStrings[i]);
        }

        boolean ascending = true;
        boolean descending = true;

        for (int i = 1; i < levels.length; i++) {
            int difference = levels[i] - levels[i - 1];

            
            if (Math.abs(difference) < 1 || Math.abs(difference) > 3) {
                return false;
            }

            
            if (difference > 0) {
                descending = false;
            } else if (difference < 0) {
                ascending = false;
            }
        }

        
        return descending || ascending;
    }
}