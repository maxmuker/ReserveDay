package br.com.futureday.reserveandplay.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.ui.auth.util.data.PhoneNumberUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.concurrent.ExecutionException;

import br.com.futureday.reserveandplay.utils.Utils;

/**
 * Created by FJ on 21/02/2018.
 */

public final class ValidacoesFirestone {

    private static boolean resultado = false;

    @SuppressLint("RestrictedApi")
    public static boolean usuarioExiste( Context context, DocumentReference documentReference) throws ExecutionException, InterruptedException {
        resultado = false;

        Task<DocumentSnapshot> docR = documentReference
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    //usuario não existe - retornar false
                    resultado = task.getResult().exists();
                }else{

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ERRO : ",e.getMessage());
            }
        });
        return resultado;
    }


}
