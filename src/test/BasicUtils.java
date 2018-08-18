package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasicUtils
{
	private WebDriverWait wait, waitForAppearance, waitForDisappearance;
	private WebDriver driver;
	
	public BasicUtils(WebDriver driver)
	{
		this.driver = driver;
		wait = new WebDriverWait(driver, Constants.defaultTimeoutSeconds);
		waitForAppearance = new WebDriverWait(driver,Constants.ifVisibleTimeoutSeconds);
		waitForDisappearance = new WebDriverWait(driver,Constants.disappearanceTimeoutSeconds);
	}
	
	private void waitForElementPresence(String xpath) throws Exception
	{
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
	}
	
	private void waitForElementDisappearance(String xpath) throws Exception
	{
		waitForDisappearance.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
	}
	public WebDriver getDriver()
	{
		return driver;
	}
	
	public String getTimeStamp()
	{
		return new SimpleDateFormat("dd_MMM_HH_mm_ss").format(new Date());
	}
	
	public String getCurrentUrl()
	{
		return driver.getCurrentUrl();
	}
	
	public void quitDriver(WebDriver driver)
	{
		driver.quit();
	}
	
	public void closeDriver(WebDriver driver)
	{
		driver.close();
	}
	
	public boolean knowIfAppears(String xpath)
	{
		try
		{
			waitForAppearance.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			return true;
		}
		catch(Exception e){}
		return false;
	}
		
	public void waitForElementVisible(String xpath) throws Exception
	{
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
	}
	
	public void waitForElementClickable(String xpath) throws Exception
	{
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
	}
	
	public void justNavigate(String url)
	{
		driver.get(url);
	}
	
	public void pause(int time) throws Exception
	{
		Thread.sleep(time);
	}
	
	public void clearBrowserCache()
	{
		driver.manage().deleteAllCookies();
	}
	
	public String getText(String xpath) throws Exception
	{
		String uiText = "";
		uiText = driver.findElement(By.xpath(xpath)).getText();
		return uiText;
	}
	
	public void scrollIntoView(String xpath) throws Exception
	{
		
	}
	
	public void scrollDown(int offsetInPixels) throws Exception
	{
		
	}
	
	public void scrollUp(int offsetInPixels) throws Exception
	{
		
	}
	
	public void scrollRight(int offsetInPixels) throws Exception
	{
		
	}
	
	public void scrollLeft(int offsetInPixels) throws Exception
	{
		
	}
	
	public void click(String xpath) throws Exception
	{
		waitForElementVisible(xpath);
		waitForElementClickable(xpath);
		driver.findElement(By.xpath(xpath)).click();
	}
	
	public void clearUpdateText(String xpath, String text) throws Exception
	{
		driver.findElement(By.xpath(xpath)).click();
		driver.findElement(By.xpath(xpath)).clear();
		driver.findElement(By.xpath(xpath)).sendKeys(text);
	}
	
	public void takeScreenshot() throws Exception
	{
		FileUtils.copyFile(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE), new File(System.getProperty("user.dir") + "\\Screenshots\\"+getTimeStamp()+".jpg"));
	}
	
	public void typeText(String xpath, String text) throws Exception
	{
		waitForElementClickable(xpath);
		driver.findElement(By.xpath(xpath)).sendKeys(text);
	}
	
	public void sendKeysUploadFile(String xpath, String text) throws Exception
	{
		driver.findElement(By.xpath(xpath)).sendKeys(text);
	}
	
	public void sendClearKeys(String xpath, String text) throws Exception
	{
		driver.findElement(By.xpath(xpath)).clear();		
		driver.findElement(By.xpath(xpath)).sendKeys(text);
	}
	
	public void pressEnterUsingActions() throws Exception
	{
		new Actions(driver).sendKeys(Keys.ENTER).build().perform();
	}
	
	public boolean isElementPresent(String xpath) throws Exception
	{
		try
		{
			driver.findElement(By.xpath(xpath));
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public void waitForElementDisappear(String xpath) throws Exception
	{
		waitForElementDisappearance(xpath);
		pause(Constants.preLoaderDefaultPauseMillis);
	}
	
	public WebElement getWebElement(String xpath) throws Exception
	{
		waitForElementPresence(xpath);
		waitForElementVisible(xpath);
		return driver.findElement(By.xpath(xpath));
	}
	
	public void switchToIframe(String iFrameXpath) throws Exception
	{
		driver.switchTo().frame(getWebElement(iFrameXpath));
	}
	
	public void switchBackToParent() throws Exception
	{
		driver.switchTo().defaultContent();
	}
	
	public String getAttribute(String xpath, String attributeName) throws Exception
	{
		WebElement element = getWebElement(xpath);
		String attribute = element.getAttribute(attributeName);
		return attribute;
	}
}
