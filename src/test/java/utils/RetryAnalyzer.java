package utils;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

@Slf4j
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;

    private static final int MAX_RETRY = 2;

    @Override
    public boolean retry(ITestResult result) {

        if (retryCount < MAX_RETRY) {

            retryCount++;

            log.warn(
                    "Reintentando '{}' intento {}/{}",
                    result.getName(),
                    retryCount,
                    MAX_RETRY
            );

            return true;
        }

        return false;

    }

}
