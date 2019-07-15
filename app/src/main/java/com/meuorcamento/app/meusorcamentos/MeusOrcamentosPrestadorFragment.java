package com.meuorcamento.app.meusorcamentos;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseFragment;
import com.meuorcamento.app.orcamento.OrcamentoAdapter;
import com.meuorcamento.app.orcamento.OrcamentoPrestadorAdapter;
import com.meuorcamento.app.orcamento.OrcamentoVisualizacaoPrestadorActivity;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.model.UsuarioPerfil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeusOrcamentosPrestadorFragment extends BaseFragment {

    private static final String TAG = "MeusOrcamentosPrestador";

    private ListView listView;
    private OrcamentoPrestadorAdapter orcamentoPrestadorAdapter;
    private ChildEventListener childEventListener;
    private List<Query> queryList = new ArrayList<>();
    private UsuarioPerfil usuarioPerfil;

    public MeusOrcamentosPrestadorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meusorcamentos_prestador, container, false);

        listView = view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Orcamento orcamento = (Orcamento) orcamentoPrestadorAdapter.getItem(position);

                OrcamentoVisualizacaoPrestadorActivity.startActivity(getActivity(), orcamento);
            }
        });


        childEventListener = new ChildEventListener() {
            List<Orcamento> orcamentoList = new ArrayList<>();

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot item: dataSnapshot.getChildren()) {

                    Orcamento orcamentoItem = item.getValue(Orcamento.class);
                    if (orcamentoList.contains(orcamentoItem)) {
                        continue;
                    }

                    orcamentoList.add(orcamentoItem);
                }
                updateListView(orcamentoList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Orcamento orcamentoItem = item.getValue(Orcamento.class);

                    if (orcamentoList.contains(orcamentoItem)) {
                        int position = orcamentoList.indexOf(orcamentoItem);
                        orcamentoList.remove(orcamentoItem);
                        orcamentoList.add(position, orcamentoItem);
                        updateListView(orcamentoList);
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (DataSnapshot item : dataSnapshot.getChildren()) {
                    Orcamento orcamentoItem = item.getValue(Orcamento.class);

                    if (orcamentoList.contains(orcamentoItem)) {
                        int position = orcamentoList.indexOf(orcamentoItem);
                        orcamentoList.remove(position);
                        updateListView(orcamentoList);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //startListeners();

        return view;
    }

    private void startListeners() {
        FirebaseUtil.getUsuariosPerfisReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioPerfil = dataSnapshot.getValue(UsuarioPerfil.class);

                for(String item : usuarioPerfil.getHabilidadesList()) {
                    Query query = FirebaseUtil.getHabilideXOrcamentosReference().equalTo(item).orderByKey();
                    queryList.add(query);
                    query.addChildEventListener(childEventListener);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Falha para carregar o perfil do usuário", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
            }
        });
    }

    private void updateListView(List<Orcamento> orcamentosList) {
        if(orcamentoPrestadorAdapter == null) {
            orcamentoPrestadorAdapter = new OrcamentoPrestadorAdapter(getActivity(), orcamentosList);
            listView.setAdapter(orcamentoPrestadorAdapter);
            return;
        }

        orcamentoPrestadorAdapter.addList(orcamentosList);
        orcamentoPrestadorAdapter.notifyDataSetChanged();
        listView.refreshDrawableState();

    }

    @Override
    public void reloadView() {

    }

    @Override
    public void onPause() {
        super.onPause();

        for(Query item : queryList) {
            item.removeEventListener(childEventListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(Query item : queryList) {
            item.removeEventListener(childEventListener);
        }

    }
}
