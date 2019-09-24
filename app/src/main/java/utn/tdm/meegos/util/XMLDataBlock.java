package utn.tdm.meegos.util;

import java.util.Enumeration;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;

public class XMLDataBlock
{
    public static final int COMPRESSION_NONE = 0;
    public static final int COMPRESSION_NM1 = 1;
    protected static int _compressionType;
    private String _tagName;
    protected Vector<XMLDataBlock> _childBlocks;
    protected StringBuffer _textData;
    protected XMLDataBlock _parent;
    protected Hashtable<String, String> _attributes;
    
    static {
        XMLDataBlock._compressionType = 0;
    }
    
    public XMLDataBlock() {
        this("unknown", null, null);
    }
    
    public XMLDataBlock(final XMLDataBlock parent) {
        this("unknown", parent, null);
    }
    
    public XMLDataBlock(final XMLDataBlock parent, final Hashtable<String, String> attributes) {
        this("unknown", parent, attributes);
    }
    
    public XMLDataBlock(final String tagName, final XMLDataBlock parent, final Hashtable<String, String> attributes) {
        this._textData = null;
        this._parent = parent;
        this._attributes = attributes;
        this._tagName = tagName;
    }
    
    public void addChild(final XMLDataBlock newData) {
        if (this._childBlocks == null) {
            this._childBlocks = new Vector();
        }
        this._childBlocks.addElement(newData);
    }
    
    public void addText(final String text) {
        if (this._textData == null) {
            this._textData = new StringBuffer();
        }
        this._textData.append(text);
    }
    
    public XMLDataBlock getParent() {
        return this._parent;
    }
    
    public byte[] getBytes() {
        final String data = this.toString();
        return data.getBytes();
    }
    
    public String getText() {
        if (this._textData != null) {
            String t;
            try {
                t = new String(this._textData.toString().getBytes(), "UTF8");
            }
            catch (UnsupportedEncodingException uee) {
                t = this._textData.toString();
            }
            return t;
        }
        return null;
    }
    
    public String getAttribute(final String attributeName) {
        if (this._attributes != null && !this._attributes.isEmpty()) {
            final Object attr = this._attributes.get(attributeName);
            if (attr != null) {
                String t;
                try {
                    t = new String(((String)attr).getBytes(), "UTF8");
                }
                catch (UnsupportedEncodingException uee) {
                    t = (String)attr;
                }
                return t;
            }
        }
        return null;
    }
    
    public int getAttributesNumber() {
        int attributeNumber = 0;
        if (this._attributes != null) {
            attributeNumber = this._attributes.size();
        }
        return attributeNumber;
    }
    
    public void setAttribute(final String attributeName, final String value) {
        if (attributeName == null || value == null) {
            return;
        }
        if (this._attributes == null) {
            this._attributes = new Hashtable();
        }
        this._attributes.put(attributeName, value);
    }
    
    public Vector<XMLDataBlock> getChildBlocks() {
        return (Vector<XMLDataBlock>)this._childBlocks;
    }
    
    public Vector<XMLDataBlock> getChildBlocks(final String blockname) {
        Vector<XMLDataBlock> foundBlocks = null;
        if (this._childBlocks == null || this._childBlocks.size() == 0) {
            return null;
        }
        for (int i = 0; i < this._childBlocks.size(); ++i) {
            final XMLDataBlock thisBlock = this._childBlocks.elementAt(i);
            if (thisBlock.getTagName().equals(blockname)) {
                if (foundBlocks == null) {
                    foundBlocks = new Vector<XMLDataBlock>();
                }
                foundBlocks.addElement(thisBlock);
            }
        }
        return foundBlocks;
    }
    
    public XMLDataBlock getChildBlock(final String blockname) {
        if (this._childBlocks == null || this._childBlocks.size() == 0) {
            return null;
        }
        for (int i = 0; i < this._childBlocks.size(); ++i) {
            final XMLDataBlock thisBlock = this._childBlocks.elementAt(i);
            if (thisBlock.getTagName().equals(blockname)) {
                return thisBlock;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        final String tagStart = this.getTagStart();
        final StringBuffer data = new StringBuffer(tagStart);
        if (this._textData != null) {
            data.append(this._textData);
        }
        if (this._childBlocks != null) {
            final Enumeration<XMLDataBlock> e = this._childBlocks.elements();
            while (e.hasMoreElements()) {
                final XMLDataBlock thisBlock = e.nextElement();
                data.append(thisBlock.toString());
            }
        }
        final String endTag = this.getTagEnd();
        data.append(endTag);
        return data.toString();
    }
    
    protected void addAttributeToStringBuffer(final StringBuffer buffer) {
        final Enumeration<String> e = this._attributes.keys();
        while (e.hasMoreElements()) {
            final String nextKey = e.nextElement();
            final String nextValue = this._attributes.get(nextKey);
            buffer.append(' ');
            buffer.append(nextKey);
            buffer.append("=\"");
            buffer.append(nextValue);
            buffer.append('\"');
        }
    }
    
    public String getTagStart() {
        final StringBuffer tagStart = new StringBuffer("<");
        tagStart.append(this.getTagName());
        if (this._attributes != null) {
            this.addAttributeToStringBuffer(tagStart);
        }
        tagStart.append('>');
        return tagStart.toString();
    }
    
    public String getTagEnd() {
        final StringBuffer end = new StringBuffer("</");
        end.append(this.getTagName());
        end.append('>');
        return end.toString();
    }
    
    public String getTagName() {
        return this._tagName;
    }
    
    public static void setCompressionType(final int compressionType) {
        XMLDataBlock._compressionType = compressionType;
    }
}
