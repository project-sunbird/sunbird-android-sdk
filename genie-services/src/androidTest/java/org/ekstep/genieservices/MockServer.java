package org.ekstep.genieservices;

import junit.framework.Assert;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Created by swayangjit on 31/8/17.
 */

public class MockServer {

    private final int MOCKED_PORT_DEV_VARIANT = 33456;
    private MockWebServer serverMock;

    public MockServer() {
        serverMock = new MockWebServer();
    }

    public void start() throws IOException {
        serverMock.start(MOCKED_PORT_DEV_VARIANT);
    }

    public void mockHttpResponse(String body, int responseCode) {
        MockResponse response = new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setResponseCode(responseCode)
                .setBody(body);
        // response.throttleBody(1024, 1, TimeUnit.SECONDS);
        serverMock.enqueue(response);
    }

    public void assertRequestCount(int timesCalled) {
        int requestCount = serverMock.getRequestCount();
        Assert.assertEquals(timesCalled, requestCount);
    }

    public void shutDown() throws IOException {
        serverMock.shutdown();
    }
}
