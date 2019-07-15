package com.meuorcamento.app.perfil;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.model.OrcamentoAvaliacao;
import com.meuorcamento.model.UsuarioPerfil;
import com.meuorcamento.utils.formatters.Formatter;

import java.util.ArrayList;


public class UsuarioPerfilVisualizacaoActivity extends BaseActivity {

    private static final String PARAM_UID_PERFIL = "PARAM_UID_PERFIL";
    private TextView tvEmail;
    private TextView tvNome;
    private TextView tvCPF;
    private TextView tvIdentidade;
    private TextView tvCep;
    private TextView tvCidadeUF;
    private TextView tvBairro;
    private TextView tvEndereco;
    private TextView tvDataNascimento;
    private TextView tvAvaliacaoBom;
    private TextView tvAvaliacaoRegular;
    private TextView tvAvaliacaoRuim;
    private ListView listViewAvaliacoes;

    private UsuarioPerfil usuarioPerfil = null;
    private String uid = "";

    public UsuarioPerfilVisualizacaoActivity() {
    }

    public static void startActivity(Context context, String uid) {
        Intent it = new Intent(context, UsuarioPerfilVisualizacaoActivity.class);
        it.putExtra(PARAM_UID_PERFIL, uid);
        context.startActivity(it);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_usuario_perfil_visualizacao);

        tvEmail = findViewById(R.id.tvEmail);
        tvNome = findViewById(R.id.tvNome);
        tvCPF = findViewById(R.id.tvCPF);
        tvIdentidade = findViewById(R.id.tvIdentidade);
        tvCep = findViewById(R.id.tvCep);
        tvCidadeUF = findViewById(R.id.tvCidadeUF);
        tvBairro = findViewById(R.id.tvBairro);
        tvEndereco = findViewById(R.id.tvEndereco);
        tvDataNascimento = findViewById(R.id.tvDataNascimento);

        tvAvaliacaoBom = findViewById(R.id.tvAvaliacaoBom);
        tvAvaliacaoRegular = findViewById(R.id.tvAvaliacaoRegular);
        tvAvaliacaoRuim = findViewById(R.id.tvAvaliacaoRuim);

        listViewAvaliacoes = findViewById(R.id.listViewAvaliacoes);

        uid = getIntent().getStringExtra(PARAM_UID_PERFIL);

        loadPerfil();
    }


    private void loadPerfil() {
        openProgressDialog();
        FirebaseUtil.getUsuariosPerfisReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioPerfil = dataSnapshot.getValue(UsuarioPerfil.class);
                closeProgressDialog();
                updateView(usuarioPerfil);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UsuarioPerfilVisualizacaoActivity.this, "Falha para carregar o perfil do usuário", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
            }
        });
    }

    private void updateView(UsuarioPerfil usuarioPerfil) {

        tvEmail.setText(usuarioPerfil.getEmail());

        if(usuarioPerfil == null)
            return;

        tvNome.setText(Formatter.getInstance(String.format("%s %s", usuarioPerfil.getNome(), usuarioPerfil.getSobreNome()), Formatter.FormatTypeEnum.TITLE_CASE).format());
        tvCPF.setText(Formatter.getInstance(usuarioPerfil.getCpf(), Formatter.FormatTypeEnum.CPF).format());
        tvIdentidade.setText(usuarioPerfil.getIdentidade());
        tvCep.setText(Formatter.getInstance(usuarioPerfil.getCep(), Formatter.FormatTypeEnum.CEP).format());
        tvCidadeUF.setText(String.format("%s, %s", usuarioPerfil.getCidade(), usuarioPerfil.getUf()));
        tvBairro.setText(usuarioPerfil.getBairro());
        tvEndereco.setText(String.format("%s, %s", usuarioPerfil.getEndereco(), usuarioPerfil.getNumeroEstabelecimento()));
        tvDataNascimento.setText(Formatter.getInstance(usuarioPerfil.getDataNascimento(), Formatter.FormatTypeEnum.DATE).format());

        ArrayList<OrcamentoAvaliacao> orcamentoAvaliacoesList = null;
        if(usuarioPerfil.getUsuarioAvaliacaoList() != null)
            orcamentoAvaliacoesList = new ArrayList<>(usuarioPerfil.getUsuarioAvaliacaoList().values());

        listViewAvaliacoes.setAdapter(new UsuarioPerfilOrcamentosAvaliacoesAdapter(UsuarioPerfilVisualizacaoActivity.this, orcamentoAvaliacoesList));

        tvAvaliacaoBom.setVisibility(View.GONE);
        tvAvaliacaoRegular.setVisibility(View.GONE);
        tvAvaliacaoRuim.setVisibility(View.GONE);

        tvAvaliacaoBom.setText(Formatter.getInstance(usuarioPerfil.getAvaliacaoGeral(), Formatter.FormatTypeEnum.DECIMAL_UM).format());
        tvAvaliacaoRegular.setText(Formatter.getInstance(usuarioPerfil.getAvaliacaoGeral(), Formatter.FormatTypeEnum.DECIMAL_UM).format());
        tvAvaliacaoRuim.setText(Formatter.getInstance(usuarioPerfil.getAvaliacaoGeral(), Formatter.FormatTypeEnum.DECIMAL_UM).format());

        if (usuarioPerfil.getAvaliacaoGeral() < 3) {
            tvAvaliacaoRuim.setVisibility(View.VISIBLE);
        } else if (usuarioPerfil.getAvaliacaoGeral() == 3) {
            tvAvaliacaoRegular.setVisibility(View.VISIBLE);
        } else
            tvAvaliacaoBom.setVisibility(View.VISIBLE);
    }
}
