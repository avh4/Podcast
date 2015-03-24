package net.avh4.podcastavh4;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.avh4.podcastavh4.audio.AndroidPlayer;
import net.avh4.podcastavh4.engine.PodcastEngine;
import net.avh4.podcastavh4.fetch.RadioLabClient;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String LOG_TAG = "Podcast/avh4";

    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private static Player nextPlayer = null;
    private static PodcastSource nextPodcastSource = null;
    private final PodcastEngine engine;

    public MainActivity() {
        final Player player;

        if (nextPlayer != null) {
            player = nextPlayer;
            nextPlayer = null;
        } else {
            player = new AndroidPlayer(this);
        }

        final PodcastSource podcastSource;
        if (nextPodcastSource != null) {
            podcastSource = nextPodcastSource;
            nextPodcastSource = null;
        } else {
            podcastSource = new RadioLabClient();
        }

        engine = new PodcastEngine(podcastSource, player);
    }

    public static void setNextPlayer(Player player) {
        nextPlayer = player;
    }

    public static void setNextPodcastSource(PodcastSource podcastSource) {
        nextPodcastSource = podcastSource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final EpisodeAdapter adapter = new EpisodeAdapter();
        recyclerView.setAdapter(adapter);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        engine.start();
        engine.subscribe(new EngineAdapter() {
            public void onNewPodcast(Episode episode) {
                adapter.setEpisode(episode);
                progressBar.setVisibility(View.GONE); // TODO animate
            }
        });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        onSectionAttached(position);
        getSupportActionBar().setTitle(mTitle); // TODO: doesn't work
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
//                mTitle = podcastSource.getTitle();
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.podcastName);
        }
    }

    static class EpisodeAdapter extends RecyclerView.Adapter {
        private Episode episode = null;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.podcast_card, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            myViewHolder.title.setText(episode.getTitle());
        }

        @Override
        public int getItemCount() {
            return episode == null ? 0 : 1;
        }

        public void setEpisode(Episode episode) {
            boolean hadEpisode = this.episode != null;
            this.episode = episode;
            if (hadEpisode) {
                notifyItemChanged(0);
            } else {
                notifyItemInserted(0);
            }
        }
    }
}
