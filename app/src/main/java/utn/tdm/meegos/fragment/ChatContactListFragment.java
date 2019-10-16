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
import utn.tdm.meegos.adapter.ChatContactListAdapter;
import utn.tdm.meegos.domain.Chat;
import utn.tdm.meegos.domain.Evento;
import utn.tdm.meegos.listener.OnListEventListener;
import utn.tdm.meegos.manager.ChatManager;
import utn.tdm.meegos.manager.ContactManager;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link ChatFragmentInteractionListener}
 * interface.
 */
public class ChatContactListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ChatFragmentInteractionListener mListener;

    private ChatManager chatManager;
    private ContactManager contactManager;
    private ChatContactListAdapter chatContactListAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatContactListFragment() {
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
            chatManager = new ChatManager(getContext());
            contactManager = new ContactManager(getContext());
            Bundle bundle = getActivity().getIntent().getExtras();

            chatContactListAdapter = new ChatContactListAdapter(
                    contactManager.findContactByLookupKey(bundle.getString("lookupKey")),
                    chatManager.findChatsByAlias(bundle.getString("alias")),
                    new ChatFragmentInteractionListener() {
                        @Override
                        public void onChatDelete(Chat chat) {
//                            chatContactListAdapter.delete
//                            int result = chatManager.deleteChat(chat);
//                            Toast.makeText(
//                                    getContext(),
//                                    "Result: " + result,
//                                    Toast.LENGTH_SHORT
//                            ).show();
//                            chatContactListAdapter.setChats(chatManager.findChatsByContact(
//                                    chat.getContactoId(),
//                                    chat.getContactoLookup(),
//                                    chat.getContactoNombre()
//                            ));
//                            chatContactListAdapter.notifyDataSetChanged();
                        }
                    }
            );

            recyclerView.setAdapter(chatContactListAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChatFragmentInteractionListener) {
            mListener = (ChatFragmentInteractionListener) context;
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
    public interface ChatFragmentInteractionListener {
        // TODO: Update argument type and name
        void onChatDelete(Chat chat);
    }
}
