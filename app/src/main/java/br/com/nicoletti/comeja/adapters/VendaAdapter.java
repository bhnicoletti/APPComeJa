package br.com.nicoletti.comeja.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;
import br.com.nicoletti.comeja.model.Endereco;
import br.com.nicoletti.comeja.model.ItemVenda;


public class VendaAdapter extends RecyclerView.Adapter<VendaAdapter.MyViewHolder> {
    private Context mContext;
    private List<ItemVenda> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width, height, roundPixels;


    public VendaAdapter(Context c, List<ItemVenda> l) {
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;

        roundPixels = (int)(2 * scale + 0.5f);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_venda, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        Locale ptBr = new Locale("pt", "BR");
        String valorString = NumberFormat.getCurrencyInstance(ptBr).format(mList.get(position).getVlrItemVenda());
        myViewHolder.nome.setText(mList.get(position).getProdutoItemVenda().getNomeProduto());
        myViewHolder.valor.setText("Valor: "+valorString);
        myViewHolder.quantidade.setText("Quantidade: "+mList.get(position).getQuantItemVenda());


        try {
            YoYo.with(Techniques.Bounce)
                    .duration(700)
                    .playOn(myViewHolder.itemView);
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(ItemVenda c, int position) {
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       public TextView nome, quantidade, valor;

        public MyViewHolder(View itemView) {
            super(itemView);


            nome = (TextView) itemView.findViewById(R.id.tv_nomeProduto);
            valor = (TextView) itemView.findViewById(R.id.tv_total);
            quantidade = (TextView) itemView.findViewById(R.id.tv_quantidade);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
}
