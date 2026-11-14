# Java 密碼檢查器與軟體測試範例

這個專案示範如何測試密碼檢查功能：確認結果正確、找出沒有測到的程式碼，以及檢查測試能否發現刻意加入的錯誤。

**第一次接觸 Java？** 請從「第一次執行」開始。只想看成果的話，下載專案後直接開啟 `reports` 裡的報告，不需要先安裝 Java。

## 目錄

- [專案在做什麼](#專案在做什麼)
- [工具各自負責什麼](#工具各自負責什麼)
- [第一次執行](#第一次執行)
- [常用指令](#常用指令)
- [只測指定案例](#只測指定案例)
- [作業 2、3：Graph 與 Stress coverage](#作業-23graph-與-stress-coverage)
- [怎麼看報告](#怎麼看報告)
- [套件快取參數說明](#套件快取參數說明)
- [專案檔案導覽](#專案檔案導覽)
- [常見問題](#常見問題)
- [1114 作業：課程報名與費用計算](#1114-作業課程報名與費用計算)

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
| JUnit 5 | 檢查實際結果是否符合預期 | 執行 41 個測試 |
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
Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

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

## 作業 2、3：Graph 與 Stress coverage

11/7（碩專）繳交的作業 2 graph coverage 與作業 3 Stress Testing coverage，每個準則各自一個測試類別，受測程式與作業 1 相同、未修改。完整分析、VS Code 操作與 Demo 步驟見 [docs/Homework2-3.md](docs/Homework2-3.md)（瀏覽器版 `docs/Homework2-3.html`）。

| 作業 | 測試類別 | 案例數 | 涵蓋率 |
|---|---|---|---|
| 2-1 Edge Coverage | `EdgeCoverageTest` | 3 | 17/17 條邊 |
| 2-2 Edge-Pair Coverage | `EdgePairCoverageTest` | 3 | 19/19 項可行 Edge-Pair（不可行 2 項） |
| 2-3 Path Coverage | `PathCoverageTest` | 3 | 5/5 項可行 Path（不可行 2 項） |
| 3 Stress Testing Coverage | `StressCoverageTest` | 5 | 極小值、極大值與 3 種無效值（低於下限、超過上限、null） |

JaCoCo 無法統計 Edge-Pair 與 Path，因此另以 `PasswordValidatorCfg`（控制流程圖模型）與 `GraphCoverageAnalysisTest` 計算三種 Graph coverage；任何需求未涵蓋時測試會失敗。

```powershell
# 各作業單獨執行並產生 JaCoCo 報告
mvn "-Dtest=EdgeCoverageTest" clean test jacoco:report
mvn "-Dtest=EdgePairCoverageTest" clean test jacoco:report
mvn "-Dtest=PathCoverageTest" clean test jacoco:report
mvn "-Dtest=StressCoverageTest" clean test jacoco:report

# 印出每個案例的測試路徑與 Edge／Edge-Pair／Path 涵蓋率
mvn "-Dtest=GraphCoverageAnalysisTest" test
```

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

以下是 2026-10-10 重新執行全部案例並更新至 `reports` 的密碼檢查器結果；1114 作業加入的 `CourseRegistrationPolicy` 結果見 [1114 作業](#1114-作業課程報名與費用計算)：

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

專案路徑含中文時，請不要使用此參數：JaCoCo agent 會位於專案內的 `.m2`，路徑中的中文傳給測試 JVM 時可能亂碼，導致 JaCoCo 報告無法產生。

## 專案檔案導覽

| 路徑 | 內容 |
|---|---|
| [PasswordValidator.java](src/main/java/tw/edu/testing/PasswordValidator.java) | 受測程式，29 行，附逐行中文註解與 N1～N15 節點 |
| [PasswordValidatorTest.java](src/test/java/tw/edu/testing/PasswordValidatorTest.java) | 九個 JUnit 案例，含輸入、預期結果與目標分析 |
| [EdgeCoverageTest.java](src/test/java/tw/edu/testing/EdgeCoverageTest.java) | 作業 2-1 Edge coverage，3 個案例 |
| [EdgePairCoverageTest.java](src/test/java/tw/edu/testing/EdgePairCoverageTest.java) | 作業 2-2 Edge-Pair coverage，3 個案例 |
| [PathCoverageTest.java](src/test/java/tw/edu/testing/PathCoverageTest.java) | 作業 2-3 Path coverage，3 個案例 |
| [StressCoverageTest.java](src/test/java/tw/edu/testing/StressCoverageTest.java) | 作業 3 Stress Testing coverage，5 個案例 |
| [CourseRegistrationPolicy.java](src/main/java/tw/edu/testing/CourseRegistrationPolicy.java) | 1114 作業受測程式：`canEnroll`、`calculateFee` |
| [CourseRegistrationLogicTest.java](src/test/java/tw/edu/testing/CourseRegistrationLogicTest.java) | 1114 作業 #4 Logical Expressions coverage，L1～L8 |
| [CourseRegistrationDomainTest.java](src/test/java/tw/edu/testing/CourseRegistrationDomainTest.java) | 1114 作業 #5 Input Domain Characterization，D1～D6 |
| [PasswordValidatorCfg.java](src/test/java/tw/edu/testing/PasswordValidatorCfg.java) | 控制流程圖模型：邊、Edge-Pair、Path 需求與不可行需求 |
| [GraphCoverageAnalysisTest.java](src/test/java/tw/edu/testing/GraphCoverageAnalysisTest.java) | 計算並驗證作業 2 三種涵蓋率皆為 100% |
| [Homework2-3.md](docs/Homework2-3.md) | 作業 2、3 測試報告（另有瀏覽器版 `Homework2-3.html`） |
| [1114_homework_report.docx](docs/1114_homework_report.docx) | 1114 作業測試報告 |
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

**專案放在中文路徑，JaCoCo 顯示「Skipping JaCoCo execution due to missing execution data file」？**

舊版 `pom.xml` 由 `jacoco:prepare-agent` 傳入絕對路徑，中文路徑傳給測試 JVM 時會亂碼，`jacoco.exec` 因此沒有產生，`verify` 的門檻也會被略過。目前 `pom.xml` 已改由 Surefire 以相對路徑 `target/jacoco.exec` 掛載 agent，不受專案路徑影響；若仍出現此訊息，請確認已取得最新的 `pom.xml`。

## 1114 作業：課程報名與費用計算

新增受測程式 `CourseRegistrationPolicy` 與兩個測試類別，沿用既有 Java 21、JUnit、JaCoCo 與 PIT 設定；本作業沒有修改 `pom.xml`、`PasswordValidator` 及其測試。

| 作業 | 受測方法 | 測試類別 | 案例數 | 最少案例與涵蓋率 |
|---|---|---|---|---|
| 4 Logical Expressions Coverage | `canEnroll` | `CourseRegistrationLogicTest` | 8（L1～L8） | Predicate 2、Clause 2、Combinatorial 8，皆 100% |
| 5 Input Domain Characterization | `calculateFee` | `CourseRegistrationDomainTest` | 6（D1～D6） | Categorization 3、Combinatorial 6，皆 100% |

- `canEnroll`：以 while 迴圈檢查是否修過先修課程 Programming，再以 if 判斷三子句報名條件 (有名額 OR 有加簽許可) AND 已修先修課程。
- `calculateFee`：標準課程每人 100、進階課程每人 200，以 for 迴圈逐位累加，團報第 2 位起每人折抵 20；人數小於等於 0 時收費 0。
- JaCoCo：`CourseRegistrationPolicy` 行覆蓋率 22/22、分支覆蓋率 14/14。
- 完整套件連同作業 1～3 共 41 個案例，全部通過：作業 1 九個、作業 2、3 十八個（含 `GraphCoverageAnalysisTest` 四個）、本作業十四個。
- 四欄案例、各目標的最少案例集合與分析，見 [1114 作業報告](docs/1114_homework_report.docx)。

```powershell
# 執行全部測試，並檢查 JaCoCo 100% 行覆蓋率門檻
mvn clean verify

# 只執行本作業的案例
mvn "-Dtest=CourseRegistrationLogicTest,CourseRegistrationDomainTest" test

# 只執行單一案例（L1）
mvn "-Dtest=CourseRegistrationLogicTest#givenNoSeatAndNoOverrideAndUnmetPrerequisites_whenCanEnroll_thenFalse" test
```

隨附的 `reports/junit` 是作業 2、3 合併前產生的副本，只含 `PasswordValidatorTest` 與兩個課程測試類別（共 23 個案例）；`reports/jacoco` 已包含 `CourseRegistrationPolicy`；`reports/pit` 仍為密碼檢查器的歷史結果。重新執行後以 `target` 下的新結果為準。

### 測試命名與 Arrange–Act–Assert

新增的 `CourseRegistrationLogicTest` 與 `CourseRegistrationDomainTest` 使用 `given條件_when受測方法_then預期結果` 命名，對應準備的情境、執行的操作與預期結果。這兩個類別的每個測試本文分成 `// Arrange`、`// Act`、`// Assert`；AAA 是本文結構，方法名稱則讓這三項資訊可直接閱讀。

L1～L8、D1～D6 的作業案例編號仍保留在 Javadoc、輸出與報告中，4-1～5-2 的最少案例集合不變。PasswordValidatorTest 保留 `rejectsNull`、`rejectsTooShort` 等原有方法名稱，對應上方 T1～T9 表格與指令。
