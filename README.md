# bitflyer4j ![Travis-CI Build Status](https://travis-ci.org/after-the-sunrise/bitflyer4j.svg?branch=master)

## Overview

[bitflyer4j](https://github.com/after-the-sunrise/bitflyer4j) is a Java wrapper library for the [bitFlyer Lightning](https://lightning.bitflyer.jp/docs?lang=en) API.


## Feature Set

The library is aimed to cover the major feature-set documented in the official documentation. 
The currently implemented features are as follows:  

- HTTP Public API
  - Market List
  - Order Book
  - Ticker
  - Execution History
  - Exchange Status
  - Chat
- HTTP Private API
  - Permissions
  - ...


## Getting Started

### Sample Code

Copy and paste the following code snippet in the ``main`` method, and run. 
It will print the list of available products in the System console.

```java
    // Create an API instance.
    Bitflyer4j api = new Bitflyer4jFactory().createInstance();
    
    // Query and print the results.
    System.out.println(api.getMarketService().getProducts().get());
```

For the full list of supported features, please refer to the interfaces, such as ``MarketService`` and/or ``OrderService``.


### Private API Authentication

In order to use the [Private API](https://lightning.bitflyer.jp/docs?lang=en#http-private-api), 
specify the authentication credentials as follows in the environment system variables:
  * bitflyer4j.auth_key
  * bitflyer4j.auth_secret

By default, the library will try to retrieve the variables in the following priority:
  1. System properties.
  2. "~/.bitflyer4j" properties file.
  3. "bitflyer4j-site.properties" file in the classpath.

The library will scan from the top of the list, skipping the files which are not available/accessible, 
and will use the first one found per entry.

Confidential parameters, such as the authentication credentials, shall only be stored privately by using 
the local "~/.bitflyer4j" properties file.  *DO NOT COMMIT/PUSH, PRINT/LOG, NOR EXPOSE THE CREDENTIALS.* 
 
```properties
# Authentication
bitflyer4j.auth_key=MY_KEY_HERE
bitflyer4j.auth_secret=MY_SECRET_HERE
```
