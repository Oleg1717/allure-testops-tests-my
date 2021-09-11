package cloud.autotests.tests;

import cloud.autotests.config.ConfigHelper;
import io.qameta.allure.Epic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cloud.autotests.api.AuthorizationData.authorizationData;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

@Epic("Login tests")
public class LoginTests extends TestBase {

    @Test
    @DisplayName("Successful login as testuser")
    void loginTest() {
        step("Open login page", () ->
                open(""));

        step("Fill login form", () -> {
            $(byName("username")).setValue(ConfigHelper.getUserLogin());
            $(byName("password")).setValue(ConfigHelper.getUserPassword())
                    .pressEnter();
        });

        step("Verify successful authorization", () ->
                $("img.Avatar__img").shouldHave(
                        attribute("alt", ConfigHelper.getUserLogin())));
    }

    @Test
    @DisplayName("Successful login with localStorage (API + UI)")
    void loginWithCookieTest() {
        step("Set auth token to browser localstorage", () -> {
            step("Open minimal content, because localstorage can be set when site is opened", () ->
                    open("/favicon.ico"));

            step("Set auth token to to browser localstorage", () ->
                    localStorage().setItem("AS_AUTH_2", authorizationData().toString()));
        });

        step("Open main page", () ->
                open(""));

        step("Verify successful authorization", () ->
                $("img.Avatar__img").shouldHave(
                        attribute("alt", ConfigHelper.getUserLogin())));
    }
}
