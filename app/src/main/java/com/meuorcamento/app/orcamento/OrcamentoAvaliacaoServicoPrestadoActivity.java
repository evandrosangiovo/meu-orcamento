package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.model.OrcamentoAvaliacao;
import com.meuorcamento.model.OrcamentoProposta;
import com.meuorcamento.model.OrcamentoPropostaInformacao;
import com.meuorcamento.model.UsuarioPerfil;
import com.meuorcamento.utils.formatters.Formatter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OrcamentoAvaliacaoServicoPrestadoActivity extends BaseActivity {


    private static final String PARAM_ORCAMENTO = "PARAM_ORCAMENTO";
    private TextView tvHabilidade;
    private TextView tvTitulo;
    private TextView tvDataPrevista;
    private TextView tvDetalhes;

    private RatingBar rbAvaliacao;
    private EditText etObservacoes;
    private Orcamento orcamento;
    private UsuarioPerfil usuarioPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento_avaliar_servico_prestado);

        tvTitulo = findViewById(R.id.tvTitulo);
        tvDataPrevista = findViewById(R.id.tvDataPrevista);
        tvHabilidade = findViewById(R.id.tvHabilidade);
        tvDetalhes = findViewById(R.id.tvDetalhes);

        rbAvaliacao = findViewById(R.id.rbAvaliacao);
        etObservacoes = findViewById(R.id.etObservacoes);

        orcamento = (Orcamento) getIntent().getExtras().get(PARAM_ORCAMENTO);

        loadUsuarioPerfil();
    }

    private void loadUsuarioPerfil() {
        openProgressDialog();
        FirebaseUtil.getUsuariosPerfisReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioPerfil = dataSnapshot.getValue(UsuarioPerfil.class);
                closeProgressDialog();
                updateView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OrcamentoAvaliacaoServicoPrestadoActivity.this, "Falha para carregar o perfil do usuário", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
                finish();
            }
        });
    }
    private void updateView() {

        tvHabilidade.setText(orcamento.getHabilidade().getTitulo());
        tvTitulo.setText(orcamento.getTitulo());
        tvDetalhes.setText(orcamento.getDetalhes());
        tvDataPrevista.setText(Formatter.getInstance(orcamento.getDataPrevista(), Formatter.FormatTypeEnum.DATE_EXTENSIVE_NO_TIME).format());

    }

    public static void startActivity(Context context, Orcamento orcamento) {
        Intent it = new Intent(context, OrcamentoAvaliacaoServicoPrestadoActivity.class);
        it.putExtra(PARAM_ORCAMENTO, orcamento);
        context.startActivity(it);
    }

    private boolean validateView() {
        boolean isValid = true;

        if(etObservacoes.getText().toString().trim().isEmpty()) {
            etObservacoes.setError("Dígite alguma informação na observação");
            isValid = false;
        }

        return isValid;
    }

    private OrcamentoAvaliacao getOrcamentoAvaliacaoFromView() {
        OrcamentoAvaliacao orcamentoAvaliacao = new OrcamentoAvaliacao();
        orcamentoAvaliacao.setKeyOrcamento(orcamento.getKey());
        orcamentoAvaliacao.setObservacoes(etObservacoes.getText().toString());
        orcamentoAvaliacao.setUsuarioPerfil(usuarioPerfil);
        orcamentoAvaliacao.setUid(usuarioPerfil.getUid());
        orcamentoAvaliacao.setNota(rbAvaliacao.getRating());
        orcamentoAvaliacao.setDataAvaliacao(Calendar.getInstance().getTime());

        return orcamentoAvaliacao;
    }

    public void btnAvaliarOnClick(View view) {
        if(!validateView())
            return;

        openProgressDialog();
        OrcamentoAvaliacao orcamentoAvaliacao = getOrcamentoAvaliacaoFromView();


        Map<String, Object> orcamentoAvaliacaoMap = new HashMap<>();
        orcamentoAvaliacaoMap.put(String.format("OrcamentoAvaliacao/%s/", orcamento.getKey()), orcamentoAvaliacao);
        orcamentoAvaliacaoMap.put(String.format("Orcamentos/%s/%s/orcamentoAvaliacao/", orcamento.getUid(), orcamento.getKey()), orcamentoAvaliacao);
        orcamentoAvaliacaoMap.put(String.format("UsuariosPerfis/%s/usuarioAvaliacaoList/%s/", orcamento.getPropostaAprovada().getUid(), orcamento.getPropostaAprovada().getKey()), orcamentoAvaliacao);

        DatabaseReference databaseReference = FirebaseUtil.getBaseReference();
        databaseReference.updateChildren(orcamentoAvaliacaoMap).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrcamentoAvaliacaoServicoPrestadoActivity.this, "Falha na tentativa de atualizar o orçamento", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                closeProgressDialog();
                Toast.makeText(OrcamentoAvaliacaoServicoPrestadoActivity.this, "Avaliação efetuado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
