package br.com.nicoletti.comeja.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.model.Ingrediente;


public class IngredienteAdapter extends ArrayAdapter<Ingrediente> {

    private List<Ingrediente> ingredienteList;
    private Context context;
    List<Ingrediente> ingredientesSelecionados;

    public IngredienteAdapter(List<Ingrediente> ingredienteList, Context context) {
        super(context, R.layout.item_ingrediente, ingredienteList);
        this.ingredienteList = ingredienteList;
        this.context = context;
        this.ingredientesSelecionados = ingredienteList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_ingrediente, parent, false);



        TextView idIngrediente = (TextView) v.findViewById(R.id.tv_id);
        TextView nomeIngrediente = (TextView) v.findViewById(R.id.tv_nome);
        CheckBox chkBox = (CheckBox) v.findViewById(R.id.cb_prod);


        Ingrediente p = ingredienteList.get(position);
        nomeIngrediente.setText(p.getNomeIngrediente());
        chkBox.setChecked(true);
        idIngrediente.setText(p.getIdIngrediente().toString());
        chkBox.setTag(p);

        chkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox check = (CheckBox) v;

                Ingrediente i = (Ingrediente) v.getTag();
                if (check.isChecked()) {
                    if(!ingredientesSelecionados.contains(i)){
                        ingredientesSelecionados.add(i);
                    }
                } else {

                    if (ingredientesSelecionados.contains(i)) {
                        //Remove da lista se existir na lista
                        ingredientesSelecionados.remove(i);
                    }
                }
            }
        });

        return v;
    }

    public List<Ingrediente> getIngredientesSelecionados() {
        return ingredientesSelecionados;
    }

    public void setIngredientesSelecionados(List<Ingrediente> ingredientesSelecionados) {
        this.ingredientesSelecionados = ingredientesSelecionados;
    }
}

