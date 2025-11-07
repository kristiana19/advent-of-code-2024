import java.util.*;

public class Day1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        
        List<Integer> leftList = new ArrayList<>(); 
        List<Integer> rightList = new ArrayList<>(); 

        System.out.println("Enter elements in the format: 'left right'. To finish input, press Enter on an empty line:");

        
        while (sc.hasNextLine()) { 
            String line = sc.nextLine().trim(); 
            if (line.isEmpty()) { 
                break; 
            }

            String[] numbers = line.split("\\s+"); 
            if (numbers.length == 2) { 
                leftList.add(Integer.parseInt(numbers[0])); 
                rightList.add(Integer.parseInt(numbers[1])); 
            } else { 
                System.out.println("Invalid input! Please enter two numbers separated by a space.");
            }
        }

        
        int[] left = leftList.stream().mapToInt(Integer::intValue).toArray(); 
        int[] right = rightList.stream().mapToInt(Integer::intValue).toArray(); 

        
        int distance = calculateDistance(left, right); 

        
        System.out.println("The total distance between the collections is: " + distance); 
    }

    
    public static int calculateDistance(int[] left, int[] right) {
        Arrays.sort(left); 
        Arrays.sort(right); 

        int distance = 0; 
        for (int i = 0; i < left.length; i++) { 
            distance += Math.abs(left[i] - right[i]); 
        }

        return distance; 
    }
}