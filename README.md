# Web and Mobile (iOS) Demo Automation Project

This multi-module Java project for test automation was built using Maven. It comprises four main modules: module-app, module-data, module-tests, and module-tools. The project supports testing for both **Web** and **Mobile** UIs.<br/>

### Supported Platforms
* (Mac) OS X
* Linux
* Windows

### Supported Browsers
* Google Chrome
* Mozilla Firefox
* Safari [Mac OS X]

### Supported iOS Simulators:
* iPad Pro (11-inch) 4th generation - iOS 16.2

## Table of Contents
1. Getting Started<br/>
2. Project Structure<br/>
2.1. Configuring project
3. Running Tests<br/>
3.1. Web Test Execution<br/>
3.2. Mobile Test Execution<br/>
4. Generating and Reviewing the Allure Report<br/>

## 1. Getting Started
#### This guide assumes to install the following:
* [Xcode](https://developer.apple.com/documentation/safari-developer-tools/installing-xcode-and-simulators) (v.16.1)<br/>
* Appium2 with [appium-xcuitest-driver](https://github.com/appium/appium-xcuitest-driver)<br/>
```
#Install Homebrew, then NodeJS, and verify versions:
    brew install node
    node -v
    npm -v
 #Install Appium and verify the version:
    npm install -g appium
    appium -v
 #Basic commands for working with drivers:
    appium driver list
    appium driver install <driver name>
    appium driver update <driver name>
    appium driver uninstall <driver name>
 #Basic commands for working with plugins:
    appium plugin list
    appium plugin install <plugin name>
    appium plugin update <plugin name>
    appium plugin uninstall <plugin name>
 #Starting the Appium server with default settings (after installing Appium-Doctor):
    appium
  ```
* [Appium-Doctor](https://www.npmjs.com/package/appium-doctor) (a tool for checking Appium installation). It checks if Node.js, JDK, Android SDK, and Xcode are installed.<br/>
```
#Check the installation of Appium and dependencies:
    npm install -g appium-doctor
#Run to check for iOS (after installing Carthage):
    appium-doctor --ios
```
* Building [WebDriverAgent (WDA) in Xcode](https://medium.com/@begunova/automating-real-ios-devices-with-appium-1fa729b58f51) for iOS Application Automation using Appium.<br/>
  
And ensure you have **Maven** and **Java 11** installed.<br/>

## 2. Project Structure
This template provides a UI test automation solution suitable for both web and mobile (iOS). It is structured as a Maven multi-module project, with the following relationships between the modules:<br/>
```
|———core-frmk
    |—-pom.xml
    |—-.env
    |—-app
        |—-[APP_NAME].app
    |—-module-app
        |—-pom.xml
        |—-src
            |—-main
                |—-java
    |—-module-data
        |—-src
            |—-main
                |—-java
        |—-pom.xml
    |—-module-tests
        |—-src
            |—-test
                |—-java
                    |—-resources
                        |—-suites
                            |—-testng.xml
                        |—-allure.properties
        |—-pom.xml
    |—-module-tools
	    |—-src
	        |—-main
	            |—-java
    |—-pom.xml
```
1. **Root Module (core-frmk)**. Serves as the parent ***pom.xml*** for all submodules. Contains common dependencies and configurations for the entire project structure. Defines four modules that are part of the project: module-app, module-data, module-tests, and module-tools.<br/>
    - ***.env*** file is a text file used for storing configuration settings and environment variables in a project. It allows you to easily manage configuration parameters such as APPIUM_URL, REMOTE_URL, BASE_URL, APP_NAME, and APP_BUNDLE_ID without hardcoding them directly in the code.<br/>
    - the local ***app*** directory contains the last build of the iOS app.<br/>
2. **Modules**:
    - ***module-app***. The main module contains the application page-object classes and custom UI elements. Depends on `module-data` for data access and `module-tools` for utilizing utilities or tools.<br/>
    - ***module-data***. It should contain code for data operations, such as database access or data manipulation (data providers, data generators, data models, etc). Depends on `module-tools`.<br/>
    - ***module-tools***. A module designed to provide common utilities and functions (Selenium wrappers, Reporters, and other generic libraries) that can be used across many other modules. The most "general" module that supports other modules by providing shared functionality.<br/>
    - ***module-tests***. This module contains implemented test cases to verify the functionality of web pages and mobile screens, and the ***testng.xml*** file is a configuration file that helps organize the test classes. Depends on `module-app` and `module-data`, allowing for testing their functionality in a controlled environment.<br/>

### 2.1. Configuring project
Run this command from the start to ensure that you don't have anything corrupted.<br/>

      $ cd core-frmk
      $ mvn clean install -U -DskipTests=true

## 3. Running Tests
Maven is used as a tool for building and managing the project, and additional options have been added for executing the tests.<br/>
*Options:*<br/>
`-Denv={String}` - environment for execution test;<br/>
`-Dbrowser={String}` - name of Browser on which should be executed web test;<br/>
`-Dtest={String}` - name of Test class.<br/>

*Common maven commands:*
- `mvn clean` removes the `target` folder before executing the tests. This step is crucial, as it ensures that the Allure tool does not incorporate results from previous runs into the report.<br/>
- `mvn test` is commonly used to run automated tests from the `testng.xml` file within the project. When run without additional parameters, it will execute the tests using the environment settings specified in the system properties, defaulting to the Chrome browser for web tests. It could usually contain more parameters, like browser, env, etc.<br/>

*For example:*

      $ cd core-frmk
      $ mvn clean test

### 3.1. Web Test Execution
Create a hidden `.env` file containing the `BASE_URL` parameter to configure the web URL for testing. This will allow you to easily manage the base URL used during your web tests.<br/>
```
BASE_URL=https://example.com
```
To execute the web test itself the next command line should be used:<br/>

      $ cd core-frmk
      $ mvn clean test -Denv=local -Dtest=LoginWebTest -Dbrowser=firefox

### 3.2. Mobile Test Execution
Create a hidden `.env` file containing the parameters `APPIUM_URL`, `APP_NAME`, and `APP_BUNDLE_ID` to configure the testing environment for mobile tests.<br/>
Place your iOS application (e.g., 'Demo.app') into the local `app` directory.<br/>
The `APP_BUNDLE_ID` is a unique bundle identifier for a mobile application on the iOS or Android platform. This identifier is used to differentiate apps in systems and services such as the App Store or Google Play.<br/>
```
APPIUM_URL=http://0.0.0.0:4723/
APP_NAME="Demo.app"
APP_BUNDLE_ID=com.example.myapp
```
To execute the mobile test itself the next command line should be used:<br/>

      $ cd core-frmk
      $ mvn clean test -Denv=regression -Dtest=LoginMobileTest

## 4. Generating and Reviewing the Allure Report
To generate a report by Allure after tests have finished, use the following command:<br/>

      $ mvn allure:report

The report can be found in the **root** folder of a project. The folder named `allure-report` contains the generated report. Just open the `index.html` file in a browser.<br/>
