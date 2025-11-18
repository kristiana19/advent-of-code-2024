import java.io.*;
import java.util.*;

public class Day20 {

    static String readAll() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) sb.append(s).append('\n');
        return sb.toString();
    }

    static List<List<Character>> parse(String input) {
        List<List<Character>> map = new ArrayList<>();
        for (String row : input.split("\n")) {
            if (row.isEmpty()) continue;
            List<Character> line = new ArrayList<>(row.length());
            for (char c : row.toCharArray()) line.add(c);
            map.add(line);
        }
        return map;
    }

    static boolean inBounds(List<List<Character>> map, int x, int y) {
        return y >= 0 && y < map.size() && x >= 0 && x < map.get(0).size();
    }

    // ----- jedinstvena staza: “prošetaj” od S do E (u zadatku je jedini put) -----
    // vraća: pathIndex (distanca od S), i listu pozicija po redoslijedu staze
    static class P { int x, y; P(int x,int y){this.x=x; this.y=y;} public boolean equals(Object o){ if(!(o instanceof P p)) return false; return p.x==x && p.y==y; } public int hashCode(){ return Objects.hash(x,y); } }
    static class PathData { Map<P,Integer> dist; List<P> order; PathData(Map<P,Integer>d,List<P>o){dist=d;order=o;} }

    static PathData findPath(List<List<Character>> map) {
        P start = null;
        outer:
        for (int y=0; y<map.size(); y++) {
            for (int x=0; x<map.get(0).size(); x++) {
                if (map.get(y).get(x) == 'S') { start = new P(x,y); break outer; }
            }
        }
        int[][] dirs = {{-1,0},{1,0},{0,1},{0,-1}};
        Map<P,Integer> dist = new HashMap<>();
        List<P> order = new ArrayList<>();
        P cur = start;
        while (map.get(cur.y).get(cur.x) != 'E') {
            order.add(cur);
            dist.put(cur, order.size()-1);
            for (int[] d: dirs) {
                int nx = cur.x + d[0], ny = cur.y + d[1];
                if (!inBounds(map,nx,ny)) continue;
                char c = map.get(ny).get(nx);
                P np = new P(nx,ny);
                if (!dist.containsKey(np) && (c=='.' || c=='E')) { cur = np; break; }
            }
        }
        order.add(cur);
        dist.put(cur, order.size()-1);
        return new PathData(dist, order);
    }

    // ----- Part 1: cheat do 2 poteza kroz zid -----
    static long part1(List<List<Character>> map) {
        PathData pd = findPath(map);
        Map<P,Integer> path = pd.dist;
        List<P> pathList = pd.order;

        int[][] twostep = { {2,0},{-2,0},{0,2},{0,-2} };
        int[][] mid = { {1,0},{-1,0},{0,1},{0,-1} };

        long count = 0;
        for (P cur : pathList) {
            for (int k=0;k<4;k++) {
                int nx = cur.x + twostep[k][0];
                int ny = cur.y + twostep[k][1];
                int mx = cur.x + mid[k][0];
                int my = cur.y + mid[k][1];
                if (!inBounds(map, nx, ny) || !inBounds(map, mx, my)) continue;
                if (map.get(my).get(mx) == '#' && map.get(ny).get(nx) == '.') {
                    P target = new P(nx, ny);
                    Integer tIdx = path.get(target);
                    Integer cIdx = path.get(cur);
                    if (tIdx != null && cIdx != null) {
                        int saved = cIdx - tIdx - 2; // koliko skratimo u odnosu na putanju
                        if (saved >= 100) count++;
                    }
                }
            }
        }
        return count;
    }

    // ----- Part 2: cheat do 20 poteza kroz zid -----
    // memo ključ: (pozicija, preostali_moves)
    static class Key {
        final int x,y,m;
        Key(int x,int y,int m){this.x=x;this.y=y;this.m=m;}
        public boolean equals(Object o){ if(!(o instanceof Key k)) return false; return x==k.x && y==k.y && m==k.m; }
        public int hashCode(){ return ((x*73856093) ^ (y*19349663) ^ (m*83492791)); }
    }

    // vrijednost mape: destinacija (track ćelija) -> minimalni utrošeni potezi do nje (<=20)
    static Map<Key, Map<P,Integer>> memo;

    static Map<P,Integer> getAllCheats(List<List<Character>> map, P start, P cur, int movesLeft) {
        if (!inBounds(map, cur.x, cur.y)) return Collections.emptyMap();
        Key kk = new Key(cur.x, cur.y, movesLeft);
        Map<P,Integer> cached = memo.get(kk);
        if (cached != null) return cached;

        Map<P,Integer> res = new HashMap<>();
        // ako smo na track-u (a nismo na start), ovo je validno završno mjesto cheata
        if (!(cur.x==start.x && cur.y==start.y) && map.get(cur.y).get(cur.x) != '#') {
            res.put(new P(cur.x, cur.y), 20 - movesLeft);
        }
        if (movesLeft <= 0) {
            memo.put(kk, res);
            return res;
        }
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] d: dirs) {
            P next = new P(cur.x + d[0], cur.y + d[1]);
            Map<P,Integer> m2 = getAllCheats(map, start, next, movesLeft - 1);
            for (Map.Entry<P,Integer> e : m2.entrySet()) {
                res.merge(e.getKey(), e.getValue(), Math::min); // zadrži minimalni trošak
            }
        }
        memo.put(kk, res);
        return res;
    }

    static long part2(List<List<Character>> map) {
        PathData pd = findPath(map);
        Map<P,Integer> path = pd.dist;
        List<P> pathList = pd.order;

        memo = new HashMap<>();
        long count = 0;
        for (P cur : pathList) {
            Map<P,Integer> ends = getAllCheats(map, cur, cur, 20);
            Integer cIdx = path.get(cur);
            for (Map.Entry<P,Integer> e : ends.entrySet()) {
                Integer tIdx = path.get(e.getKey());
                if (tIdx == null) continue; // završetak mora biti na stazi
                int cheatCost = e.getValue(); // koliko poteza trošimo u cheatu
                int saved = tIdx - cIdx - cheatCost;
                if (saved >= 100) count++;
            }
        }
        return count;
    }

    public static void main(String[] args) throws Exception {
        List<List<Character>> map = parse(readAll());
        System.out.println(part1(map));
        System.out.println(part2(map));
    }
}