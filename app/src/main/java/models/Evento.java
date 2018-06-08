package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by supanonymous on 06/06/18.
 */

public class Evento implements Serializable {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Nome")
    @Expose
    private String nome;
    @SerializedName("Imagem")
    @Expose
    private String imagem;
    @SerializedName("UsarImpressora")
    @Expose
    private Boolean usarImpressora;
    @SerializedName("CapturarAssinatura")
    @Expose
    private Boolean capturarAssinatura;
    @SerializedName("ClienteImagem")
    @Expose
    private String clienteImagem;
    @SerializedName("Inicio")
    @Expose
    private String inicio;
    @SerializedName("Quando")
    @Expose
    private String quando;
    @SerializedName("Local")
    @Expose
    private String local;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public Boolean getUsarImpressora() {
        return usarImpressora;
    }

    public void setUsarImpressora(Boolean usarImpressora) {
        this.usarImpressora = usarImpressora;
    }

    public Boolean getCapturarAssinatura() {
        return capturarAssinatura;
    }

    public void setCapturarAssinatura(Boolean capturarAssinatura) {
        this.capturarAssinatura = capturarAssinatura;
    }

    public String getClienteImagem() {
        return clienteImagem;
    }

    public void setClienteImagem(String clienteImagem) {
        this.clienteImagem = clienteImagem;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getQuando() {
        return quando;
    }

    public void setQuando(String quando) {
        this.quando = quando;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
