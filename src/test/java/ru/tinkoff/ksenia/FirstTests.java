package ru.tinkoff.ksenia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.tinkoff.ksenia.components.ElementSearchType;
import ru.tinkoff.ksenia.components.Page;
import ru.tinkoff.ksenia.components.TextInput;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FirstTests extends BaseRunner {

    private final Logger logger = LoggerFactory.getLogger(FirstTests.class);

    @Test
    public void testPhoneNumberErr(){
        logger.info("Начали тест на проверку ошибок в поле номера");

        Page page = new Page(driver);
        page.goToUrl(baseUrl)
                .setTextToElement(ElementSearchType.NAME, "name", "Ваня Ваня")
                .setTextToElement(ElementSearchType.NAME, "birthday", "01.01.0001")
                .setTextToElement(ElementSearchType.NAME, "city", "Екатеринбург Екатеринбург")
                .setTextToElement(ElementSearchType.NAME, "phone", "+7(123)")
                .setTextToElement(ElementSearchType.NAME, "email", "12@ae.ru");

        List<ErrorType> errorTypes = Collections.singletonList(ErrorType.MOBILE_PHONE_ERROR);
        checkErrorText(page.getElementText(ElementSearchType.XPATH, "(.//*[normalize-space(text()) and normalize-space(.)='Заполните " +
                        "анкету'])[1]/following::div[2]").split("\n"),
                errorTypes);

        logger.info("Закончили тест на проверку ошибок в поле номера");
    }

    @Test
    public void testOnlyClickErr(){
        logger.info("Начали тест на проверку ошибок при клике");

        Page page = new Page(driver);
        page.goToUrl(baseUrl)
                .clickElement(ElementSearchType.NAME, "name")
                .clickElement(ElementSearchType.NAME, "birthday")
                .clickElement(ElementSearchType.NAME, "city")
                .clickElement(ElementSearchType.NAME, "email")
                .clickElement(ElementSearchType.NAME, "phone")
                .clickElement(ElementSearchType.NAME, "name");

        List<ErrorType> errorTypes = Collections.singletonList(ErrorType.BIRTHDAY_REQUIRED_ERROR);
        checkErrorText(page.getElementText(ElementSearchType.XPATH, "(.//*[normalize-space(text()) and normalize-space(.)='Заполните " +
                        "анкету'])[1]/following::div[2]").split("\n"),
                errorTypes);

        logger.info("Закончили тест на проверку ошибок при клике");
    }
}
