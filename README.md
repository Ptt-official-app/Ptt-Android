PTT Android 開發求生指南

General Info
---
- Android App Repo: https://github.com/Ptt-official-app/Ptt-Android
- Api Repo: https://github.com/Ptt-official-app/go-openbbsmiddleware
- Api 文件: https://doc.devptt.dev/
- Zeplin 設計稿文件(Mockup): https://app.zeplin.io/project/5d7346333838ff497428d1c5

加入方式
---
由於此專案為自願性，且仍在開發初期，需要你主動加入，可由以下方式加入：
- 在 Telegram 找 @kenhuang1120，會協助你加入 Ptt Android 的 Telegram 群組與取得相關資訊
- 直接在 [Android App Repo](https://github.com/Ptt-official-app/Ptt-Android) 發 PR or Issue


Android 專案架構&資訊
---
此專案參考 [Android Architecture](https://developer.android.com/topic/architecture)，分為 `UI layer`, `Domain layer`, `Data Layer`，以 module 形式切分，使用 Kotlin 開發並使用 Koin 實作 DI。
以下將逐一介紹各 module、職責與所使用的技術

#### APP Module 
對應 `Android Architecture` 的 `UI layer`

Component:
- `Activity`、`Fragment`、etc... : 由於本專案採用 MVVM 架構，所有 data 與 ui 之間的交換都要透過 `ViewModel` 交換，View 與 ViewModel 之間的 data 交換推薦使用 [StateFlow 和 SharedFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow?hl=zh-tw)。
- Navigation: 頁面轉換使用 [Navigation component](https://developer.android.com/guide/navigation/navigation-getting-started)，相關的程式碼統一放在 [Navigation.kt](https://github.com/Ptt-official-app/Ptt-Android/blob/dev/app/src/main/java/cc/ptt/android/Navigation.kt) 管理

#### Domain Module
對應 `Android Architecture` 的 `Domain layer`

Component:
- UseCase: Data layer 與 ViewModel 間的 data 轉換與商業邏輯


#### Data Module
對應 `Android Architecture` 的 `Data layer`

Components﹔

- ApiServices : 本專案採用 OKHttp 作為 Http client，並使用 Retrofit 原始資料轉換成 App 內定義的資料結構，並使用 Flow 傳遞
- `Repository`：將資料公開到 `Domain layer` ，`Repository` 可以根據需要將資料 merge 、 filter 、transform 等等資料操作，這些處理目的是為了提供給外部需要且可靠的資料結構，而不會執行任何商業邏輯。 
- `DataSource`：分為 `remote(network)` 及 `local(database)` ，用以區隔網路來的資料或是 local 端資料。

#### Common Module
擺放其他共用元件(Ex: Logger、extensions、etc...)

How To Start
---

專案使用 Git hooks 在 commit 前進行 coding style 檢查。

到專案 root folder 執行：
```
chmod +x ./script/pre_commit_format.sh && ./script/pre_commit_format.sh
```
之後確認 `.git/hooks` 下已新增 **pre-commit** ，如果找不到 `.git/hooks` 請自行新增 git hook 。

### Coding Style

參考 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html), [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)。

### Git

#### Workflow

遵循 [Git workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)。

#### Git commit message

請盡量使用英文描述 commit message。

### Lint

目前有使用 [spotless](https://github.com/diffplug/spotless) 做程式碼的檢查及整理。

#### spotless

使用 spotless 來為專案 reformat，專案在 build 之前會自動進行一次 reformat ，除此之外執行上面的 [Getting Start](README.md#getting-start) 後在 commit 前 spotless 也會做一次 coding style 的檢查。

spotless 外掛 [ktlint](https://github.com/pinterest/ktlint) 及 [google-java-format](https://github.com/google/google-java-format) 當作他的檢查工具。

平常 coding 時可使用 `gradlew spotlessCheck` 及 `gradlew spotlessApply` 檢查程式碼格式。

##### ktlint

由於 Google 及 Jetbrains 都沒有對外釋出 kotlin 的官方版 lint 工具，目前我們是使用第三方的 ktlint 來做 lint 檢查。

ktlint 的 code style 參考自 [kotlinlang.org](https://kotlinlang.org/docs/reference/coding-conventions.html) 及 [Android Kotlin Style Guide](https://android.github.io/kotlin-guides/style.html)，另外 lint 的 rule 規則可參考 ktlint 的 [Standard rules](https://github.com/pinterest/ktlint/blob/master/README.md#standard-rules) (例外：專案內依然允許 wildcard `import` s)。

##### google-java-format

Google 的 format 工具，專案裡把縮排的規則改成 4 格。

### Configuration

#### API
1. 開發前請確認 `build variant` 設定為 `stagingDebug` 再行開發
2. (optional)自行在專案`local.properties`裡面加入：
```
HOST=https://<api_host_domain>
ACCOUNT=<unit_test_account>
PASSWORD=<unit_test_account_password>
```

> 目前僅支援 [go-openbbsmiddleware](https://github.com/Ptt-official-app/go-openbbsmiddleware) 版本API
