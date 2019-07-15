package com.meuorcamento.app.perfil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.model.UsuarioPerfil;
import com.meuorcamento.repositorios.RepoUsuarioPerfil;
import com.meuorcamento.utils.Mask;
import com.meuorcamento.utils.ValidacoesHelper;
import com.meuorcamento.utils.formatters.Formatter;

import java.util.Date;

public class UsuarioPerfilManutencaoActivity extends BaseActivity {


    private static final String TAG = "PerfilManutencao";
    private TextView tvEmail;
    private TextView tvEmailVerificado;
    private TextView tvEmailNaoVerificado;
    private EditText etNome;
    private EditText etSobrenome;
    private EditText etDataNascimento;
    private EditText etCelular;
    private EditText etSkype;
    private EditText etCPF;
    private EditText etIdentidade;

    private EditText etCep;
    private EditText etCidade;
    private EditText etUF;
    private EditText etEndereco;
    private EditText etNumeroEstabelecimento;
    private EditText etBairro;

    private RepoUsuarioPerfil repoUsuarioPerfil;

    public static void startActivity(Context context) {
        Intent openMainActivity = new Intent(context, UsuarioPerfilManutencaoActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        context.startActivity(openMainActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil_manutencao);
        repoUsuarioPerfil = new RepoUsuarioPerfil(this);
        tvEmail = findViewById(R.id.tvEmail);
        tvEmailVerificado = findViewById(R.id.tvEmailVerificado);
        tvEmailNaoVerificado = findViewById(R.id.tvEmailNaoVerificado);
        etNome = findViewById(R.id.etNome);
        etSobrenome = findViewById(R.id.etSobrenome);
        etDataNascimento = findViewById(R.id.etDataNascimento);
        etDataNascimento.addTextChangedListener(Mask.insert("##/##/####", etDataNascimento));
        etCelular = findViewById(R.id.etCelular);
        etCelular.addTextChangedListener(Mask.insert("(##) #####-####", etCelular));
        etSkype = findViewById(R.id.etSkype);
        etCPF = findViewById(R.id.etCPF);
        etCPF.addTextChangedListener(Mask.insert("###.###.###-##", etCPF));
        etIdentidade = findViewById(R.id.etIdentidade);
        etNumeroEstabelecimento = findViewById(R.id.etNumeroEstabelecimento);

        etCep = findViewById(R.id.etCep);
        etCep.addTextChangedListener(Mask.insert("#####-###", etCep));
        etCidade = findViewById(R.id.etCidade);
        etUF = findViewById(R.id.etUF);
        etEndereco = findViewById(R.id.etEndereco);
        etBairro = findViewById(R.id.etBairro);

        FirebaseUtil.loadUsuarioPerfil(this);
    }

    private UsuarioPerfil usuarioPerfil = null;
    @Override
    public void onDataReceived(Object result) {
        super.onDataReceived(result);

        usuarioPerfil = (UsuarioPerfil)result;

        updateView(usuarioPerfil);
    }

    @Override
    public void onError(DatabaseError databaseError) {
        super.onError(databaseError);
    }

    private void updateView(UsuarioPerfil usuarioPerfil) {
        FirebaseUser firebaseUser = FirebaseUtil.getFirebaseUser();

        if(firebaseUser != null) {
            tvEmail.setText(firebaseUser.getEmail());
            Log.i(TAG, firebaseUser.getEmail());
            if(firebaseUser.isEmailVerified()) {
                tvEmailVerificado.setVisibility(View.VISIBLE);
            } else {
                tvEmailNaoVerificado.setVisibility(View.VISIBLE);
            }
        }

        if(usuarioPerfil == null)
            return;

        tvEmail.setText(usuarioPerfil.getEmail());
        etNome.setText(usuarioPerfil.getNome());
        etSobrenome.setText(usuarioPerfil.getSobreNome());
        etCPF.setText(Formatter.getInstance(usuarioPerfil.getCpf(), Formatter.FormatTypeEnum.CPF).format());
        etIdentidade.setText(usuarioPerfil.getIdentidade());
        if(!usuarioPerfil.getCelular().isEmpty()) {
            etCelular.setActivated(false);
        } else {
            etCelular.setActivated(true);
        }
        etCelular.setText(Formatter.getInstance(usuarioPerfil.getCelular(), Formatter.FormatTypeEnum.TELEFONE).format());
        etSkype.setText(usuarioPerfil.getSkype());

        etCep.setText(Formatter.getInstance(usuarioPerfil.getCep(), Formatter.FormatTypeEnum.CEP).format());
        etUF.setText(usuarioPerfil.getUf());
        etBairro.setText(usuarioPerfil.getBairro());
        etEndereco.setText(usuarioPerfil.getEndereco());
        etNumeroEstabelecimento.setText(Formatter.getInstance(usuarioPerfil.getNumeroEstabelecimento(), Formatter.FormatTypeEnum.INTEIRO).format());
        etCidade.setText(usuarioPerfil.getCidade());
        etDataNascimento.setText(Formatter.getInstance(usuarioPerfil.getDataNascimento(), Formatter.FormatTypeEnum.DATE).format());
    }

    private boolean validateView() {
        boolean isValid = true;

        if(etNome.getText().toString().trim().isEmpty()) {
            etNome.setError("Nome deve ser informado");
            isValid = false;
        }

        if(etSobrenome.getText().toString().trim().isEmpty()) {
            etSobrenome.setError("Sobrenome deve ser informado");
            isValid = false;
        }

        if (!ValidacoesHelper.isValidCPF(etCPF.getText().toString().trim())) {
            etCPF.setError("CPF inválido");
            isValid = false;
        }

        if(etIdentidade.getText().toString().trim().isEmpty()) {
            etIdentidade.setError("Celular inválido");
            isValid = false;
        }

        if(etCelular.getText().toString().trim().isEmpty()) {
            etCelular.setError("Celular inválido");
            isValid = false;
        }

        if(etEndereco.getText().toString().trim().isEmpty()) {
            etEndereco.setError("O endereço deve ser informado");
            isValid = false;
        }

        try {
            int numeroEstabelecimento = Integer.parseInt(etNumeroEstabelecimento.getText().toString());
        }catch (Exception ex) {
            etNumeroEstabelecimento.setError("Número do estabelecimento inválido");
            isValid = false;
        }

        if(etCep.getText().toString().trim().isEmpty()) {
            etCep.setError("CEP inválido...");
            isValid = false;
        }
        if(etBairro.getText().toString().trim().isEmpty()) {
            etBairro.setError("O bairro deve ser informado");
            isValid = false;
        }

        try {
            String dataEditText = etDataNascimento.getText().toString();
            if (dataEditText.length() > 0) {
                Date dataNascimento = ((Date) Formatter.getInstance(dataEditText, Formatter.FormatTypeEnum.DATE).convert());
            }
        } catch (Exception ex) {
            etDataNascimento.setError("Data de nascimento inválida");
            isValid = false;
        }
        return isValid;
    }

    public void btnGravarOnClick(View view) {
        if(!validateView())
            return;

        UsuarioPerfil usuarioPerfil = getModelFromView();
        repoUsuarioPerfil.insertOrUpdate(usuarioPerfil);
        DatabaseReference ref = FirebaseUtil.getUsuariosPerfisReference();
        ref.setValue(usuarioPerfil).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(UsuarioPerfilManutencaoActivity.this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private UsuarioPerfil getModelFromView() {
        if(usuarioPerfil == null) {
            usuarioPerfil = new UsuarioPerfil();
            usuarioPerfil.setUid(getUID());
        }

        FirebaseUser firebaseUser = FirebaseUtil.getFirebaseUser();
        if(firebaseUser != null) {
            usuarioPerfil.setEmail(firebaseUser.getEmail());
        }

        usuarioPerfil.setNome(etNome.getText().toString());
        usuarioPerfil.setSobreNome(etSobrenome.getText().toString());

        usuarioPerfil.setCelular(Mask.unmask(etCelular.getText().toString()));
        usuarioPerfil.setSkype(etSkype.getText().toString());
        usuarioPerfil.setCpf(Mask.unmask(etCPF.getText().toString()));
        usuarioPerfil.setIdentidade(etIdentidade.getText().toString());

        usuarioPerfil.setCep(Mask.unmask(etCep.getText().toString()));
        usuarioPerfil.setUf(etUF.getText().toString());
        usuarioPerfil.setBairro(etBairro.getText().toString());
        usuarioPerfil.setEndereco(etEndereco.getText().toString());
        usuarioPerfil.setCidade(etCidade.getText().toString());
        int numeroEstabelecimento = 0;
        try {
            numeroEstabelecimento = Integer.parseInt(etNumeroEstabelecimento.getText().toString());
        }catch (Exception ex) {
            numeroEstabelecimento = 0;
        }
        usuarioPerfil.setNumeroEstabelecimento(numeroEstabelecimento);
        try {
            String dataEditText = etDataNascimento.getText().toString();
            if (dataEditText.length() > 0) {
                usuarioPerfil.setDataNascimento((Date) Formatter.getInstance(dataEditText, Formatter.FormatTypeEnum.DATE).convert());
            }
        } catch (Exception ex) {

        }


        return usuarioPerfil;
    }
}
