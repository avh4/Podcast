package net.avh4.podcastavh4;

public interface PodcastSource {
    void get(PodcastSourceListener listener);

    String getTitle();

    interface PodcastSourceListener {
        public void onPodcastReady(Episode episode);

        public void onError(Throwable throwable);
    }
}
