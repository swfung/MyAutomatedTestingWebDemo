package driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class MyChromeDriver extends ChromeDriver {
    public MyChromeDriver(ChromeOptions options){
        super(options);
    }

    public MyChromeDriver(DesiredCapabilities cap){
        super(cap);
    }

    @Override
    public void setSessionId(String sessionId){
        super.setSessionId(sessionId);
    }

}
