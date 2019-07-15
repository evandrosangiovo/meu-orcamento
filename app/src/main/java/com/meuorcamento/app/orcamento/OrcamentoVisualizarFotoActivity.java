package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.SystemConfiguration;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.utils.GenericFileProvider;
import com.meuorcamento.utils.IOHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrcamentoVisualizarFotoActivity extends BaseActivity {

    private static final String PARAM_ORCAMENTO = "PARAM_ORCAMENTO";
    private static final String TAG = "OrcamentoVisualizarFoto";
    private List<String> fotosOrcamento = new ArrayList<>();

    private ListView listView;
    private OrcamentoFotoAdapter orcamentoFotoAdapter;

    private Orcamento orcamento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento_visualizar_foto);

        listView = findViewById(R.id.listView);
        orcamento = (Orcamento) getIntent().getExtras().get(PARAM_ORCAMENTO);
        fotosOrcamento = orcamento.getFotosList();
        orcamentoFotoAdapter = new OrcamentoFotoAdapter(this, fotosOrcamento);
        listView.setAdapter(orcamentoFotoAdapter);
        efetuarDownloadFotos();
    }

    private void efetuarDownloadFotos() {

        if(fotosOrcamento.isEmpty())
            return;

        final StorageReference fotoStorageReference = FirebaseUtil.getFotosOrcamentosStorageReference(orcamento.getKey());

        for (final String item : fotosOrcamento) {

            File file = new File(SystemConfiguration.FOLDER_IMAGENS, item);
            if(file.exists()) {
                updateListView();
                continue;
            }

            fotoStorageReference.child(item).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    File localFile = new File(SystemConfiguration.FOLDER_IMAGENS, item);
                    fotoStorageReference.child(item).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            updateListView();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    });
                }
            });
        }
    }


    public static void startActivity(Context context, Orcamento orcamento) {
        Intent it = new Intent(context, OrcamentoVisualizarFotoActivity.class);
        it.putExtra(PARAM_ORCAMENTO, orcamento);
        context.startActivity(it);
    }

    private void updateListView() {
        orcamentoFotoAdapter.addList(fotosOrcamento);
        orcamentoFotoAdapter.notifyDataSetChanged();
        listView.refreshDrawableState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_orcamento_adicionar_foto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default: {

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
