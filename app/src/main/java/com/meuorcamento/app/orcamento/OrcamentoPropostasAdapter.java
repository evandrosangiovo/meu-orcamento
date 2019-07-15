package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meuorcamento.R;
import com.meuorcamento.SystemConfiguration;
import com.meuorcamento.model.OrcamentoProposta;
import com.meuorcamento.utils.IOHelper;
import com.meuorcamento.utils.formatters.Formatter;

import java.util.List;

public class OrcamentoPropostasAdapter extends BaseAdapter {

    private List<OrcamentoProposta> orcamentoPropostaList;
    private LayoutInflater inflater;

    public OrcamentoPropostasAdapter(Context context, List<OrcamentoProposta> orcamentoPropostaList) {
        this.orcamentoPropostaList = orcamentoPropostaList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if (orcamentoPropostaList != null) {
            return orcamentoPropostaList.size();
        }
        return 0;
    }

    public void addList(List<OrcamentoProposta> orcamentoPropostaList) {
        this.orcamentoPropostaList = orcamentoPropostaList;
    }


    @Override
    public Object getItem(int position) {
        return orcamentoPropostaList.get(position);
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
            view = inflater.inflate(R.layout.item_adapter_orcamentos_propostas, null);
            view.setTag(holder);
            holder.tvNomePrestador = view.findViewById(R.id.tvNomePrestador);
            holder.tvObservacoes  = view.findViewById(R.id.tvObservacoes);
            holder.tvAvaliacaoBom = view.findViewById(R.id.tvAvaliacaoBom);
            holder.tvAvaliacaoRegular = view.findViewById(R.id.tvAvaliacaoRegular);
            holder.tvAvaliacaoRuim = view.findViewById(R.id.tvAvaliacaoRuim);
            holder.tvValor = view.findViewById(R.id.tvValor);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        OrcamentoProposta orcamentoProposta = orcamentoPropostaList.get(position);

        holder.tvNomePrestador.setText(String.format("%s %s", orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getNome(), orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getSobreNome()));
        holder.tvObservacoes.setText(orcamentoProposta.getOrcamentoPropostaInformacao().getObservacoes());
        holder.tvValor.setText(Formatter.getInstance(orcamentoProposta.getOrcamentoPropostaInformacao().getValorProposta(), Formatter.FormatTypeEnum.DECIMAL_DOIS_MOEDA).format());

        holder.tvAvaliacaoBom.setVisibility(View.GONE);
        holder.tvAvaliacaoRegular.setVisibility(View.GONE);
        holder.tvAvaliacaoRuim.setVisibility(View.GONE);

        holder.tvAvaliacaoBom.setText(Formatter.getInstance(orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getAvaliacaoGeral(), Formatter.FormatTypeEnum.DECIMAL_DOIS).format());
        holder.tvAvaliacaoRegular.setText(Formatter.getInstance(orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getAvaliacaoGeral(), Formatter.FormatTypeEnum.DECIMAL_DOIS).format());
        holder.tvAvaliacaoRuim.setText(Formatter.getInstance(orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getAvaliacaoGeral(), Formatter.FormatTypeEnum.DECIMAL_DOIS).format());

        if (orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getAvaliacaoGeral() < 3) {
            holder.tvAvaliacaoRuim.setVisibility(View.VISIBLE);
        } else if (orcamentoProposta.getOrcamentoPropostaInformacao().getUsuarioPerfil().getAvaliacaoGeral() == 3) {
            holder.tvAvaliacaoRegular.setVisibility(View.VISIBLE);
        } else
            holder.tvAvaliacaoBom.setVisibility(View.VISIBLE);
        return view;
    }

    private class ViewHolder {
        TextView tvNomePrestador;
        TextView tvObservacoes;
        TextView tvAvaliacaoBom;
        TextView tvAvaliacaoRegular;
        TextView tvAvaliacaoRuim;
        TextView tvValor;

    }
}
