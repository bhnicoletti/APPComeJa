package br.com.nicoletti.comeja.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.adapters.CarrinhoAdapter;
import br.com.nicoletti.comeja.adapters.ItemIngredienteAdpater;
import br.com.nicoletti.comeja.extras.CarrinhoTouchHelper;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;
import br.com.nicoletti.comeja.model.ItemVenda;


public class CarrinhoFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    private List<ItemVenda> mList;
    private TextView txtCarrinho;
    private RecyclerView mRecyclerViewDialogo;
    private ArrayList<HashMap<String,String>> mListDialogo;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_carrinho, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_listCarrinho);
        txtCarrinho = (TextView) view.findViewById(R.id.txtCarrinho);
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

        mList = ((MainActivity) getActivity()).getCarrinhoCompras();
        CarrinhoAdapter adapter = new CarrinhoAdapter(getActivity(), mList);
        if(mList.isEmpty()){
            txtCarrinho.setText("Nenhum produto adicionado");
        }
        else{
            txtCarrinho.setText("Total: R$"+((MainActivity) getActivity()).getValorTotal().toString());
        }
        mRecyclerView.setAdapter( adapter );
        ItemTouchHelper.Callback callback = new CarrinhoTouchHelper(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mList.isEmpty())
                ((MainActivity)getActivity()).iniciarFragFinalizarCompra();
                else{
                    Toast.makeText(getContext(),"O carrinho está vazio! Adicione algum produto para processeguir",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    @Override
    public void onClickListener(View view, int position) {
        mListDialogo = new ArrayList<>();
        LayoutInflater inflaterDialogo = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewDialogo  = inflaterDialogo.inflate(R.layout.dialogo, null);
        mRecyclerViewDialogo = (RecyclerView) viewDialogo.findViewById(R.id.rv_listaIngredientes);


        mRecyclerViewDialogo.setClickable(false);

        mRecyclerViewDialogo.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


            }
        });


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerViewDialogo.setLayoutManager(llm);

        for (int i = 0; i < mList.get(position).getIngredientesProduto().size(); i++) {
            HashMap<String, String> lista = new HashMap<>();
            lista.put("tipo" , "produto");
            lista.put("ingrediente" , mList.get(position).getIngredientesProduto().get(i).getNomeIngrediente());
            mListDialogo.add(lista);

        }


        for (int i = 0; i < mList.get(position).getIngredientesAdicionais().size(); i++) {
            HashMap<String, String> lista = new HashMap<>();
            lista.put("tipo" , "adicional");
            lista.put("ingrediente" , mList.get(position).getIngredientesAdicionais().get(i).getNomeIngrediente());
            mListDialogo.add(lista);

        }


        ItemIngredienteAdpater adapter = new ItemIngredienteAdpater(getActivity(), mListDialogo);

        mRecyclerViewDialogo.setAdapter(adapter);

        Dialog dl = new Dialog(getActivity());
        dl.setContentView(viewDialogo);
        dl.setTitle("Ingredientes");
        dl.show();


    }
    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(getActivity(),"Para remover este item, basta puxa-lo para o lado", Toast.LENGTH_SHORT).show();
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
}
