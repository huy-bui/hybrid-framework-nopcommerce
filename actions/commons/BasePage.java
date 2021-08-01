package commons;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
	
	// Web Browser
	
	public void openPageURL(WebDriver driver, String url) {
		driver.get(url);
	}
	
	public String getPageTitle(WebDriver driver) {
		return driver.getTitle();
	}
	
	public String getCurrentUrl(WebDriver driver) {
		return driver.getCurrentUrl();
	}
	
	public String getPageSource(WebDriver driver) {
		return driver.getPageSource();
	}
	
	public Alert waitForAlertPresent(WebDriver driver) {
		explicitWait = new WebDriverWait(driver, timeout);
		return explicitWait.until(ExpectedConditions.alertIsPresent());
	}
	
	public void acceptAlert(WebDriver driver) {
		alert = waitForAlertPresent(driver);
		alert.accept();
	}
	
	public void cancelAlert(WebDriver driver) {
		alert = waitForAlertPresent(driver);
		alert.dismiss();
	}
	
	public void sendKeysToAlert(WebDriver driver, String value) {
		alert = waitForAlertPresent(driver);
		alert.sendKeys(value);
	}
	
	public String getAlertText(WebDriver driver) {
		alert = waitForAlertPresent(driver);
		return alert.getText();
	}
	
	public void switchToWindowById(WebDriver driver, String parentID) {
		Set<String>  allWindowsId = driver.getWindowHandles();
		for (String windowId : allWindowsId) {
			if(!windowId.equals(parentID)) {
				driver.switchTo().window(windowId);
				break;
			}
		}
	}
	
	public void switchToWindowByTitle(WebDriver driver, String windowTitle) {
		Set<String>  allWindowsId = driver.getWindowHandles();
		for (String windowId : allWindowsId) {
			driver.switchTo().window(windowId);
			String actualWindowTitle = driver.getTitle();
			if(actualWindowTitle.equals(windowTitle)) {
				break;
			}
		}
	}
	
	public void closeAllWindowsExceptParent(WebDriver driver, String parentId) {
		Set<String> allWindowsId = driver.getWindowHandles();
		for (String windowId : allWindowsId) {
			if(!windowId.equals(parentId)) {
				driver.switchTo().window(windowId);
				driver.close();
				sleepInSecond(1);
			}
			if (driver.getWindowHandles().size()==1) {
				driver.switchTo().window(parentId);
				break;
			}
		}
	}
	
	public void sleepInSecond(long timeSleep) {
		try {
			Thread.sleep(timeSleep*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void backToPreviousPage(WebDriver driver) {
		driver.navigate().back();
	}
	
	public void forwardToPage(WebDriver driver) {
		driver.navigate().forward();;
	}
	
	public void refreshCurrentPage(WebDriver driver) {
		driver.navigate().refresh();;
	}
	
	// Web Element
	
	public By getByXpath(String locator) {
		return By.xpath(locator);
	}
	
	public WebElement getElementByXpath(WebDriver driver, String locator) {
		return driver.findElement(getByXpath(locator));
	}
	
	public List<WebElement> getElementsByXpath(WebDriver driver, String locator) {
		return driver.findElements(getByXpath(locator));
	}
	
	public void clickToElement(WebDriver driver, String locator) {
		getElementByXpath(driver, locator).click();
	}
	
	public void sendKeysToElement(WebDriver driver, String locator, String value) {
		getElementByXpath(driver, locator).clear();
		getElementByXpath(driver, locator).sendKeys(value);
	}
	
	public int getNumberOfElements(WebDriver driver, String locator) {
		return getElementsByXpath(driver, locator).size();
	}
	
	public void selectDropDownByText(WebDriver driver, String locator, String itemText) {
		select = new Select(getElementByXpath(driver, locator));
		select.selectByVisibleText(itemText);
	}
	
	public String getSelectedItemDropDown(WebDriver driver, String locator) {
		select = new Select(getElementByXpath(driver, locator));
		return select.getFirstSelectedOption().getText();
	}
	
	public boolean isDropDownMultiple(WebDriver driver, String locator) {
		select = new Select(getElementByXpath(driver, locator));
		return select.isMultiple();
	}
	
	public void selectItemInCustomDropDownByText(WebDriver driver, String parentLocator, String childLocator, String itemText) {
		getElementByXpath(driver, parentLocator).click();
		sleepInSecond(1);
		
		explicitWait = new WebDriverWait(driver, timeout);
		List<WebElement> allItems = explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(childLocator)));
		
		for (WebElement item : allItems) {
			if(item.getText().trim().equals(itemText)) {
				jsExecutor = (JavascriptExecutor) driver;
				jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);
				sleepInSecond(1);
				
				item.click();
				sleepInSecond(1);
				break;
			}
		}
	}
	
	public String getElementAttribute(WebDriver driver, String locator, String attributeName) {
		return getElementByXpath(driver, locator).getAttribute(attributeName);
	}
	
	public String getElementText(WebDriver driver, String locator) {
		return getElementByXpath(driver, locator).getText();
	}
	
	public void checkToCheckboxOrRadio(WebDriver driver, String locator) {
		if(!isElementSelected(driver, locator)) {
			getElementByXpath(driver, locator).click();
		}
	}
	
	public void uncheckToCheckbox(WebDriver driver, String locator) {
		if(isElementSelected(driver, locator)) {
			getElementByXpath(driver, locator).click();
		}
	}
	
	public boolean isElementSelected(WebDriver driver, String locator) {
		return getElementByXpath(driver, locator).isSelected();
	}
	
	public boolean isElementEnbaled(WebDriver driver, String locator) {
		return getElementByXpath(driver, locator).isEnabled();
	}
	
	public boolean isElementDisplayed(WebDriver driver, String locator) {
		return getElementByXpath(driver, locator).isDisplayed();
	}
	
	public WebDriver switchToIframeByElement(WebDriver driver, String locator) {
		return driver.switchTo().frame(getElementByXpath(driver, locator));
	}
	
	public WebDriver switchToDefaultContent(WebDriver driver) {
		return driver.switchTo().defaultContent();
	}
	
	// Actions
	
	public void hoverToElement(WebDriver driver, String locator) {
		actions = new Actions(driver);
		actions.moveToElement(getElementByXpath(driver, locator)).perform();
	}
	
	public void doubleClickToElement(WebDriver driver, String locator) {
		actions = new Actions(driver);
		actions.doubleClick(getElementByXpath(driver, locator)).perform();
	}
	
	public void rightClickToElement(WebDriver driver, String locator) {
		actions = new Actions(driver);
		actions.contextClick(getElementByXpath(driver, locator)).perform();
	}
	
	public void dragAndDropElement(WebDriver driver, String sourceLocator, String targetLocator) {
		actions = new Actions(driver);
		actions.dragAndDrop(getElementByXpath(driver, sourceLocator), getElementByXpath(driver, targetLocator)).perform();
	}
	
	public void pressKeyToElement(WebDriver driver, String locator, Keys keyboard) {
		actions = new Actions(driver);
		actions.sendKeys(getElementByXpath(driver, locator), keyboard).perform();
	}

	// JavaScripts Executor - Not implemented yet
	
	// Wait handling
	
	public void waitForElementVisible(WebDriver driver, String locator) {
		explicitWait = new WebDriverWait(driver, timeout);
		explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(locator)));
	}
	
	public void waitForAllElementsVisible(WebDriver driver, String locator) {
		explicitWait = new WebDriverWait(driver, timeout);
		explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(locator)));
	}
	
	public void waitForElementInvisible(WebDriver driver, String locator) {
		explicitWait = new WebDriverWait(driver, timeout);
		explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(getByXpath(locator)));
	}
	
	public void waitForElementClickable(WebDriver driver, String locator) {
		explicitWait = new WebDriverWait(driver, timeout);
		explicitWait.until(ExpectedConditions.elementToBeClickable(getByXpath(locator)));
	}
	
	
	private Alert alert;
	private Select select;
	private Actions actions;
	private long timeout = 30;
	private WebDriverWait explicitWait;
	private JavascriptExecutor jsExecutor;
}