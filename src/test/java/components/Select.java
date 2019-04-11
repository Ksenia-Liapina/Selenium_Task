package components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

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
