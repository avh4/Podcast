package net.avh4.podcastavh4;

import org.jdeferred.Promise;

public interface MediaStore {
    Promise<Void, Void, Void> download(String url);
}
