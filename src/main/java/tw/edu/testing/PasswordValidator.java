package tw.edu.testing; 

// 密碼檢查器：長度 4～6，至少包含一個英文字母和一個數字。
public class PasswordValidator { 

    public boolean isValid(String password) { 
        if (password == null) {                         // N1：檢查是否未提供密碼（null）。
            return false;                              // N2：密碼為 null，回傳不合格並結束方法。
        } 
        if (password.length() < 4 || password.length() > 6) { // N3：檢查長度是否小於 4 或大於 6。
            return false;                              // N4：長度不合格，回傳 false 並結束方法。
        } 

        boolean hasLetter = false;                     // N5：記錄是否找到英文字母，初始為尚未找到。
        boolean hasDigit = false;                      // N6：記錄是否找到數字，初始為尚未找到。
        int index = 0;                                 // N7：從索引 0，也就是第一個字元開始檢查。
        while (index < password.length()) {             // N8：尚未到字串結尾時，繼續執行迴圈。
            String character = password.substring(index, index + 1); // N9：取出目前位置的字元。
            if (character.matches("[A-Za-z]")) {        // N10：判斷目前字元是否為大寫或小寫英文字母。
                hasLetter = true;                      // N11：找到英文字母，將紀錄設為 true。
            } 
            if (character.matches("[0-9]")) {           // N12：判斷目前字元是否為數字 0～9。
                hasDigit = true;                       // N13：找到數字，將紀錄設為 true。
            } 
            index++;                                   // N14：索引加 1，準備檢查下一個字元。
        } // 本次迴圈結束，回到 while 條件重新判斷。
        return hasLetter && hasDigit;                  // N15：同時含英文與數字才回傳 true。
    } 
} 
