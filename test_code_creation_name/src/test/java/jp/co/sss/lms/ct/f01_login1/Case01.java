package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト ログイン機能① ケース01
 * @author 岩月光義
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース01 ログイン画面への遷移")
public class Case01 {

	/** 前処理：シークレットモードでChromeを起動する。 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理：Chromeを終了する。 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// トップページへアクセスし、ログインID欄の表示を待つ。
		goTo("http://localhost:8080/lms");
		visibilityTimeout(By.id("loginId"), 10);
		getEvidence(new Object() {});

		// ログイン画面のタイトルと入力欄を確認する。
		assertEquals("ログイン | LMS", webDriver.getTitle());
		assertTrue(webDriver.findElement(By.id("loginId")).isDisplayed());
		WebElement password = webDriver.findElement(By.id("password"));
		assertTrue(password.isDisplayed());
		assertEquals("password", password.getAttribute("type"));

		// ログインボタンの表示とボタン名を確認する。
		WebElement loginButton = webDriver.findElement(By.cssSelector("input[type=submit]"));
		assertTrue(loginButton.isDisplayed());
		assertEquals("ログイン", loginButton.getAttribute("value"));

		// パスワード再設定、マニュアル、FAQの表示を確認する。
		assertTrue(webDriver.findElement(By.linkText("パスワードを忘れた方はこちら")).isDisplayed());
		assertTrue(webDriver.findElement(By.xpath(
				"//h3[normalize-space()='マニュアルはこちらからダウンロード']")).isDisplayed());
		assertTrue(webDriver.findElement(By.cssSelector("a[href*='manual_stud_001.pdf']")).isDisplayed());
		WebElement faqLink = webDriver.findElement(By.cssSelector("a[href*='/faq']"));
		assertTrue(faqLink.isDisplayed());
		assertEquals("よくあるご質問", faqLink.getText());
	}

}
