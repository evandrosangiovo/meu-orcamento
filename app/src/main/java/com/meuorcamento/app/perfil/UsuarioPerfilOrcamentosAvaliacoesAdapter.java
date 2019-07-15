package com.meuorcamento.app.perfil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meuorcamento.R;
import com.meuorcamento.model.OrcamentoAvaliacao;
import com.meuorcamento.utils.formatters.Formatter;

import java.util.List;
import java.util.Map;

public class UsuarioPerfilOrcamentosAvaliacoesAdapter extends BaseAdapter {

    private List<OrcamentoAvaliacao> orcamentoAvaliacaoList;
    private LayoutInflater inflater;

    public UsuarioPerfilOrcamentosAvaliacoesAdapter(Context context, List<OrcamentoAvaliacao> orcamentoAvaliacaoList) {
        this.orcamentoAvaliacaoList = orcamentoAvaliacaoList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if (orcamentoAvaliacaoList != null) {
            return orcamentoAvaliacaoList.size();
        }
        return 0;
    }

    public void addList(List<OrcamentoAvaliacao> orcamentoAvaliacaoList) {
        this.orcamentoAvaliacaoList = orcamentoAvaliacaoList;
    }


    @Override
    public Object getItem(int position) {
        return orcamentoAvaliacaoList.get(position);
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
            view = inflater.inflate(R.layout.item_adapter_usuarios_perfil_orcamentos_avaliacoes, null);
            view.setTag(holder);
            holder.tvNomeUsuario = view.findViewById(R.id.tvNomeUsuario);
            holder.tvObservacoes = view.findViewById(R.id.tvObservacoes);
            holder.tvAvaliacaoBom = view.findViewById(R.id.tvAvaliacaoBom);
            holder.tvAvaliacaoRegular = view.findViewById(R.id.tvAvaliacaoRegular);
            holder.tvAvaliacaoRuim = view.findViewById(R.id.tvAvaliacaoRuim);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        OrcamentoAvaliacao orcamentoAvaliacao = orcamentoAvaliacaoList.get(position);

        holder.tvAvaliacaoBom.setVisibility(View.GONE);
        holder.tvAvaliacaoRegular.setVisibility(View.GONE);
        holder.tvAvaliacaoRuim.setVisibility(View.GONE);

        if(orcamentoAvaliacao.getUsuarioPerfil() != null)
            holder.tvNomeUsuario.setText(String.format("%s", orcamentoAvaliacao.getUsuarioPerfil().getNome()));
        holder.tvObservacoes.setText(orcamentoAvaliacao.getObservacoes());

        holder.tvAvaliacaoBom.setText(Formatter.getInstance(orcamentoAvaliacao.getNota(), Formatter.FormatTypeEnum.DECIMAL_UM).format());
        holder.tvAvaliacaoRegular.setText(Formatter.getInstance(orcamentoAvaliacao.getNota(), Formatter.FormatTypeEnum.DECIMAL_UM).format());
        holder.tvAvaliacaoRuim.setText(Formatter.getInstance(orcamentoAvaliacao.getNota(), Formatter.FormatTypeEnum.DECIMAL_UM).format());

        if (orcamentoAvaliacao.getNota() < 3) {
            holder.tvAvaliacaoRuim.setVisibility(View.VISIBLE);
        } else if (orcamentoAvaliacao.getNota() == 3) {
            holder.tvAvaliacaoRegular.setVisibility(View.VISIBLE);
        } else
            holder.tvAvaliacaoBom.setVisibility(View.VISIBLE);

        return view;
    }

    private class ViewHolder {
        TextView tvNomeUsuario;
        TextView tvObservacoes;
        TextView tvAvaliacaoBom;
        TextView tvAvaliacaoRegular;
        TextView tvAvaliacaoRuim;
    }
}
