package com.meuorcamento.app.dialog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.meuorcamento.R;


public class DialogFragmentConfirmarTelefone extends DialogFragmentAbstract {

    private View view;
    private EditText etCodigoConfirmacao;

    public DialogFragmentConfirmarTelefone() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public String getSMSCode() {
        return etCodigoConfirmacao.getText().toString();
    }

    @Override
    public String getViewTag() {
        return "dialog_confirmar_telefone";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(view != null) {
            attachActionBar(view);
            return view;
        }

        view = inflater.inflate(R.layout.dialog_confirmar_telefone, container, false);
        etCodigoConfirmacao = view.findViewById(R.id.etCodigoConfirmacao);

        attachActionBar(view);
        return view;
    }

    @Override
    public void resetView() {
    }

}
