package br.com.nicoletti.comeja.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.adapters.EmpresaAdapter;
import br.com.nicoletti.comeja.adapters.ProdutoAdapter;
import br.com.nicoletti.comeja.extras.UtilTCM;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;
import br.com.nicoletti.comeja.model.Cidade;
import br.com.nicoletti.comeja.model.WrapObjToNetwork;
import br.com.nicoletti.comeja.network.NetworkConnection;
import br.com.nicoletti.comeja.network.Transaction;

public class EmpresasFragment extends Fragment implements RecyclerViewOnClickListenerHack, Transaction {

    protected static final String TAG = "LOG";
    private RecyclerView mRecyclerView;
    private ArrayList<HashMap<String, String>> mList;
    private String categoriaPesquisada;
    private Spinner spinner;
    private List<Cidade> cidades;
    private String cidadeSelecionada;
    private boolean busca = true;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empresa, container, false);
        spinner = (Spinner) view.findViewById(R.id.sp_cidades);
        cidades = ((MainActivity)getActivity()).getCidades();
        String[] cidade = new String[cidades.size() + 1];
        cidade[0] = "Todas";
        cidadeSelecionada = "Todas";
        for (int i = 1; i <= cidades.size(); i++) {
            cidade[i] = cidades.get(i-1).getNome();

        }
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, cidade);
        adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cidadeSelecionada != spinner.getSelectedItem().toString()) {
                    cidadeSelecionada = spinner.getSelectedItem().toString();


                    mList = new ArrayList<HashMap<String, String>>();
                    EmpresaAdapter adapterNovo = new EmpresaAdapter(getActivity(), mList);
                    mRecyclerView.setAdapter(adapterNovo);
                    busca = true;

                    NetworkConnection.getInstance(getActivity()).executeGET(EmpresasFragment.this, ProdutosFragment.class.getName());
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mList = new ArrayList<>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_listEmpresas);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e("Scroll", "Rodou");
                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();

                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) {
                    Log.e("Scroll", "Chamou");
                    NetworkConnection.getInstance(getActivity()).executeGET(EmpresasFragment.this, EmpresasFragment.class.getName());
                }
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        EmpresaAdapter adapter = new EmpresaAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(adapter);


        return view;
    }


    @Override
    public void onResume() { // HACKCODE TO WORK WHEN JUST COME BACK
        super.onResume();
        callVolleyRequest();
    }


    public void callVolleyRequest() {
        NetworkConnection.getInstance(getActivity()).executeGET(this, EmpresasFragment.class.getName());
    }


    @Override
    public void onClickListener(View view, int position) {

        Long id = Long.parseLong(mList.get(position).get("idEmpresa"));
        ((MainActivity) getActivity()).buscarEmpresa(id);


    }

    @Override
    public void onLongPressClickListener(View view, int position) {
        Toast.makeText(getActivity(), "onLongPressClickListener(): " + position, Toast.LENGTH_SHORT).show();

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
        NetworkConnection.getInstance(getActivity()).getRequestQueue().cancelAll(EmpresasFragment.class.getName());
    }


    @Override
    public WrapObjToNetwork doBefore() {

        if (UtilTCM.verifyConnection(getActivity())) {
            ((MainActivity) getActivity()).exibeProgressBar();
            HashMap<String, String> params = new HashMap<>();
            categoriaPesquisada = ((MainActivity) getActivity()).getCategoriaEmpresa();
            if (cidadeSelecionada.equals("Todas"))
                return (new WrapObjToNetwork(params, "empresas?quant=2&&total=" + mList.size() + "&&categoria=" + ((MainActivity) getActivity()).getCategoriaEmpresa(), "usuario"));
            else
                return (new WrapObjToNetwork(params, "empresasPorCidade?quant=2&&total=" + mList.size() + "&&categoria=" + ((MainActivity) getActivity()).getCategoriaEmpresa() + "&&cidade=" + cidadeSelecionada, "usuario"));
        } else {
            Toast.makeText(getContext(), "Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    @Override
    public void doAfter(JSONArray jsonArray) {
        ((MainActivity) getActivity()).ocultaProgressBar();
        Log.e("Tamanho", "" + jsonArray.length());
        if (jsonArray.length() > 0) {
            EmpresaAdapter adapter = (EmpresaAdapter) mRecyclerView.getAdapter();
            int auxPosition = 0, position;
            try {
                for (int i = 0, tamI = jsonArray.length(); i < tamI; i++) {
                    HashMap<String, String> listaEmpresas = new HashMap<>();
                    listaEmpresas.put("idEmpresa", jsonArray.getJSONArray(i).getString(0));
                    listaEmpresas.put("nomeEmpresa", jsonArray.getJSONArray(i).getString(1));
                    listaEmpresas.put("imagemEmpresa", jsonArray.getJSONArray(i).getString(2));

                    position = auxPosition == 0 ? mList.size() : 0;
                    // mList.add(listaEmpresas);
                    adapter.addListItem(listaEmpresas, position);

                    if (auxPosition == 1) {
                        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, position);
                    }
                }


            } catch (JSONException e) {
                Log.i(TAG, "doAfter(): " + e.getMessage());
            }
        } else {
            if (mList.isEmpty()) {
                Toast.makeText(getActivity(), "Nenhum dado encontrado.", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).fecharFragmentos();
            }

        }
    }

}
