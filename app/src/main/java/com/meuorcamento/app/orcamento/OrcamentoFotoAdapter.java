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
import com.meuorcamento.utils.IOHelper;

import java.util.List;

public class OrcamentoFotoAdapter extends BaseAdapter {

    private List<String> fotosList;
    private LayoutInflater inflater;

    public OrcamentoFotoAdapter(Context context, List<String> fotosList) {
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

    public void addList(List<String> fotosList) {
        this.fotosList = fotosList;
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
            view = inflater.inflate(R.layout.item_adapter_orcamentos_fotos, null);
            view.setTag(holder);
            holder.ivFoto = view.findViewById(R.id.ivFoto);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        String urlFoto = fotosList.get(position);

        holder.ivFoto.setImageBitmap(IOHelper.decodeBitmapFromUrl(String.format("%s/%s", SystemConfiguration.FOLDER_IMAGENS, urlFoto), 300, 600));


        System.gc();
        return view;
    }

    private class ViewHolder {
        ImageView ivFoto;

    }
}
