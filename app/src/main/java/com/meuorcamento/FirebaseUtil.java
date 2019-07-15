package com.meuorcamento;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.meuorcamento.app.interfaces.IFirebaseResult;
import com.meuorcamento.model.UsuarioPerfil;

public class FirebaseUtil {



    private static FirebaseDatabase firebaseDatabase;
    public static boolean userAuthenticated() {

        if(getFirebaseAuthInstance().getCurrentUser() == null)
            return false;


        return true;
    }

    public static boolean userEmailVerified() {
        FirebaseUser firebaseUser = getFirebaseUser();

        if(firebaseUser == null)
            return false;

        if(!firebaseUser.isEmailVerified())
            return false;

        return true;
    }

    public static FirebaseUser getFirebaseUser() {
        return getFirebaseAuthInstance().getCurrentUser();
    }

    public static void signInWithEmailAndPassword(String email, String password, OnCompleteListener onCompleteListener) {
        getFirebaseAuthInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(onCompleteListener);
    }

    public static void createUserWithEmailAndPassword(String email, String senha, OnCompleteListener onCompleteListener){
        getFirebaseAuthInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener(onCompleteListener);
    }

    public static void sendPasswordResetEmail(String email, OnCompleteListener onCompleteListener) {

        getFirebaseAuthInstance().sendPasswordResetEmail(email).addOnCompleteListener(onCompleteListener);
    }

    public static void sendEmailVerification() {
        getFirebaseUser().sendEmailVerification();
    }

    public static void signOut() {
        if(userAuthenticated())
            getFirebaseAuthInstance().signOut();
    }

    public static FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }

    public static String getUid() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser == null)
            return "";
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public static DatabaseReference getBaseReference() {
        if(firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            //firebaseDatabase.setPersistenceEnabled(true);

        }


        return firebaseDatabase.getReference();
    }

    public static StorageReference getBaseStorageReference() {
        return FirebaseStorage.getInstance().getReference();
    }

    public static StorageReference getFotosOrcamentosStorageReference() {
        return getBaseStorageReference().child("Orcamentos");
    }

    public static StorageReference getFotosOrcamentosStorageReference(String orcamentoKey) {
        return getBaseStorageReference().child(String.format("Orcamentos/%s/", orcamentoKey));
    }

    public static DatabaseReference getUsuariosPerfisReference(String uid) {
        DatabaseReference usuariosPerfisReference = getBaseReference().child(String.format("UsuariosPerfis/%s/", uid));
        //usuariosPerfisReference.keepSynced(true);
        return usuariosPerfisReference;
    }


    public static DatabaseReference getUsuariosPerfisReference() {
        DatabaseReference usuariosPerfisReference = getBaseReference().child(String.format("UsuariosPerfis/%s/", getUid()));
        //usuariosPerfisReference.keepSynced(true);
        return usuariosPerfisReference;
    }

    public static DatabaseReference getOrcamentosReference() {
        return getBaseReference().child(String.format("Orcamentos/%s/", getUid()));
    }

    public static DatabaseReference getOrcamentosAprovadosReference() {
        return getBaseReference().child(String.format("OrcamentosAprovados/%s/", getUid()));
    }

    public static DatabaseReference getOrcamentosReference(String orcamentoKey) {
        return getBaseReference().child(String.format("Orcamentos/%s/%s", getUid(), orcamentoKey));
    }

    public static DatabaseReference getOrcamentosFotosListReference(String orcamentoKey) {
        return getBaseReference().child(String.format("Orcamentos/%s/%s/fotosList/", getUid(), orcamentoKey));
    }

    public static DatabaseReference getHabilidesReference() {
        return getBaseReference().child("Habilidades/");
    }

    public static DatabaseReference getHabilideXOrcamentosReference() {
        return getBaseReference().child("HabilidadesXOrcamentos/");
    }

    public static DatabaseReference getOrcamentosPropostaReference() {
        return getBaseReference().child("OrcamentosProposta/");
    }

    public static DatabaseReference getOrcamentosAvaliacaoReference() {
        return getBaseReference().child("OrcamentosAvaliacoes/");
    }

    public static void sendEmailVerification(Activity activity) {
        getFirebaseUser().sendEmailVerification().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public static void loadUsuarioPerfil(final IFirebaseResult firebaseResult) {
        firebaseResult.openProgressDialog();

        FirebaseUtil.getUsuariosPerfisReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firebaseResult.onDataReceived(dataSnapshot.getValue(UsuarioPerfil.class));
                firebaseResult.closeProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                firebaseResult.onError(databaseError);
                firebaseResult.closeProgressDialog();
            }
        });
    }
}
