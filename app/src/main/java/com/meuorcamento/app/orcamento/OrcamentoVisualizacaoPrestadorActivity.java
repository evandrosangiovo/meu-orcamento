package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.utils.formatters.Formatter;

public class OrcamentoVisualizacaoPrestadorActivity extends BaseActivity {

    private static final String TAG = "OrcamentoVisualizacaoPrestador";
    private static final String PARAM_ORCAMENTO = "PARAM_ORCAMENTO";

    private View layoutPropostaAprovada;
    private TextView tvValorProposta;
    private TextView tvObservacoesProposta;

    private Button btnAdicionarProposta;
    private TextView tvHabilidade;
    private TextView tvTitulo;
    private TextView tvDataPrevista;
    private TextView tvDetalhes;
    private TextView tvCep;
    private TextView tvEndereco;
    private TextView tvBairro;
    private TextView tvEnderecoPontoReferencia;

    private Orcamento orcamento;

    private ChildEventListener childEventListener;
    //private DatabaseReference databaseReference;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento_visualizacao_prestador);

        tvTitulo = findViewById(R.id.tvTitulo);
        tvDataPrevista = findViewById(R.id.tvDataPrevista);

        tvHabilidade = findViewById(R.id.tvHabilidade);

        tvDetalhes = findViewById(R.id.tvDetalhes);
        tvCep = findViewById(R.id.tvCep);
        tvEndereco = findViewById(R.id.tvEndereco);
        tvBairro = findViewById(R.id.tvBairro);
        tvEnderecoPontoReferencia = findViewById(R.id.tvEnderecoPontoReferencia);

        layoutPropostaAprovada = findViewById(R.id.layoutPropostaAprovada);
        tvValorProposta = findViewById(R.id.tvValorProposta);
        tvObservacoesProposta = findViewById(R.id.tvObservacoesProposta);

        btnAdicionarProposta = findViewById(R.id.btnAdicionarProposta);

        orcamento = (Orcamento) getIntent().getExtras().get(PARAM_ORCAMENTO);
        updateView(orcamento);


        query = FirebaseUtil.getOrcamentosReference().equalTo(orcamento.getKey()).orderByKey();
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                orcamento = dataSnapshot.getValue(Orcamento.class);
                updateView(orcamento);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                orcamento = dataSnapshot.getValue(Orcamento.class);
                updateView(orcamento);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                finish();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        query.addChildEventListener(childEventListener);

    }

    public static void startActivity(Context context, Orcamento orcamento) {
        Intent it = new Intent(context, OrcamentoVisualizacaoPrestadorActivity.class);
        it.putExtra(PARAM_ORCAMENTO, orcamento);
        context.startActivity(it);
    }

    private void updateView(Orcamento orcamento) {

        tvHabilidade.setText(orcamento.getHabilidade().getTitulo());
        tvTitulo.setText(orcamento.getTitulo());
        tvDetalhes.setText(orcamento.getDetalhes());
        tvDataPrevista.setText(Formatter.getInstance(orcamento.getDataPrevista(), Formatter.FormatTypeEnum.DATE_EXTENSIVE_NO_TIME).format());

        tvEndereco.setText(String.format("%s, %s, %s / %s",
                Formatter.getInstance(orcamento.getEndereco(), Formatter.FormatTypeEnum.TITLE_CASE).format(),
                Formatter.getInstance(orcamento.getNumeroEstabelecimento(), Formatter.FormatTypeEnum.INTEIRO).format(),
                Formatter.getInstance(orcamento.getCidade(), Formatter.FormatTypeEnum.TITLE_CASE).format(),
                orcamento.getUf()));

        tvCep.setText(Formatter.getInstance(orcamento.getCep(), Formatter.FormatTypeEnum.CEP).format());
        tvBairro.setText(orcamento.getBairro());
        tvEnderecoPontoReferencia.setText(orcamento.getEnderecoPontoReferencia());

        if(orcamento.getPropostaAprovada() == null) {
            layoutPropostaAprovada.setVisibility(View.GONE);
        } else {
            layoutPropostaAprovada.setVisibility(View.VISIBLE);
            tvValorProposta.setText(Formatter.getInstance(orcamento.getPropostaAprovada().getOrcamentoPropostaInformacao().getValorProposta(), Formatter.FormatTypeEnum.DECIMAL_DOIS_MOEDA).format());
            tvObservacoesProposta.setText(orcamento.getPropostaAprovada().getOrcamentoPropostaInformacao().getObservacoes());
        }

        if(orcamento.getPropostaAprovada() == null)
            btnAdicionarProposta.setVisibility(View.VISIBLE);
        else
            btnAdicionarProposta.setVisibility(View.GONE);

    }

    public void btnVisualizarFotosOnClick(View view) {
        OrcamentoVisualizarFotoActivity.startActivity(this, orcamento);
    }

    public void btnAdicionarPropostaOnClick(View view) {
        OrcamentoAdicionarPropostaActivity.startActivity(this, orcamento);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        query.removeEventListener(childEventListener);
    }
}
