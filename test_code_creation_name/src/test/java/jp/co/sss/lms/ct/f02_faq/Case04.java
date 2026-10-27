package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.time.Duration;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト よくある質問機能
 * ケース04
 * @author 岩月光義
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

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
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// 上部メニューの「機能」を開き、「ヘルプ」へ遷移する。
		webDriver.findElement(By.linkText("機能")).click();
		visibilityTimeout(By.linkText("ヘルプ"), 10);
		webDriver.findElement(By.linkText("ヘルプ")).click();
		visibilityTimeout(By.linkText("よくある質問"), 10);
		getEvidence(new Object() {});

		// ヘルプ画面の遷移先、タイトル、見出しとFAQリンクを確認する。
		String path = URI.create(webDriver.getCurrentUrl()).getPath().split(";")[0];
		assertEquals("/lms/help", path);
		assertEquals("ヘルプ | LMS", webDriver.getTitle());
		assertEquals("ヘルプ", webDriver.findElement(By.tagName("h2")).getText());
		assertTrue(webDriver.findElement(By.linkText("よくある質問")).isDisplayed());
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// 遷移前のタブを記録し、FAQリンクを押下する。
		String helpWindow = webDriver.getWindowHandle();
		Set<String> beforeWindows = webDriver.getWindowHandles();
		webDriver.findElement(By.linkText("よくある質問")).click();

		// 別タブが開いたことを確認し、新しいタブへ切り替える。
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.numberOfWindowsToBe(beforeWindows.size() + 1));
		for (String handle : webDriver.getWindowHandles()) {
			if (!beforeWindows.contains(handle)) {
				webDriver.switchTo().window(handle);
				break;
			}
		}
		visibilityTimeout(By.id("form"), 10);
		getEvidence(new Object() {});

		// FAQ画面の遷移先、タイトル、見出し、検索欄を確認する。
		assertNotEquals(helpWindow, webDriver.getWindowHandle());
		String path = URI.create(webDriver.getCurrentUrl()).getPath().split(";")[0];
		assertEquals("/lms/faq", path);
		assertEquals("よくある質問 | LMS", webDriver.getTitle());
		assertEquals("よくある質問", webDriver.findElement(By.tagName("h2")).getText());
		assertTrue(webDriver.findElement(By.id("form")).isDisplayed());
		assertTrue(webDriver.findElement(By.xpath(
				"//legend[normalize-space()='カテゴリ検索']")).isDisplayed());

		// 元のヘルプ画面が残っていることも確認する。
		webDriver.switchTo().window(helpWindow);
		assertEquals("ヘルプ | LMS", webDriver.getTitle());
	}

}
