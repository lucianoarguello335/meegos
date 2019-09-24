package utn.tdm.meegos.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import utn.tdm.meegos.domain.Contacto;

public class ContactManager {
    private final Context context;

    public ContactManager(Context context) {
        this.context = context;
    }

    public ArrayList<Contacto> findAllContacts() {
        ArrayList<Contacto> contactos = new ArrayList<Contacto>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null //MeegosPreferences.getContactOrderCriteria(context)
        );

        while (cursor.moveToNext()) {
            Log.d("last_time_contacted: ", ""+cursor.getString(cursor.getColumnIndex("last_time_contacted")));
            Log.d("phonetic_name: ", ""+cursor.getString(cursor.getColumnIndex("phonetic_name")));
            Log.d("custom_ringtone: ", ""+cursor.getString(cursor.getColumnIndex("custom_ringtone")));
            Log.d("contact_status_ts: ", ""+cursor.getString(cursor.getColumnIndex("contact_status_ts")));
            Log.d("pinned: ", ""+cursor.getString(cursor.getColumnIndex("pinned")));
            Log.d("photo_id: ", ""+cursor.getString(cursor.getColumnIndex("photo_id")));
            Log.d("photo_file_id: ", ""+cursor.getString(cursor.getColumnIndex("photo_file_id")));
            Log.d("c_s_res_package: ", ""+cursor.getString(cursor.getColumnIndex("contact_status_res_package")));
            Log.d("c_c_capability: ", ""+cursor.getString(cursor.getColumnIndex("contact_chat_capability")));
            Log.d("contact_status_icon: ", ""+cursor.getString(cursor.getColumnIndex("contact_status_icon")));
            Log.d("display_name_alt: ", ""+cursor.getString(cursor.getColumnIndex("display_name_alt")));
            Log.d("sort_key_alt: ", ""+cursor.getString(cursor.getColumnIndex("sort_key_alt")));
            Log.d("in_visible_group: ", ""+cursor.getString(cursor.getColumnIndex("in_visible_group")));
            Log.d("starred: ", ""+cursor.getString(cursor.getColumnIndex("starred")));
            Log.d("contact_status_label: ", ""+cursor.getString(cursor.getColumnIndex("contact_status_label")));
            Log.d("phonebook_label: ", ""+cursor.getString(cursor.getColumnIndex("phonebook_label")));
            Log.d("is_user_profile: ", ""+cursor.getString(cursor.getColumnIndex("is_user_profile")));
            Log.d("has_phone_number: ", ""+cursor.getString(cursor.getColumnIndex("has_phone_number")));
            Log.d("display_name_source: ", ""+cursor.getString(cursor.getColumnIndex("display_name_source")));
            Log.d("phonetic_name_style: ", ""+cursor.getString(cursor.getColumnIndex("phonetic_name_style")));
            Log.d("send_to_voicemail: ", ""+cursor.getString(cursor.getColumnIndex("send_to_voicemail")));
            Log.d("lookup: ", ""+cursor.getString(cursor.getColumnIndex("lookup")));
            Log.d("phonebook_label_alt: ", ""+cursor.getString(cursor.getColumnIndex("phonebook_label_alt")));
            Log.d("c_l_updated_timestamp: ", ""+cursor.getString(cursor.getColumnIndex("contact_last_updated_timestamp")));
            Log.d("photo_uri: ", ""+cursor.getString(cursor.getColumnIndex("photo_uri")));
            Log.d("phonebook_bucket: ", ""+cursor.getString(cursor.getColumnIndex("phonebook_bucket")));
            Log.d("contact_status: ", ""+cursor.getString(cursor.getColumnIndex("contact_status")));
            Log.d("display_name: ", ""+cursor.getString(cursor.getColumnIndex("display_name")));
            Log.d("sort_key: ", ""+cursor.getString(cursor.getColumnIndex("sort_key")));
            Log.d("photo_thumb_uri: ", ""+cursor.getString(cursor.getColumnIndex("photo_thumb_uri")));
            Log.d("contact_presence: ", ""+cursor.getString(cursor.getColumnIndex("contact_presence")));
            Log.d("in_default_directory: ", ""+cursor.getString(cursor.getColumnIndex("in_default_directory")));
            Log.d("times_contacted: ", ""+cursor.getString(cursor.getColumnIndex("times_contacted")));
            Log.d("_id: ", ""+cursor.getString(cursor.getColumnIndex("_id")));
            Log.d("name_raw_contact_id: ", ""+cursor.getString(cursor.getColumnIndex("name_raw_contact_id")));
            Log.d("phonebook_bucket_alt: ", ""+cursor.getString(cursor.getColumnIndex("phonebook_bucket_alt")));

            /*
             * Assuming the current Cursor position is the contact you want, gets the thumbnail ID
             */
            String thumbnailUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
            contactos.add( new Contacto(
                    cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE)),
                    (thumbnailUri != null ? loadContactPhotoThumbnail(thumbnailUri) : null)
                )
            );
        }

        cursor.close();
        return contactos;
    }

    public Contacto findContactByIdAndLookupKey(Long contact_id, String contact_lookup_key) {
        ArrayList<Contacto> contactos = new ArrayList<Contacto>();
        ContentResolver cr = context.getContentResolver();



        return null;
    }

    public Contacto findContactByPhoneNumbre(String phoneNumber) {
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(
                lookupUri,
                null,
                null,
                null,
                null
        );

        Contacto contacto = null;

        if(cursor.getCount() == 0) {
            return null;
        } else {
            cursor.moveToFirst();
            /*
             * Assuming the current Cursor position is the contact you want, gets the thumbnail ID
             */
            String thumbnailUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
            contacto = new Contacto(
                    cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE)),
                    (thumbnailUri != null ? loadContactPhotoThumbnail(thumbnailUri) : null)
            );

            cursor.close();
            return contacto;
        }
    }

    /**
     * Load a contact photo thumbnail and return it as a Bitmap, resizing the image to the provided image dimensions as needed.
     * @param photoData the value of PHOTO_THUMBNAIL_URI.
     * @return A thumbnail Bitmap, sized to the provided width and height. Returns null if the thumbnail is not found.
     */
    private Bitmap loadContactPhotoThumbnail(String photoData) {
        // Creates an asset file descriptor for the thumbnail file.
        AssetFileDescriptor afd = null;
        // try-catch block for file not found
        try {
            // Creates a holder for the URI.
            Uri thumbUri = Uri.parse(photoData);
            /*
             * Retrieves an AssetFileDescriptor object for the thumbnail
             * URI
             * using ContentResolver.openAssetFileDescriptor
             */
            afd = context.getContentResolver().openAssetFileDescriptor(thumbUri, "r");
            /*
             * Gets a file descriptor from the asset file descriptor.
             * This object can be used across processes.
             */
            FileDescriptor fileDescriptor = afd.getFileDescriptor();
            // Decode the photo file and return the result as a Bitmap
            // If the file descriptor is valid
            if (fileDescriptor != null) {
                // Decodes the bitmap
                return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null);
            }
            // If the file isn't found
        } catch (FileNotFoundException e) {
            /*
             * Handle file not found errors
             */
            // In all cases, close the asset file descriptor
        } finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {}
            }
        }
        return null;
    }
}
