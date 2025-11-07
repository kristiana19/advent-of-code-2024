import java.io.*;
import java.util.*;

public class Day6 {

    static class Parsed {
        boolean[][] map;    
        int startX, startY; 
        Parsed(boolean[][] m, int sx, int sy) { map = m; startX = sx; startY = sy; }
    }

    static String readAll() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line).append('\n');
        return sb.toString();
    }

    static Parsed parseInput(String input) {
        List<String> rows = new ArrayList<>();
        for (String raw : input.split("\n")) {
            String line = raw.trim();
            if (!line.isEmpty()) rows.add(line);
        }
        int h = rows.size();
        int w = rows.get(0).length();
        boolean[][] map = new boolean[h][w];
        int sx = -1, sy = -1;

        for (int y = 0; y < h; y++) {
            String line = rows.get(y);
            for (int x = 0; x < w; x++) {
                char c = line.charAt(x);
                if (c == '#') map[y][x] = true;
                else if (c == '.') map[y][x] = false;
                else if (c == '^') { map[y][x] = false; sx = x; sy = y; }
            }
        }
        return new Parsed(map, sx, sy);
    }

    static boolean out(boolean[][] map, int x, int y) {
        return x < 0 || y < 0 || y >= map.length || x >= map[0].length;
    }

    // 0=up, 1=right, 2=down, 3=left
    static int rotateRight(int o) { return (o + 1) & 3; }

    static int[] move(int x, int y, int o) {
        switch (o) {
            case 0: return new int[]{x, y - 1};
            case 1: return new int[]{x + 1, y};
            case 2: return new int[]{x, y + 1};
            case 3: return new int[]{x - 1, y};
        }
        return new int[]{x, y};
    }

    static boolean hasClear(boolean[][] map, int x, int y, int o) {
        int[] n = move(x, y, o);
        int nx = n[0], ny = n[1];
        return out(map, nx, ny) || !map[ny][nx];
    }

    static boolean inLoop(boolean[][] map, int sx, int sy, int startO) {
        int x = sx, y = sy, o = startO;
        Set<String> seen = new HashSet<>();
        seen.add(x + "," + y + "," + o);

        while (!out(map, x, y)) {
            boolean moved = false;
            if (hasClear(map, x, y, o)) {
                int[] n = move(x, y, o);
                x = n[0]; y = n[1];
                moved = true;
            } else {
                o = rotateRight(o);
            }
            if (moved) {
                String key = x + "," + y + "," + o;
                if (seen.contains(key)) return true;
                seen.add(key);
            }
        }
        return false;
    }

    // ====== Part 1 ======
    static int part1(Parsed p) {
        boolean[][] map = p.map;
        int x = p.startX, y = p.startY, o = 0; 
        Set<String> visited = new HashSet<>();

        while (!out(map, x, y)) {
            visited.add(x + "," + y);
            if (hasClear(map, x, y, o)) {
                int[] n = move(x, y, o);
                x = n[0]; y = n[1];
            } else {
                o = rotateRight(o);
            }
        }
        return visited.size();
    }

    // ====== Part 2 ======
    static int part2(Parsed p) {
        int h = p.map.length, w = p.map[0].length;
        int count = 0;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (p.map[y][x]) continue;                 
                if (x == p.startX && y == p.startY) continue;

                p.map[y][x] = true;                        
                if (inLoop(p.map, p.startX, p.startY, 0)) count++;
                p.map[y][x] = false;                       
            }
        }
        return count;
    }

    public static void main(String[] args) throws Exception {
        Parsed parsed = parseInput(readAll());
        System.out.println(part1(parsed));
        System.out.println(part2(parsed));
    }
}