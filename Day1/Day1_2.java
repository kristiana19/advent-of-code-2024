import java.util.*;

public class Day1_2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        
        System.out.println("Enter numbers for the left and right lists in two columns (enter 0 0 to finish):");

        while (true) {
            
            int leftNumber = scanner.nextInt();
            int rightNumber = scanner.nextInt();

            
            if (leftNumber == 0 && rightNumber == 0) {
                break;
            }

            
            leftList.add(leftNumber);
            rightList.add(rightNumber);
        }

    
        int similarityResult = 0;
        for (Integer number : leftList) {
            int occurrenceCount = Collections.frequency(rightList, number);
            similarityResult += number * occurrenceCount;
        }

        
        System.out.println("The total similarity between the lists is: " + similarityResult);

        scanner.close();
    }
}