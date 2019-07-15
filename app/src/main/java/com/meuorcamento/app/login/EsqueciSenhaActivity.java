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
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.utils.ValidacoesHelper;

public class EsqueciSenhaActivity extends BaseActivity {

    private EditText etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);
        etEmail = findViewById(R.id.etEmail);
    }

    public static void startActivity(Context context) {
        Intent it = new Intent(context, EsqueciSenhaActivity.class);
        context.startActivity(it);
    }

    public void btnRecuperarSenhaOnClick(View view) {
        String email = etEmail.getText().toString();
        if(!ValidacoesHelper.isValidEmail(email)) {
            Toast.makeText(this, "O email deve ser informado corretamente!", Toast.LENGTH_SHORT).show();
            return;
        }

        this.openProgressDialog();
        FirebaseUtil.sendPasswordResetEmail(email, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(EsqueciSenhaActivity.this, "email enviado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EsqueciSenhaActivity.this, "ocorreu um erro ao enviar o email, verifique o email digitado!", Toast.LENGTH_SHORT).show();
                }
                closeProgressDialog();

            }
        });
    }
}
