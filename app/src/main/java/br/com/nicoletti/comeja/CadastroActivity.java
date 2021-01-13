package br.com.nicoletti.comeja;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.nicoletti.comeja.extras.CPFValidator;
import br.com.nicoletti.comeja.extras.Criptografia;
import br.com.nicoletti.comeja.extras.Mask;
import br.com.nicoletti.comeja.extras.UtilTCM;
import br.com.nicoletti.comeja.model.Pessoa;
import br.com.nicoletti.comeja.network.CustomRequestObject;
import br.com.nicoletti.comeja.network.NetworkConnection;

/**
 * Created by Nicoletti on 04/04/2016
 */
public class CadastroActivity extends ActionBarActivity {
    EditText edtnome, edtsenha, edtemail, edtdatanasc, edttelefone, edtcelular, edtconfirmasenha;
    CheckBox cbtermos;
    Button btnSalvar;
    private Pessoa usuario;
    private NetworkConnection networkConnection;
    private String url = "www.comeja.com.br";
    private TextView termos;

    public Pessoa getUsuario() {
        return usuario;
    }

    public void setUsuario(Pessoa usuario) {
        this.usuario = usuario;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_usuario);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        networkConnection = new NetworkConnection(getApplication());
        networkConnection.getRequestQueue();

        usuario = new Pessoa();
        edtnome = (EditText) findViewById(R.id.nomePessoa);

        edttelefone = (EditText) findViewById(R.id.telefoneFixo);
        edttelefone.addTextChangedListener(Mask.insert(Mask.TELEFONE_MASK, edttelefone));
        edtcelular = (EditText) findViewById(R.id.telefoneCelular);
        edtcelular.addTextChangedListener(Mask.insert(Mask.CELULAR_MASK, edtcelular));
        edtemail = (EditText) findViewById(R.id.email);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        edtdatanasc = (EditText) findViewById(R.id.dataNasc);
        edtdatanasc.addTextChangedListener(Mask.insert(Mask.DATA_MASK, edtdatanasc));
        edtsenha = (EditText) findViewById(R.id.senha);
        edtconfirmasenha = (EditText) findViewById(R.id.confirmaSenha);
        cbtermos = (CheckBox) findViewById(R.id.checkboxTermos);
        termos = (TextView) findViewById(R.id.termos);

        btnSalvar = (Button) findViewById(R.id.btnSalvar);

        cbtermos.setText(
                Html.fromHtml(
                        "Eu li e aceito os <a href=\"http://comeja.com.br/paginasUsuario/termos.jsf\">Termos e condições de uso</a>  do site "));
        cbtermos.setMovementMethod(LinkMovementMethod.getInstance());


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valida()) {

                    if (edtsenha.getText().toString().equals(edtconfirmasenha.getText().toString())) {


                        usuario.setNomeFantasiaPessoa(edtnome.getText().toString());
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        Date parsed = null;
                        try {
                            parsed = format.parse(edtdatanasc.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        usuario.setDataNascPessoa(parsed);
                        usuario.setSenhaPessoa(Criptografia.criptografar(edtsenha.getText().toString()));

                        usuario.setTelefonePessoa(edttelefone.getText().toString());
                        usuario.setCelularPessoa(edtcelular.getText().toString());
                        usuario.setEmailPessoa(edtemail.getText().toString());

                        usuario.setTipoPessoa("Cliente");


                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd")
                                .create();
                        String s = gson.toJson(usuario);


                        JsonObjectRequest request = null;
                        try {
                            request = new JsonObjectRequest("http://" + url + "/service/usuario/editar", new JSONObject(s),
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.e("Resposta", response.toString());
                                            Gson gson = new Gson();
                                            usuario = gson.fromJson(response.toString(), Pessoa.class);
                                            Toast.makeText(getApplicationContext(), "Usuário Cadastrado, efetue o login para acessar o APP", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    VolleyLog.e("Error: ", error.getMessage());
                                }
                            }) {
                                @Override
                                public String getBodyContentType() {
                                    return String.format("application/json; charset=utf-8");
                                }
                            };
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        request.setTag("request");


                        if (UtilTCM.verifyConnection(getApplicationContext())) {
                            networkConnection.addRequestQueue(request);
                        } else {
                            exibirAlerta();
                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Senhas diferentes", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    public void exibirAlerta() {
        AlertDialog.Builder build = new AlertDialog.Builder(CadastroActivity.this, R.style.MaterialDrawerBaseTheme_Dialog);
        build.setTitle("Alerta");
        build.setMessage("Sem conexão com o servidor, tente novamente mais tarde");
        build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        build.create().show();
    }

    private boolean valida() {
        if (edtsenha.getText().toString().length() > 0
                && edtconfirmasenha.getText().toString().length() > 0
                && edtnome.getText().toString().length() > 0
                && edtdatanasc.getText().toString().length() > 0
                && edtcelular.getText().toString().length() > 0
                && edtemail.getText().toString().length() > 0
                && validar(edtemail.getText().toString())
                && cbtermos.isChecked()) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean validar(String email)
    {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }


}
