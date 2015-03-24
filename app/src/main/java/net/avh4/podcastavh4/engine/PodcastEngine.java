package net.avh4.podcastavh4.engine;

import net.avh4.podcastavh4.Episode;
import net.avh4.podcastavh4.Player;
import net.avh4.podcastavh4.PodcastSource;

import org.jdeferred.DoneCallback;

public class PodcastEngine {

    private final PodcastSource podcastSource;
    private final Player player;

    private EngineListener listener;

    public PodcastEngine(PodcastSource podcastSource, Player player) {
        this.podcastSource = podcastSource;
        this.player = player;
    }

    public void start() {
        podcastSource.get().then(new DoneCallback<Episode>() {
            @Override
            public void onDone(Episode episode) {
                listener.onNewPodcast(episode);
                player.playStream(episode.getMediaUri());
            }
        });
    }

    public void subscribe(EngineListener listener) {
        this.listener = listener;
    }

    public void play(Episode episode) {
        player.playDownload(episode);
    }
}
