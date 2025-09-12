package project.tools.reports;

import io.qameta.allure.Step;
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

public final class AllureUtils {

    @Step("{0}")
    public static void step(String step) {
        Reporter.log("\nLOG - " + step, true);
    }

    public static synchronized byte[] makeScreenshot(WebDriver driver) {
        try {
            byte[] originalBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            ByteArrayInputStream bais = new ByteArrayInputStream(originalBytes);
            BufferedImage originalImage = ImageIO.read(bais);

            int scaledWidth = (int) (originalImage.getWidth() * 0.5);
            int scaledHeight = (int) (originalImage.getHeight() * 0.5);

            BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(scaledImage, "jpg", baos);
            return baos.toByteArray();

        } catch (IOException e) {
            System.err.printf("In AllureUtils#takeScreenshot: %s\n", e.getMessage());
            return null;
        }
    }
}
