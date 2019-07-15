package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.meuorcamento.app.perfil.UsuarioPerfilVisualizacaoActivity;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.model.OrcamentoProposta;
import com.meuorcamento.model.OrcamentoPropostaInformacao;
import com.meuorcamento.model.UsuarioPerfil;
import com.meuorcamento.utils.formatters.Formatter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class OrcamentoVisualizarPropostaActivity extends BaseActivity {


    private static final String PARAM_ORCAMENTO = "PARAM_ORCAMENTO";
    private static final String PARAM_ORCAMENTO_PROPOSTA = "PARAM_ORCAMENTO_PROPOSTA";

    private TextView tvNomePrestador;
    private TextView tvHabilidade;
    private TextView tvTitulo;
    private TextView tvDataPrevista;
    private TextView tvDetalhes;
    private TextView tvValorProposta;
    private TextView tvObservacoes;
    private TextView tvAvaliacaoBom;
    private TextView tvAvaliacaoRegular;
    private TextView tvAvaliacaoRuim;

    private Button btnAutorizarProposta;
    private Orcamento orcamento;
    private OrcamentoProposta orcamentoProposta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento_visualizar_proposta);

        tvTitulo = findViewById(R.id.tvTitulo);
        tvDataPrevista = findViewById(R.id.tvDataPrevista);
        tvHabilidade = findViewById(R.id.tvHabilidade);
        tvDetalhes = findViewById(R.id.tvDetalhes);
        tvValorProposta = findViewById(R.id.tvValorProposta);
        tvObservacoes = findViewById(R.id.tvObservacoes);

        tvAvaliacaoBom = findViewById(R.id.tvAvaliacaoBom);
        tvAvaliacaoRegular = findViewById(R.id.tvAvaliacaoRegular);
        tvAvaliacaoRuim = findViewById(R.id.tvAvaliacaoRuim);

        tvNomePrestador  = findViewById(R.id.tvNomePrestador);
        btnAutorizarProposta = findViewById(R.id.btnAutorizarProposta);

        orcamento = (Orcamento) getIntent().getExtras().get(PARAM_ORCAMENTO);
        orcamentoProposta = (OrcamentoProposta) getIntent().getExtras().get(PARAM_ORCAMENTO_PROPOSTA);

        updateView();
    }

    private void updateView() {

        if(orcamento.getPropostaAprovada() != null) {
            btnAutorizarProposta.setVisibility(View.GONE);
        }

        tvHabilidade.setText(orcamento.getHabilidade().getTitulo());
        tvTitulo.setText(orcamento.getTitulo());
        tvDetalhes.setText(orcamento.getDetalhes());
        tvDataPrevista.setText(Formatter.getInstance(orcamento.getDataPrevista(), Formatter.FormatTypeEnum.DATE_EXTENSIVE_NO_TIME).format());

        tvNomePrestador.setText(String.format("%s %s", orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getNome(), orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getSobreNome()));
        tvObservacoes.setText(orcamentoProposta.getOrcamentoPropostaInformacao().getObservacoes());

        tvAvaliacaoBom.setVisibility(View.GONE);
        tvAvaliacaoRegular.setVisibility(View.GONE);
        tvAvaliacaoRuim.setVisibility(View.GONE);

        tvAvaliacaoBom.setText(Formatter.getInstance(orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getAvaliacaoGeral(), Formatter.FormatTypeEnum.DECIMAL_DOIS).format());
        tvAvaliacaoRegular.setText(Formatter.getInstance(orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getAvaliacaoGeral(), Formatter.FormatTypeEnum.DECIMAL_DOIS).format());
        tvAvaliacaoRuim.setText(Formatter.getInstance(orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getAvaliacaoGeral(), Formatter.FormatTypeEnum.DECIMAL_DOIS).format());

        if (orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getAvaliacaoGeral() < 3) {
            tvAvaliacaoRuim.setVisibility(View.VISIBLE);
        } else if (orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getAvaliacaoGeral() == 3) {
            tvAvaliacaoRegular.setVisibility(View.VISIBLE);
        } else
            tvAvaliacaoBom.setVisibility(View.VISIBLE);

        tvValorProposta.setText(Formatter.getInstance(orcamentoProposta.getOrcamentoPropostaInformacao().getValorProposta(), Formatter.FormatTypeEnum.DECIMAL_DOIS_MOEDA).format());

    }

    public static void startActivity(Context context, Orcamento orcamento, OrcamentoProposta orcamentoProposta) {
        Intent it = new Intent(context, OrcamentoVisualizarPropostaActivity.class);
        it.putExtra(PARAM_ORCAMENTO, orcamento);
        it.putExtra(PARAM_ORCAMENTO_PROPOSTA, orcamentoProposta);
        context.startActivity(it);
    }

    public void btnVisualizarPerfilPrestadorOnClick(View view) {
        UsuarioPerfilVisualizacaoActivity.startActivity(this, orcamentoProposta.getUid());
    }

    public void btnAceitarPropostaOnClick(View view) {

        openProgressDialog();

        orcamento.setPropostaAprovada(orcamentoProposta);
        Map<String, Object> orcamentoPropostaAprovada = new HashMap<>();

        orcamentoPropostaAprovada.put(String.format("Orcamentos/%s/%s/propostaAprovada/", orcamento.getUid(), orcamento.getKey()), orcamentoProposta);
        orcamentoPropostaAprovada.put(String.format("HabilidadesXOrcamentos/%s/%s/", orcamento.getHabilidade().getKey(), orcamento.getKey()), null);
        orcamentoPropostaAprovada.put(String.format("OrcamentosAprovados/%s/%s/", orcamento.getPropostaAprovada().getUid(), orcamento.getKey()), orcamento);

        DatabaseReference databaseReference = FirebaseUtil.getBaseReference();
        databaseReference.updateChildren(orcamentoPropostaAprovada).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrcamentoVisualizarPropostaActivity.this, "Falha na tentativa de atualizar o orçamento", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                closeProgressDialog();
                Toast.makeText(OrcamentoVisualizarPropostaActivity.this, "Aprovação efetuada com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
