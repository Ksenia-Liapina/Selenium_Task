package components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CheckBox {

    private final WebElement checkBoxWithDescription;

    public CheckBox(final WebElement checkBoxWithDescription){
        this.checkBoxWithDescription = checkBoxWithDescription;
    }

    public boolean isEnabled(){
        return getCheckBox().isSelected();
    }

    public CheckBox enable(){
        return switchCheckbox(true);
    }

    public CheckBox disable(){
        return switchCheckbox(false);
    }

    public String getCheckBoxTitle(){
        return checkBoxWithDescription.getText();
    }

    public WebElement getElement(){
        return this.checkBoxWithDescription;
    }

    private CheckBox switchCheckbox(final boolean needEnable){
        boolean isEnabled = isEnabled();
        if(!isEnabled && needEnable){
            checkBoxWithDescription.findElement(By.cssSelector(":first-child")).click();
        }

        if(isEnabled && !needEnable){
            checkBoxWithDescription.findElement(By.cssSelector(":first-child")).click();
        }

        return this;
    }

    private WebElement getCheckBox(){
        return checkBoxWithDescription.findElement(By.cssSelector("input[type='checkbox']"));
    }

}
