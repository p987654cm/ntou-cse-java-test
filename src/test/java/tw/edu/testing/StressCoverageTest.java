package tw.edu.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/*
 * 作業 3 Stress Testing coverage（Testing-Introduction.ppt p.50）
 *
 * 資料的有效範圍：
 * - 密碼字串長度：4～6 個字元。
 * - 資料筆數：迴圈逐字元檢查，每個字元為一筆資料，有效筆數同為 4～6 筆。
 * - 內容：至少一個英文字母與一個數字（極值案例皆使用內容合格的字串，只測長度極限）。
 *
 * 測試需求：有效範圍內極小值、有效範圍內極大值，以及無效值（低於下限、超過上限、null），共 5 項。
 * 空字串、非常長的字串與長度 3、長度 7 分屬同一種無效值（走相同路徑、結果相同），
 * 不另增案例，以最少的 5 個測試案例達到 100%。
 */
class StressCoverageTest {

    private final PasswordValidator validator = new PasswordValidator();

    /*
     * 測試案例 ST1：有效範圍內極小值（長度 4、迴圈最少 4 筆）
     *
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "abc0"（長度 4）
     *
     * 2. expected result（正確的結果）
     *    true
     *
     * 3. test program's result（程式執行後的結果）
     *    true（JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    Stress coverage: 有效範圍 4～6 的極小值 4，迴圈執行最少的 4 次。
     */
    @Test
    void acceptsMinimumValidLength() {
        assertEquals(true, validator.isValid("abc0"));
    }

    /*
     * 測試案例 ST2：有效範圍內極大值（長度 6、迴圈最多 6 筆）
     *
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "A00000"（長度 6）
     *
     * 2. expected result（正確的結果）
     *    true
     *
     * 3. test program's result（程式執行後的結果）
     *    true（JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    Stress coverage: 有效範圍 4～6 的極大值 6，迴圈執行最多的 6 次。
     */
    @Test
    void acceptsMaximumValidLength() {
        assertEquals(true, validator.isValid("A00000"));
    }

    /*
     * 測試案例 ST3：無效值 低於下限（長度 3）
     *
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "a1b"（長度 3）
     *
     * 2. expected result（正確的結果）
     *    false
     *
     * 3. test program's result（程式執行後的結果）
     *    false（JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    Stress coverage: 無效值 長度 3（下限 4 減 1），內容雖有英文和數字仍不合格。
     */
    @Test
    void rejectsLengthBelowMinimum() {
        assertEquals(false, validator.isValid("a1b"));
    }

    /*
     * 測試案例 ST4：無效值 超過上限（長度 7）
     *
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "abc1234"（長度 7）
     *
     * 2. expected result（正確的結果）
     *    false
     *
     * 3. test program's result（程式執行後的結果）
     *    false（JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    Stress coverage: 無效值 長度 7（上限 6 加 1），內容雖有英文和數字仍不合格。
     */
    @Test
    void rejectsLengthAboveMaximum() {
        assertEquals(false, validator.isValid("abc1234"));
    }

    /*
     * 測試案例 ST5：無效值 null（Null references）
     *
     * 1. Input values（測試時候需要控制資料的值）
     *    password = null
     *
     * 2. expected result（正確的結果）
     *    false，且不拋出 NullPointerException
     *
     * 3. test program's result（程式執行後的結果）
     *    false（JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    Stress coverage: 無效值 null，不是字串；程式在 N1 就結束，不會讀取長度。
     */
    @Test
    void rejectsNull() {
        assertEquals(false, validator.isValid(null));
    }
}
