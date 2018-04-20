package br.com.futureday.reserveandplay.utils;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.futureday.reserveandplay.R;

/**
 * Created by FJ on 07/02/2018.
 */

public final class Utils {

    public static void esconderTeclado(Activity activity) {

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static String codificarBase64(String texto) {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decodificarBase64(String texto) {
        return new String(Base64.decode(texto, Base64.DEFAULT));
    }

    public static String getStringFromDate(Date data){
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy HH:mm");

       return out.format(data);
    }

}
