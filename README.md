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
   * 1.1. [Installation prerequisites](#one-one)
   * 1.2. [Build WebDriverAgent (WDA) in Xcode](#one-two)
   * 1.3. [Build Test Application](#one-three)
2. [Framework Structure](#two)
   * 2.1. [Configuring project](#two-one)
   * 2.2. [Design pattern architecture](#two-two)
3. [Tests Execution](#three)
   * 3.1. [Web:](#three-one)
   * 3.2. [Mobile:](#three-two)
4. [Generate and Review Allure Report](#four)


<a id="one"></a>
## 1. Getting Started
<a id="one-one"></a>
### 1.1. Installation prerequisites:
1. [Xcode](https://developer.apple.com/documentation/safari-developer-tools/installing-xcode-and-simulators) (>= v.16.4)<br/>
   * Update **Components** (Xcode -> Settings -> Components) to the [latest versions](https://developer.apple.com/documentation/ios-ipados-release-notes).<br/>
<img width="828" height="553" alt="Components" src="https://github.com/user-attachments/assets/6581a929-a673-4440-8cf9-377498133fc8" />

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
<a id="one-two"></a>
### 1.2. Build [WebDriverAgent (WDA) in Xcode](https://docs.katalon.com/katalon-studio/manage-projects/set-up-projects/mobile-testing/ios/mobile-install-webdriveragent-for-real-ios-devices-in-katalon-studio):<br/>
Building WebDriverAgent (WDA) in Xcode is crucial for using Appium to automate iOS applications. WDA is a server that allows Appium to interact with iOS devices and simulators. 
Here’s a step-by-step guide on how to build and configure WebDriverAgent in Xcode:<br/>
##### 1. Locate the **WebDriverAgent** Directory:<br/>
   Path typically:<br/>
   ```text
   /usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent
   ```
   Or:<br/>
   ```text
   ~/.npm-global/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent
   ```
##### 2. Open **WebDriverAgent** in Xcode:<br/>
   * Open Xcode<br/>
   * Select **File** -> **Open**<br/>
   * Navigate to the WebDriverAgent folder you found in the previous step<br/>
   * Open **WebDriverAgent.xcodeproj**<br/>

##### 3. Select Scheme: At top left, choose **WebDriverAgentRunner** scheme<br/>
##### 4. Configure Signing:<br/>
   Ensure the project is set to use automatic signing:<br/>
   * Click on the **WebDriverAgentRunner** in the project navigator<br/>
   * Select the target (on the right side in the project settings)<br/>
   * Under the **Signing & Capabilities** tab:<br/>
     * Select your development team from the **Team** drop-down<br/>
     * Make sure the **Automatically manage signing** option is enabled<br/>

##### 5. Connect Device:<br/>
   * Connect your physical iOS device via USB or choose a simulator<br/>
   * In the top left corner, select your device or simulator from the device drop-down list<br/>

##### 6. Build the Project:<br/>
   * With the correct scheme selected and device input, click on **Product** in the Xcode menu<br/>
   * Select **Build** or press **Command + B**<br/>
   * Monitor the **Issue Navigator** (the triangle icon) for any errors<br/>

##### 7. Fix Errors (if any): Resolve signing issues, dependencies, or iOS compatibility<br/>
##### 8. Run **WebDriverAgent Server**:<br/>
   * Open a new terminal window<br/>
   * Run the following commands to start the Appium server:<br/>
```shell
   appium --log-level debug
```
   * WebDriverAgent will launch automatically<br/>

##### 9. Verify WebDriverAgent Status:<br/>
```html
   http://<device-ip>:8100/status
```
Replace `<device-ip>` with the IP address of your iOS device while ensuring that the device’s WDA server is running.<br/>

##### Result:
<img width="1717" height="932" alt="WebDriverAgent" src="https://github.com/user-attachments/assets/e12429ae-e4f2-4470-bcf9-bbc650285175" />

<a id="one-three"></a>
### 1.3. Build Test Application:
1. Clone (or use option 'Open with Xcode') the project (iOS app for testing) from this [GitHub repository](https://github.com/kmartita/xcode-test-app).
   This is a simple 'Hello World!' application for iOS devices using Swift 6.1.2, created for testing purposes with Appium.
2. Select a iOS simulator (e.g., `iPad (A16)`) in Xcode and build application. After a successful build, the `.app` file will appear in `DerivedData`:<br/>
```text
   ~/Library/Developer/Xcode/DerivedData/YourProject-XXXXXX/Build/Products/Debug-iphonesimulator/TestApp.app
```

##### Result:
<img width="1213" height="652" alt="TestApp-Xcode" src="https://github.com/user-attachments/assets/1ad96c6f-0cb7-42c0-9c69-11c4fae832ef" />
<img width="1053" height="824" alt="TestApp-Simulator" src="https://github.com/user-attachments/assets/ee8dfa19-bfa5-489a-9e4e-23fcb9417bfa" />


<a id="two"></a>
## 2. Framework Structure
This template provides a UI test automation solution suitable for both web and mobile (iOS). It is structured as a Maven multi-module project, with the following relationships between the modules:<br/>
```
|———core-frmk
    |—-pom.xml
    |—-config
        |—-.env
    |—-module-app
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
                        |—-testng.xml
                        |—-allure.properties
    |—-module-tools
        |—-pom.xml
        |—-src
            |—-main
               |—-java
```
1. **Root Module (`core-frmk`)**. The parent project that defines shared dependencies, plugin configurations, and manages overall builds. Contains the `config` directory, where environment variables (`.env` files of various extensions) are stored for easy configuration management.<br/>
2. **Modules**:
   - `module-app`. The main module containing page object classes, UI components, and core application logic. It depends on **module-tools** for shared utilities and design pattern implementations.<br/>
   - `module-tools`. A utility module providing shared helpers, including support for browser configuration and Selenium integration, scalable Page Object pattern implementations, report generation, logging, and other common functions used across modules.<br/>
   - `module-tests`. This module contains test classes and test configurations (`testng.xml`). It depends on **module-app** to access page objects and perform web UI testing in a controlled environment.<br/>

<a id="two-one"></a>
### 2.1. Configuring project:
Run this command from the start to ensure that you don't have anything corrupted.<br/>
```bash
cd core-frmk
mvn clean install -U -DskipTests=true
```

<a id="two-two"></a>
### 2.2. Design pattern architecture:
<img width="1906" height="1980" alt="design-pattern-selenium" src="https://github.com/user-attachments/assets/04aad3ae-e4f3-48a1-b19b-8af70fde68f5" />


<a id="three"></a>
## 3. Tests Execution
Maven is used as the build and test management tool, with additional options for test configuration:<br/>
`-Denv={String}`  specifies the environment for test execution (default: `test`)<br/>
`-Dbrowser={String}` - defines the browser to run web tests on (default: `chrome`)<br/>
`-DiOS={int}` - defines the iOS version to run mobile tests on (default: the version set in Xcode)<br/>
-`Ddevice={String}` - defines the name of device for run mobile tests on (default: the name set in Xcode)<br/>
`-Dthreads={int}` -  specifies the number of threads for parallel test execution<br/>
`-Dtest={String}` - the specific test class to run<br/>

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
### 3.1. Web:
Create a hidden `.env` file containing the `BASE_URL` parameter to configure the web URL for testing. This will allow you to easily manage the base URL used during your web tests.<br/>
```properties
BASE_URL = https://demoqa.com
```

#### Supported Browsers:
* Google Chrome
* Mozilla Firefox
* Safari [Mac OS X]

#### Usage web examples:
To execute a specific test with default settings the next command line should be used:<br/>
```bash
mvn clean test -Dtest=HomeWebTest
```
To execute a test with custom options the next command line should be used:<br/>
```bash
mvn clean test -Dtest=HomeWebTest -Dbrowser=firefox -Denv=dev
```
To execute tests in parallel with `testng.xml` the next command line should be used:<br/>
```bash
mvn clean test -DsuiteFile=src/test/resources/suites/web.xml -Dthreads=3
```

<a id="three-two"></a>
### 3.2. Mobile:
Create a hidden `.env` file containing the parameters `APPIUM_URL`, `APP_NAME`, and `APP_BUNDLE_ID` to configure the testing environment for mobile tests.<br/>
Place your builded iOS application (e.g., `TestApp.app`) from `DerivedData` folder into the local `app` directory.<br/>
The `APP_BUNDLE_ID` is a unique bundle identifier for a mobile application on the iOS or Android platform. This identifier is used to differentiate apps in systems and services such as the App Store or Google Play.<br/>
```properties
APPIUM_UR = http://0.0.0.0:4723/
APP_NAME = "TestApp.app"
APP_BUNDLE_ID = com.kmartita
```
To execute the mobile test itself the next command line should be used:<br/>
```bash
cd core-frmk
mvn clean test -Denv=dev -Dtest=HomeMobileTest -DiOS=18.6 -Ddevice="iPad Pro 13-inch (M4)"
```
#### Supported iOS Simulators (by default):
* iPad (A16) - iOS 18.6

#### List of available devices:
<img width="918" height="441" alt="Devices" src="https://github.com/user-attachments/assets/6d1c0972-360c-4163-967e-9f5946a7f614" />


<a id="four"></a>
## 4. Generate and Review Allure Report
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
#### Suites (Web & Mobile):
```bash
mvn clean test -Denv=dev
```
```requirements
Browser: Chrome
iOS-version: 18.6
Deevice: iPad (A16)
```
##### Result:
<img width="1530" height="957" alt="suites_overview" src="https://github.com/user-attachments/assets/786561dd-917c-4b71-8d1f-175c67bf00ce" />
<img width="1412" height="572" alt="suites_behaviors" src="https://github.com/user-attachments/assets/784db419-8df3-4155-b93b-e74040220fe6" />

#### Mobile:
```bash
mvn clean test -DiOS=18.6 -Ddevice="iPad Air 11-inch (M3)" -Denv=dev -DsuiteFile=src/test/resources/suites/mobile.xml
```
##### Result:
<img width="1420" height="804" alt="mobile_overview" src="https://github.com/user-attachments/assets/e56a435d-709b-4de1-a234-b2258e64148c" />
<img width="1420" height="1194" alt="mobile-packages" src="https://github.com/user-attachments/assets/e9f771a7-ee4d-44ae-90be-06f487841408" />

#### Web: 
```bash
mvn clean test -Dbrowser=firefox -DsuiteFile=src/test/resources/suites/web.xml
```
```requirements
Env: test
```
##### Result:
<img width="1412" height="781" alt="web_overview" src="https://github.com/user-attachments/assets/38cb9642-0b5c-4d69-8464-b374b56fbcb7" />
<img width="1418" height="1110" alt="web_failed" src="https://github.com/user-attachments/assets/158f7f45-a867-4289-9882-7bb439f31290" />

