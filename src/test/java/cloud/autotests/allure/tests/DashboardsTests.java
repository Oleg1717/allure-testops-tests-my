package cloud.autotests.allure.tests;

import cloud.autotests.allure.api.steps.DashboardsApi;
import cloud.autotests.allure.config.ConfigHelper;
import cloud.autotests.allure.ui.data.ErrorMessages;
import cloud.autotests.allure.ui.data.TestData;
import cloud.autotests.allure.ui.data.dashboards.DashboardActionItem;
import cloud.autotests.allure.ui.data.dashboards.FormGroupByItem;
import cloud.autotests.allure.ui.data.dashboards.FormLaunchAnalyticMetricItem;
import cloud.autotests.allure.ui.data.dashboards.FormTopTestCasesMetricItem;
import cloud.autotests.allure.ui.data.dashboards.FormTreeItem;
import cloud.autotests.allure.ui.data.dashboards.FormTypeItem;
import cloud.autotests.allure.ui.data.dashboards.WidgetActionItem;
import cloud.autotests.allure.ui.helpers.WithLogin;
import cloud.autotests.allure.ui.pages.DashboardsPage;
import cloud.autotests.allure.ui.components.forms.DashboardEditForm;
import cloud.autotests.allure.ui.components.forms.DashboardWidgetEditForm;
import cloud.autotests.allure.ui.components.forms.DeleteForm;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static io.qameta.allure.Allure.parameter;

@Owner("Oleg1717")
@Feature("Dashboards tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DashboardsTests extends TestBase {

    DashboardsPage dashboardsPage = new DashboardsPage();
    DashboardEditForm dashboardEditForm = new DashboardEditForm();
    DashboardWidgetEditForm dashboardWidgetEditForm = new DashboardWidgetEditForm();
    DeleteForm deleteForm = new DeleteForm();
    DashboardsApi dashboardsApi = new DashboardsApi();

    String dashboardForTestsUrl;

    @BeforeAll
    void addDashboardForTests() {
        dashboardForTestsUrl = TestData.DASHBOARDS_URL + dashboardsApi.addDashboard("ForWidgetsTests");
    }

    @AfterAll
    void deleteDashboardsAfterTests() {
        dashboardsApi.deleteAllDashboards(ConfigHelper.getProjectId());
    }

    //region Add new dashboards tests
    @WithLogin
    @Test
    @Story("Add new dashboards tests")
    @DisplayName("Add new dashboard")
    void addDashboard() {
        String dashboardName = "NewDashboard";
        dashboardsPage.openDashboardPage(TestData.DASHBOARDS_URL)
                .clickNewDashboardButton();
        dashboardEditForm.setNameInput(dashboardName)
                .clickSubmitButton();
        dashboardsPage.checkThatDashboardExist(dashboardName);
    }

    @WithLogin
    @Test
    @Story("Add new dashboards tests")
    @DisplayName("Add dashboard with blank name")
    void addDashboardWithBlankName() {
        String dashboardName = "";
        dashboardsPage.openDashboardPage(TestData.DASHBOARDS_URL)
                .clickNewDashboardButton();
        dashboardEditForm.setNameInput(dashboardName)
                .clickSubmitButton()
                .checkThatNameErrorMessageIs(ErrorMessages.NAME_IS_REQUIRED);
    }

    @WithLogin
    @Test
    @Story("Add new dashboards tests")
    @DisplayName("Add dashboard with only space characters in name")
    void addDashboardWithSpacesName() {
        String dashboardName = "   ";
        dashboardsPage.openDashboardPage(TestData.DASHBOARDS_URL)
                .clickNewDashboardButton();
        dashboardEditForm.setNameInput(dashboardName)
                .clickSubmitButton()
                .checkThatNameErrorMessageIs(ErrorMessages.NAME_SHOULD_NOT_BE_BLANK);
    }

    @WithLogin
    @Test
    @Story("Add new dashboards tests")
    @DisplayName("Cancel adding new dashboard using cancel button")
    void cancelAddingDashboardByCancelButton() {
        String dashboardName = "CancelButton";
        dashboardsPage.openDashboardPage(TestData.DASHBOARDS_URL)
                .clickNewDashboardButton();
        dashboardEditForm.setNameInput(dashboardName)
                .clickCancelButton();
        dashboardsPage.confirmAlert()
                .checkThatDashboardNotExist(dashboardName);
    }

    @WithLogin
    @Test
    @Story("Add new dashboards tests")
    @DisplayName("Cancel adding new dashboard using close button")
    void cancelAddingDashboardByCloseButton() {
        String dashboardName = "CloseButton";
        dashboardsPage.openDashboardPage(TestData.DASHBOARDS_URL)
                .clickNewDashboardButton();
        dashboardEditForm.setNameInput(dashboardName)
                .clickCloseButton();
        dashboardsPage.confirmAlert()
                .checkThatDashboardNotExist(dashboardName);
    }

    @WithLogin
    @Test
    @Story("Add new dashboards tests")
    @DisplayName("Cancel adding new dashboard with blank name using cancel button")
    void cancelAddingDashboardWithBlankNameByCancelButton() {
        String dashboardName = "";
        dashboardsPage.openDashboardPage(TestData.DASHBOARDS_URL)
                .clickNewDashboardButton();
        dashboardEditForm.setNameInput(dashboardName)
                .clickCancelButton()
                .checkThatFormIsClosed();
    }

    @WithLogin
    @Test
    @Story("Add new dashboards tests")
    @DisplayName("Cancel adding new dashboard with blank name using close button")
    void cancelAddingDashboardWithBlankNameByCloseButton() {
        String dashboardName = "";
        dashboardsPage.openDashboardPage(TestData.DASHBOARDS_URL)
                .clickNewDashboardButton();
        dashboardEditForm.setNameInput(dashboardName)
                .clickCloseButton()
                .checkThatFormIsClosed();
    }
    //endregion

    //region Various dashboard tests
    @WithLogin
    @Test
    @Story("Various dashboard tests")
    @DisplayName("Switch dashboard full screen mode on/off")
    void dashboardFullScreenMode() {
        String dashboardName = "FullScreen";
        String dashboardUrl = TestData.DASHBOARDS_URL + dashboardsApi.addDashboard(dashboardName);
        dashboardsPage.openDashboardPage(dashboardUrl)
                .selectDashboardAction(DashboardActionItem.FULLSCREEN_MODE)
                .checkFullScreenModeIsOn()
                .exitFullScreenMode()
                .checkFullScreenModeIsOff();
    }

    @WithLogin
    @Test
    @Story("Various dashboard tests")
    @DisplayName("Edit dashboard name")
    void editDashboardName() {
        String dashboardName = "Edit";
        String newDashboardName = "EditNew";
        String dashboardUrl = TestData.DASHBOARDS_URL + dashboardsApi.addDashboard(dashboardName);
        dashboardsPage.openDashboardPage(dashboardUrl)
                .selectDashboardAction(DashboardActionItem.EDIT_DASHBOARD);
        dashboardEditForm.setNameInput(newDashboardName)
                .clickSubmitButton();
        dashboardsPage.checkThatDashboardExist(newDashboardName);
    }

    @WithLogin
    @Test
    @Story("Various dashboard tests")
    @DisplayName("Delete dashboard")
    void deleteDashboard() {
        String dashboardName = "Delete";
        String dashboardUrl = TestData.DASHBOARDS_URL + dashboardsApi.addDashboard(dashboardName);
        dashboardsPage.openDashboardPage(dashboardUrl)
                .selectDashboardAction(DashboardActionItem.DELETE_DASHBOARD);
        deleteForm.clickDeleteButton();
        dashboardsPage.checkThatDashboardNotExist(dashboardName);
    }
    //endregion

    //region Standard (Overview) dashboard widgets tests
    @WithLogin
    @Test
    @Story("Standard (Overview) dashboard widgets tests")
    @DisplayName("Standard dashboard should contains 5 widgets")
    void defaultDashboardShouldContains5Widgets() {
        dashboardsPage.openDashboardPage(TestData.DASHBOARDS_URL)
                .checkThatWidgetsDisplayed();
    }
    //endregion

    //region Custom dashboard widgets tests
    @WithLogin
    @Test
    @Story("Various dashboard widgets tests")
    @DisplayName("Add widget using 'Add widget' button")
    void addWidgetByAddWidgetButton() {
        String dashboardName = "ByAddWidgetButton";
        String dashboardUrl = TestData.DASHBOARDS_URL + dashboardsApi.addDashboard(dashboardName);
        dashboardsPage.openDashboardPage(dashboardUrl)
                .addWidgetButtonClick();
        dashboardWidgetEditForm.setNameInput(dashboardName)
                .selectTypeItem(FormTypeItem.LAUNCHES)
                .clickSubmitButton();
        dashboardsPage.checkThatWidgetExist(dashboardName);
    }

    @WithLogin
    @Test
    @Story("Various dashboard widgets tests")
    @DisplayName("Edit widget")
    void editWidget() {
        String widgetName = "EditWidget";
        String newName = widgetName + "New";
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.LAUNCHES)
                .clickSubmitButton();
        dashboardsPage.closeNotification()
                .selectWidgetAction(widgetName, WidgetActionItem.EDIT);
        dashboardWidgetEditForm.setNameInput(newName)
                .selectTypeItem(FormTypeItem.LAUNCH_STATISTIC_TREND)
                .clickSubmitButton();
        dashboardsPage.checkThatWidgetExist(newName)
                .checkWidgetHaveTrendChartGraph(newName);
    }

    @WithLogin
    @Test
    @Story("Various dashboard widgets tests")
    @DisplayName("Clone widget")
    void cloneWidget() {
        String widgetName = "CloneWidget";
        String newName = widgetName + "New";
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.LAUNCHES)
                .clickSubmitButton();
        dashboardsPage.closeNotification()
                .selectWidgetAction(widgetName, WidgetActionItem.CLONE);
        dashboardWidgetEditForm.setNameInput(newName)
                .selectTypeItem(FormTypeItem.LAUNCHES)
                .clickSubmitButton();
        dashboardsPage.checkThatWidgetExist(newName);
    }

    @WithLogin
    @Test
    @Story("Various dashboard widgets tests")
    @DisplayName("Delete widget")
    void deleteWidget() {
        String widgetName = "DeleteWidget";
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.LAUNCHES)
                .clickSubmitButton();
        dashboardsPage.closeNotification()
                .selectWidgetAction(widgetName, WidgetActionItem.DELETE)
                .checkThatWidgetNotExist(widgetName);
    }
    //endregion

    //region Add some types of widgets
    @WithLogin
    @Test
    @Story("Add some types of dashboard widgets")
    @DisplayName("Add widget with type 'Markdown'")
    void addMarkdownTypeWidget() {
        String widgetName = FormTypeItem.MARKDOWN.toString();
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.MARKDOWN)
                .fillContentWriteTextArea(widgetName)
                .clickSubmitButton();
        dashboardsPage.checkWidgetHaveMarkdownArticles(widgetName);
    }

    @WithLogin
    @Test
    @Story("Add some types of dashboard widgets")
    @DisplayName("Add widget with type 'Launch statistic Trend'")
    void addLaunchStatisticTrendTypeWidget() {
        String widgetName = FormTypeItem.LAUNCH_STATISTIC_TREND.toString();
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.LAUNCH_STATISTIC_TREND)
                .clickSubmitButton();
        dashboardsPage.checkWidgetHaveTrendChartGraph(widgetName);
    }

    @WithLogin
    @Test
    @Story("Add some types of dashboard widgets")
    @DisplayName("Add widget with type 'Automation Trend'")
    void addAutomationTrendTypeWidget() {
        String widgetName = FormTypeItem.AUTOMATION_TREND.toString();
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.AUTOMATION_TREND)
                .clickSubmitButton();
        dashboardsPage.checkWidgetHaveTrendChartGraph(widgetName);
    }

    @WithLogin
    @Test
    @Story("Add some types of dashboard widgets")
    @DisplayName("Add widget with type 'Launches'")
    void addLaunchesTypeWidget() {
        String widgetName = FormTypeItem.LAUNCHES.toString();
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.LAUNCHES)
                .clickSubmitButton();
        dashboardsPage.checkWidgetHaveLaunchRowList(widgetName);
    }

    @WithLogin
    @Story("Add dashboard widgets with type 'Launch Analytics'")
    @ParameterizedTest(name = "Add widget with: 'Metric' item = {0}")
    @EnumSource(value = FormLaunchAnalyticMetricItem.class)
    void addLaunchAnalyticsWidgets(FormLaunchAnalyticMetricItem item) {
        parameter("'Metric' item", item.toString());
        String widgetName = item.toString();
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.LAUNCH_ANALYTICS)
                .selectLaunchAnalyticMetricItem(item)
                .clickSubmitButton();
        dashboardsPage.checkWidgetHaveTrendChartGraph(widgetName);
    }

    @WithLogin
    @Story("Add dashboard widgets with type 'Test Case Pie Chart'")
    @ParameterizedTest(name = "Add widget with: 'Group by' item = {0}")
    @EnumSource(value = FormGroupByItem.class)
    void addTestCasePieChartWidgets(FormGroupByItem item) {
        parameter("'Group by' item", item.toString());
        String widgetName = item.toString();
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.TEST_CASE_PIE_CHART)
                .selectGroupByItem(item)
                .clickSubmitButton();
        dashboardsPage.checkWidgetHavePieChartGraph(widgetName);
    }

    @WithLogin
    @Story("Add dashboard widgets with type 'Top Test Cases'")
    @ParameterizedTest(name = "Add widget with: 'Metric' item = {0}")
    @EnumSource(value = FormTopTestCasesMetricItem.class)
    void addTopTestCasesWidgets(FormTopTestCasesMetricItem item) {
        parameter("'Metric' item", item.toString());
        String widgetName = item.toString();
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.TOP_TEST_CASES)
                .selectTopTestCasesMetricItem(item)
                .clickSubmitButton();
        dashboardsPage.checkWidgetHaveTestCaseRowList(widgetName);
    }

    @WithLogin
    @Story("Add dashboard widgets with type 'Test Case Tree Map Chart'")
    @ParameterizedTest(name = "Add widget with: 'Tree' item = {0}")
    @EnumSource(value = FormTreeItem.class)
    void addTestCaseTreeMapChartWidgets(FormTreeItem item) {
        parameter("'Tree' item", item.toString());
        String widgetName = item.toString();
        dashboardsPage.openDashboardPage(dashboardForTestsUrl)
                .selectDashboardAction(DashboardActionItem.ADD_WIDGET);
        dashboardWidgetEditForm.setNameInput(widgetName)
                .selectTypeItem(FormTypeItem.TEST_CASE_TREE_MAP_CHART)
                .selectTreeItem(item)
                .clickSubmitButton();
        dashboardsPage.checkWidgetHaveTreeViewChartGraph(widgetName);
    }
    //endregion
}