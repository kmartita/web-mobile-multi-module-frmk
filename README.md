![Java](https://img.shields.io/badge/java-white?style=for-the-badge&logo=openjdk&logoSize=auto&color=%23e69138&cacheSeconds=3600&link=https%3A%2F%2Fwww.oracle.com%2Fjava%2F)
![Maven](https://img.shields.io/badge/maven-white?style=for-the-badge&logo=apachemaven&logoSize=auto&color=%23cc0000&cacheSeconds=3600&link=https%3A%2F%2Fmaven.apache.org)
![Selenium](https://img.shields.io/badge/selenium-info?style=for-the-badge&logo=selenium&logoSize=auto&color=%23d9ead3&cacheSeconds=3600&link=https%3A%2F%2Fwww.selenium.dev%2Fdocumentation%2F)
![Appium2.0](https://img.shields.io/badge/appium-info?style=for-the-badge&logo=appium&logoSize=auto&color=%23f4cccc&cacheSeconds=3600&link=https%3A%2F%2Fappium.io%2Fdocs%2Fen%2F2.0%2F)
![Xcode](https://img.shields.io/badge/Xcode-info?style=for-the-badge&logo=Xcode&color=%23cfe2f3&cacheSeconds=3600)
![TestNG](https://img.shields.io/badge/testng-white?style=for-the-badge&logoSize=auto&color=%233d85c6&cacheSeconds=3600&link=https%3A%2F%2Ftestng.org)
![Allure Report](https://img.shields.io/badge/allure-white?style=for-the-badge&logoSize=auto&color=%23f1c232&cacheSeconds=3600&link=https%3A%2F%2Fallurereport.org)


# Web & Mobile (iOS) Automation Demo Project: Selenium & Appium
This multi-module Java framework for test automation has been built with Maven and utilizes Selenium and Appium for browser and mobile automation, providing fast and reliable execution of web & mobile tests with detailed reporting and integration capabilities.

### Tech Stack:
- **Programming Language**: Java
- **Build Tool**: Maven
- **UI Automation Frameworks**: Selenium, Appium
- **Testing Framework**: TestNG
- **Reporting**: Allure Report
- **IDE for iOS development**: Xcode

### Requirements:
Requires **Java 17**, **Maven 3.9.x**, **Allure Report 2.33.x**, and **Xcode 16.x** to be installed and properly configured on your local machine.<br/>

### Supported Platform:
* (Mac) OS X

## Table of Contents
1. [Getting Started](#one)
2. [Framework Structure](#two)
   * 2.1. [Configuring project](#two-one)
3. [Tests Execution](#three)
   * 3.1. [Web:](#three-one)
   * 3.2. [Mobile:](#three-two)
4. [Generate and Review Allure Report](#four)


<a id="one"></a>
### 1. Getting Started
**This guide assumes to install the following:**
1. [Xcode](https://developer.apple.com/documentation/safari-developer-tools/installing-xcode-and-simulators) (>= v.16.4)<br/>
   * Update **Components** (Xcode -> Settings -> Components ) to the latest versions<br/>
//TODO - Components
2. Appium2 with [appium-xcuitest-driver](https://github.com/appium/appium-xcuitest-driver)<br/>
3. [Appium-Doctor](https://www.npmjs.com/package/appium-doctor) (a tool for checking Appium installation). It checks if Node.js, JDK, Android SDK, and Xcode are installed.<br/>

##### Install Homebrew, then NodeJS, and verify versions:
```commandline
brew install node
node -v
npm -v
```
##### Install Appium and verify the version:
```commandline
npm install -g appium
appium -v
```
##### Basic commands for working with Appium [Drivers](https://appium.io/docs/en/2.5/ecosystem/drivers/):
```bash
appium driver list
appium driver install <driver name>
appium driver update <driver name>
appium driver uninstall <driver name>
```
   * *Parameter:*<br/>
     `<driver name>` - XCUITest driver
##### Basic commands for working with Appium [Plugins](https://appium.io/docs/en/2.5/ecosystem/plugins/):
```bash
appium plugin list
appium plugin install <plugin name>
appium plugin update <plugin name>
appium plugin uninstall <plugin name>
```
##### Starting the Appium server with default settings (after installing Appium-Doctor):
```commandline
appium
```
##### Check the installation of Appium and dependencies:
```commandline
npm install -g appium-doctor
```
##### Run to check for iOS (after installing Carthage):
```commandline
appium-doctor --ios
```

#### Build [WebDriverAgent (WDA) in Xcode](https://docs.katalon.com/katalon-studio/manage-projects/set-up-projects/mobile-testing/ios/mobile-install-webdriveragent-for-real-ios-devices-in-katalon-studio) for iOS Application Automation using Appium.<br/>
Building WebDriverAgent (WDA) in Xcode is crucial for using Appium to automate iOS applications. WDA is a server that allows Appium to interact with iOS devices and simulators. 
Here’s a step-by-step guide on how to build and configure WebDriverAgent in Xcode:<br/>
1. Locate the WebDriverAgent Directory<br/>
   Path typically:<br/>
   ```text
   /usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent
   ```
   Or:<br/>
   ```text
   ~/.npm-global/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent
   ```
2. Open WebDriverAgent in Xcode<br/>
   * Open Xcode<br/>
   * Select **File** -> **Open**<br/>
   * Navigate to the WebDriverAgent folder you found in the previous step<br/>
   * Open **WebDriverAgent.xcodeproj**<br/>

3. Select Scheme: At top left, choose **WebDriverAgentRunner** scheme<br/>

4. Configure Signing<br/>
   Ensure the project is set to use automatic signing:<br/>
   * Click on the **WebDriverAgentRunner** in the project navigator<br/>
   * Select the target (on the right side in the project settings)<br/>
   * Under the **Signing & Capabilities** tab:<br/>
     * Select your development team from the **Team** drop-down<br/>
     * Make sure the **Automatically manage signing** option is enabled<br/>

5. Connect Device<br/>
   * Connect your physical iOS device via USB or choose a simulator<br/>
   * In the top left corner, select your device or simulator from the device drop-down list<br/>

6. Build the Project<br/>
   * With the correct scheme selected and device input, click on **Product** in the Xcode menu.<br/>
   * Select **Build** or press **Command + B**.<br/>
   * Monitor the **Issue Navigator** (the triangle icon) for any errors.<br/>

7. Fix Errors (if any): Resolve signing issues, dependencies, or iOS compatibility<br/>
8. Run **WebDriverAgent Server**:<br/>
   * Open a new terminal window<br/>
   * Run the following commands to start the Appium server:<br/>
```shell
   appium --log-level debug
```
   * WebDriverAgent will launch automatically<br/>
9. Verify WebDriverAgent Status:<br/>
```html
   http://<device-ip>:8100/status
```
Replace `<device-ip>` with the IP address of your iOS device while ensuring that the device’s WDA server is running.<br/>

#### As a result, your WebDriverAgent should look like this:
//TODO - WebDriverAgent


#### Build Test Application:
1. Clone (or use option 'Open with Xcode') the project (iOS app for testing) from this [GitHub repository](https://github.com/kmartita/xcode-test-app).
   This is a simple 'Hello World!' application for iOS devices using Swift 6.1.2, created for testing purposes with Appium.
2. Select a iOS simulator (e.g., `iPad (A16)`) in Xcode and build application. After a successful build, the .app file will appear in `DerivedData`:<br/>
```text
   ~/Library/Developer/Xcode/DerivedData/YourProject-XXXXXX/Build/Products/Debug-iphonesimulator/TestApp.app
```
//TODO - TestApp


<a id="two"></a>
### 2. Framework Structure
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
        |—-pom.xml
        |—-src
            |—-main
                |—-java
    |—-module-tests
        |—-pom.xml
        |—-src
            |—-test
                |—-java
                    |—-resources
                        |—-suites
                            |—-testng.xml
                        |—-allure.properties
    |—-module-tools
        |—-pom.xml
        |—-src
            |—-main
               |—-java
```
1. **Root Module (core-frmk)**. Serves as the parent ***pom.xml*** for all submodules. Contains common dependencies and configurations for the entire project structure. Defines four modules that are part of the project: module-app, module-data, module-tests, and module-tools.<br/>
    - ***.env*** file is a text file used for storing configuration settings and environment variables in a project. It allows you to easily manage configuration parameters such as APPIUM_URL, REMOTE_URL, BASE_URL, APP_NAME, and APP_BUNDLE_ID without hardcoding them directly in the code.<br/>
    - the local ***app*** directory contains the last build of the iOS app.<br/>
2. **Modules**:
    - ***module-app***. The main module contains the application page-object classes and custom UI elements. Depends on `module-data` for data access and `module-tools` for utilizing utilities or tools.<br/>
    - ***module-data***. It should contain code for data operations, such as database access or data manipulation (data providers, data generators, data models, etc). Depends on `module-tools`.<br/>
    - ***module-tools***. A module designed to provide common utilities and functions (Selenium wrappers, Reporters, and other generic libraries) that can be used across many other modules. The most "general" module that supports other modules by providing shared functionality.<br/>
    - ***module-tests***. This module contains implemented test cases to verify the functionality of web pages and mobile screens, and the ***testng.xml*** file is a configuration file that helps organize the test classes. Depends on `module-app` and `module-data`, allowing for testing their functionality in a controlled environment.<br/>

<a id="two-one"></a>
#### 2.1. Configuring project
Run this command from the start to ensure that you don't have anything corrupted.<br/>
```bash
cd core-frmk
mvn clean install -U -DskipTests=true
```

<a id="three"></a>
### 3. Tests Execution
Maven is used as a tool for building and managing the project, and additional options have been added for executing the tests.<br/>
*Options:*<br/>
`-Denv={String}` - environment for execution test;<br/>
`-Dbrowser={String}` - name of Browser on which should be executed web test;<br/>
`-Dtest={String}` - name of Test class.<br/>

##### Common Maven Commands:
1. Removes the `target` directory before running tests. Ensures that previous results do not affect the Allure report.
```bash
mvn clean
```
2. Runs tests defined in your `testng.xml`, using default system property settings (e.g., env, browser). By default, it executes with Chrome.<br/>
```bash
mvn test
```

*For example:*
```bash
cd core-frmk
mvn clean test
```

<a id="three-one"></a>
#### 3.1. Web:
Create a hidden `.env` file containing the `BASE_URL` parameter to configure the web URL for testing. This will allow you to easily manage the base URL used during your web tests.<br/>
```properties
BASE_URL = https://example.com
```
To execute the web test itself the next command line should be used:<br/>
```bash
cd core-frmk
mvn clean test -Denv=local -Dtest=LoginWebTest -Dbrowser=firefox
```

<a id="three-two"></a>
#### 3.2. Mobile:
Create a hidden `.env` file containing the parameters `APPIUM_URL`, `APP_NAME`, and `APP_BUNDLE_ID` to configure the testing environment for mobile tests.<br/>
Place your iOS application (e.g., 'Demo.app') into the local `app` directory.<br/>
The `APP_BUNDLE_ID` is a unique bundle identifier for a mobile application on the iOS or Android platform. This identifier is used to differentiate apps in systems and services such as the App Store or Google Play.<br/>
```properties
APPIUM_UR = http://0.0.0.0:4723/
APP_NAME = "Demo.app"
APP_BUNDLE_ID = com.example.myapp
```
To execute the mobile test itself the next command line should be used:<br/>
```bash
cd core-frmk
mvn clean test -Denv=regression -Dtest=LoginMobileTest
```

<a id="four"></a>
### 4. Generate and Review Allure Report
To generate a report by Allure after tests have finished, use the following command:<br/>
```bash
mvn allure:report
```
The report can be found in the **root** folder of a project. The folder named `allure-report` contains the generated report. Just open the `index.html` file in a browser.<br/>

Alternatively, Allure provides a command to serve the report directly:<br/>
```bash
mvn allure:serve 
```
This command starts a local web server and automatically opens the generated report in your default browser.<br/>

An example of the generated [Allure TestNG](https://allurereport.org/docs/testng/) report looks like this:<br/>
#### Mobile:
<img width="1920" height="1038" alt="Mobile_Overview" src="https://github.com/user-attachments/assets/a42bc769-0b99-4792-81a4-4a9cb28f19f4" />
<img width="1920" height="466" alt="Mobile_Packages" src="https://github.com/user-attachments/assets/cd350a01-eca5-4c62-9cca-f7bf2e413b9c" />

#### Web:
<img width="1920" height="1036" alt="Web_Overview" src="https://github.com/user-attachments/assets/911a14b1-7511-483c-a693-ed7dcb663ef4" />
<img width="1920" height="450" alt="Web_Packages" src="https://github.com/user-attachments/assets/c6e77727-bddb-4830-8c2e-c065f9b66506" />



### Supported Browsers:
* Google Chrome
* Mozilla Firefox
* Safari [Mac OS X]

### Supported iOS Simulators:
* iPad Pro (11-inch) 4th generation - iOS 16.2