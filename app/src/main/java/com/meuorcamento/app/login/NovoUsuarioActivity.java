package com.meuorcamento.app.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.repositorios.RepoUsuario;
import com.meuorcamento.utils.ValidacoesHelper;

public class NovoUsuarioActivity extends BaseActivity {

    private EditText etEmail;
    private EditText etSenha;
    private EditText etConfirmacaoSenha;
    private RepoUsuario repoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etConfirmacaoSenha = findViewById(R.id.etConfirmacaoSenha);

        repoUsuario = new RepoUsuario(this);
    }

    public static void startActivity(Context context) {
        Intent it = new Intent(context, NovoUsuarioActivity.class);
        context.startActivity(it);
    }

    public void btnCriarNovoUsuarioOnClick(View view) {
        String email = etEmail.getText().toString();
        String senha = etSenha.getText().toString();
        String confirmacaoSenha = etConfirmacaoSenha.getText().toString();

        if (!senha.equals(confirmacaoSenha)) {
            Toast.makeText(this, "As senhas não conferem!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!ValidacoesHelper.isValidEmail(email)) {
            Toast.makeText(this, "O email deve ser informado corretamente!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUtil.createUserWithEmailAndPassword(email, senha, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUtil.sendEmailVerification();

                    Toast.makeText(NovoUsuarioActivity.this, "Usuário criado com sucesso, você recebera um email para ativar sua conta!", Toast.LENGTH_LONG).show();
                    finish();
                } else {

                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                    switch (errorCode) {
                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(NovoUsuarioActivity.this, "O email digitado não é válido", Toast.LENGTH_LONG).show();
                            etEmail.setError("O email está em um formato incorreto.");
                            etEmail.requestFocus();
                            etEmail.selectAll();
                            break;

                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            Toast.makeText(NovoUsuarioActivity.this, "O endereço de email já está em uso!", Toast.LENGTH_LONG).show();
                            etEmail.setError("Este email já está em uso.");
                            etEmail.requestFocus();
                            break;
                        case "ERROR_WEAK_PASSWORD":
                            Toast.makeText(NovoUsuarioActivity.this, "A senha deve possuir pelo menos 6 dígitos", Toast.LENGTH_LONG).show();
                            etSenha.setError("A senha deve possuir pelo menos 6 dígitos");
                            etSenha.requestFocus();
                            etSenha.setText("");
                            etConfirmacaoSenha.setText("");
                            break;

                        default: {
                            Toast.makeText(NovoUsuarioActivity.this, "Ocorreu um erro ao tentar criar o usuário", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                closeProgressDialog();
            }
        });
    }
}
