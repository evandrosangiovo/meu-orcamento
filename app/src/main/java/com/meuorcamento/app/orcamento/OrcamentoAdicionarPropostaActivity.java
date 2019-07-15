package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.model.OrcamentoProposta;
import com.meuorcamento.model.OrcamentoPropostaInformacao;
import com.meuorcamento.model.UsuarioPerfil;
import com.meuorcamento.utils.formatters.Formatter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class OrcamentoAdicionarPropostaActivity extends BaseActivity {


    private static final String PARAM_ORCAMENTO = "PARAM_ORCAMENTO";
    private TextView tvHabilidade;
    private TextView tvTitulo;
    private TextView tvDataPrevista;
    private TextView tvDetalhes;
    private EditText etValorProposta;
    private EditText etObservacoes;
    private Orcamento orcamento;
    private UsuarioPerfil usuarioPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento_adicionar_proposta);

        tvTitulo = findViewById(R.id.tvTitulo);
        tvDataPrevista = findViewById(R.id.tvDataPrevista);

        tvHabilidade = findViewById(R.id.tvHabilidade);

        tvDetalhes = findViewById(R.id.tvDetalhes);

        etValorProposta = findViewById(R.id.etValorProposta);
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
                Toast.makeText(OrcamentoAdicionarPropostaActivity.this, "Falha para carregar o perfil do usuário", Toast.LENGTH_SHORT).show();
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
        Intent it = new Intent(context, OrcamentoAdicionarPropostaActivity.class);
        it.putExtra(PARAM_ORCAMENTO, orcamento);
        context.startActivity(it);
    }

    private boolean validateView() {
        boolean isValid = true;

        if(etObservacoes.getText().toString().trim().isEmpty()) {
            etObservacoes.setError("Dígite alguma informação na observação");
            isValid = false;
        }

        try {
            Formatter.getInstance(etValorProposta.getText(), Formatter.FormatTypeEnum.DECIMAL_DOIS).convert();
        }catch (Exception ex) {
            etValorProposta.setError("Valor digitado inválido");
            resetEtValorProposta();
            isValid = false;
        }

        return isValid;
    }

    private void resetEtValorProposta() {
        etValorProposta.setText(Formatter.getInstance(.0, Formatter.FormatTypeEnum.DECIMAL_DOIS).format());

        View currentFocus = getCurrentFocus();
        if(currentFocus != null)
            currentFocus.clearFocus();

        etValorProposta.requestFocus();
        etValorProposta.selectAll();
    }

    private double getValorPropostaFromView() throws ParseException {
        return (double)Formatter.getInstance(etValorProposta.getText().toString(), Formatter.FormatTypeEnum.DECIMAL_DOIS).convert();
    }

    private OrcamentoProposta getOrcamentoPropostaFromView() throws Exception {
        OrcamentoProposta orcamentoProposta = new OrcamentoProposta();
        OrcamentoPropostaInformacao orcamentoPropostaInformacao = new OrcamentoPropostaInformacao();
        orcamentoPropostaInformacao.setKeyOrcamento(orcamento.getKey());
        orcamentoPropostaInformacao.setObservacoes(etObservacoes.getText().toString());
        orcamentoPropostaInformacao.setValorProposta(getValorPropostaFromView());
        orcamentoPropostaInformacao.setUsuarioPerfil(usuarioPerfil);
        orcamentoPropostaInformacao.setUid(usuarioPerfil.getUid());
        orcamentoProposta.setUid(usuarioPerfil.getUid());
        orcamentoProposta.setOrcamentoPropostaInformacao(orcamentoPropostaInformacao);
        return orcamentoProposta;
    }

    public void btnAdicionarPropostaOnClick(View view) {
        if(!validateView())
            return;

        openProgressDialog();
        OrcamentoProposta orcamentoProposta = null;
        try {
            orcamentoProposta = getOrcamentoPropostaFromView();
        }catch (Exception ex) {
            Toast.makeText(OrcamentoAdicionarPropostaActivity.this, "Ocorreu erro no processamento", Toast.LENGTH_SHORT).show();
            return;
        }


        DatabaseReference databaseReferenceProposta = FirebaseUtil.getOrcamentosPropostaReference();
        orcamentoProposta.setKey(databaseReferenceProposta.push().getKey());

        Map<String, Object> orcamentoPropostaMap = new HashMap<>();
        orcamentoPropostaMap.put(String.format("HabilidadesXOrcamentos/%s/%s/orcamentoPropostaList/%s", orcamento.getHabilidade().getKey(), orcamento.getKey(), orcamentoProposta.getUid()), orcamentoProposta);
        orcamentoPropostaMap.put(String.format("Orcamentos/%s/%s/orcamentoPropostaList/%s", orcamento.getUid(), orcamento.getKey(), orcamentoProposta.getUid()), orcamentoProposta);
        orcamentoPropostaMap.put(String.format("OrcamentosProposta/%s/%s", orcamento.getKey(), orcamentoProposta.getUid()), orcamentoProposta);

        DatabaseReference databaseReference = FirebaseUtil.getBaseReference();
        databaseReference.updateChildren(orcamentoPropostaMap).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrcamentoAdicionarPropostaActivity.this, "Falha na tentativa de atualizar o orçamento", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                closeProgressDialog();
                finish();
            }
        });

        /*
        DatabaseReference ref = FirebaseUtil.getOrcamentosPropostaReference().child("q45KL2FJsUaT407pLY9Lt5Zrcp52").child(getUID());
        String key = ref.push().getKey();
        orcamentoProposta.setKey(key);
        openProgressDialog();
        ref.child(key).setValue(orcamentoProposta).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(OrcamentoAdicionarPropostaActivity.this, "Proposta enviada com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
                closeProgressDialog();
            }
        });
        */

    }
}
