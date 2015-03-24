package net.avh4.podcastavh4;

import org.jdeferred.Promise;

public interface PodcastSource {
    Promise<Episode, Void, Void> get();

    String getTitle();
}
