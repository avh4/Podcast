package net.avh4.podcastavh4.fetch;

public class RadioLabClient extends RssPodcastSource {
    public RadioLabClient() {
        super("http://feeds.wnyc.org/radiolab");
    }
}