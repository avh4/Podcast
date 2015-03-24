package net.avh4.podcastavh4.fetch;

import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SaxAsyncHttpResponseHandler;

import net.avh4.podcastavh4.Episode;
import net.avh4.podcastavh4.PodcastSource;

import org.apache.http.Header;
import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

public class RssPodcastSource implements PodcastSource {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private final String feedUrl;
    private final String title;

    public RssPodcastSource(String title, String feedUrl) {
        this.title = title;
        this.feedUrl = feedUrl;
    }

    @Override
    public Promise<Episode, Void, Void> get() {
        final Deferred<Episode, Void, Void> deferred = new DeferredObject<>();
        client.get(feedUrl, null, new SaxAsyncHttpResponseHandler<RssHandler>(new RssHandler()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, RssHandler handler) {
                Uri uri = Uri.parse(handler.latestMediaUrl());
                Episode episode = new Episode(title, handler.latestTitle(), uri);
                deferred.resolve(episode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, RssHandler handler) {
                Log.e("RssPodcastSource", "Request failed: " + statusCode);
                deferred.reject(null);
            }
        });
        return deferred.promise();
    }

    @Override
    public String getTitle() {
        return title;
    }
}
