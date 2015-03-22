package net.avh4.podcastavh4;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class RadioLabClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(AsyncHttpResponseHandler responseHandler) {
        client.get("http://feeds.wnyc.org/radiolab", null, responseHandler);
    }
}
