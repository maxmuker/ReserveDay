package br.com.futureday.reserveandplay.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import br.com.futureday.reserveandplay.config.ConfiguracaoFirebase;
import br.com.futureday.reserveandplay.model.Usuario;
import br.com.futureday.reserveandplay.utils.Constantes;
import br.com.futureday.reserveandplay.utils.GerenciarContatos;


public class ServicoAtualizaContatos extends IntentService {


    public ServicoAtualizaContatos() {
        super("ServicoAtualizaContatos");
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d("TESTE","TESTE ON START COMMAND");
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final List<Usuario> listaDeContatos = GerenciarContatos.carregaContatos(getApplicationContext(), getContentResolver());
            for (Usuario usuario : listaDeContatos) {
                ConfiguracaoFirebase.getFirestore().collection(Constantes.TABELA_USUARIOS).document(usuario.getTelefone()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if(task.getResult()!=null && task.getResult().exists()){
                                       int posicao= listaDeContatos.indexOf(new Usuario("",task.getResult().getId().toString(),null));
                                       ConfiguracaoFirebase.getFirestore().collection(Constantes.TABELA_USUARIOS)
                                               .document(ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser().getPhoneNumber())
                                               .collection(Constantes.TABELA_USUARIOS_CONTATOS)
                                               .document(listaDeContatos.get(posicao).getTelefone())
                                               .set(listaDeContatos.get(posicao))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(!task.isSuccessful()){
                                                            Log.d("ERRO - ",task.getException().getMessage());
                                                        }
                                                    }
                                                });
                                    }

                                } else {


                                }

                            }
                        });
            }
        }
    }


}
