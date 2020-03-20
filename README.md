# SimpleHttpServer
[![watchers](https://img.shields.io/github/watchers/ktt-development/simplehttpserver?color=5555ff&label=watchers&style=flat-square)](https://github.com/Ktt-Development/simplehttpserver/watchers)
[![stars](https://img.shields.io/github/stars/Ktt-Development/simplehttpserver?color=5555ff&style=flat-square)](https://github.com/Ktt-Development/simplehttpserver/stargazers)
[![forks](https://img.shields.io/github/forks/Ktt-Development/simplehttpserver?color=5555ff&style=flat-square)](https://github.com/Ktt-Development/simplehttpserver/network/members)
[![issues](https://img.shields.io/github/issues/Ktt-Development/simplehttpserver?color=ffaa00&style=flat-square)](https://github.com/Ktt-Development/simplehttpserver/issues)
[![pull](https://img.shields.io/github/issues-pr/ktt-development/simplehttpserver?color=ff55ff&style=flat-square)](https://github.com/Ktt-Development/simplehttpserver/pulls)
[![license](https://img.shields.io/github/license/Ktt-Development/simplehttpserver?color=ff5555&style=flat-square)](https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html)

[![jitpack](https://jitpack.io/v/com.kttdevelopment/simplehttpserver.svg?style=flat-square)](https://jitpack.io/#com.kttdevelopment/simplehttpserver)
[![jitci](https://jitci.com/gh/Ktt-Development/simplehttpserver/svg?style=flat-square)](https://jitci.com/gh/Ktt-Development/simplehttpserver)
[![version](https://img.shields.io/github/v/release/ktt-development/simplehttpserver?color=ff5555&include_prereleases&style=flat-square)](https://github.com/Ktt-Development/simplehttpserver/releases)
[![downloads](https://img.shields.io/github/downloads/ktt-development/simplehttpserver/total?color=ff5555&style=flat-square)](https://github.com/Ktt-Development/simplehttpserver/releases)

Based on the java sun Http Server. Complicated features made easy.

## New Http Server Methods
#### Temporary Contexts
Handlers that will process a single request before removing itself. Useful for single use download links or image hosting.

#### Context Retrieval
Get the contexts that the server has loaded (why wasn't this already a feature?).

## New Exchange Methods
#### HTTP GET
Get requests are parsed into a hashmap.
#### HTTP POST
Post requests are parsed into a hashmap (`multipart/form-data` as well!).

##Http Handlers
#### Redirect Handler
A simple handler to redirect links without leaving a mark in the history.
#### Predicate Handler
A simple handler to handle requests based on a predicate.
#### Root Handler
A handler to help process homepage and 404  requests.
#### File Handler
Host files, set their contexts, control their output, host directories and support for file walking,

## New Features
#### Constants
Static variables for HTTP codes and methods.
#### Http Session
Keep track of the user across different exchanges.
#### Cookies
Get and set cookies.
