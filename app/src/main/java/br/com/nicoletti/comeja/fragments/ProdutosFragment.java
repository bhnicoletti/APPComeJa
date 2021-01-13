package br.com.nicoletti.comeja.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import br.com.nicoletti.comeja.InformacoesEmpresa;
import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.adapters.ProdutoAdapter;
import br.com.nicoletti.comeja.extras.UtilTCM;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;
import br.com.nicoletti.comeja.model.Pessoa;
import br.com.nicoletti.comeja.model.WrapObjToNetwork;
import br.com.nicoletti.comeja.network.NetworkConnection;
import br.com.nicoletti.comeja.network.Transaction;


public class ProdutosFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction {
    protected static final String TAG = "LOG";
    private RecyclerView mRecyclerView;
    private ArrayList<HashMap<String, String>> mList;
    private ViewPager mViewPager;
    private String categoriaSelecionada;
    private Pessoa empresa;
    private TextView nomeEmpresa, telefoneEmpresa, tempoPreparo, valorEntrega;
    private Spinner spinner;
    private boolean busca = true;
    private Button btnInformacoes;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

        categoriaSelecionada = "Selecione uma categoria";
        nomeEmpresa = (TextView) view.findViewById(R.id.tv_nomeEmpresa);
        /*telefoneEmpresa = (TextView) view.findViewById(R.id.tv_telefone);
        tempoPreparo = (TextView) view.findViewById(R.id.tv_tempoPreparo);
        valorEntrega = (TextView) view.findViewById(R.id.tv_valorEntrega);*/
        spinner = (Spinner) view.findViewById(R.id.sp_categorias);
        btnInformacoes = (Button) view.findViewById(R.id.btnInformacoes);
       // btnCarregarMais = (Button) view.findViewById(R.id.btnCarregarMais);
        empresa = ((MainActivity) getActivity()).getEmpresaSelecionada();
        nomeEmpresa.setText(empresa.getNomeFantasiaPessoa());
       /* valorEntrega.setText("Valor Entrega: R$ " + empresa.getValorEntrega());
        tempoPreparo.setText("Tempo Preparo: " + empresa.getTempoPreparo());
        telefoneEmpresa.setText("Telefones: " + empresa.getCelularPessoa()
                + " | " + empresa.getTelefonePessoa());*/
        String[] categorias = new String[empresa.getCategorias().size() + 1];
        categorias[0] = "Selecione uma categoria";
        for (int i = 1; i <= empresa.getCategorias().size(); i++) {
            Log.e("" + i, empresa.getCategorias().get(i - 1).getTituloCategoria());
            categorias[i] = empresa.getCategorias().get(i - 1).getTituloCategoria();

        }

        btnInformacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //InformacoesEmpresa informacoesEmpresa = new InformacoesEmpresa(empresa);
                startActivity(new Intent(getActivity(), InformacoesEmpresa.class));
            }
        });

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categorias);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (categoriaSelecionada != spinner.getSelectedItem().toString()) {
                    categoriaSelecionada = spinner.getSelectedItem().toString();



                    mList = new ArrayList<HashMap<String, String>>();
                    ProdutoAdapter adapterNovo = new ProdutoAdapter(getActivity(), mList);
                    mRecyclerView.setAdapter(adapterNovo);
                    busca = true;

                    NetworkConnection.getInstance(getActivity()).executeGET(ProdutosFragment.this, ProdutosFragment.class.getName());
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mList = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_listProduto);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // true


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("Scroll", "Rodou");

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();


                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    if (busca) {
                        NetworkConnection.getInstance(getActivity()).executeGET(ProdutosFragment.this, ProdutosFragment.class.getName());
                    }
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        ProdutoAdapter adapter = new ProdutoAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapter);


        return view;

    }


    @Override
    public void onResume() { // HACKCODE TO WORK WHEN JUST COME BACK
        super.onResume();
        callVolleyRequest();

    }
    public void callVolleyRequest() {
        NetworkConnection.getInstance(getActivity()).executeGET(this, ProdutosFragment.class.getName());
    }


    @Override
    public void onClickListener(View view, int position) {
        Long id = Long.parseLong(mList.get(position).get("idProduto"));
        ((MainActivity) getActivity()).buscarIngredientesAdicionais(empresa.getIdPessoa(), id);


    }

    @Override
    public void onLongPressClickListener(View view, int position) {


    }


    public static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {
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
                                rv.getChildAdapterPosition(cv));
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    boolean callContextMenuStatus = false;
                    if (cv instanceof CardView) {
                        float x = ((RelativeLayout) ((CardView) cv).getChildAt(0)).getChildAt(2).getX();
                        float w = ((RelativeLayout) ((CardView) cv).getChildAt(0)).getChildAt(2).getWidth();
                        float y;// = ((RelativeLayout) ((CardView) cv).getChildAt(0)).getChildAt(3).getY();
                        float h = ((RelativeLayout) ((CardView) cv).getChildAt(0)).getChildAt(2).getHeight();

                        Rect rect = new Rect();
                        ((RelativeLayout) ((CardView) cv).getChildAt(0)).getChildAt(2).getGlobalVisibleRect(rect);
                        y = rect.top;

                        if (e.getX() >= x && e.getX() <= w + x && e.getRawY() >= y && e.getRawY() <= h + y) {
                            callContextMenuStatus = true;
                        }
                    }


                    if (cv != null && mRecyclerViewOnClickListenerHack != null && !callContextMenuStatus) {
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildAdapterPosition(cv));
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
        public void onRequestDisallowInterceptTouchEvent(boolean b) {
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll(ProdutosFragment.class.getName());
    }


    @Override
    public WrapObjToNetwork doBefore() {

        if (UtilTCM.verifyConnection(getActivity())) {
            ((MainActivity)getActivity()).exibeProgressBar();
            HashMap<String, String> params = new HashMap<>();
            if (categoriaSelecionada.equals("Selecione uma categoria"))
                return (new WrapObjToNetwork(params, "porempresa?empresa=" + ((MainActivity) getActivity()).getEmpresaSelecionada().getIdPessoa() + "&&quant=2&&total=" + mList.size(), "produto"));
            else
                return (new WrapObjToNetwork(params, "porempresaCategoria?empresa=" + ((MainActivity) getActivity()).getEmpresaSelecionada().getIdPessoa() + "&&quant=2&&total=" + mList.size() + "&&categoria=" + categoriaSelecionada, "produto"));
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
            ProdutoAdapter adapter = (ProdutoAdapter) mRecyclerView.getAdapter();
            int auxPosition = 0, position;
            try {
                for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {
                    HashMap<String, String> listaProdutos = new HashMap<>();
                    listaProdutos.put("idProduto", jsonArray.getJSONArray(i).getString(0));
                    listaProdutos.put("nomeProduto", jsonArray.getJSONArray(i).getString(1));
                    listaProdutos.put("valorProduto", jsonArray.getJSONArray(i).getString(2));
                    listaProdutos.put("imagemProduto", jsonArray.getJSONArray(i).getString(3));

                    position = auxPosition == 0 ? mList.size() : 0;
                    adapter.addListItem(listaProdutos, position);
                    if (auxPosition == 1) {
                        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, position);
                    }

                }


            } catch (JSONException e) {
                Log.i(TAG, "doAfter(): " + e.getMessage());
            }
        } else {
            busca = false;
            if (mList.isEmpty())
                Toast.makeText(getActivity(), "Nenhum dado encontrado.", Toast.LENGTH_SHORT).show();
        }
    }
}
