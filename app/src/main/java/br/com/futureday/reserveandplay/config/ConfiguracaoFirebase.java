package br.com.futureday.reserveandplay.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by FJ on 01/02/2018.
 */

public final class ConfiguracaoFirebase {


    private static FirebaseFirestore firebaseFirestore;
    private static FirebaseAuth firebaseAuth;
    private static ConnectivityManager cm;

    public static void iniciarlizaFireBaseAuth(){
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
    }

    public static FirebaseFirestore getFirestore(){
        if(firebaseFirestore == null){
            firebaseFirestore = FirebaseFirestore.getInstance();
        }
        return firebaseFirestore;
    }


    public static boolean isAutenticado(){

        iniciarlizaFireBaseAuth();
        return firebaseAuth.getCurrentUser() != null;
    }
    public static boolean isConectado(Context context){

        cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static FirebaseAuth getFirebaseAuth(){
        iniciarlizaFireBaseAuth();
        return firebaseAuth ;
    }

    public static void deslogarUsuario(){
        iniciarlizaFireBaseAuth();
        firebaseAuth.signOut();
    }

}
