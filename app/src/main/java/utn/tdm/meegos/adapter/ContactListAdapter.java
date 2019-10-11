package utn.tdm.meegos.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import utn.tdm.meegos.R;
import utn.tdm.meegos.activity.HistoryContactActivity;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.fragment.ContactListFragment;
import utn.tdm.meegos.fragment.LogInFragment;
import utn.tdm.meegos.fragment.dummy.DummyContent.DummyItem;
import utn.tdm.meegos.preferences.MeegosPreferences;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link ContactListFragment.OnContactListFragmentListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactHolder> {

    //    private final List<DummyItem> mValues;
    private final ArrayList<Contacto> contactos;
    private final ContactListFragment.OnContactListFragmentListener mListener;

    public ContactListAdapter(Context context, ArrayList<Contacto> contactos, ContactListFragment.OnContactListFragmentListener listener) {
        this.contactos = contactos;
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item_fragment, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactHolder holder, int position) {
        final Contacto contacto = contactos.get(position);

        holder.mBadge.assignContactUri(ContactsContract.Contacts.getLookupUri(
                contacto.getId(),
                contacto.getLookupKey()
        ));
        if (contacto.getPhotoThumbnail() == null) {
            holder.mBadge.setImageToDefault();
        } else {
            /*
             * Sets the image in the QuickContactBadge
             * QuickContactBadge inherits from ImageView, so
             */
            holder.mBadge.setImageBitmap(contacto.getPhotoThumbnail());
        }
//        holder.mPosition.setText(String.valueOf(position));

        //Cargo el nombre segun la Preferences si ordena por nombre o apellido.
        if (MeegosPreferences.getContactOrderBy(holder.mView.getContext()).equals("display_name")) {
            holder.mContentView.setText(contacto.getNombre());
        } else {
            holder.mContentView.setText(contacto.getApellido());
        }

        holder.historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(view.getContext(), HistoryContactActivity.class);

                Bundle bundle = new Bundle();
                bundle.putLong("contact_id", contacto.getId());
                bundle.putString("contact_lookup", contacto.getLookupKey());
                bundle.putString("contact_nombre", contacto.getNombre());

                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
         });

        holder.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.contacto);
//                }
//            }
//        });
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final QuickContactBadge mBadge;
//        public final TextView mPosition;
        public final TextView mContentView;
        public final ImageButton historyButton;
        public final ImageButton chatButton;

        public ContactHolder(View view) {
            super(view);
            mView = view;
            mBadge = (QuickContactBadge) view.findViewById(R.id.quickbadge);
//            mPosition = (TextView) view.findViewById(R.id.position);
            mContentView = (TextView) view.findViewById(R.id.contactName);
            historyButton = (ImageButton) view.findViewById(R.id.contactHistoryButton);
            chatButton = (ImageButton) view.findViewById(R.id.contactChatButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
