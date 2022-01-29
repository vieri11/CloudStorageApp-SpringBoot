package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void unauthorizedUser() {
		driver.get("http://localhost:" + this.port + "/home");

		// Check if we have been redirected to login page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());

		driver.get("http://localhost:" + this.port + "/signup");

		// Check if we have access to signup page
		Assertions.assertEquals("http://localhost:" + this.port + "/signup", driver.getCurrentUrl());
	}

	@Test
	public void logInLogOut() {
		// Create a test account
		doMockSignUp("qwerty","asdfg","LI","123");
		doLogIn("LI", "123");

		// Check home page accessible after login
		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		doLogOut();

		// check to see if unauthorized after logout
		unauthorizedUser();
	}

	@Test
	public void addNote() {
		// Create a test account
		doMockSignUp("URL","Test","AN","123");
		doLogIn("AN", "123");

		// add note helper adds a single note
		addNoteHelper();

		// go back to home and click Notes Tab
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// check home page still available
		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		// click Notes Tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		// check notes title is available in notes list
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-1")));

		// assert note-1 was created
		Assertions.assertTrue(driver.findElement(By.id("note-title-1")).getText().contains("note title text"));
	}

	// assumes you're successfully logged in and adds a note
	private void addNoteHelper()
	{
		// Visit home page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/home");

		// find and click Notes Tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		// click add new note button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-new-note")));
		WebElement addNewNote = driver.findElement(By.id("add-new-note"));
		addNewNote.click();

		// add note title
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("note title text");

		//add note description
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("note title description");

		// click save button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-button")));
		WebElement saveNote = driver.findElement(By.id("save-note-button"));
		saveNote.click();

		// confirm result was a success
		webDriverWait.until(ExpectedConditions.titleContains("Result"));
		Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));
	}

	@Test
	public void editNote() throws InterruptedException {
		// Create a test account
		doMockSignUp("URL","Test","EN","123");
		doLogIn("EN", "123");

		// add note helper adds a single note
		addNoteHelper();

		// go back to home and click Notes Tab
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// check home page still available
		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		// click Notes Tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		// check notes title is available in notes list
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-1")));

		// assert note-1 was created
		Assertions.assertTrue(driver.findElement(By.id("note-title-1")).getText().contains("note title text"));

		// click edit button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note-1")));
		WebElement editNote = driver.findElement(By.id("edit-note-1"));
		editNote.click();

		// add note title
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.clear();
		noteTitle.sendKeys("edit title text");

		//add note description
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.clear();
		noteDescription.sendKeys("edit title description");

		Thread.sleep(3000);

		// click save button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-button")));
		WebElement saveNote = driver.findElement(By.id("save-note-button"));
		saveNote.click();

		// confirm result was a success
		webDriverWait.until(ExpectedConditions.titleContains("Result"));
		Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

		// go back to home and click Notes Tab
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait = new WebDriverWait(driver, 2);

		// check home page still available
		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		// click Notes Tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		// check notes title is available in notes list
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-1")));

		Thread.sleep(3000);

		// assert note had edited title
		Assertions.assertTrue(driver.findElement(By.id("note-title-1")).getText().contains("edit title text"));

		// assert note had edited description
		Assertions.assertTrue(driver.findElement(By.id("note-description-1")).getText().contains("edit title description"));
	}

	@Test
	public void deleteNote() throws InterruptedException {
		doMockSignUp("qwerty","asdfgh","DN","123");
		doLogIn("DN", "123");

		addNoteHelper();

		Thread.sleep(2000);

		// go back to home and click Notes Tab
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		// click Notes Tab
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		Thread.sleep(1000);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note-1")));
		WebElement deleteNote = driver.findElement(By.id("delete-note-1"));
		deleteNote.click();

		Thread.sleep(1000);

		webDriverWait.until(ExpectedConditions.titleContains("Result"));
		Assertions.assertTrue(driver.findElement(By.id("success")).getText().contains("Success"));

		Thread.sleep(1000);

		// go back to home and click Notes Tab
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait = new WebDriverWait(driver, 2);

		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		Thread.sleep(2000);

		// note-1 should not be found
		Assertions.assertFalse(noteFound("note-title-1"));
	}

	private boolean noteFound(String locatorId) {
		try {
			driver.findElement(By.id(locatorId));
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}

	
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

		// Check home page accessible after login
		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
	}

	private void doLogOut()
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/home");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
		WebElement loginButton = driver.findElement(By.id("logout-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Login"));

		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}



}
