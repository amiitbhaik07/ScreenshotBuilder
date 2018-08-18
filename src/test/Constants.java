package test;

public class Constants
{		
	public static final int defaultTimeoutSeconds = 180;
	public static final long ifLogicTimeoutSeconds = 30;
	public static final int ifVisibleTimeoutSeconds = 4;
	public static final int disappearanceTimeoutSeconds = 7;
	
	public static final long notPresenceTimeoutMillis = 30000;
	public static final int actionTryGapMillis = 5000;
	public static final int defaultPauseMillis = 100;
	public static final int preLoaderDefaultPauseMillis = 50;

	public static final int defaultVerticalScrollPixel = 200;
	public static final int defaultHorizontalScrollPixel = 30;
	
	public static final int clearCacheAndRestartAfterCount = 25;
	
	public static final int actionTryCount = 7;
	
	
	
	
	
	//Parallel Processing
	public static final int minimumDealsForParallelProcessing = clearCacheAndRestartAfterCount;
	public static final int maximumThreads = 5;
	public static final int maximumDealsPerThead = clearCacheAndRestartAfterCount;

	public static final String preLoaderXpath = "//img[contains(@src,'preloader')]";
	
	
	//Exceptions when Execution would cease
	public static final String errorOnNotFindingDealIdTextbox = "Unable to find Deal ID Textbox on UI";
	public static final String errorOnClosingFirefox = "Sorry! You firefox was abruptly closed!";
	public static final String errorOnClosingFirefox1 = "Failed to decode response from marionette";
	public static final String errorOnClosingFirefox2 = "Tried to run command without establishing a connection";
	
	//Exceptions when Execution continues
	public static final String errorOnNotSearchingDealId = "Deal ID could not be searched : ";
	public static final String errorOnWronglySearchingDealId = "Issue in searching Deal ID : ";
	public static final String errorOnUnableToClickDealPricing = "Unable to click on Deal Pricing tab ";
	public static final String errorOnUnableToClickDealPricingExpandAll = "Unable to Expand All fields in Deal Pricing tab ";
	public static final String errorOnUnableToClickDealPricingExportDropdown = "Unable to click on Export dropdown in Deal Pricing tab ";
	public static final String errorOnUnableToFindDealPricingExportDropdown = "Unable to find Export dropdown in Deal Pricing tab ";
	public static final String errorOnUnableToClickDealPricingExportButton = "Unable to Advance Export in Deal Pricing tab ";
	public static final String errorOnUnableToClickDealSummary = "Unable to click on Deal Summary tab ";
	public static final String errorOnUnableToClickDealSummaryExportDropdown = "Unable to click on Export button on Deal Summary tab ";
	
	

	
}
