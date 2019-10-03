package utn.tdm.meegos.Task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

    private HttpsURLConnection httpsURLConnection;
    private ServerListener serverListener;
    private Context context;

    public ServerTask(Context context, ServerListener serverListener) {
        this.context = context;
        this.serverListener = serverListener;
    }

    @Override
    protected XMLDataBlock doInBackground(XMLDataBlock... requestBodyBlock) {
        XMLDataBlock response = null;
        try {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
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

            in.close();
            return customXMLParser.getDataBlock();
        } catch (Exception e) {
            e.printStackTrace();
            response.setAttribute("type", "exception");
            response.setAttribute("message", e.getMessage());
        } finally {
            httpsURLConnection.disconnect();
        }
        return response;
    }

    @Override
    protected void onPostExecute(XMLDataBlock xmlDataBlock) {
        super.onPostExecute(xmlDataBlock);

        if (xmlDataBlock.getAttribute("type").equals("error")){
            int Rid;
            switch (xmlDataBlock.getChildBlock("detail").getAttribute("code")){
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
            Toast.makeText(context, Rid, Toast.LENGTH_LONG).show();
        } else if (xmlDataBlock.getAttribute("type").equals("success")) {
            if (serverListener == null) {
                Toast.makeText(context, "Error: serverListener == null", Toast.LENGTH_LONG).show();
            } else {
                serverListener.toDoOnSuccessPostExecute(xmlDataBlock);
            }
        } else if (xmlDataBlock.getAttribute("type").equals("exception")) {
            Toast.makeText(context, xmlDataBlock.getAttribute("message"), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Interface para ejecutar cuando finaliza ok.
     */
    public interface ServerListener {
        void toDoOnSuccessPostExecute(XMLDataBlock xmlDataBlock);
    }
}
