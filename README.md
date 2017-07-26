# bitflyer4j ![Travis-CI Build Status](https://travis-ci.org/after-the-sunrise/bitflyer4j.svg?branch=master)

## Overview

[bitflyer4j](https://github.com/after-the-sunrise/bitflyer4j) is a Java wrapper library for the [bitFlyer Lightning](https://lightning.bitflyer.jp/docs?lang=en) API.


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
It will print the list of available products in the System console.

```java:Sample.java
public class Sample {

    public static void main(String[] args) throws Exception {

        // Create an API instance.
        Bitflyer4j api = new Bitflyer4jFactory().createInstance();

        // Query and print the results.
        System.out.println(api.getMarketService().getTick("BTC_JPY").get());

    }

}
```

For the full list of supported features, please refer to the interface definitions, such as ``MarketService``, ``AccountService`` and/or ``OrderService``.


### Private API Authentication

In order to use the [Private API](https://lightning.bitflyer.jp/docs?lang=en#http-private-api), 
specify the authentication credentials as follows in the environment system variables:
  * `bitflyer4j.auth_key`
  * `bitflyer4j.auth_secret`

By default, the library will try to retrieve the variables in the following priority:
  1. System properties. (`-Dbitflyer4j.auth_key=... -Dbitflyer4j.auth_secret=...`)
  2. `${HOME}/.bitflyer4j` properties file.
  3. `bitflyer4j-site.properties` file in the classpath.

The library will scan from the top of the list, skipping the files which are not available/accessible, 
and will use the first one found per entry.

Confidential parameters shall only be stored privately by using the local `${HOME}/.bitflyer4j` properties 
file.  *DO NOT COMMIT/PUSH, LOG/PRINT, NOR EXPOSE/DISSEMINATE THE CREDENTIALS.* 
 
```properties
# Authentication
bitflyer4j.auth_key=MY_KEY_HERE
bitflyer4j.auth_secret=MY_SECRET_HERE
```


## Feature Sets

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
    - [ ] New Child Order `/v1/me/sendchildorder`
    - [ ] Cancel Child Order `/v1/me/cancelchildorder`
    - [ ] New Parent Order `/v1/me/sendparentorder`
    - [ ] Cancel Parent Order `/v1/me/cancelparentorder`
    - [ ] Cancel All Orders `/v1/me/cancelallchildorders`
  - Order Detail
    - [ ] Child Order List `/v1/me/getchildorders`
    - [ ] Parent Order List `/v1/me/getparentorders`
    - [ ] Parent Order Detail `/v1/me/getparentorder`
    - [ ] Executions `/v1/me/getexecutions`
    - [ ] Open Interest Summary `/v1/me/getpositions`
    - [ ] Trading Commission `/v1/me/gettradingcommission`
- Realtime API
  - [ ] Order Book Snapshot `lightning_board_snapshot_*`
  - [ ] Order Book Update `lightning_board_*`
  - [ ] Ticker `lightning_ticker_*`
  - [ ] Execution `lightning_executions_*`


### Network Proxy

In case if this library needs to be used behind a HTTP proxy, specify the following environmental variables.
The variables are fetched by the library in the same manner as described previously in the authentication section.

```properties
# HTTP Proxy
bitflyer4j.http_proxy_host=127.0.0.1
bitflyer4j.http_proxy_host=8080
```


### Other Configurations

For the complete list of configurable parameters, refer to the `KeyType` javadoc.
