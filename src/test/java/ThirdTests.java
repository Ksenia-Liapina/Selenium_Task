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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

    private void goToNextTabAndCloseCurrent(){
        List<String> pages = new ArrayList<>(driver.getWindowHandles());
        for(int i = 0; i < pages.size(); i++){
            if(pages.get(i).equals(driver.getWindowHandle())){
                driver.close();
                driver.switchTo().window(pages.get(i + 1));
                break;
            }
        }
    }

    @Test
    public void testRegionChanging() {
        driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");
        selectRegion("Москва");
        assertEquals("Москва и Московская область", getCurrentRegionTitle());

        driver.navigate().refresh();
        assertEquals("Москва и Московская область", getCurrentRegionTitle());

        int defaultMoscowPrice = Integer.parseInt(getElementByPartText("Общая цена").getText().replaceAll("\\D+",""));

        checkAll();
        int fullMoscowPrice = Integer.parseInt(getElementByPartText("Общая цена").getText().replaceAll("\\D+",""));

        selectRegion("Краснодар");
        int defaultKrasnodarPrice = Integer.parseInt(getElementByPartText("Общая цена").getText().replaceAll("\\D+", ""));

        checkAll();
        int fullKrasnodarPrice = Integer.parseInt(getElementByPartText("Общая цена").getText().replaceAll("\\D+",""));
        assertNotEquals(defaultMoscowPrice, defaultKrasnodarPrice);
        assertEquals(fullMoscowPrice, fullKrasnodarPrice);
    }

    private void checkAll(){
        List<WebElement> checkBoxes = driver.findElements(By.cssSelector("div.CheckboxWithDescription__checkbox_2E0r_"));
        checkBoxes.forEach(checkBox -> {
            WebElement inputCheck = checkBox.findElement(By.cssSelector("input[type='checkbox']"));
            if(!inputCheck.isSelected()){
                checkBox.findElement(By.cssSelector(":first-child")).click();
            }
        });

        List<WebElement> dropdowns = driver.findElements(By.cssSelector("div.ui-form__row_select"));
        dropdowns.forEach(dropdown -> {
            dropdown.click();
            if(dropdown.getText().contains("Интернет")){
                List<WebElement> selects = dropdown.findElements(By.cssSelector("div.ui-dropdown-field-list__item"));
                selects.forEach(select -> {
                    if(select.getText().contains("Безлимитный интернет")){
                        select.click();
                    }
                });
            }
            if(dropdown.getText().contains("Звонки")){
                List<WebElement> selects = dropdown.findElements(By.cssSelector("div.ui-dropdown-field-list__item"));
                selects.forEach(select -> {
                    if(select.getText().contains("Безлимитные минуты")){
                        select.click();
                    }
                });
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

    private WebElement getElementByPartText(final String partText){
       return driver.findElement(By.xpath(String.format("//*[text()[contains(.,'%s')]]", partText)));
    }

    @Test
    public void testButtonIsActive() {
        driver.get("https://www.tinkoff.ru/mobile-operator/tariffs/");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Интернет'])[1]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='на&nbsp;30&nbsp;дней'])[1]/following::div[4]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Звонки'])[1]/following::span[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Интернет с данным пакетом недоступен'])[1]/following::div[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Безлимитный интернет в приложениях'])[1]/following::label[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='?'])[1]/following::label[1]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Заказать сим-карту'])[1]/following::div[3]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Выберите пакеты услуг'])[1]/following::form[1]")).click();
        driver.findElement(By.name("fio")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Выберите пакеты услуг'])[1]/following::form[1]")).click();
        driver.findElement(By.name("fio")).clear();
        driver.findElement(By.name("fio")).sendKeys("Иванов Александр Петрович");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Выберите пакеты услуг'])[1]/following::form[1]")).click();
        driver.findElement(By.name("phone_mobile")).click();
        driver.findElement(By.name("phone_mobile")).clear();
        driver.findElement(By.name("phone_mobile")).sendKeys("+7(999) 999-99-99");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Заказать сим-карту'])[1]/following::div[2]")).click();
        assertEquals("Тарифы Тинькофф Мобайла", driver.getTitle());
    }
}
