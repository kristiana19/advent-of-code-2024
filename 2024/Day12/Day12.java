import java.io.*;
import java.util.*;

public class Day12 {

    public record Point(int x, int y) {}

    static String readAll() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) sb.append(s).append('\n');
        return sb.toString();
    }

    // mapa kao lista listi char-ova
    static List<List<Character>> parse(String input) {
        List<List<Character>> map = new ArrayList<>();
        for (String line : input.split("\n")) {
            if (line.isEmpty()) continue;
            List<Character> row = new ArrayList<>(line.length());
            for (char c : line.toCharArray()) row.add(c);
            map.add(row);
        }
        return map;
    }

    static boolean inBounds(List<List<Character>> map, int x, int y) {
        return y >= 0 && y < map.size() && x >= 0 && x < map.get(0).size();
    }

    static List<Point> detectRegionFromPoint(List<List<Character>> map,
                                             Set<Point> globalVisited,
                                             Point start) {
        char plant = map.get(start.y()).get(start.x());
        Deque<Point> q = new ArrayDeque<>();
        List<Point> region = new ArrayList<>();
        int[][] dirs = { {-1,0}, {0,1}, {1,0}, {0,-1} };

        q.add(start);
        region.add(start);
        while (!q.isEmpty()) {
            Point p = q.poll();
            globalVisited.add(p);

            for (int[] d : dirs) {
                int nx = p.x() + d[0], ny = p.y() + d[1];
                Point np = new Point(nx, ny);
                if (!globalVisited.contains(np)
                        && inBounds(map, nx, ny)
                        && map.get(ny).get(nx) == plant) {
                    q.add(np);
                    region.add(np);
                    globalVisited.add(np);
                }
            }
        }
        return region;
    }

    static List<List<Point>> detectRegions(List<List<Character>> map) {
        List<List<Point>> regions = new ArrayList<>();
        Set<Point> visited = new HashSet<>();
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(0).size(); x++) {
                Point p = new Point(x, y);
                if (!visited.contains(p)) {
                    regions.add(detectRegionFromPoint(map, visited, p));
                }
            }
        }
        return regions;
    }

    static long area(List<Point> region) { return region.size(); }

    static long perimeter(List<List<Character>> map, List<Point> region) {
        long per = 0;
        int[][] dirs = { {-1,0}, {0,1}, {1,0}, {0,-1} };

        for (Point p : region) {
            char plant = map.get(p.y()).get(p.x());
            for (int[] d : dirs) {
                int nx = p.x() + d[0], ny = p.y() + d[1];
                if (!inBounds(map, nx, ny) || map.get(ny).get(nx) != plant) per++;
            }
        }
        return per;
    }

    // sides (broj "stranica" kroz broj uglova: spoljašnji + unutrašnji)
    static long sides(List<List<Character>> map, List<Point> region) {
        long cnt = 0;
        char regionPlant = map.get(region.get(0).y()).get(region.get(0).x());

        // helper: da li je (x,y) u regionu (po tipu)
        java.util.function.BiFunction<Integer,Integer,Boolean> isInRegion =
                (x, y) -> inBounds(map, x, y) && map.get(y).get(x) == regionPlant;

        for (Point p : region) {
            int x = p.x(), y = p.y();

            // outer corners
            if (!isInRegion.apply(x-1, y) && !isInRegion.apply(x, y-1)) cnt++;
            if (!isInRegion.apply(x+1, y) && !isInRegion.apply(x, y-1)) cnt++;
            if (!isInRegion.apply(x-1, y) && !isInRegion.apply(x, y+1)) cnt++;
            if (!isInRegion.apply(x+1, y) && !isInRegion.apply(x, y+1)) cnt++;

            // inner corners
            if (isInRegion.apply(x-1, y) && isInRegion.apply(x, y-1) && !isInRegion.apply(x-1, y-1)) cnt++;
            if (isInRegion.apply(x+1, y) && isInRegion.apply(x, y-1) && !isInRegion.apply(x+1, y-1)) cnt++;
            if (isInRegion.apply(x-1, y) && isInRegion.apply(x, y+1) && !isInRegion.apply(x-1, y+1)) cnt++;
            if (isInRegion.apply(x+1, y) && isInRegion.apply(x, y+1) && !isInRegion.apply(x+1, y+1)) cnt++;
        }
        return cnt;
    }

    static long part1(List<List<Character>> map) {
        List<List<Point>> regions = detectRegions(map);
        long cost = 0;
        for (List<Point> r : regions) {
            cost += area(r) * perimeter(map, r);
        }
        return cost;
    }

    static long part2(List<List<Character>> map) {
        List<List<Point>> regions = detectRegions(map);
        long cost = 0;
        for (List<Point> r : regions) {
            cost += area(r) * sides(map, r);
        }
        return cost;
    }

    public static void main(String[] args) throws Exception {
        List<List<Character>> map = parse(readAll());
        System.out.println(part1(map));
        System.out.println(part2(map));
    }
}