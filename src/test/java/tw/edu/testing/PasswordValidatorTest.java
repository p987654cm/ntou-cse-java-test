package tw.edu.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    /*
     * 測試案例 T1：未提供密碼
     *
     * 【對應作業】
     * 1. Line coverage
     * 2. graph coverage
     * 3. Stress Testing coverage
     * 5. Input Domain Characterization
     * 6. mutation testing coverage
     * 7. Equivalence partition
     *
     * 【測試內容】
     * 1. Input values（測試時候需要控制資料的值）
     *    password = null
     *
     * 2. expected result（正確的結果）
     *    false
     *
     * 3. test program's result（程式執行後的結果）
     *    false（本次 JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    - 沒有提供密碼時，應回傳 false。
     *    - 確認程式先檢查 null，不會直接讀取密碼長度。
     *
     *    Line coverage: 測試案例涵蓋的lines: 4 7 8
     */
    @Test
    void rejectsNull() {
        assertEquals(false, validator.isValid(null));
    }

    /*
     * 測試案例 T2：密碼太短（3 個字元）
     *
     * 【對應作業】
     * 1. Line coverage
     * 2. graph coverage
     * 3. Stress Testing coverage
     * 4. Logical Expressions coverage
     * 5. Input Domain Characterization
     * 6. mutation testing coverage
     * 7. Equivalence partition
     * 8. Boundary value
     *
     * 【測試內容】
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "a1b"
     *
     * 2. expected result（正確的結果）
     *    false
     *
     * 3. test program's result（程式執行後的結果）
     *    false（本次 JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    - 密碼已有英文和數字，但長度只有 3。
     *    - 確認少於 4 個字元時，仍然不能通過。
     *
     *    Line coverage: 測試案例涵蓋的lines: 4 7 10 11
     */
    @Test
    void rejectsTooShort() {
        assertEquals(false, validator.isValid("a1b"));
    }

    /*
     * 測試案例 T3：最短有效密碼（4 個字元）
     *
     * 【對應作業】
     * 1. Line coverage
     * 2. graph coverage
     * 3. Stress Testing coverage
     * 4. Logical Expressions coverage
     * 5. Input Domain Characterization
     * 6. mutation testing coverage
     * 7. Equivalence partition
     * 8. Boundary value
     *
     * 【測試內容】
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "abc0"
     *
     * 2. expected result（正確的結果）
     *    true
     *
     * 3. test program's result（程式執行後的結果）
     *    true（本次 JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    - 長度剛好是允許的下限 4，應通過檢查。
     *    - 唯一的數字 0 放在最後，確認最後一個字元也有被檢查。
     *
     *    Line coverage: 測試案例涵蓋的lines: 4 7 10 14 15 16 17 18 19 20 22 23 25 26 27
     */
    @Test
    void acceptsMinimumLength() {
        assertEquals(true, validator.isValid("abc0"));
    }

    /*
     * 測試案例 T4：一般有效密碼（5 個字元，含大寫英文）
     *
     * 【對應作業】
     * 2. graph coverage
     * 6. mutation testing coverage
     * 8. Boundary value
     *
     * 【測試內容】
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "0000A"
     *
     * 2. expected result（正確的結果）
     *    true
     *
     * 3. test program's result（程式執行後的結果）
     *    true（本次 JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    - 長度 5 在允許範圍內，應通過檢查。
     *    - 唯一的英文字母 A 放在最後，確認大寫英文和最後一字都能辨識。
     *
     *    Line coverage: 測試案例涵蓋的lines: 4 7 10 14 15 16 17 18 19 20 22 23 25 26 27
     */
    @Test
    void acceptsMiddleLengthAndUppercase() {
        assertEquals(true, validator.isValid("0000A"));
    }

    /*
     * 測試案例 T5：最長有效密碼（6 個字元）
     *
     * 【對應作業】
     * 2. graph coverage
     * 3. Stress Testing coverage
     * 6. mutation testing coverage
     * 8. Boundary value
     *
     * 【測試內容】
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "A00000"
     *
     * 2. expected result（正確的結果）
     *    true
     *
     * 3. test program's result（程式執行後的結果）
     *    true（本次 JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    - 長度剛好是允許的上限 6，應通過檢查。
     *    - 確認程式不會把 6 個字元誤判為太長。
     *
     *    Line coverage: 測試案例涵蓋的lines: 4 7 10 14 15 16 17 18 19 20 22 23 25 26 27
     */
    @Test
    void acceptsMaximumLength() {
        assertEquals(true, validator.isValid("A00000"));
    }

    /*
     * 測試案例 T6：密碼太長（7 個字元）
     *
     * 【對應作業】
     * 2. graph coverage
     * 3. Stress Testing coverage
     * 4. Logical Expressions coverage
     * 5. Input Domain Characterization
     * 6. mutation testing coverage
     * 7. Equivalence partition
     * 8. Boundary value
     *
     * 【測試內容】
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "abc1234"
     *
     * 2. expected result（正確的結果）
     *    false
     *
     * 3. test program's result（程式執行後的結果）
     *    false（本次 JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    - 密碼已有英文和數字，但長度是 7。
     *    - 確認超過 6 個字元時不能通過。
     *
     *    Line coverage: 測試案例涵蓋的lines: 4 7 10 11
     */
    @Test
    void rejectsTooLong() {
        assertEquals(false, validator.isValid("abc1234"));
    }

    /*
     * 測試案例 T7：只有英文，沒有數字
     *
     * 【對應作業】
     * 4. Logical Expressions coverage
     * 5. Input Domain Characterization
     * 6. mutation testing coverage
     * 7. Equivalence partition
     *
     * 【測試內容】
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "abcd"
     *
     * 2. expected result（正確的結果）
     *    false
     *
     * 3. test program's result（程式執行後的結果）
     *    false（本次 JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    - 長度符合規定，也有英文，但完全沒有數字。
     *    - 確認不能只因為有英文就讓密碼通過。
     *
     *    Line coverage: 測試案例涵蓋的lines: 4 7 10 14 15 16 17 18 19 20 22 25 26 27
     */
    @Test
    void rejectsLettersOnly() {
        assertEquals(false, validator.isValid("abcd"));
    }

    /*
     * 測試案例 T8：只有數字，沒有英文
     *
     * 【對應作業】
     * 4. Logical Expressions coverage
     * 5. Input Domain Characterization
     * 6. mutation testing coverage
     * 7. Equivalence partition
     *
     * 【測試內容】
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "1234"
     *
     * 2. expected result（正確的結果）
     *    false
     *
     * 3. test program's result（程式執行後的結果）
     *    false（本次 JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    - 長度符合規定，也有數字，但完全沒有英文。
     *    - 確認不能只因為有數字就讓密碼通過。
     *
     *    Line coverage: 測試案例涵蓋的lines: 4 7 10 14 15 16 17 18 19 22 23 25 26 27
     */
    @Test
    void rejectsDigitsOnly() {
        assertEquals(false, validator.isValid("1234"));
    }

    /*
     * 測試案例 T9：只有特殊符號
     *
     * 【對應作業】
     * 2. graph coverage
     * 4. Logical Expressions coverage
     * 5. Input Domain Characterization
     *
     * 【測試內容】
     * 1. Input values（測試時候需要控制資料的值）
     *    password = "!!!!"
     *
     * 2. expected result（正確的結果）
     *    false
     *
     * 3. test program's result（程式執行後的結果）
     *    false（本次 JUnit 執行通過）。
     *
     * 4. criteria analysis（測試目標的分析）
     *    - 長度符合規定，但只有特殊符號。
     *    - 確認特殊符號不會被當成英文或數字，結果應為 false。
     *
     *    Line coverage: 測試案例涵蓋的lines: 4 7 10 14 15 16 17 18 19 22 25 26 27
     */
    @Test
    void rejectsNeitherLetterNorDigit() {
        assertEquals(false, validator.isValid("!!!!"));
    }
}
