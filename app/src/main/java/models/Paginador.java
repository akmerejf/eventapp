package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by supanonymous on 07/06/18.
 */

public class Paginador {
    @SerializedName("Pagina")
    @Expose
    private Integer pagina;
    @SerializedName("TotalPaginas")
    @Expose
    private Integer totalPaginas;
    @SerializedName("RegistrosPorPagina")
    @Expose
    private Integer registrosPorPagina;
    @SerializedName("TotalRegistros")
    @Expose
    private Integer totalRegistros;

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Integer getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(Integer totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public Integer getRegistrosPorPagina() {
        return registrosPorPagina;
    }

    public void setRegistrosPorPagina(Integer registrosPorPagina) {
        this.registrosPorPagina = registrosPorPagina;
    }

    public Integer getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(Integer totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

}
