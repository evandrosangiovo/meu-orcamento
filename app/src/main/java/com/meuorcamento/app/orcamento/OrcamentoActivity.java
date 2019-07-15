package com.meuorcamento.app.orcamento;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.model.Habilidade;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.model.UsuarioPerfil;
import com.meuorcamento.utils.Mask;
import com.meuorcamento.utils.formatters.Formatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrcamentoActivity extends BaseActivity {

    private static final String TAG = "OrcamentoActivity";

    private View layoutHabilidades;
    private View layoutInformacoes;
    private ListView listView;
    private HabilidadeAdapter habilidadeAdapter;
    private List<Habilidade> habilidadeList;
    private Habilidade habilidadeSelecionada;
    private EditText etTitulo;
    private EditText etDataPrevista;
    private EditText etDetalhes;
    private EditText etCep;
    private EditText etEndereco;
    private EditText etCidade;
    private EditText etBairro;
    private EditText etUF;
    private EditText etNumeroEstabelecimento;
    private EditText etEnderecoPontoReferencia;

    Calendar calendarDataSelecionada = Calendar.getInstance();
    DatePickerDialog datePickerDialog;


    private UsuarioPerfil usuarioPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento);

        layoutHabilidades = findViewById(R.id.layoutHabilidades);
        layoutInformacoes = findViewById(R.id.layoutInformacoes);

        layoutHabilidades.setVisibility(View.VISIBLE);
        layoutInformacoes.setVisibility(View.GONE);

        etTitulo = findViewById(R.id.etTitulo);
        etDataPrevista = findViewById(R.id.etDataPrevista);
        etDataPrevista.setFocusable(false);
        etDataPrevista.setClickable(true);

        habilidadeList = new ArrayList<>();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendarDataSelecionada.set(Calendar.YEAR, year);
                calendarDataSelecionada.set(Calendar.MONTH, monthOfYear);
                calendarDataSelecionada.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEditTextDataPrevista();
            }
        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        etDataPrevista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        etDetalhes = findViewById(R.id.etDetalhes);
        etCep = findViewById(R.id.etCep);
        etCep.addTextChangedListener(Mask.insert("#####-###", etCep));
        etEndereco = findViewById(R.id.etEndereco);
        etCidade = findViewById(R.id.etCidade);
        etBairro = findViewById(R.id.etBairro);
        etUF = findViewById(R.id.etUF);
        etUF.addTextChangedListener(Mask.insert("##", etUF));
        etNumeroEstabelecimento = findViewById(R.id.etNumeroEstabelecimento);
        etEnderecoPontoReferencia = findViewById(R.id.etEnderecoPontoReferencia);
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                habilidadeSelecionada = (Habilidade) habilidadeAdapter.getItem(position);
                showLayoutInformacoes();

            }
        });

        loadData();
    }

    private void loadData() {
        openProgressDialog();
        FirebaseUtil.getHabilidesReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot item: dataSnapshot.getChildren()) {
                    habilidadeList.add(item.getValue(Habilidade.class));
                }
                updateListView(habilidadeList);
                closeProgressDialog();
                loadUsuarioPerfil();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OrcamentoActivity.this, "Falha para carregar as habilidades", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
                finish();
            }
        });
    }
    private void loadUsuarioPerfil() {
        openProgressDialog();
        FirebaseUtil.getUsuariosPerfisReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioPerfil = dataSnapshot.getValue(UsuarioPerfil.class);
                closeProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OrcamentoActivity.this, "Falha para carregar o perfil do usuário", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
                finish();
            }
        });
    }

    private void showLayoutInformacoes() {
        layoutInformacoes.setVisibility(View.VISIBLE);
        layoutHabilidades.setVisibility(View.GONE);
    }

    private void showLayoutHabilidades() {
        layoutInformacoes.setVisibility(View.GONE);
        layoutHabilidades.setVisibility(View.VISIBLE);
    }

    private void updateListView(List<Habilidade> habilidadeList) {
        if(habilidadeAdapter == null) {
            habilidadeAdapter = new HabilidadeAdapter(this, habilidadeList);
            listView.setAdapter(habilidadeAdapter);
            return;
        }

        habilidadeAdapter.addList(habilidadeList);
        habilidadeAdapter.notifyDataSetChanged();
        listView.refreshDrawableState();
    }

    private void updateEditTextDataPrevista() {
        etDataPrevista.setText(Formatter.getInstance(calendarDataSelecionada.getTime(), Formatter.FormatTypeEnum.DATE).format());
    }

    public static void startActivity(Context context) {
        Intent it = new Intent(context, OrcamentoActivity.class);
        context.startActivity(it);
    }


    public void btnSolicitarOrcamentoOnClick(View view) {
        if(!validateView())
            return;

        Orcamento orcamento = getModelFromView();

        DatabaseReference databaseReferenceOrcamentos = FirebaseUtil.getOrcamentosReference();
        String keyOrcamento = databaseReferenceOrcamentos.push().getKey();
        String keyHabilidade = orcamento.getHabilidade().getKey();
        orcamento.setKey(keyOrcamento);

        Map<String, Object> orcamentoMap = new HashMap<>();
        orcamentoMap.put(String.format("HabilidadesXOrcamentos/%s/%s", keyHabilidade, keyOrcamento), orcamento);
        orcamentoMap.put(String.format("Orcamentos/%s/%s", getUID(), keyOrcamento), orcamento);
        DatabaseReference databaseReference = FirebaseUtil.getBaseReference();


        openProgressDialog();
        databaseReference.updateChildren(orcamentoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(OrcamentoActivity.this, "Orçamento solicitado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
                closeProgressDialog();
            }
        });
    }

    private boolean validateView() {
        boolean isValid = true;

        if(etTitulo.getText().toString().trim().isEmpty()) {
            etTitulo.setError("Título deve ser informado");
            isValid = false;
        }

        try {
            String dataEditText = etDataPrevista.getText().toString();
            if (dataEditText.length() > 0) {
                Date dataPrevista = ((Date) Formatter.getInstance(dataEditText, Formatter.FormatTypeEnum.DATE).convert());
            }
        } catch (Exception ex) {
            etDataPrevista.setError("Data de nascimento inválida");
            isValid = false;
        }
        if(etDetalhes.getText().toString().trim().isEmpty()) {
            etDetalhes.setError("Sobrenome deve ser informado");
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

        return isValid;
    }

    private Orcamento getModelFromView() {
        Orcamento orcamento = new Orcamento();

        orcamento.setUsuarioPerfil(usuarioPerfil);
        orcamento.setUid(usuarioPerfil.getUid());

        orcamento.setUsuarioPerfil(usuarioPerfil);
        orcamento.setTitulo(etTitulo.getText().toString());
        orcamento.setDetalhes(etDetalhes.getText().toString());
        try {
            String dataEditText = etDataPrevista.getText().toString();
            orcamento.setDataPrevista((Date) Formatter.getInstance(dataEditText, Formatter.FormatTypeEnum.DATE).convert());
        }catch (Exception ex) {

        }

        orcamento.setCep(Mask.unmask(etCep.getText().toString()));
        orcamento.setUf(etUF.getText().toString());
        orcamento.setBairro(etBairro.getText().toString());
        orcamento.setEndereco(etEndereco.getText().toString());
        orcamento.setCidade(etCidade.getText().toString());
        orcamento.setEnderecoPontoReferencia(etEnderecoPontoReferencia.getText().toString());
        orcamento.setHabilidade(habilidadeSelecionada);
        orcamento.setDataLancamento(Calendar.getInstance().getTime());

        int numeroEstabelecimento = 0;
        try {
            numeroEstabelecimento = Integer.parseInt(etNumeroEstabelecimento.getText().toString());
        }catch (Exception ex) {
            numeroEstabelecimento = 0;
        }
        orcamento.setNumeroEstabelecimento(numeroEstabelecimento);

        return orcamento;
    }
    @Override
    public void onBackPressed() {

        if(layoutHabilidades.getVisibility() == View.VISIBLE) {
            this.finish();
            return;
        }


        showLayoutHabilidades();
    }
}
