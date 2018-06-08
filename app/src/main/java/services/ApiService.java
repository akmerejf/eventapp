package services;


import java.util.List;
import java.util.Map;

import models.Evento;
import models.Participante;
import models.ParticipantesRequest;
import models.ParticipantesResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by supanonymous on 03/04/18.
 */

public interface ApiService {

    @GET("Evento/EventosAtivosDoCliente?idCliente=-1")
    Call<List<Evento>> getEventos();

    @GET("Evento/EventosAtivosDoCliente")
    Call<List<Evento>> getEvento(@Query("idCliente") String idCliente);

    @POST("Evento/ParticipantesDoEvento")
    Call <ParticipantesResponse> getParticipantes(
            @Query("idEvento") String id,
            @Body ParticipantesRequest participantesRequest
    );

    @GET("Participante/ObterParticipante")
    Call<Participante> getParticipante(@Query("idParticipante") String idParticipante);
}
