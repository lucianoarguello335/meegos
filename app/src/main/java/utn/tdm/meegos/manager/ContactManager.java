package utn.tdm.meegos.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import utn.tdm.meegos.database.MeegosSQLHelper;
import utn.tdm.meegos.domain.Contacto;
import utn.tdm.meegos.preferences.MeegosPreferences;

public class ContactManager {
    private final Context context;
    private final MeegosSQLHelper meegosSQLHelper;

    public ContactManager(Context context) {
        this.context = context;
        meegosSQLHelper = new MeegosSQLHelper(context);
    }

    public ArrayList<Contacto> findAllContacts() {
        ArrayList<Contacto> contactos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        String selection = MeegosPreferences.isContactHasPhoneNumberFiltered(context)
            ? (ContactsContract.Contacts.HAS_PHONE_NUMBER + " = 1")
            : "";
        Cursor cursor = cr.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                selection,
                null,
                MeegosPreferences.getContactOrderBy(context)
                    + " "
                    + MeegosPreferences.getContactOrderCriteria(context)
        );

        while (cursor.moveToNext()) {
            String alias = "";
            Cursor c = meegosSQLHelper.getAliasByContactLookupKey(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)));
            if (c.moveToFirst()) {
                alias = c.getString(1);
            }

            /*
             * Assuming the current Cursor position is the contact you want, gets the thumbnail ID
             */
            String thumbnailUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
            contactos.add( new Contacto(
                    cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE)),
                    alias,
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
                    "",
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
