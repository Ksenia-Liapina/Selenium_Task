import components.TextInput;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FirstTests extends BaseRunner {

    @Test
    public void testPhoneNumberErr(){
        driver.get(baseUrl);
        new TextInput(driver.findElement(By.name("name"))).clearAndAddText("Ваня Ваня");
        new TextInput(driver.findElement(By.name("birthday"))).clearAndAddText("01.01.0001");
        new TextInput(driver.findElement(By.name("city"))).clearAndAddText("Екатеринбург Екатеринбург");
        new TextInput(driver.findElement(By.name("phone"))).clearAndAddText("+7(123)");
        new TextInput(driver.findElement(By.name("email"))).clearAndAddText("12@ae.ru");

        WebElement e = getFormElement(driver);

        List<ErrorType> errorTypes = Collections.singletonList(ErrorType.MOBILE_PHONE_ERROR);
        checkErrorText(e.getText().split("\n"), errorTypes);
    }

    @Test
    public void testOnlyClickErr(){
        driver.get(baseUrl);
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("birthday")).click();
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("phone")).click();
        driver.findElement(By.name("name")).click();

        WebElement e = getFormElement(driver);

        List<ErrorType> errorTypes = Collections.singletonList(ErrorType.BIRTHDAY_REQUIRED_ERROR);
        checkErrorText(e.getText().split("\n"), errorTypes);
    }
}
