package com.meuorcamento.app.perfil;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.R;
import com.meuorcamento.app.BaseFragment;
import com.meuorcamento.model.UsuarioPerfil;
import com.meuorcamento.utils.formatters.Formatter;



public class UsuarioPerfilVisualizacaoFragment extends BaseFragment {

    private TextView tvEmail;
    private TextView tvNome;
    private TextView tvCPF;
    private TextView tvIdentidade;
    private TextView tvCep;
    private TextView tvCidadeUF;
    private TextView tvBairro;
    private TextView tvEndereco;
    private TextView tvDataNascimento;
    private Button btnAlterarPerfil;
    private TextView tvEmailVerificado;
    private TextView tvEmailNaoVerificado;

    private Button btnVerificarEmail;

    public UsuarioPerfilVisualizacaoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuario_perfil_visualizacao, container, false);
        tvEmailVerificado = view.findViewById(R.id.tvEmailVerificado);
        tvEmailNaoVerificado = view.findViewById(R.id.tvEmailNaoVerificado);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvNome = view.findViewById(R.id.tvNome);
        tvCPF = view.findViewById(R.id.tvCPF);
        tvIdentidade = view.findViewById(R.id.tvIdentidade);
        tvCep = view.findViewById(R.id.tvCep);
        tvCidadeUF = view.findViewById(R.id.tvCidadeUF);
        tvBairro = view.findViewById(R.id.tvBairro);
        tvEndereco = view.findViewById(R.id.tvEndereco);
        tvDataNascimento = view.findViewById(R.id.tvDataNascimento);
        btnVerificarEmail= view.findViewById(R.id.btnVerificarEmail);
        btnVerificarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUtil.sendEmailVerification(getActivity());
                Toast.makeText(getActivity(), "Acesse seu email e confirme sua conta", Toast.LENGTH_SHORT).show();
            }
        });

        btnAlterarPerfil = view.findViewById(R.id.btnAlterarPerfil);
        btnAlterarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioPerfilManutencaoActivity.startActivity(getActivity());
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadPerfil();
    }

    private void loadPerfil() {
        FirebaseUtil.loadUsuarioPerfil(this);
    }

    private void updateView(UsuarioPerfil usuarioPerfil) {

        FirebaseUser firebaseUser = FirebaseUtil.getFirebaseUser();

        if(firebaseUser != null) {
            tvEmail.setText(firebaseUser.getEmail());
            if(firebaseUser.isEmailVerified()) {
                tvEmailVerificado.setVisibility(View.VISIBLE);
                tvEmailNaoVerificado.setVisibility(View.GONE);
                btnVerificarEmail.setVisibility(View.GONE);
            } else {
                tvEmailNaoVerificado.setVisibility(View.VISIBLE);
                tvEmailVerificado.setVisibility(View.GONE);
                btnVerificarEmail.setVisibility(View.VISIBLE);
            }
        }

        if(usuarioPerfil == null)
            return;

        tvNome.setText(Formatter.getInstance(String.format("%s %s", usuarioPerfil.getNome(), usuarioPerfil.getSobreNome()), Formatter.FormatTypeEnum.TITLE_CASE).format());
        tvCPF.setText(Formatter.getInstance(usuarioPerfil.getCpf(), Formatter.FormatTypeEnum.CPF).format());
        tvIdentidade.setText(usuarioPerfil.getIdentidade());
        tvCep.setText(Formatter.getInstance(usuarioPerfil.getCep(), Formatter.FormatTypeEnum.CEP).format());
        tvCidadeUF.setText(String.format("%s, %s", usuarioPerfil.getCidade(), usuarioPerfil.getUf()));
        tvBairro.setText(usuarioPerfil.getBairro());
        tvEndereco.setText(String.format("%s, %s", usuarioPerfil.getEndereco(), usuarioPerfil.getNumeroEstabelecimento()));
        tvDataNascimento.setText(Formatter.getInstance(usuarioPerfil.getDataNascimento(), Formatter.FormatTypeEnum.DATE).format());

    }

    @Override
    public void reloadView() {
        loadPerfil();
    }

    @Override
    public void onDataReceived(Object result) {
        UsuarioPerfil usuarioPerfil = (UsuarioPerfil)result;
        updateView(usuarioPerfil);
    }

    @Override
    public void onError(DatabaseError databaseError) {
        Toast.makeText(getActivity(), "Erro ao carregar o perfil...", Toast.LENGTH_SHORT).show();
    }
}
