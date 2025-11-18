import java.io.*;
import java.util.*;

public class Day9 {

    static String readLine() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

    // ===== Part 1 parsing: ekspandiraj u blokove (id ili -1 za prazno) =====
    static List<Integer> parseBlocks(String s) {
        List<Integer> out = new ArrayList<>();
        int id = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            int len = ch - '0';
            if (i % 2 == 0) { // file
                for (int k = 0; k < len; k++) out.add(id);
                id++;
            } else { // free
                for (int k = 0; k < len; k++) out.add(-1);
            }
        }
        return out;
    }

    // ===== Part 2 parsing: segmenti (id,len,wasMoved) =====
    static class Seg {
        int id;       // -1 = free
        int len;
        boolean moved;
        Seg(int id, int len, boolean moved) { this.id = id; this.len = len; this.moved = moved; }
    }

    static List<Seg> parseSegments(String s) {
        List<Seg> out = new ArrayList<>();
        int id = 0;
        for (int i = 0; i < s.length(); i++) {
            int len = s.charAt(i) - '0';
            if (i % 2 == 0) { // file
                out.add(new Seg(id++, len, false));
            } else { // free
                out.add(new Seg(-1, len, false));
            }
        }
        return out;
    }

    // ===== Part 1 =====
    static long part1(String line) {
        List<Integer> disk = parseBlocks(line);
        int back = disk.size() - 1;
        // pomjeri back na zadnji ne-prazan
        while (back >= 0 && disk.get(back) == -1) back--;

        long checksum = 0;
        for (int i = 0; i <= back; i++) {
            if (disk.get(i) == -1) {
                // swap s kraja (zadnji file blok)
                int v = disk.get(back);
                disk.set(i, v);
                disk.set(back, -1);
                // pomjeri back ulijevo preko praznina
                do {
                    back--;
                } while (back >= 0 && disk.get(back) == -1);
            }
            // ako je -1, to znači i > back – ali tada petlja svakako završava
            if (disk.get(i) != -1) {
                checksum += (long) i * disk.get(i);
            }
        }
        // ako pređemo preko back, ostatak je prazan – ionako se ne dodaje u checksum
        return checksum;
    }

    // ===== Part 2 helpers =====
    // traži s kraja (do index i) prvi file segment koji NIJE pomaknut i stane u numEmpty
    // vrati (segmentZaUmetnuti, preostalaPraznina) i usput označi segment kao moved
    static class FillResult {
        Seg filled;
        int emptyLeft;
        FillResult(Seg f, int e) { filled = f; emptyLeft = e; }
    }

    static FillResult fillEmpty(List<Seg> disk, int numEmpty, int i) {
        int back = disk.size() - 1;
        while (i <= back) {
            Seg s = disk.get(back);
            if (s.id != -1 && !s.moved && s.len <= numEmpty) {
                s.moved = true; // označi da je pomjeren
                return new FillResult(new Seg(s.id, s.len, true), numEmpty - s.len);
            }
            back--;
        }
        return null;
    }

    static long part2(String line) {
        List<Seg> disk = parseSegments(line);
        List<Seg> rearr = new ArrayList<>();

        for (int i = 0; i < disk.size(); i++) {
            Seg cur = disk.get(i);
            if (cur.id != -1 && !cur.moved) {
                // datoteka na svom mjestu, još nije pomjerana -> ostaje
                rearr.add(new Seg(cur.id, cur.len, cur.moved));
                continue;
            }

            // praznina: pokušaj popuniti cijelim fajlovima s kraja
            int empty = cur.len;
            while (empty > 0) {
                FillResult fr = fillEmpty(disk, empty, i);
                if (fr == null) break;
                empty = fr.emptyLeft;
                rearr.add(fr.filled);
            }
            if (empty > 0) rearr.add(new Seg(-1, empty, false));
        }

        long checksum = 0;
        int pos = 0;
        for (Seg s : rearr) {
            if (s.id == -1) {
                pos += s.len;
            } else {
                for (int k = 0; k < s.len; k++) {
                    checksum += (long) s.id * pos;
                    pos++;
                }
            }
        }
        return checksum;
    }

    public static void main(String[] args) throws Exception {
        String line = readLine();
        System.out.println(part1(line));
        System.out.println(part2(line));
    }
}