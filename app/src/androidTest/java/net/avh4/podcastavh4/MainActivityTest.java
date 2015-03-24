package net.avh4.podcastavh4;

import android.content.Intent;
import android.net.Uri;
import android.test.ActivityUnitTestCase;
import android.view.ContextThemeWrapper;

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

public class MainActivityTest extends ActivityUnitTestCase<MainActivity> {

    private static final Uri uri = Uri.parse("http://example.com/podcast.mp3");
    private static final String title = "Fu-Go";

    @Mock
    private Player player;
    @Mock
    private PodcastSource podcastSource;
    private Deferred<Episode, Void, Void> podcastSourceGet;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        podcastSourceGet = new DeferredObject<>();

        MockitoAnnotations.initMocks(this);
        stub(podcastSource.get()).toReturn(podcastSourceGet.promise());
        MainActivity.setNextPlayer(player);
        MainActivity.setNextPodcastSource(podcastSource);

        super.setUp();
        ContextThemeWrapper context = new ContextThemeWrapper(getInstrumentation().getTargetContext(), R.style.AppTheme);
        setActivityContext(context);
        startActivity(new Intent(getInstrumentation().getTargetContext(), MainActivity.class), null, null);
    }

    public void testFetchesPodcast() {
        verify(podcastSource).get();
    }

    public void testOnSuccessStartsPlayer() {
        podcastSourceGet.resolve(new Episode(null, title, uri));
        verify(player).playStream(uri);
    }
}
