package utn.tdm.meegos.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MeegosPreferences {
    private SharedPreferences sharedPreferences;
    private static String PREF_NAME = "MeegosPreferences";

    public MeegosPreferences() {
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getContactOrderAttribute(Context context) {
        return getPrefs(context).getString("contact_order_attribute", "first_name");
    }

    public static void setContactOrderAttribute(Context context, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("contact_order_attribute", value);
        editor.commit();
    }

    public static String getContactOrderCriteria(Context context) {
        return getPrefs(context).getString("contact_order_criteria", "ASC");
    }

    public static void setContactOrderCriteria(Context context, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("contact_order_criteria", value);
        editor.commit();
    }

    public static String getContactFilter(Context context) {
        return getPrefs(context).getString("contact_filter", "ALL");
    }

    public static void setContactFilter(Context context, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("contact_filter", value);
        editor.commit();
    }

    public static String getRecordFilter(Context context) {
        return getPrefs(context).getString("contact_record_filter", "ALL");
    }

    public static void setRecordFilter(Context context, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("contact_record_filter", value);
        editor.commit();
    }

    public static String getRecordOrder(Context context) {
        return getPrefs(context).getString("contact_record_order", "ASC");
    }

    public static void setRecordOrder(Context context, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("contact_record_order", value);
        editor.commit();
    }

    public static String getWebMsgFilter(Context context) {
        return getPrefs(context).getString("web_msg_filter", "ALL");
    }

    public static void setWebMsgFilter(Context context, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("web_msg_filter", value);
        editor.commit();
    }

    public static String getWebMsgOrder(Context context) {
        return getPrefs(context).getString("web_msg_order", "ASC");
    }

    public static void setWebMsgOrder(Context context, String value) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("web_msg_order", value);
        editor.commit();
    }
}