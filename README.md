# Ptt Android App

## Getting Start

專案使用 Git hooks 在 commit 前進行 coding style 檢查。

到專案 root folder 執行：
```
chmod +x ./script/pre_commit_format.sh && ./script/pre_commit_format.sh
```
之後確認 `.git/hooks` 下已新增 **pre-commit** ，如果找不到 `.git/hooks` 請自行新增 git hook 。

## Coding Style

參考 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html), [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide)。

## Git

### Workflow

遵循 [Git workflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow)。

### Git commit message

請盡量使用英文描述 commit message。

## Lint

目前有使用 [spotless](https://github.com/diffplug/spotless) 做程式碼的檢查及整理。

### spotless

使用 spotless 來為專案 reformat，專案在 build 之前會自動進行一次 reformat ，除此之外執行上面的 [Getting Start](README.md#getting-start) 後在 commit 前 spotless 也會做一次 coding style 的檢查。

spotless 外掛 [ktlint](https://github.com/pinterest/ktlint) 及 [google-java-format](https://github.com/google/google-java-format) 當作他的檢查工具。

平常 coding 時可使用 `gradlew spotlessCheck` 及 `gradlew spotlessApply` 檢查程式碼格式。

#### ktlint

由於 Google 及 Jetbrains 都沒有對外釋出 kotlin 的官方版 lint 工具，目前我們是使用第三方的 ktlint 來做 lint 檢查。

ktlint 的 code style 參考自 [kotlinlang.org](https://kotlinlang.org/docs/reference/coding-conventions.html) 及 [Android Kotlin Style Guide](https://android.github.io/kotlin-guides/style.html)，另外 lint 的 rule 規則可參考 ktlint 的 [Standard rules](https://github.com/pinterest/ktlint/blob/master/README.md#standard-rules) (例外：專案內依然允許 wildcard `import` s)。

#### google-java-format

Google 的 format 工具，專案裡把縮排的規則改成 4 格。

## Configuration

### API
1. 開發前請確認 `build variant` 設定為 `stagingDebug` 再行開發
2. 請各位開發者自行在專案`local.properties`裡面加入：
```
host=https://<api_host_domain>
```
> 目前僅支援 [go-openbbsmiddleware](https://github.com/Ptt-official-app/go-openbbsmiddleware) 版本API