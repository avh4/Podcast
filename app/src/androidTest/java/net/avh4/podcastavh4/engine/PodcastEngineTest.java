package net.avh4.podcastavh4.engine;

import android.net.Uri;

import junit.framework.TestCase;

import net.avh4.podcastavh4.Episode;
import net.avh4.podcastavh4.Player;
import net.avh4.podcastavh4.PodcastSource;

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class PodcastEngineTest extends TestCase {

    private static final Episode episode = new Episode(null, null, Uri.parse("https://example.com/podcast.mp3"));

    private PodcastEngine subject;
    @Mock
    private PodcastSource podcastSource;
    @Mock
    private Player player;
    @Mock
    private EngineListener subscriber;
    private Deferred<Episode, Void, Void> podcastSourceGet;

    public void setUp() throws Exception {
        super.setUp();

        podcastSourceGet = new DeferredObject<>();

        MockitoAnnotations.initMocks(this);
        subject = new PodcastEngine(podcastSource, player);
        subject.subscribe(subscriber);
        stub(podcastSource.get()).toReturn(podcastSourceGet.promise());
    }

    public void testInitialRun_fetchesLatestEpisode() {
        subject.start();
        verify(podcastSource).get();
    }

    public void testWhenSourceLoads_playsEpisode() {
        subject.start();
        podcastSourceGet.resolve(episode);
        verify(player).playStream(episode.getMediaUri());
    }

    public void testWhenSourceLoads_notifiesListener() {
        subject.start();
        podcastSourceGet.resolve(episode);
        verify(subscriber).onNewPodcast(episode);
    }
}