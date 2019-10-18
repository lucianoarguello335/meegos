package utn.tdm.meegos.util;

import android.content.Context;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.UUID;

import utn.tdm.meegos.preferences.MeegosPreferences;

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

    public static XMLDataBlock getDataBlockRegisterUser(final String username, final String password) {
//        action Block
        XMLDataBlock requestBodyBlock = new XMLDataBlock("action", null, null);
        requestBodyBlock.setAttribute("id", UUID.randomUUID().toString());
        requestBodyBlock.setAttribute("name", "register-user");

//        action-detail block
        XMLDataBlock actionDetailBlock = new XMLDataBlock("action-detail", requestBodyBlock, null);

//        user block
        XMLDataBlock userBlock = new XMLDataBlock("user", actionDetailBlock, null);
        userBlock.setAttribute("username", username);
        userBlock.setAttribute("password", password);

        actionDetailBlock.addChild(userBlock);
        requestBodyBlock.addChild(actionDetailBlock);
        return requestBodyBlock;
    }

    public static XMLDataBlock getDataBlockGetMessages(Context context) {
//        action Block
        XMLDataBlock requestBodyBlock = new XMLDataBlock("action", null, null);
        requestBodyBlock.setAttribute("id", UUID.randomUUID().toString());
        requestBodyBlock.setAttribute("name", "get-messages");

//        action-detail block
        XMLDataBlock actionDetailBlock = new XMLDataBlock("action-detail", requestBodyBlock, null);

//        auth block
        XMLDataBlock authBlock = new XMLDataBlock("auth", actionDetailBlock, null);
        authBlock.setAttribute("username", MeegosPreferences.getUsername(context));
        authBlock.setAttribute("key", MeegosPreferences.getPassword(context));

//        filter block
        XMLDataBlock filterBlock = new XMLDataBlock("filter", actionDetailBlock, null);
        filterBlock.setAttribute("type", "timestamp");

        filterBlock.addText(MeegosPreferences.getTimestamp(context));

        actionDetailBlock.addChild(authBlock);
        actionDetailBlock.addChild(filterBlock);
        requestBodyBlock.addChild(actionDetailBlock);
        return requestBodyBlock;
    }

    public static XMLDataBlock getDataBlockSendMessage(final String toUsername, final String message, Context context) {
//        action Block
        XMLDataBlock requestBodyBlock = new XMLDataBlock("action", null, null);
        requestBodyBlock.setAttribute("id", UUID.randomUUID().toString());
        requestBodyBlock.setAttribute("name", "send-message");

//        action-detail block
        XMLDataBlock actionDetailBlock = new XMLDataBlock("action-detail", requestBodyBlock, null);

//        auth block
        XMLDataBlock authBlock = new XMLDataBlock("auth", actionDetailBlock, null);
        authBlock.setAttribute("username", MeegosPreferences.getUsername(context));
        authBlock.setAttribute("key", MeegosPreferences.getPassword(context));

//        message block
        XMLDataBlock messageBlock = new XMLDataBlock("message", actionDetailBlock, null);
        messageBlock.setAttribute("to", toUsername);
        messageBlock.addText(getCDATA(message));

        actionDetailBlock.addChild(authBlock);
        actionDetailBlock.addChild(messageBlock);
        requestBodyBlock.addChild(actionDetailBlock);
        return requestBodyBlock;
    }
}
