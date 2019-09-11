package utn.tdm.meegos.adapter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import utn.tdm.meegos.R;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.fragment.ContactListFragment.OnListFragmentInteractionListener;
import utn.tdm.meegos.fragment.dummy.DummyContent.DummyItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactoHolder> {

    //    private final List<DummyItem> mValues;
    private final ArrayList<Contacto> contactos;
    private final OnListFragmentInteractionListener mListener;

    public ContactListAdapter(Context context, ArrayList<Contacto> contactos, OnListFragmentInteractionListener listener) {
        this.contactos = contactos;
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    @Override
    public ContactoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item_fragment, parent, false);
        return new ContactoHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactoHolder holder, int position) {
        Contacto contacto = contactos.get(position);

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
        holder.mContentView.setText(contacto.getNombre());

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

    public class ContactoHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final QuickContactBadge mBadge;
//        public final TextView mPosition;
        public final TextView mContentView;

        public ContactoHolder(View view) {
            super(view);
            mView = view;
            mBadge = (QuickContactBadge) view.findViewById(R.id.quickbadge);
//            mPosition = (TextView) view.findViewById(R.id.position);
            mContentView = (TextView) view.findViewById(R.id.contactName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
