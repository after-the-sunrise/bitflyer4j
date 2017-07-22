# bitflyer4j ![Travis-CI Build Status](https://travis-ci.org/after-the-sunrise/bitflyer4j.svg?branch=master)

## Overview

[bitflyer4j](https://github.com/after-the-sunrise/bitflyer4j) is a Java wrapper library for the [bitFlyer Lightning](https://lightning.bitflyer.jp/docs?lang=en) API.


## Feature Set

## Authentication

In order to use the [Private API](https://lightning.bitflyer.jp/docs?lang=en#http-private-api), 
specify the authentication credentials as follows in the environment system variables:
  * bitflyer4j.auth_key
  * bitflyer4j.auth_secret

By default, the library will try to retrieve the variables in the following order:
  1. System properties.
  2. "~/.bitflyer4j" properties file.
  3. "bitflyer4j-site.properties" file in the classpath.
  4. "bitflyer4j-default.properties" file in the classpath.

The library will scan from the top of the list using the first one found, skipping the files which are not available.  
An example "~/.bitflyer4j" properties file should look like as follows:
```properties
# Authentication
bitflyer4j.auth_key=MY_KEY_HERE
bitflyer4j.auth_secret=MY_SECRET_HERE
```
