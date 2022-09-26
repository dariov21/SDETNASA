package co.cobre.qa.fwk.core;

import co.cobre.qa.prj.data.UserAccount;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PropertyManager {
    private static Logger logger = Logger.getLogger(PropertyManager.class);
    private static ThreadLocal<Map<String, String>> testInfo = new ThreadLocal<>();
    public static String KEY_RUNNING_SCENARIO = "scenario";
    public static String KEY_RUNNING_TAG = "tag";

    private static final String PROPERTY_FILE_NAME = "/config.properties";
    private static final String DEV_ENV_URL = "https://api-movil-util-dev.cobre.co";
    private static final String QA_ENV_URL = "https://api-movil-util.qa.cobre.co";

    public static final String AUTOMATE_USERNAME = "alvaroangulo_Krx7Pi";
    public static final String AUTOMATE_ACCESS_KEY = "bCJ6sMBBeo4m23aay3sH";

    private static Properties properties;

    private PropertyManager() {
    }

    private static Properties getProperties() {
        if (properties == null) {
            try {
                loadProperties();
            } catch (IOException var1) {
                var1.printStackTrace();
            }
        }

        return properties;
    }

    public static String getProperty(String propertyKey) {
        String value = getProperties().getProperty(propertyKey);
        return value;
    }

    public static boolean isPropertyPresentAndNotEmpty(String propertyKey) {
        return getProperties().containsKey(propertyKey) && !getProperties().getProperty(propertyKey).isEmpty();
    }

    private static void loadProperties() throws IOException {
        InputStream inputStream = PropertyManager.class.getResourceAsStream(PROPERTY_FILE_NAME);
        properties = new Properties();
        properties.load(inputStream);
    }

    public static void setAvd(String avdName){
        properties.setProperty("cobre.mobile.avd",avdName);
    }

    public static String getAvd(){
        return properties.getProperty("cobre.mobile.avd");
    }

    public static boolean isGridOn() {
        return getProperty("cobre.grid.hub") != null && !getProperty("cobre.grid.hub").isEmpty();
    }

    public static String getGridUrl() {
        return getProperty("cobre.grid.hub");
    }

    public static String getEnv(){
        return getProperty("cobre.env");
    }

    public static boolean isQAEnv(){
        return getProperty("cobre.env").equalsIgnoreCase("QA");
    }

    public static String getEnvUrl() {
        String env = getEnv();
        switch (env){
            case "DEV" : return DEV_ENV_URL;
            case "QA" : return QA_ENV_URL;
            default: return "";
        }
    }

    public static boolean userRealDevice() {
        String value =  getProperty("cobre.mobile.device.real");
        if(value == null){
            return false;
        }
        return !value.isEmpty() && Boolean.parseBoolean(value);
    }

    public static void setTestInfo(String key, String value) {
        if(testInfo.get() == null){
            testInfo.set(new HashMap<>());
        }
        testInfo.get().put(key,value);
    }

    public static String getTestInfo(String key){
        return testInfo.get().get(key);
    }

    public static String getBrowserStackApkId() {
        String path = PropertyManager.getProperty("cobre.browserstack.file.apk.upladed.id");
        if(path.equalsIgnoreCase("${cobre.browserstack.file.apk.upladed.id}")||path == null || path.isEmpty()){
            return "";
        }
        return getApkIdFromJason(path);
    }

    private static String getApkIdFromJason(String file){
        logger.info("Getting APKID from file : "+ file);
        try(final InputStream inputStream = Files.newInputStream(Paths.get(file.trim()))) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Gson gson = new Gson();
            Map<String,String> map = gson.fromJson(bufferedReader, Map.class);
            Assert.assertTrue(map.containsKey("app_url"),"Error there is not app_url in json file, please review apkid.json file.");
            return map.get("app_url");
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isBrowserStackSON() {
        String bsUser = PropertyManager.getProperty("cobre.browserstack.user");
        if(bsUser != null && !bsUser.isEmpty()){
            logger.info("##BROWSERSTACK_ON##");
            return true;
        }else{
            logger.info("##BROWSERSTACK_OFF##");
            return false;
        }
    }

    public static int getFluentWaitPollingTime() {
        String pollingTime = getProperty("cobre.wait.fluent.polling.time");
        if(StringUtils.isEmpty(pollingTime)){
            return 10;
        }else{
            return Integer.parseInt(pollingTime);
        }
    }


}
