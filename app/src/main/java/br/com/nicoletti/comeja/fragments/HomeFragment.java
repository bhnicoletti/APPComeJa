package br.com.nicoletti.comeja.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.devspark.robototextview.widget.RobotoTextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.List;

import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.adapters.ConfiguracoesAdapter;
import br.com.nicoletti.comeja.extras.Mask;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;
import br.com.nicoletti.comeja.model.Configuracao;
import br.com.nicoletti.comeja.model.Endereco;


public class HomeFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    private List<Configuracao> mList;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        mList = ((MainActivity) getActivity()).getMural();

        boolean mensagem = false;
        if (MainActivity.updated) {
            for (int i = 0; i < mList.size(); i++) {
                Log.e("Versão", MainActivity.versao.toString());
                if (MainActivity.versao < mList.get(i).getVersaoAppAndroid()) {
                    mensagem = true;
                }

            }
        }
        if (mensagem) {
            AlertDialog.Builder build = new AlertDialog.Builder(getContext(), R.style.MaterialDrawerBaseTheme_Dialog);

            build.setTitle("INFORMAÇÔES");
            build.setMessage("Para o bom funcionamento do aplicativo recomendamos que seja realizado a atualização do app no Google Play.");
            build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    final String appPackageName = "br.com.nicoletti.comeja";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            build.create().show();
        } else {
            MainActivity.updated = true;
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.listaMensagens);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(llm);


        ConfiguracoesAdapter adapter = new ConfiguracoesAdapter(getActivity(), mList);

        mRecyclerView.setAdapter(adapter);


        return view;

    }

    @Override
    public void onClickListener(View view, int position) {


    }

    @Override
    public void onLongPressClickListener(View view, int position) {


    }


    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh) {
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv,
                                rv.getChildPosition(cv));
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildPosition(cv));
                    }

                    return (true);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onResume() { // HACKCODE TO WORK WHEN JUST COME BACK
        super.onResume();
    }


    @Override
    public void onStop() {
        super.onStop();
    }


}
