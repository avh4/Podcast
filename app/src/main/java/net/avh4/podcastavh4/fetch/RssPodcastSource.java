package net.avh4.podcastavh4.fetch;

import android.net.Uri;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SaxAsyncHttpResponseHandler;

import net.avh4.podcastavh4.PodcastSource;

import org.apache.http.Header;

public class RssPodcastSource implements PodcastSource {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private final String feedUrl;
    private final String title;

    public RssPodcastSource(String title, String feedUrl) {
        this.title = title;
        this.feedUrl = feedUrl;
    }

    @Override
    public void get(final PodcastSourceListener listener) {
        client.get(feedUrl, null, new SaxAsyncHttpResponseHandler<RssHandler>(new RssHandler()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, RssHandler handler) {
                Uri uri = Uri.parse(handler.latestMediaUrl());
                listener.onPodcastReady(uri);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, RssHandler handler) {
                listener.onError(new Exception("Request failed: " + statusCode));
            }
        });
    }

    @Override
    public String getTitle() {
        return title;
    }
}
