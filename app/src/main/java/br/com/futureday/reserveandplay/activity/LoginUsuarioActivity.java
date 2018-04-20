package br.com.futureday.reserveandplay.activity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.util.data.PhoneNumberUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import br.com.futureday.reserveandplay.R;
import br.com.futureday.reserveandplay.config.ConfiguracaoFirebase;
import br.com.futureday.reserveandplay.config.ValidacoesFirestone;
import br.com.futureday.reserveandplay.model.Usuario;
import br.com.futureday.reserveandplay.utils.Utils;

public class LoginUsuarioActivity extends AppCompatActivity {

//    private FirebaseAuth autenticacao;
//    private TextView telefone;
//    private Button botaoCadastrar;
//    private Button botaoVerificarCodigo;
//    private Usuario usuario;
//    private ProgressBar progressBar;
//    private String verificadorId;
//    private String codigoVerificante;
//    private PhoneAuthProvider.ForceResendingToken tokenReenvio;
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificacaoCallbacks;
//    private boolean verificacaoEmProgresso = false;
//    private TextView codigoVerificador;
//    private SmsRetrieverClient client;
//
//
//    //Constantes
//    private static final String CHAVE_VERIFICACAO_EM_PROGRESSO = "chave_verificacao_em_progresso";
//    private static final int ESTADO_INICIAL = 1;
//    private static final int ESTADO_AGUARDAR_CODIGO = 2;

    private static final int RC_SIGN_IN = 1234;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);



        if (ConfiguracaoFirebase.isAutenticado()) {
            encaminharPaginaInicial();
        }else{
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.PhoneBuilder().build()))
                            .build(),
                    RC_SIGN_IN);

//            startActivityForResult(
//                    AuthUI.getInstance()
//                            .createSignInIntentBuilder()
//                            .setAvailableProviders(Arrays.asList(
//                                    new AuthUI.IdpConfig.EmailBuilder().build() ,
//                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
//                            .build(),
//                    RC_SIGN_IN);
        }


//        if (savedInstanceState != null) {
//            onRestoreInstanceState(savedInstanceState);
//        }
//
//        telefone = findViewById(R.id.cadastroTextTelefoneId);
//        telefone.setText("+" + Utils.GetCountryZipCode(this));
//        botaoCadastrar = findViewById(R.id.cadastroBotaoCadastroId);
//        progressBar = findViewById(R.id.cadastroProgressBarId);
//        codigoVerificador = findViewById(R.id.cadastroVeriricadorId);
//        botaoVerificarCodigo = findViewById(R.id.cadastroBotaoVerificacaoCodigoId);
//        desativarProgressBar();

//        if (verificacaoEmProgresso) {
//            atualizarTela(ESTADO_AGUARDAR_CODIGO);
//        } else {
//            atualizarTela(ESTADO_INICIAL);
//        }

//        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                disableViews(botaoCadastrar);
//                Utils.esconderTeclado(LoginUsuarioActivity.this);
//                if (validaCamposEnviarSMS()) {//Campos Ok
//                    usuario = new Usuario();
//                    usuario.setTelefone(telefone.getText().toString());
//
//                    Log.i("VERIFICA", "Tudo ok");
//
//                    iniciarVerificacaoTelefone();
//                    ativarProgressBar();
//                    //cadastrarUsuario();
//
//                }
//
//            }
//        });
//
//        botaoVerificarCodigo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                botaoCadastrar.setEnabled(false);
//                Utils.esconderTeclado(LoginUsuarioActivity.this);
//                if (validaCamposVerificacao()) {//Campos Ok
//                    usuario = new Usuario();
//                    usuario.setTelefone(telefone.getText().toString());
//
//                    Log.i("VERIFICA", "Tudo ok");
//
//                    verificarCodigo();
//                    ativarProgressBar();
//                    //cadastrarUsuario();
//
//                }
//
//            }
//        });
//
//        verificacaoCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                verificacaoEmProgresso = false;
//                Log.i("VERIFICA", phoneAuthCredential.getProvider());
//                desativarProgressBar();
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                Log.i("VERIFICA", e.getMessage());
//                verificacaoEmProgresso = false;
//                desativarProgressBar();
//            }
//
//            @Override
//            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//
//                verificadorId = s;
//                tokenReenvio = forceResendingToken;
//                desativarProgressBar();
//                verificarCodigo();
//            }
//
//        };
    }

    private void encaminharPaginaInicial() {
        Intent intent = new Intent(this, PaginaInicialActivity.class);
        startActivity(intent);
        finish();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
             //   cadastrarUsuario();
                encaminharPaginaInicial();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, getResources().getText(R.string.login_cancelado),Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, getResources().getText(R.string.login_sem_conexao),Toast.LENGTH_LONG).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, getResources().getText(R.string.login_unknown),Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(this, getResources().getText(R.string.login_cancelado),Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("RestrictedApi")
    private void cadastrarUsuario(){
        final String telefone = ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser().getPhoneNumber();
        DocumentReference documentReference = ConfiguracaoFirebase.getFirestore().collection("usuario").document(telefone);

        documentReference
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().exists()){
                        if(ConfiguracaoFirebase.isAutenticado()){

                            Usuario usuario = new Usuario();
                            usuario.setTelefone(PhoneNumberUtils.formatUsingCurrentCountry(telefone, getApplicationContext()));
                            usuario.setAuthor_id(ConfiguracaoFirebase.getFirebaseAuth().getCurrentUser().getUid());
                            usuario.salvar();
                        }
                    }else{

                    }
                }else{
                    cadastrarUsuario();
                }
            }
        });


    }

//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//    }
//
//    private boolean validaCamposEnviarSMS() {
//
//        if (telefone.getText().toString().isEmpty()) {
//            telefone.setError(getResources().getString(R.string.cadastro_telefone_obrigatorio));
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validaCamposVerificacao() {
//
//        if (codigoVerificador.getText().toString().isEmpty()) {
//            codigoVerificador.setError(getResources().getString(R.string.cadastro_senha_obrigatoria));
//            return false;
//        }
//        return true;
//    }
//
//
//

//
//    public void verificarCodigo() {
//        if (verificadorId != null && codigoVerificador.getText().length() != 0) {
//            FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
//            firebaseAuth.signInWithCredential(PhoneAuthProvider.getCredential(verificadorId, codigoVerificador.getText().toString()))
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                if (task.getResult().getUser() != null) {
//                                    encaminharPaginaInicial();
//                                }
//                            } else {
//
//                            }
//                            desativarProgressBar();
//                        }
//                    });
//
//        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean(CHAVE_VERIFICACAO_EM_PROGRESSO, true);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        verificacaoEmProgresso = savedInstanceState.getBoolean(CHAVE_VERIFICACAO_EM_PROGRESSO);
//    }
//
//    private void enableViews(View... views) {
//        for (View v : views) {
//            v.setEnabled(true);
//            v.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void disableViews(View... views) {
//        for (View v : views) {
//            v.setEnabled(false);
//            v.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    private void atualizarTela(int estadoId) {
//
//        switch (estadoId) {
//            case ESTADO_INICIAL:
//                disableViews(codigoVerificador, botaoVerificarCodigo);
//                enableViews(botaoCadastrar, telefone);
//                break;
//            case ESTADO_AGUARDAR_CODIGO:
//                disableViews(botaoCadastrar, telefone);
//                enableViews(codigoVerificador, botaoVerificarCodigo);
//                break;
//
//        }
//    }
//
//    private void ativarProgressBar() {
//        progressBar.setVisibility(ProgressBar.VISIBLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }
//
//    private void desativarProgressBar() {
//        progressBar.setVisibility(ProgressBar.GONE);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//    }


    //    private void cadastrarUsuario(){
//
//        progressBar.setVisibility(ProgressBar.VISIBLE);
//        autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
//        autenticacao.createUserWithEmailAndPassword(
//                usuario.getEmail(),
//                usuario.getSenha()
//        ).addOnCompleteListener(LoginUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                if(task.isSuccessful()){
//                    Toast.makeText(LoginUsuarioActivity.this,R.string.cadastro_sucesso,Toast.LENGTH_LONG).show();
//                    usuario.setId(task.getResult().getUser().getUid());
//                    usuario.salvar();
//                    progressBar.setVisibility(ProgressBar.INVISIBLE);
//                    Intent intent = new Intent(LoginUsuarioActivity.this,PaginaInicialActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else {
//                    try{
//                        progressBar.setVisibility(ProgressBar.INVISIBLE);
//                        botaoCadastrar.setEnabled(true);
//                        throw task.getException();
//                    }catch (FirebaseAuthWeakPasswordException fawe){
//                        senha.setError(getResources().getString(R.string.cadastro_senha_fraca));
//                    }catch (FirebaseAuthUserCollisionException faee){
//                        email.setError(getResources().getString(R.string.cadastro_usuario_existente));
//                    }catch (FirebaseAuthInvalidCredentialsException face){
//                        email.setError(getResources().getString(R.string.cadastro_email_invalido));
//                    }catch (Exception e){
//                        Toast.makeText(LoginUsuarioActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
//                    }finally {
//
//                    }
//
//                }
//
//
//            }
//        });
//    }
}
