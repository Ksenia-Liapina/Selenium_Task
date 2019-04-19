package ru.tinkoff.ksenia.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Select {

    private final WebElement dropdown;

    private final String dropdownElementSelector;

    public Select(final WebElement dropDown, final String dropdownElementSelector){
        this.dropdown = dropDown;
        this.dropdownElementSelector = dropdownElementSelector;
    }

    public String getName(){
        return dropdown.getText();
    }

    public WebElement getDropdownElementForName(final String name){
        dropdown.click();
        return dropdown.findElements(By.cssSelector(dropdownElementSelector))
                       .stream()
                       .filter(sel -> sel.getText().contains(name))
                       .findFirst()
                       .get();
    }
}
