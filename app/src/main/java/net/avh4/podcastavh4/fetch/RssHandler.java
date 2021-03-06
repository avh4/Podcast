package net.avh4.podcastavh4.fetch;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class RssHandler extends DefaultHandler {
    private String buffer;
    private boolean sawItem;

    private String latestTitle;
    private String latestMediaUrl;

    @Override
    public void startDocument() throws SAXException {
        sawItem = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (sawItem && localName.equals("title")) {
            buffer = "";
        } else if (localName.equals("item")) {
            sawItem = true;
        } else if (localName.equals("enclosure")) {
            if (latestMediaUrl == null) {
                latestMediaUrl = attributes.getValue("url");
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("title")) {
            if (latestTitle == null) {
                latestTitle = buffer;
                buffer = null;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (buffer == null) {
            return;
        }
        buffer += String.valueOf(ch, start, length);
    }

    public String latestTitle() {
        return latestTitle;
    }

    public String latestMediaUrl() {
        return latestMediaUrl;
    }
}
