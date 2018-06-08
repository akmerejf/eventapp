package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by supanonymous on 07/06/18.
 */

public class ParticipantesResponse {
    @SerializedName("Lista")
    @Expose
    private List<Participante> participantesList = null;
    @SerializedName("Paginador")
    @Expose
    private Paginador paginador;

    public List<Participante> getParticipantesList() {
        return participantesList;
    }

    public void setParticipantesList(List<Participante> lista) {
        this.participantesList = lista;
    }

    public Paginador getPaginador() {
        return paginador;
    }

    public void setPaginador(Paginador paginador) {
        this.paginador = paginador;
    }
}
