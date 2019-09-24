package utn.tdm.meegos.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import utn.tdm.meegos.R;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.fragment.ContactListFragment;
import utn.tdm.meegos.fragment.LogInFragment;

public class ContactActivity extends AppCompatActivity implements ContactListFragment.OnListFragmentInteractionListener, LogInFragment.OnFragmentInteractionListener {

    public static final String[] MEEGOS_PERMISOS = {
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_PHONE_NUMBERS,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.WRITE_CALL_LOG,
        Manifest.permission.INTERNET
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean allPermission = true;
            for (int i=0; i < permissions.length; i++) {
                if(ContextCompat.checkSelfPermission(this, permissions[i]) == PackageManager.PERMISSION_DENIED){
                    this.enforceCallingPermission(permissions[i], permissions[i]);
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
        switch (item.getItemId()) {
            case R.id.action_chat:
                // TODO: Esta el usuario logueado?
                    // TODO: Si esta logueado ir al activity de chat
                if(true) {
                    startActivity(new Intent(this, ChatContactActivity.class));
                } else {
                    LogInFragment lf = new LogInFragment();
                    lf.show(getSupportFragmentManager(), "LogInFragment");
                }
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Alert Dialog");
//                builder.setMessage("Alert Dialog inside DialogFragment");
//
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//                builder.create();

//                Toast.makeText(getApplicationContext(), "action_chat", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "action_settings", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
//        return super.onOptionsItemSelected(item);
    }

    private MenuItem.OnMenuItemClickListener startChat = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            Toast.makeText(getApplicationContext(), "CHAT", Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    public boolean startChat222(View v) {
        Toast.makeText(this, "CHAT", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onListFragmentInteraction(Contacto contacto) {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
