package br.com.futureday.reserveandplay.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FJ on 24/02/2018.
 */

public class Permissao {

    public static boolean validaPermissoes(Activity activity, String[] permissoes){

        if(Build.VERSION.SDK_INT >= 23){

            List<String> listaDePermissoes = new ArrayList<String>();

            for (String permissao : permissoes){
                boolean valida = ContextCompat.checkSelfPermission( activity,permissao)==
                        PackageManager.PERMISSION_GRANTED;

                if(!valida){
                    listaDePermissoes.add(permissao);
                }
            }

            String[] array = new String[listaDePermissoes.size()];
            listaDePermissoes.toArray(array);
            if(!listaDePermissoes.isEmpty()){
                ActivityCompat.requestPermissions(activity,array,1);
            }


        }

        return true;
    }

    public static boolean validaListaPermissoes(Activity activity,String[] permissoes){
        for (String permissao : permissoes){
            boolean valida = ContextCompat.checkSelfPermission( activity,permissao)==
                    PackageManager.PERMISSION_GRANTED;

            if(!valida){
                return false;
            }
        }
        return true;
    }

}
