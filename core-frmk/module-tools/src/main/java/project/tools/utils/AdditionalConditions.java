package project.tools.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.function.Supplier;

public class AdditionalConditions {

    public static ExpectedCondition<Boolean> documentReadyStateIsComplete() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) ((JavascriptExecutor) driver).executeScript("return document.readyState;").toString()
                        .equals("complete");
            }
        };
    }

    public static ExpectedCondition<Boolean> jQueryIsPresent() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) ((JavascriptExecutor) driver).executeScript("return (window.jQuery != null)");
            }
        };
    }

    public static ExpectedCondition<Boolean> jQueryAjaxCallsHaveCompleted() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return (Boolean) ((JavascriptExecutor) driver)
                        .executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        };
    }

    public static ExpectedCondition<Boolean> jQueryAjaxCallsHaveCompleted2() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                System.out.println(((JavascriptExecutor) driver).executeScript("return (jQuery.active);"));
                return (Boolean) ((JavascriptExecutor) driver)
                        .executeScript("return (window.jQuery != null) && (jQuery.active === 0);");
            }
        };
    }

    public static ExpectedCondition<Boolean> angularIsPresent() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                Boolean x = Boolean.valueOf(((JavascriptExecutor) driver)
                        .executeScript("return (window.angular !== undefined)").toString());
                return x;
            }
        };
    }

    public static ExpectedCondition<Boolean> angularHasFinishedProcessing() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return Boolean.valueOf(((JavascriptExecutor) driver)
                        .executeScript(
                                "return (window.angular !== undefined) && (angular.element(document).injector() !== undefined) && (angular.element(document).injector().get('$http').pendingRequests.length === 0)")
                        .toString());
            }
        };
    }

    public static ExpectedCondition<Boolean> isTrue(Supplier<Boolean> isTrue) {
        return driver -> isTrue.get().equals(Boolean.TRUE);
    }

    public static ExpectedCondition<Boolean> exists(WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    element.isDisplayed();
                    return true;
                } catch (StaleElementReferenceException e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "element to exist: " + element;
            }
        };
    }
}
