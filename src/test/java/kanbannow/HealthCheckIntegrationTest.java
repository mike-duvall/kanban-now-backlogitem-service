package kanbannow;

import com.yammer.dropwizard.config.ConfigurationException;
import com.yammer.dropwizard.testing.junit.DropwizardServiceRule;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class HealthCheckIntegrationTest {

    public static final String CONFIG_FILE = "service-config-for-health-check-integration-test.yml";
    public static final String PROPERTIES_PATH = "src/test/resources/";
    private static final String HEALTHCHECK_SERVICE_LOG = "healthcheck-service.log";

    private DropwizardServiceRule<BacklogItemServiceConfiguration> serviceRule = new DropwizardServiceRule<BacklogItemServiceConfiguration>(BacklogItemService.class, PROPERTIES_PATH + CONFIG_FILE);

    @Rule
    public DropwizardServiceRule<BacklogItemServiceConfiguration> getServiceRule() {
        return serviceRule;
    }

    @BeforeClass
    public static void beforeClass() {
        deletePreviousLog();
    }

    private static void deletePreviousLog() {
        File logFile = new File(HEALTHCHECK_SERVICE_LOG);
        if( logFile.exists())
            logFile.delete();
    }

    @Test
    public void healthCheckShouldReturnHealthy() throws IOException, ConfigurationException {
        String uri = getHealthCheckURL();
        HttpResponse httpResponse = callHealthCheck(uri);
        validateResponseFromHealthCheck(httpResponse);
        checkForErrorsInLogFile();
    }


    private void checkForErrorsInLogFile() throws IOException {
        BufferedReader br = getLogFileReader();
        scanForErrors(br);
    }

    private BufferedReader getLogFileReader() throws FileNotFoundException {
        File logFile = getLogFile();
        return new BufferedReader(new FileReader(logFile));
    }


    private void scanForErrors(BufferedReader br) throws IOException {
        String line;
        try {
            while ((line = br.readLine()) != null)
                processLine( line);
        }
        finally {
            br.close();
        }
    }


    private void processLine( String line) throws IOException {
        if(lineContainsWarnOrError(line))
            throw new RuntimeException("Test failed:  ERRORS or WARN in log file");
    }

    private boolean lineContainsWarnOrError(String line) {
        return StringUtils.contains(line, "ERROR") || StringUtils.contains(line, "WARN");
    }

    private File getLogFile() {
        File logFile = new File(HEALTHCHECK_SERVICE_LOG);
        if( !logFile.exists())
            throw new RuntimeException("Error:  Expected logfile");
        return logFile;
    }

    private void validateResponseFromHealthCheck(HttpResponse httpResponse) throws IOException {
        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        if( statusCode != 200) {
            String healthCheckText = getStringFromHttpResponse(httpResponse);
            throw new RuntimeException("Expected status code " + 200 + " but was instead " + statusCode + healthCheckText);
        }
    }

    private HttpResponse callHealthCheck(String uri) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(uri);
        return httpclient.execute(httpget);
    }

    private String getHealthCheckURL() throws IOException, ConfigurationException {
        int port = readAdminPortFromConfiguration();
        return "http://localhost:" + port + "/healthcheck";
    }

    private int readAdminPortFromConfiguration() throws IOException, ConfigurationException {
        BacklogItemServiceConfiguration configuration = serviceRule.getConfiguration();
        return configuration.getHttpConfiguration().getAdminPort();
    }

    private String getStringFromHttpResponse(HttpResponse httpResponse) throws IOException {
        BufferedReader bufferedReader = getBufferedReader(httpResponse);
        String fullResponseText = "";
        String nextLine = bufferedReader.readLine();
        while(nextLine != null) {
            fullResponseText += System.getProperty("line.separator") + nextLine;
            nextLine = bufferedReader.readLine();
        }
        return fullResponseText;
    }

    private BufferedReader getBufferedReader(HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }


}
