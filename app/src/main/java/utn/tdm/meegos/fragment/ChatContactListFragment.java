package utn.tdm.meegos.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import utn.tdm.meegos.R;
import utn.tdm.meegos.adapter.ChatContactListAdapter;
import utn.tdm.meegos.domain.Chat;
import utn.tdm.meegos.listener.ChatListener;
import utn.tdm.meegos.listener.ListListener;
import utn.tdm.meegos.manager.ChatManager;
import utn.tdm.meegos.manager.ContactManager;
import utn.tdm.meegos.preferences.MeegosPreferences;

public class ChatContactListFragment extends Fragment implements ListListener {

    private RecyclerView recyclerView;
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
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            chatManager = new ChatManager(getContext());
            contactManager = new ContactManager(getContext());
            final Bundle bundle = getActivity().getIntent().getExtras();

            chatContactListAdapter = new ChatContactListAdapter(
                    contactManager.findContactByLookupKey(bundle.getString("lookupKey")),
                    chatManager.findChatsByAlias(bundle.getString("alias")),
                    new ChatListener() {
                        @Override
                        public int onChatDelete(Chat chat) {
                            if (chatManager.deleteChat(chat) > 0) {
                                Toast.makeText(getContext(), R.string.deleted_success, Toast.LENGTH_LONG).show();
                            }else{
                                Snackbar.make(getView(), R.string.deleted_failed, Snackbar.LENGTH_LONG).show();
                            }
                            Bundle bundle = getActivity().getIntent().getExtras();
                            if (bundle != null) {
                                chatContactListAdapter.setChats(chatManager.findChatsByAlias(bundle.getString("alias")));
                                chatContactListAdapter.notifyDataSetChanged();
                                return 1;
                            }
                            return 0;
                        }
                    }
            );
            recyclerView.setAdapter(chatContactListAdapter);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onListUpdate();
    }

    @Override
    public void onListUpdate() {
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            ArrayList<Chat> chats = chatManager.findChatsByAlias(bundle.getString("alias"));
            chatContactListAdapter.setChats(chats);
            chatContactListAdapter.notifyDataSetChanged();
            if(MeegosPreferences.getChatOrder(recyclerView.getContext()).equals("_id ASC")) {
                recyclerView.smoothScrollToPosition(chats.size()-1);
            } else {
                recyclerView.smoothScrollToPosition(0);
            }
        }

    }
}
