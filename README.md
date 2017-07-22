# bitflyer4j ![Travis-CI Build Status](https://travis-ci.org/after-the-sunrise/bitflyer4j.svg?branch=master)

## Overview

[bitflyer4j](https://github.com/after-the-sunrise/bitflyer4j) is a Java wrapper library for the [bitFlyer Lightning](https://lightning.bitflyer.jp/docs?lang=en) API.


## Feature Set


## Getting Started


### Authentication

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
