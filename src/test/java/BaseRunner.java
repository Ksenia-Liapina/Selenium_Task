import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class BaseRunner {

    private static ThreadLocal<WebDriver> tl = new ThreadLocal<>();
    WebDriver driver;
    private String browserName = System.getProperty("browser");
    protected String baseUrl;


    @Before
    public void setUp(){
        if (tl.get() != null) {
            driver = tl.get();
        } else {
            driver = getDriver();
            tl.set(driver);
        }
        driver.manage().window().maximize();
        baseUrl = "https://www.tinkoff.ru/career/vacancies/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            driver.quit();
            driver = null;
        }));
    }

    @After
    public void tearDown() {
        //driver.quit();
    }

    private WebDriver getDriver() {
        return BrowsersFactory.valueOf(browserName).create();
    }


    private final String NAME_FIELD_TEXT = "Фамилия и имя";
    private final String BIRTHDAY_FIELD_TEXT = "Дата рождения";
    private final String CITY_FIELD_TEXT = "Город проживания";
    private final String EMAIL_FIELD_TEXT = "Электронная почта";
    private final String MOBILE_FIELD_TEXT = "Мобильный телефон";


    protected void checkErrorText(String[] textFields, List<ErrorType> needCheckErrorTypes){
        for(int i = 0; i < textFields.length; i++){
            String fieldName = textFields[i];
            switch (fieldName){
                case NAME_FIELD_TEXT:{
                    if(!needCheckErrorTypes.contains(ErrorType.NAME_ERROR)){
                        continue;
                    }
                    assertEquals("Допустимо использовать только буквы русского алфавита и дефис", textFields[i + 1]);
                    break;
                }
                case BIRTHDAY_FIELD_TEXT:{
                    if(needCheckErrorTypes.contains(ErrorType.BIRTHDAY_REQUIRED_ERROR)){
                        assertEquals("Поле обязательное", textFields[i + 1]);;
                    } else if(needCheckErrorTypes.contains(ErrorType.BIRTHDAY_ERROR)) {
                        assertEquals("Поле заполнено некорректно", textFields[i + 1]);;
                    } else {
                        continue;
                    }

                    break;
                }
                case EMAIL_FIELD_TEXT:{
                    if(!needCheckErrorTypes.contains(ErrorType.EMAIL_VALIDATION_ERROR)){
                        continue;
                    }
                    assertEquals("Введите корректный адрес эл. почты", textFields[i + 1]);
                    break;
                }
                case MOBILE_FIELD_TEXT:{
                    if(!needCheckErrorTypes.contains(ErrorType.MOBILE_PHONE_ERROR)){
                        continue;
                    }
                    assertEquals("Номер телефона должен состоять из 10 цифр, начиная с кода оператора", textFields[i + 1]);
                    break;
                }
            }
        }
    }
}
