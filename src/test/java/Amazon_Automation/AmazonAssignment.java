package Amazon_Automation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AmazonAssignment {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebDriverManager.edgedriver().setup();
		WebDriver driver = new EdgeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			driver.get("https://www.amazon.in");

			WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
			searchBox.sendKeys("lg soundbar");
			WebElement searchButton = driver.findElement(By.id("nav-search-submit-button"));
			searchButton.click();

			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".s-main-slot")));

			List<Map<String, String>> products = new ArrayList<>();

			List<WebElement> productElements = driver.findElements(By.cssSelector(".s-main-slot .s-result-item"));

			for (WebElement productElement : productElements) {
				String productName = "";
				String productPrice = "0";

				try {

					WebElement nameElement = productElement.findElement(By.cssSelector("h2 span.a-text-normal"));
					productName = nameElement.getText();
				} catch (Exception e) {

					System.out.println("Product name not found for an item.");
				}

				try {

					List<WebElement> priceElements = productElement
							.findElements(By.cssSelector(".a-price .a-offscreen"));
					if (!priceElements.isEmpty()) {
						productPrice = priceElements.get(0).getText().replace("₹", "").replace(",", "").trim();
					}
				} catch (Exception e) {

					System.out.println("Product price not found for an item.");
				}

				if (!productName.isEmpty() && !productPrice.equals("0")) {
					products.add(Map.of("name", productName, "price", productPrice));
				}
			}

			products.sort(Comparator.comparingInt(p -> {
				try {
					return Integer.parseInt(p.get("price"));
				} catch (NumberFormatException e) {
					return Integer.MAX_VALUE; // Move invalid prices to the end
				}
			}));

			for (Map<String, String> product : products) {
				System.out.println(product.get("price") + " " + product.get("name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			driver.quit();
		}
	}

}
