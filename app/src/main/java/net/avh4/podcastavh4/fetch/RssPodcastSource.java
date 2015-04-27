package net.avh4.podcastavh4.fetch;

import android.net.Uri;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import net.avh4.podcastavh4.Episode;
import net.avh4.podcastavh4.PodcastSource;

import org.jdeferred.Deferred;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import java.io.IOException;

public class RssPodcastSource implements PodcastSource {
    private static OkHttpClient client = new OkHttpClient();
    private final String feedUrl;
    private final String title;

    public RssPodcastSource(String title, String feedUrl) {
        this.title = title;
        this.feedUrl = feedUrl;
    }

    @Override
    public Promise<Episode, Void, Void> get() {
        final Deferred<Episode, Void, Void> deferred = new DeferredObject<>();
        Request request = new Request.Builder().url(feedUrl).build();
        client.newCall(request).enqueue(new SaxCallback(new RssHandler()) {
            @Override
            protected void onSuccess(int statusCode) {
                Uri uri = Uri.parse(handler.latestMediaUrl());
                Episode episode = new Episode(title, handler.latestTitle(), uri);
                deferred.resolve(episode);
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("RssPodcastSource", "Request failed", e);
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
