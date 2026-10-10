# Java 密碼檢查器與軟體測試範例

這個專案示範如何測試密碼檢查功能：確認結果正確、找出沒有測到的程式碼，以及檢查測試能否發現刻意加入的錯誤。

**第一次接觸 Java？** 請從「第一次執行」開始。只想看成果的話，下載專案後直接開啟 `reports` 裡的報告，不需要先安裝 Java。

## 目錄

- [專案在做什麼](#專案在做什麼)
- [工具各自負責什麼](#工具各自負責什麼)
- [第一次執行](#第一次執行)
- [常用指令](#常用指令)
- [只測指定案例](#只測指定案例)
- [怎麼看報告](#怎麼看報告)
- [套件快取參數說明](#套件快取參數說明)
- [專案檔案導覽](#專案檔案導覽)
- [常見問題](#常見問題)

## 專案在做什麼

`PasswordValidator` 是提供密碼檢查功能的 Java 類別，其中的 `isValid` 方法接收密碼，回傳 `true`（合格）或 `false`（不合格）。

密碼必須符合以下規則：

- 長度為 **4～6 個字元**。
- 至少包含一個英文字母（A～Z 或 a～z）及一個數字（0～9）。
- 其他字元可以出現，但不算英文字母或數字。
- `null` 表示沒有提供字串，一律不合格；它不同於空字串 `""`。

| 輸入 | 結果 | 原因 |
|---|---|---|
| `"abc0"` | `true` | 長度 4，同時有英文與數字 |
| `"abcd"` | `false` | 沒有數字 |
| `"a1b"` | `false` | 長度只有 3 |
| `null` | `false` | 沒有提供密碼 |

下面是 Java 呼叫範例，**不是要貼到 PowerShell 的指令**：

```java
PasswordValidator validator = new PasswordValidator(); // 建立密碼檢查器
boolean result = validator.isValid("abc0");            // result 為 true
```

本專案透過自動化測試呼叫這個方法，沒有輸入密碼的視窗或 `main` 啟動程式。

## 工具各自負責什麼

| 工具 | 白話說明 | 本專案的用途 |
|---|---|---|
| JDK 21 | Java 開發工具，包含編譯器與執行環境 | 編譯並執行 Java 程式 |
| Maven 3.9 | 管理套件與建置流程 | 讀取 `pom.xml`，下載套件、編譯及執行測試 |
| JUnit 5 | 檢查實際結果是否符合預期 | 執行九個測試案例 |
| Surefire | Maven 的測試執行外掛 | 執行 JUnit 並產生結果報告 |
| JaCoCo | 觀察哪些程式碼有被測到 | 產生行覆蓋率與分支覆蓋率報告 |
| PIT | 刻意改動程式，看測試能否發現 | 進行變異測試（Mutation Testing） |

只需要自行準備 JDK 與 Maven，其他工具由 Maven 依 `pom.xml` 下載。

## 第一次執行

以下以 **Windows PowerShell** 為例，每個指令請整行複製、逐一執行。

### 1. 確認 Java 與 Maven

安裝 JDK 21 與 Maven 3.9，將兩者的 `bin` 目錄加入環境變數 `Path`，並將 `JAVA_HOME` 設為 JDK 安裝目錄（不要加上 `bin`）。設定後重新開啟 PowerShell。

```powershell
java -version
javac -version
mvn -version
```

`java` 與 `javac` 應顯示版本 21；`mvn -version` 應顯示 Maven 3.9.x，並確認 Maven 使用的 Java 也是 21。

### 2. 進入專案資料夾

下載並解壓縮專案，在 PowerShell 切換到**包含 `pom.xml` 的資料夾**。以下路徑請改成你自己的位置：

```powershell
cd "C:\Git\java_test"
```

### 3. 執行第一次測試

```powershell
mvn test
```

第一次需要連線下載套件，可能較久。完成後確認輸出包含：

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

這表示九個案例全部執行，沒有失敗、錯誤或跳過。Maven 會自動編譯，不需要手動執行 `javac`。

### 4. 產生全部工具的報告

```powershell
mvn clean verify org.pitest:pitest-maven:mutationCoverage
```

這一行會清除舊建置結果，重新執行測試、產生 JaCoCo 報告、檢查 100% 行覆蓋率門檻，最後執行 PIT。

完成後，在檔案總管找到以下檔案：

- **JaCoCo：** 用瀏覽器開啟 `target/site/jacoco/index.html`。
- **PIT：** 用瀏覽器開啟 `target/pit-reports/index.html`。
- **JUnit：** 用記事本查看 `target/surefire-reports` 裡的 `.txt`。

## 常用指令

所有指令都在專案根目錄執行。

| 想做的事 | 指令 |
|---|---|
| 只執行全部 JUnit 測試 | `mvn test` |
| 執行測試並產生 JaCoCo 報告 | `mvn clean test jacoco:report` |
| 同上，並檢查 100% 行覆蓋率門檻 | `mvn clean verify` |
| 重新建置並執行 PIT | `mvn clean test org.pitest:pitest-maven:mutationCoverage` |
| 產生全部報告並檢查覆蓋率門檻 | `mvn clean verify org.pitest:pitest-maven:mutationCoverage` |
| 產生全部報告，不檢查覆蓋率門檻 | `mvn clean test jacoco:report org.pitest:pitest-maven:mutationCoverage` |

指令中的詞代表：

| 名稱 | 意思 |
|---|---|
| `clean` | 清除 `target`，包含舊編譯檔與報告；不會刪除 `src` 或 `reports` |
| `test` | 編譯程式與測試，再執行 JUnit |
| `jacoco:report` | 根據覆蓋率資料產生報告；本身不執行測試 |
| `verify` | 執行前面的建置與測試階段，以及本專案設定的 JaCoCo 報告與門檻檢查 |
| `org.pitest:pitest-maven:mutationCoverage` | 呼叫 PIT 執行變異測試 |

若剛成功執行 `mvn clean verify`，且沒有修改程式或測試，可直接執行 `mvn org.pitest:pitest-maven:mutationCoverage`。

**`clean` 會清除前一次留在 `target` 的報告。** 若需要同時保留 JaCoCo 與 PIT 報告，使用上表的整合指令。

## 只測指定案例

不需要刪除或註解其他案例。`-Dtest` 指定範圍，`#` 後面填方法名稱，多個方法用 `+` 連接。PowerShell 中請保留雙引號。

**只測一個案例（T1）：**

```powershell
mvn "-Dtest=PasswordValidatorTest#rejectsNull" test
```

**只測兩個案例（T1、T2）：**

```powershell
mvn "-Dtest=PasswordValidatorTest#rejectsNull+rejectsTooShort" test
```

**執行整個測試類別：**

```powershell
mvn "-Dtest=PasswordValidatorTest" test
```

**產生指定案例的 JaCoCo 報告：**

```powershell
mvn "-Dtest=PasswordValidatorTest#rejectsNull" clean test jacoco:report
```

最後這個指令會清除舊資料，讓報告只反映本次選取的案例。完成後開啟 `target/site/jacoco/index.html`。多個案例同樣可以用 `+` 連接。

只測部分案例時，覆蓋率可能低於 100%，所以這裡使用 `test jacoco:report`，不執行 `verify` 的門檻檢查。

### 九個案例對照表

| 案例 | 方法名稱 | 輸入 | 預期結果 | 檢查重點 |
|---|---|---|---|---|
| T1 | `rejectsNull` | `null` | `false` | 未提供密碼 |
| T2 | `rejectsTooShort` | `"a1b"` | `false` | 長度 3，低於下限 |
| T3 | `acceptsMinimumLength` | `"abc0"` | `true` | 最小合法長度 4 |
| T4 | `acceptsMiddleLengthAndUppercase` | `"0000A"` | `true` | 長度 5、大寫字母 |
| T5 | `acceptsMaximumLength` | `"A00000"` | `true` | 最大合法長度 6 |
| T6 | `rejectsTooLong` | `"abc1234"` | `false` | 長度 7，超過上限 |
| T7 | `rejectsLettersOnly` | `"abcd"` | `false` | 只有英文 |
| T8 | `rejectsDigitsOnly` | `"1234"` | `false` | 只有數字 |
| T9 | `rejectsNeitherLetterNorDigit` | `"!!!!"` | `false` | 既無英文也無數字 |

`-Dtest` 是 Surefire 的 JUnit 篩選參數，不能用它來指定 PIT 只測同一個方法。PIT 依 `pom.xml` 的 `targetClasses` 與 `targetTests` 設定分析本專案。

## 怎麼看報告

### 新報告與隨附副本

| 工具 | 每次執行後的新報告 | 隨附的報告副本 |
|---|---|---|
| JUnit | `target/surefire-reports/` | [reports/junit](reports/junit/) |
| JaCoCo | `target/site/jacoco/index.html` | [reports/jacoco/index.html](reports/jacoco/index.html) |
| PIT | `target/pit-reports/index.html` | [reports/pit/index.html](reports/pit/index.html) |

**`target` 是自動產生的結果；`reports` 是保留給讀者查看的副本，不會自動更新。** 修改程式後，以重新執行所得的 `target` 報告為準。

需要更新副本時，將表中對應的整個報告資料夾內容複製過去，包含圖片與樣式檔，不要只複製 `index.html`。GitHub 通常顯示 HTML 原始碼；請下載專案後，在本機用瀏覽器開啟。

### JaCoCo：哪些程式碼有被測到？

| 指標或標示 | 意思 |
|---|---|
| Line Coverage（行覆蓋率） | 可執行程式碼行被測試涵蓋的比例，是本作業的主要指標 |
| Branch Coverage（分支覆蓋率） | 判斷分支被涵蓋的比例，作為補充參考 |
| 綠色底色 | 該行的指令已完整執行到 |
| 黃色底色 | 該行的指令只執行到一部分 |
| 紅色底色 | 該行的指令尚未執行到 |
| 黃色菱形 | 判斷分支只涵蓋一部分 |
| 紅色／綠色菱形 | 判斷分支完全未涵蓋／全部涵蓋 |

黃色不代表測試失敗。例如只輸入 `null`，只測到 `password == null` 成立的分支，還沒測到不成立的分支。

`N1～N15` 是自訂流程圖節點，不是行號，也不是 JaCoCo 產生的編號。只有 `}` 的行也可能對應編譯後的指令，例如迴圈跳回條件判斷，因此可能出現覆蓋率顏色。

### PIT：測試能發現程式改錯嗎？

| 狀態 | 意思 |
|---|---|
| `KILLED` | 測試成功偵測到變異 |
| `SURVIVED` | 改動後測試仍通過，未偵測到該變異 |
| `NO_COVERAGE` | 沒有測試涵蓋該變異位置 |

### 隨附報告的結果

以下是 2026-10-10 重新執行全部案例並更新至 `reports` 的結果：

| 項目 | 結果 |
|---|---|
| JUnit | **9 個案例全部通過** |
| JaCoCo 行覆蓋率 | **17/17，100%** |
| JaCoCo 分支覆蓋率 | **16/16，100%** |
| PIT | **16/16 個變異 KILLED，100%** |

覆蓋率 100% 不代表程式沒有錯誤，也不代表測過所有完整路徑。作業要求的 Edge、Edge-Pair、Path 與邏輯條件組合等分析仍須分別說明，不能只用工具的整體百分比代替。

## 套件快取參數說明

前面的指令都可以直接執行，**不一定要加 `"-Dmaven.repo.local=.m2"`**。這個選用參數只是指定 Maven 套件的儲存位置。

| 部分 | 意思 |
|---|---|
| `-D` | 設定屬性，格式為 `-D名稱=值` |
| `maven.repo.local` | Maven 本機套件庫的位置 |
| `.m2` | 目前執行目錄下的 `.m2` 資料夾 |
| 外層雙引號 | 讓 PowerShell 正確傳遞參數，不是路徑的一部分 |

在專案根目錄執行：

```powershell
# 使用 Maven 設定的本機套件庫
mvn test

# 使用專案內的 .m2 資料夾
mvn "-Dmaven.repo.local=.m2" test

# 也可以同時指定測試案例
mvn "-Dmaven.repo.local=.m2" "-Dtest=PasswordValidatorTest#rejectsNull" test
```

省略時，預設位置為使用者目錄下的 `.m2/repository`，Windows 通常是 `C:\Users\你的帳號\.m2\repository`；若 Maven 的 `settings.xml` 另有設定，則使用該位置。

切換套件庫後，缺少的套件可能需要重新下載。此參數只對當次命令生效，不會永久修改設定，也不會改變報告的輸出位置。

## 專案檔案導覽

| 路徑 | 內容 |
|---|---|
| [PasswordValidator.java](src/main/java/tw/edu/testing/PasswordValidator.java) | 受測程式，29 行，附逐行中文註解與 N1～N15 節點 |
| [PasswordValidatorTest.java](src/test/java/tw/edu/testing/PasswordValidatorTest.java) | 九個 JUnit 案例，含輸入、預期結果與目標分析 |
| [pom.xml](pom.xml) | Maven 建置、套件版本與測試工具設定 |
| [spec.md](spec.md) | 原始作業要求 |
| [測試報告.docx](docs/測試報告.docx) | Word 測試報告 |
| [PasswordValidator-CFG.png](docs/PasswordValidator-CFG.png) | 控制流程圖 |
| `reports/` | 保留的測試報告副本 |
| `target/` | Maven 自動產生的編譯結果與報告 |
| `.m2/` | 使用專案內快取參數時產生的套件庫 |

打包或上傳 GitHub 時，保留原始碼、`pom.xml`、README、作業文件及要展示的 `reports`；不需要包含 `target`、`.m2` 或 Word 暫存／備份檔。

## 常見問題

**找不到 java、javac 或 mvn？**

確認已安裝 JDK 與 Maven，將各自的 `bin` 加入 `Path` 後重新開啟 PowerShell。只有 Java 執行環境而沒有 JDK，可能找不到 `javac`。

**出現 `release version 21 not supported`？**

執行 `mvn -version` 查看 Maven 實際使用的 Java，確認 `JAVA_HOME` 指向 JDK 21。

**出現找不到 POM 或沒有專案的訊息？**

目前目錄不對，請先切換到包含 `pom.xml` 的專案根目錄。

**測試通過，為什麼 verify 仍失敗？**

本專案另有 100% 行覆蓋率門檻。若錯誤指出 coverage check 未通過，請查看 JaCoCo 報告；只測部分案例時，可以用 `clean test jacoco:report` 查看結果。

**跑完 mvn test，HTML 報告為什麼沒更新？**

`test` 會更新 JUnit 結果，但不會產生 JaCoCo HTML 或執行 PIT。請選用「常用指令」中的報告指令，並查看 `target` 下的新結果。
