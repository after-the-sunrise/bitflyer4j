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

```java
    // Create an API instance.
    Bitflyer4j api = new Bitflyer4jFactory().createInstance();
    
    // Query and print the results.
    System.out.println(api.getMarketService().getProducts().get());
```

For the full list of supported features, please refer to the interface definitions, such as ``MarketService``, ``AccountService`` and/or ``OrderService``.


### Private API Authentication

In order to use the [Private API](https://lightning.bitflyer.jp/docs?lang=en#http-private-api), 
specify the authentication credentials as follows in the environment system variables:
  * ``bitflyer4j.auth_key``
  * ``bitflyer4j.auth_secret``

By default, the library will try to retrieve the variables in the following priority:
  1. System properties. (``-Dbitflyer4j.auth_key=...`` )
  2. ``~/.bitflyer4j`` properties file.
  3. ``bitflyer4j-site.properties`` file in the classpath.

The library will scan from the top of the list, skipping the files which are not available/accessible, 
and will use the first one found per entry.

Confidential parameters shall only be stored privately by using the local "~/.bitflyer4j" properties 
file.  *DO NOT COMMIT/PUSH, PRINT/LOG, NOR EXPOSE THE CREDENTIALS.* 
 
```properties
# Authentication
bitflyer4j.auth_key=MY_KEY_HERE
bitflyer4j.auth_secret=MY_SECRET_HERE
```


## Feature Set

The library is aimed to cover the major feature-set documented in the official documentation. 
The currently implemented features are as follows:  

- HTTP Public API
  - Market List : ``/v1/markets``
  - Order Book : ``/v1/board``
  - Ticker : ``/v1/ticker``
  - Execution History : ``/v1/executions``
  - Exchange Status : ``/v1/gethealth``
  - Chat : ``/v1/getchats``
- HTTP Private API
  - Account Detail : ``GET``
    - Permissions : ``/v1/me/getpermissions``
    - Balance : ``/v1/me/getbalance``
    - Collateral : ``/v1/me/getcollateral``
    - Margin : ``/v1/me/getcollateralaccounts``
    - Address : ``/v1/me/getaddresses``
    - Coin In : ``/v1/me/getcoinins``
    - Coin Out : ``/v1/me/getcoinouts``
    - Bank : ``/v1/me/getbankaccounts``
    - Deposit : ``/v1/me/getdeposits``
    - Withdrawal : ``/v1/me/getwithdrawals``
  - Account Action
    - Withdraw : ``/v1/me/withdraw``
  - Order Detail : ``GET``
    - (TODO)
  - Order Action : ``POST``
    - (TODO)
- Realtime API
  - (TODO)
