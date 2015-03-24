package net.avh4.podcastavh4.engine;

import android.util.Log;

import net.avh4.podcastavh4.Episode;
import net.avh4.podcastavh4.Player;
import net.avh4.podcastavh4.PodcastSource;

public class PodcastEngine {

    private final PodcastSource podcastSource;
    private final Player player;

    private EngineListener listener;

    public PodcastEngine(PodcastSource podcastSource, Player player) {
        this.podcastSource = podcastSource;
        this.player = player;
    }

    public void start() {
        podcastSource.get(new PodcastSource.PodcastSourceListener() {
            @Override
            public void onPodcastReady(Episode episode) {
                listener.onNewPodcast(episode);
                player.playStream(episode.getMediaUri());
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("PodcastEngine", "Request failed", throwable);
            }
        });
    }

    public void subscribe(EngineListener listener) {
        this.listener = listener;
    }
}
