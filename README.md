# TheBug 

Spring Boot + Thymeleaf + MyBatis で構築した、**実務でよくあるバグ**を「誤ったコード」と「修正後コード」で対比表示する Web アプリです。

## 技術スタック

- **Spring Boot 3.3** / Spring Web / Thymeleaf
- **MyBatis**（H2 メモリ DB）
- **H2**（組み込み、起動時に schema + DataLoader で初期データ投入）

## 起動方法

```bash
./mvnw spring-boot:run
```

起動後: **http://localhost:8080/**

## 機能

- **トップ (/)**: 分類別にバグ一覧を表示（spring / mybatis / thymeleaf / security 等）
- **バグ詳細**: 各バグに独立したパス（`/bug/001`, `/bug/002` 等）

各バグ詳細ページには以下が表示されます：
1. 概要
2. 発生条件
3. エラーメッセージ
4. **誤ったコード**（赤枠で表示）
5. 原因分析
6. **修正後コード**（緑枠で表示、参照用）
7. 再発防止策

## プロジェクト構成

### コントローラー
- **IndexController**: トップページ（`/`）
- **Bug001Controller ~ Bug008Controller**: 各バグに独立したコントローラー
  - `/bug/001` → Bug001Controller
  - `/bug/002` → Bug002Controller
  - ...（1バグ = 1コントローラー = 1パス）

### Mapper
- **BugMapper**: バグデータの取得・登録（全件・分類別・ID 取得・分類一覧・insert）

### エンティティ
- **Bug**: バグ情報を保持（id, category, bugCode, title, wrongCode, correctCode 等）

### テンプレート
- **共通フラグメント**: `fragments/layout.html`（サイドバー）、`fragments/bug-detail.html`（バグ詳細共通部分）
- **個別ページ**: `bugs/001.html` ~ `bugs/008.html`（各バグ専用テンプレート）
- **トップ**: `index.html`

### 静的リソース
- **CSS**: `static/css/common.css`（共通スタイル）、`static/css/detail.css`（詳細ページ専用）
- **JavaScript**: `static/js/common.js`（共通スクリプト）

### 初期データ
- **DataLoader**: ApplicationRunner で起動時に Java から投入（複数行コードを安全に扱うため）

## 規約

1. **1バグ = 1コントローラー = 1パス**: 各バグに独立したコントローラーとパスを割り当て
2. **CSS/JS は独立ファイル**: インラインスタイル・スクリプトは使用せず、`static/` 配下に配置
3. **共通部分はフラグメント化**: Thymeleaf fragments で共通部分を再利用
4. **コメントは日本語**: コード内コメントは日本語（ビジネス/技術用語はカタカナ）で統一
