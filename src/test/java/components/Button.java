package components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Button {

    private final WebElement buttonElement;

    public Button(WebElement buttonElement) {
        this.buttonElement = buttonElement;
    }

    public boolean isEnabled() {
        return buttonElement.isEnabled();
    }

    public Button click() {
        buttonElement.click();
        return this;
    }

    public String getText() {
        return buttonElement.getText();
    }

    public static Button findButtonByText(final WebElement formWithButtons, final String text) {
        return new Button(formWithButtons.findElements(By.tagName("button")).stream()
                                         .filter(but -> but.getText().equals(text))
                                         .findFirst().get());
    }
}
