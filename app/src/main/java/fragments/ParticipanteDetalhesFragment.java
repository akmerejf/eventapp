package fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.supanonymous.eventapp.R;

import java.util.List;

import models.Evento;
import models.Participante;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.ApiService;
import utils.DecoderUtil;
import utils.ServiceGenerator;

/**
 * Created by supanonymous on 07/06/18.
 */

public class ParticipanteDetalhesFragment extends DialogFragment {

    private String participanteId;
    private ConstraintLayout participanteContainer;
    private TextView nomeParticipanteTv;
    private TextView checkinParticipanteTv;
    private TextView emailParticipanteTv;
    private TextView dataParticipanteTv;
    private TextView nomeEventoTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.participante_detalhes_fragment, container, false);
        participanteId = getArguments().getString("participante_id");

        participanteContainer = view.findViewById(R.id.participante_detalhes_fragment_container);
        ImageButton closeBtn = view.findViewById(R.id.participante_detalhes_fragment_fechar);
        nomeParticipanteTv = view.findViewById(R.id.participante_detalhes_fragment_nome);
        checkinParticipanteTv = view.findViewById(R.id.participante_detalhes_fragment_checkin);
        emailParticipanteTv = view.findViewById(R.id.participante_detalhes_fragment_email);
        dataParticipanteTv = view.findViewById(R.id.participante_detalhes_fragment_data);
        nomeEventoTv = view.findViewById(R.id.participante_detalhes_fragment_header_tv);

        //Nome do evento
        nomeEventoTv.setText(getArguments().getString("nome_evento"));
        //Fecha o dialog
        closeBtn.setOnClickListener(v -> getDialog().dismiss());

        getParticipanteDetalhes();

        return view;
    }


    private void getParticipanteDetalhes() {
        /*
            Faz a chamada na API instanciando o serviço com o Retrofit
            Passa o ID do participante como parâmetro do método de callback
        */

        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getParticipante(participanteId).enqueue(new Callback<Participante>() {
            @Override
            public void onResponse(Call<Participante> call, Response<Participante> response) {
                if (!response.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(participanteContainer, "Ocorreu um erro",  Snackbar.LENGTH_INDEFINITE)
                            .setAction("Tentar novamente", v1 -> getParticipanteDetalhes() );
                    snackbar.show();
                    Log.e(""+response.code(), response.message());
                }else {
                    Participante participante = response.body();
                    if (participante != null) {
                        nomeParticipanteTv.setText("Nome: "+participante.getNome());
                        if (participante.getCheckIn()!=null) {
                            checkinParticipanteTv.setText("Fez CheckIn: Sim");
                        }else {
                            checkinParticipanteTv.setText("Fez CheckIn: Não");
                        }
                        emailParticipanteTv.setText("Email: "+participante.getEmail());
                        dataParticipanteTv.setText("Data cadastro: "+ participante.getDataCadastro());
                    }else{
                        Snackbar snackbar = Snackbar.make(participanteContainer, "não há informações disponíveis :(",  Snackbar.LENGTH_INDEFINITE)
                                .setAction("Fechar", v1 -> getDialog().dismiss() );
                        snackbar.show();
                    }

                }

            }

            @Override
            public void onFailure(Call <Participante> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(participanteContainer, "Falha ao se conectar com o servidor",  Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente", v1 -> getParticipanteDetalhes() );
                snackbar.show();
                t.printStackTrace();
            }
        });
    }

}
