package br.com.nicoletti.comeja.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.adapters.UltimosPedidosAdapter;
import br.com.nicoletti.comeja.extras.UtilTCM;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;
import br.com.nicoletti.comeja.model.WrapObjToNetwork;
import br.com.nicoletti.comeja.network.NetworkConnection;
import br.com.nicoletti.comeja.network.Transaction;


public class UltimosPedidosFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction {

    private RecyclerView mRecyclerView;
    private ArrayList<HashMap<String, String>> mList;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ultimasvendas, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_listaultimasvendas);
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
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener( getActivity(), mRecyclerView, this ));


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mList = new ArrayList<HashMap<String, String>>();
        UltimosPedidosAdapter adapterNovo = new UltimosPedidosAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapterNovo);





        return view;
    }

    @Override
    public void onClickListener(View view, int position) {
        ((MainActivity)getActivity()).buscarVenda(Integer.parseInt(mList.get(position).get("idVenda")));
    }
    @Override
    public void onLongPressClickListener(View view, int position) {
       // Toast.makeText(getActivity(), "onLongPressClickListener(): "+position, Toast.LENGTH_SHORT).show();

        /*CarAdapter adapter = (CarAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);*/
    }



    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh){
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv,
                                rv.getChildPosition(cv) );
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if(cv != null && mRecyclerViewOnClickListenerHack != null){
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildPosition(cv) );
                    }

                    return(true);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    @Override
    public void onResume() { // HACKCODE TO WORK WHEN JUST COME BACK
        super.onResume();
        callVolleyRequest();

    }




    public void callVolleyRequest() {
        NetworkConnection.getInstance(getActivity()).executeGET(this, UltimosPedidosFragment.class.getName());
    }
    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll(UltimosPedidosFragment.class.getName());
    }


    @Override
    public WrapObjToNetwork doBefore() {
        if (UtilTCM.verifyConnection(getActivity())) {
            ((MainActivity)getActivity()).exibeProgressBar();
            HashMap<String, String> params = new HashMap<>();
                return (new WrapObjToNetwork(params, "ultimascompras?idCliente=" + ((MainActivity) getActivity()).getUsuarioLogado().getIdPessoa(), "venda"));

        } else {
            Toast.makeText(getContext(), "Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void doAfter(JSONArray jsonArray) {
        ((MainActivity)getActivity()).ocultaProgressBar();
        Log.e("Tamanho", "" + jsonArray.length());
        if (jsonArray.length() > 0) {
            UltimosPedidosAdapter adapter = (UltimosPedidosAdapter) mRecyclerView.getAdapter();
            int auxPosition = 0, position;
            try {
                for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {
                    HashMap<String, String> listaProdutos = new HashMap<>();
                    listaProdutos.put("idVenda", jsonArray.getJSONArray(i).getString(0));
                    listaProdutos.put("formaPagamento", jsonArray.getJSONArray(i).getString(1));
                    listaProdutos.put("dataVenda", jsonArray.getJSONArray(i).getString(2));
                    listaProdutos.put("vlrTotalVenda", jsonArray.getJSONArray(i).getString(3));

                    position = auxPosition == 0 ? mList.size() : 0;
                    adapter.addListItem(listaProdutos, position);

                 /*   if (auxPosition == 1) {
                        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, position);
                    }*/
                }


            } catch (JSONException e) {
                Log.i("Erro", "doAfter(): " + e.getMessage());
            }
        } else {
            if (mList.isEmpty())
                Toast.makeText(getActivity(), "Nenhum dado encontrado.", Toast.LENGTH_SHORT).show();
        }

    }
}
