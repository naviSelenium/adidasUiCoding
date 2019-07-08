package adidas.search;

import org.openqa.selenium.By;

class SearchForm {
    static By SEARCH_FIELD = By.xpath("(//input[@name='search'])[1]");
    static By SEARCH_BUTTON = By.xpath("(//input[@name='go'])[1]");
}