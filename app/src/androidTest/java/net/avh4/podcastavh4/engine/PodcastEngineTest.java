package net.avh4.podcastavh4.engine;

import android.net.Uri;

import junit.framework.TestCase;

import net.avh4.podcastavh4.Episode;
import net.avh4.podcastavh4.Player;
import net.avh4.podcastavh4.PodcastSource;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static net.avh4.podcastavh4.PodcastSource.PodcastSourceListener;
import static org.mockito.Mockito.doNothing;
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
    @Captor
    private ArgumentCaptor<PodcastSourceListener> listener;

    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        subject = new PodcastEngine(podcastSource, player);
        subject.subscribe(subscriber);
        doNothing().when(podcastSource).get(listener.capture());
    }

    public void testInitialRun_fetchesLatestEpisode() {
        subject.start();
        verify(podcastSource).get(Mockito.<PodcastSourceListener>any());
    }

    public void testWhenSourceLoads_playsEpisode() {
        subject.start();
        listener.getValue().onPodcastReady(episode);
        verify(player).playStream(episode.getMediaUri());
    }

    public void testWhenSourceLoads_notifiesListener() {
        subject.start();
        listener.getValue().onPodcastReady(episode);
        verify(subscriber).onNewPodcast(episode);
    }
}