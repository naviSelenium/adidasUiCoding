package adidas.stepdefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

import static adidas.matchers.TextMatcher.textOf;
import static org.assertj.core.api.Assertions.assertThat;

import adidas.navigation.NavigateTo;
import adidas.search.SearchFor;
import adidas.search.SearchResult;

public class SearchOnWikiGoStepDefinitions {

    @Steps
    NavigateTo navigateTo;

    @Steps
    SearchFor searchFor;

    @Steps
    SearchResult searchResult;

	    @Given("^User is on the wiki home page")
    public void i_am_on_the_wiki_home_page() {
        navigateTo.theWikiGoHomePage();
    }

    @When("^User searches for \"(.*)\"")
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
}
