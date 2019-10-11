package utn.tdm.meegos.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
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

import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

import utn.tdm.meegos.R;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.fragment.ContactListFragment;
import utn.tdm.meegos.fragment.LogInFragment;
import utn.tdm.meegos.preferences.MeegosPreferences;
import utn.tdm.meegos.receiver.NetworkStatusReceiver;
import utn.tdm.meegos.service.MessageService;

public class ContactActivity extends AppCompatActivity implements ContactListFragment.OnContactListFragmentListener, LogInFragment.OnFragmentInteractionListener {

    public static final String[] MEEGOS_PERMISOS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE
    };

    private Timer serviceTimer;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            boolean allPermission = true;
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, ContactActivity.MEEGOS_PERMISOS, 1);
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

        /**
         * Las apps que se orientan a Android 7.0 (nivel de API 24) y versiones posteriores
         * no reciben emisiones de CONNECTIVITY_ACTION si especifican el receptor de emisión en su
         * manifiesto. De igual manera, las apps recibirán emisiones de CONNECTIVITY_ACTION si
         * registran su BroadcastReceiver con el Context.registerReceiver() y ese contexto
         * sigue siendo válido.
         * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
         */
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        getApplicationContext().registerReceiver(new NetworkStatusReceiver(), intentFilter);

        // TODO: ELIMINAR
        MeegosPreferences.setUsername(this, "");
        MeegosPreferences.setPassword(this, "");

//        Corremos el servicio para obtener los mensajes web
        runMessageService();
    }

    public void runMessageService() {
        serviceTimer = new Timer();
        serviceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                startService(new Intent(getApplicationContext(), MessageService.class));
            }
        }, 0, 15000);
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
                String networkStatus = MeegosPreferences.getNetworkStatus(this);
                String username = MeegosPreferences.getUsername(this);
                if (networkStatus.equals("1")) {
                    if (!username.isEmpty()) {
                        startActivity(new Intent(this, ChatContactActivity.class));
                    } else {
                        LogInFragment lf = new LogInFragment();
                        lf.show(getSupportFragmentManager(), "LogInFragment");
                    }
                } else {
                    // TODO: si no esta conectado.
                }
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("fragment", 1);
                intent.putExtras(bundle);
                startActivity(intent);
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
    public void onChatInteraction(Contacto contacto, View view) {
        String networkStatus = MeegosPreferences.getNetworkStatus(this);
        String username = MeegosPreferences.getUsername(this);
//        Nos fijamos si hay conexion
        if (networkStatus.equals("1")) {
//            Nos fijamos si el usuario esta registrado
            if (!username.isEmpty()) {
//                Nos fijamos si el contacto tiene un alias
                if (!contacto.getAlias().isEmpty()) {
//                    TODO: IR al CHAT
//                    startActivity(new Intent(this, ChatContactActivity.class));
                    Snackbar.make(view, "IR AL CHAT", Snackbar.LENGTH_LONG).show();
                } else {
//                    El contacto no tiene un alias
//                    TODO: Crear fragment para registrar alias
                    Snackbar.make(view, R.string.error_1, Snackbar.LENGTH_LONG).show();
                }
            } else {
//                No tiene un usuario registrado
                LogInFragment lf = new LogInFragment();
                lf.show(getSupportFragmentManager(), "LogInFragment");
            }
        } else {
//            No esta conectado a la red
            Snackbar.make(view, R.string.network_not_connected, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onDestroy() {
//        Detenemos el servicio
        serviceTimer.cancel();
        super.onDestroy();
    }
}
