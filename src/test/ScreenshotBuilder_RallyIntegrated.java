package test;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;
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
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.Select;


public class ScreenshotBuilder_RallyIntegrated implements NativeKeyListener
{
	static ArrayList<BufferedImage> allImages = new ArrayList<BufferedImage>();
	static ArrayList<String> allDescription = new ArrayList<String>();
	static ArrayList<String> rallyUpdateScenarios = new ArrayList<String>();
	Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	Rectangle screenRect = new Rectangle(dimension);
	BufferedImage capture;
	ByteArrayOutputStream baos;
	ByteArrayInputStream bais;
	InputStream is;
	String filePath="", wordDocumentAbsolutePath="", testCaseName="", testCaseNameInputFromUser="";
	XWPFDocument docx;
	static JFrame frame;
	static JPanel p1,p2,p3,p4,p5;
	static JLabel started1 = new JLabel("Screenshot Builder Running... Please press F9 to capture screenshots");
	static JLabel complete = new JLabel(".                 One Test Case Completed successfully!                 .");
	static JTextField testName = new JTextField(40);
	static JLabel l1 = new JLabel("Test Case Name (Optional)");
	static JLabel l2 = new JLabel("Test Step Comments (Optional)");
	static JTextArea testComments = new JTextArea("", 7,40);
	static JScrollPane scrollPane = new JScrollPane(testComments);  
	static JButton buttonOk = new JButton("Start");
	static JButton buttonClearMemory = new JButton("Clear Memory");
	static JButton buttonComplete = new JButton("Complete");
	static JButton buttonUpdateRally = new JButton("Update Rally");
	static JButton buttonExit = new JButton("Exit");
	static JLabel info = new JLabel("F9 = Add Screenshot  ;  F8 = Completed");
	
	static JLabel rallyCecIdLabel = new JLabel("       CEC ID (For Rally) : ");
	static JTextField rallyCecIdTextBox = new JTextField(System.getProperty("user.name")+"@cisco.com",31);
	static JLabel rallyPasswordLabel = new JLabel("Password (For Rally) : ");
	//static JPasswordField rallyPasswordTextBox = new JPasswordField(31);
	static JPasswordField rallyPasswordTextBox;
	static JLabel rallyUserStoryLabel = new JLabel("User Story (For Rally) : ");
	//static JTextField rallyUserStoryTextBox = new JTextField("",31);
	static JTextField rallyUserStoryTextBox;
	static String testFolderTimeStamp = "";
	
	static Properties prop;
	
	
	static String lastScreenshotWordPath="", lastTestCaseName="";
	static String rallyCecId="", rallyCecPassword="", rallyUserStory="", rallyBuild="", rallyTestCaseStatus="", rallyNotes="";
	
	public static void main(String[] args) throws Exception
	{		
		ScreenshotBuilder_RallyIntegrated s = new ScreenshotBuilder_RallyIntegrated();
		s.loadPropertiesFile();
		s.launchUI();
	}
	
	public void loadPropertiesFile() throws Exception
	{
		try
		{
			prop = new Properties();
			InputStream input = new FileInputStream("inputDefaults.properties");
			prop.load(input);
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public void launchUI() throws Exception
	{
		try
		{
			frame.dispose();
		}
		catch(Exception e){}
		System.out.println("Launched UI 1");
		frame = new JFrame("Screenshots Builder ~ Amit_Bhaik");
		frame.setLayout(new GridLayout(5,1));
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();		
		p5 = new JPanel();
		
		rallyPasswordTextBox = new JPasswordField(prop.getProperty("cecPassword"), 31);
		rallyUserStoryTextBox = new JTextField(prop.getProperty("rallyUserStory"),31);
			
		p1.add(rallyCecIdLabel);
		p1.add(rallyCecIdTextBox);
		p1.add(rallyPasswordLabel);
		p1.add(rallyPasswordTextBox);
		p1.add(rallyUserStoryLabel);
		p1.add(rallyUserStoryTextBox);
		
		p2.add(new JLabel());
		p2.add(l1);
		
		try
		{
			testName.setText("");
		}
		catch(Exception e){}
		
		p2.add(testName);		
		
		p3.add(l2);

		p3.add(scrollPane);
				
		buttonOk.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e) { 
			   System.out.println("OK Pressed");
			  			   
			   testCaseNameInputFromUser = testName.getText();		
			   
			   try
			   {
				   p5.remove(complete);
			   }
			   catch(Exception e1){}
			   
			   p5.add(started1);			  
			   frame.add(p5);
			   frame.setVisible(false);	   
			   frame.setVisible(true);
			   
			   try 
			   {
				   startOver();
			   }
			   catch (Exception e1)
			   {
				   e1.printStackTrace();
			   }
			  } 
			} );
		
		
		buttonClearMemory.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
			   System.out.println("Clear Memory Pressed");
			   try
			   {
				   clearAllCacheFromMemory();
				   JOptionPane.showMessageDialog(null, "Successfully Cleared Memory!", "Screenshots Builder Clear Memory Success ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
			   }
			   catch(Exception e1){
				   JOptionPane.showMessageDialog(null, "Error while clearing Memory!", "Screenshots Builder Clear Memory Error ~ Amit_Bhaik", JOptionPane.ERROR_MESSAGE);
			   }
			  }
			} );
		
		buttonComplete.addActionListener(new ActionListener(){
			  public void actionPerformed(ActionEvent e) { 
			   System.out.println("Complete Pressed");
			   
			   testCaseNameInputFromUser = testName.getText();	
			  
			   //JOptionPane.showMessageDialog(null, testCaseNameInputFromUser, "Screenshots Builder Clear Memory Success ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
			   
			   frame.setVisible(false);
			   frame.setVisible(true);
			   
			   try 
			   {
				   uponCompletion();
			   }
			   catch (Exception e1) 
			   {
				   e1.printStackTrace();
				   try
				   {
					   clearAllCacheFromMemory();
				   }
				   catch(Exception e2){}
			   }
			  } 
			} );
		
		
		buttonUpdateRally.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
			   System.out.println("Update Rally pressed");
			   try
			   {
				   try
				   {
					   clearAllCacheFromMemory();
				   }
				   catch(Exception e2){}
				   
				   rallyCecId = rallyCecIdTextBox.getText();
				   rallyCecPassword = rallyPasswordTextBox.getText();
				   rallyUserStory = rallyUserStoryTextBox.getText();
				   
				   if(rallyCecId.split("@")[0].equalsIgnoreCase(""))
				   {
					   JOptionPane.showMessageDialog(null, "Please enter your CEC ID to use this feature", "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				   }
				   
				   else if(rallyCecPassword.equalsIgnoreCase(""))
				   {
					   JOptionPane.showMessageDialog(null, "Please enter your CEC Password to use this feature", "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				   }
				   
				   else if(rallyUserStory.equalsIgnoreCase(""))
				   {
					   JOptionPane.showMessageDialog(null, "Please enter User Story to use this feature", "Rally Updator ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				   }
				   
				   if(! rallyCecId.split("@")[0].equalsIgnoreCase(""))
				   {
					   if(!rallyCecPassword.equalsIgnoreCase(""))
					   {
						   if(!rallyUserStory.equalsIgnoreCase(""))
						   {
							   Thread t1 = new Thread(new RallyUpdator());
							   t1.start();
						   }
					   }
				   }
			   }
			   catch(Exception e1){
				   e1.printStackTrace();
			   }
			  }
			} );
		
		
		buttonExit.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
			   System.out.println("Exit Pressed");
			   try
			   {
				   clearAllCacheFromMemory();
			   }
			   catch(Exception e2){}
			   frame.setVisible(false);
			   frame.dispose();
			   System.exit(0);
			  }
			} );
		
		p4.setLayout(new GridBagLayout());
		p4.add(buttonOk);
		p4.add(buttonClearMemory);
		p4.add(buttonComplete);
		p4.add(buttonUpdateRally);
		p4.add(buttonExit);			
		
		p5.add(info);
		
		frame.add(p1);
		frame.add(p2);
		frame.add(p3);		
		frame.add(p4);
		frame.add(p5);
		
		frame.setBounds(600,200,550,600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buttonOk.doClick();
	}
	
	
	
	public void startOver() throws Exception
	{	
		generateScreenshotFolder();		
		System.out.println("1");
		if(! GlobalScreen.isNativeHookRegistered())
		{
			registerAndStartNativeHook();
		}
		System.out.println("2");
	}	
	
	public void generateScreenshotFolder() throws Exception
	{
		testFolderTimeStamp = new SimpleDateFormat("MMM_dd_yyyy").format(new Date());
		filePath = System.getProperty("user.dir")+"\\TestCaseScreenshots\\"+testFolderTimeStamp+"\\";
		new File(filePath).mkdirs();
	}
	
	public void initializeWordDocument() throws Exception
	{
		docx = new XWPFDocument();
		System.out.println("Initialized Word Document");
	}
	
	public void registerAndStartNativeHook() throws Exception
	{
		try
		{
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) 
		{
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		GlobalScreen.addNativeKeyListener(new ScreenshotBuilder_RallyIntegrated());
	}
	
	public void putAllImagesAndDescriptionInWord() throws Exception
	{
		int i=0;
		docx = new XWPFDocument();
		System.out.println("New document, images : " + allImages.size());
		for(BufferedImage b : allImages)
		{
			try
			{
				docx.createParagraph().createRun().setText(allDescription.get(i++));
			}
			catch(Exception e){e.printStackTrace();}
			setInputStreamForBufferedImage(b);
			try
			{
				docx.createParagraph().createRun().addPicture(is, Document.PICTURE_TYPE_JPEG, "my pic", Units.toEMU(dimension.getWidth()/3.5), Units.toEMU(dimension.getHeight()/3.5));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void setInputStreamForBufferedImage(BufferedImage b) throws Exception
	{
		baos = new ByteArrayOutputStream();
		ImageIO.write(b, "jpg", baos);	
		byte[] imageInByte = baos.toByteArray();
		baos.flush();			
		baos.close();			
		bais = new ByteArrayInputStream(imageInByte);
		is = bais;
	}
	
	public void initializeWordDocName() throws Exception
	{
		String testCaseTimeStamp = new SimpleDateFormat("MMMdd_HH_mm_ss").format(new Date());
		testCaseName = "TC_"+testCaseTimeStamp;
		try
		{			
			if(testCaseNameInputFromUser!=null)
			{				
				if(!testCaseNameInputFromUser.equalsIgnoreCase(""))
				{
					testCaseName = "TC_"+testCaseTimeStamp+"_"+testCaseNameInputFromUser.replace(" ", "_");					
				}
				else
				{
					System.out.println("_______________________________________________________________________________Else 1");
					testCaseName = "TC_"+testCaseTimeStamp;					
				}
			}
			else
			{
				System.out.println("_______________________________________________________________________________Else 2");
				testCaseName = "TC_"+testCaseTimeStamp;				
			}
		}
		catch(Exception e)
		{
			System.out.println("_______________________________________________________________________________In Catch");
			e.printStackTrace();
			testCaseName = "TC_"+testCaseTimeStamp;			
		}
		filePath = System.getProperty("user.dir")+"\\TestCaseScreenshots\\"+testFolderTimeStamp+"\\";		
		wordDocumentAbsolutePath = filePath+ testCaseName+".docx";
		
		//Logic for a valid location / file name length
		if(wordDocumentAbsolutePath.length()>250)
		{
			int diff = wordDocumentAbsolutePath.length() - 250;
			int original = testCaseName.length();
			testCaseName = testCaseName.substring(0,original-diff);
			wordDocumentAbsolutePath = filePath+ testCaseName+".docx";
		}
	}
	
	public void writeWordDocument() throws Exception
	{
		initializeWordDocName();
		FileOutputStream fos = new FileOutputStream(new File(wordDocumentAbsolutePath));
		docx.write(fos);
		try
		{
			lastScreenshotWordPath = wordDocumentAbsolutePath;
			lastTestCaseName = testCaseNameInputFromUser;
		}
		catch(Exception e){}
		fos.close();
	}
	
	public void addImageInArrayList() throws Exception
	{
		System.out.println("Added image in arraylist");
		allImages.add(getScreenCapture());
	}
	
	public void addDescriptionInArrayList(String desc) throws Exception
	{
		System.out.println("Added description in arraylist");
		allDescription.add(desc);
	}
	
	public BufferedImage getScreenCapture() throws Exception
	{
		return new Robot().createScreenCapture(screenRect);
	}
	
	public void clearAllCacheFromMemory() throws Exception
	{
		//allImages.removeAll(allImages);
		allImages = new ArrayList<BufferedImage>();
		allImages.clear();
		allDescription = new ArrayList<String>();
		allDescription.clear();
	}
	
	public void nativeKeyPressed(NativeKeyEvent e) 
	{
		System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
		if (e.getKeyCode() == NativeKeyEvent.VC_F9) 
		{
			System.out.println("This Key is Pressed : " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			try 
			{	
				addDescriptionInArrayList(getDescription());
				addImageInArrayList();
			}
			catch (Exception e1) 
			{
				e1.printStackTrace();
			}
		} else if (e.getKeyCode() == NativeKeyEvent.VC_F8)
		{			 
		   System.out.println("F8 Pressed");
		   
		   testCaseNameInputFromUser = testName.getText();	
		   
		   System.out.println("=======================================================Test case name : " + testCaseNameInputFromUser);
		   
		   frame.setVisible(false);
		   frame.dispose();	   
		   frame.setVisible(true);			   
		   try 
		   {
			   uponCompletion();
		   }
		   catch (Exception e1) 
		   {
			   e1.printStackTrace();
		   }
		}		
	}
	
	public String getDescription() throws Exception
	{
		String desc = testComments.getText();
		try
		{			
			testComments.setText("");
		}
		catch(Exception e1){}
		if(desc==null)
		{
			desc="";
		}
		return desc;
	}
	
	public void nativeKeyReleased(NativeKeyEvent e){}
	
	public void nativeKeyTyped(NativeKeyEvent e){}	
	
	public void uponCompletion() throws Exception
	{
		try
		{
			try
			{
				frame.setVisible(false);
				frame.setVisible(true);
			}
			catch(Exception e){}
			
			if(allImages.size()!=0)
			{
				//initializeWordDocName();s
				testName.setText("");
				frame.setVisible(false);
				frame.setVisible(true);
				System.out.println("Pressed F8 so saving all the '"+allImages.size()+"' screenshots in word format");
				putAllImagesAndDescriptionInWord();
				System.out.println("4");
				writeWordDocument();		
				System.out.println("Success writing word document : " + wordDocumentAbsolutePath);
				JOptionPane.showMessageDialog(null, "Successfully generated Word document having '"+allImages.size()+"' Screenshots!\n\n"+wordDocumentAbsolutePath + "\n\nScreenshots Builder still running, please continue execution.", "Screenshots Builder Success ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
				System.out.println("Before : "+allImages.size());
				clearAllCacheFromMemory();
				System.out.println("After : "+allImages.size());
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Oops! Seems there are no screenshots in memory!\n\nScreenshots Builder still running, please continue execution.", "Screenshots Builder Success ~ Amit_Bhaik", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error generating Word document having '"+allImages.size()+"' Screenshots.\n\n"+wordDocumentAbsolutePath + "\n\nError Message : \n\n" + e1.getMessage(), "Screenshots Builder Error ~ Amit_Bhaik", JOptionPane.ERROR_MESSAGE);
		}
	}
}