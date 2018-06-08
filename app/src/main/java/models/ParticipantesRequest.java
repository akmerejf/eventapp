package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by supanonymous on 07/06/18.
 */

public class ParticipantesRequest {

    @SerializedName("Pagina")
    @Expose
    private Integer pagina;
    @SerializedName("RegistrosPorPagina")
    @Expose
    private Integer registrosPorPagina;


    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Integer getRegistrosPorPagina() {
        return registrosPorPagina;
    }

    public void setRegistrosPorPagina(Integer registrosPorPagina) {
        this.registrosPorPagina = registrosPorPagina;
    }
}
