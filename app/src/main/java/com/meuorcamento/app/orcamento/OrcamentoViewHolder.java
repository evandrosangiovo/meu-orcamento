package com.meuorcamento.app.orcamento;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.meuorcamento.R;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.utils.formatters.Formatter;

public class OrcamentoViewHolder extends RecyclerView.ViewHolder {

    public TextView tvHabilidadeCategorias;
    public TextView tvTitulo;
    public TextView tvDataLancamento;
    public TextView tvQuantidadePropostas;

    public OrcamentoViewHolder(View view) {
        super(view);

        tvHabilidadeCategorias = view.findViewById(R.id.tvHabilidade);
        tvTitulo = view.findViewById(R.id.tvTitulo);
        tvDataLancamento = view.findViewById(R.id.tvDataLancamento);
        tvQuantidadePropostas = view.findViewById(R.id.tvQuantidadePropostas);
    }

    public void bind(Orcamento orcamento, View.OnClickListener onClickListener) {

        tvDataLancamento.setText(Formatter.getInstance(orcamento.getDataLancamento(), Formatter.FormatTypeEnum.DATE_EXTENSIVE_NO_TIME).format());
        tvHabilidadeCategorias.setText(orcamento.getTitulo());
        tvTitulo.setText(orcamento.getTitulo());
        tvQuantidadePropostas.setText(Formatter.getInstance(0, Formatter.FormatTypeEnum.INTEIRO).format());

        itemView.setOnClickListener(onClickListener);
    }
}
