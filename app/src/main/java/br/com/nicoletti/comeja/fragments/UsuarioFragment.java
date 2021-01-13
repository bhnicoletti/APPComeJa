package br.com.nicoletti.comeja.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.nicoletti.comeja.MainActivity;
import br.com.nicoletti.comeja.R;
import br.com.nicoletti.comeja.extras.Criptografia;
import br.com.nicoletti.comeja.extras.Mask;
import br.com.nicoletti.comeja.interfaces.RecyclerViewOnClickListenerHack;
import br.com.nicoletti.comeja.model.Endereco;
import br.com.nicoletti.comeja.model.Pessoa;
import br.com.nicoletti.comeja.model.WrapObjToNetwork;
import br.com.nicoletti.comeja.network.Transaction;


public class UsuarioFragment extends Fragment implements Transaction {

    private EditText nome, dataNasc, telefonefixo, telefoneCelular, senha, confirmasenha, email;
    private Button btnSalvar;
    private Pessoa pessoa;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usuario, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        nome = (EditText) view.findViewById(R.id.nomePessoa);

        telefonefixo = (EditText) view.findViewById(R.id.telefoneFixo);
        telefoneCelular = (EditText) view.findViewById(R.id.telefoneCelular);
        senha = (EditText) view.findViewById(R.id.senha);
        confirmasenha = (EditText) view.findViewById(R.id.confirmaSenha);
        email = (EditText) view.findViewById(R.id.email);
        dataNasc = (EditText) view.findViewById(R.id.dataNasc);
        btnSalvar = (Button) view.findViewById(R.id.btnSalvar);

        pessoa = ((MainActivity) getActivity()).getUsuarioLogado();

        nome.setText(pessoa.getNomeFantasiaPessoa());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        dataNasc.setText(df.format(pessoa.getDataNascPessoa()));
        dataNasc.addTextChangedListener(Mask.insert(Mask.DATA_MASK, dataNasc));

        telefonefixo.setText(pessoa.getTelefonePessoa());
        telefonefixo.addTextChangedListener(Mask.insert(Mask.TELEFONE_MASK, telefonefixo));
        telefoneCelular.setText(pessoa.getCelularPessoa());
        telefoneCelular.addTextChangedListener(Mask.insert(Mask.CELULAR_MASK, telefoneCelular));
        email.setText(pessoa.getEmailPessoa());

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (senha.getText().toString().equals(confirmasenha.getText().toString()) && !senha.getText().toString().equals("")) {
                    pessoa.setNomeFantasiaPessoa(nome.getText().toString());
                    pessoa.setTelefonePessoa(telefonefixo.getText().toString());
                    pessoa.setCelularPessoa(telefoneCelular.getText().toString());
                    pessoa.setEmailPessoa(email.getText().toString());
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date parsed = null;
                    try {
                        parsed = format.parse(dataNasc.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    pessoa.setDataNascPessoa(parsed);
                    pessoa.setSenhaPessoa(Criptografia.criptografar(senha.getText().toString()));

                    Gson gson = new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd")
                            .create();
                    try {
                        ((MainActivity)getActivity()).atualizarUsuario(gson.toJson(pessoa));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getContext(),"As senhas digitadas estão diferentes",Toast.LENGTH_LONG).show();
                    senha.findFocus();
                }
            }
        });


        return view;
    }


    @Override
    public WrapObjToNetwork doBefore() {
        return null;
    }

    @Override
    public void doAfter(JSONArray jsonArray) {

    }
}
