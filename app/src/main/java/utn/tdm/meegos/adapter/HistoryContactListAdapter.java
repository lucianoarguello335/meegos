package utn.tdm.meegos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import utn.tdm.meegos.R;
import utn.tdm.meegos.domain.Evento;
import utn.tdm.meegos.listener.OnListEventListener;
import utn.tdm.meegos.util.Constants;
import utn.tdm.meegos.util.DateUtil;

public class HistoryContactListAdapter extends RecyclerView.Adapter<HistoryContactListAdapter.ViewHolder> {

    private List<Evento> eventos;
    private final OnListEventListener onListEventListener;

    public HistoryContactListAdapter(List<Evento> eventos, OnListEventListener onListEventListener) {
        this.eventos = eventos;
        this.onListEventListener = onListEventListener;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_contact_list_item_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Evento evento = eventos.get(position);

        if (evento.getTipo() == Evento.LLAMADA) {
            holder.tipoEventoImageView.setImageResource(R.drawable.baseline_call_black_48);
        } else {
            holder.tipoEventoImageView.setImageResource(R.drawable.baseline_sms_black_48);
        }
        holder.tipoEventoImageView.setColorFilter(holder.view.getResources().getColor(R.color.list_tipo_evento,null));

        if (evento.getOrigen() == Evento.ENTRANTE) {
            holder.origenImageView.setImageResource(R.drawable.baseline_call_received_black_48);
            holder.origenImageView.setColorFilter(holder.view.getResources().getColor(R.color.colorPrimaryDark,null));
        } else {
            holder.origenImageView.setImageResource(R.drawable.baseline_call_made_black_48);
            holder.origenImageView.setColorFilter(holder.view.getResources().getColor(R.color.colorAccent,null));
        }

        Calendar fecha = Calendar.getInstance();
        fecha.setTimeInMillis(evento.getFecha());

        holder.fechaTextView.setText(DateUtil.getCalendarAsString(fecha, Constants.CALENDAR_FORMAT_PATTERN));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListEventListener.onDeleteEvent(evento);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final ImageView tipoEventoImageView;
        public final ImageView origenImageView;
        public final TextView fechaTextView;
        public final TextView mContentView;
        public final ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tipoEventoImageView = (ImageView) view.findViewById(R.id.tipoEvento);
            origenImageView = (ImageView) view.findViewById(R.id.origen);
            fechaTextView = (TextView) view.findViewById(R.id.fecha);
            mContentView = (TextView) view.findViewById(R.id.content);
            deleteButton = (ImageButton) view.findViewById(R.id.history_contact_list_deleteButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
