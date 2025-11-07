import java.io.*;
import java.util.*;

public class Day10 {

    public record Point(int x, int y) {}

    static String readAll() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) sb.append(s).append('\n');
        return sb.toString();
    }

    static List<List<Integer>> parse(String input) {
        List<List<Integer>> out = new ArrayList<>();
        for (String line : input.split("\n")) {
            if (line.isEmpty()) continue;
            List<Integer> row = new ArrayList<>(line.length());
            for (char c : line.toCharArray()) {
                if (c == '.') row.add(11);
                else row.add(c - '0');
            }
            out.add(row);
        }
        return out;
    }

    static boolean inBounds(List<List<Integer>> g, Point p) {
        return p.x() >= 0 && p.x() < g.get(0).size() && p.y() >= 0 && p.y() < g.size();
    }

    static int findPaths(List<List<Integer>> grid, Point start, List<Point> moves) {
        Queue<Point> q = new ArrayDeque<>();
        Set<Point> vis = new HashSet<>();
        q.add(start);

        int count = 0;
        while (!q.isEmpty()) {
            Point cur = q.poll();
            if (!vis.contains(cur) && grid.get(cur.y()).get(cur.x()) == 9) {
                count++;
                vis.add(cur);
                continue;
            }
            vis.add(cur);

            int ch = grid.get(cur.y()).get(cur.x());
            for (Point mv : moves) {
                Point nxt = new Point(cur.x() + mv.x(), cur.y() + mv.y());
                if (inBounds(grid, nxt)
                        && grid.get(nxt.y()).get(nxt.x()) - ch == 1
                        && !vis.contains(nxt)) {
                    q.add(nxt);
                }
            }
        }
        return count;
    }

    static int findPathRating(List<List<Integer>> grid, Point start, List<Point> moves) {
        Queue<Point> q = new ArrayDeque<>();
        Set<Point> vis = new HashSet<>();
        q.add(start);

        int count = 0;
        while (!q.isEmpty()) {
            Point cur = q.poll();
            if (grid.get(cur.y()).get(cur.x()) == 9) {
                count++;
                continue;
            }
            vis.add(cur);

            int ch = grid.get(cur.y()).get(cur.x());
            for (Point mv : moves) {
                Point nxt = new Point(cur.x() + mv.x(), cur.y() + mv.y());
                if (inBounds(grid, nxt)
                        && grid.get(nxt.y()).get(nxt.x()) - ch == 1
                        && !vis.contains(nxt)) {
                    q.add(nxt);
                }
            }
        }
        return count;
    }

    static int part1(List<List<Integer>> grid) {
        List<Point> moves = Arrays.asList(new Point(-1,0), new Point(1,0), new Point(0,1), new Point(0,-1));
        int sum = 0;
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(0).size(); x++) {
                if (grid.get(y).get(x) == 0) {
                    sum += findPaths(grid, new Point(x,y), moves);
                }
            }
        }
        return sum;
    }

    static int part2(List<List<Integer>> grid) {
        List<Point> moves = Arrays.asList(new Point(-1,0), new Point(1,0), new Point(0,1), new Point(0,-1));
        int sum = 0;
        for (int y = 0; y < grid.size(); y++) {
            for (int x = 0; x < grid.get(0).size(); x++) {
                if (grid.get(y).get(x) == 0) {
                    sum += findPathRating(grid, new Point(x,y), moves);
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        List<List<Integer>> grid = parse(readAll());
        System.out.println(part1(grid));
        System.out.println(part2(grid));
    }
}