package net.avh4.podcastavh4.engine;

import net.avh4.podcastavh4.Episode;

public interface EngineListener {
    void onNewPodcast(Episode episode);
}
