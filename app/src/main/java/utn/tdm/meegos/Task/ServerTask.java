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
import java.util.Calendar;

import utn.tdm.meegos.R;
import utn.tdm.meegos.database.MeegosSQLHelper;
import utn.tdm.meegos.domain.Transaccion;
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
    private Transaccion transaccion;
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
        this.transaccion = new Transaccion();
    }

    @Override
    protected XMLDataBlock doInBackground(XMLDataBlock... xmlDataBlocks) {
        XMLDataBlock requestBodyBlock = xmlDataBlocks[0];
//        Registramos transaccion
        transaccion.setRequestId(requestBodyBlock.getAttribute("id"));
        transaccion.setRequestName(requestBodyBlock.getAttribute("name"));
        transaccion.setFecha(Calendar.getInstance().getTimeInMillis());

        XMLDataBlock response = new XMLDataBlock("result", null, null);
        try {
            httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);

            OutputStream out = new BufferedOutputStream(httpsURLConnection.getOutputStream());
            out.write(requestBodyBlock.getBytes());
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
        registerRequest(xmlDataBlock);

        if (xmlDataBlock.getAttribute("type").equals("error")) {
            int Rid;
            switch (Integer.parseInt(xmlDataBlock.getChildBlock("detail").getAttribute("code"))) {
                case Constants.ERROR_MALFORMED_XML:
                    Rid = R.string.error_1;
                    break;
                case Constants.ERROR_MISSING_REQUEST_ID:
                    Rid = R.string.error_2;
                    break;
                case Constants.ERROR_MALFORMED_ACTION_REQUEST:
                    Rid = R.string.error_3;
                    break;
                case Constants.ERROR_MISSING_ACTION_PARAMETER:
                    Rid = R.string.error_4;
                    break;
                case Constants.ERROR_USERNAME_ALREADY_REGISTERED:
                    Rid = R.string.error_5;
                    break;
                case Constants.ERROR_MISSING_MESSAGE:
                    Rid = R.string.error_6;
                    break;
                case Constants.ERROR_INVALID_FILTER_TYPE:
                    Rid = R.string.error_7;
                    break;
                case Constants.ERROR_INVALID_ACTION:
                    Rid = R.string.error_8;
                    break;
                case Constants.ERROR_MISSING_ACTION:
                    Rid = R.string.error_9;
                    break;
                case Constants.MAX_PASSWORD_LENGTH:
                    Rid = R.string.error_10;
                    break;
                case Constants.ERROR_USERNAME_NOT_REGISTERED:
                    Rid = R.string.error_11;
                    break;
                case Constants.MAX_USERNAME_LENGTH:
                    Rid = R.string.error_12;
                    break;
                case Constants.ERROR_WRONG_PARAMETER_VALUE:
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

    private void registerRequest(XMLDataBlock xmlDataBlock) {
        String transactionType = xmlDataBlock.getAttribute("type");
        transaccion.setResponseType(transactionType);
        if (transactionType.equals("error")) {
            transaccion.setErrorCode(xmlDataBlock.getChildBlock("detail").getAttribute("code"));
        }
        new MeegosSQLHelper(context).insertTransaccion(transaccion);
    }

    /**
     * Interface para ejecutar cuando finaliza ok.
     */
    public interface ServerListener {
        void toDoOnSuccessPostExecute(XMLDataBlock responseXMLDataBlock);
    }
}
