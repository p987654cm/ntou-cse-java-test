package tw.edu.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/*
 * 作業 2-3 Path Coverage：
 * (1) 沒繞過迴圈：不經過 back edge（N14 → N8）的完整路徑 P1～P3，其中 P3（迴圈 0 次）不可行。
 * (2) 有繞過迴圈：迴圈主體每一條內部路徑都要繞回 N8 一次 P4～P7，其中 P7 不可行。
 * 測試需求共 7 項，可行需求 5 項，3 個測試案例即可全部涵蓋。
 */
class PathCoverageTest {

    private final PasswordValidator validator = new PasswordValidator();

    /*
     * 測試案例 PC1：沒繞過迴圈，未提供密碼
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
     *    Path coverage (1) 沒繞過迴圈: 測試案例涵蓋的 Path: P1 = N1 N2
     */
    @Test
    void rejectsNullWithoutLoop() {
        assertEquals(false, validator.isValid(null));
    }

    /*
     * 測試案例 PC2：沒繞過迴圈，長度不合格
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
     *    Path coverage (1) 沒繞過迴圈: 測試案例涵蓋的 Path: P2 = N1 N3 N4
     */
    @Test
    void rejectsInvalidLengthWithoutLoop() {
        assertEquals(false, validator.isValid("a1b"));
    }

    /*
     * 測試案例 PC3：有繞過迴圈，三種字元都出現
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
     *    測試路徑：N1 N3 N5 N6 N7 N8 [P4] [P4] [P5] [P6] N15，迴圈繞 4 圈後離開
     *    Path coverage (2) 有繞過迴圈: 測試案例涵蓋的 Paths:
     *    P4 = N8 N9 N10 N11 N12 N14 N8（英文字母 a、b）
     *    P5 = N8 N9 N10 N12 N13 N14 N8（數字 1）
     *    P6 = N8 N9 N10 N12 N14 N8（特殊符號 !）
     */
    @Test
    void acceptsAfterLoopingAllBodyPaths() {
        assertEquals(true, validator.isValid("ab1!"));
    }
}
