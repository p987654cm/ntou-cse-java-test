package tw.edu.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/*
 * 作業 2-2 Edge-Pair Coverage：所有長度 2 的路徑（連續兩條邊）都要被走過。
 * 測試需求共 21 項，其中 [N7 N8 N15]、[N11 N12 N13] 不可行，可行需求 19 項。
 * 與 Edge coverage 相比，多了「非英文也非數字」的字元，才能走到 [N10 N12 N14]。
 */
class EdgePairCoverageTest {

    private final PasswordValidator validator = new PasswordValidator();

    /*
     * 測試案例 EP1：未提供密碼
     *
     * 1. Input values（測試時候需要控制資料的值）
     *    password = null
     *
     * 2. expected result（正確的結果）
     *    false
     *
     * 3. test program's result（程式執行後的結果）
     *    false（JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    測試路徑：N1 N2
     *    Edge-Pair coverage: 測試案例涵蓋的 Edge-Pairs: [N1 N2]
     */
    @Test
    void rejectsNull() {
        assertEquals(false, validator.isValid(null));
    }

    /*
     * 測試案例 EP2：長度不合格（3 個字元）
     *
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "a1b"
     *
     * 2. expected result（正確的結果）
     *    false
     *
     * 3. test program's result（程式執行後的結果）
     *    false（JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    測試路徑：N1 N3 N4
     *    Edge-Pair coverage: 測試案例涵蓋的 Edge-Pairs: [N1 N3 N4]
     */
    @Test
    void rejectsInvalidLength() {
        assertEquals(false, validator.isValid("a1b"));
    }

    /*
     * 測試案例 EP3：英文、數字、特殊符號都出現的合格密碼
     *
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "ab1!"
     *
     * 2. expected result（正確的結果）
     *    true
     *
     * 3. test program's result（程式執行後的結果）
     *    true（JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    測試路徑：N1 N3 N5 N6 N7 N8
     *              （a、b：N9 N10 N11 N12 N14 N8）×2（1：N9 N10 N12 N13 N14 N8）
     *              （!：N9 N10 N12 N14 N8）N15
     *    Edge-Pair coverage: 測試案例涵蓋其餘 17 項可行 Edge-Pairs：
     *    [N1 N3 N5] [N3 N5 N6] [N5 N6 N7] [N6 N7 N8] [N7 N8 N9] [N8 N9 N10]
     *    [N9 N10 N11] [N9 N10 N12] [N10 N11 N12] [N11 N12 N14] [N10 N12 N13]
     *    [N10 N12 N14] [N12 N13 N14] [N12 N14 N8] [N13 N14 N8] [N14 N8 N9] [N14 N8 N15]
     */
    @Test
    void acceptsLetterDigitAndSymbol() {
        assertEquals(true, validator.isValid("ab1!"));
    }
}
