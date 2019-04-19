package ru.tinkoff.ksenia;

import ru.tinkoff.ksenia.components.Button;
import ru.tinkoff.ksenia.components.CheckBox;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.ksenia.components.ElementSearchType;
import ru.tinkoff.ksenia.components.Page;

import java.util.List;

import static org.junit.Assert.*;

public class ThirdTests extends BaseRunner {

    private final Logger logger = LoggerFactory.getLogger(ThirdTests.class);

    @Test
    public void testPageChanging() {
        logger.info("Начали тест на проверку смены страницы");
        Page page = new Page(driver);
        page.goToUrl("https://www.google.ru/");

        page.clickElement(ElementSearchType.NAME, "q")
                .clearElement(ElementSearchType.NAME, "q")
                .setTextToElement(ElementSearchType.NAME, "q", "мобайл тинькофф")
                .applyKeyToElement(ElementSearchType.NAME, "q", Keys.ENTER);

        page.clickElement(ElementSearchType.PARTIAL, "Тарифы Тинькофф Мобайла");

        page.goToNextTabAndCloseCurrent();

        assertEquals("Тарифы Тинькофф Мобайла", page.getElementText(
                ElementSearchType.XPATH,
                "(.//*[normalize-space(text()) and normalize-space(.)='регион'])[1]/following::p[1]")
        );
        assertEquals("https://www.tinkoff.ru/mobile-operator/tariffs/", page.getPageUrl());
        logger.info("Закончили тест на проверку смены страницы");
    }

    @Test
    public void testRegionChanging() {
        logger.info("Начали тест на проверку смены региона и совпадения цен");

        Page page = new Page(driver);
        page.goToUrl("https://www.tinkoff.ru/mobile-operator/tariffs/");
        selectRegion(page, "Москва");
        assertEquals("Москва и Московская область", getCurrentRegionTitle(page));

        driver.navigate().refresh();
        assertEquals("Москва и Московская область", getCurrentRegionTitle(page));

        int defaultMoscowPrice = getPrice(page);
        turnOptions(page, true);
        int fullMoscowPrice = getPrice(page);

        selectRegion(page, "Краснодар");

        int defaultKrasnodarPrice = getPrice(page);
        turnOptions(page, true);
        int fullKrasnodarPrice = getPrice(page);

        assertNotEquals(defaultMoscowPrice, defaultKrasnodarPrice);
        assertEquals(fullMoscowPrice, fullKrasnodarPrice);

        logger.info("Закончили тест на проверку смены региона и совпадения цен");
    }

    @Test
    public void testButtonIsActive() {
        logger.info("Начали тест на проверку активации кнопки");

        Page page = new Page(driver);
        page.goToUrl("https://www.tinkoff.ru/mobile-operator/tariffs/");
        selectRegion(page, "Москва");
        turnOptions(page, false);

        Button button = page.getButtonByName("form", "Заказать сим-карту");

        assertEquals(0, getPrice(page));
        assertTrue(button.isEnabled());

        logger.info("Закончили тест на проверку активации кнопки");
    }

    private void turnOptions(Page page, final boolean enable){
        page.getCheckboxesByCss("div.CheckboxWithDescription__checkbox_2E0r_").forEach(checkBox -> {
            if(enable){
                checkBox.enable();
            } else {
                checkBox.disable();
            }
        });

        page.getDropDownsByCssNames("div.ui-form__row_select", "div.ui-dropdown-field-list__item")
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


    private String getCurrentRegionTitle(Page page){
        String regTitleText = page.getElementText(ElementSearchType.CSS_SELECTOR, ".MvnoRegionConfirmation__title_DOqnW");
        if(regTitleText.endsWith("?")){
            regTitleText =  page.getElementText(ElementSearchType.CSS_SELECTOR, ".MvnoRegionConfirmation__optionRejection_1NrnL");
        }
        return regTitleText;
    }

    private void selectRegion(Page page, final String regionName){
        String regTitleText = page.getElementText(ElementSearchType.CSS_SELECTOR, ".MvnoRegionConfirmation__title_DOqnW");
        if(regTitleText.endsWith("?")){
            page.clickElement(ElementSearchType.CSS_SELECTOR, ".MvnoRegionConfirmation__optionRejection_1NrnL");
        } else {
            page.clickElement(ElementSearchType.CSS_SELECTOR, ".MvnoRegionConfirmation__title_DOqnW");
        }

        page.clickElement(ElementSearchType.PART_TEXT, regionName);
    }

    private int getPrice(Page page){
        return Integer.parseInt(page.getElementText(ElementSearchType.PART_TEXT, "Общая цена").replaceAll("\\D+",""));
    }
}
