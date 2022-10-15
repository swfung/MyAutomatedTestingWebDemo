package constant;

import utilities.ExcelUtil;

public class Xpath_Locator_BC_Web {
	public static final String mainPageURL = "https://www.babyclan.com.hk/";
	public static final String bcHomeHeader ="//h1[text()='全新手機應用程式']";
	public static final String loginRegBtn = "//*[@type='button' and @id='loginSelBtn']";
	public static final String mobileInputField = "//input[@id='loginUser' and @type='text']";
	public static final String pwInputField = "//input[@id='loginPwd' and @type='password']";
	public static final String loginBtn = "//*[@type='button' and @id='loginBtn']";
	public static final String regBtn = "//*[@type='button' and @id='regBtn']";
	public static final String ptsBtn ="//*[@type='button' and contains(text(),'套票及積分')]";
	public static final String logoutBtn ="//*[@type='button' and contains(text(),'登出')]";
}
