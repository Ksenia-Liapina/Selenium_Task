import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FirstTests extends BaseRunner {

    @Test
    public void testPhoneNumberErr(){
        driver.get(baseUrl);
        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("Ваня Ваня");
        driver.findElement(By.name("birthday")).click();
        driver.findElement(By.name("birthday")).clear();
        driver.findElement(By.name("birthday")).sendKeys("01.01.0001");
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).click();
        driver.findElement(By.name("city")).clear();
        driver.findElement(By.name("city")).sendKeys("Екатеринбург Екатеринбург");
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("phone")).click();
        driver.findElement(By.name("phone")).clear();
        driver.findElement(By.name("phone")).sendKeys("+7(123)");
        driver.findElement(By.name("email")).click();
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("12@ae.ru");

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
