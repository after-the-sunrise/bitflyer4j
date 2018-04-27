# bitflyer4j 
[![Build Status][travis-icon]][travis-page] [![Coverage Status][coverall-icon]][coverall-page] [![Maven Central][maven-icon]][maven-page] [![Javadocs][javadoc-icon]][javadoc-page]

* :us: [English](./README.md)
* :jp: [日本語](./README_jp.md)


## 概要

**bitflyer4j** (bitFlyer for Java)は[ビットフライヤー・ライトニング][bf-ltng]APIのラッパーライブラリです。

[ビットフライヤー][bf-site]は暗号通貨を取り扱う日本の取引所であり、他社の取引所と同様にJSON+RESTによるAPIを提供しています。 
このAPI電文や通信方式をカプセル化し、いくつかの便利な機能を加え、静的に型付けされたAPIを提供することがこのライブラリの目的です。

* 静的に型付けされたメソッド呼び出し、引数および戻り値
* `java.util.concurrent.CompletableFuture` によるキューイング・速度制限・メソッドチェインなどの非同期実行
* リアルタイムデータの購読に標準対応
* APIのプラベートキーを文字どおりプライベートに保管
* 作者自身が実践検証済


## 使い方

### ダウンロード

[Maven](https://maven.apache.org/)あるいは[Gradle](https://gradle.org/)を利用して、
ライブラリのJARとその依存関係を[セントラル・レポジトリ][maven-page]より自動で取得します。

#### Maven (`pom.xml`)
```xml
<dependency>
    <groupId>com.after_sunrise.cryptocurrency</groupId>
    <artifactId>bitflyer4j</artifactId>
    <version>${VERSION}</version>
</dependency>
```

### サンプル・コード

下記のコードを`main`文へコピペし、実行してください。

#### 時価の取得

最良気配・直近の価格・数量、取引高などの時価情報を取得。

```java
public class QueryTickSample {

    public static void main(String[] args) throws Exception {

        Bitflyer4j api = new Bitflyer4jFactory().createInstance();

        Tick.Request request = Tick.Request.builder().product("ETH_BTC").build();

        System.out.println(api.getMarketService().getTick(request).get());

        api.close();

    }

}
```

#### 注文作成

注文を新規で作成。（ビットフライヤー用語での子注文のこと） 

```java
public class SendOrderSample {

    public static void main(String[] args) throws Exception {

        Bitflyer4j api = new Bitflyer4jFactory().createInstance();

        OrderCreate.Request request = OrderCreate.Request.builder()
                .product("FX_BTC_JPY").type(ConditionType.LIMIT).side(SideType.BUY)
                .price(new BigDecimal("12345.6789")).size(BigDecimal.ONE).build();

        System.out.println(api.getOrderService().sendOrder(request).get());

        api.close();

    }

}
```

#### 注文取消

既存注文の取り消し。注文ID、あるいは受付IDのいずれかを指定してください。

```java
public class CancelOrderSample {

    public static void main(String[] args) throws Exception {

        Bitflyer4j api = new Bitflyer4jFactory().createInstance();

        OrderCancel.Request request = OrderCancel.Request.builder()
                .product("BTCJPY_MAT1WK").orderId("JOR20150707-055555-022222").build();

        System.out.println(api.getOrderService().cancelOrder(request).get());

        api.close();

    }

}
```

#### リアルタイムデータ購読

[PubNub](https://www.pubnub.com/)ストリーミング時価データの購読を開始。

```java
public class RealtimeSample {

    public static void main(String[] args) throws Exception {

        Bitflyer4j api = new Bitflyer4jFactory().createInstance();

        api.getRealtimeService().addListener(new RealtimeListenerAdapter() {
            @Override
            public void onTicks(String product, List<Tick> values) {
                System.out.println("(" + product + ")" + values);
            }
        });

        System.out.println(api.getRealtimeService().subscribeTick(Arrays.asList("BTC_JPY")).get());

        TimeUnit.SECONDS.sleep(30L);

        api.close();

    }

}
```

その他の機能やサンプルコードについては 
[Bitflyer4jTest](./src/test/java/com/after_sunrise/cryptocurrency/bitflyer4j/Bitflyer4jTest.java)
を参照してください。


## 機能・設定

以下は機能や設定は、用法・用量を守って正しくお使い下さい。

### プライベートAPI認証

[プライベートAPI](https://lightning.bitflyer.jp/docs?http-private-api)を使用するためには、
以下を環境変数あるいは設定ファイルにて指定します：
  * `bitflyer4j.auth_key`
  * `bitflyer4j.auth_secret`

APIライブラリは初期化される際、以下を探索します：
  1. Javaの実行時環境変数 (`java -Dbitflyer4j.auth_key=... -Dbitflyer4j.auth_secret=...`)
  2. `${HOME}/.bitflyer4j`設定ファイル
  3. クラスパス中の`bitflyer4j-site.properties`ファイル

APIライブラリは上記リストの先頭から探索を始め、存在しないもの・アクセス不可なものは無視し、最初に見つけた値を使用します。

これらのプライベートな設定値は`${HOME}/.bitflyer4j`を利用して、ローカルにのみ保存することをお勧めします。
*ログ出力やコミットなどで、設定値を公開しないでください。* 
 
```properties:${HOME}/.bitflyer4j
# Authentication
bitflyer4j.auth_key=MY_KEY_HERE
bitflyer4j.auth_secret=MY_SECRET_HERE
```
`.bitflyer4j`の雛形ファイルは 
[こちら](./src/test/resources/.bitflyer4j)
からダウンロードできます。


### ネットワーク・プロキシ設定

ライブラリをネットワーク・プロキシの内側から使用する場合、以下の環境変数を設定してください。
これらの変数は前述のプライベート認証と同様の探索方法によって読み込まれます。

```properties:${HOME}/.bitflyer4j
# HTTP Proxy
bitflyer4j.http_proxy_type=HTTP
bitflyer4j.http_proxy_host=127.0.0.1
bitflyer4j.http_proxy_port=8080
```


### HTTPアクセス速度制限

ビットフライヤーは一定時間内に要求できるHTTPリクエストの回数に制限を設けています。

このライブラリでは、それぞれのHTTPリクエストはまずキューイングされ、バックグラウンドのスレッドがキューから順にリクエストを取り出し、
速度制限を設けることでDOS攻撃してしまうことを予防しています。そのため、それぞれのHTTPリクエストは
`java.util.concurrent.CompletableFuture` を戻り値とするように実装され、
HTTPリクエストがバックグランドで実際に処理されたタイミングで完了する仕組みとなっています。 

この非同期HTTPリクエストを同期化するためには、単純に`CompletableFuture#get()`を呼び出してください。


### その他の設定値

原則、以下の設定は変更する必要はありませんが、それぞれ外部化されているため、必要に応じて環境変数で上書きできます。それぞれの詳細は
[KeyType](./src/main/java/com/after_sunrise/cryptocurrency/bitflyer4j/core/KeyType.java)
を参照してください。


|キー項目                               |初期値                                     |説明                                                                                  |
|--------------------------------------|------------------------------------------|--------------------------------------------------------------------------------------|
|bitflyer4j.site                       |local                                     |実行環境を識別するための任意の文字列。|
|bitflyer4j.auth_key                   |                                          |プライベートAPIの認証キー。|
|bitflyer4j.auth_secret                |                                          |プライベートAPIの秘密文字列。|
|bitflyer4j.http_url                   |https://api.bitflyer.jp                   |サービスのエンドポイントURL。|
|bitflyer4j.http_proxy_type            |                                          |HTTPのプロキシ種別(DIRECT/HTTP/SOCKS)、無効にする場合は空欄。|
|bitflyer4j.http_proxy_host            |                                          |HTTPのプロキシサーバーのアドレス。プロキシ種別の指定が必須。|
|bitflyer4j.http_proxy_port            |                                          |HTTPのプロキシサーバーのポート番号。プロキシ種別の指定が必須。|
|bitflyer4j.http_timeout               |180000                                    |HTTPのソケット通信タイムアウト（ミリ秒）。無制限とする場合は空欄。|
|bitflyer4j.http_threads               |8                                         |HTTPリクエストの並列スレッド数。|
|bitflyer4j.http_limit_interval        |60000                                     |HTTPリクエストの回数制限時間（ミリ秒）。|
|bitflyer4j.http_limit_criteria_address|500                                       |単一IPアドレスからの制限時間内アクセス可能回数。|
|bitflyer4j.http_limit_criteria_private|200                                       |プライベートAPIの制限時間内アクセス可能回数。|
|bitflyer4j.http_limit_criteria_dormant|10                                        |休眠口座の制限時間内アクセス可能回数。|
|bitflyer4j.realtime_type              |                                          |リアルタイムデータの購読に使用する実装の種別|
|bitflyer4j.pubnub_key                 |sub-c-52a9ab50-291b-11e5-baaa-0619f8945a4f|PubNub購読キー。|
|bitflyer4j.pubnub_reconnect           |LINEAR                                    |PubNub再接続方式。|
|bitflyer4j.pubnub_secure              |true                                      |PubNubのSSLフラグ。|
|bitflyer4j.socket_endpoint            |https://io.lightstream.bitflyer.com       |Socket.IOのエンドポイントURL。|


## エンドポイント

現在、以下のエンドポイントがライブラリに実装されています:

- HTTP Public API
  - [x] マーケットの一覧 : `/v1/markets`
  - [x] マーケットの一覧 USA : `/v1/markets/usa`
  - [x] マーケットの一覧 EUR : `/v1/markets/eu`
  - [x] 板情報 : `/v1/board`
  - [x] ティッカー : `/v1/ticker`
  - [x] 約定履歴 : `/v1/executions`
  - [x] チャット : `/v1/getchats`
  - [x] チャット USA : `/v1/getchats/usa`
  - [x] チャット EUR : `/v1/getchats/eu`
  - [x] 取引所の状態 : `/v1/gethealth`
  - [x] 板の状態 : `/v1/getboardstate`
- HTTP Private API
  - 口座（参照）
    - [x] API キーの権限を取得 : `/v1/me/getpermissions`
    - [x] 資産残高を取得 : `/v1/me/getbalance`
    - [x] 証拠金の状態を取得 : `/v1/me/getcollateral`
    - [x] 証拠金の状態を取得（通貨別） : `/v1/me/getcollateralaccounts`
    - [x] 預入用アドレス取得 : `/v1/me/getaddresses`
    - [x] 仮想通貨預入履歴 : `/v1/me/getcoinins`
    - [x] 仮想通貨送付履歴 : `/v1/me/getcoinouts`
    - [x] 銀行口座一覧取得 : `/v1/me/getbankaccounts`
    - [x] 入金履歴 : `/v1/me/getdeposits`
    - [x] 出金履歴 : `/v1/me/getwithdrawals`
  - 口座（操作）
    - [x] 出金 : `/v1/me/withdraw`
  - トレード（発注）
    - [x] 新規注文を出す : `/v1/me/sendchildorder`
    - [x] 注文をキャンセルする : `/v1/me/cancelchildorder`
    - [x] 新規の親注文を出す（特殊注文） : `/v1/me/sendparentorder`
    - [x] 親注文をキャンセルする : `/v1/me/cancelparentorder`
    - [x] すべての注文をキャンセルする : `/v1/me/cancelallchildorders`
  - トレード（参照）
    - [x] 注文の一覧を取得 : `/v1/me/getchildorders`
    - [x] 親注文の一覧を取得 : `/v1/me/getparentorders`
    - [x] 親注文の詳細を取得 : `/v1/me/getparentorder`
    - [x] 約定の一覧を取得 : `/v1/me/getexecutions`
    - [x] 建玉の一覧を取得 : `/v1/me/getpositions`
    - [x] 証拠金の変動履歴を取得 : `/v1/me/getcollateralhistory`
    - [x] 取引手数料を取得 : `/v1/me/gettradingcommission`
- リアルタイムAPI
  - PubNub
    - [x] 板情報の差分 : `lightning_board_*`
    - [x] 板情報 : `lightning_board_snapshot_*`
    - [x] ティッカー : `lightning_ticker_*`
    - [x] 約定 : `lightning_executions_*`
  - Socket.IO
    - [x] 板情報の差分 : `lightning_board_*`
    - [x] 板情報 : `lightning_board_snapshot_*`
    - [x] ティッカー : `lightning_ticker_*`
    - [x] 約定 : `lightning_executions_*`


[bf-site]:https://bitflyer.jp?bf=yolu1tm3
[bf-ltng]:https://lightning.bitflyer.jp/?bf=yolu1tm3
[travis-page]:https://travis-ci.org/after-the-sunrise/bitflyer4j
[travis-icon]:https://travis-ci.org/after-the-sunrise/bitflyer4j.svg?branch=master
[coverall-page]:https://coveralls.io/github/after-the-sunrise/bitflyer4j?branch=master
[coverall-icon]:https://coveralls.io/repos/github/after-the-sunrise/bitflyer4j/badge.svg?branch=master
[maven-page]:https://maven-badges.herokuapp.com/maven-central/com.after_sunrise.cryptocurrency/bitflyer4j
[maven-icon]:https://maven-badges.herokuapp.com/maven-central/com.after_sunrise.cryptocurrency/bitflyer4j/badge.svg
[javadoc-page]:https://www.javadoc.io/doc/com.after_sunrise.cryptocurrency/bitflyer4j
[javadoc-icon]:https://www.javadoc.io/badge/com.after_sunrise.cryptocurrency/bitflyer4j.svg
