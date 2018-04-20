package br.com.futureday.reserveandplay.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.futureday.reserveandplay.R;
import br.com.futureday.reserveandplay.config.ConfiguracaoFirebase;
import br.com.futureday.reserveandplay.utils.Utils;

public class MainActivity extends AppCompatActivity {

    TextView email = null;
    TextView senha = null;
    FirebaseAuth autenticacao;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ConfiguracaoFirebase.isAutenticado()) {
            Intent intent = new Intent(MainActivity.this, PaginaInicialActivity.class);
            startActivity(intent);
            finish();
        }
        progressBar = findViewById(R.id.loginProgressBarId);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        email = findViewById(R.id.loginEmailId);
        senha = findViewById(R.id.loginSenhaId);

    }

    public void cadastrarUsuario(View view) {
        Intent intent = new Intent(MainActivity.this, LoginUsuarioActivity.class);
        startActivity(intent);
    }

    public void realizarLogin(View view) {
        Utils.esconderTeclado(MainActivity.this);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        if (email.getText() == null || email.getText().toString().equals("")) {
            email.setError(getResources().getString(R.string.login_email_nao_informado));

        } else if (senha.getText() == null || senha.getText().toString().equals("")) {
            senha.setError(getResources().getString(R.string.cadastro_senha_obrigatoria));

        } else {

            autenticacao = ConfiguracaoFirebase.getFirebaseAuth();
            autenticacao.signInWithEmailAndPassword(email.getText().toString(), senha.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), getResources().getText(R.string.login_realizado_com_sucesso), Toast.LENGTH_LONG).show();
                                //Enviar para página inicial
                                Intent intent = new Intent(MainActivity.this, PaginaInicialActivity.class);
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                startActivity(intent);
                                finish();

                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    email.setError(getResources().getString(R.string.login_email_nao_cadastrado));
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    email.setError(getResources().getString(R.string.login_senha_incorreta));
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.erro), Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        progressBar.setVisibility(ProgressBar.INVISIBLE);


    }
}
