import java.io.*;
import java.util.*;

public class Day5 {

    
    static class Parsed {
        Map<Integer, List<Integer>> rules;       
        List<List<Integer>> updates;             
        Parsed(Map<Integer, List<Integer>> r, List<List<Integer>> u) {
            this.rules = r; this.updates = u;
        }
    }

    
    static String readAll() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }

    
    static Parsed parseInput(String input) {
        Map<Integer, List<Integer>> rules = new HashMap<>();
        List<List<Integer>> updates = new ArrayList<>();

        String[] lines = input.split("\n");
        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) continue;

            if (line.contains("|")) {
                String[] parts = line.split("\\|");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                rules.computeIfAbsent(x, k -> new ArrayList<>()).add(y);
            } else {
                String[] parts = line.split(",");
                List<Integer> update = new ArrayList<>(parts.length);
                for (String p : parts) update.add(Integer.parseInt(p));
                updates.add(update);
            }
        }
        return new Parsed(rules, updates);
    }

    
    static boolean checkOrder(List<Integer> update, int index, List<Integer> mustBeAfter) {
        for (int i = index; i >= 0; i--) {
            int val = update.get(i);
            for (int r : mustBeAfter) {
                if (val == r) return false;
            }
        }
        return true;
    }

   
    static boolean checkOrderAndFix(List<Integer> update, int index, List<Integer> mustBeAfter) {
        for (int i = index; i >= 0; i--) {
            int val = update.get(i);
            for (int r : mustBeAfter) {
                if (val == r) {
                    // swap sa elementom desno od 'index' (pomakni krivi element udesno)
                    int tmp = update.get(index + 1);
                    update.set(index + 1, update.get(i));
                    update.set(i, tmp);
                    return false;
                }
            }
        }
        return true;
    }

    static int part1(Parsed parsed) {
        Map<Integer, List<Integer>> rules = parsed.rules;
        List<List<Integer>> updates = parsed.updates;

        List<Integer> okIdx = new ArrayList<>();
        for (int i = 0; i < updates.size(); i++) {
            List<Integer> u = updates.get(i);
            boolean isOrdered = true;
            for (int j = u.size() - 1; j > 0; j--) {
                int key = u.get(j);
                List<Integer> r = rules.get(key);
                if (r == null) continue;
                if (!checkOrder(u, j - 1, r)) isOrdered = false;
            }
            if (isOrdered) okIdx.add(i);
        }

        int sum = 0;
        for (int idx : okIdx) {
            List<Integer> u = updates.get(idx);
            sum += u.get(u.size() / 2);
        }
        return sum;
    }

    static int part2(Parsed parsed) {
        Map<Integer, List<Integer>> rules = parsed.rules;
        List<List<Integer>> updates = new ArrayList<>();
        for (List<Integer> u : parsed.updates) updates.add(new ArrayList<>(u));

        List<Integer> fixedIdx = new ArrayList<>();
        for (int i = 0; i < updates.size(); i++) {
            List<Integer> u = updates.get(i);
            boolean wasOrdered = true;

            for (int j = u.size() - 1; j > 0; j--) {
                int key = u.get(j);
                List<Integer> r = rules.get(key);
                if (r == null) continue;

                if (!checkOrderAndFix(u, j - 1, r)) {
                    wasOrdered = false;
                    j++; 
                }
            }
            if (!wasOrdered) fixedIdx.add(i); 
        }

        int sum = 0;
        for (int idx : fixedIdx) {
            List<Integer> u = updates.get(idx);
            sum += u.get(u.size() / 2);
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        String all = readAll();
        Parsed parsed = parseInput(all);

        int p1 = part1(parsed);
        int p2 = part2(parsed);

        System.out.println(p1);
        System.out.println(p2);
    }
}