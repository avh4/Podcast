package net.avh4.podcastavh4;

import android.net.Uri;

public interface PodcastSource {
    void get(PodcastSourceListener listener);

    interface PodcastSourceListener {
        public void onPodcastReady(Uri uri);

        public void onError(Throwable throwable);
    }
}