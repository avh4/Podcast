package net.avh4.podcastavh4;

import android.content.Intent;
import android.net.Uri;
import android.test.ActivityUnitTestCase;
import android.view.ContextThemeWrapper;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

    private static final Uri uri = Uri.parse("http://example.com/podcast.mp3");

    private TestPlayer player;
    private TestPodcastSource podcastSource;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        player = new TestPlayer();
        MainActivity.setNextPlayer(player);
        podcastSource = new TestPodcastSource();
        MainActivity.setNextPodcastSource(podcastSource);
        super.setUp();
        ContextThemeWrapper context = new ContextThemeWrapper(getInstrumentation().getTargetContext(), R.style.AppTheme);
        setActivityContext(context);
        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);
    }

    public void testFetchesPodcast() {
        assertNotNull(podcastSource.listener);
    }

    public void testOnSuccessStartsPlayer() {
        podcastSource.listener.onPodcastReady(uri);
        assertNotNull(player.uri);
        assertEquals(uri, player.uri);
    }

    static class TestPlayer implements Player {
        public Uri uri;

        @Override
        public void playStream(Uri uri) {
            this.uri = uri;
        }
    }

    static class TestPodcastSource implements PodcastSource {
        public PodcastSourceListener listener;

        @Override
        public void get(PodcastSourceListener listener) {
            this.listener = listener;
        }

        @Override
        public String getTitle() {
            return null;
        }
    }
}
