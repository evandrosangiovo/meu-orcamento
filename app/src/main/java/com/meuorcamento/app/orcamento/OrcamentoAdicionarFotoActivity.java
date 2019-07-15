package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class OrcamentoAdicionarFotoActivity extends BaseActivity {

    private static final String PARAM_ORCAMENTO = "PARAM_ORCAMENTO";
    private static final String TAG = "OrcamentoAdicionarFoto";
    private List<String> fotosSelecionadasList = new ArrayList<>();
    private List<String> fotosExistentesStorageList = new ArrayList<>();

    private ListView listView;
    private OrcamentoEnviarFotoAdapter orcamentoEnviarFotoAdapter;

    private Orcamento orcamento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orcamento_adicionar_foto);

        listView = findViewById(R.id.listView);
        orcamento = (Orcamento) getIntent().getExtras().get(PARAM_ORCAMENTO);
        fotosSelecionadasList = orcamento.getFotosList();
        fotosExistentesStorageList = orcamento.getFotosList();
        //limparImagens();
        orcamentoEnviarFotoAdapter = new OrcamentoEnviarFotoAdapter(this, fotosSelecionadasList);
        listView.setAdapter(orcamentoEnviarFotoAdapter);
        efetuarDownloadFotos();
    }

    private void efetuarDownloadFotos() {

        if(fotosExistentesStorageList.isEmpty())
            return;

        //openProgressDialog();
        final StorageReference fotoStorageReference = FirebaseUtil.getFotosOrcamentosStorageReference(orcamento.getKey());

        for (final String item : fotosExistentesStorageList) {

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
        Intent it = new Intent(context, OrcamentoAdicionarFotoActivity.class);
        it.putExtra(PARAM_ORCAMENTO, orcamento);
        context.startActivity(it);
    }

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public void btnAdicionarNovaFotoOnClick(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            btnAdicionarNovaFotoBeforeLollipopOnClick(view);
        }
        else {
            btnAdicionarNovaFotoAfterLollipopOnClick(view);
        }
    }
    private void btnAdicionarNovaFotoAfterLollipopOnClick(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            photoFile = createImageFile();

            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, GenericFileProvider.getUriForFile(this, "com.meuorcamento.utils.genericfileprovider", photoFile));
                //cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private void btnAdicionarNovaFotoBeforeLollipopOnClick(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            photoFile = createImageFile();

            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                //cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }



    private File createImageFile() {

        File imageFile = new File(SystemConfiguration.FOLDER_IMAGENS, getFileName());
        return imageFile;
    }

    private String getFileName() {
        return String.format("%s_%s.jpg", orcamento.getKey(), String.valueOf(fotosSelecionadasList.size()));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            fotosSelecionadasList.add(getFileName());
        }

        updateListView();
    }

    private void updateListView() {
        orcamentoEnviarFotoAdapter.addList(fotosSelecionadasList);
        orcamentoEnviarFotoAdapter.notifyDataSetChanged();
        listView.refreshDrawableState();

    }

    private void limparImagens() {
        IOHelper.clearDirectory( String.format("%s%s", SystemConfiguration.FOLDER_IMAGENS, orcamento.getKey()));
    }

    public void btnEnviarFotosOnClick(View view) {

        openProgressDialog();
        final StorageReference storageReference = FirebaseUtil.getFotosOrcamentosStorageReference();

        final StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        //final List<UploadTask> uploadTaskList = new ArrayList<>(fotosSelecionadasList.size());
        for (final String item : fotosSelecionadasList) {

            storageReference.child(orcamento.getKey()).child(item).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //Toast.makeText(OrcamentoAdicionarFotoActivity.this,"Arquivo "+ item + ": 100% done", Toast.LENGTH_SHORT).show();
                    if(!fotosExistentesStorageList.contains(item))
                        fotosExistentesStorageList.add(item);
                    verificarEnvioConcluido();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    int errorCode = ((StorageException) exception).getErrorCode();
                    if (errorCode != StorageException.ERROR_OBJECT_NOT_FOUND)
                        return;

                    storageReference.child(orcamento.getKey()).child(item).putFile(Uri.fromFile(new File(String.format("%s%s", SystemConfiguration.FOLDER_IMAGENS, item))), metadata)
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    Toast.makeText(OrcamentoAdicionarFotoActivity.this, "Arquivo " + item + ":" + progress + "% done", Toast.LENGTH_SHORT).show();
                                }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (!fotosExistentesStorageList.contains(item))
                                fotosExistentesStorageList.add(item);
                            verificarEnvioConcluido();
                        }
                    });
                }
            });


        }
    }

    private void updateOrcamentoFotosEnviadas() {

        Map<String, Object> orcamentoFotosMap = new HashMap<>();
        orcamentoFotosMap.put(String.format("HabilidadesXOrcamentos/%s/%s/fotosList/", orcamento.getHabilidade().getKey(), orcamento.getKey()), fotosExistentesStorageList);
        orcamentoFotosMap.put(String.format("Orcamentos/%s/%s/fotosList/", getUID(), orcamento.getKey()), fotosExistentesStorageList);

        DatabaseReference databaseReference = FirebaseUtil.getBaseReference();
        databaseReference.updateChildren(orcamentoFotosMap).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrcamentoAdicionarFotoActivity.this, "Falha na tentativa de atualizar o orçamento", Toast.LENGTH_SHORT).show();
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
        FirebaseUtil.getOrcamentosFotosListReference(orcamento.getKey()).setValue(fotosExistentesStorageList).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrcamentoAdicionarFotoActivity.this, "Falha na tentativa de atualizar o orçamento", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(OrcamentoAdicionarFotoActivity.this, "Orçamento atualizado com sucesso", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
                finish();
            }
        });
        */
    }

    private void verificarEnvioConcluido() {

        if (!(fotosSelecionadasList.size() == fotosExistentesStorageList.size()))
            return ;

        updateOrcamentoFotosEnviadas();
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
