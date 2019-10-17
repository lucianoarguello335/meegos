package utn.tdm.meegos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import utn.tdm.meegos.R;
import utn.tdm.meegos.adapter.TransactionListAdapter;
import utn.tdm.meegos.manager.TransaccionManager;

public class TransactionListFragment extends Fragment {

    private TransaccionManager transaccionManager;
    private TransactionListAdapter transactionListAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TransactionListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_list_fragment, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            transaccionManager = new TransaccionManager(getContext());
            transactionListAdapter = new TransactionListAdapter(transaccionManager.findAllTransacciones());
            recyclerView.setAdapter(transactionListAdapter);
        }
        return view;
    }
}
