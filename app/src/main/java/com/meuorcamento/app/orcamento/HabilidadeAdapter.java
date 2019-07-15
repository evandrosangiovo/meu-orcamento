package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.meuorcamento.R;
import com.meuorcamento.model.Habilidade;

import java.util.List;

public class HabilidadeAdapter extends BaseAdapter {

    private List<Habilidade> habilidadeList;
    private LayoutInflater inflater;

    public HabilidadeAdapter(Context context, List<Habilidade> habilidadeList) {
        this.habilidadeList = habilidadeList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if (habilidadeList != null) {
            return habilidadeList.size();
        }
        return 0;
    }

    public void addList(List<Habilidade> habilidadeList) {
        this.habilidadeList = habilidadeList;
    }

    @Override
    public Object getItem(int position) {
        return habilidadeList.get(position);
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
            view = inflater.inflate(R.layout.item_adapter_habilidades, null);
            view.setTag(holder);

            holder.tvHabilidadeTitulo = view.findViewById(R.id.tvHabilidadeTitulo);
            holder.tvDescricao = view.findViewById(R.id.tvDescricao);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        Habilidade habilidade = habilidadeList.get(position);

        holder.tvHabilidadeTitulo.setText(habilidade.getTitulo());
        holder.tvDescricao.setText(habilidade.getDescricao());


        return view;
    }

    private class ViewHolder {

        TextView tvHabilidadeTitulo;
        TextView tvDescricao;

    }
}
