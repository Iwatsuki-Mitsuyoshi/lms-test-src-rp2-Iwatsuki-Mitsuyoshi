package jp.co.sss.lms.ct.f01_login1;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.time.Duration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
/** @author 岩月光義 */
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {
    @BeforeAll static void before() { createIncognitoDriver(); }
    @AfterAll static void after() { closeDriver(); }
    static void evidence(String path) throws Exception {
        Path target = Path.of(path); Files.createDirectories(target.getParent());
        Files.write(target, ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BYTES));
    }

    @Test void unregisteredUser() throws Exception {
        goTo("http://localhost:8080/lms");
        new WebDriverWait(webDriver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.id("loginId")));
        assertEquals("ログイン | LMS", webDriver.getTitle());
        assertAll(
            () -> assertTrue(webDriver.findElement(By.id("loginId")).isDisplayed()),
            () -> assertTrue(webDriver.findElement(By.id("password")).isDisplayed()),
            () -> assertTrue(webDriver.findElement(By.cssSelector("input[type=submit]")).isDisplayed()),
            () -> assertEquals("ログイン",webDriver.findElement(By.cssSelector("input[type=submit]")).getAttribute("value"))
        );
        webDriver.findElement(By.id("loginId")).sendKeys("NoUserIW01");
        webDriver.findElement(By.id("password")).sendKeys("NoUserIW01");
        assertEquals("NoUserIW01",webDriver.findElement(By.id("loginId")).getAttribute("value"));
        assertEquals("password",webDriver.findElement(By.id("password")).getAttribute("type"));
        evidence("evidence/Case02/00_input.png");
        webDriver.findElement(By.cssSelector("input[type=submit]")).click();
        WebElement error = new WebDriverWait(webDriver,Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".help-inline.error")));
        evidence("evidence/Case02/01_login_error.png");
        assertEquals("ログインに失敗しました。",error.getText().replaceFirst("^[*\s\u00a0]+", ""));
        assertEquals("ログイン | LMS",webDriver.getTitle());
        assertFalse(webDriver.getCurrentUrl().contains("/course/detail"));
    }
}
