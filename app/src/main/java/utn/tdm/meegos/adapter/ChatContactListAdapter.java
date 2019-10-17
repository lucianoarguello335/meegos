package utn.tdm.meegos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import utn.tdm.meegos.R;
import utn.tdm.meegos.domain.Chat;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.listener.ChatListener;

public class ChatContactListAdapter extends RecyclerView.Adapter<ChatContactListAdapter.ViewHolder> {

    private List<Chat> chats;
    private final ChatListener chatListener;
    private Contacto contacto;

    public ChatContactListAdapter(Contacto contacto, List<Chat> chats, ChatListener chatListener) {
        this.contacto = contacto;
        this.chats = chats;
        this.chatListener = chatListener;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    @Override
    public int getItemViewType(int position) {
        return chats.get(position).getOrigen();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        if (viewType == Chat.ENVIADO) {
            view = layout.inflate(R.layout.chat_sent_item_fragment, parent, false);
        } else {
            view = layout.inflate(R.layout.chat_received_item_fragment, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Chat chat = chats.get(position);

        if (chat.getOrigen() == Chat.RECIBIDO) {
            holder.imageProfile.setImageBitmap(contacto.getPhotoThumbnail());
        }
        //holder.from.setText(chat.getFrom());
        holder.texto.setText(chat.getMessage());
        holder.timestamp.setText(chat.getTimestamp());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatListener.onChatDelete(chat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;

        public final ImageView imageProfile;
        public final TextView from;
        public final TextView texto;
        public final TextView timestamp;
        public final ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            imageProfile = view.findViewById(R.id.chat_image_profile);
            from = null; //view.findViewById(R.id.chat_from);
            texto = view.findViewById(R.id.chat_texto);
            timestamp = view.findViewById(R.id.chat_timestamp);
            deleteButton = view.findViewById(R.id.chat_deleteButton);
        }
    }
}
