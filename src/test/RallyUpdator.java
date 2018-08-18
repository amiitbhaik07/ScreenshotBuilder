package test;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.Select;

public class RallyUpdator implements Runnable
{
	String rallyBuild;
	String rallyTestCaseStatus = "";
	String rallyNotes = "";
	String rallyCecId = ScreenshotBuilder_RallyIntegrated.rallyCecId;
	String rallyCecPassword = ScreenshotBuilder_RallyIntegrated.rallyCecPassword;
	String rallyUserStory = ScreenshotBuilder_RallyIntegrated.rallyUserStory;
	String testCaseId = "";
	String lastTestCaseName = ScreenshotBuilder_RallyIntegrated.lastTestCaseName;
	String lastScreenshotWordPath = ScreenshotBuilder_RallyIntegrated.lastScreenshotWordPath;
	
	static WebDriver firefoxDriver = null, phantomDriver=null, chromeDriver=null;
	static BasicUtils basicFirefox=null, basicPhantom=null, basicChrome=null;	
	Properties prop = null;
	static String browserName_testPass = "C";
	static String browserName_defect = "C";
	static long lastUsedFirefox=0, lastUsedChrome=0, lastUsedPhantom=0;
	static long rallySessionTimeout = 12600000; //3.5 hours	
		
	public void loadPropertiesFile() throws Exception
	{
		try
		{
			prop = new Properties();
			InputStream input = new FileInputStream("inputDefaults.properties");
			prop.load(input);
		}
		catch(Exception e)
		{
			prop = ScreenshotBuilder_RallyIntegrated.prop;
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			loadPropertiesFile();
		}
		catch(Exception e){}
		System.out.println("Running Rally intergrator...");			
		JFrame frame = new JFrame("Rally Integrator ~ Amit_Bhaik");		
		frame.setLayout(new GridLayout(11,2));
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		JPanel p5 = new JPanel();
		JPanel p6 = new JPanel();
		JPanel p7 = new JPanel();
		JPanel p8 = new JPanel();
		JPanel p9 = new JPanel();
		JPanel p10 = new JPanel();
		JPanel p11 = new JPanel();
		JLabel cecIdLabel = new JLabel("                          CEC ID : ");
		JLabel passwordLabel = new JLabel("                   Password : ");
		JLabel userStoryLabel = new JLabel("                   User Story : ");
		JLabel testCaseIdLabel = new JLabel("                Test Case ID : ");
		JLabel testCaseNameLabel = new JLabel("(OR) Test Case Name : ");
		JLabel screenshotPathLabel = new JLabel("         Screenshot Path : ");
		JLabel buildLabel = new JLabel("                               Build : ");
		JLabel statusLabel = new JLabel("        Test Case Status : ");
		JLabel notesLabel = new JLabel("                             Notes : ");		
		JLabel infoLabel = new JLabel("You will be notified once the upload is complete.");		
		JTextField cecIdTf = new JTextField(rallyCecId,30);
		JPasswordField passwordTf = new JPasswordField(rallyCecPassword,30);
		JTextField userStoryTf = new JTextField(rallyUserStory,30);
		JTextField testCaseIdTf = new JTextField(testCaseId,30);
		JTextField testCaseNameTf = new JTextField(lastTestCaseName,30);
		JTextField screenshotPathTf = new JTextField(lastScreenshotWordPath,30);
		JTextField buildTf = new JTextField(new SimpleDateFormat("MMMdd_HH_mm_ss").format(new Date()), 30);
		JComboBox<String> statusSelect = new JComboBox<String>(new String[] {"Blocked", "Error", "Fail", "Inconclusive", "Pass"});
		statusSelect.setSelectedIndex(4);
		JTextArea notesTa = new JTextArea("", 3, 30);
		JScrollPane notesTaScroll = new JScrollPane(notesTa);
		cecIdTf.setEditable(false);
		passwordTf.setEditable(false);		
		
		p1.add(cecIdLabel);
		p1.add(cecIdTf);
		p2.add(passwordLabel);
		p2.add(passwordTf);
		p3.add(userStoryLabel);
		p3.add(userStoryTf);
		p4.add(testCaseIdLabel);
		p4.add(testCaseIdTf);
		p5.add(testCaseNameLabel);
		p5.add(testCaseNameTf);
		p6.add(screenshotPathLabel);
		p6.add(screenshotPathTf);
		p7.add(buildLabel);
		p7.add(buildTf);
		p8.add(statusLabel);
		p8.add(statusSelect);
		p9.add(notesLabel);
		p9.add(notesTaScroll);	
		p10.setOpaque(true);
		p10.add(infoLabel);
		
		JButton continueButton = new JButton("Continue");
		continueButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
			   System.out.println("Continue pressed");
			   try
			   {
				   //rallyCecId = cecIdTf.getText();
				   //rallyCecPassword = passwordTf.getText();
				   rallyUserStory = userStoryTf.getText();
				   testCaseId = testCaseIdTf.getText();
				   lastTestCaseName = testCaseNameTf.getText();
				   //JOptionPane.showMessageDialog(null, "Test Case Name :\n\n"+lastTestCaseName, "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				   lastScreenshotWordPath = screenshotPathTf.getText();				   
				   rallyBuild = buildTf.getText();
				   rallyTestCaseStatus = (String)statusSelect.getSelectedItem();
				   rallyNotes = notesTa.getText();
				   if(lastTestCaseName.equalsIgnoreCase("") && testCaseId.equalsIgnoreCase(""))
				   {
					   JOptionPane.showMessageDialog(null, "Please enter either Test Case ID or Test Case Name to proceed", "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				   }
				   else if(lastScreenshotWordPath.equalsIgnoreCase(""))
				   {
					   JOptionPane.showMessageDialog(null, "Please provide the screenshot document path", "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				   }
				   else if(rallyBuild.equalsIgnoreCase(""))
				   {
					   JOptionPane.showMessageDialog(null, "Please provide the Build name", "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				   }
				   
				   if(! (lastTestCaseName.equalsIgnoreCase("") && testCaseId.equalsIgnoreCase("")))
				   {
					   if(! lastScreenshotWordPath.equalsIgnoreCase(""))
					   {
						   if(! rallyBuild.equalsIgnoreCase(""))
						   {
							   frame.setVisible(false);
							   frame.dispose();	
							   BasicUtils basic1 = null;
							   
							   try
							   {								   
								   setupBrowser(browserName_testPass);
								   if(browserName_testPass.equalsIgnoreCase("P"))
									   basic1 = basicPhantom;
								   else if(browserName_testPass.equalsIgnoreCase("F"))
									   basic1 = basicFirefox;
								   else if (browserName_testPass.equalsIgnoreCase("C"))
									   basic1 = basicChrome;
								   else
									   basic1 = basicChrome;
								   actualRallyUpload(basic1, browserName_testPass, rallyCecId, rallyCecPassword, rallyUserStory, testCaseId, lastTestCaseName, lastScreenshotWordPath, rallyBuild, rallyTestCaseStatus, rallyNotes);
							   }
							   catch(Exception e3)
							   {
								   if(e3.getMessage().contains("Unable to search the"))
								   {
									   JOptionPane.showMessageDialog(null, e3.getMessage().replace("java.lang.Exception:", ""), "Rally Updator ~ Amit_Bhaik", JOptionPane.ERROR_MESSAGE);
								   }
								   else if(e3.getMessage().contains("Unable to Log-in Rally"))
								   {
									   JOptionPane.showMessageDialog(null, e3.getMessage().replace("java.lang.Exception:", ""), "Rally Updator ~ Amit_Bhaik", JOptionPane.ERROR_MESSAGE);
								   }
								   else if(e3.getMessage().contains("Unable to open New Defects page"))
								   {
									   JOptionPane.showMessageDialog(null, e3.getMessage().replace("java.lang.Exception:", ""), "Rally Updator ~ Amit_Bhaik", JOptionPane.ERROR_MESSAGE);
								   }
								   else
								   {
									   try
									   {   
										   try
										   {
											   phantomDriver=null;
											   firefoxDriver=null;
											   chromeDriver=null;
										   }
										   catch(Exception e1){}
										   
										   basic1.pause(1000);
										   setupBrowser(browserName_testPass);
										   
										   if(browserName_testPass.equalsIgnoreCase("P"))
											   basic1 = basicPhantom;
										   else if(browserName_testPass.equalsIgnoreCase("F"))
											   basic1 = basicFirefox;
										   else if (browserName_testPass.equalsIgnoreCase("C"))
											   basic1 = basicChrome;
										   else
											   basic1 = basicChrome;									   
										   
										   basic1.getDriver().manage().deleteAllCookies();
										   basic1.getDriver().manage().window().maximize();									   
										   actualRallyUpload(basic1, browserName_testPass, rallyCecId, rallyCecPassword, rallyUserStory, testCaseId, lastTestCaseName, lastScreenshotWordPath, rallyBuild, rallyTestCaseStatus, rallyNotes);
									   }
									   catch(Exception e4)
									   {
										   JOptionPane.showMessageDialog(null, "Error in updating Test Case in Rally :\n\n" + e4.getMessage(), "Rally Updator ~ Amit_Bhaik", JOptionPane.ERROR_MESSAGE);
									   }   
								   }
							   }
						   }
					   }
				   }				   
			   }
			   catch(Exception e1)
			   {
				   e1.printStackTrace();
			   }
			  }
			} );
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
			   System.out.println("Cancel pressed");
			   try
			   {
				   frame.setVisible(false);
				   frame.dispose();
			   }
			   catch(Exception e1)
			   {
				   e1.printStackTrace();
			   }
			  }
			} );
		
		JButton newDefectButton = new JButton("New Defect");
		newDefectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				
				lastScreenshotWordPath = screenshotPathTf.getText();
				//JOptionPane.showMessageDialog(null, "New Defect......", "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				if(lastScreenshotWordPath.equalsIgnoreCase(""))
				{
				   JOptionPane.showMessageDialog(null, "Please provide the screenshot document path", "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				}
				
				frame.setVisible(false);
				frame.dispose();				
				if(! lastScreenshotWordPath.equalsIgnoreCase(""))
				{
					try
					{
						setupBrowser(browserName_defect);
						
						BasicUtils basic = null;
						if(browserName_defect.equalsIgnoreCase("F"))
							basic = basicFirefox;
						else
							basic = basicChrome;						
						
						rallyLogin(basic, rallyCecId, rallyCecPassword);
						searchUserStory(basic, rallyUserStory);
						openNewDefectTab(basic);
						basic.sendKeysUploadFile("//input[@class='attachment-upload-button']", lastScreenshotWordPath);
						
						try
						{
							if(prop.getProperty("environment")!=null)
							{
								if(! prop.getProperty("environment").equals(""))
									basic.sendClearKeys("//input[@name='Environment']", prop.getProperty("environment").trim());
							}
						}
						catch(Exception e2){}		
						
						try
						{
							if(prop.getProperty("priority")!=null)
							{
								if(! prop.getProperty("priority").equals(""))
									basic.sendClearKeys("//input[@name='Priority']", prop.getProperty("priority").trim());
							}
						}
						catch(Exception e2){}			
						
						try
						{
							if(prop.getProperty("severity")!=null)
							{
								if(! prop.getProperty("severity").equals(""))
									basic.sendClearKeys("//input[@name='Severity']", prop.getProperty("severity").trim());
							}
						}
						catch(Exception e2){}
						
						try
						{
							if(prop.getProperty("release")!=null)
							{
								if(! prop.getProperty("release").equals(""))
								{
									String release = prop.getProperty("release").trim();
									basic.click("//input[@name='Release']");
									basic.pause(500);
									basic.click("//div[@class='timebox-name' and text()='"+release+"']");
								}
							}
						}
						catch(Exception e2){}
						
						basic.click("//div[@class='head' and text()='Custom']/preceding-sibling::div");
						
						try
						{
							if(prop.getProperty("assignedToGroup")!=null)
							{
								if(! prop.getProperty("assignedToGroup").equals(""))
									basic.sendClearKeys("//input[@name='c_AssignedtoGroup']", prop.getProperty("assignedToGroup").trim());
							}
						}
						catch(Exception e2){}		
						
						try
						{
							if(prop.getProperty("defectType")!=null)
							{
								if(! prop.getProperty("defectType").equals(""))
									basic.sendClearKeys("//input[@name='c_DefectType']", prop.getProperty("defectType").trim());
							}		
						}
						catch(Exception e2){}
						
						try
						{
							if(prop.getProperty("owningTrack")!=null)
							{
								if(! prop.getProperty("owningTrack").equals(""))
									basic.sendClearKeys("//input[@name='c_OwningTrack']", prop.getProperty("owningTrack").trim());
							}		
						}
						catch(Exception e2){}
						
						try
						{
							if(prop.getProperty("reportedTrack")!=null)
							{
								if(! prop.getProperty("reportedTrack").equals(""))
									basic.sendClearKeys("//input[@name='c_ReportedTrack']", prop.getProperty("reportedTrack").trim());
							}
						}
						catch(Exception e2){}								
						
						try
						{
							if(prop.getProperty("owner")!=null)
							{
								if(! prop.getProperty("owner").equals(""))
									basic.sendClearKeys("//input[@name='Owner']", prop.getProperty("owner").trim());
							}		
						}
						catch(Exception e2){}										
												
						setLastUsed(basic);
						JOptionPane.showMessageDialog(null, "Successfully entered all Default fields in Defects tab, please proceed...", "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
					}
					catch(Exception e4){
						JOptionPane.showMessageDialog(null, "Error while creating Defect\n\n" + e4.getMessage(), "Rally Updator ~ Amit_Bhaik", JOptionPane.ERROR_MESSAGE);
					}		
				}
			}
		});
		
		statusSelect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				String selected = (String)statusSelect.getSelectedItem();
				if(selected.equalsIgnoreCase("Fail"))
				{
					p11.add(newDefectButton);
				}
				else
				{
					p11.remove(newDefectButton);
				}
				frame.setVisible(false);
				frame.setVisible(true);
			}
		});
		
		p11.add(continueButton);
		p11.add(cancelButton);		
		frame.add(p1);
		frame.add(p2);
		frame.add(p3);
		frame.add(p4);
		frame.add(p5);
		frame.add(p6);
		frame.add(p7);
		frame.add(p8);
		frame.add(p9);
		frame.add(p10);
		frame.add(p11);		
		frame.setBounds(600,200,580,450);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public void setLastUsed(BasicUtils basic)
	{
		try
		{
			if(basic.getDriver() instanceof FirefoxDriver)
			{
				lastUsedFirefox = System.currentTimeMillis();
			}
			else if(basic.getDriver() instanceof PhantomJSDriver)
			{
				lastUsedPhantom = System.currentTimeMillis();
			}
			else if(basic.getDriver() instanceof ChromeDriver)
			{
				lastUsedChrome = System.currentTimeMillis();
			}
		}
		catch(Exception e){}
	}
	
	
	public void actualRallyUpload(BasicUtils basic, String browserName, String rallyCecId, String rallyCecPassword, String rallyUserStory, String testCaseId, String lastTestCaseName, String lastScreenshotWordPath, String rallyBuild, String rallyTestCaseStatus, String rallyNotes) throws Exception
	{
		try
		{
			rallyLogin(basic, rallyCecId, rallyCecPassword);
			searchUserStory(basic, rallyUserStory);
			openTestCaseTab(basic);
			if(testCaseId!=null)
			{
				if(! testCaseId.trim().equalsIgnoreCase(""))
				{
					//test case id is present successfully!
					searchForTestCaseId(basic, testCaseId, rallyUserStory);
					basic.click("//td[contains(@class,'formattedid')]/a[contains(text(),'"+testCaseId.trim()+"')]/ancestor::tr/descendant::a[contains(@id,'addtcr_')]");
				}
				else
				{
					//test case id is "", so search by test case name
					searchForTestCase(basic, lastTestCaseName, rallyUserStory);
					System.out.println("Clicking on Add Test case result...");
					basic.click("//a[text()=\""+lastTestCaseName+"\"]/parent::td/parent::tr/descendant::a[contains(@id,'addtcr') and contains(@class,'new-test-case-result')]");
				}
			}
			else
			{
				//test case id is null, so search by test case name
				searchForTestCase(basic, lastTestCaseName, rallyUserStory);
				System.out.println("Clicking on Add Test case result...");
				basic.click("//a[text()=\""+lastTestCaseName+"\"]/parent::td/parent::tr/descendant::a[contains(@id,'addtcr') and contains(@class,'new-test-case-result')]");
			}
			
			
			
			System.out.println("New window opened");
			Thread.sleep(1200);
			String originalWindow = basic.getDriver().getWindowHandle();
			Set<String> allWindows = basic.getDriver().getWindowHandles();
			for(String window : allWindows)
			{
				basic.getDriver().switchTo().window(window);
				System.out.println("Switched to new window : " + window);
			}
			
			basic.typeText("//input[@name='build']", rallyBuild);
			new Select(basic.getDriver().findElement(By.xpath("//select[@name='verdict']"))).selectByVisibleText(rallyTestCaseStatus);
			basic.sendKeysUploadFile("//input[@name='file']", lastScreenshotWordPath);
			
			basic.getDriver().switchTo().frame(basic.getDriver().findElement(By.id("notesEditorContent")));
			basic.typeText("//body[@id='notesEditorContent']", rallyNotes);
			
			if(prop.getProperty("test")!=null)
			{
				if(prop.getProperty("test").equalsIgnoreCase("test"))
				{
					//Do nothing
					JOptionPane.showMessageDialog(null, "Unable to Save and Close, please remove 'test=test' field from inputDefaults.properties and try again.", "Rally Updator ~ Amit_Bhaik", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					//JOptionPane.showMessageDialog(null, "Clicking Save and Close", "Rally Updator ~ Amit_Bhaik", JOptionPane.ERROR_MESSAGE);
					basic.click("//button[@id='save_and_close_btn']");
					JOptionPane.showMessageDialog(null, "Test Case successfully updated in Rally!\nTC Name : "+lastTestCaseName+"\nStatus : " + rallyTestCaseStatus, "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				}			
			}
			else
			{
				//JOptionPane.showMessageDialog(null, "Clicking Save and Close", "Rally Updator ~ Amit_Bhaik", JOptionPane.ERROR_MESSAGE);
				basic.click("//button[@id='save_and_close_btn']");
				JOptionPane.showMessageDialog(null, "Test Case successfully updated in Rally!\nTC Name : "+lastTestCaseName+"\nStatus : " + rallyTestCaseStatus, "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
			}
				
			
			System.out.println("Successfully submitted test case status");
			
			
			basic.getDriver().switchTo().window(originalWindow);
			
			setLastUsed(basic);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	
	public boolean isUpAndRunning(WebDriver driver)
	{
		try
		{
			driver.getWindowHandle();
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public boolean isOnCorrectPage(WebDriver driver)
	{
		try
		{
			driver.getWindowHandle();
			
			
			if(driver instanceof FirefoxDriver)
			{
				if(System.currentTimeMillis()-lastUsedFirefox > rallySessionTimeout)
				{
					return false;
				}
			}
			else if(driver instanceof PhantomJSDriver)
			{
				if(System.currentTimeMillis()-lastUsedPhantom > rallySessionTimeout)
				{
					return false;
				}
			}
			else if(driver instanceof ChromeDriver)
			{
				if(System.currentTimeMillis()-lastUsedChrome > rallySessionTimeout)
				{
					return false;
				}
			}			
			driver.findElement(By.xpath("//div[@class='icon-search']"));
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	
	public void setupBrowser(String browserName) throws Exception
	{
		if(browserName.equalsIgnoreCase("F"))
		{
			if(firefoxDriver==null)
			{
				System.setProperty("webdriver.firefox.marionette", System.getProperty("user.dir")+"/lib/geckodriver.exe");
				firefoxDriver = new FirefoxDriver();
				firefoxDriver.manage().window().maximize();
				basicFirefox = new BasicUtils(firefoxDriver);
				setLastUsed(basicFirefox);
			}	
			else if(! isUpAndRunning(firefoxDriver))
			{
				System.setProperty("webdriver.firefox.marionette", System.getProperty("user.dir")+"/lib/geckodriver.exe");
				firefoxDriver = new FirefoxDriver();
				firefoxDriver.manage().window().maximize();
				basicFirefox = new BasicUtils(firefoxDriver);
				setLastUsed(basicFirefox);
			}	
			else if(! isOnCorrectPage(firefoxDriver))
			{
				try
				{
					firefoxDriver.quit();
				}
				catch(Exception e){}
				System.setProperty("webdriver.firefox.marionette", System.getProperty("user.dir")+"/lib/geckodriver.exe");
				firefoxDriver = new FirefoxDriver();
				firefoxDriver.manage().window().maximize();
				firefoxDriver.manage().deleteAllCookies();
				basicFirefox = new BasicUtils(firefoxDriver);
				setLastUsed(basicFirefox);
			}	
			else
			{
				System.out.println("Firefox already defined and running");
			}
		}
		else if(browserName.equalsIgnoreCase("P"))
		{
			if(phantomDriver==null)
			{
				System.setProperty("phantomjs.binary.path", System.getProperty("user.dir")+"/lib/phantomjs.exe");
				phantomDriver = new PhantomJSDriver();
				phantomDriver.manage().window().maximize();
				basicPhantom = new BasicUtils(phantomDriver);
				setLastUsed(basicPhantom);
			}	
			else if(! isUpAndRunning(phantomDriver))
			{
				System.setProperty("phantomjs.binary.path", System.getProperty("user.dir")+"/lib/phantomjs.exe");
				phantomDriver = new PhantomJSDriver();
				phantomDriver.manage().window().maximize();
				basicPhantom = new BasicUtils(phantomDriver);
				setLastUsed(basicPhantom);
			}	
			else if(! isOnCorrectPage(phantomDriver))
			{
				try
				{
					phantomDriver.quit();
				}
				catch(Exception e){}
				System.setProperty("phantomjs.binary.path", System.getProperty("user.dir")+"/lib/phantomjs.exe");
				phantomDriver = new PhantomJSDriver();
				phantomDriver.manage().window().maximize();
				phantomDriver.manage().deleteAllCookies();
				basicPhantom = new BasicUtils(phantomDriver);
				setLastUsed(basicPhantom);
			}	
			else
			{
				System.out.println("Phantomjs already defined and running");
			}
		}
		else if(browserName.equalsIgnoreCase("C"))
		{
			if(chromeDriver==null)
			{
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/lib/chromedriver.exe");
				chromeDriver = new ChromeDriver();
				chromeDriver.manage().window().maximize();
				basicChrome = new BasicUtils(chromeDriver);
				setLastUsed(basicChrome);
			}	
			else if(! isUpAndRunning(chromeDriver))
			{
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/lib/chromedriver.exe");
				chromeDriver = new ChromeDriver();
				chromeDriver.manage().window().maximize();
				basicChrome = new BasicUtils(chromeDriver);
				setLastUsed(basicChrome);
			}	
			else if(! isOnCorrectPage(chromeDriver))
			{
				try
				{
					chromeDriver.quit();
				}
				catch(Exception e){}
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/lib/chromedriver.exe");
				chromeDriver = new ChromeDriver();
				chromeDriver.manage().window().maximize();
				chromeDriver.manage().deleteAllCookies();
				basicChrome = new BasicUtils(chromeDriver);
				setLastUsed(basicChrome);
			}	
			else
			{
				System.out.println("Chrome already defined and running");
			}
		}
	}
	
	
	public void rallyLogin(BasicUtils basic, String cecIdValue, String passwordValue) throws Exception
	{
		try
		{
			basic.getDriver().findElement(By.xpath("//div[@class='icon-search']"));
			System.out.println("Already Logged In");
		}
		catch(Exception e)
		{
			System.out.println("Logging In Rally...");
			basic.clearBrowserCache();
			basic.justNavigate("https://rally1.rallydev.com");
			if(cecIdValue.contains("@cisco.com"))
			{
				basic.typeText("//input[@id='j_username']", cecIdValue);
			}
			else
			{
				basic.typeText("//input[@id='j_username']", cecIdValue+"@cisco.com");
			}
			basic.typeText("//input[@id='j_password']", passwordValue);
			basic.click("//input[@id='login-button']");
			
			if(cecIdValue.contains("@cisco.com"))
			{
				basic.clearUpdateText("//input[@id='userInput']", cecIdValue.split("@")[0]);
			}
			else
			{
				basic.clearUpdateText("//input[@id='userInput']", cecIdValue);
			}
			
			basic.clearUpdateText("//input[@id='passwordInput']", passwordValue);
			basic.click("//input[@id='login-button']");
			
			try
			{
				basic.waitForElementDisappear("//input[@id='login-button']");
			}
			catch(Exception e3){
				throw new Exception("Unable to Log-in Rally, please verify your credentials");
			}
			
			System.out.println("Logged In Rally !!!");
		}		
	}	
	
	public void searchUserStory(BasicUtils basic, String rallyUserStory) throws Exception
	{
		try
		{
			basic.click("//div[@class='icon-search']");
			if(! basic.knowIfAppears("//input[contains(@id,'rallytextfield') and @placeholder='Search']"))
			{
				basic.click("//div[@class='icon-search']");
				if(! basic.knowIfAppears("//input[contains(@id,'rallytextfield') and @placeholder='Search']"))
				{
					basic.click("//div[@class='icon-search']");
				}
			}
			System.out.println("Clicked on Search ICON");
		}
		catch(Exception e){
			throw new Exception("Unable to Log-in Rally; please verify your credentials!");
		}
		System.out.println("Typing '"+rallyUserStory.trim()+"' in US");
		basic.typeText("//input[contains(@id,'rallytextfield') and @placeholder='Search']", rallyUserStory.trim());
		Thread.sleep(250);
		basic.pressEnterUsingActions();
		System.out.println("Opened User story '"+rallyUserStory+"'");
	}
	
	public void openNewDefectTab(BasicUtils basic) throws Exception
	{
		System.out.println("Opening Defects Tab");
		try
		{
			basic.waitForElementVisible("//div[@class='rly-label' and text()='Defects']");
		}
		catch(Exception e){
			throw new Exception("Unable to search the User Story, please try again!");
		}
		basic.click("//div[@class='rly-label' and text()='Defects']");
		
		try
		{
			basic.waitForElementDisappear("//input[contains(@id,'rallyusersearchcombobox-')]");
		}
		catch(Exception e)
		{
			basic.click("//div[@class='rly-label' and text()='Defects']");
		}		
		
		
		basic.waitForElementVisible("//span[contains(text(),'Add with Details')]/following-sibling::span");
		
		basic.click("//span[contains(text(),'Add with Details')]/following-sibling::span");
		
		try
		{
			basic.waitForElementDisappear("//input[contains(@id,'rallytextfield-')]");
		}
		catch(Exception e)
		{
			basic.click("//span[contains(text(),'Add with Details')]/following-sibling::span");
		}
		
		try
		{
			basic.waitForElementVisible("//input[@class='simpleTextDetailField']");
		}
		catch(Exception e)
		{
			throw new Exception("Unable to open New Defects page");
		}
		
		basic.click("//input[@class='simpleTextDetailField']");
		System.out.println("Opened Defects Tab");
	}
	
	public void openTestCaseTab(BasicUtils basic) throws Exception
	{		
		try
		{
			basic.waitForElementVisible("//div[@class='rly-label' and text()='Test Cases']");
		}
		catch(Exception e)
		{
			throw new Exception("Unable to Search User Story, please try again!");
		}
		basic.click("//div[@class='rly-label' and text()='Test Cases']");
		try
		{
			basic.waitForElementDisappear("//input[contains(@id,'rallyusersearchcombobox-')]");
		}
		catch(Exception e2)
		{
			basic.click("//div[@class='rly-label' and text()='Test Cases']");			
		}
		
		basic.waitForElementVisible("//input[@id='detailtestcaseDispNumItem']");
		try
		{
			if(!basic.knowIfAppears("//input[@id='detailtestcaseDispNumItem' and @value='99']"))
			{
				basic.scrollIntoView("//input[@id='detailtestcaseDispNumItem']");
				basic.clearUpdateText("//input[@id='detailtestcaseDispNumItem']", "99");		
				basic.click("//a[@title='Update' and contains(@class,'update')]");
				basic.pause(3000);
				basic.waitForElementVisible("//input[@id='detailtestcaseDispNumItem' and @value='99']");
			}
		}
		catch(Exception e){}
	}
	
	public void searchForTestCaseId(BasicUtils basic, String testCaseId, String userStory) throws Exception
	{
		boolean isTestCaseAppeared=false;
		
		try
		{
			basic.scrollIntoView("//input[@name='idFilter']");
			basic.clearUpdateText("//input[@name='idFilter']", testCaseId);
			basic.pressEnterUsingActions();
			basic.pause(1000);
			if(basic.knowIfAppears("//table[@id='detailtestcase_tps']/tbody/tr[contains(@id,'tr')]"))
			{
				//Search Successful
				isTestCaseAppeared=true;
				try
				{
					basic.waitForElementVisible("//td[contains(@class,'formattedid')]/a[contains(text(),'"+testCaseId.trim()+"')]");
				}
				catch(Exception e)
				{
					throw new Exception("Unable to search the Test Case ID '"+testCaseId+"' in User Story '"+userStory+"'");
				}
			}
			else if(basic.knowIfAppears("//p[contains(text(),'There are no Test Cases to display')]"))
			{
				throw new Exception("Unable to search the Test Case ID '"+testCaseId+"' in User Story '"+userStory+"'");
			}
			else
			{
				throw new Exception("Unable to search the Test Case ID '"+testCaseId+"' in User Story '"+userStory+"'...!!!");
			}
		}
		catch(Exception e){
			throw new Exception(e.getMessage());
		}
		
		if(! isTestCaseAppeared)
		{
			throw new Exception("Unable to search the following Test Case in User Story '"+userStory+"' :\n"+lastTestCaseName);
		}
		else
		{
			System.out.println("Success in searching test case name :" + lastTestCaseName);
		}
	}
	
	
	
	
	
	public void searchForTestCase(BasicUtils basic, String lastTestCaseName, String userStory) throws Exception
	{
		String tempStr="";
		boolean isTestCaseAppeared=false;
		try
		{
			basic.waitForElementVisible("//input[@name='idFilter']");
		}
		catch(Exception e)
		{
			throw new Exception("Page changed, please try again");
		}
		
		if(! basic.knowIfAppears("//input[@name='idFilter' and @value='']"))
		{
			basic.scrollIntoView("//input[@name='idFilter' and @value!='']");
			basic.clearUpdateText("//input[@name='idFilter' and @value!='']", "");
			basic.pressEnterUsingActions();
			basic.pause(1000);
			basic.waitForElementVisible("//table[@id='detailtestcase_tps']/tbody[count(tr)>1]");
		}
			
		while(true)
		{
			if(basic.knowIfAppears("//a[text()=\""+lastTestCaseName+"\"]"))
			{
				System.out.println("Test case appeared");
				isTestCaseAppeared=true;
				break;
			}
			
			tempStr = basic.getText("//table[@id='detailtestcase_tps']/tbody/tr/td[2]/a").trim();
			
			if(basic.knowIfAppears("//a[@title='Next' and contains(@class,'next') and not(contains(@class,'disabled'))]"))
			{
				basic.click("//a[@title='Next' and contains(@class,'next')]");
			}
			else if(basic.knowIfAppears("//span[@title='Next' and contains(@class,'next') and contains(@class,'disabled')]"))
			{
				isTestCaseAppeared=false;
				break;
			}
			
			for(int i=0; i<30; i++)
			{
				if(basic.getText("//table[@id='detailtestcase_tps']/tbody/tr/td[2]/a").trim().equalsIgnoreCase(tempStr))
					Thread.sleep(1000);
				else
					break;
			}			
		}
		
		if(! isTestCaseAppeared)
		{
			throw new Exception("Unable to search the following Test Case in User Story '"+userStory+"' :\n"+lastTestCaseName);
		}
		else
		{
			System.out.println("Success in searching test case name :" + lastTestCaseName);
		}
	}

}
