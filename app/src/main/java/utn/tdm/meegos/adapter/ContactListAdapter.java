package utn.tdm.meegos.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import utn.tdm.meegos.R;

public class ContactListAdapter implements ListAdapter {
    private final Context context;
    String[] contactList = new String[] {"Name 1","Name 2","Name 3","Name 4","Name 5","Name 6","Name 7","Name 8","Name 9","Name 10","Name 11","Name 12","Name 13","Name 14","Name 15","Name 16","Name 17","Name 18","Name 19","Name 20"};;

    public ContactListAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        if(i >= 0 && i < contactList.length)
            return true;
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return contactList.length;
    }

    @Override
    public Object getItem(int i) {
        return contactList[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contact_list_item, viewGroup, false);
        TextView textView = rowView.findViewById(R.id.nombreContacto);
        textView.setText(contactList[i]);

        return rowView;
    }

    @Override
    public int getItemViewType(int i) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

//    adsfasd

}
