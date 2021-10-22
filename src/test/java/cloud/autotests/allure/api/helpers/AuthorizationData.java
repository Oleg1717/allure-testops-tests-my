package cloud.autotests.allure.api.helpers;

import cloud.autotests.allure.api.steps.UserApi;
import cloud.autotests.allure.config.ConfigHelper;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationData {

    private final String sessionToken;
    private final Map<String, String> sessionCookies = new HashMap<>();

    private static class InitAuthorizationData {
        private static final AuthorizationData authorizationData = new AuthorizationData();
    }

    private AuthorizationData() {
        sessionToken = new UserApi().getSessionToken();
        sessionCookies.put("XSRF-TOKEN", ConfigHelper.getXsrfToken());
        sessionCookies.put("ALLURE_TESTOPS_SESSION", sessionToken);
    }

    public static AuthorizationData authorizationData() {
        return InitAuthorizationData.authorizationData;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public Map<String, String> getSessionCookies() {
        return sessionCookies;
    }

}