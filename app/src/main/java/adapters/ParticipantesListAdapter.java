package adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.supanonymous.eventapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import interfaces.AdapterCallback;
import models.Participante;

public class ParticipantesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private static List<Participante> participantes = new ArrayList<>();
    private AdapterCallback mAdapterCallback;
    private Context context;

    // Items para adicionar dinamicamente ao fim da lista
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean listEnd = false;

    public ParticipantesListAdapter( Context context, List<Participante> participantes, RecyclerView recyclerView, AdapterCallback adapterCallback) {
        ParticipantesListAdapter.participantes = participantes;
        this.context = context;
        this.mAdapterCallback = adapterCallback;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return participantes.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.participantes_list_item, parent, false);

            vh = new Visao(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder visao, int position) {
        if (visao instanceof Visao) {
            Participante participante = participantes.get(position);
            ((Visao)visao).nomeParticipante.setText(participante.getNome());
            if (participante.getCheckIn()!=null) {
                ((Visao) visao).checkinParticipante.setText("Fez CheckIn: Sim");
            }else {
                ((Visao) visao).checkinParticipante.setText("Fez CheckIn: Não");
            }

            //Abre detalhes do participante pela interface implementada
            // em ParticipantesListFragment
            ((Visao)visao).itemView.setOnClickListener(v -> {
                mAdapterCallback.onMethodCallback(String.valueOf(participante.getId()));
            });

        } else {
            ((ProgressViewHolder) visao).progressBar.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {

        return participantes.size();
    }

    public boolean isListEnd() {
        return listEnd;
    }

    public static class Visao extends RecyclerView.ViewHolder{

        TextView nomeParticipante;

        TextView checkinParticipante;

        public Visao(final View itemView) {
            super(itemView);

            nomeParticipante = itemView.findViewById(R.id.participantes_list_item_nome);
            checkinParticipante = itemView.findViewById(R.id.participantes_list_item_checkin);

        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setListEnd(){
        listEnd = true;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}