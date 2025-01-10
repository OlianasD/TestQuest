# TestQuest



## Approach
TestQuest is a Kotlin plugin for IntelliJ IDEA to support Web test maintenance, with a focus towards locators and PageObjects robustness. 

The process describing how TestQuest works is sketched in the following Figure.

![TestQuest Approach](./testquest.pdf)  

...



## Modules
TestQuest is composed by the following main modules:  
- [gamification](./path/to/core/README.md): ...
- [listener](./path/to/gamification/README.md): ... 
- [locator](./path/to/integration/README.md): ...
- [ui](./path/to/integration/README.md): ...
- [utils](./path/to/integration/README.md): ...




## Usage
To install TestQuest and related components into an IntelliJ test project:

- Import Gamification Library jar used to intercept test events: 
    - File > Project Structure
    - From the Modules panel to the right, under the section Dependencies, add gamification-library.jar via '+' button, then confirm
    In this repository we provided a .jar file that supports Java 20, see more at [Gamification Library project](https://github.com/Paolobd/gamification-library))
- Import TestQuest zip: 
    - File > Settings > Plugin > Click engine symbol > Import from File system 
    - Select testquest.zip provided in this repository (or create your own zip by ...)
- Be sure that all test artifacts (test cases, Page Objects) are stored under a test folder. 
