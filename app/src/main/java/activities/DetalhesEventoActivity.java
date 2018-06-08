package activities;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.supanonymous.eventapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fragments.ParticipantesListFragment;
import models.Evento;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import services.ApiService;
import utils.DecoderUtil;
import utils.ServiceGenerator;

/**
 * Created by supanonymous on 06/06/18.
 */

public class DetalhesEventoActivity extends AppCompatActivity{

    private ImageButton voltarBtn;
    private ImageView imagemEventoIv;
    private TextView nomeEventoTv;
    private TextView dataEventoTv;
    private ScrollView eventoContainer;
    private TextView localEventoTv;
    private String eventoId;
    private FrameLayout headerContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evento);
        voltarBtn = findViewById(R.id.nova_ocorrencia_retornar);
        imagemEventoIv = findViewById(R.id.detalhes_evento_imagem);
        nomeEventoTv = findViewById(R.id.detalhes_evento_nome);
        dataEventoTv = findViewById(R.id.detalhes_evento_data);
        localEventoTv = findViewById(R.id.detalhes_evento_local);
        eventoContainer = findViewById(R.id.detalhes_evento_container);
        headerContainer = findViewById(R.id.detalhes_evento_header_container);

        //Move o scroll pro topo da scrollview
        eventoContainer.smoothScrollTo(0,0);

        //Listener botão de voltar
        voltarBtn.setOnClickListener(v ->finish());
        //Carrega as informações do evento
        if (getIntent().getExtras()!=null) {
            Bundle bundle = getIntent().getExtras();
            eventoId = bundle.getString("evento_id");
            Log.e("evento", ""+eventoId);
            getDetalhesEvento();
        }
    }



    private void getDetalhesEvento() {
        /*
            Faz a chamada na API instanciando o serviço com o Retrofit
            Passa o ID do evento como parâmetro do método de callback
            Outra forma seria passar os dados do evento nos extras.
        */

        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getEvento(eventoId).enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call <List<Evento>> call, Response <List<Evento>> response) {
                if (!response.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(eventoContainer, "Ocorreu um erro",  Snackbar.LENGTH_INDEFINITE)
                            .setAction("Tentar novamente", v1 -> getDetalhesEvento() );
                    snackbar.show();
                }else {
                    Evento evento = response.body().get(0);
                    if (evento != null) {
                        nomeEventoTv.setText(evento.getNome());
                        localEventoTv.setText(evento.getLocal());
                        dataEventoTv.setText(evento.getQuando());
                        Bitmap eventImage = DecoderUtil.decodeBase64(evento.getImagem());
                        //Verifica se a imagem é um bitmap válido, caso contrário carrega a img padrão
                        if (eventImage != null) {
                            imagemEventoIv.setImageBitmap(eventImage);
                        } else {
                            Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.psycho_beach);
                            imagemEventoIv.setImageBitmap(defaultBitmap);
                        }

                        //Inicia o fragment que contém a lista de participantes
                        ParticipantesListFragment participantesListFragment = new ParticipantesListFragment();

                        // Passa o id do evento para o fragment
                        Bundle bundle = new Bundle();
                        bundle.putString("evento_id", eventoId);
                        bundle.putString("nome_evento", evento.getNome());
                        participantesListFragment.setArguments(bundle);

                        // Adiciona o fragment para o 'detalhes_evento_fragment_container' FrameLayout
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.detalhes_evento_fragment_container, participantesListFragment).commit();

                    }

                }

            }

            @Override
            public void onFailure(Call <List<Evento>> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(eventoContainer, "Falha ao se conectar com o servidor",  Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente", v1 -> getDetalhesEvento() );
                snackbar.show();
                t.printStackTrace();
            }
        });
    }
}
