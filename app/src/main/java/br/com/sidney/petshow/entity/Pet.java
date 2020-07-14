package br.com.sidney.petshow.entity;

import java.io.Serializable;

/**
 * @author Sidney Toshidi OKu
 */
public class Pet implements Serializable {

    public Pet() {}

    public Pet(String id, String nome, String tipo, String porte, String idProprietario, String imagem1) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.porte = porte;
        this.idProprietario = idProprietario;
        this.imagem1 = imagem1;
    }

    private String id;
    private String nome;
    private String tipo;
    private String porte;
    private String idProprietario;

    private String imagem1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public String getImagem1() {
        return imagem1;
    }

    public void setImagem1(String imagem1) {
        this.imagem1 = imagem1;
    }

    public String getIdProprietario() {
        return idProprietario;
    }

    public void setIdProprietario(String idProprietario) {
        this.idProprietario = idProprietario;
    }

}
