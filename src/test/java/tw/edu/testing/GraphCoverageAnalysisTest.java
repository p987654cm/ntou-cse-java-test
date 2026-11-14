package tw.edu.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/*
 * 作業 2 Graph coverage 的涵蓋率計算（不是受測程式的測試案例）。
 *
 * JaCoCo 只統計 Line／Branch，無法統計 Edge-Pair 與 Path，
 * 因此以 PasswordValidatorCfg 推導各測試案例的測試路徑，計算三種準則的涵蓋率，
 * 並在主控台印出報告中使用的分析結果。
 */
class GraphCoverageAnalysisTest {

    // 與 EdgeCoverageTest、EdgePairCoverageTest、PathCoverageTest 使用的輸入值一致。
    private static final List<String> EDGE_INPUTS = Arrays.asList(null, "a1b", "abc0");
    private static final List<String> EDGE_PAIR_INPUTS = Arrays.asList(null, "a1b", "ab1!");
    private static final List<String> PATH_INPUTS = Arrays.asList(null, "a1b", "ab1!");

    @Test
    void testPathsAreCompletePathsOfCfg() {
        for (String input : List.of("a1b", "abc0", "ab1!", "A00000", "!!!!")) {
            assertTrue(PasswordValidatorCfg.isCompletePath(PasswordValidatorCfg.testPath(input)), input);
        }
        assertTrue(PasswordValidatorCfg.isCompletePath(PasswordValidatorCfg.testPath(null)));
    }

    @Test
    void edgeCoverageIs100Percent() {
        Set<List<Integer>> covered = new LinkedHashSet<>();
        for (String input : EDGE_INPUTS) {
            List<Integer> path = PasswordValidatorCfg.testPath(input);
            covered.addAll(PasswordValidatorCfg.subpaths(path, 2));
            print("Edge", input, path);
        }
        Set<List<Integer>> required = new LinkedHashSet<>(PasswordValidatorCfg.EDGES);
        assertEquals(17, required.size());
        assertEquals(required, covered);
        System.out.printf("Edge coverage: %d/%d = 100%%%n%n", covered.size(), required.size());
    }

    @Test
    void edgePairCoverageIs100PercentOfFeasible() {
        Set<List<Integer>> all = PasswordValidatorCfg.allEdgePairs();
        Set<List<Integer>> feasible = new LinkedHashSet<>(all);
        feasible.removeAll(PasswordValidatorCfg.INFEASIBLE_EDGE_PAIRS);

        Set<List<Integer>> covered = new LinkedHashSet<>();
        for (String input : EDGE_PAIR_INPUTS) {
            List<Integer> path = PasswordValidatorCfg.testPath(input);
            print("Edge-Pair", input, path);
            for (List<Integer> pair : all) {
                if (PasswordValidatorCfg.tours(path, pair)) {
                    covered.add(pair);
                }
            }
        }
        assertEquals(21, all.size());
        assertEquals(19, feasible.size());
        assertEquals(feasible, covered);
        System.out.printf("Edge-Pair coverage: %d/%d (feasible) = 100%%, infeasible: %d%n%n",
                covered.size(), feasible.size(), all.size() - feasible.size());
    }

    @Test
    void pathCoverageIs100PercentOfFeasible() {
        List<List<Integer>> paths = new ArrayList<>();
        for (String input : PATH_INPUTS) {
            paths.add(PasswordValidatorCfg.testPath(input));
            print("Path", input, paths.get(paths.size() - 1));
        }

        int feasible = 0;
        int covered = 0;
        for (List<Integer> required : PasswordValidatorCfg.PATHS_WITHOUT_LOOP) {
            boolean hit = paths.contains(required); // (1) 必須是整條完整路徑
            feasible += PasswordValidatorCfg.INFEASIBLE_PATHS.contains(required) ? 0 : 1;
            covered += hit ? 1 : 0;
        }
        for (List<Integer> required : PasswordValidatorCfg.PATHS_WITH_LOOP) {
            boolean hit = paths.stream().anyMatch(path -> PasswordValidatorCfg.tours(path, required)); // (2) 繞過迴圈
            feasible += PasswordValidatorCfg.INFEASIBLE_PATHS.contains(required) ? 0 : 1;
            covered += hit ? 1 : 0;
        }
        assertEquals(5, feasible);
        assertEquals(feasible, covered);
        System.out.printf("Path coverage: %d/%d (feasible) = 100%%, infeasible: 2%n%n", covered, feasible);
    }

    private static void print(String criterion, String input, List<Integer> path) {
        String shown = input == null ? "null" : "\"" + input + "\"";
        System.out.printf("[%s] %s -> %s%n", criterion, shown, PasswordValidatorCfg.format(path));
    }
}
