package br.com.futureday.reserveandplay.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.futureday.reserveandplay.R;
import br.com.futureday.reserveandplay.model.Mesa;
import br.com.futureday.reserveandplay.model.Usuario;
import br.com.futureday.reserveandplay.utils.Utils;

/**
 * Created by FJ on 24/02/2018.
 */

public class ListaMesaAdapter extends BaseAdapter {

    private List<Mesa> listaMesas;
    private List<Mesa> listaMesasTodos;
    private Activity activityMesa;

    public ListaMesaAdapter(List<Mesa> listaMesas, Activity activity) {
        this.listaMesas = listaMesas;
        activityMesa =activity;
        listaMesasTodos = new ArrayList<>();
        for (Mesa mesa : listaMesas) {
            listaMesasTodos.add(mesa);
        }
    }

    public List<Mesa> getListaMesasTodos() {
        return listaMesasTodos;
    }

    @Override
    public int getCount() {
        return listaMesas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaMesas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = activityMesa.getLayoutInflater().inflate(R.layout.layout_lista_mesas,parent,false);
        Mesa mesa = listaMesas.get(position);
        TextView nome = view.findViewById(R.id.nomeMesaId);
        TextView data = view.findViewById(R.id.dataMesaId);
        ImageView imagem = view.findViewById(R.id.imagemMesasId);

        nome.setText(mesa.getNome());
        data.setText(Utils.getStringFromDate(mesa.getDataAtividade()));

        if(mesa.getFoto()==null){
            imagem.setImageResource(R.drawable.ic_action_user);
        }else{
            imagem.setImageBitmap(mesa.getFoto());
        }

        return view;
    }

    public void filtrar(String texto){
            listaMesas.clear();
            if(texto.length() == 0){
                listaMesas.addAll(listaMesasTodos);
            }else{
                for (Mesa mesa : listaMesasTodos){
                    String dataAtividade = Utils.getStringFromDate(mesa.getDataAtividade());
                    if(mesa.getNome().toUpperCase().contains(texto.toUpperCase())||dataAtividade.toUpperCase().contains(texto.toUpperCase())){
                        listaMesas.add(mesa);
                    }
                }


            }
            notifyDataSetChanged();
    }
}
