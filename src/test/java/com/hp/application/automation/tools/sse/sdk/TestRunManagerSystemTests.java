package com.hp.application.automation.tools.sse.sdk;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.hp.application.automation.tools.model.CdaDetails;
import com.hp.application.automation.tools.model.SseModel;
import com.hp.application.automation.tools.rest.RestClient;
import com.hp.application.automation.tools.sse.ArgsFactory;
import com.hp.application.automation.tools.sse.result.model.junit.JUnitTestCaseStatus;
import com.hp.application.automation.tools.sse.result.model.junit.Testcase;
import com.hp.application.automation.tools.sse.result.model.junit.Testsuites;

/**
 * 
 * @author Amir Zahavi
 * 
 */
@Ignore
public class TestRunManagerSystemTests {
    
    private static final String SERVER_NAME = "zahavia1.emea.hpqcorp.net";
    private static final int PORT = 8080;
    private static final String PROJECT = "Project1";
    private static int TEST_SET_ID = 2;
    private static int BVS_ID = 1084;
    
    @Test
    public void testEndToEndTestSet() throws InterruptedException {
        
        SseModel model = createModel(SseModel.TEST_SET, SERVER_NAME, PROJECT, PORT, TEST_SET_ID);
        Args args = new ArgsFactory().create(model);
        RestClient connection =
                new RestClient(
                        args.getUrl(),
                        args.getDomain(),
                        args.getProject(),
                        args.getUsername());
        Testsuites testsuites = new RunManager().execute(connection, args, new ConsoleLogger());
        
        assertTestsuitesPassed(testsuites);
    }
    
    @Test
    public void testEndToEndBVS() throws InterruptedException {
        
        SseModel model = createModel(SseModel.BVS, SERVER_NAME, PROJECT, PORT, BVS_ID);
        Args args = new ArgsFactory().create(model);
        RestClient connection =
                new RestClient(
                        args.getUrl(),
                        args.getDomain(),
                        args.getProject(),
                        args.getUsername());
        Testsuites testsuites = new RunManager().execute(connection, args, new ConsoleLogger());
        
        Assert.assertNotNull(testsuites);
        assertTestsuitesPassed(testsuites);
    }
    
    private void assertTestsuitesPassed(Testsuites testsuites) {
        
        Testcase testcase = testsuites.getTestsuite().get(0).getTestcase().get(0);
        Assert.assertNotNull(testcase);
        Assert.assertTrue(
                "Test did not run successfully",
                testcase.getStatus().equals(JUnitTestCaseStatus.PASS));
    }
    
    /**
     * 
     * @param entityType
     *            "BVS" or "TEST_SET"
     */
    private SseModel createModel(
            String entityType,
            String serverName,
            String projectName,
            int port,
            int testSetID) {
        
        final String userName = "sa";
        String description = "";
        final String password = "";
        String domain = "DEFAULT";
        String timeslotDuration = "30";
        String postRunAction = "Collate";
        String environmentConfigurationId = "";
        CdaDetails cdaDetails = null;
        SseModel ret =
                new MockSseModel(
                        serverName,
                        userName,
                        password,
                        domain,
                        projectName,
                        entityType,
                        String.valueOf(testSetID),
                        timeslotDuration,
                        description,
                        postRunAction,
                        environmentConfigurationId,
                        cdaDetails);
        ret.setAlmServerUrl(String.format("http://%s:%d/qcbin", serverName, port));
        
        return ret;
    }
    
}
