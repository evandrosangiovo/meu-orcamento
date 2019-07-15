package com.meuorcamento.app.meusorcamentos;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseFragment;
import com.meuorcamento.app.configuracoes.ConfiguracoesActivity;
import com.meuorcamento.app.orcamento.OrcamentoActivity;
import com.meuorcamento.app.orcamento.OrcamentoAdapter;
import com.meuorcamento.app.orcamento.OrcamentoVisualizacaoActivity;
import com.meuorcamento.model.Orcamento;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeusOrcamentosFragment extends BaseFragment {


    private View btnNovoOrcamento;
    private static final String TAG = "DashboardFragment";

    private ListView listView;
    private OrcamentoAdapter orcamentoAdapter;
    private ChildEventListener childEventListener;
    private DatabaseReference databaseReference;

    public MeusOrcamentosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meusorcamentos, container, false);

        btnNovoOrcamento = view.findViewById(R.id.btnNovoOrcamento);

        btnNovoOrcamento.setFocusable(false);
        btnNovoOrcamento.setClickable(true);
        btnNovoOrcamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrcamentoActivity.startActivity(getActivity());
            }
        });

        listView = view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Orcamento orcamento = (Orcamento) orcamentoAdapter.getItem(position);

                OrcamentoVisualizacaoActivity.startActivity(getActivity(), orcamento);
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
                Log.i(TAG, dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(childEventListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        databaseReference = FirebaseUtil.getOrcamentosReference();
        databaseReference.addChildEventListener(childEventListener);
    }

    private void updateListView(List<Orcamento> orcamentosList) {
        if(orcamentoAdapter == null) {
            orcamentoAdapter = new OrcamentoAdapter(getActivity(), orcamentosList);
            listView.setAdapter(orcamentoAdapter);
            return;
        }

        orcamentoAdapter.addList(orcamentosList);
        orcamentoAdapter.notifyDataSetChanged();
        listView.refreshDrawableState();

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
