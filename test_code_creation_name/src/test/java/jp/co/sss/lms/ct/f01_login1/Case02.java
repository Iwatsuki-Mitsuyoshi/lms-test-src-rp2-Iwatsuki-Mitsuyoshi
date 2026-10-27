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
 * 結合テスト ログイン機能① ケース02
 * @author 岩月光義
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

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
		// トップページへアクセスし、ログイン画面の表示を確認する。
		goTo("http://localhost:8080/lms");
		visibilityTimeout(By.id("loginId"), 10);
		getEvidence(new Object() {});

		assertEquals("ログイン | LMS", webDriver.getTitle());
		assertTrue(webDriver.findElement(By.id("loginId")).isDisplayed());
		assertTrue(webDriver.findElement(By.id("password")).isDisplayed());
		WebElement loginButton = webDriver.findElement(By.cssSelector("input[type=submit]"));
		assertTrue(loginButton.isDisplayed());
		assertEquals("ログイン", loginButton.getAttribute("value"));
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		// 事前にDBに未登録であることを確認したログイン情報を入力する。
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		WebElement password = webDriver.findElement(By.id("password"));
		loginId.clear();
		password.clear();
		loginId.sendKeys("NoUserIW01");
		password.sendKeys("NoUserIW01");

		assertEquals("NoUserIW01", loginId.getAttribute("value"));
		assertEquals("password", password.getAttribute("type"));
		getEvidence(new Object() {}, "input");

		// 認証失敗メッセージの表示を待ち、画面のエビデンスを保存する。
		webDriver.findElement(By.cssSelector("input[type=submit]")).click();
		visibilityTimeout(By.cssSelector(".help-inline.error"), 10);
		getEvidence(new Object() {});

		// 装飾の「*」と先頭空白を除き、共通設計のメッセージと比較する。
		WebElement error = webDriver.findElement(By.cssSelector(".help-inline.error"));
		String errorMessage = error.getText().replaceFirst("^[*\s\u00a0]+", "");
		assertEquals("ログインに失敗しました。", errorMessage);
		assertEquals("ログイン | LMS", webDriver.getTitle());
		assertFalse(webDriver.getCurrentUrl().contains("/course/detail"));
	}

}
