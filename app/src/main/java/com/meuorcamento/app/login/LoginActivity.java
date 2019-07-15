package com.meuorcamento.app.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.SystemConfiguration;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.R;
import com.meuorcamento.app.mainmenu.MenuSliderActivity;
import com.meuorcamento.app.perfil.UsuarioPerfilManutencaoActivity;
import com.meuorcamento.model.UsuarioPerfil;
import com.meuorcamento.utils.ValidacoesHelper;

public class LoginActivity extends BaseActivity {


    private static final String TAG = "LoginActivity";

    private EditText editTextEmail;
    private EditText editTextSenha;

    public static void startActivity(Context context) {
        Intent it = new Intent(context, LoginActivity.class);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);

        SystemConfiguration.criarDiretoriosDoSistema();

    }

    public void btnNovoUsuarioOnClick(View view) {
        NovoUsuarioActivity.startActivity(this);
    }

    public void btnEsqueciSenhaOnClick(View view) {
        EsqueciSenhaActivity.startActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


        if(!FirebaseUtil.userAuthenticated()) {
            return;
        }

        if(!FirebaseUtil.userEmailVerified())
        {
            Toast.makeText(LoginActivity.this, "Sua conta não está ativa, acesse seu email e ative-o", Toast.LENGTH_SHORT).show();
            return;
        }

        onAuthenticationSuccess();
    }


    private void signIn() {
        if (!validateForm()) {
            return;
        }

        openProgressDialog();

        String email = editTextEmail.getText().toString();
        String password = editTextSenha.getText().toString();

        FirebaseUtil.signInWithEmailAndPassword(email, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                closeProgressDialog();

                if (task.isSuccessful()) {
                    FirebaseUtil.getFirebaseUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final FirebaseUser user = FirebaseUtil.getFirebaseUser();
                            if (!user.isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Sua conta não está ativa, acesse seu email e ative-o", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            onAuthenticationSuccess();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Não foi possível efetuar o login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void onAuthenticationSuccess(){
        FirebaseUtil.loadUsuarioPerfil(this);
    }

    private boolean validateForm() {
        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();

        if (email == null || !ValidacoesHelper.isValidEmail(email)) {
            Toast.makeText(LoginActivity.this, "e-mail inválido", Toast.LENGTH_SHORT).show();
            return false;

        }

        if (senha == null || senha.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Senha inválida", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void btnEfetuarLoginOnClick(View view) {
        signIn();
    }

    @Override
    public void onDataReceived(Object result) {
        super.onDataReceived(result);

        UsuarioPerfil usuarioPerfil = (UsuarioPerfil)result;

        if(usuarioPerfil == null) {
            Toast.makeText(this, "Você deve completar o perfil para prosseguir.", Toast.LENGTH_SHORT).show();
            UsuarioPerfilManutencaoActivity.startActivity(this);
            finish();
            return;
        }

        MenuSliderActivity.startActivity(this);
        finish();

    }
}

