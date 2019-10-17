package utn.tdm.meegos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import utn.tdm.meegos.R;
import utn.tdm.meegos.domain.Chat;
import utn.tdm.meegos.manager.ChatManager;
import utn.tdm.meegos.manager.ContactManager;
import utn.tdm.meegos.notifications.MeegosNotifications;
import utn.tdm.meegos.preferences.MeegosPreferences;
import utn.tdm.meegos.task.ServerTask;
import utn.tdm.meegos.util.Constants;
import utn.tdm.meegos.util.DateUtil;
import utn.tdm.meegos.util.XMLDataBlock;
import utn.tdm.meegos.util.XMLUtil;

public class ChatContactActivity extends AppCompatActivity {

    private ContactManager contactManager = new ContactManager(this);
    private ChatManager chatManager = new ChatManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_contact_activity);
        Toolbar toolbar = findViewById(R.id.chat_contact_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.chat_sender);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String networkStatus = MeegosPreferences.getNetworkStatus(view.getContext());
                String username = MeegosPreferences.getUsername(view.getContext());
//                Nos fijamos si hay conexion
                if (networkStatus.equals("1")) {
                    Bundle bundle = getIntent().getExtras();
                    String alias = contactManager.findContactByLookupKey(bundle.getString("lookupKey")).getAlias();
                    String message = ((EditText) findViewById(R.id.chat_etText)).getText().toString();

                    final Chat chat = new Chat(
                            DateUtil.getCalendarAsString(Calendar.getInstance(), Constants.CALENDAR_FORMAT_PATTERN),
                            MeegosPreferences.getUsername(view.getContext()),
                            alias,
                            Chat.ENVIADO,
                            message
                    );

//                    Armamos el request para enviar un mensaje
                    XMLDataBlock requestBodyBlock = XMLUtil.getDataBlockSendMessage(
                            alias,
                            message,
                            view.getContext()
                    );

                    new ServerTask(view.getContext(), view, new ServerTask.ServerListener() {
                        @Override
                        public void toDoOnSuccessPostExecute(XMLDataBlock responseXMLDataBlock) {
                            chatManager.saveChat(chat);
                            MeegosNotifications.messageResultNotification(view.getContext(), true, 0);

//                            ChatContactListFragment chatContactListFragment = new ChatContactListFragment();
//                            FragmentManager fm = getSupportFragmentManager();
//                            FragmentTransaction ft = fm.beginTransaction();
//                            ft.add(chatContactListFragment, null);
//                            ft.commit();
                        }
                    }).execute(requestBodyBlock);

                    ((EditText) findViewById(R.id.chat_etText)).setText("");
                } else {
//                    No esta conectado a la red
                    Snackbar.make(view, R.string.network_not_connected, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_contact_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                Bundle bundleSettings = new Bundle();
                bundleSettings.putInt("fragment", 3);
                intentSettings.putExtras(bundleSettings);
                startActivity(intentSettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
