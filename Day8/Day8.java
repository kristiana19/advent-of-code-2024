import java.io.*;
import java.util.*;

public class Day8 {

    static int W = -1, H = -1;

    static long key(int x, int y) { return (((long) y) << 32) ^ (x & 0xffffffffL); }

    static String readAll() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line).append('\n');
        return sb.toString();
    }

    static Map<Character, List<int[]>> parse(String input) {
        Map<Character, List<int[]>> out = new HashMap<>();
        String[] raw = input.split("\n");
        H = 0;
        for (String s : raw) { if (!s.isEmpty()) { W = s.length(); break; } }

        for (int y = 0; y < raw.length; y++) {
            String line = raw[y];
            if (line.isEmpty()) continue;
            H++;

            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                if (c == '.') continue;
                out.computeIfAbsent(c, k -> new ArrayList<>()).add(new int[]{x, y});
            }
        }
        return out;
    }

    static boolean inBounds(int x, int y) {
        return x >= 0 && x < W && y >= 0 && y < H;
    }

    // === Part 1: za par antena A(a) i A(b) vrati točno dva antinoda (a - v) i (b + v), v = b - a
    static long[] antiNodes2(int ax, int ay, int bx, int by) {
        if (ay > by) { int tx = ax, ty = ay; ax = bx; ay = by; bx = tx; by = ty; }

        int dx = Math.abs(ax - bx);
        int dy = Math.abs(ay - by);

        int nx1, ny1, nx2, ny2;
        if (ax < bx) { // v = (+dx, +dy)
            nx1 = ax - dx; ny1 = ay - dy; // a - v
            nx2 = bx + dx; ny2 = by + dy; // b + v
        } else {        // v = (-dx, +dy)
            nx1 = ax + dx; ny1 = ay - dy;
            nx2 = bx - dx; ny2 = by + dy;
        }
        return new long[]{ key(nx1, ny1), key(nx2, ny2) };
    }

    // === Part 2 ===
    static void antiNodesAll(int ax, int ay, int bx, int by, Set<Long> out) {
        if (ay > by) { int tx = ax, ty = ay; ax = bx; ay = by; bx = tx; by = ty; }
        int dx = Math.abs(ax - bx);
        int dy = Math.abs(ay - by);

        int x1 = ax, y1 = ay;
        int x2 = bx, y2 = by;

        while (inBounds(x1, y1) || inBounds(x2, y2)) {
            if (inBounds(x1, y1)) out.add(key(x1, y1));
            if (inBounds(x2, y2)) out.add(key(x2, y2));

            if (ax < bx) { // v = (+dx, +dy)
                x1 -= dx; y1 -= dy; // minus v
                x2 += dx; y2 += dy; // plus v
            } else {       // v = (-dx, +dy)
                x1 += dx; y1 -= dy;
                x2 -= dx; y2 += dy;
            }
        }
    }

    static int part1(Map<Character, List<int[]>> map) {
        Set<Long> uniq = new HashSet<>();

        for (Map.Entry<Character, List<int[]>> e : map.entrySet()) {
            List<int[]> ants = e.getValue();
            for (int i = 0; i < ants.size() - 1; i++) {
                for (int j = i + 1; j < ants.size(); j++) {
                    int[] a = ants.get(i), b = ants.get(j);
                    long[] k = antiNodes2(a[0], a[1], b[0], b[1]);
                    int x1 = (int) k[0]; // packed; need to unpack:
                    int y1 = (int) (k[0] >> 32);
                    int x2 = (int) k[1];
                    int y2 = (int) (k[1] >> 32);
                    if (inBounds(x1, y1)) uniq.add(k[0]);
                    if (inBounds(x2, y2)) uniq.add(k[1]);
                }
            }
        }
        return uniq.size();
    }

    static int part2(Map<Character, List<int[]>> map) {
        Set<Long> uniq = new HashSet<>();
        for (Map.Entry<Character, List<int[]>> e : map.entrySet()) {
            List<int[]> ants = e.getValue();
            for (int i = 0; i < ants.size() - 1; i++) {
                for (int j = i + 1; j < ants.size(); j++) {
                    int[] a = ants.get(i), b = ants.get(j);
                    antiNodesAll(a[0], a[1], b[0], b[1], uniq);
                }
            }
        }
        return uniq.size();
    }

    public static void main(String[] args) throws Exception {
        String all = readAll();
        Map<Character, List<int[]>> map = parse(all);
        System.out.println(part1(map));
        System.out.println(part2(map));
    }
}