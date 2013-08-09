package kanbannow;

import com.yammer.dropwizard.config.ConfigurationException;
import com.yammer.dropwizard.testing.junit.DropwizardServiceRule;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Rule;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class HealthCheckIntegrationTest {

    // This test requires the existence of a property file at: ../properties/card-service.yml
    public static final String CONFIG_FILE = "backlogitem-service.yml";
    public static final String PROPERTIES_PATH = "resources/config-for-health-check-integration-test/";

    private DropwizardServiceRule<BacklogItemServiceConfiguration> serviceRule = new DropwizardServiceRule<BacklogItemServiceConfiguration>(BacklogItemService.class, PROPERTIES_PATH + CONFIG_FILE);

    @Rule
    public DropwizardServiceRule<BacklogItemServiceConfiguration> getServiceRule() {
        return serviceRule;
    }

    @Test
    public void healthCheckShouldReturnHealthy() throws IOException, ConfigurationException {
        String uri = getHealthCheckURL();
        HttpResponse httpResponse = callHealthCheck(uri);
        validateResponseFromHealthCheck(httpResponse);
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
