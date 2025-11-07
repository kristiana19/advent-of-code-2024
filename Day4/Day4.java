import java.io.*;
import java.util.*;

public class Day4 {

    static int[] dx = { -1,  1, -1, -1,  0,  1,  1,  0 };
    static int[] dy = {  1,  1,  0, -1,  1,  0, -1, -1 };

    static List<List<Character>> parseInput(String input) {
        String[] lines = input.split("\n");
        List<List<Character>> res = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.isEmpty()) continue;
            List<Character> row = new ArrayList<>(line.length());
            for (char c : line.toCharArray()) row.add(c);
            res.add(row);
        }
        return res;
    }

    static boolean isValid(List<List<Character>> grid, int x, int y) {
        return y >= 0 && y < grid.size() && x >= 0 && x < grid.get(0).size();
    }

    static boolean findWord(List<List<Character>> grid, String word, int x, int y, int dir, int idx) {
        if (idx == word.length()) return true;
        if (!isValid(grid, x, y)) return false;
        if (grid.get(y).get(x) != word.charAt(idx)) return false;
        return findWord(grid, word, x + dx[dir], y + dy[dir], dir, idx + 1);
    }

    static int part1(List<List<Character>> grid) {
        int h = grid.size();
        int w = grid.get(0).size();
        int sum = 0;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                for (int d = 0; d < 8; d++) {
                    if (findWord(grid, "XMAS", j, i, d, 0)) sum++;
                }
            }
        }
        return sum;
    }

    static int part2(List<List<Character>> grid) {
        int h = grid.size();
        int w = grid.get(0).size();
        int sum = 0;
        
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                boolean diagDR = findWord(grid, "MAS", j, i, 1, 0) || findWord(grid, "SAM", j, i, 1, 0);   // ↘
                if (diagDR) {
                    boolean diagDL = findWord(grid, "MAS", j + 2, i, 0, 0) || findWord(grid, "SAM", j + 2, i, 0, 0); // ↙ (start s desne strane X-a)
                    if (diagDL) sum++;
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }
        List<List<Character>> grid = parseInput(sb.toString().trim());

        int p1 = part1(grid);
        int p2 = part2(grid);

        System.out.println(p1);
        System.out.println(p2);
    }
}