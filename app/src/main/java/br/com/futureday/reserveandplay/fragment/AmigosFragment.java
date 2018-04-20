package br.com.futureday.reserveandplay.fragment;


import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.provider.ContactsContract;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.auth.util.data.PhoneNumberUtils;
import com.google.android.gms.common.api.Batch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import br.com.futureday.reserveandplay.Adapter.ListaContatosAdapter;
import br.com.futureday.reserveandplay.R;
import br.com.futureday.reserveandplay.config.ConfiguracaoFirebase;
import br.com.futureday.reserveandplay.config.ValidacoesFirestone;
import br.com.futureday.reserveandplay.model.Usuario;
import br.com.futureday.reserveandplay.utils.Constantes;
import br.com.futureday.reserveandplay.utils.GerenciarContatos;
import br.com.futureday.reserveandplay.utils.Permissao;


public class AmigosFragment extends Fragment {

    private ListView listViewContatos;
    private ProgressBar progressBarAmigos;
    private ContentResolver contentResolver;
    private List<Usuario> listaDeContatos;
    private List<String> listaDeContatostelefone;
    private List<Usuario> listaDeContatosRegistrados;
    private int contador = 1000;
    private ListenerRegistration listenerContatos;
    private EventListener eventListener = null;
    private String telefoneUser="";

    private final int SERVICO_SINCRONIZAR_CONTATOS = 1243;

    //    private SearchView searchView;


    public AmigosFragment() {
        // Required empty public constructor
    }

    public void adicionarUsuario(final Usuario usuario) throws Exception {
        if (listaDeContatosRegistrados.contains(usuario)) {
            return;
        }
        progressBarAmigos.setVisibility(ProgressBar.VISIBLE);

        final Usuario usuarioFinal = usuario;
        ConfiguracaoFirebase.getFirestore().collection(Constantes.TABELA_USUARIOS).document(usuario.getTelefone())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        //usuario encontrado
                        if (usuarioFinal.isUsuarioValido()) {

                            ConfiguracaoFirebase.getFirestore()
                                    .collection(Constantes.TABELA_USUARIOS)
                                    .document(ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser().getPhoneNumber())
                                    .collection(Constantes.TABELA_USUARIOS_CONTATOS)
                                    .document(usuario.getTelefone()).set(usuario);

//                            if (listaDeContatosRegistrados == null) {
//                                listaDeContatosRegistrados = new ArrayList<Usuario>();
//                            }
//                            listaDeContatosRegistrados.add(usuarioFinal);
//                            if (listViewContatos.getAdapter() == null) {
//                                listViewContatos.setAdapter(new ListaContatosAdapter(listaDeContatosRegistrados, getActivity()));
//                            }
//                            ((ListaContatosAdapter) listViewContatos.getAdapter()).notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.nomeTelefoneObrigatorios), Toast.LENGTH_LONG).show();

                        }
                    } else {
                        //usuario não existe - retornar false
                        Toast.makeText(getContext(), getResources().getString(R.string.usuarioNaoCadastrado), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //sem sucesso

                }
                progressBarAmigos.setVisibility(ProgressBar.INVISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERRO : ", e.getMessage());
                progressBarAmigos.setVisibility(ProgressBar.INVISIBLE);

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);


        contentResolver = getActivity().getContentResolver();

        listViewContatos = view.findViewById(R.id.listaContatosId);
        progressBarAmigos = view.findViewById(R.id.progressBarAmigos);
        progressBarAmigos.setVisibility(ProgressBar.INVISIBLE);
        telefoneUser = ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser().getPhoneNumber();
        iniciarlizarContatos();
        eventListener = new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null){
                    Log.d("ERRO LISTA CONTATOS",e.getMessage());
                    return;
                }else{
                    List<DocumentSnapshot> documentosSnap= documentSnapshots.getDocuments();
                    listaDeContatosRegistrados.clear();
                    for(DocumentSnapshot docS : documentosSnap){
                        listaDeContatosRegistrados
                                .add(new Usuario(
                                        String.valueOf(docS.getData().get(Constantes.ATRIBUTO_USUARIO_NOME)),
                                        String.valueOf(docS.getData().get(Constantes.ATRIBUTO_USUARIO_TELEFONE)),
                                        null));
                    }
                    Usuario usuarioComparator = new Usuario();
                    Collections.sort(listaDeContatosRegistrados,usuarioComparator);
                    ((ListaContatosAdapter) listViewContatos.getAdapter()).notifyDataSetChanged();
                }
            }
        };
        listenerContatos = ConfiguracaoFirebase.getFirestore().collection(Constantes.TABELA_USUARIOS)
                .document(telefoneUser)
                .collection(Constantes.TABELA_USUARIOS_CONTATOS)
                .addSnapshotListener(eventListener);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        sincronizaContatos();

        if(telefoneUser!=null && !telefoneUser.isEmpty()) {
            listenerContatos = ConfiguracaoFirebase.getFirestore().collection(Constantes.TABELA_USUARIOS)
                    .document(telefoneUser)
                    .collection(Constantes.TABELA_USUARIOS_CONTATOS)
                    .addSnapshotListener(eventListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        listenerContatos = null;
    }

    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.main_menu,menu);
//
//        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        searchView = menu.findItem(R.id.item_pesquisa);
//
//    }
    public Usuario consultaUsuarioPorTelefone(String telefone) {

        for (Usuario usuario : listaDeContatos) {
            if (usuario.getTelefone().equals(telefone)) {
                return usuario;
            }
        }
        return null;
    }

    private void carregarTelefonesCadastrados() {
        ConfiguracaoFirebase.getFirestore()
                .collection(Constantes.TABELA_USUARIOS)
                .document("TodosTelefones")
                .collection("Telefones").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> listaDocumentos = task.getResult().getDocuments();
                } else {

                }
            }
        });
    }

    public void iniciarlizarContatos() {
        listaDeContatosRegistrados = new ArrayList<>();
        if (listViewContatos.getAdapter() == null) {
            listViewContatos.setAdapter(new ListaContatosAdapter(listaDeContatosRegistrados, getActivity()));
        }
        ((ListaContatosAdapter) listViewContatos.getAdapter()).notifyDataSetChanged();



//
//        ConfiguracaoFirebase.getFirestore().collection(Constantes.TABELA_USUARIOS).get().

//
//        listViewContatos.setAdapter(new ListaContatosAdapter(listaDeContatosRegistrados, getActivity()));
//        DocumentReference documentReference = ConfiguracaoFirebase.getFirestore()
//                .collection(Constantes.TABELA_USUARIOS).document(ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser().getPhoneNumber());
//
//        documentReference.update("contatos",listaDeContatostelefone);
//
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                if(e!=null){
//
//                }else{
//                     ArrayList<String> telefonesContatos = null;
//                    if(documentSnapshot!=null ){
//                        telefonesContatos = (ArrayList<String>)documentSnapshot.getData().get("contatos");
//                        if(telefonesContatos!=null){
//                            listaDeContatosRegistrados.clear();
//                            for (String telefoneCont: telefonesContatos){
//                                listaDeContatosRegistrados.add(new Usuario("",telefoneCont,null));
//                            }
//                            ((ListaContatosAdapter) listViewContatos.getAdapter()).notifyDataSetChanged();
//                        }else{
//
//                        }
//                    }
//
//                }
//            }
//        });


        //    carregaContatos();


//        new Thread() {
//            @Override
//            public void run() {
//                carregaContatos();
//                ConfiguracaoFirebase.getFirestore().collection("usuario")
//                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.getResult() != null) {
//
//                            List<DocumentSnapshot> usuarios = task.getResult().getDocuments();
//                            for (DocumentSnapshot documentSnapshot : usuarios) {
//                                for (Usuario user : listaDeContatos) {
//                                    if (user.getTelefone().equals(documentSnapshot.getId())) {
//                                        listaDeContatosRegistrados.add(user);
////                                        listViewContatos.post(new Runnable() {
////                                            @Override
////                                            public void run() {
////                                                ((ListaContatosAdapter) listViewContatos.getAdapter()).notifyDataSetChanged();
////                                            }
////                                        });
//                                        break;
//                                    }
//                                }
//                            }
//                            listViewContatos.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    listViewContatos.setAdapter(new ListaContatosAdapter(listaDeContatosRegistrados, getActivity()));
//
//                                }
//                            });
//                        }
//                    }
//                });
//
//
//            }
//        }.start();

    }

    private void sincronizaContatos() {
//        if(listaDeContatos != null && !listaDeContatos.isEmpty()){
//            for (Usuario usuario : listaDeContatos){
//                ConfiguracaoFirebase.getFirestore()
//                        .collection(Constantes.TABELA_USUARIOS)
//                        .document(usuario.)
//            }
//        }
    }



//    private String carregaNomeComplete(String contatoId) {
//        String nomeCompleto = "";
//        String whereName = ContactsContract.Data.MIMETYPE
//                + " = ? AND "
//                + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID
//                + " = ? ";
//        String[] whereNameParams = new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, contatoId};
//        Cursor nameCur = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, whereName, whereNameParams, ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
//        while (nameCur.moveToNext()) {
////            nomeCompleto += nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
////            nomeCompleto += nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
//            nomeCompleto += nameCur.getString(nameCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
//            break;
//        }
//        nameCur.close();
//        return nomeCompleto;
//    }




    //    private String[] FROM_COLUMNS_CONTATOS = {
//
//            ContactsContract.Contacts._ID,
//            Build.VERSION.SDK_INT
//                    >= Build.VERSION_CODES.HONEYCOMB ?
//                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
//                    ContactsContract.Contacts.DISPLAY_NAME,
//
//            ContactsContract.Contacts.HAS_PHONE_NUMBER
//
//    };


    public void filtrarContatos(String texto) {
        if (listViewContatos != null)
            ((ListaContatosAdapter) listViewContatos.getAdapter()).filtrar(texto);
    }

}
