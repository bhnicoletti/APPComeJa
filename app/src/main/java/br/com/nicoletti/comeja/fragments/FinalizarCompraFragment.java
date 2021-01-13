package br.com.nicoletti.comeja.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.adapters.CarrinhoAdapter;
import br.com.nicoletti.comeja.extras.CarrinhoTouchHelper;
import br.com.nicoletti.comeja.extras.UtilTCM;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;
import br.com.nicoletti.comeja.model.Endereco;
import br.com.nicoletti.comeja.model.FormaPagamento;
import br.com.nicoletti.comeja.model.ItemVenda;
import br.com.nicoletti.comeja.model.WrapObjToNetwork;
import br.com.nicoletti.comeja.network.NetworkConnection;
import br.com.nicoletti.comeja.network.Transaction;


public class FinalizarCompraFragment extends Fragment {
    private RadioGroup rgEndereco, rgFormaPagamente;
    private TextView valorTotal;
    private String formaPagamento;
    private EditText troco;
    private Endereco endereco;
    private Boolean end = false;
    private Boolean pag = false;
    private List<Endereco> listaEndereco = new ArrayList<>();
    private List<FormaPagamento> listaFormaPagamento = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finalizarcompra, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        listaEndereco = ((MainActivity) getActivity()).getUsuarioLogado().getEnderecos();
        listaFormaPagamento = ((MainActivity) getActivity()).getCarrinhoCompras().get(0).getProdutoItemVenda().getEmpresaProduto().getListaFormaPagamento();
        //Botao
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pag) {
                    Toast.makeText(getContext(), "Selecione uma forma de pagamento", Toast.LENGTH_SHORT).show();
                }
                else if(!end){
                    Toast.makeText(getContext(), "Selecione um Endereço", Toast.LENGTH_SHORT).show();
                }
                else {
                    ((MainActivity) getActivity()).setEndereco(endereco);
                    Double valor = null;
                    if (!troco.getText().toString().equals("")) {
                        valor = Double.parseDouble(troco.getText().toString().replace(',', '.'));
                        Log.e("valor", valor.toString());
                    }
                    ((MainActivity) getActivity()).finalizarVenda(formaPagamento, valor);

                    try {
                        realizarVenda();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        rgEndereco = (RadioGroup) view.findViewById(R.id.rgEndereco);
        valorTotal = (TextView) view.findViewById(R.id.valorTotal);
        troco = (EditText) view.findViewById(R.id.edtTroco);
        Locale ptBr = new Locale("pt", "BR");
        String valorString = NumberFormat.getCurrencyInstance(ptBr).format(((MainActivity) getActivity()).getValorTotal());
        valorTotal.setText("Valor da compra: " + valorString);
        rgFormaPagamente = (RadioGroup) view.findViewById(R.id.rgFormaPagamento);

        for (int i = 0; i < listaFormaPagamento.size(); i++) {
            FormaPagamento fp = listaFormaPagamento.get(i);
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(fp.getFormaPagamento());
            radioButton.setTag(fp.getFormaPagamento());
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pag = true;
                    formaPagamento = v.getTag().toString();
                    if(formaPagamento.equals("Dinheiro")) {
                        troco.setVisibility(View.VISIBLE);
                    }
                    else{
                        troco.setVisibility(View.INVISIBLE);
                    }
                }
            });
            rgFormaPagamente.addView(radioButton);
        }


        for (int i = 0; i <= listaEndereco.size(); i++) {
            RadioButton radioButtonView = new RadioButton(getContext());

            if (i < listaEndereco.size()) {
                String stringendereco = listaEndereco.get(i).getRuaEndereco() + ", nº " + listaEndereco.get(i).getNumeroEndereco();

                radioButtonView.setText(stringendereco);
                radioButtonView.setTag(listaEndereco.get(i));
                radioButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        end = true;
                        endereco = (Endereco) v.getTag();
                    }
                });
            } else {
                radioButtonView.setText("Retirar no local");
                radioButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        end = true;
                        endereco = null;
                    }
                });

            }
            rgEndereco.addView(radioButtonView);
        }


        return view;
    }


    public void realizarVenda() throws JSONException {

        ((MainActivity) getActivity()).enviarVenda();

    }


}
