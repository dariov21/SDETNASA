package co.cobre.qa.fwk.driver;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;

public interface IDriverBuilder {
    RemoteWebDriver getDriver() throws Exception;
}
