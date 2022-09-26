package com.deviget.ui;

import org.openqa.selenium.remote.RemoteWebDriver;

public interface IDriverBuilder {
    RemoteWebDriver getDriver() throws Exception;
}
