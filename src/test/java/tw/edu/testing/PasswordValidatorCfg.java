package tw.edu.testing;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/*
 * PasswordValidator 的控制流程圖（CFG）模型，供作業 2 Graph coverage 計算測試需求（TR）與涵蓋率。
 *
 * 節點 N1～N15 對應 PasswordValidator.java 的註解，流程圖見 docs/PasswordValidator-CFG.png。
 * 本類別只負責「分析」：依 CFG 推導輸入值會走的測試路徑，再計算涵蓋了哪些 Edge、Edge-Pair、Path。
 * 程式實際的執行結果仍由 JUnit 驗證，實際執行到的程式行由 JaCoCo 驗證。
 */
final class PasswordValidatorCfg {

    // 17 條邊（Edge），格式為 {起點, 終點}；T／F 為判斷式成立／不成立的分支。
    static final List<List<Integer>> EDGES = List.of(
            List.of(1, 2),   // E1：N1 T，password == null
            List.of(1, 3),   // E2：N1 F
            List.of(3, 4),   // E3：N3 T，長度不合格
            List.of(3, 5),   // E4：N3 F
            List.of(5, 6),   // E5
            List.of(6, 7),   // E6
            List.of(7, 8),   // E7
            List.of(8, 9),   // E8：N8 T，進入迴圈主體
            List.of(8, 15),  // E9：N8 F，離開迴圈
            List.of(9, 10),  // E10
            List.of(10, 11), // E11：N10 T，英文字母
            List.of(10, 12), // E12：N10 F
            List.of(11, 12), // E13
            List.of(12, 13), // E14：N12 T，數字
            List.of(12, 14), // E15：N12 F
            List.of(13, 14), // E16
            List.of(14, 8)); // E17：迴圈繞回（back edge）

    // 不可行（infeasible）的 Edge-Pair：程式邏輯上不可能走到，不列入分母。
    static final Set<List<Integer>> INFEASIBLE_EDGE_PAIRS = Set.of(
            List.of(7, 8, 15),   // 能走到 N7 代表長度為 4～6，第一次判斷 N8 必定成立
            List.of(11, 12, 13)); // 同一個字元不可能同時是英文字母與數字

    // 迴圈主體繞一圈（N8 → … → N8）的三條可行內部路徑。
    static final List<Integer> LETTER_ROUND = List.of(8, 9, 10, 11, 12, 14, 8);
    static final List<Integer> DIGIT_ROUND = List.of(8, 9, 10, 12, 13, 14, 8);
    static final List<Integer> OTHER_ROUND = List.of(8, 9, 10, 12, 14, 8);

    // Path coverage 測試需求：(1) 沒繞過迴圈的完整路徑；(2) 有繞過迴圈的迴圈路徑。
    static final List<List<Integer>> PATHS_WITHOUT_LOOP = List.of(
            List.of(1, 2),
            List.of(1, 3, 4),
            List.of(1, 3, 5, 6, 7, 8, 15)); // 迴圈 0 次，不可行
    static final List<List<Integer>> PATHS_WITH_LOOP = List.of(
            LETTER_ROUND,
            DIGIT_ROUND,
            OTHER_ROUND,
            List.of(8, 9, 10, 11, 12, 13, 14, 8)); // 同一字元既是英文又是數字，不可行
    static final Set<List<Integer>> INFEASIBLE_PATHS = Set.of(
            List.of(1, 3, 5, 6, 7, 8, 15),
            List.of(8, 9, 10, 11, 12, 13, 14, 8));

    private PasswordValidatorCfg() {
    }

    // 依 CFG 推導輸入值的測試路徑（test path）。
    static List<Integer> testPath(String password) {
        if (password == null) {
            return List.of(1, 2);
        }
        if (password.length() < 4 || password.length() > 6) {
            return List.of(1, 3, 4);
        }
        List<Integer> path = new ArrayList<>(List.of(1, 3, 5, 6, 7));
        for (int index = 0; index < password.length(); index++) {
            String character = password.substring(index, index + 1);
            List<Integer> round = character.matches("[A-Za-z]") ? LETTER_ROUND
                    : character.matches("[0-9]") ? DIGIT_ROUND
                    : OTHER_ROUND;
            path.addAll(round.subList(0, round.size() - 1)); // 最後的 N8 由下一圈或出口補上
        }
        path.add(8);
        path.add(15);
        return path;
    }

    // 測試路徑必須從 N1 開始、在終點節點結束，且每一步都是 CFG 中存在的邊。
    static boolean isCompletePath(List<Integer> path) {
        if (path.get(0) != 1 || !Set.of(2, 4, 15).contains(path.get(path.size() - 1))) {
            return false;
        }
        for (int i = 0; i + 1 < path.size(); i++) {
            if (!EDGES.contains(List.of(path.get(i), path.get(i + 1)))) {
                return false;
            }
        }
        return true;
    }

    /*
     * Edge-Pair 測試需求：所有長度 2 的路徑 [a, b, c]；
     * 無法延伸成長度 2 的邊（N1 → N2）則以邊本身列為需求，共 21 項。
     */
    static Set<List<Integer>> allEdgePairs() {
        Set<List<Integer>> pairs = new LinkedHashSet<>();
        for (List<Integer> first : EDGES) {
            boolean extended = false;
            for (List<Integer> second : EDGES) {
                if (first.get(1).equals(second.get(0))) {
                    pairs.add(List.of(first.get(0), first.get(1), second.get(1)));
                    extended = true;
                }
            }
            boolean hasPrevious = EDGES.stream().anyMatch(edge -> edge.get(1).equals(first.get(0)));
            if (!extended && !hasPrevious) {
                pairs.add(first);
            }
        }
        return pairs;
    }

    // 取出測試路徑中所有長度為 length 的連續子路徑（length 2 為邊，3 為 Edge-Pair）。
    static Set<List<Integer>> subpaths(List<Integer> path, int length) {
        Set<List<Integer>> result = new LinkedHashSet<>();
        for (int i = 0; i + length <= path.size(); i++) {
            result.add(List.copyOf(path.subList(i, i + length)));
        }
        return result;
    }

    // 判斷 required 是否為 path 的連續子路徑（路徑涵蓋：tour）。
    static boolean tours(List<Integer> path, List<Integer> required) {
        return java.util.Collections.indexOfSubList(path, required) >= 0;
    }

    // 將節點序列格式化為 N1 N3 N4 的文字。
    static String format(List<Integer> nodes) {
        StringBuilder text = new StringBuilder();
        for (int node : nodes) {
            text.append(text.isEmpty() ? "" : " ").append("N").append(node);
        }
        return text.toString();
    }
}
