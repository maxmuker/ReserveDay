package br.com.futureday.reserveandplay.utils;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.firebase.ui.auth.util.data.PhoneNumberUtils;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.futureday.reserveandplay.config.ConfiguracaoFirebase;
import br.com.futureday.reserveandplay.model.Usuario;

/**
 * Created by FJ on 28/02/2018.
 */

public class GerenciarContatos {

    private static String MIMETYPE = "vnd.android.cursor.item/br.com.reserveday.contact";


    public static void addContato(Context context, Usuario usuario){

        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI,
                ContactsContract.RawContacts.ACCOUNT_TYPE + " = ? AND" + ContactsContract.RawContacts.CONTACT_ID + " = ? ",
                new String[]{"reserveday",usuario.getContatoId()});


        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.RawContacts.CONTENT_URI,true))
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,"br.com.reserveday.contatos")
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,"reserveday")
                .withValue(ContactsContract.RawContacts.CONTACT_ID,usuario.getContatoId())
                .build()
        );

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Settings.CONTENT_URI,true))
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME,"reserveday")
                .withValue(ContactsContract.Settings.UNGROUPED_VISIBLE,1)
                .build()
        );

        ops.add(ContentProviderOperation.newInsert(addCallerIsSyncAdapterParameter(ContactsContract.Data.CONTENT_URI,true))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,usuario.getTelefone())
                .withValue(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,usuario.getNome())
                .build()
        );

        try {
            ContentProviderResult[] results = contentResolver.applyBatch(ContactsContract.AUTHORITY,ops);
            if(results.length==0){

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }

    }

    private static Uri addCallerIsSyncAdapterParameter(Uri uri,boolean isSyncOperation){
        if(isSyncOperation){
            return uri.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER,"true").build();
        }
        return uri;
    }

    private static String[] FROM_COLUMNS_CONTATOS = {

            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID

    };


    public static Bitmap carregaFoto(String photoURI, ContentResolver contentResolver) {
        //Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contatoId));

        AssetFileDescriptor afd = null;

        if (photoURI == null || photoURI.isEmpty()) {
            return null;
        }
        try {

            Uri thumbUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                thumbUri = Uri.parse(photoURI);
            } else {

                final Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, photoURI);

                thumbUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            }
            afd = contentResolver.openAssetFileDescriptor(thumbUri, "r");

            FileDescriptor fileDescriptor = afd.getFileDescriptor();
            if (fileDescriptor != null) {

                return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null);
            }

        } catch (FileNotFoundException e) {

        } finally {
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {

                }
            }
        }
//
//        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, contactUri);
//        if (inputStream != null) {
//            return BitmapFactory.decodeStream(inputStream);
//        }
        return null;
    }

    public static List<Usuario> carregaContatos(Context context, ContentResolver contentResolver) {


        List<Usuario> listaDeContatos = new ArrayList<Usuario>();
//        Cursor cursor = contentResolver.query(
//                ContactsContract.Contacts.CONTENT_URI,
//                FROM_COLUMNS_CONTATOS,
//                null,
//                null,
//                (Build.VERSION.SDK_INT
//                        >= Build.VERSION_CODES.HONEYCOMB ?
//                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
//                        ContactsContract.Contacts.DISPLAY_NAME));
        Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                FROM_COLUMNS_CONTATOS,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        );

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {


                String photoThumbURI = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                //String nome = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String numeroSemZero = cursor
                        .getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        .replaceAll("^0+(?!$)", "").replace(" ", "").replace("-", "");
                String nome = cursor
                        .getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));


//                Cursor cursorRawContatct = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI,
//                        null,
//                        ContactsContract.RawContacts._ID + " = ? ",
//                        new String[]{rawContactId},
//                        null);
//                int temNumeroTelefone = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
//                if (temNumeroTelefone > 0) {
//                    Cursor cursorTelefones = contentResolver.query(
//                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ",
//                            new String[]{contatoId},
//                            null
//                    );

//                    while (cursorTelefones.moveToNext()) {
//                        String numeroSemZero = cursorTelefones
//                                .getString(cursorTelefones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                                .replaceAll("^0+(?!$)", "");
                @SuppressLint("RestrictedApi") final String numeroTelefone = PhoneNumberUtils.formatUsingCurrentCountry(
                        numeroSemZero,
                        context.getApplicationContext()
                );

                //final String nome = carregaNomeComplete(contatoId);
                Usuario usuario = new Usuario(nome, numeroTelefone, photoThumbURI);
                usuario.setContatoId(contactId);
                usuario.setAuthor_id(ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser().getUid());
                if (!listaDeContatos.contains(usuario)) {
                    listaDeContatos.add(usuario);
                }
//                if (!listaDeContatostelefone.contains(numeroTelefone)) {
//                    listaDeContatostelefone.add(numeroTelefone);
//                }


//                    }

//                    cursorTelefones.close();
//                }
            }

        }
        cursor.close();
        return listaDeContatos;
    }

}
