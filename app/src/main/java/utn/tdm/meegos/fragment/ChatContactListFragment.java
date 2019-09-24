package utn.tdm.meegos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
public class ChatContactListFragment extends Fragment implements OnListEventListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private OnListEventListener onListEventListener;

    private EventoManager eventoManager;
    private HistoryContactListAdapter historyContactListAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatContactListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ChatContactListFragment newInstance(int columnCount) {
        ChatContactListFragment fragment = new ChatContactListFragment();
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
        View view = inflater.inflate(R.layout.chat_contact_list_fragment, container, false);

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
            Bundle bundle = getActivity().getIntent().getExtras();

            historyContactListAdapter = new HistoryContactListAdapter(
                    eventoManager.findEventosByContact(
                            bundle.getLong("contact_id"),
                            bundle.getString("contact_lookup"),
                            bundle.getString("contact_nombre")
                    ),
                    new OnListEventListener() {
                        @Override
                        public void onDeleteEvent(Evento evento) {
                            // TODO: Hacer un DialogFragment para confirmar operacin
                            int result = eventoManager.deleteEvento(evento);
                            Toast.makeText(
                                    getContext(),
                                    "Result: " + result,
                                    Toast.LENGTH_SHORT
                            ).show();
                            historyContactListAdapter.setEventos(eventoManager.findEventosByContact(
                                    evento.getContactoId(),
                                    evento.getContactoLookup(),
                                    evento.getContactoNombre()
                            ));
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Evento evento);
    }
}
