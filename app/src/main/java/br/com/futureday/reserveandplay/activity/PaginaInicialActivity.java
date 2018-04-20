package br.com.futureday.reserveandplay.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.util.data.PhoneNumberUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import br.com.futureday.reserveandplay.Adapter.TabAdapter;
import br.com.futureday.reserveandplay.R;
import br.com.futureday.reserveandplay.fragment.MesasFragment;
import br.com.futureday.reserveandplay.services.ServicoAtualizaContatos;
import br.com.futureday.reserveandplay.config.ConfiguracaoFirebase;
import br.com.futureday.reserveandplay.fragment.AmigosFragment;
import br.com.futureday.reserveandplay.model.Usuario;
import br.com.futureday.reserveandplay.utils.Permissao;

public class PaginaInicialActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  AmigosFragment amigosFragment;
    private  MesasFragment mesasFragment;
    private String[] permissoes = new String[]{
            Manifest.permission.READ_CONTACTS
    };

    private final int PICK_CONTACT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicial);

        if(Permissao.validaListaPermissoes(this,permissoes)){
            inicializa();
        }

        Permissao.validaPermissoes(this,permissoes);



    }

    public void inicializa(){
        //valida autenticação
        Intent intent = new Intent(this, ServicoAtualizaContatos.class);
        startService(intent);

        if(!ConfiguracaoFirebase.isAutenticado()){
            finish();
        }

        toolbar= findViewById(R.id.toolbar);
        toolbar.setTitle("ReserveDay");
//        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tl_tab);
        viewPager = findViewById(R.id.vp_pagina);
        tabLayout.setTabTextColors(ContextCompat.getColor(this,R.color.white),ContextCompat.getColor(this,R.color.white));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this,R.color.primaryLightColor));

        //Adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_CONTACT){

            if(data!=null) {

                Uri contactData = data.getData();
                Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
                if (cursor.moveToFirst()) {
                    String nome = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String telefone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            .replaceAll("^0+(?!$)", "").replace(" ", "").replace("-", "");
                    telefone = PhoneNumberUtils.formatUsingCurrentCountry(telefone,getApplicationContext());

                    Usuario usuario = new Usuario(nome, telefone, null);
                    AmigosFragment amigosFragment = getAmigosFragment();
                    if (amigosFragment != null) {
                        try {
                            amigosFragment.adicionarUsuario(usuario);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                cursor.close();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);

        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.item_pesquisa).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrar(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrar(newText);
                return true;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrar(((SearchView) v).getQuery().toString());
            }
        });
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private AmigosFragment getAmigosFragment(){
        viewPager.setCurrentItem(1);
        Fragment fr = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_pagina + ":" + viewPager.getCurrentItem());
        if(fr instanceof AmigosFragment){
            AmigosFragment amigosFragment = (AmigosFragment)fr;
            if(amigosFragment!=null) {
                return amigosFragment;
            }
        }
        return null;
    }

    private void filtrar(String texto) {
        if(amigosFragment==null){
            //amigosFragment = (AmigosFragment) ((TabAdapter)viewPager.getAdapter()).getItem(Constantes.TABLAYOUT_PAGINAINICIAL_AMIGOS);
            Fragment fr = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_pagina + ":" + viewPager.getCurrentItem());
            if(fr instanceof AmigosFragment){
                AmigosFragment amigosFragment = (AmigosFragment)fr;
                if(amigosFragment!=null) {
                    amigosFragment.filtrarContatos(texto);
                }
            }else if (fr instanceof MesasFragment){
                MesasFragment amigosFragment = (MesasFragment)fr;
                if(mesasFragment!=null) {
                    mesasFragment.filtrarMesas(texto);
                }
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_configuracao:
                return true;
            case R.id.item_adicionar_amigo:
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent,PICK_CONTACT);
                return true;
            case R.id.item_adicionar_mesa:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        for (int resultado: grantResults){

            if(resultado == PackageManager.PERMISSION_DENIED){
                    finish();

            }

        }
        if(!isFinishing()){
            inicializa();
        }
    }

    private void deslogarUsuario(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(PaginaInicialActivity.this, LoginUsuarioActivity.class));
                        finish();
                    }
                });
    }
}
