# Getting started with Serenity and Cucumber 2.4


Serenity BDD is a library that makes it easier to write high quality automated acceptance tests, with powerful reporting and living documentation features. It has strong support for both web testing with Selenium, and API testing using RestAssured. 

Serenity strongly encourages good test automation design, and supports several design patterns, including classic Page Objects, the newer Lean Page Objects/ Action Classes approach, and the more sophisticated and flexible Screenplay pattern.
 
The latest version of Serenity supports both Cucumber 2.4 and the more recent Cucumber 4.x. Cucumber 4 is not backward compatible with Cucumber 2.


### The project directory structure
The project has build scripts for both Maven and Gradle, and follows the standard directory structure used in most Serenity projects:
```Gherkin
src
  + main
  + test
    + java                        Test runners and supporting code
    + resources
      + features                  Feature files
          + search                  Feature file subdirectories 
             search_by_keyword.feature 
       + webdriver                 Bundled webdriver binaries
         + linux
         + mac
         + windows 
           chromedriver.exe       OS-specific Webdriver binaries 
           geckodriver.exe
```

## The scenario
Both variations of the  project uses the  Cucumber scenario. In this scenario, User (who likes to search for stuff) is performing a search on the wiktionary :

```Gherkin
Feature: Search by keyword

Scenario: Searching for a Apple in wiki Page
Given User is on the wiki home page
When he searches for "apple"
Then all the result titles should contain the word "A common, round fruit produced by the tree Malus domestica, cultivated in temperate climates."

Scenario: Searching for a Pear in wiki Page
Given User is on the wiki home page
When he searches for "pear"
Then all the result titles should contain the word "An edible fruit produced by the pear tree, similar to an apple but elongated towards the stem."
```


The glue code for this scenario looks like this:

```java
@Given("^User is on the wiki home page")
public void i_am_on_the_wiki_home_page() {
navigateTo.theWikiGoHomePage();
}

@When("^he searches for \"(.*)\"")
public void i_search_for(String term) {
searchFor.term(term);
}

@Then("^all the result titles should contain the word \"(.*)\"")
public void all_the_result_titles_should_contain_the_word(String term) {
assertThat(searchResult.titles())
.matches(results -> results.size() > 0)
.allMatch(title -> textOf(title).containsIgnoringCase(term));
System.out.println("User searchs item text as " + searchResult.titles().toString());
}
```

### Lean Page Objects and Action Classes
The glue code shown above uses Serenity step libraries as _action classes_ to make the tests easier to read, and to improve maintainability.

These classes are declared using the Serenity `@Steps` annotation, shown below:
```java
    @Steps
    NavigateTo navigateTo;

    @Steps
    SearchFor searchFor;

    @Steps
    SearchResult searchResult;
```

The `@Steps`annotation tells Serenity to create a new instance of the class, and inject any other steps or page objects that this instance might need. 

Each action class models a particular facet of user behaviour: navigating to a particular page, performing a search, or retrieving the results of a search. These classes are designed to be small and self-contained, which makes them more stable and easier to maintain.

The Screenplay DSL is rich and flexible, and well suited to teams working on large test automation projects with many team members, and who are reasonably comfortable with Java and design patterns. The Lean Page Objects/Action Classes approach proposes a gentler learning curve, but still provides significant advantages in terms of maintainability and reusability.

## Executing the tests
To run the project, you can either just run the `CucumberTestSuite` from src/test/java/adidas test runner class, or run  `mvn verify`from the command line.


By default, the tests will run using Chrome. You can run them in Firefox by overriding the `driver` system property, e.g.
```json
$ mvn clean verify -Ddriver=firefox
```
Or 
```json
$ gradle clean test -Pdriver=firefox
```

The test results will be recorded in the `target/site/serenity` directory.

## Simplified WebDriver configuration and other Serenity extras
The sample projects both use some Serenity features which make configuring the tests easier. In particular, Serenity uses the `serenity.conf` file in the `src/test/resources` directory to configure test execution options.  
### Webdriver configuration
The WebDriver configuration is managed entirely from this file, as illustrated below:
```java
webdriver {
    driver = chrome
}
headless.mode = true

chrome.switches="""--start-maximized;--test-type;--no-sandbox;--ignore-certificate-errors;
                   --disable-popup-blocking;--disable-default-apps;--disable-extensions-file-access-check;
                   --incognito;--disable-infobars,--disable-gpu"""

```

The project also bundles some of the WebDriver binaries that you need to run Selenium tests in the `src/test/resources/webdriver` directories. These binaries are configured in the `drivers` section of the `serenity.conf` config file:
```json
drivers {
  windows {
    webdriver.chrome.driver = "src/test/resources/webdriver/windows/chromedriver.exe"
    webdriver.gecko.driver = "src/test/resources/webdriver/windows/geckodriver.exe"
  }
  mac {
    webdriver.chrome.driver = "src/test/resources/webdriver/mac/chromedriver"
    webdriver.gecko.driver = "src/test/resources/webdriver/mac/geckodriver"
  }
  linux {
    webdriver.chrome.driver = "src/test/resources/webdriver/linux/chromedriver"
    webdriver.gecko.driver = "src/test/resources/webdriver/linux/geckodriver"
  }
}
```
This configuration means that development machines and build servers do not need to have a particular version of the WebDriver drivers installed for the tests to run correctly.

### Environment-specific configurations
We can also configure environment-specific properties and options, so that the tests can be run in different environments. Here, we configure three environments, __dev__, _staging_ and _prod_, with different starting URLs for each:
```json
environments {
default {
webdriver.base.url = "https://en.wiktionary.org/"
}
```
  
You use the `environment` system property to determine which environment to run against. For example to run the tests in the staging environment, you could run:
```json
$ mvn clean verify -Denvironment=default
```
