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
import utn.tdm.meegos.adapter.ContactListAdapter;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.manager.ContactManager;

public class ContactListFragment extends Fragment {

    private OnContactListFragmentListener mListener;

    private RecyclerView recyclerView;
    private ContactManager contactManager;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_list_fragment, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            contactManager = new ContactManager(getContext());
        }
        return view;
    }

    public void onPermissionsAccepted() {
        recyclerView.setAdapter(new ContactListAdapter(getContext(), contactManager.findAllContacts(), (OnContactListFragmentListener) getActivity()));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactListFragmentListener) {
            mListener = (OnContactListFragmentListener) context;
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
    public void onResume() {
        super.onResume();
        if (recyclerView.getAdapter() != null) {
            recyclerView.setAdapter(new ContactListAdapter(getContext(), contactManager.findAllContacts(), (OnContactListFragmentListener) getActivity()));
        }
    }

    public interface OnContactListFragmentListener {
        void onChatInteraction(Contacto contacto, View view);
    }
}
