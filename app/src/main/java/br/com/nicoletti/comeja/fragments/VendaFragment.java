package br.com.nicoletti.comeja.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Animatable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.adapters.EnderecosAdapter;
import br.com.nicoletti.comeja.adapters.IngredienteAdapter;
import br.com.nicoletti.comeja.adapters.IngredienteAdicionaisAdapter;
import br.com.nicoletti.comeja.adapters.ItemIngredienteAdpater;
import br.com.nicoletti.comeja.adapters.VendaAdapter;
import br.com.nicoletti.comeja.extras.Utility;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;
import br.com.nicoletti.comeja.model.Endereco;
import br.com.nicoletti.comeja.model.Ingrediente;
import br.com.nicoletti.comeja.model.ItemVenda;
import br.com.nicoletti.comeja.model.Produto;
import br.com.nicoletti.comeja.model.Venda;


public class VendaFragment extends Fragment implements RecyclerViewOnClickListenerHack {
    TextView txtDataVenda, txtStatusVenda, txtVlrVenda, txtIdVenda, txtFormaPagamento, txtHora;
    private RecyclerView mRecyclerView;
    private List<ItemVenda> mList;
    private Venda venda;

    private RecyclerView mRecyclerViewDialogo;
    private ArrayList<HashMap<String,String>> mListDialogo;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_venda, container, false);

        txtIdVenda = (TextView) view.findViewById(R.id.txtIdVenda);
        txtDataVenda = (TextView) view.findViewById(R.id.txtDataVenda);
        txtStatusVenda = (TextView) view.findViewById(R.id.txtstatusVenda);
        txtVlrVenda = (TextView) view.findViewById(R.id.txtvlrVenda);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listaProdutos);
        txtFormaPagamento = (TextView) view.findViewById(R.id.txtFormaPagamento);
       // txtHora = (TextView) view.findViewById(R.id.txtHora);

        venda = ((MainActivity)getActivity()).getVendaSelecionada();

        txtIdVenda.setText("Empresa: "+venda.getEmpresa().getNomeFantasiaPessoa());
        Locale ptBr = new Locale("pt", "BR");
        String valorString = NumberFormat.getCurrencyInstance(ptBr).format(venda.getVlrTotalVenda());
        txtVlrVenda.setText("Valor Total: "+valorString);
        txtStatusVenda.setText("Status: "+venda.getStatusVenda());
        txtFormaPagamento.setText("Forma de Pagamento: "+venda.getFormPagamento());


        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        String data = formataData.format(venda.getDataVenda());
        txtDataVenda.setText(data+" - "+venda.getHora());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.listaProdutos);
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

        mList = ((MainActivity) getActivity()).getVendaSelecionada().getCarrinho();
        VendaAdapter adapter = new VendaAdapter(getActivity(), mList);

        mRecyclerView.setAdapter(adapter);


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
