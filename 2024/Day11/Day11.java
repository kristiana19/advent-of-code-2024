import java.io.*;
import java.util.*;

public class Day11 {

    // ključ za cache: (stone, depth)
    static record Key(long stone, int depth) {}

    static String readLine() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    static List<Long> parse(String line) {
        String[] parts = line.trim().split("\\s+");
        List<Long> out = new ArrayList<>(parts.length);
        for (String p : parts) out.add(Long.parseLong(p));
        return out;
    }

    // --- Part 1: naivna simulacija liste ---
    static List<Long> blinkNaive(List<Long> stones) {
        List<Long> next = new ArrayList<>();
        for (long x : stones) {
            if (x == 0L) {
                next.add(1L);
            } else {
                String s = Long.toString(x);
                if ((s.length() & 1) == 0) { // paran broj cifara
                    int half = s.length() / 2;
                    long left = Long.parseLong(s.substring(0, half));
                    long right = Long.parseLong(s.substring(half));
                    next.add(left);
                    next.add(right);
                } else {
                    next.add(x * 2024L);
                }
            }
        }
        return next;
    }

    static long part1(String line) {
        List<Long> stones = parse(line);
        for (int i = 0; i < 25; i++) stones = blinkNaive(stones);
        return stones.size();
    }

    // --- Part 2: brojanje pomoću memoizacije (ne gradimo listu) ---
    static Map<Key, Long> cache = new HashMap<>();

    static long countAfter(int depth, long stone) {
        Key k = new Key(stone, depth);
        Long got = cache.get(k);
        if (got != null) return got;

        long res;
        if (depth == 0) {
            res = 1L;
        } else if (stone == 0L) {
            res = countAfter(depth - 1, 1L);
        } else {
            String s = Long.toString(stone);
            if ((s.length() & 1) == 0) {
                int half = s.length() / 2;
                long left = Long.parseLong(s.substring(0, half));
                long right = Long.parseLong(s.substring(half));
                res = countAfter(depth - 1, left) + countAfter(depth - 1, right);
            } else {
                res = countAfter(depth - 1, stone * 2024L);
            }
        }

        cache.put(k, res);
        return res;
    }

    static long part2(String line) {
        List<Long> stones = parse(line);
        int blinks = 75;
        long sum = 0L;
        for (long x : stones) sum += countAfter(blinks, x);
        return sum;
    }

    public static void main(String[] args) throws Exception {
        String line = readLine();
        System.out.println(part1(line));
        System.out.println(part2(line));
    }
}