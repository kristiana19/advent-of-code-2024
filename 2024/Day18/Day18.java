import java.io.*;
import java.util.*;

public class Day18 {

    static final int SIZE = 71;           // koordinate 0..70
    static final int GOAL_X = 70, GOAL_Y = 70;

    static String readAll() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) sb.append(s).append('\n');
        return sb.toString();
    }

    // svaka linija: "x,y"
    static List<int[]> parse(String all) {
        List<int[]> bytes = new ArrayList<>();
        for (String line : all.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] p = line.split(",");
            bytes.add(new int[]{ Integer.parseInt(p[0]), Integer.parseInt(p[1]) });
        }
        return bytes;
    }

    // true = slobodno, false = korumpirano
    static boolean[][] createMap(List<int[]> bytes, int take) {
        boolean[][] free = new boolean[SIZE][SIZE];
        for (int y = 0; y < SIZE; y++) Arrays.fill(free[y], true);
        for (int i = 0; i < take && i < bytes.size(); i++) {
            int[] b = bytes.get(i);
            free[b[1]][b[0]] = false;
        }
        return free;
    }

    // BFS najkraći put (4-smjerna). Vraća broj koraka ili -1 ako nema puta
    static int shortest(boolean[][] free) {
        if (!free[0][0] || !free[GOAL_Y][GOAL_X]) return -1;
        int[][] dist = new int[SIZE][SIZE];
        for (int[] row : dist) Arrays.fill(row, -1);
        ArrayDeque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{0,0});
        dist[0][0] = 0;

        int[][] d4 = {{1,0},{-1,0},{0,1},{0,-1}};
        while (!q.isEmpty()) {
            int[] p = q.poll();
            int x = p[0], y = p[1];
            if (x == GOAL_X && y == GOAL_Y) return dist[y][x];
            for (int[] d : d4) {
                int nx = x + d[0], ny = y + d[1];
                if (nx < 0 || ny < 0 || nx >= SIZE || ny >= SIZE) continue;
                if (!free[ny][nx]) continue;
                if (dist[ny][nx] != -1) continue;
                dist[ny][nx] = dist[y][x] + 1;
                q.add(new int[]{nx, ny});
            }
        }
        return -1;
    }

    static int part1(List<int[]> bytes) {
        boolean[][] map = createMap(bytes, 1024);
        return shortest(map);
    }

    static String part2(List<int[]> bytes) {
        for (int i = 0; i < bytes.size(); i++) {
            boolean[][] map = createMap(bytes, i + 1);
            int steps = shortest(map);
            if (steps == -1) {
                int[] b = bytes.get(i);
                return b[0] + "," + b[1];
            }
        }
        return ""; // ako se nikad ne prekine put
    }

    public static void main(String[] args) throws Exception {
        String all = readAll();
        List<int[]> bytes = parse(all);
        System.out.println(part1(bytes));    // minimalni broj koraka nakon 1024 bajta
        System.out.println(part2(bytes));    // prvo x,y gdje put više ne postoji
    }
}