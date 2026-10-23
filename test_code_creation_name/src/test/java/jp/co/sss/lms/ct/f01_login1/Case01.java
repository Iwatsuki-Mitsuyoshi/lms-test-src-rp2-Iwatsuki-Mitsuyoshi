package jp.co.sss.lms.ct.f01_login1;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.time.Duration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
/** @author 岩月光義 */
@DisplayName("ケース01 ログイン画面への遷移")
public class Case01 {
    @BeforeAll static void before() { createIncognitoDriver(); }
    @AfterAll static void after() { closeDriver(); }
    static void evidence(String path) throws Exception {
        Path target = Path.of(path); Files.createDirectories(target.getParent());
        Files.write(target, ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BYTES));
    }

    @Test void loginPage() throws Exception {
        goTo("http://localhost:8080/lms");
        new WebDriverWait(webDriver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.id("loginId")));
        evidence("evidence/Case01/01_login.png");
        assertAll(
            () -> assertEquals("ログイン | LMS", webDriver.getTitle()),
            () -> assertTrue(webDriver.findElement(By.id("loginId")).isDisplayed()),
            () -> assertTrue(webDriver.findElement(By.id("password")).isDisplayed()),
            () -> assertTrue(webDriver.findElement(By.cssSelector("input[type=submit]")).isDisplayed()),
            () -> assertTrue(webDriver.findElement(By.cssSelector("a[href*='/faq']")).isDisplayed()),
            () -> assertTrue(webDriver.findElement(By.xpath("//h3[normalize-space()='マニュアルはこちらからダウンロード']")).isDisplayed()),
            () -> assertEquals("password", webDriver.findElement(By.id("password")).getAttribute("type")),
            () -> assertEquals("ログイン", webDriver.findElement(By.cssSelector("input[type=submit]")).getAttribute("value")),
            () -> assertTrue(webDriver.findElement(By.linkText("パスワードを忘れた方はこちら")).isDisplayed()),
            () -> assertTrue(webDriver.findElement(By.cssSelector("a[href*='manual_stud_001.pdf']")).isDisplayed()),
            () -> assertEquals("よくあるご質問", webDriver.findElement(By.cssSelector("a[href*='/faq']")).getText(), "よくある質問リンクの表示文言")
        );
    }
}

