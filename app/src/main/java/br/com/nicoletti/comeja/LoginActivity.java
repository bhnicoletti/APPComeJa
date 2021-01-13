package br.com.nicoletti.comeja;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import br.com.nicoletti.comeja.extras.UtilTCM;
import br.com.nicoletti.comeja.model.Pessoa;
import br.com.nicoletti.comeja.network.CustomRequestObject;
import br.com.nicoletti.comeja.network.NetworkConnection;

/**
 * Created by Nicoletti on 04/04/2016
 */
public class LoginActivity extends ActionBarActivity {
    EditText edtLogin, edtSenha;
    Button btnLogar, btnCadastrar;
    private Toolbar mToolbar;
    private Toolbar mToolbarBottom;
    private static Pessoa usuario;
    private NetworkConnection networkConnection;
    private String url = "www.comeja.com.br";
    private ProgressBar progressBar;

    public static Pessoa getUsuario() {
        return usuario;
    }


    public static void setUsuario(Pessoa usuario) {
        LoginActivity.usuario = usuario;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        networkConnection = new NetworkConnection(getApplication());
        networkConnection.getRequestQueue();


        SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
        String temp = shared.getString("usuario", "");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        if (temp.length() > 5) {
            Log.e("valor", temp);
            Gson gson = new Gson();
            usuario = gson.fromJson(temp, Pessoa.class);
            SharedPreferences pref;
            pref = getSharedPreferences("info", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("usuario", gson.toJson(usuario));
            editor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtSenha = (EditText) findViewById(R.id.edtSenha);

        btnLogar = (Button) findViewById(R.id.btnAcessar);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);


        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);


        mToolbarBottom = (Toolbar) findViewById(R.id.inc_tb_bottom);
        mToolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()) {
                    case R.id.action_facebook:
                        it = new Intent(Intent.ACTION_VIEW);
                        it.setData(Uri.parse("https://www.facebook.com/comejaoficial/"));
                        break;
                }

                startActivity(it);
                return true;
            }
        });
        mToolbarBottom.inflateMenu(R.menu.menu_bottom);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Login", "fora");
                Log.e("Senha", edtSenha.getText().toString());
                Log.e("Login", edtLogin.getText().toString());
                progressBar.setVisibility(View.VISIBLE);

                final CustomRequestObject request = new CustomRequestObject(Request.Method.GET,
                        "http://" + url + "/service/usuario/" + "logar?email=" + edtLogin.getText().toString() + "&&senha=" + edtSenha.getText().toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressBar.setVisibility(View.GONE);
                                Gson gson = new GsonBuilder()
                                        .setDateFormat("yyyy-MM-dd")
                                        .create();
                                usuario = new Pessoa();
                                usuario = gson.fromJson(response.toString(), Pessoa.class);
                                SharedPreferences pref;
                                pref = getSharedPreferences("info", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("usuario", gson.toJson(usuario));
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Dados digitados incorretos!", Toast.LENGTH_LONG).show();
                                Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                                Log.e("GET", "Falso");

                            }
                        });


                request.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



                if (UtilTCM.verifyConnection(getApplicationContext())) {
                    networkConnection.addRequestQueue(request);
                } else {
                    exibirAlerta();
                }


            }
        });
    }

    public void exibirAlerta() {
        AlertDialog.Builder build = new AlertDialog.Builder(LoginActivity.this, R.style.MaterialDrawerBaseTheme_Dialog);
        build.setTitle("Alerta");
        build.setMessage("Sem conexão com o servidor, tente novamente mais tarde");
        build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        build.create().show();
    }
}
