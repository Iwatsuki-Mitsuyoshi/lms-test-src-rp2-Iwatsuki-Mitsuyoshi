package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
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
 * ケース05
 * @author 岩月光義
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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

		// 元のヘルプ画面を確認した後、検索を行うFAQ画面へ戻る。
		String faqWindow = webDriver.getWindowHandle();
		webDriver.switchTo().window(helpWindow);
		assertEquals("ヘルプ | LMS", webDriver.getTitle());
		webDriver.switchTo().window(faqWindow);
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		// 質問・回答のどちらかに「研修」を含むデータを検索する。
		WebElement keywordInput = webDriver.findElement(By.id("form"));
		keywordInput.sendKeys("研修");
		webDriver.findElement(By.cssSelector("input[type=submit]")).click();
		visibilityTimeout(By.id("form"), 10);
		getEvidence(new Object() {});

		// キーワードが保持され、該当する2件だけが表示されることを確認する。
		assertEquals("よくある質問 | LMS", webDriver.getTitle());
		assertEquals("研修", webDriver.findElement(By.id("form")).getAttribute("value"));
		List<WebElement> results = webDriver.findElements(
				By.cssSelector(".sortabletable tbody dl"));
		assertEquals(2, results.size());

		Set<String> actualQuestions = new HashSet<>();
		for (WebElement result : results) {
			String question = result.findElement(By.cssSelector("dt span:last-child")).getText();
			// 回答は折りたたまれているため、表示状態に左右されない本文を取得する。
			String answer = result.findElement(By.cssSelector("dd span:last-child"))
					.getDomProperty("textContent");
			assertTrue(question.contains("研修") || answer.contains("研修"),
					"質問または回答に検索キーワードが含まれること");
			actualQuestions.add(question);
		}
		assertEquals(Set.of(
				"研修の申し込みはどのようにすれば良いですか？",
				"助成金書類の作成方法が分かりません"), actualQuestions);
	}
	
	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		// 検索したキーワードが残っている状態でクリアする。
		WebElement keywordInput = webDriver.findElement(By.id("form"));
		assertEquals("研修", keywordInput.getAttribute("value"));
		webDriver.findElement(By.cssSelector("input[value='クリア']")).click();
		getEvidence(new Object() {});

		// クリア後の入力欄が空文字であることを確認する。
		assertEquals("", keywordInput.getAttribute("value"));
	}

}
