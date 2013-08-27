package kanbannow.health;

import com.yammer.metrics.core.HealthCheck;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static org.fest.assertions.Assertions.assertThat;

public class BacklogItemHealthCheck extends HealthCheck {

    public BacklogItemHealthCheck() {
        super("backlogItemService");
    }

    @Override
    protected Result check() throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        String uri = buildURI();
        HttpGet httpget = new HttpGet(uri);
        HttpResponse httpResponse = httpclient.execute(httpget);
        verifyResponseWas200(httpResponse);
        String resultString = getStringFromResponse(httpResponse);
        assertThat(resultString).isEqualTo("[]");
        return Result.healthy();
    }

    private String buildURI() {
        int port = 9690;
        return "http://localhost:" + port + "/backlogitems/user/1";
    }

    private void verifyResponseWas200(HttpResponse httpResponse) {
        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = statusLine.getStatusCode();

        if( statusCode != 200)
            throw new RuntimeException("statusCode was =" + statusCode);
    }

    private String getStringFromResponse(HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        return bufferedReader.readLine();
    }


}
