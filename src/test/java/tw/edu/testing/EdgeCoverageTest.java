package tw.edu.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/*
 * 作業 2-1 Edge Coverage：CFG 的 17 條邊（E1～E17）每一條都要被測試路徑走過。
 * 每個測試路徑只會停在 N2、N4、N15 其中一個終點，因此至少需要 3 個測試案例。
 */
class EdgeCoverageTest {

    private final PasswordValidator validator = new PasswordValidator();

    /*
     * 測試案例 EC1：未提供密碼
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
     *    Edge coverage: 測試案例涵蓋的 Edges: E1(N1-N2)
     */
    @Test
    void rejectsNull() {
        assertEquals(false, validator.isValid(null));
    }

    /*
     * 測試案例 EC2：長度不合格（3 個字元）
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
     *    Edge coverage: 測試案例涵蓋的 Edges: E2(N1-N3), E3(N3-N4)
     */
    @Test
    void rejectsInvalidLength() {
        assertEquals(false, validator.isValid("a1b"));
    }

    /*
     * 測試案例 EC3：有英文也有數字的合格密碼
     *
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "abc0"
     *
     * 2. expected result（正確的結果）
     *    true
     *
     * 3. test program's result（程式執行後的結果）
     *    true（JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    測試路徑：N1 N3 N5 N6 N7 N8（a、b、c：N9 N10 N11 N12 N14 N8）×3
     *              （0：N9 N10 N12 N13 N14 N8）N15
     *    Edge coverage: 測試案例涵蓋的 Edges: E2, E4～E17
     *    英文字母走 E11、E13、E15；數字走 E12、E14、E16；最後由 E9 離開迴圈。
     */
    @Test
    void acceptsLetterAndDigit() {
        assertEquals(true, validator.isValid("abc0"));
    }
}
