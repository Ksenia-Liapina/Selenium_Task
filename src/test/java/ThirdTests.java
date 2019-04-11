import components.Button;
import components.CheckBox;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ThirdTests extends BaseRunner {

    @Test
    public void testPageChanging() {
        driver.get("https://www.google.ru/");
        driver.findElement(By.name("q")).click();
        driver.findElement(By.name("q")).clear();
        driver.findElement(By.name("q")).sendKeys("мобайл тинькофф");
        driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
        driver.findElement(By.partialLinkText("Тарифы Тинькофф Мобайла")).click();

        goToNextTabAndCloseCurrent();

        WebElement titleElement = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)" +
                                                      "='регион'])[1]/following::p[1]"));

        assertEquals("Тарифы Тинькофф Мобайла", titleElement.getText());
        assertEquals("https://www.tinkoff.ru/mobile-operator/tariffs/", driver.getCurrentUrl());
    }

    @Test
    public void testRegionChanging() {
        driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");
        selectRegion("Москва");
        assertEquals("Москва и Московская область", getCurrentRegionTitle());

        driver.navigate().refresh();
        assertEquals("Москва и Московская область", getCurrentRegionTitle());

        int defaultMoscowPrice = getPrice();
        turnOptions(true);
        int fullMoscowPrice = getPrice();

        selectRegion("Краснодар");

        int defaultKrasnodarPrice = getPrice();
        turnOptions(true);
        int fullKrasnodarPrice = getPrice();

        assertNotEquals(defaultMoscowPrice, defaultKrasnodarPrice);
        assertEquals(fullMoscowPrice, fullKrasnodarPrice);
    }

    @Test
    public void testButtonIsActive() {
        driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");
        selectRegion("Москва");
        turnOptions(false);

        Button button = Button.findButtonByText(driver.findElement(By.name("form")), "Заказать сим-карту");

        assertEquals(0, getPrice());
        assertTrue(button.isEnabled());
    }

    private void turnOptions(final boolean enable){
        List<WebElement> checkBoxes = driver.findElements(By.cssSelector("div.CheckboxWithDescription__checkbox_2E0r_"));
        checkBoxes.stream().map(CheckBox::new).forEach(checkBox -> {
            if(enable){
                checkBox.enable();
            } else {
                checkBox.disable();
            }
        });

        List<WebElement> dropdowns = driver.findElements(By.cssSelector("div.ui-form__row_select"));
        dropdowns.stream()
                 .map(dropdown -> new components.Select(dropdown, "div.ui-dropdown-field-list__item"))
                 .forEach(dropdown -> {
                     if(dropdown.getName().contains("Интернет")){
                         WebElement selectElement = enable
                                                    ? dropdown.getDropdownElementForName("Безлимитный интернет")
                                                    : dropdown.getDropdownElementForName("0 ГБ");
                         selectElement.click();
                     }
                     if(dropdown.getName().contains("Звонки")){
                         WebElement selectElement = enable
                                                    ? dropdown.getDropdownElementForName("Безлимитные минуты")
                                                    : dropdown.getDropdownElementForName("0 минут");
                         selectElement.click();
                     }
                 });
    }


    private String getCurrentRegionTitle(){
        WebElement regTitle = driver.findElement(By.cssSelector(".MvnoRegionConfirmation__title_DOqnW"));
        if(regTitle.getText().endsWith("?")){
            regTitle = driver.findElement(By.cssSelector(".MvnoRegionConfirmation__optionRejection_1NrnL"));
        }
        return regTitle.getText();
    }

    private void selectRegion(final String regionName){
        WebElement regTitle = driver.findElement(By.cssSelector(".MvnoRegionConfirmation__title_DOqnW"));
        if(regTitle.getText().endsWith("?")){
            regTitle = driver.findElement(By.cssSelector(".MvnoRegionConfirmation__optionRejection_1NrnL"));
        }
        regTitle.click();
        getElementByPartText(regionName).click();
    }

    private int getPrice(){
        return Integer.parseInt(getElementByPartText("Общая цена").getText().replaceAll("\\D+",""));
    }
}
