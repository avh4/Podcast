package net.avh4.podcastavh4;

import android.net.Uri;

public class Episode {
    private final String channelTitle;
    private final String title;
    private final Uri mediaUri;

    public Episode(String channelTitle, String title, Uri mediaUri) {
        this.channelTitle = channelTitle;
        this.title = title;
        this.mediaUri = mediaUri;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public String getTitle() {
        return title;
    }

    public Uri getMediaUri() {
        return mediaUri;
    }
}
