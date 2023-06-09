# TACoS
Time Attendance Coordination on Slack  
略して TACoS (タコス)  

# 機能
## シフト
- 勤務予定のシフトを入力する
- 過去に入力したシフトを確認する

## 勤怠打刻
実際の勤務時間を把握するために勤怠打刻機能を提供したい  

- 入退室
- 休憩

## 給与
- シフトから給料日に支給予定の給与を計算する
- 給与実績を記録する
- 過去nヶ月の給与実績から年間の給与支給予定額を予測する

## プラグイン
本体の機能をなるべく小さくして機能をくっつけたい  
プラグインという提供形態でなくてもマイクロサービスな感じで提供してもいいかも  

### ジョブカン

- シフト入力
    - 既に入力されてるものをジョブカンに反映
    - 既存のシフト入力フローの最後に「この内容をジョブカンに反映しますか？(再度表示しない)」みたいな画面を表示する
- 打刻
    - 給与実績を打刻するためのTACoSの打刻とジョブカンの打刻を同時に行う
    - どちらかが失敗した場合slackでエラーメッセージを返す
- 出勤簿からのデータの吸い出し
- (管理者側での承認機能？)
- (アルバイト以外の勤務形態 例)業務委託 の打刻機能？)

### スプシ

- シフト入力
    - A列をhonahukuとして登録、ではなく特定の文字列が含まれる列を入力対象とする、みたいな感じ
    - 入力前プレビュー

### 万屋一家

- 給与明細PDFダウンロード
    - DBにPDFを保存
        - ユーザーが後からダウンロード出来るように
    - 給与実績反映

### メール

- 日報
- 月報
- 入力前プレビュー

### テキスト

- 勤務状況のテキスト出力
- 正規表現？

## データインポート/エクスポート
計画中...  

# 設計
## アーキテクチャ
- 目的
    - 勤怠管理と給与管理
        - 簡単にシフトの追加・修正を行う
        - 支給額の予測
        - 税金管理
- コンポーネント
    - フロントエンド(Vue.js)
        - シフト入力
        - 過去シフト表示
        - エラーハンドリング
            - フロントエンドとバックエンドのエラーをユーザーに表示する
    - バックエンド(Scala)
        - Google Calendar API
            - DBの勤務シフトをカレンダーに同期する
        - データベース
            - シフト入力
            - ユーザー情報登録・取得・更新
            - ユーザー情報
        - フロントへのレスポンス
        - 認証
            - メアド・パスワード
            - OAuth
                - GitHub
                - Twitter
    - データベース
        - 勤務シフト
        - 勤怠実績
            - 勤務日
            - 勤務開始・終了時間
            - 休憩開始・終了時間
            - 有給
        - ユーザー情報
            - 時給
            - 暗号化される
            - 運営も中を見れない
    - セキュリティ
        - ハッシュ化
        - ソルト
        - HTTPS
    - API
    - エラーハンドリング
    - 認証認可
        - パスワード管理
        - アクセストークン管理
        - セッション管理

## DB設計
設計中...  

## API設計

- auth
    - POST /auth/signup
    - POST /auth/login
    - POST /auth/logout
- shifts (シフトに関するエンドポイント)
    - POST /shifts (新しいシフトの作成)
    - GET /shifts/{shiftId} (特定のシフトの取得)
    - PUT /shifts/{shiftId} (特定のシフトの更新)
    - DELETE /shifts/{shiftId} (特定のシフトの削除)
    - GET /shifts/batch (条件を指定した複数シフトの取得)
    - POST /shifts/batch (複数のシフトの一括操作)
- timecard (打刻と勤務記録に関するエンドポイント)
    - GET /timecard (現在の打刻情報の取得)
    - POST /timecard (新しい打刻の作成)
    - GET /timecard/{tcId} (特定の打刻の取得)
    - PUT /timecard/{tcId} (特定の打刻の更新)
    - DELETE /timecard/{tcId} (特定の打刻の削除)
    - GET /timecard/batch (条件を指定した勤務記録の取得)
    - POST /timecard/batch (複数の勤務記録の操作)
- sync
    - POST /sync/{service} (特定のサービスとの同期)
- settings
    - GET /settings/user (ユーザー情報の取得)
    - PUT /settings/user (ユーザー情報の更新)
    - GET /settings/workplace (勤務先情報の取得)
    - PUT /settings/workplace (勤務先情報の更新)
- export
    - GET /export/{dataType} (特定のデータ種別のエクスポート)

# 技術選定
## フロントエンド
TypeScript + Vue.js  
  
& Bootstrap ?  

## バックエンド
Scala + Play Framework  

## DB
Cloudflare D1  
