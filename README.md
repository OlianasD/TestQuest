# TestQuest



## Approach
TestQuest is a Kotlin plugin for IntelliJ IDEA to support Web test maintenance based on Selenium WebDriver APIs, with a focus towards locators and PageObjects robustness. 

The process describing how TestQuest works is sketched in the following Figure.

![TestQuest Approach](./testquest.png)  

...



## Modules
TestQuest is composed by the following main modules:  
- [Gamification](./src/main/kotlin/gamification/): ...
- [Listener](./src/main/kotlin/listener/): ... 
- [Locator](./src/main/kotlin/locator/): ...
- [UI](./src/main/kotlin/ui/): ...
- [Utils](./src/main/kotlin/utils/): ...




## Usage
To install and use TestQuest into your IntelliJ test project:

- Import **Gamification Library** _jar_ used to intercept test events: 
    - File > Project Structure
    - From the Modules panel to the right, under the section Dependencies, add gamification-library.jar via '+' button, then confirm
    In this repository we provided a .jar file that supports Java 20, see more at [Gamification Library project](https://github.com/Paolobd/gamification-library))
- Import **TestQuest Library** _zip_: 
    - File > Settings > Plugin > Click engine symbol > Import from File system 
    - Select testquest.zip provided in this repository (or create your own zip by ...)
- Be sure that: 
    - All test artifacts (test cases, Page Objects) are stored under a _test_ folder
    - Test cases are named as _testCaseName__Test
    - Page Objects are named as _pageObjectName__Page 
    - All locators are declared in their full form (i.e., WebElement locatorName = driver.findElement(By.locatorStrategy(...)))



