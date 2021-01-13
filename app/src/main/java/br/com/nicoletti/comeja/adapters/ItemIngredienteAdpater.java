package br.com.nicoletti.comeja.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.devspark.robototextview.widget.RobotoTextView;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;


public class ItemIngredienteAdpater extends RecyclerView.Adapter<ItemIngredienteAdpater.MyViewHolder> {
    private Context mContext;
    private ArrayList<HashMap<String, String>> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width, height, roundPixels;


    public ItemIngredienteAdpater(Context c, ArrayList<HashMap<String, String>> l) {
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
        View v = mLayoutInflater.inflate(R.layout.item_ingredientelista, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {



        myViewHolder.tv_nomeIngrediente.setText(mList.get(position).get("ingrediente"));
        if(mList.get(position).get("tipo").equals("produto")){
            myViewHolder.iv_icone.setImageResource(R.drawable.check);
        }
        else if(mList.get(position).get("tipo").equals("retirado")){
            myViewHolder.iv_icone.setImageResource(R.drawable.not);
        }
        else if(mList.get(position).get("tipo").equals("adicional")){
            myViewHolder.iv_icone.setImageResource(R.drawable.check);
            myViewHolder.linear.setBackgroundResource(R.color.md_yellow_400);
        }

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


    public void addListItem(HashMap<String, String> c, int position) {
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RobotoTextView tv_nomeIngrediente;
        public ImageView iv_icone;
        public LinearLayout linear;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_nomeIngrediente = (RobotoTextView) itemView.findViewById(R.id.tv_nomeIngrediente);
            iv_icone = (ImageView) itemView.findViewById(R.id.iv_icone);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);

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
