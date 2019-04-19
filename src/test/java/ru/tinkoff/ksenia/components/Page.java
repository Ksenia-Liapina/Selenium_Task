package ru.tinkoff.ksenia.components;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Page {

    private final Logger logger = LoggerFactory.getLogger(Page.class);

    private final WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
    }

    public String getPageUrl(){
        logger.info("Взяли текущий урл страницы");
        return driver.getCurrentUrl();
    }

    public Page goToUrl(final String url){
        driver.get(url);
        logger.info("Перешли на страницу : {}", url);

        return this;
    }

    public Page refresh(){
        driver.navigate().refresh();
        logger.info("Обновили страницу");
        return this;
    }

    public Page goToNextTabAndCloseCurrent(){
        List<String> pages = new ArrayList<>(driver.getWindowHandles());
        for(int i = 0; i < pages.size(); i++){
            if(pages.get(i).equals(driver.getWindowHandle())){
                driver.close();
                driver.switchTo().window(pages.get(i + 1));
                break;
            }
        }

        logger.info("Перешли на новую вкладку");

        return this;
    }

    private WebElement getElementByType(final ElementSearchType type, final String searchText){
        switch (type){
            case NAME:
                return driver.findElement(By.name(searchText));
            case XPATH:
                return driver.findElement(By.xpath(searchText));
            case CSS_CLASS:
                return driver.findElement(By.className(searchText));
            case CSS_SELECTOR:
                return driver.findElement(By.cssSelector(searchText));
            case PARTIAL:
                return driver.findElement(By.partialLinkText(searchText));
            case PART_TEXT:
                return driver.findElement(By.xpath(String.format("//*[text()[contains(.,'%s')]]", searchText)));
        }
        return driver.findElement(By.name(searchText));
    }

    public Page clickElement(final ElementSearchType type, final String searchElementText){
        getElementByType(type, searchElementText).click();

        logger.info("Кликнули на элемент : {}", searchElementText);
        return this;
    }

    public Page clearElement(final ElementSearchType type, final String searchElementText){
        getElementByType(type, searchElementText).clear();

        logger.info("Очистили поле элемента : {}", searchElementText);
        return this;
    }

    public Page setTextToElement(final ElementSearchType type, final String searchElementText, final String text){
        new TextInput(getElementByType(type, searchElementText)).clearAndAddText(text);

        logger.info("Добавили текст ({}) в поле элемента : {}", text, searchElementText);
        return this;
    }

    public Page applyKeyToElement(final ElementSearchType type, final String searchElementText, final Keys key){
        getElementByType(type, searchElementText).sendKeys(key);

        logger.info("Нажали клавишу ({}) в поле элемента : {}", key, searchElementText);

        return this;
    }

    public String getElementText(final ElementSearchType type, final String searchElementText){
        logger.info("Взяли текст элемента : {}", searchElementText);

        return getElementByType(type, searchElementText).getText();
    }

    public Button getButtonByName(final String formButtonName, final String buttonText){
        logger.info("Взяли кнопку по имени : {}", buttonText);

        return Button.findButtonByText(driver.findElement(By.name(formButtonName)), buttonText);
    }

    public List<CheckBox> getCheckboxesByCss(final String searchElementText){
        logger.info("Нашли чекбоксы : {}", searchElementText);

        return driver.findElements(By.cssSelector(searchElementText))
                .stream()
                .map(CheckBox::new)
                .collect(Collectors.toList());
    }

    public List<Select> getDropDownsByCssNames(final String searchElementText, final String selectCssName){
        logger.info("Нашли селекты : {}", searchElementText);

        return driver.findElements(By.cssSelector(searchElementText))
                .stream()
                .map(dropdown -> new ru.tinkoff.ksenia.components.Select(dropdown, selectCssName))
                .collect(Collectors.toList());
    }
}
