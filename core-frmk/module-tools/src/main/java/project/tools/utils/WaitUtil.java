package project.tools.utils;

import com.google.common.base.Stopwatch;
import io.qameta.allure.Allure;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public final class WaitUtil {

    private static final String RETRY_FAILED_MESSAGE = "Exception occurred during retry condition.";
    private static final String RETRY_DID_NOT_SUCCEED = "Retried condition did not succeed within '%d' sec timeout: %s.";

    public static final int DEFAULT_POLLING_INTERVAL_MS = 300;

    public static final int DEFAULT_TIMEOUT_TO_WAIT = 40;
    public static final int SHORT_TIMEOUT_TO_WAIT = 5;

    private WaitUtil() {}

    public static boolean retryUntilTrue(Callable<Boolean> callable, String message) {
        return retryUntilTrue(callable, message, DEFAULT_TIMEOUT_TO_WAIT, DEFAULT_POLLING_INTERVAL_MS);
    }

    public static boolean retryUntilTrue(Callable<Boolean> callable, String message, int maxWaitTimeInSec, int pollingIntervalMs) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            while (stopwatch.elapsed(TimeUnit.SECONDS) <= maxWaitTimeInSec) {
                if (callable.call()) {
                    return true;
                }
                waitFor(pollingIntervalMs);
            }
        } catch (Exception e) {
            Allure.step(RETRY_FAILED_MESSAGE);
        } finally {
            stopwatch.stop();
        }
        throw new RuntimeException(String.format(RETRY_DID_NOT_SUCCEED, maxWaitTimeInSec, message));
    }

    public static void waitFor(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
