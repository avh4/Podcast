package net.avh4.podcastavh4;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class AndroidPlayer implements Player {

    private final Context context;

    public AndroidPlayer(Context context) {
        this.context = context;
    }

    @Override
    public void playStream(Uri uri) {
        MediaPlayer player = MediaPlayer.create(context, uri);
        player.start();
    }
}
