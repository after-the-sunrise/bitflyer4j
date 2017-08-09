# bitflyer4j ![Travis-CI Build Status](https://travis-ci.org/after-the-sunrise/bitflyer4j.svg?branch=master) [![Coverage Status](https://coveralls.io/repos/github/after-the-sunrise/bitflyer4j/badge.svg)](https://coveralls.io/github/after-the-sunrise/bitflyer4j)

## Overview

[bitflyer4j](https://github.com/after-the-sunrise/bitflyer4j) (bitFlyer for Java) is a Java wrapper library for the bitFlyer's [Lightning API](https://lightning.bitflyer.jp/docs?lang=en).


## Getting Started

### Repository

Use Maven or Gradle to import the library and its dependencies.

```xml
<dependency>
    <groupId>com.after_sunrise.cryptocurrency</groupId>
    <artifactId>bitflyer4j</artifactId>
    <version>${VERSION}</version>
</dependency>
```

### Sample Code

Copy and paste the following code snippet in the ``main`` method, and run. 

#### Query Tick
```java
public class QueryTickSample {

    public static void main(String[] args) throws Exception {

        Bitflyer4j api = new Bitflyer4jFactory().createInstance();

        System.out.println(api.getMarketService().getTick("BTC_JPY").get());

    }

}
```

#### Send New Order
```java
public class SendOrderSample {

    public static void main(String[] args) throws Exception {

        Bitflyer4j api = new Bitflyer4jFactory().createInstance();

        OrderCreate request = OrderCreate.builder().product("BTC_JPY")
                .type(ConditionType.LIMIT).side(SideType.BUY)
                .price(BigDecimal.TEN).size(BigDecimal.ONE).build();

        System.out.println(api.getOrderService().sendOrder(request).get());

    }

}
```

#### Cancel Existing Order
```java
public class CancelOrderSample {

    public static void main(String[] args) throws Exception {

        Bitflyer4j api = new Bitflyer4jFactory().createInstance();

        OrderCancel request = OrderCancel.builder()
                .product("BTC_JPY").orderId("JOR20150707-055555-022222").build();

        System.out.println(api.getOrderService().cancelOrder(request).get());

    }

}
```

#### Subscribe to Realtime Feed
```java
public class RealtimeSample {

    public static void main(String[] args) throws Exception {

        Bitflyer4j api = new Bitflyer4jFactory().createInstance();

        api.getRealtimeService().addListener(new RealtimeListenerAdapter() {
            @Override
            public void onTicks(List<Tick> values) {
                System.out.println(values);
            }
        });

        api.getRealtimeService().subscribeTick(Arrays.asList("BTC_JPY")).get();

        TimeUnit.SECONDS.sleep(30L);

    }

}
```

For the full list of supported features, please refer to the interface definitions, such as ``MarketService``, ``AccountService`` and/or ``OrderService``.


## Features

### Private API Authentication

In order to use the [Private API](https://lightning.bitflyer.jp/docs?lang=en#http-private-api), 
specify the following authentication variables:
  * `bitflyer4j.auth_key`
  * `bitflyer4j.auth_secret`

By default, the library will try to retrieve the environment variables in the following priority:
  1. Runtime properties. (`-Dbitflyer4j.auth_key=... -Dbitflyer4j.auth_secret=...`)
  2. `${HOME}/.bitflyer4j` properties file.
  3. `bitflyer4j-site.properties` file in the classpath.

The library will scan from the top of the list, skipping the ones which are not available/accessible, 
and will use the first one found per variable.

Note that Confidential parameters shall only be stored privately, 
preferably by using the local `${HOME}/.bitflyer4j` properties 
file.  *DO NOT COMMIT, LOG, NOR DISSEMINATE THE CREDENTIALS.* 
 
```properties:${HOME}/.bitflyer4j
# Authentication
bitflyer4j.auth_key=MY_KEY_HERE
bitflyer4j.auth_secret=MY_SECRET_HERE
```


### Network Proxy

In case if this library is to be used behind a network proxy, specify the following environment variables.
The variables are fetched by the library in the same manner, as described previously in the authentication section.

```properties:${HOME}/.bitflyer4j
# HTTP Proxy
bitflyer4j.http_proxy_type=HTTP
bitflyer4j.http_proxy_host=127.0.0.1
bitflyer4j.http_proxy_port=8080
```


### HTTP Access Throttling

bitFlyer limits the number of HTTP requests allowed per interval.

In this library, each HTTP calls are queued first, and a background thread takes them out of the queue, 
with a throttling mechanism to avoid DOS-attacking the service and comply with the limit.  Therefore, 
each HTTP calls are designed to return a `java.util.concurrent.CompletableFuture` instance, 
which is completed only after the HTTP request has been actually executed by the background thread.

To make these asynchronous HTTP calls synchronous, simply call the `CompletableFuture#get()` method.


### Other Configurations

Although there should be no need to overwrite the defaut configurations, the following parameters are externalized 
and can be overwritten by the environment variables.  For the complete list of configurable parameters and details, 
refer to the `KeyType` javadoc.


|Property Key                          |Default Value                             |Description                                                                           |
|--------------------------------------|------------------------------------------|--------------------------------------------------------------------------------------|
|bitflyer4j.site                       |                                          |Optional free-text label to identify the environment which the client app is running. |
|bitflyer4j.auth_key                   |                                          |Authentication key for the private API.                                               |
|bitflyer4j.auth_secret                |                                          |Authentication secret for the private API.                                            |
|bitflyer4j.http_url                   |https://api.bitflyer.jp                   |Endpoint URL of the service.                                                          |
|bitflyer4j.http_proxy_type            |                                          |Proxy type (DIRECT/HTTP/SOCKS) used for HTTP calls. Leave empty to disable proxy.     |
|bitflyer4j.http_proxy_host            |                                          |Address of the proxy server. Proxy type must be specified for the proxy enablement.   |
|bitflyer4j.http_proxy_port            |                                          |Port of the proxy server. Proxy type must be specified for the proxy enablement.      |
|bitflyer4j.http_timeout               |                                          |HTTP socket/read timeout interval in millis. Leave empty for no timeout.              |
|bitflyer4j.http_limit_interval        |60000                                     |HTTP access throttling interval in millis.                                            |
|bitflyer4j.http_limit_criteria_address|500                                       |Number of allowed HTTP access for a single source IP, within the throttling interval. |
|bitflyer4j.http_limit_criteria_private|200                                       |Number of allowed HTTP access for private API calls, within the throttling interval.  |
|bitflyer4j.http_limit_criteria_dormant|10                                        |Number of allowed HTTP access for dormant accounts, within the throttling interval.   |
|bitflyer4j.pubnub_key                 |sub-c-52a9ab50-291b-11e5-baaa-0619f8945a4f|PubNub subscription key for realtime subscription.                                    |
|bitflyer4j.pubnub_reconnect           |LINEAR                                    |PubNub reconnect policy.                                                              |
|bitflyer4j.pubnub_secure              |true                                      |PubNub secure flag for enabling SSL.                                                  |


### Endpoint Paths

The library is aimed to cover the paths documented in the official documentation. 
Currently implemented paths are as follows:  

- HTTP Public API
  - [x] Market List : `/v1/markets`
  - [x] Order Book : `/v1/board`
  - [x] Order Book : `/v1/board`
  - [x] Ticker : `/v1/ticker`
  - [x] Execution History : `/v1/executions`
  - [x] Exchange Status : `/v1/gethealth`
  - [x] Chat : `/v1/getchats`
- HTTP Private API
  - Account Detail
    - [x] Permissions : `/v1/me/getpermissions`
    - [x] Balance : `/v1/me/getbalance`
    - [x] Collateral : `/v1/me/getcollateral`
    - [x] Margin : `/v1/me/getcollateralaccounts`
    - [x] Address : `/v1/me/getaddresses`
    - [x] Coin In : `/v1/me/getcoinins`
    - [x] Coin Out : `/v1/me/getcoinouts`
    - [x] Bank : `/v1/me/getbankaccounts`
    - [x] Deposit : `/v1/me/getdeposits`
    - [x] Withdrawal : `/v1/me/getwithdrawals`
  - Account Action
    - [x] Withdraw : `/v1/me/withdraw`
  - Order Action
    - [x] New Child Order `/v1/me/sendchildorder`
    - [x] Cancel Child Order `/v1/me/cancelchildorder`
    - [x] New Parent Order `/v1/me/sendparentorder`
    - [x] Cancel Parent Order `/v1/me/cancelparentorder`
    - [x] Cancel All Orders `/v1/me/cancelallchildorders`
  - Order Detail
    - [x] Child Order List `/v1/me/getchildorders`
    - [x] Parent Order List `/v1/me/getparentorders`
    - [x] Parent Order Detail `/v1/me/getparentorder`
    - [x] Executions `/v1/me/getexecutions`
    - [ ] Open Interest Summary `/v1/me/getpositions`
    - [ ] Margin Change History `/v1/me/getmycollateralhistory`
    - [x] Trading Commission `/v1/me/gettradingcommission`
- Realtime API
  - [x] Order Book Snapshot `lightning_board_snapshot_*`
  - [ ] Order Book Update `lightning_board_*`
  - [x] Ticker `lightning_ticker_*`
  - [x] Execution `lightning_executions_*`
