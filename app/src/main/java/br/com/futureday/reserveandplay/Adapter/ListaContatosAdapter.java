package br.com.futureday.reserveandplay.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.futureday.reserveandplay.R;
import br.com.futureday.reserveandplay.model.Usuario;
import br.com.futureday.reserveandplay.utils.GerenciarContatos;

/**
 * Created by FJ on 24/02/2018.
 */

public class ListaContatosAdapter extends BaseAdapter {

    private List<Usuario> listaUsuarios;
    private List<Usuario> listaUsuariosTodos;
    private Activity activityAmigos;

    public ListaContatosAdapter(List<Usuario> listaUsuario, Activity activity) {
        listaUsuarios = listaUsuario;
        activityAmigos =activity;
        listaUsuariosTodos = new ArrayList<Usuario>();
        for (Usuario user : listaUsuario) {
            listaUsuariosTodos.add(user);
        }
    }

    public List<Usuario> getListaUsuariosTodos() {
        return listaUsuariosTodos;
    }

    @Override
    public int getCount() {
        return listaUsuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return listaUsuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = activityAmigos.getLayoutInflater().inflate(R.layout.layout_lista_contatos,parent,false);
        Usuario usuario = listaUsuarios.get(position);
        TextView nome = view.findViewById(R.id.nomeContatoId);
        TextView telefone = view.findViewById(R.id.telefoneContatoId);
        ImageView imagem = view.findViewById(R.id.imagemContatosId);

        nome.setText(usuario.getNome());
        telefone.setText(usuario.getTelefone());

        if(usuario.getFoto()==null){
            if(usuario.getFotoInterna()!=null && !usuario.getFotoInterna().isEmpty()){
                imagem.setImageBitmap(GerenciarContatos.carregaFoto(usuario.getFotoInterna(), activityAmigos.getContentResolver()));
            }else{
                imagem.setImageResource(R.drawable.ic_action_user);
            }

        }else{
            imagem.setImageBitmap(usuario.getFoto());
        }

        return view;
    }

    public void filtrar(String texto){
            listaUsuarios.clear();
            if(texto.length() == 0){
                listaUsuarios.addAll(listaUsuariosTodos);
            }else{
                for (Usuario user : listaUsuariosTodos){
                    if(user.getNome().toUpperCase().contains(texto.toUpperCase())||user.getTelefone().toUpperCase().contains(texto.toUpperCase())){
                        listaUsuarios.add(user);
                    }
                }


            }
            notifyDataSetChanged();
    }
}
