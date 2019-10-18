package utn.tdm.meegos.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class MeegosPreferences {

    public MeegosPreferences() {
    }

    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getContactOrderBy(Context context) {
        return getPrefs(context).getString("preference_contact_order_by", "display_name");
    }

    public static String getContactOrderCriteria(Context context) {
        return getPrefs(context).getString("preference_contact_criteria", "ASC");
    }

    public static Boolean isContactHasPhoneNumberFiltered(Context context) {
        return getPrefs(context).getBoolean("preference_contact_has_phone_number", false);
    }

    public static String getHistoryOrder(Context context) {
        return getPrefs(context).getString("preference_history_order", "fecha ASC");
    }

    public static Boolean isHistoryCallFiltered(Context context) {
        return getPrefs(context).getBoolean("preference_history_call_filtered", true);
    }

    public static Boolean isHistorySMSFiltered(Context context) {
        return getPrefs(context).getBoolean("preference_history_sms_filtered", true);
    }

    public static String getChatOrder(Context context) {
        return getPrefs(context).getString("preference_chat_order", "_id ASC");
    }

    public static Boolean isChatSentFiltered(Context context) {
        return getPrefs(context).getBoolean("preference_chat_sent_filtered", true);
    }

    public static Boolean isChatReceivedFiltered(Context context) {
        return getPrefs(context).getBoolean("preference_chat_received_filtered", true);
    }

    public static String getNetworkStatus(Context context) {
        return getPrefs(context).getString("current_network_status", "-1");
    }

    public static void setNetworkStatus(Context context, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("current_network_status", value);
        editor.commit();
    }

    public static String getUsername(Context context) {
        return getPrefs(context).getString("chat_username", "");
    }

    public static void setUsername(Context context, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("chat_username", value);
        editor.commit();
    }

    public static String getPassword(Context context) {
        return getPrefs(context).getString("chat_password", "");
    }

    public static void setPassword(Context context, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("chat_password", value);
        editor.commit();
    }

    public static String getTimestamp(Context context) {
        return getPrefs(context).getString( "chat_timestamp", /* Fecha en que rendimos TDM */ "17/10/2019 18:00:00");
    }

    public static void setTimestamp(Context context, String timestamp) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("chat_timestamp", timestamp);
        editor.commit();
    }
}
