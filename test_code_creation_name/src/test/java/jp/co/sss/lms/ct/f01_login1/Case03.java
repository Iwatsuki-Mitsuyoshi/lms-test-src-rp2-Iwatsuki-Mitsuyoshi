package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;

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
 * 結合テスト ログイン機能①
 * ケース03
 * @author 岩月光義
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース03 受講生 ログイン 正常系")
public class Case03 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// トップページへアクセスし、ログイン画面の表示を待つ。
		goTo("http://localhost:8080/lms");
		visibilityTimeout(By.id("loginId"), 10);
		getEvidence(new Object() {});

		// タイトル、入力欄、ログインボタンを確認する。
		assertEquals("ログイン | LMS", webDriver.getTitle());
		assertTrue(webDriver.findElement(By.id("loginId")).isDisplayed());
		assertTrue(webDriver.findElement(By.id("password")).isDisplayed());
		WebElement loginButton = webDriver.findElement(By.cssSelector("input[type=submit]"));
		assertTrue(loginButton.isDisplayed());
		assertEquals("ログイン", loginButton.getAttribute("value"));
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// 事前に初回ログインを完了した受講生の情報を入力する。
		webDriver.findElement(By.id("loginId")).sendKeys("StudentAA01");
		webDriver.findElement(By.id("password")).sendKeys("StudentAA01");
		webDriver.findElement(By.cssSelector("input[type=submit]")).click();

		// コース詳細画面への遷移を待ち、エビデンスを保存する。
		visibilityTimeout(By.cssSelector(".breadcrumb li.active"), 10);
		getEvidence(new Object() {});

		// ログイン後の遷移先、タイトル、パンくずリストを確認する。
		String path = URI.create(webDriver.getCurrentUrl()).getPath().split(";")[0];
		assertEquals("/lms/course/detail", path);
		assertEquals("コース詳細 | LMS", webDriver.getTitle());
		assertEquals("コース詳細",
				webDriver.findElement(By.cssSelector(".breadcrumb li.active")).getText());
		// 受講生に割り当てられたコース名が表示されることを確認する。
		assertTrue(webDriver.findElement(By.cssSelector("h2.di"))
				.getText().startsWith("DEMOコース"));
	}

}
