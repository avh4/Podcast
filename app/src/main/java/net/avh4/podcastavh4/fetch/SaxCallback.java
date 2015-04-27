package net.avh4.podcastavh4.fetch;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Response;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public abstract class SaxCallback implements Callback {
    public static final Charset UTF8 = Charset.forName("UTF-8");

    protected RssHandler handler;

    protected SaxCallback(RssHandler handler) {
        this.handler = handler;
    }

    protected abstract void onSuccess(int code);

    @Override
    public void onResponse(Response response) throws IOException {
        InputStream instream = response.body().byteStream();
        InputStreamReader inputStreamReader = null;
        if (instream != null) {
            try {
                SAXParserFactory sfactory = SAXParserFactory.newInstance();
                SAXParser sparser = sfactory.newSAXParser();
                XMLReader rssReader = sparser.getXMLReader();
                rssReader.setContentHandler(handler);
                inputStreamReader = new InputStreamReader(instream, UTF8);
                rssReader.parse(new InputSource(inputStreamReader));
                onSuccess(response.code());
            } catch (SAXException | ParserConfigurationException e) {
                Log.e("RssPodcastSource", "getResponseData exception", e);
            } finally {
                instream.close();
                if (inputStreamReader != null) {
                    try {
                        inputStreamReader.close();
                    } catch (IOException e) { /*ignore*/ }
                }
            }
        }
    }
}
