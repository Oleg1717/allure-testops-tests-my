package cloud.autotests.allure.tests.api;

import cloud.autotests.allure.api.data.JobsErrorMessage;
import cloud.autotests.allure.api.models.ResponseErrorBody;
import cloud.autotests.allure.api.models.jobs.Job;
import cloud.autotests.allure.api.steps.JobsApi;
import cloud.autotests.allure.api.testdata.JobData;
import cloud.autotests.allure.ui.helpers.allure.Layer;
import io.qameta.allure.AllureId;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Owner("OlegV")
@Feature("Jobs")
@Layer("api")
@Tag("jobs")
public class JobsTests {

    JobsApi jobsApi = new JobsApi();

    @Test
    @AllureId("5605")
    @Story("Add a job")
    @DisplayName("Add a job with max valid data")
    public void addJobWithFullValidData() {
        //given
        Job jobRequestData = JobData.maxJobData;
        //when
        Response response = jobsApi.addJob(jobRequestData);
        Job jobResponseData = response.as(Job.class);
        //then
        jobsApi.checkStatusCode(response.statusCode(), 200);
        jobsApi.checkResponseBody(jobRequestData, jobResponseData);
        //and
        jobsApi.deleteJob(jobResponseData.getId());
    }

    @Test
    @AllureId("5602")
    @Story("Add a job")
    @DisplayName("Add a job with min valid data")
    public void addJobWithMinValidData() {
        //given
        Job jobRequestData = JobData.minJobData;
        //when
        Response response = jobsApi.addJob(jobRequestData);
        Job jobResponseData = response.as(Job.class);
        //then
        jobsApi.checkStatusCode(response.statusCode(), 200);
        jobsApi.checkResponseBody(jobRequestData, jobResponseData);

        //and
        jobsApi.deleteJob(jobResponseData.getId());
    }

    @Test
    @Story("Add a job")
    @DisplayName("Add a copy of existing job")
    public void addCopyOfExistingJob() {
        //given
        Job jobRequestData = JobData.maxJobData;
        Response firstJobResponse = jobsApi.addJob(jobRequestData);
        //when
        Response secondJobResponse = jobsApi.addJob(jobRequestData);
        ResponseErrorBody errorBody = secondJobResponse.as(ResponseErrorBody.class);
        //then
        jobsApi.checkStatusCode(secondJobResponse.statusCode(), 409);
        jobsApi.checkResponseErrorMessage(errorBody.getMessage(), JobsErrorMessage.COULD_NOT_EXECUTE_STATEMENT);
        //and
        jobsApi.deleteJob(firstJobResponse.as(Job.class).getId());
    }

}