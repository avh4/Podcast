package net.avh4.podcastavh4;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class RssHandler extends DefaultHandler {
    private String buffer;
    private String latestTitle;
    private boolean sawItem;

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
}
