package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by supanonymous on 07/06/18.
 */

public class Participante {
    @SerializedName("Nome")
    @Expose
    private String nome;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("CheckIn")
    @Expose
    private String checkIn;
    @SerializedName("DataCadastro")
    @Expose
    private String dataCadastro;
    @SerializedName("Assinatura")
    @Expose
    private String assinatura;
    @SerializedName("Id")
    @Expose
    private Integer id;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
