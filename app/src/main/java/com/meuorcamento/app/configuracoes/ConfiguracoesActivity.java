package com.meuorcamento.app.configuracoes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.app.orcamento.OrcamentoAdicionarPropostaActivity;
import com.meuorcamento.data.CodeScope;
import com.meuorcamento.data.Session;
import com.meuorcamento.model.Habilidade;
import com.meuorcamento.repositorios.RepoHabilidade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfiguracoesActivity extends BaseActivity {

    private static final String TAG = "ConfiguracoesActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
    }


    public static void startActivity(Context context) {
        Intent it = new Intent(context, ConfiguracoesActivity.class);
        context.startActivity(it);
    }
    public void btnCriarHabilidadesOnClick(View view) {

        final RepoHabilidade repoHabilidade  = new RepoHabilidade(this);

        List<Habilidade> habilidadesList = repoHabilidade.findAll();

        DatabaseReference ref = FirebaseUtil.getHabilidesReference();
        Map<String, Object> habilidadesMap = new HashMap<>();

        if(habilidadesList == null || habilidadesList.size() == 0) {
            habilidadesList = new ArrayList<>();
            habilidadesList.add(new Habilidade("Assistência Técnica - Ar Condicionado", "Limpeza, instalações e configurações de ar condicionados", "Assistência Técnica"));
            habilidadesList.add(new Habilidade("Informática - Residencial", "Manutenção, instalações ou configuraçoes de computadores, notebooks", "Assistência Técnica"));
            habilidadesList.add(new Habilidade("Informática - Empresarial", "Manutenção, instalações ou configuraçoes de servidores e redes", "Assistência Técnica"));
            habilidadesList.add(new Habilidade("Refrigeradores (Geladeiras e Freezers)", "Manutenção em geral", "Assistência Técnica"));

            habilidadesList.add(new Habilidade("Encanador", "Encanador", "Reformas e manutenções domésticas"));
            habilidadesList.add(new Habilidade("Eletrecista", "Serviços de instalações eletrícas em geral", "Reformas e manutenções domésticas"));
            habilidadesList.add(new Habilidade("Jardinagem", "Serviços de jardinagem em geral", "Reformas e manutenções domésticas"));
            habilidadesList.add(new Habilidade("Montador de móveis", "Serviço de montagem de móveis em geral", "Reformas e manutenções domésticas"));
            habilidadesList.add(new Habilidade("Paisagista", "Serviços de paisagista em geral", "Reformas e manutenções domésticas"));
            habilidadesList.add(new Habilidade("Pintor / Pedreiro", "Serviços de pinturas e pedreiro em geral", "Reformas e manutenções domésticas"));
            habilidadesList.add(new Habilidade("Vidraceiro", "Serviços de vidraçaria em geral", "Reformas e manutenções domésticas"));

            habilidadesList.add(new Habilidade("Babá", "Serviços de babá", "Serviços domésticos"));
            habilidadesList.add(new Habilidade("Cozinheira", "Serviços de cozinheira", "Serviços domésticos"));
            habilidadesList.add(new Habilidade("Diarista", "Serviços de diareista", "Serviços domésticos"));
            habilidadesList.add(new Habilidade("Babá de cães", "Serviços para cuidados de cães", "Serviços domésticos"));

            for (Habilidade item : habilidadesList) {
                item.setKey(ref.push().getKey());
                Log.i(TAG, item.getKey());
                habilidadesMap.put(item.getKey(), item);
            }
            repoHabilidade.insertOrUpdate(habilidadesList);
        }

        for (Habilidade item : habilidadesList) {
            habilidadesMap.put(item.getKey(), item);
        }

        openProgressDialog();
        ref.updateChildren(habilidadesMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ConfiguracoesActivity.this, "Habilidades cadastradas com sucesso!", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
            }
        });

        Toast.makeText(ConfiguracoesActivity.this, "Habilidades cadastradas com sucesso!", Toast.LENGTH_SHORT).show();

    }

    public void btnAdicionarOrcamentoPropostaOnClick(View view) {
        //OrcamentoAdicionarPropostaActivity.startActivity(this);
    }

}
