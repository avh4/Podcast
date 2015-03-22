package net.avh4.podcastavh4;

import junit.framework.TestCase;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class RssHandlerTest extends TestCase {

    private RssHandler subject;

    public void setUp() {
        subject = new RssHandler();
    }

    public void testLatestTitle() throws Exception {
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        startElement("title");
        characters("Fu-Go");
        endElement("title");
        endElement("item");
        endElement("channel");
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleWithExtraCharsInBuffer() throws Exception {
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        startElement("title");
        subject.characters("XXFu-GoXX".toCharArray(), 2, 5);
        endElement("title");
        endElement("item");
        endElement("channel");
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleInMultipleChunks() throws Exception {
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        startElement("title");
        characters("Fu");
        characters("-Go");
        endElement("title");
        endElement("item");
        endElement("channel");
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleWithOlderItems() throws Exception {
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        startElement("title");
        characters("Fu-Go");
        endElement("title");
        endElement("item");
        startElement("item");
        startElement("title");
        characters("La Mancha Screwjob");
        endElement("title");
        endElement("item");
        endElement("channel");
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleWithSubsequentItemFields() throws Exception {
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        startElement("title");
        characters("Fu-Go");
        endElement("title");
        startElement("description");
        characters("During World War II...");
        endElement("description");
        endElement("item");
        endElement("channel");
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleWithPriorItemFields() throws Exception {
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        startElement("description");
        characters("During World War II...");
        endElement("description");
        startElement("title");
        characters("Fu-Go");
        endElement("title");
        endElement("item");
        endElement("channel");
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleWithChannelTitle() throws Exception {
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("title");
        subject.characters("Radiolab from WNYC".toCharArray(), 0, 0);
        endElement("title");
        startElement("item");
        startElement("title");
        characters("Fu-Go");
        endElement("title");
        endElement("item");
        endElement("channel");
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestMediaUrl() throws Exception {
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        enclosure("audio/mpeg", "http://feeds.wnyc.org/~r/radiolab/~5/lkhKLVXJosc/radiolab_podcast15fugo.mp3");
        endElement("item");
        endElement("channel");
        subject.endDocument();

        assertEquals("http://feeds.wnyc.org/~r/radiolab/~5/lkhKLVXJosc/radiolab_podcast15fugo.mp3",
                subject.latestMediaUrl());
    }

    public void testLatestMediaUrlWithOlderItems() throws Exception {
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        enclosure("audio/mpeg", "http://feeds.wnyc.org/~r/radiolab/~5/lkhKLVXJosc/radiolab_podcast15fugo.mp3");
        endElement("item");
        startElement("item");
        enclosure("audio/mpeg", "http://feeds.wnyc.org/~r/radiolab/~5/8rAUNHzHyBI/radiolab_podcast09joshgreene.mp3");
        endElement("item");
        endElement("channel");
        subject.endDocument();

        assertEquals("http://feeds.wnyc.org/~r/radiolab/~5/lkhKLVXJosc/radiolab_podcast15fugo.mp3",
                subject.latestMediaUrl());
    }

    private void enclosure(String type, String url) throws SAXException {
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, "url", "url", null, url);
        attributes.addAttribute(null, "type", "type", null, type);
        subject.startElement(null, "enclosure", "enclosure", attributes);
        subject.endElement(null, "enclosure", "enclosure");
    }

    private void startElement(String localName) throws SAXException {
        subject.startElement(null, localName, localName, null);
    }

    private void endElement(String localName) throws SAXException {
        subject.endElement(null, localName, localName);
    }

    private void characters(String string) throws SAXException {
        subject.characters(string.toCharArray(), 0, string.length());
    }
}
