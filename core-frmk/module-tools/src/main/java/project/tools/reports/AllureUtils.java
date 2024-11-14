package project.tools.reports;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@UtilityClass
public final class AllureUtils {

    @Step("{0}")
    public static void step(String step) {
        Reporter.log("\nLOG - " + step, true);
    }

    @Attachment(value = "{0}", type = "image/jpg")
    public static synchronized byte[] makeScreenshot(String ignoredName, WebDriver driver) {
        byte[] imageAsBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(imageAsBytes);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            BufferedImage originalImage = ImageIO.read(bais);

            // Scale the image
            int scaledWidth = (int) (originalImage.getWidth() * 0.5);
            int scaledHeight = (int) (originalImage.getHeight() * 0.5);

            // Create a new scaled image
            BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            // Write to a new byte array
            ImageIO.write(scaledImage, "jpg", baos);
            baos.flush();
            return baos.toByteArray();

        } catch (IOException e) {
            System.err.printf("Failed to create screenshot: %s", e.getMessage());
            e.printStackTrace(System.err);
            return null;
        }
    }
}
