package project.tools.utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.offset.PointOption;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import project.tools.pageobject.AbstractApp;
import project.tools.pageobject.mobile.AbstractMobileApp;

import java.time.Duration;
import java.util.List;

import static io.appium.java_client.touch.TapOptions.tapOptions;
import static io.appium.java_client.touch.offset.ElementOption.element;

@UtilityClass
public final class MobileElementUtils {

    public void tap(WebElement element) {
        if (WebElementUtils.isElementShown(element)) {
            new TouchAction<>(AbstractMobileApp.getIOSDriver()).tap(tapOptions().withElement(element(element))).perform();
        } else {
            int x = element.getLocation().getX() + element.getSize().getWidth() / 2;
            int y = element.getLocation().getY() + element.getSize().getHeight() / 2;
            tap(x, y);
        }
    }

    public void tap(int coordinateX, int coordinateY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), coordinateX, coordinateY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        AbstractMobileApp.getIOSDriver().perform(List.of(tap));

        /*new TouchAction<>(AbstractMobileApp.getIOSDriver())
                .tap(tapOptions().withPosition(PointOption.point(coordinateX, coordinateY)))
                .release()
                .perform();*/
    }

    public void hideKeyboard() {
        if (isHideKeyBoardButtonShown()) {
            tap(getKeyboardButtons().get(0));
        }
    }

    private boolean isHideKeyBoardButtonShown() {
        try {
            return getKeyboardButtons().stream()
                    .anyMatch(WebElementUtils::isElementShown);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    private List<WebElement> getKeyboardButtons() {
        return AbstractMobileApp.getAppiumDriver().findElements(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeButton' and name == 'Hide keyboard'"));
    }

    public void swipe(int startX, int startY, int finishX, int finishY) {
        TouchAction<?> action = new TouchAction<>(AbstractMobileApp.getIOSDriver());
        action.longPress(LongPressOptions.longPressOptions().withPosition(PointOption.point(startX, startY)))
                .moveTo(PointOption.point(finishX, finishY));
        action.perform();
    }

    public void swipeDown(int startX, int startY, int offset) {
        swipe(startX, startY, startX, startY - offset);
    }

    public void swipeUp(int startX, int startY, int offset) {
        swipe(startX, startY, startX, startY + offset);
    }

    public void scrollDownOverlay() {
        swipeDown(360, 400, 340);
    }

    public void scrollUpOverlay() {
        swipeUp(345, 334, 340);
    }

    public boolean isElementShown(By locator) {
        List<WebElement> elementList = AbstractApp.getInstanceDriver().findElements(locator);
        return !elementList.isEmpty() && elementList.get(0).isDisplayed();
    }

    public void scrollToOverlayElement(By locator, int swipeAttempt) {
        int swipeCount = swipeAttempt;

        while (swipeCount > 0) {
            if (isElementShown(locator))
                break;
            else {
                scrollDownOverlay();
                swipeCount--;
            }
        }

        while (swipeCount < swipeAttempt) {
            if (isElementShown(locator))
                break;
            else {
                scrollUpOverlay();
                swipeCount++;
            }
        }
    }

    public void scrollDownToElement(WebElement element) {
        int swipeCount = 5;
        while (swipeCount > 0) {
            if (WebElementUtils.isElementShown(element))
                break;
            else {
                scrollDownOverlay();
                swipeCount--;
            }
        }
    }

    public void scrollUpToElement(WebElement element, int swipeAttempt) {
        int swipeCount = swipeAttempt;
        while (swipeCount > 0) {
            if (WebElementUtils.isElementShown(element))
                break;
            else {
                scrollUpOverlay();
                swipeCount--;
            }
        }
    }

    public void swipeLeft(int startX, int startY, int offset) {
        swipe(startX, startY, startX - offset, startY);
    }

    public void swipeRight(int startX, int startY, int offset) {
        swipe(startX, startY, startX + offset, startY);
    }

    public void swipeLeftFromElement(WebElement element) {
        Point elementLocation = element.getLocation();
        int centerX = elementLocation.getX();
        int centerY = elementLocation.getY();
        int centerZ = element.getRect().getHeight() * 20;
        swipeLeft(centerX, centerY, centerZ);
    }

    public void swipeRightFromElement(WebElement element) {
        Point elementLocation = element.getLocation();
        int centerX = elementLocation.getX();
        int centerY = elementLocation.getY();
        int centerZ = element.getRect().getHeight() * 20;
        swipeRight(centerX, centerY, centerZ);
    }

    private List<WebElement> section(String sectionName) {
        return AbstractMobileApp.getAppiumDriver().findElements(AppiumBy.iOSClassChain(
                String.format("**/XCUIElementTypeOther[`label == '%s'`]", sectionName)));
    }

    public void scrollToSection(String sectionName) {
        boolean isSectionVisible = false;
        int attempts = 0;
        while (!isSectionVisible && attempts <= 20) {
            if (attempts < 10) {
                isSectionVisible = scrollDownToSection(sectionName);
            } else {
                isSectionVisible = scrollUpToSection(sectionName);
            }
            attempts++;
        }
    }

    private boolean scrollDownToSection(String sectionName) {
        boolean isSectionVisible = false;
        if (!section(sectionName).isEmpty()) {
            isSectionVisible = section(sectionName).get(0).isDisplayed();
            if (!isSectionVisible) {
                swipeDown(650, 600, 220);
            }
            if (section(sectionName).get(0).getRect().getY() > 700) {
                swipeDown(650, 600, 220);
            }
        } else {
            swipeDown(650, 600, 220);
        }
        return isSectionVisible;
    }

    private boolean scrollUpToSection(String sectionName) {
        boolean isSectionVisible = false;
        if (!section(sectionName).isEmpty()) {
            isSectionVisible = section(sectionName).get(0).isDisplayed();
            if (!isSectionVisible) {
                swipeUp(650, 600, 220);
            }
            if (section(sectionName).get(0).getRect().getY() > 700) {
                swipeUp(650, 600, 200);
            }
        } else {
            swipeUp(650, 600, 220);
        }
        return isSectionVisible;
    }

    public void scrollUpToElement(WebElement webElement) {
        boolean isElementVisible = WebElementUtils.isElementShown(webElement);
        int attempts = 0;
        while (!isElementVisible && attempts < 7) {
            isElementVisible = WebElementUtils.isElementShown(webElement);
            swipeUp(600, 600, 400);
            attempts++;
        }
    }

    public Point getCenter(WebElement element) {
        Point upperLeft = element.getLocation();
        Dimension dimensions = element.getSize();
        return new Point(upperLeft.getX() + dimensions.getWidth() / 2, upperLeft.getY() + dimensions.getHeight() / 2);
    }
}
