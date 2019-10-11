package utn.tdm.meegos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import utn.tdm.meegos.R;
import utn.tdm.meegos.adapter.HistoryContactListAdapter;
import utn.tdm.meegos.domain.Evento;
import utn.tdm.meegos.listener.OnListEventListener;
import utn.tdm.meegos.manager.EventoManager;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HistoryContactListFragment extends Fragment implements OnListEventListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private OnListEventListener onListEventListener;

    private EventoManager eventoManager;
    private HistoryContactListAdapter historyContactListAdapter;
    private Bundle contactBundle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistoryContactListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HistoryContactListFragment newInstance(int columnCount) {
        HistoryContactListFragment fragment = new HistoryContactListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_contact_list_fragment, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            eventoManager = new EventoManager(getContext());
            contactBundle = getActivity().getIntent().getExtras();

            historyContactListAdapter = new HistoryContactListAdapter(
                    eventoManager.findEventosByContact(
                            contactBundle.getLong("contact_id"),
                            contactBundle.getString("contact_lookup"),
                            contactBundle.getString("contact_nombre")
                    ),
                    new OnListEventListener() {
                        @Override
                        public void onDeleteEvent(Evento evento) {
//                            Eliminamos el evento
                            int result = eventoManager.deleteEvento(evento);
//                            Notificamos el resultado
                            if (result > 0) {
                                Toast.makeText(getContext(), R.string.deleted_success, Toast.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(getView(), R.string.deleted_failed, Snackbar.LENGTH_LONG).show();
                            }
//                            Actualizamos la coleccion del adapter
                            historyContactListAdapter.setEventos(eventoManager.findEventosByContact(
                                    evento.getContactoId(),
                                    evento.getContactoLookup(),
                                    evento.getContactoNombre()
                            ));
//                            ejecutamos el notifyDataSetChanged
                            historyContactListAdapter.notifyDataSetChanged();
                        }
                    }
            );

            recyclerView.setAdapter(historyContactListAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDeleteEvent(Evento evento) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (contactBundle != null) {
            historyContactListAdapter.setEventos(eventoManager.findEventosByContact(
                    contactBundle.getLong("contact_id"),
                    contactBundle.getString("contact_lookup"),
                    contactBundle.getString("contact_nombre")
            ));
            historyContactListAdapter.notifyDataSetChanged();
        }
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Evento evento);
    }
}
