package com.meuorcamento.app.orcamento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meuorcamento.R;
import com.meuorcamento.SystemConfiguration;
import com.meuorcamento.utils.AndroidHelper;
import com.meuorcamento.utils.IOHelper;

import java.util.List;

public class OrcamentoEnviarFotoAdapter extends BaseAdapter {

    private List<String> fotosList;
    private LayoutInflater inflater;

    public OrcamentoEnviarFotoAdapter(Context context, List<String> fotosList) {
        this.fotosList = fotosList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        if (fotosList != null) {
            return fotosList.size();
        }
        return 0;
    }

    public void addList(List<String> habilidadeList) {
        this.fotosList = habilidadeList;
    }


    @Override
    public Object getItem(int position) {
        return fotosList.get(position);
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
            view = inflater.inflate(R.layout.item_adapter_orcamentos_fotos_enviar, null);
            view.setTag(holder);

            holder.tvNomeArquivo = view.findViewById(R.id.tvNomeArquivo);
            holder.ivFoto = view.findViewById(R.id.ivFoto);
            holder.pb = view.findViewById(R.id.pb);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        String urlFoto = fotosList.get(position);

        holder.tvNomeArquivo.setText(urlFoto);
        holder.ivFoto.setImageBitmap(IOHelper.decodeBitmapFromUrl(String.format("%s/%s", SystemConfiguration.FOLDER_IMAGENS, urlFoto), 300, 600));
        holder.pb.animate();


        System.gc();
        return view;
    }

    private class ViewHolder {

        TextView tvNomeArquivo;
        ImageView ivFoto;
        ProgressBar pb;

    }
}
