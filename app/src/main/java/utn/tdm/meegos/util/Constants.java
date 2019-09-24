package utn.tdm.meegos.util;

public class Constants
{
    public static final int ERROR_MALFORMED_XML = 1;
    public static final int ERROR_MISSING_REQUEST_ID = 2;
    public static final int ERROR_MALFORMED_ACTION_REQUEST = 3;
    public static final int ERROR_MISSING_ACTION_PARAMETER = 4;
    public static final int ERROR_USERNAME_ALREADY_REGISTERED = 5;
    public static final int ERROR_MISSING_MESSAGE = 6;
    public static final int ERROR_INVALID_FILTER_TYPE = 7;
    public static final int ERROR_INVALID_ACTION = 8;
    public static final int ERROR_MISSING_ACTION = 9;
    public static final int ERROR_INVALID_HTTP_METHOD_REQUESTED = 10;
    public static final int ERROR_USERNAME_NOT_REGISTERED = 11;
    public static final int ERROR_AUTHENTICATION_FAIL = 12;
    public static final int ERROR_WRONG_PARAMETER_VALUE = 13;
    public static final int MIN_USERNAME_LENGTH = 5;
    public static final int MAX_USERNAME_LENGTH = 12;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 10;
    public static final int MIN_MESSAGE_LENGTH = 1;
    public static final int MAX_MESSAGE_LENGTH = 200;
    public static final String CALENDAR_FORMAT_PATTERN = "dd/MM/yyyy HH:mm:ss";
    public static final String SERVER_URL = "https://meegosmessagesender.herokuapp.com/MessageSender";
}
