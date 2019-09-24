package utn.tdm.meegos.util;

import org.xml.sax.SAXException;
import java.util.Hashtable;
import org.xml.sax.Attributes;
import javax.xml.parsers.SAXParser;
import java.io.ByteArrayInputStream;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;

public class CustomXMLParser extends DefaultHandler
{
    private XMLDataBlock currentBlock;
    private XMLDataBlock _discoveredBlock;
    
    public void parse(final String xml) {
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            factory.setFeature("http://xml.org/sax/features/namespaces", false);
            factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
//            factory.setXIncludeAware(false);
            final SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new ByteArrayInputStream(xml.getBytes()), this);
        }
        catch (Throwable err) {
            err.printStackTrace();
        }
    }
    
    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        Hashtable<String, String> ht = null;
        if (attributes != null) {
            final int len = attributes.getLength();
            if (len > 0) {
                ht = new Hashtable<String, String>();
                for (int i = 0; i < len; ++i) {
                    ht.put(attributes.getQName(i), attributes.getValue(i));
                }
            }
        }
        this.currentBlock = new XMLDataBlock((localName != null && localName.length() != 0) ? localName : qName, this.currentBlock, (Hashtable)ht);
    }
    
    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (this.currentBlock == null) {
            return;
        }
        final XMLDataBlock parent = this.currentBlock.getParent();
        if (parent == null) {
            this._discoveredBlock = this.currentBlock;
        }
        else {
            parent.addChild(this.currentBlock);
        }
        this.currentBlock = parent;
    }
    
    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        super.characters(ch, start, length);
        if (this.currentBlock != null) {
            this.currentBlock.addText(new String(ch, start, length));
        }
    }
    
    public XMLDataBlock getDataBlock() {
        return this._discoveredBlock;
    }
}
