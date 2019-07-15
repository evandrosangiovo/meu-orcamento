package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.meuorcamento.R;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.model.OrcamentoProposta;

import java.util.ArrayList;
import java.util.List;

public class OrcamentoVisualizarPropostasActivity extends BaseActivity {

    private static final String PARAM_ORCAMENTO = "PARAM_ORCAMENTO";
    private static final String TAG = "OrcamentoVisualizarPropostas";

    private ListView listView;
    private OrcamentoPropostasAdapter orcamentoPropostasAdapter;

    private Orcamento orcamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento_visualizar_propostas);

        listView = findViewById(R.id.listView);
        orcamento = (Orcamento) getIntent().getExtras().get(PARAM_ORCAMENTO);
        final List<OrcamentoProposta> orcamentoPropostaList = new ArrayList<>();

        if(orcamento.getOrcamentoPropostaList() != null) {
            orcamentoPropostaList.addAll(orcamento.getOrcamentoPropostaList().values());
        }
        orcamentoPropostasAdapter = new OrcamentoPropostasAdapter(this, orcamentoPropostaList);
        listView.setAdapter(orcamentoPropostasAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OrcamentoProposta orcamentoProposta = orcamentoPropostaList.get(i);

                OrcamentoVisualizarPropostaActivity.startActivity(OrcamentoVisualizarPropostasActivity.this, orcamento, orcamentoProposta);
            }
        });
    }


    public static void startActivity(Context context, Orcamento orcamento) {
        Intent it = new Intent(context, OrcamentoVisualizarPropostasActivity.class);
        it.putExtra(PARAM_ORCAMENTO, orcamento);
        context.startActivity(it);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
