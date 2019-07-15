package com.meuorcamento.app.meusorcamentos;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseFragment;
import com.meuorcamento.app.orcamento.OrcamentoAdapter;
import com.meuorcamento.app.orcamento.OrcamentoAprovadoPrestadorAdapter;
import com.meuorcamento.app.orcamento.OrcamentoPrestadorAdapter;
import com.meuorcamento.app.orcamento.OrcamentoVisualizacaoActivity;
import com.meuorcamento.app.orcamento.OrcamentoVisualizacaoPrestadorActivity;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.model.UsuarioPerfil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeusOrcamentosAprovadosPrestadorFragment extends BaseFragment {

    private static final String TAG = "MeusOrcamentosAprovadosPrestador";

    private ListView listView;
    private OrcamentoAprovadoPrestadorAdapter orcamentoAprovadoPrestadorAdapter;
    private ChildEventListener childEventListener;

    private DatabaseReference databaseReference;

    public MeusOrcamentosAprovadosPrestadorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meusorcamentosaprovados_prestador, container, false);

        listView = view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Orcamento orcamento = (Orcamento) orcamentoAprovadoPrestadorAdapter.getItem(position);

                OrcamentoVisualizacaoPrestadorActivity.startActivity(getActivity(), orcamento);
            }
        });


        childEventListener = new ChildEventListener() {
            List<Orcamento> orcamentoList = new ArrayList<>();

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Orcamento orcamentoItem = dataSnapshot.getValue(Orcamento.class);
                if (orcamentoList.contains(orcamentoItem)) {
                    return;
                }

                orcamentoList.add(orcamentoItem);
                updateListView(orcamentoList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Orcamento item = dataSnapshot.getValue(Orcamento.class);

                if (orcamentoList.contains(item)) {
                    int position = orcamentoList.indexOf(item);
                    orcamentoList.remove(item);
                    orcamentoList.add(position, item);
                    updateListView(orcamentoList);
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Orcamento item = dataSnapshot.getValue(Orcamento.class);

                if (orcamentoList.contains(item)) {
                    int position = orcamentoList.indexOf(item);
                    orcamentoList.remove(position);
                    updateListView(orcamentoList);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        databaseReference = FirebaseUtil.getOrcamentosAprovadosReference();

        return view;
    }

    private void updateListView(List<Orcamento> orcamentosList) {
        if(orcamentoAprovadoPrestadorAdapter == null) {
            orcamentoAprovadoPrestadorAdapter = new OrcamentoAprovadoPrestadorAdapter(getActivity(), orcamentosList);
            listView.setAdapter(orcamentoAprovadoPrestadorAdapter);
            return;
        }

        orcamentoAprovadoPrestadorAdapter.addList(orcamentosList);
        orcamentoAprovadoPrestadorAdapter.notifyDataSetChanged();
        listView.refreshDrawableState();

    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(childEventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        databaseReference.addChildEventListener(childEventListener);
    }


    @Override
    public void reloadView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(childEventListener);
    }
}
