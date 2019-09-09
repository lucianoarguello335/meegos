package utn.tdm.meegos.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import utn.tdm.meegos.R;
import utn.tdm.meegos.fragment.ContactListFragment;
import utn.tdm.meegos.fragment.dummy.DummyContent;

public class HistoryContactActivity extends AppCompatActivity implements ContactListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_contact_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        getMenuInflater().inflate(R.menu.history_contact_menu, menu);
        return true;
    }


//metodo de fragment


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
