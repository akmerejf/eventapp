package fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supanonymous.eventapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.ParticipantesListAdapter;
import interfaces.AdapterCallback;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import models.Paginador;
import models.Participante;
import models.ParticipantesRequest;
import models.ParticipantesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.ApiService;
import utils.ServiceGenerator;


/**
 * Created by supanonymous on 07/06/18.
 */

public class ParticipantesListFragment extends Fragment implements AdapterCallback {

    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView participantesRv;
    private FrameLayout progressView;
    private String eventoId;
    private LinearLayoutManager layoutManager;
    private List<Participante> participantesList;
    private ApiService apiService;
    private ConstraintLayout containerLayout;
    private ParticipantesListAdapter participantesListAdapter;
    private ParticipantesRequest participantesRequest;
    private Paginador paginador;
    private String nomeEvento;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla o layout deste fragment
        View view = inflater.inflate(R.layout.participantes_list_fragment, container, false);

        progressView = view.findViewById(R.id.progress_view);
        containerLayout = view.findViewById(R.id.participantes_list_item_container);
        participantesRv = view.findViewById(R.id.detalhes_evento_participantes_rv);
        layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        participantesRv.setLayoutManager(layoutManager);
        participantesRv.setHasFixedSize(true);
        participantesList = new ArrayList<>();
        participantesListAdapter = new ParticipantesListAdapter(getContext(), participantesList, participantesRv, this);
        participantesRv.setItemAnimator(new SlideInUpAnimator());
        participantesRv.setAdapter(participantesListAdapter);

        if (getArguments()!=null) {
            eventoId = getArguments().getString("evento_id");
            nomeEvento = getArguments().getString("nome_evento");
            loadParticipantes();
        }

//------------------listener carregar mais itens na rolagem-----------------------------------------//
        participantesListAdapter.setOnLoadMoreListener(() -> {
            //add progress item
            if (!participantesListAdapter.isListEnd()) {
                participantesList.add(null);
                participantesListAdapter.notifyItemInserted(participantesList.size() - 1);
                loadMoreParticipantes();
            }
        });
        return view;
    }


    private void loadParticipantes() {
        participantesRequest = new ParticipantesRequest();
        participantesRequest.setPagina(1);
        participantesRequest.setRegistrosPorPagina(10);
        apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getParticipantes(eventoId, participantesRequest).enqueue(new Callback<ParticipantesResponse>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<ParticipantesResponse> call, Response<ParticipantesResponse> response) {
                if (!response.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(containerLayout, "Não foi possível exibir",  Snackbar.LENGTH_INDEFINITE)
                            .setAction("Tentar novamente", v1 -> loadParticipantes() );
                    snackbar.show();
                    Log.e(""+response.code(), response.message());
                }else {
                    paginador = response.body().getPaginador();
                    participantesList.clear();
                    participantesList.addAll(response.body().getParticipantesList());
                    participantesListAdapter.notifyDataSetChanged();
                }

                progressView.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<ParticipantesResponse> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(containerLayout, "Falha ao se conectar com o servidor",  Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente", v1 -> loadParticipantes() );
                snackbar.show();
                progressView.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }


    public void loadMoreParticipantes() {
        // parametros de busca
        apiService.getParticipantes(eventoId, participantesRequest).enqueue(new Callback<ParticipantesResponse>() {
            //metodos de respostas
            @Override
            public void onResponse(Call<ParticipantesResponse> call, Response<ParticipantesResponse> response) {
                if (!response.isSuccessful()) {
                    Snackbar snackbar = Snackbar.make(containerLayout, "Não foi possível exibir",  Snackbar.LENGTH_INDEFINITE)
                            .setAction("Tentar novamente", v1 -> loadMoreParticipantes() );
                    snackbar.show();
                    Log.e(""+response.code(), response.message());
                } else { //condição se os dados foram capturados
                    paginador = response.body().getPaginador();
                    participantesList.remove(participantesList.size() - 1);
                    //Condição de parada caso chegue ao fim da lista
                    if (paginador.getPagina()+1 > paginador.getTotalPaginas()){
                        participantesListAdapter.setListEnd();
                        return;
                    }
                    participantesListAdapter.notifyItemRemoved(participantesList.size());
                    participantesRequest.setPagina(paginador.getPagina()+1);
                    participantesList.addAll(response.body().getParticipantesList());
                    //Atualiza o adapter com os items da api
                    participantesListAdapter.notifyDataSetChanged();
                    participantesListAdapter.setLoaded();

                }
            }

            @Override
            public void onFailure(Call<ParticipantesResponse> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(containerLayout, "Falha ao se conectar com o servidor",  Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente", v1 -> loadMoreParticipantes() );
                snackbar.show();
                progressView.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onMethodCallback(String key) {
        ParticipanteDetalhesFragment newFragment = new ParticipanteDetalhesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("participante_id", key);
        bundle.putString("nome_evento", nomeEvento);
        newFragment.setArguments(bundle);
        newFragment.show(getFragmentManager(), "participante_detalhes");
    }
}
