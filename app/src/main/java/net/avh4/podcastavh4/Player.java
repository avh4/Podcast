package net.avh4.podcastavh4;

import android.net.Uri;

public interface Player {
    void playStream(Uri uri);

    void playDownload(Episode episode);
}
