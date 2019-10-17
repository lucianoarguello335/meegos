package utn.tdm.meegos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import utn.tdm.meegos.R;
import utn.tdm.meegos.domain.Transaccion;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {

    private List<Transaccion> transacciones;

    public TransactionListAdapter(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_item_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Transaccion transaccion = transacciones.get(position);

        holder.name.setText(transaccion.getRequestName());
        holder.timestamp.setText(transaccion.getTimestamp());

        if (transaccion.getResponseType().equals("success")) {
            holder.responseTypeImage.setImageResource(R.drawable.baseline_done_24);
            holder.responseTypeImage.setColorFilter(holder.view.getResources().getColor(R.color.colorPrimary, null));
        } else {
            holder.responseTypeImage.setImageResource(R.drawable.baseline_close_24);
            holder.responseTypeImage.setColorFilter(holder.view.getResources().getColor(R.color.colorAccent, null));
        }

        holder.responseType.setText(transaccion.getResponseType());
        holder.errorCode.setText(transaccion.getErrorCode());
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView timestamp;
        public final ImageView responseTypeImage;
        public final TextView responseType;
        public final TextView errorCode;
//        public final ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.transaction_name);
            timestamp = view.findViewById(R.id.transaction_timestamp);
            responseTypeImage = view.findViewById(R.id.transaction_response_type_image);
            responseType = view.findViewById(R.id.transaction_response_type);
            errorCode = view.findViewById(R.id.transaction_error_code);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
