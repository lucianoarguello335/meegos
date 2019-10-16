package utn.tdm.meegos.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import utn.tdm.meegos.R;
import utn.tdm.meegos.domain.Chat;
import utn.tdm.meegos.domain.Evento;
import utn.tdm.meegos.fragment.ChatContactListFragment;

public class ChatContactActivity extends AppCompatActivity implements ChatContactListFragment.ChatFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_contact_activity);
        Toolbar toolbar = findViewById(R.id.chat_contact_toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


//        ContactListFragment contactFragment = new ContactListFragment();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.add(contactFragment, "EL SUper TAG");
//        ft.commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_contact_menu, menu);
        return true;
    }

//Listener del fragment
    @Override
    public void onChatDelete(Chat chat) {
        Toast.makeText(this, "onListFragmentInteraction Se elimino el evento " + chat.getId(), Toast.LENGTH_SHORT).show();
    }
}
