package co.cobre.qa.fwk.driver;


import co.cobre.qa.fwk.core.pageObjects.BasePage;
import co.cobre.qa.prj.driver.CobreDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DriverManager {
    private static Logger logger = Logger.getLogger(DriverManager.class);

    protected DriverManager() {

    }

    private static ThreadLocal<List<BasePage>> pageThreadLocal = new ThreadLocal<List<BasePage>>();

    private static ThreadLocal<RemoteWebDriver> localDriver = new ThreadLocal<RemoteWebDriver>();

    public static void createDriverInstance(TestType type) throws Exception {
        if (!isDriverCreated() || !isAValidDriver()) {
            if (localDriver.get() != null) {
                localDriver.remove();
            }

            RemoteWebDriver driver = type.getDriver();
            logger.info("Driver Capabilities are : \n "+ driver.getCapabilities().asMap().toString());
            localDriver.set(driver);
        }
    }
    public static RemoteWebDriver getDriverInstance() {
        return localDriver.get();
    }

    public static void dismissCurrentDriver() {
        if (isDriverCreated()) {
            localDriver.get().quit();
            localDriver.remove();
        }
        if(isTherePages()){
            pageThreadLocal.remove();
        }

    }

    private static boolean isTherePages(){
        return pageThreadLocal.get() !=null && pageThreadLocal.get().size()>0;
    }

    private static boolean isDriverCreated() {
        return localDriver.get() != null;
    }

    private static boolean isAValidDriver() {
        return localDriver.get().getSessionId() != null;
    }

    public static void dismissDriver() {
        if(localDriver.get() != null){
            getDriverInstance().quit();
        }
    }

    public static void resetDriver() {
        dismissCurrentDriver();
    }


}
