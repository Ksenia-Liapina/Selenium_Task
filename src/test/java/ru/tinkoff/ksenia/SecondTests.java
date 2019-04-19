package ru.tinkoff.ksenia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.ksenia.components.ElementSearchType;
import ru.tinkoff.ksenia.components.Page;
import ru.tinkoff.ksenia.components.TextInput;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class SecondTests extends BaseRunner {

    private final Logger logger = LoggerFactory.getLogger(SecondTests.class);

    @Test
    public void testIncorrectMultipleValues(){

        logger.info("Начали тест на проверку ошибок многих полей");
        Page page = new Page(driver);
        page.goToUrl(baseUrl)
                .clickElement(ElementSearchType.NAME, "name")
                .clickElement(ElementSearchType.NAME, "birthday")
                .clickElement(ElementSearchType.NAME, "city")
                .clickElement(ElementSearchType.NAME, "email")
                .clickElement(ElementSearchType.NAME, "phone")
                .clickElement(ElementSearchType.XPATH, "(.//*[normalize-space(text()) and normalize-space(.)='Перетащите файлы сюда'])[1]/following::span[2]")
                .clickElement(ElementSearchType.CSS_SELECTOR, "svg.ui-icon-checkbox.ui-checkbox__icon")
                .setTextToElement(ElementSearchType.NAME, "name", "Иван12")
                .clickElement(ElementSearchType.XPATH, "(.//*[normalize-space(text()) and normalize-space(.)='Заполните анкету'])[1]/following::div[1]")
                .setTextToElement(ElementSearchType.NAME, "birthday", "01.")
                .setTextToElement(ElementSearchType.NAME, "city", "г")
                .clickElement(ElementSearchType.XPATH, "(.//*[normalize-space(text()) and normalize-space(.)='Заполните анкету'])[1]/following::div[1]")
                .setTextToElement(ElementSearchType.NAME, "email", "123")
                .setTextToElement(ElementSearchType.NAME, "phone", "+7(123)")
                .clickElement(ElementSearchType.XPATH, "(.//*[normalize-space(text()) and normalize-space(.)='Заполните анкету'])[1]/following::div[1]")
                .clickElement(ElementSearchType.XPATH, "(.//*[normalize-space(text()) and normalize-space(.)='условиями передачи информации'])[1]/following::span[1]");

        List<ErrorType> errorTypes = Arrays.asList(ErrorType.NAME_ERROR, ErrorType.BIRTHDAY_ERROR, ErrorType
                .EMAIL_VALIDATION_ERROR, ErrorType.MOBILE_PHONE_ERROR);

        checkErrorText(page.getElementText(ElementSearchType.XPATH, "(.//*[normalize-space(text()) and normalize-space(.)='Заполните " +
                "анкету'])[1]/following::div[2]").split("\n"),
                errorTypes);

        logger.info("Закончили тест на проверку ошибок многих полей");
    }

}
