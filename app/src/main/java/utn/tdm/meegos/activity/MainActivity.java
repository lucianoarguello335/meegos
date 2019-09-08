package utn.tdm.meegos.activity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import utn.tdm.meegos.R;
import utn.tdm.meegos.adapter.ContactListAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String[] MEEGOS_PERMISOS = {
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_PHONE_NUMBERS,
        Manifest.permission.READ_CALL_LOG
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("MainActivity: ", "Entro al onCreate");


//        Cuando usamos emuladores de android no nos pide los permisos
//        Por eso si no los tiene los pedimos en tiempo de ejecucion
        ActivityCompat.requestPermissions(this, MEEGOS_PERMISOS, 0);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("PERMISSION_GRANTED", "FALSE");
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
//                Log.d("PermissionDenied", "TRUE");
//            } else {
//                Log.d("PermissionDenied", "FALSE");
//            }
//        } else {
//            Log.d("PERMISSION_GRANTED", "TRUE");
//        }

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        final ListView contactListView = findViewById(R.id.contact_list);
        contactListView.setAdapter(new ContactListAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



//Metodos del ListActivity
    protected void onListItemClick(ListView l, View v, int position, long id) {
        throw new RuntimeException("Stub!");
    }

    protected void onRestoreInstanceState(Bundle state) {
        throw new RuntimeException("Stub!");
    }

    protected void onDestroy() {
        super.onDestroy();
        throw new RuntimeException("Stub!");
    }

    public void onContentChanged() {
        Log.d("MainActivity","onContentChanged");
//        throw new RuntimeException("Stub!");
    }

    public void setListAdapter(ListAdapter adapter) {
        throw new RuntimeException("Stub!");
    }

    public void setSelection(int position) {
        throw new RuntimeException("Stub!");
    }

    public int getSelectedItemPosition() {
        throw new RuntimeException("Stub!");
    }

    public long getSelectedItemId() {
        throw new RuntimeException("Stub!");
    }

    public ListView getListView() {
        throw new RuntimeException("Stub!");
    }

    public ListAdapter getListAdapter() {
        throw new RuntimeException("Stub!");
    }

}
