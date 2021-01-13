package br.com.nicoletti.comeja.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;


public class EmpresaAdapter extends RecyclerView.Adapter<EmpresaAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<HashMap<String, String>> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private float scale;
    private int width, height, roundPixels;


    public EmpresaAdapter(Context c, ArrayList<HashMap<String, String>> l){
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
        View v = mLayoutInflater.inflate(R.layout.item_empresa_card, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        myViewHolder.tv_idEmpresa.setText(mList.get(position).get("idEmpresa"));
        myViewHolder.tv_nomeEmpresa.setText(mList.get(position).get("nomeEmpresa"));

        ControllerListener listener = new BaseControllerListener(){
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

        int w = 0;
        if (myViewHolder.imgEmpresa.getLayoutParams().width == FrameLayout.LayoutParams.MATCH_PARENT
                || myViewHolder.imgEmpresa.getLayoutParams().width == FrameLayout.LayoutParams.WRAP_CONTENT) {

            Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            try {
                w = size.x;
            } catch (Exception e) {
                w = display.getWidth();
            }
        }

        Uri uri = Uri.parse("http://www.comeja.com.br/imagens/"+mList.get(position).get("imagemEmpresa"));


        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setControllerListener(listener)
                .setOldController(myViewHolder.imgEmpresa.getController())
                .build();

        RoundingParams rp = RoundingParams.fromCornersRadii(roundPixels, roundPixels, 0, 0);
        myViewHolder.imgEmpresa.setController(dc);
        myViewHolder.imgEmpresa.getHierarchy().setRoundingParams(rp);

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


    public void addListItem(HashMap<String, String> c, int position){
        mList.add(c);
        notifyItemInserted(position);
    }


    public void removeListItem(int position){
        mList.remove(position);
        notifyItemRemoved(position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public SimpleDraweeView imgEmpresa;
        public TextView tv_nomeEmpresa, tv_idEmpresa;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgEmpresa = (SimpleDraweeView) itemView.findViewById(R.id.imgEmpresa);
            tv_nomeEmpresa = (TextView) itemView.findViewById(R.id.tv_nomeEmpresa);
            tv_idEmpresa = (TextView) itemView.findViewById(R.id.tv_idEmpresa);

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
