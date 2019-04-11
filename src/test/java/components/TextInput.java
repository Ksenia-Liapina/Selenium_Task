package components;

import org.openqa.selenium.WebElement;

public class TextInput {

    private final WebElement textElement;

    public TextInput(WebElement textElement) {
        this.textElement = textElement;
    }

    public TextInput addText(final String text){
        textElement.click();
        textElement.sendKeys(text);
        return this;
    }

    public TextInput clearAndAddText(final String text){
        textElement.click();
        textElement.clear();
        textElement.sendKeys(text);
        return this;
    }

    public String getText(){
        return textElement.getText();
    }
}
