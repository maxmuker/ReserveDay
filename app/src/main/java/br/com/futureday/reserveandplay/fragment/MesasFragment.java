package br.com.futureday.reserveandplay.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.com.futureday.reserveandplay.Adapter.ListaMesaAdapter;
import br.com.futureday.reserveandplay.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MesasFragment extends Fragment {

    private ListView listViewMesas;

    public MesasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);


        listViewMesas = view.findViewById(R.id.listaMesasId);

        return view;
    }


    public void filtrarMesas(String texto) {
        if (listViewMesas != null)
            ((ListaMesaAdapter) listViewMesas.getAdapter()).filtrar(texto);
    }

}
