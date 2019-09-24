package utn.tdm.meegos.util;

import java.util.Hashtable;

public class XMLUtil
{
    private static CustomXMLParser parser;
    
    static {
        XMLUtil.parser = new CustomXMLParser();
    }
    
    public static XMLDataBlock parse(final String xml) {
        XMLUtil.parser.parse(xml);
        return XMLUtil.parser.getDataBlock();
    }
    
    public static XMLDataBlock getDataBlockError(final String requestId, final int errorCode) {
        return getDataBlockError(requestId, errorCode, (String)null);
    }
    
    public static String getRequestId(final XMLDataBlock db) {
        String requestId = null;
        if (db != null) {
            requestId = db.getAttribute("id");
        }
        return requestId;
    }
    
    public static XMLDataBlock getDataBlockSuccess(final String requestId, final XMLDataBlock dbResultChild) {
        final XMLDataBlock dbSuccess = new XMLDataBlock("result", (XMLDataBlock)null, (Hashtable)null);
        dbSuccess.setAttribute("type", "success");
        if (requestId != null) {
            dbSuccess.setAttribute("id", requestId);
        }
        if (dbResultChild != null) {
            dbSuccess.addChild(dbResultChild);
        }
        return dbSuccess;
    }
    
    public static XMLDataBlock getDataBlockError(final String requestId, final int errorCode, final String message) {
        final XMLDataBlock dbError = new XMLDataBlock("result", (XMLDataBlock)null, (Hashtable)null);
        dbError.setAttribute("type", "error");
        if (requestId != null) {
            dbError.setAttribute("id", requestId);
        }
        final XMLDataBlock dbDetail = new XMLDataBlock("detail", (XMLDataBlock)null, (Hashtable)null);
        dbDetail.setAttribute("code", String.valueOf(errorCode));
        if (message != null) {
            dbDetail.setAttribute("description", message);
        }
        dbError.addChild(dbDetail);
        return dbError;
    }
    
    public static String[] getUserCredentials(final XMLDataBlock db) {
        final String[] credentials = new String[2];
        if (db != null) {
            final XMLDataBlock dbAuth = db.getChildBlock("auth");
            if (dbAuth != null) {
                credentials[0] = dbAuth.getAttribute("username");
                credentials[1] = dbAuth.getAttribute("key");
            }
        }
        return credentials;
    }
    
    public static String getCDATA(final String message) {
        final StringBuilder builder = new StringBuilder("<![CDATA[");
        if (message != null) {
            builder.append(message);
        }
        builder.append("]]>");
        return builder.toString();
    }
    
    public static String getActionName(final XMLDataBlock db) {
        String action = null;
        if (db != null && "action".equals(db.getTagName())) {
            action = db.getAttribute("name");
        }
        return action;
    }
}
