package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.supanonymous.eventapp.R;

import java.text.DateFormatSymbols;
import java.util.List;

import activities.DetalhesEventoActivity;
import models.Evento;
import utils.DecoderUtil;

/**
 * Created by supanonymous on 06/06/18.
 */

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.EventsViewHolder>{

    private  Context context;
    private  List<Evento> eventList;

    public EventsListAdapter(Context context, List<Evento> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public EventsViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.cardview_evento, parent, false);

        return new EventsViewHolder(v);
    }

    @Override
    public void onBindViewHolder( EventsViewHolder holder, int position) {
            Evento evento = eventList.get(position);

            //Se a data não estiver vazia converte para dia e Mẽs
            if (!TextUtils.isEmpty(evento.getQuando())) {
                String[] dayMonthArray = new String[2];
                dayMonthArray = parseDate(evento);
                holder.dataDiaEvento.setText(dayMonthArray[0]);
                holder.dataMesEvento.setText(dayMonthArray[1]);
            }else {
                holder.dataDiaEvento.setText("--");
                holder.dataMesEvento.setText("--");
            }

            holder.nomeEvento.setText(evento.getNome());
            holder.localEvento.setText(evento.getLocal());

            //Decodifica a string em base64 para bitmap
            Bitmap eventImage = DecoderUtil.decodeBase64(evento.getImagem());
            Bitmap eventClientImage = DecoderUtil.decodeBase64(evento.getClienteImagem());

            //Verifica se a imagem é um bitmap válido, caso contrário carrega a img padrão
            if (eventImage!=null){
                holder.imagemEvento.setImageBitmap(eventImage);
            }else{
                Bitmap defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.psycho_beach);
                holder.imagemEvento.setImageBitmap(defaultBitmap);
            }

            if (eventClientImage!=null){
                holder.imagemClienteEvento.setImageBitmap(eventClientImage);
            }else{
                Bitmap defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.image_event);
                holder.imagemClienteEvento.setImageBitmap(defaultBitmap);
            }

            //Listener para abrir detalhes do evento
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetalhesEventoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("evento_id", String.valueOf(evento.getId()));
                intent.putExtras(bundle);

                context.startActivity(intent);
            });

    }


    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private String[] parseDate(Evento evento) {
        String[] partsArray = evento.getQuando().split("/");
        String[] dateArray = new String[2];
        dateArray[0] = partsArray[0];
        dateArray[1] = new DateFormatSymbols().getMonths()[Integer.parseInt(partsArray[1]) - 1];
        return dateArray;
    }


    public class EventsViewHolder extends RecyclerView.ViewHolder{
        private final TextView nomeEvento;
        private final TextView dataMesEvento;
        private final TextView dataDiaEvento;
        private final TextView localEvento;
        private final ImageView imagemEvento;
        private final ImageView imagemClienteEvento;

        public EventsViewHolder(final View itemView) {
            super(itemView);

            nomeEvento = itemView.findViewById(R.id.cardview_event_nome);
            dataMesEvento = itemView.findViewById(R.id.cardview_event_tv_mes);
            dataDiaEvento = itemView.findViewById(R.id.cardview_event_tv_dia);
            localEvento = itemView.findViewById(R.id.cardview_event_local);
            imagemEvento = itemView.findViewById(R.id.cardview_event_image);
            imagemClienteEvento = itemView.findViewById(R.id.cardview_event_image_client);

        }

    }
}
