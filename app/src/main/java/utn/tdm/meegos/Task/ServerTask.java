package utn.tdm.meegos.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import utn.tdm.meegos.R;
import utn.tdm.meegos.util.Constants;
import utn.tdm.meegos.util.CustomXMLParser;
import utn.tdm.meegos.util.XMLDataBlock;

public class ServerTask extends AsyncTask<XMLDataBlock, Void, XMLDataBlock> {
    private URL url;
    {
        try {
            url = new URL(Constants.SERVER_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

//    private HttpsURLConnection httpsURLConnection;
    private HttpURLConnection httpsURLConnection;
    private ServerListener serverListener;
    private Context context;
    private View view;

    /**
     *
     * @param context
     * @param view if view is null, error messages will print in console, othewise will run a Snackbar
     * @param serverListener
     */
    public ServerTask(Context context, View view, ServerListener serverListener) {
        this.context = context;
        this.view = view;
        this.serverListener = serverListener;
    }

    @Override
    protected XMLDataBlock doInBackground(XMLDataBlock... requestBodyBlock) {
        XMLDataBlock response = new XMLDataBlock("result", null, null);
        try {
            httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(httpsURLConnection.getOutputStream());
            out.write(requestBodyBlock[0].getBytes());
            out.close();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpsURLConnection.getInputStream())
            );

            String requestResponse = "", s = null;
            while ((s = in.readLine()) != null) {
                requestResponse += s;
            }
            CustomXMLParser customXMLParser = new CustomXMLParser();
            customXMLParser.parse(requestResponse);
            response = customXMLParser.getDataBlock();

            in.close();
            return customXMLParser.getDataBlock();
        } catch (Exception e) {
            e.printStackTrace();
            response.setAttribute("type", "exception");
            response.setAttribute("message", e.getMessage());
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(XMLDataBlock xmlDataBlock) {
        super.onPostExecute(xmlDataBlock);

        if (xmlDataBlock.getAttribute("type").equals("error")) {
            int Rid;
            switch (xmlDataBlock.getChildBlock("detail").getAttribute("code")) {
                case "1":
                    Rid = R.string.error_1;
                    break;
                case "2":
                    Rid = R.string.error_2;
                    break;
                case "3":
                    Rid = R.string.error_3;
                    break;
                case "4":
                    Rid = R.string.error_4;
                    break;
                case "5":
                    Rid = R.string.error_5;
                    break;
                case "6":
                    Rid = R.string.error_6;
                    break;
                case "7":
                    Rid = R.string.error_7;
                    break;
                case "8":
                    Rid = R.string.error_8;
                    break;
                case "9":
                    Rid = R.string.error_9;
                    break;
                case "10":
                    Rid = R.string.error_10;
                    break;
                case "11":
                    Rid = R.string.error_11;
                    break;
                case "12":
                    Rid = R.string.error_12;
                    break;
                case "13":
                    Rid = R.string.error_13;
                    break;
                default:
                    Rid = R.string.error_default;
            }
            if (view == null){
                Log.e("onPostExecuteError", context.getString(Rid));
            } else {
                Snackbar.make(view, Rid, Snackbar.LENGTH_LONG).show();
            }
        } else if (xmlDataBlock.getAttribute("type").equals("exception")) {
            if (view == null){
                Log.e("onPostExecuteException", xmlDataBlock.getAttribute("message"));
            } else {
                Snackbar.make(view, xmlDataBlock.getAttribute("message"), Snackbar.LENGTH_LONG).show();
            }
        } else if (xmlDataBlock.getAttribute("type").equals("success")) {
            if (serverListener == null) {
                Snackbar.make(view, R.string.server_listener_null, Snackbar.LENGTH_LONG).show();
            } else {
                serverListener.toDoOnSuccessPostExecute(xmlDataBlock);
            }
        }
    }

    /**
     * Interface para ejecutar cuando finaliza ok.
     */
    public interface ServerListener {
        void toDoOnSuccessPostExecute(XMLDataBlock responseXMLDataBlock);
    }
}
