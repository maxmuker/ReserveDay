package br.com.futureday.reserveandplay.model;

import android.graphics.Bitmap;

import java.util.Date;
import java.util.List;

/**
 * Created by Filipe on 15/03/2018.
 */

public class Mesa {


    private String nome;
    private String uidCriadorMesa;
    private Date dataCriacao;
    private int quantidadeVagas;
    private List<String> participantes;
    private Bitmap foto;
    private List<String> jogos;
    private String tipoEvento;
    private boolean ativo;
    private Date dataAtividade;

    public Mesa() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUidCriadorMesa() {
        return uidCriadorMesa;
    }

    public void setUidCriadorMesa(String uidCriadorMesa) {
        this.uidCriadorMesa = uidCriadorMesa;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public int getQuantidadeVagas() {
        return quantidadeVagas;
    }

    public void setQuantidadeVagas(int quantidadeVagas) {
        this.quantidadeVagas = quantidadeVagas;
    }

    public List<String> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<String> participantes) {
        this.participantes = participantes;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public List<String> getJogos() {
        return jogos;
    }

    public void setJogos(List<String> jogos) {
        this.jogos = jogos;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Date getDataAtividade() {
        return dataAtividade;
    }

    public void setDataAtividade(Date dataAtividade) {
        this.dataAtividade = dataAtividade;
    }
}
