package br.com.nicoletti.comeja.adapters;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;
import br.com.nicoletti.comeja.model.ItemVenda;


/**
 * Created by viniciusthiengo on 4/5/15.
 */
public class CarrinhoAdapter extends RecyclerView.Adapter<CarrinhoAdapter.MyViewHolder> {
    private Context mContext;
    private List<ItemVenda> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width;
    private int height;


    public CarrinhoAdapter(Context c, List<ItemVenda> l){
        mContext = c;
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        scale = mContext.getResources().getDisplayMetrics().density;
        width = mContext.getResources().getDisplayMetrics().widthPixels - (int)(14 * scale + 0.5f);
        height = (width / 16) * 9;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_carrinho, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        Locale ptBr = new Locale("pt", "BR");
        String valorString = NumberFormat.getCurrencyInstance(ptBr).format(mList.get(position).getVlrItemVenda());
        myViewHolder.idprodItemVenda.setText(mList.get(position).getProdutoItemVenda().getIdProduto().toString());
        myViewHolder.prodItemVenda.setText(mList.get(position).getProdutoItemVenda().getNomeProduto().toString());
        myViewHolder.quantItemVenda.setText("Quantidade: "+mList.get(position).getQuantItemVenda().toString());
        myViewHolder.valorItemVenda.setText("Total: "+valorString);
        ControllerListener listener = new BaseControllerListener() {
            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                Log.i("LOG", "onFinalImageSet");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                Log.i("LOG", "onFailure");
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
                Log.i("LOG", "onIntermediateImageFailed");
            }

            @Override
            public void onIntermediateImageSet(String id, Object imageInfo) {
                super.onIntermediateImageSet(id, imageInfo);
                Log.i("LOG", "onIntermediateImageSet");
            }

            @Override
            public void onRelease(String id) {
                super.onRelease(id);
                Log.i("LOG", "onRelease");
            }

            @Override
            public void onSubmit(String id, Object callerContext) {
                super.onSubmit(id, callerContext);
                Log.i("LOG", "onSubmit");
            }
        };

        Uri uri = Uri.parse("http://www.comeja.com.br/imagens/" + mList.get(position).getProdutoItemVenda().getImagemProduto());


        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setControllerListener(listener)
                .setOldController(myViewHolder.imgProduto.getController())
                .build();
        myViewHolder.imgProduto.setController(dc);

        try{
            YoYo.with(Techniques.Bounce)
                    .duration(700)
                    .playOn(myViewHolder.itemView);
        }
        catch(Exception e){}
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }


    public void addListItem(ItemVenda c, int position){
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }




    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView idprodItemVenda, prodItemVenda, quantItemVenda, valorItemVenda;
        public SimpleDraweeView imgProduto;

        public MyViewHolder(View itemView) {
            super(itemView);

            idprodItemVenda = (TextView) itemView.findViewById(R.id.tv_idproduto);
            prodItemVenda = (TextView) itemView.findViewById(R.id.tv_nomeProduto);
            quantItemVenda = (TextView) itemView.findViewById(R.id.tv_quantidade);
            valorItemVenda = (TextView) itemView.findViewById(R.id.tv_valor);
            imgProduto = (SimpleDraweeView) itemView.findViewById(R.id.imgProduto);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
}
