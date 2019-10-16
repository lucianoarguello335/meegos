package utn.tdm.meegos.activity;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import utn.tdm.meegos.R;
import utn.tdm.meegos.domain.Evento;
import utn.tdm.meegos.domain.Transaccion;
import utn.tdm.meegos.fragment.ChatContactListFragment;
import utn.tdm.meegos.fragment.TransactionListFragment;

public class TransactionActivity extends AppCompatActivity implements TransactionListFragment.TransactionFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_activity);
        Toolbar toolbar = findViewById(R.id.transaction_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.history_contact_menu, menu);
        // return true;
        return super.onCreateOptionsMenu(menu);
    }

//Listener del fragment
    @Override
    public void onListFragmentInteraction(Transaccion transaccion) {
        Toast.makeText(this, "onListFragmentInteraction Se elimino el transaccion " + transaccion.getRequestId(), Toast.LENGTH_SHORT).show();

    }
}
