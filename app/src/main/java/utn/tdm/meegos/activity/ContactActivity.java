package utn.tdm.meegos.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import utn.tdm.meegos.R;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.fragment.ContactListFragment;

public class ContactActivity extends AppCompatActivity implements ContactListFragment.OnListFragmentInteractionListener {

    public static final String[] MEEGOS_PERMISOS = {
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_PHONE_NUMBERS,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.WRITE_CALL_LOG
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean allPermission = true;
            for (int i=0; i < permissions.length; i++) {
                if(ContextCompat.checkSelfPermission(this, permissions[i]) == PackageManager.PERMISSION_DENIED){
                    allPermission = false;
                }
            }
            if (allPermission) {
                ContactListFragment contactListFragment = (ContactListFragment) getSupportFragmentManager().getFragments().get(0);
                contactListFragment.onPermissionsAccepted();
            } else {
                ActivityCompat.requestPermissions(this, ContactActivity.MEEGOS_PERMISOS, 1);
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this, ContactActivity.MEEGOS_PERMISOS, 1);


//        Cuando usamos emuladores de android no nos pide los permisos
//        Por eso si no los tiene los pedimos en tiempo de ejecucion
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_menu, menu);
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

    @Override
    public void onListFragmentInteraction(Contacto contacto) {

    }
}
