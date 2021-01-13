package br.com.nicoletti.comeja.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.net.Uri;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.adapters.IngredienteAdapter;
import br.com.nicoletti.comeja.adapters.IngredienteAdicionaisAdapter;
import br.com.nicoletti.comeja.extras.Utility;
import br.com.nicoletti.comeja.model.Ingrediente;
import br.com.nicoletti.comeja.model.ItemVenda;
import br.com.nicoletti.comeja.model.Produto;


public class ProdutoFragment extends Fragment {
    private Produto produto;
    private TextView nomeProduto, descricaoProduto, valorProduto, txtingredientes, txtingredientesAdicionais;
    private SimpleDraweeView imagemProduto;
    private EditText quantidade;
    private Button btnAdicione;
    private ItemVenda itemVenda = new ItemVenda();
    private ListView listaIngredientes, listaIngredientesAdicionais;
    private RadioGroup rgTamanho;
    private CheckBox cbmetade;
    private Double valor;
    private Boolean metade = false;
    private TextView txtQuantidade;
    private String tamanho;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Fresco.initialize(getContext());
        View view = inflater.inflate(R.layout.fragment_produto, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        produto = ((MainActivity) getActivity()).getProd();
        nomeProduto = (TextView) view.findViewById(R.id.txtNomeProduto);
        descricaoProduto = (TextView) view.findViewById(R.id.txtDescricaoProduto);
        valorProduto = (TextView) view.findViewById(R.id.txtValorProduto);
        imagemProduto = (SimpleDraweeView) view.findViewById(R.id.imgProduto);
        quantidade = (EditText) view.findViewById(R.id.edtQuantidade);
        btnAdicione = (Button) view.findViewById(R.id.btnAdicionar);
        listaIngredientes = (ListView) view.findViewById(R.id.lv_ingredientes);
        listaIngredientesAdicionais = (ListView) view.findViewById(R.id.lv_adicionais);
        txtingredientes = (TextView) view.findViewById(R.id.txtingredientes);
        txtingredientesAdicionais = (TextView) view.findViewById(R.id.txtIngredientesAdicionais);
        rgTamanho = (RadioGroup) view.findViewById(R.id.rgTamanho);
        cbmetade = (CheckBox) view.findViewById(R.id.cbmetade);
        txtQuantidade = (TextView) view.findViewById(R.id.txtQuantidade);
        valor = null;
        tamanho = "";

        if(produto.getCategoriaProduto().getTituloCategoria().equals("Pizza")){
            cbmetade.setVisibility(View.VISIBLE);
        }
        List<Ingrediente> listaIngrediente = ((MainActivity) getActivity()).getListaIngredientesAdicionais();
        final IngredienteAdicionaisAdapter ingredienteAdicionaisAdapter = new IngredienteAdicionaisAdapter(listaIngrediente, getContext());
        if (!produto.getCategoriaProduto().getTituloCategoria().equals("Bebida")) {
            txtingredientes.setText("Ingredientes");
            txtingredientesAdicionais.setText("Ingredientes Adicionais");
            listaIngredientesAdicionais.setAdapter(ingredienteAdicionaisAdapter);

            Utility.setListViewHeightBasedOnChildren(listaIngredientesAdicionais);
        }

        List<Ingrediente> listaIngredienteProduto = produto.getIngredientesProduto();
        final IngredienteAdapter ingredienteAdapter = new IngredienteAdapter(listaIngredienteProduto, getContext());
        listaIngredientes.setAdapter(ingredienteAdapter);
        Utility.setListViewHeightBasedOnChildren(listaIngredientes);


        nomeProduto.setText(produto.getNomeProduto());
        descricaoProduto.setText(produto.getDescricaoProduto());


        for (int i = 0; i < produto.getTamanhos().size(); i++) {
            Locale ptBr = new Locale("pt", "BR");
            String valorString = NumberFormat.getCurrencyInstance(ptBr).format(produto.getTamanhos().get(i).getValorTamanho());
            RadioButton radioButtonCartao = new RadioButton(getContext());
            radioButtonCartao.setText(produto.getTamanhos().get(i).getTamanho() + "  " +valorString );
            radioButtonCartao.setTag(R.id.valor,produto.getTamanhos().get(i).getValorTamanho());
            radioButtonCartao.setTag(R.id.tamanho,produto.getTamanhos().get(i).getTamanho());
            radioButtonCartao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    valor = Double.parseDouble(v.getTag(R.id.valor).toString());
                    tamanho = v.getTag(R.id.tamanho).toString();
                }
            });
            rgTamanho.addView(radioButtonCartao);
        }


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

        Uri uri = Uri.parse("http://www.comeja.com.br/imagens/" + produto.getImagemProduto());


        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setControllerListener(listener)
                .setOldController(imagemProduto.getController())
                .build();
        imagemProduto.setController(dc);


        cbmetade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cbmetade.isChecked()){
                    metade = true;
                    quantidade.setVisibility(View.INVISIBLE);
                    txtQuantidade.setVisibility(View.INVISIBLE);
                }
                else {
                    metade = false;
                    quantidade.setVisibility(View.VISIBLE);
                    txtQuantidade.setVisibility(View.VISIBLE);
                }

            }
        });
        btnAdicione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!metade && quantidade.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Por favor, digite a quantidade.", Toast.LENGTH_SHORT).show();
                }
                else if(valor == null){
                    Toast.makeText(getContext(), "Por favor, selecione o valor do produto.", Toast.LENGTH_SHORT).show();
                }else {
                    //Adicionar ao carrinho
                    itemVenda.setIngredientesAdicionais(ingredienteAdicionaisAdapter.getIngredientesSelecionados());
                    itemVenda.setIngredientesProduto(ingredienteAdapter.getIngredientesSelecionados());
                    itemVenda.setProdutoItemVenda(produto);
                    if(metade){
                        itemVenda.setQuantItemVenda(0.5);
                    }else {
                        itemVenda.setQuantItemVenda(Double.parseDouble(quantidade.getText().toString()));
                    }

                    ((MainActivity) getActivity()).setValor(valor);
                    Log.e("Ingredientes Produto", produto.getIngredientesProduto().size()+"");
                    Log.e("Ingredientes", itemVenda.getIngredientesProduto().size()+"");
                    ((MainActivity) getActivity()).adicionarAoCarrinhoModoUsuario(produto, itemVenda.getQuantItemVenda(), itemVenda.getIngredientesProduto(), itemVenda.getIngredientesAdicionais(), metade, tamanho);
                }

            }
        });

        return view;

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
