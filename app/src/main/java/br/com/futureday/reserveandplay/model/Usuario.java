package br.com.futureday.reserveandplay.model;

import android.graphics.Bitmap;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Comparator;

import br.com.futureday.reserveandplay.config.ConfiguracaoFirebase;
import br.com.futureday.reserveandplay.utils.Constantes;

/**
 * Created by FJ on 01/02/2018.
 */

public class Usuario implements Comparator{

    private String author_id;
    private String nome;
    private String telefone;
    private String email;
    private String senha;
    private Bitmap foto;
    private String fotoInterna;

    //Off
    private String rawContactId;
    private String contatoId;

    public Usuario() {
    }
    public Usuario(String nome, String telefone, String fotoUri) {
        this.nome = nome;
        this.telefone = telefone;
        this.fotoInterna = fotoUri;
    }
    public void salvar() {

        FirebaseFirestore db = ConfiguracaoFirebase.getFirestore();
        db.collection("usuario").document(getTelefone()).set(this);
        db.collection(Constantes.TABELA_USUARIOS).document("TodosTelefones").collection("Telefones").document(this.getTelefone());
    }
    public boolean isUsuarioValido(){

        if(getNome()==null || getNome().isEmpty()){
            return false;
        }
        return !(getTelefone() == null || getTelefone().isEmpty());
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

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

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Exclude
    public String getRawContactId() {
        return rawContactId;
    }

    public void setRawContactId(String rawContactId) {
        this.rawContactId = rawContactId;
    }
    @Exclude
    public String getContatoId() {
        return contatoId;
    }

    public void setContatoId(String contatoId) {
        this.contatoId = contatoId;
    }

    public Bitmap getFoto() {

        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public String getFotoInterna() {
        return fotoInterna;
    }

    public void setFotoInterna(String fotoInterna) {
        this.fotoInterna = fotoInterna;
    }

    @Override
    public int compare(Object o1, Object o2) {
        return ((Usuario)o1).getNome().compareTo(((Usuario)o2).getNome());
    }

    @Override
    public boolean equals(Object obj) {

        String telefone = ((Usuario)obj).getTelefone();
        return getTelefone().equals(telefone);

    }
}
