package activities;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.supanonymous.eventapp.R;

import java.util.ArrayList;
import java.util.List;

import adapters.EventsListAdapter;
import models.Evento;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.ApiService;
import utils.ServiceGenerator;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView eventsRecyclerView;
    private SwipeRefreshLayout eventsSwipeRefreshLayout;
    private LinearLayoutManager layoutManager;
    private List<Evento> eventsList;
    private EventsListAdapter eventsListAdapter;
    private FrameLayout progressView;
    private ConstraintLayout mainContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = findViewById(R.id.progress_view);
        mainContainer = findViewById(R.id.main_activity_container);
        initRecycleView();
        getEvents();
    }

    private void initRecycleView() {
        eventsSwipeRefreshLayout = findViewById(R.id.main_activity_events_srl);
        eventsRecyclerView = findViewById(R.id.main_activity_events_rv);
        eventsSwipeRefreshLayout.setOnRefreshListener(this);
        eventsList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        eventsListAdapter = new EventsListAdapter(MainActivity.this, eventsList);
        eventsRecyclerView.setLayoutManager(layoutManager);
        eventsRecyclerView.setAdapter(eventsListAdapter);
    }

    @Override
    public void onRefresh() {
        eventsSwipeRefreshLayout.setRefreshing(true);
        getEvents();

    }

    private void getEvents() {
        if (eventsSwipeRefreshLayout.isRefreshing())
            eventsSwipeRefreshLayout.setRefreshing(false);

        //Carrega a view de ProgressBar antes da chamada à API
        progressView.setVisibility(View.VISIBLE);

        //Faz a chamada na API instanciando o serviço com o Retrofit
        ApiService apiService = ServiceGenerator.createService(ApiService.class);
        apiService.getEventos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (!response.isSuccessful()){
                    Snackbar snackbar = Snackbar.make(mainContainer, "Não foi possível exibir",  Snackbar.LENGTH_INDEFINITE)
                            .setAction("Tentar novamente", v1 -> getEvents() );
                    snackbar.show();
                }else {
                    eventsList.clear();
                    eventsList.addAll(response.body());

                    eventsListAdapter.notifyDataSetChanged();
                }

                progressView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Snackbar snackbar = Snackbar.make(mainContainer, "Falha ao se conectar com o servidor",  Snackbar.LENGTH_INDEFINITE)
                        .setAction("Tentar novamente", v1 -> getEvents() );
                snackbar.show();
                progressView.setVisibility(View.GONE);
            }
        });

    }
}
