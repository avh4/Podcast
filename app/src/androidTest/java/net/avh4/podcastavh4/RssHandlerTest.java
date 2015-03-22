package net.avh4.podcastavh4;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

public class RssHandlerTest extends TestCase {

    private RssHandler subject;

    public void testLatestTitle() throws Exception {
        subject = new RssHandler();
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        startElement("title");
        characters("Fu-Go");
        endElement("title");
        endElement("item");
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleWithExtraCharsInBuffer() throws Exception {
        subject = new RssHandler();
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        startElement("title");
        subject.characters("XXFu-GoXX".toCharArray(), 2, 5);
        endElement("title");
        endElement("item");
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleInMultipleChunks() throws Exception {
        subject = new RssHandler();
        subject.startDocument();
        startElement("rss");
        startElement("channel");
        startElement("item");
        startElement("title");
        characters("Fu");
        characters("-Go");
        endElement("title");
        endElement("item");
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleWithOlderTitles() throws Exception {
        subject = new RssHandler();
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
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleWithSubsequentItemFields() throws Exception {
        subject = new RssHandler();
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
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleWithPriorItemFields() throws Exception {
        subject = new RssHandler();
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
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
    }

    public void testLatestTitleWithChannelTitle() throws Exception {
        subject = new RssHandler();
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
        subject.endDocument();

        assertEquals("Fu-Go", subject.latestTitle());
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
