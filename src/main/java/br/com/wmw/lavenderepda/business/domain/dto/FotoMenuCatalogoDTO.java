package br.com.wmw.lavenderepda.business.domain.dto;

import totalcross.sys.Time;

public class FotoMenuCatalogoDTO {

    private String nmFoto;
    private int nuTamanho;
    private Time dtModificacao;

    public String getNmFoto() {
        return nmFoto;
    }

    public void setNmFoto(String nmFoto) {
        this.nmFoto = nmFoto;
    }

    public int getNuTamanho() {
        return nuTamanho;
    }

    public void setNuTamanho(int nuTamanho) {
        this.nuTamanho = nuTamanho;
    }

    public Time getDtModificacao() {
        return dtModificacao;
    }

    public void setDtModificacao(Time dtModificacao) {
        this.dtModificacao = dtModificacao;
    }
}
