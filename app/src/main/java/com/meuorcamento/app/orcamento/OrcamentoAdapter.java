package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meuorcamento.R;
import com.meuorcamento.model.Orcamento;
import com.meuorcamento.utils.formatters.Formatter;

import java.util.List;

public class OrcamentoAdapter extends BaseAdapter {

    private List<Orcamento> orcamentoList;
    private LayoutInflater inflater;

    public OrcamentoAdapter(Context context, List<Orcamento> orcamentoList) {
        this.orcamentoList = orcamentoList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if (orcamentoList != null) {
            return orcamentoList.size();
        }
        return 0;
    }

    public void addList(List<Orcamento> orcamentoList) {
        this.orcamentoList = orcamentoList;
    }

    @Override
    public Object getItem(int position) {
        return orcamentoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;


        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_adapter_orcamento, null);
            view.setTag(holder);

            holder.tvHabilidade = view.findViewById(R.id.tvHabilidade);
            holder.tvTitulo = view.findViewById(R.id.tvTitulo);
            holder.tvDetalhes= view.findViewById(R.id.tvDetalhes);
            holder.tvDataLancamento = view.findViewById(R.id.tvDataLancamento);

            holder.tvQuantidadePropostas = view.findViewById(R.id.tvQuantidadePropostas);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        Orcamento orcamento = orcamentoList.get(position);

        holder.tvDataLancamento.setText(Formatter.getInstance(orcamento.getDataLancamento(), Formatter.FormatTypeEnum.DATE_EXTENSIVE_NO_TIME).format());

        holder.tvHabilidade.setText(orcamento.getHabilidade().getTitulo());
        holder.tvTitulo.setText(orcamento.getTitulo());
        holder.tvDetalhes.setText(orcamento.getDetalhes());
        holder.tvQuantidadePropostas.setText(Formatter.getInstance(orcamento.getQuantidadePropostas(), Formatter.FormatTypeEnum.INTEIRO).format());


        return view;
    }

    private class ViewHolder {

        TextView tvHabilidade;
        TextView tvTitulo;
        TextView tvDetalhes;
        TextView tvDataLancamento;
        TextView tvQuantidadePropostas;

    }
}
