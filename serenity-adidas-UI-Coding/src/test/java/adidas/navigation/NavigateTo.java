package adidas.navigation;

import net.thucydides.core.annotations.Step;

public class NavigateTo {

	WikiGoHomePage wikiGoHomePage;

    @Step("Open the Wiki home page")
    public void theWikiGoHomePage() {
    	wikiGoHomePage.open();
    }
}
