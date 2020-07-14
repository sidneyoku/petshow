package br.com.sidney.petshow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import br.com.sidney.petshow.R;
import br.com.sidney.petshow.entity.Pet;


/**
 * @author Sidney Toshidi Oku
 */
public class CadastroPetAdapter extends ArrayAdapter<Pet> {

    private static final int VIEW_LAYOUT = R.layout.cadastropet_adapter;
    private LayoutInflater li;

    public CadastroPetAdapter(@NonNull Context ctx) {
        super(ctx, VIEW_LAYOUT);
        li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = li.inflate(VIEW_LAYOUT, null);
        }

        ImageView img1 = (ImageView) view.findViewById(R.id.img1_cadastropet_adapter);
        TextView txtNome = (TextView) view.findViewById(R.id.txt_nome_cadastropet_adapter);
        TextView txtTipo = (TextView) view.findViewById(R.id.txt_tipo_cadastropet_adapter);
        TextView txtPorte= (TextView) view.findViewById(R.id.txt_porte_cadastropet_adapter);

        Pet pet = getItem(position);
//        img1.setBackground(pet.getImagem1().);
        txtNome.setText(pet.getNome());
        txtTipo.setText(pet.getTipo());
        txtPorte.setText(pet.getPorte());

        return view;
    }
}
