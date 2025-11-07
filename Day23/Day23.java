import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Day23 {

    static String readAll() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) sb.append(s).append('\n');
        return sb.toString();
    }

    static Map<String, Set<String>> parse(String input) {
        Map<String, Set<String>> g = new HashMap<>();
        for (String line : input.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] ab = line.split("-");
            String a = ab[0], b = ab[1];
            g.computeIfAbsent(a, k -> new HashSet<>()).add(b);
            g.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }
        return g;
    }

    //Part 1
    static int part1(Map<String, Set<String>> g) {
        List<String> nodes = new ArrayList<>(g.keySet());
        Collections.sort(nodes);
        Set<List<String>> triangles = new HashSet<>();

        int n = nodes.size();
        for (int i = 0; i < n; i++) {
            String u = nodes.get(i);
            Set<String> Nu = g.get(u);
            for (int j = i + 1; j < n; j++) {
                String v = nodes.get(j);
                if (!Nu.contains(v)) continue;
                for (int k = j + 1; k < n; k++) {
                    String w = nodes.get(k);
                    if (g.get(v).contains(w) && Nu.contains(w)) {
                        List<String> tri = Arrays.asList(u, v, w);
                        triangles.add(tri);
                    }
                }
            }
        }

        int countWithT = 0;
        for (List<String> tri : triangles) {
            if (tri.get(0).charAt(0) == 't' ||
                tri.get(1).charAt(0) == 't' ||
                tri.get(2).charAt(0) == 't') {
                countWithT++;
            }
        }
        return countWithT;
    }

    //Part 2 
    static Set<String> largestClique(Map<String, Set<String>> g) {
        List<String> all = new ArrayList<>(g.keySet());
        Set<String> best = new HashSet<>();

        Deque<State> stack = new ArrayDeque<>();
        stack.push(new State(new HashSet<>(), new HashSet<>(all), new HashSet<>()));

        while (!stack.isEmpty()) {
            State st = stack.pop();
            if (st.P.isEmpty() && st.X.isEmpty()) {
                if (st.R.size() > best.size()) best = new HashSet<>(st.R);
                continue;
            }
            List<String> cand = new ArrayList<>(st.P);
            for (String v : cand) {
                Set<String> Nv = g.get(v);
                Set<String> R2 = new HashSet<>(st.R); R2.add(v);
                Set<String> P2 = st.P.stream().filter(Nv::contains).collect(Collectors.toSet());
                Set<String> X2 = st.X.stream().filter(Nv::contains).collect(Collectors.toSet());
                stack.push(new State(R2, P2, X2));

                st.P.remove(v);
                st.X.add(v);
            }
        }
        return best;
    }

    static class State {
        Set<String> R, P, X;
        State(Set<String> R, Set<String> P, Set<String> X){ this.R=R; this.P=P; this.X=X; }
    }

    static String part2(Map<String, Set<String>> g) {
        Set<String> clique = largestClique(g);
        List<String> sorted = new ArrayList<>(clique);
        Collections.sort(sorted);
        return String.join(",", sorted);
    }

    public static void main(String[] args) throws Exception {
        String all = readAll();
        Map<String, Set<String>> graph = parse(all);
        System.out.println(part1(graph));       // broj trojkica (3-clique) sa 't*'
        System.out.println(part2(graph));       // najveća klika, sortirana i spojena zarezima
    }
}