package br.com.nicoletti.comeja;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import br.com.nicoletti.comeja.extras.UtilTCM;
import br.com.nicoletti.comeja.extras.Utility;
import br.com.nicoletti.comeja.fragments.CarrinhoFragment;
import br.com.nicoletti.comeja.fragments.EmpresasFragment;
import br.com.nicoletti.comeja.fragments.EnderecoFragment;
import br.com.nicoletti.comeja.fragments.EnderecosFragment;
import br.com.nicoletti.comeja.fragments.FinalizarCompraFragment;
import br.com.nicoletti.comeja.fragments.HomeFragment;
import br.com.nicoletti.comeja.fragments.ProdutoFragment;
import br.com.nicoletti.comeja.fragments.ProdutosFragment;
import br.com.nicoletti.comeja.fragments.UltimosPedidosFragment;
import br.com.nicoletti.comeja.fragments.UsuarioFragment;
import br.com.nicoletti.comeja.fragments.VendaFragment;
import br.com.nicoletti.comeja.model.Categoria;
import br.com.nicoletti.comeja.model.Cidade;
import br.com.nicoletti.comeja.model.Configuracao;
import br.com.nicoletti.comeja.model.Dispositivo;
import br.com.nicoletti.comeja.model.Endereco;
import br.com.nicoletti.comeja.model.Ingrediente;
import br.com.nicoletti.comeja.model.ItemVenda;
import br.com.nicoletti.comeja.model.Pessoa;
import br.com.nicoletti.comeja.model.Produto;
import br.com.nicoletti.comeja.model.TokenEvent;
import br.com.nicoletti.comeja.model.Venda;
import br.com.nicoletti.comeja.network.CustomRequest;
import br.com.nicoletti.comeja.network.CustomRequestObject;
import br.com.nicoletti.comeja.network.NetworkConnection;
import br.com.nicoletti.comeja.service.FirebaseInstanceIDService;
import br.com.nicoletti.comeja.service.FirebaseMessagingService;


public class MainActivity extends ActionBarActivity {
    private Toolbar mToolbar;
    private Drawer.Result navigationDrawerLeft;
    private AccountHeader.Result headerNavigationLeft;
    private String categoriaSelecionada;
    public static Pessoa empresaSelecionada;
    private Pessoa usuarioLogado;
    private ItemVenda itemVenda;
    private Venda vend;
    private ArrayList<ItemVenda> carrinhoCompras;
    private Double valorTotal = 0.00;
    private Endereco endereco;
    private Produto prod;
    private String categoriaEmpresa;
    private List<Ingrediente> listaIngredientesAdicionais;
    private NetworkConnection networkConnection;
    private boolean fecharEmpresa;
    private Integer pos = -1;
    private boolean logado;
    private ProgressBar progressBar;
    private String url = "www.comeja.com.br";
    private Venda vendaSelecionada;
    private List<Configuracao> mural;
    public static Integer versao;
    private ProgressDialog pd;
    private Double valor;
    private Boolean alcoolico = false;
    private List<Cidade> cidades;
    private Integer quantMetadeGrande = 0;
    private Integer quantMetadeMedia = 0;
    private Integer quantMetadeBroto = 0;
    private Integer quantMetadeMini = 0;
    private Dispositivo dispositivo;
    public static Boolean updated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("");
        mToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(mToolbar);
        networkConnection = new NetworkConnection(getApplication());
        networkConnection.getRequestQueue();
        vend = new Venda();
        carrinhoCompras = new ArrayList();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        try {
            versao = getPackageManager().getPackageInfo(getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        pd = new ProgressDialog(MainActivity.this, R.style.estiloDialog);
        pd.setMessage("Processando...");

        pd.setCancelable(false);


        if (!UtilTCM.verifyConnection(getApplicationContext())) {
            exibirAlerta();

        } else {
            SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
            String temp = shared.getString("usuario", "");
            if (temp.length() > 5) {
                Gson gson = new Gson();
                usuarioLogado = gson.fromJson(temp, Pessoa.class);
                logado = true;
            }

            buscarMural();
        }

        startService(new Intent(this, FirebaseMessagingService.class));
        startService(new Intent(this, FirebaseInstanceIDService.class));




        SharedPreferences shared = getSharedPreferences("info", MODE_PRIVATE);
        String tempD = shared.getString("dispositivo", "");
        if (tempD.length() > 5) {
            Gson gson = new Gson();
            dispositivo = gson.fromJson(tempD, Dispositivo.class);

            try {
                atualizarDispositivo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            dispositivo = new Dispositivo();
            dispositivo.setTokenDispositivo(FirebaseInstanceId.getInstance().getToken());
            dispositivo.setIdEmpresa(usuarioLogado.getIdPessoa());

            try {
                atualizarDispositivo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        buscarCidades();
        // NAVIGATIOn DRAWER
        headerNavigationLeft = new AccountHeader()
                .withActivity(this)
                .withCompactStyle(true)
                .withSavedInstance(savedInstanceState)
                .withHeaderBackground(R.color.red)
                .withProfileImagesVisible(false)
                .addProfiles(
                        new ProfileDrawerItem().withName(usuarioLogado.getNomeFantasiaPessoa()).withEmail(usuarioLogado.getEmailPessoa())

                )
                .build();


        navigationDrawerLeft = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(-1)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(headerNavigationLeft)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (pos == position) {

                        } else {
                            pos = position;
                            switch (position) {
                                case 0:
                                    setCategoriaEmpresa("Pizzaria");
                                    iniciarFragmentoEmpresa();
                                    break;
                                case 1:
                                    setCategoriaEmpresa("Restaurante");
                                    iniciarFragmentoEmpresa();
                                    break;
                                case 2:
                                    setCategoriaEmpresa("Lanchonete");
                                    iniciarFragmentoEmpresa();
                                    break;
                                case 4:
                                    iniciarFragCarrinho();
                                    break;
                                case 6:
                                    abrirFragmentoEnderecos();
                                    break;
                                case 7:
                                    abrirFragmentoUltimosPedidos();
                                    break;
                                case 8:
                                    iniciarFragUsuario();
                                    break;
                                case 9:
                                    SharedPreferences preferences = getSharedPreferences("info", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear().commit();
                                    logado = false;
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                })
                .build();


        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Pizzarias"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Restaurantes"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Lanchonetes"));
        navigationDrawerLeft.addItem(new DividerDrawerItem());
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Carrinho").withIcon(R.drawable.cartoutline));
        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Configurações"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Endereços").withIcon(R.drawable.earth));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Últimos Pedidos").withIcon(R.drawable.viewheadline));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Editar Dados").withIcon(R.drawable.account));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Sair"));
    }


    public void alterarMenu(){
        headerNavigationLeft.getProfiles().get(0).setName(usuarioLogado.getNomeFantasiaPessoa());
        headerNavigationLeft.getProfiles().get(0).setName(usuarioLogado.getEmailPessoa());
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = navigationDrawerLeft.saveInstanceState(outState);
        headerNavigationLeft.saveInstanceState(outState);
        outState.putParcelableArrayList("mural", (ArrayList<? extends Parcelable>) mural);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mural = savedInstanceState.getParcelableArrayList("mural");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Subscribe
    public void onNewToken(TokenEvent tokenEvent){
       dispositivo.setTokenDispositivo(tokenEvent.getToken());
        try {
            updateToken(dispositivo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawerLeft.isDrawerOpen()) {
            navigationDrawerLeft.closeDrawer();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            }
             else {
                HomeFragment frag = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
                if (frag != null && frag.isVisible()) {
                    finish();

                } else {
                    fecharFragmentos();
                }
            }
        }
    }

    @Override
    public void onStop() {
        if (logado) {
            SharedPreferences pref;
            pref = getSharedPreferences("info", MODE_PRIVATE);
            Gson gson = new Gson();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("usuario", gson.toJson(usuarioLogado));
            editor.commit();

            SharedPreferences pref2;
            pref2 = getSharedPreferences("info", MODE_PRIVATE);

            SharedPreferences.Editor editor2 = pref2.edit();
            editor.putString("dispositivo", gson.toJson(dispositivo));
            editor.commit();
        }
        super.onStop();
    }

    public void exibirAlerta(){
        AlertDialog.Builder build = new AlertDialog.Builder(MainActivity.this, R.style.MaterialDrawerBaseTheme_Dialog);

        build.setTitle("Alerta");
        build.setMessage("Sem conexão com o servidor, tente novamente mais tarde");
        build.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        build.create().show();
    }

    public List<Produto> getListaProdutos(int quant) {
        Long idEmpresa = empresaSelecionada.getIdPessoa();
        List<Produto> listAux = new ArrayList<>();


        return (listAux);
    }

    public List<Pessoa> getListaEmpresa(int quant, int total) {

        List<Pessoa> listAux = new ArrayList<>();


        return (listAux);
    }

    public String[] getListaCategorias() {
        String[] categorias = {};
        int i = 0;
        for (Categoria c : empresaSelecionada.getCategorias()) {
            categorias[i] = c.getTituloCategoria();
            i++;
        }
        return categorias;
    }

    public MainActivity() {
        this.usuarioLogado = LoginActivity.getUsuario();
    }

    public String getCategoriaSelecionada() {
        return categoriaSelecionada;
    }

    public void setCategoriaSelecionada(String categoriaSelecionada) {
        this.categoriaSelecionada = categoriaSelecionada;
    }

    public String getCategoriaEmpresa() {
        return categoriaEmpresa;
    }

    public void setCategoriaEmpresa(String categoriaEmpresa) {
        this.categoriaEmpresa = categoriaEmpresa;
    }

    public Pessoa getEmpresaSelecionada() {
        return empresaSelecionada;
    }

    public void setEmpresaSelecionada(Pessoa empresa) {
        empresaSelecionada = empresa;
    }

    public void buscarEmpresa(Long id) {

        exibeProgressBar();

        CustomRequestObject request = new CustomRequestObject(Request.Method.GET,
                "http://" + url + "/service/usuario/" + "empresaPorId?id=" + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Gson gson = new Gson();
                        empresaSelecionada = new Pessoa();
                        Log.e("Antes", "Json");
                        empresaSelecionada = gson.fromJson(response.toString(), Pessoa.class);
                        Log.e("Nome", empresaSelecionada.getNomeFantasiaPessoa());
                        Log.e("Categorias", empresaSelecionada.getCategorias().get(0).getTituloCategoria());
                        iniciarFragProduto();
                        ocultaProgressBar();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                        Log.e("GET", "Falso ao trazer empresa");
                        ocultaProgressBar();


                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }


    }

    public void salvaEnderecoWeb(String json) throws JSONException {
        exibeProgressBar();
        JsonObjectRequest request = new JsonObjectRequest("http://" + url + "/service/usuario/editarEndereco", new JSONObject(json),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Log.e("Resposta", response.toString());
                        Gson gson = new Gson();
                        Endereco endereco = gson.fromJson(response.toString(), Endereco.class);
                        boolean exec = false;
                        for (int i = 0; i < getUsuarioLogado().getEnderecos().size(); i++) {
                            if (endereco.getIdEndereco().equals(getUsuarioLogado().getEnderecos().get(i).getIdEndereco())) {
                                getUsuarioLogado().getEnderecos().remove(i);
                                getUsuarioLogado().getEnderecos().add(endereco);
                                exec = true;
                            }

                        }
                        if (!exec) {
                            getUsuarioLogado().getEnderecos().add(endereco);
                        }
                        fecharFragmentos();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ocultaProgressBar();
                VolleyLog.e("Error: ", error.getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void atualizarUsuario(String s) throws JSONException {
        exibeProgressBar();
        Log.e("Usuario",s);
        JsonObjectRequest request = new JsonObjectRequest("http://" + url + "/service/usuario/editar", new JSONObject(s),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Log.e("Resposta", response.toString());
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd")
                                .create();
                        usuarioLogado = gson.fromJson(response.toString(), Pessoa.class);
                        alterarMenu();
                        fecharFragmentos();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                ocultaProgressBar();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }


    public void enviarVenda() throws JSONException {
        exibeProgressBar();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();
        String s = gson.toJson(vend);
        JsonObjectRequest request = new JsonObjectRequest("http://" + url + "/service/venda/realizarvenda", new JSONObject(s),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();

                        Toast.makeText(getApplicationContext(), "Sucesso", Toast.LENGTH_LONG).show();
                        limparCampos();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ocultaProgressBar();
                VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        request.setTag("request");
        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }


    }

    public void buscarProduto(Long id) {
        exibeProgressBar();
        CustomRequestObject request = new CustomRequestObject(Request.Method.GET,
                "http://" + url + "/service/produto/" + "porid?idProduto=" + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Gson gson = new Gson();
                        prod = new Produto();
                        Log.e("Antes", "Json");
                        prod = gson.fromJson(response.toString(), Produto.class);
                        iniciarFragProdutoNovo();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                        Log.e("GET", "Falso ao trazer produto");
                        ocultaProgressBar();

                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void buscarVenda(Integer id) {
        exibeProgressBar();
        CustomRequestObject request = new CustomRequestObject(Request.Method.GET,
                "http://" + url + "/service/venda/" + "porid?idVenda=" + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Gson gson = new Gson();
                        vendaSelecionada = gson.fromJson(response.toString(), Venda.class);
                        iniciarFragVenda();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                        Log.e("GET", "Falso ao trazer venda");
                        ocultaProgressBar();

                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void buscarIngredientesAdicionais(Long id, final Long idProduto) {

        exibeProgressBar();

        CustomRequest request = new CustomRequest(Request.Method.GET,
                "http://" + url + "/service/produto/" + "ingredientes?idEmpresa=" + id,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ocultaProgressBar();
                        Gson gson = new Gson();
                        listaIngredientesAdicionais = new ArrayList<>();
                        for (int i = 0, tamI = response.length(); i < tamI; i++) {
                            try {
                                Ingrediente ingrediente = new Ingrediente();
                                ingrediente = gson.fromJson(response.getString(i), Ingrediente.class);
                                listaIngredientesAdicionais.add(ingrediente);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        buscarProduto(idProduto);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                        Log.e("GET", "Falso ao trazer produto");
                        ocultaProgressBar();

                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void buscarCidades() {

        exibeProgressBar();

        CustomRequest request = new CustomRequest(Request.Method.GET,
                "http://" + url + "/service/usuario/" + "cidades",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ocultaProgressBar();
                        Gson gson = new Gson();
                        cidades = new ArrayList<>();
                        for (int i = 0, tamI = response.length(); i < tamI; i++) {
                            try {
                                Cidade cidade = new Cidade();
                                cidade = gson.fromJson(response.getString(i), Cidade.class);
                                cidades.add(cidade);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOG", "onErrorResponse(): " + error.getMessage());
                        Log.e("GET", "Falso ao trazer produto");
                        ocultaProgressBar();

                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void buscarMural() {
        exibeProgressBar();

        CustomRequest request = new CustomRequest(Request.Method.GET,
                "http://" + url + "/service/usuario/mural",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ocultaProgressBar();
                        Gson gson = new Gson();
                        mural = new ArrayList<>();
                        for (int i = 0, tamI = response.length(); i < tamI; i++) {
                            try {
                                Configuracao configuracao = new Configuracao();
                                configuracao = gson.fromJson(response.getString(i), Configuracao.class);
                                mural.add(configuracao);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        iniciarFragmentoHome();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Erro",error.toString());
                        ocultaProgressBar();

                    }
                });


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    // FRAGMENT
    private void iniciarFragmentoEmpresa() {
        fecharFragmentos();
        EmpresasFragment frag = (EmpresasFragment) getSupportFragmentManager().findFragmentByTag("empresasFrag");
       /* if (frag == null) {
            frag = new EmpresasFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "empresasFrag").addToBackStack(null);
            ft.commit();
        } else {*/
            frag = new EmpresasFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "empresasFrag").addToBackStack(null);
            ft.commit();
     //   }
    }

    public void iniciarFragProduto() {
        fecharFragmentos();
        ProdutosFragment frag = (ProdutosFragment) getSupportFragmentManager().findFragmentByTag("produtosFrag");
        if (frag == null) {
            frag = new ProdutosFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "produtosFrag").addToBackStack(null);
            ft.commit();
        } else {
            frag = new ProdutosFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "produtosFrag").addToBackStack(null);
            ft.commit();
        }
    }

    public void iniciarFragProdutoNovo() {
        fecharFragmentos();
        ProdutoFragment frag = (ProdutoFragment) getSupportFragmentManager().findFragmentByTag("produtoFrag");
        if (frag == null) {
            frag = new ProdutoFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "produtoFrag").addToBackStack(null);
            ft.commit();
        } else {
            frag = new ProdutoFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "produtoFrag").addToBackStack(null);
            ft.commit();
        }
    }

    public void iniciarFragVenda() {
        fecharFragmentos();
        VendaFragment frag = (VendaFragment) getSupportFragmentManager().findFragmentByTag("vendaFrag");
        if (frag == null) {
            frag = new VendaFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "vendaFrag").addToBackStack(null);
            ft.commit();
        } else {
            frag = new VendaFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "vendaFrag").addToBackStack(null);
            ft.commit();
        }
    }

    private void iniciarFragCarrinho() {

        if (fecharEmpresa) {
            fecharFragmentos();
            fecharEmpresa = false;
        }
        CarrinhoFragment frag = (CarrinhoFragment) getSupportFragmentManager().findFragmentByTag("carrinhoFrag");
        if (frag == null) {
            frag = new CarrinhoFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "carrinhoFrag").addToBackStack(null);
            ft.commit();
        }
    }

    private void iniciarFragUsuario() {
        fecharFragmentos();
        UsuarioFragment frag = (UsuarioFragment) getSupportFragmentManager().findFragmentByTag("usuarioFrag");
        if (frag == null) {
            frag = new UsuarioFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "usuarioFrag").addToBackStack(null);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "usuarioFrag").addToBackStack(null);
            ft.commit();
        }
    }

    public void iniciarFragFinalizarCompra() {

        fecharFragmentos();
        FinalizarCompraFragment frag = (FinalizarCompraFragment) getSupportFragmentManager().findFragmentByTag("finalizarCompra");
        if (frag == null) {
            frag = new FinalizarCompraFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "finalizarCompra").addToBackStack(null);
            ft.commit();
        }
    }

    public void fecharFragmentos() {

        getSupportFragmentManager().getFragments().clear();
        navigationDrawerLeft.setSelection(-1);
        networkConnection.getRequestQueue().cancelAll("request");
        HomeFragment frag = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
        if (frag == null) {
            frag = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "homeFrag");
            ft.commit();
        }
    }

    public void abrirFragmentoEndereco(Endereco end) {
        endereco = end;
        fecharFragmentos();
        EnderecoFragment frag = (EnderecoFragment) getSupportFragmentManager().findFragmentByTag("enderecoFrag");
        if (frag == null) {
            frag = new EnderecoFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "enderecoFrag").addToBackStack(null);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "enderecoFrag").addToBackStack(null);
            ft.commit();
        }
    }

    public void iniciarFragmentoHome() {
        HomeFragment frag = (HomeFragment) getSupportFragmentManager().findFragmentByTag("homeFrag");
        if (frag == null) {
            frag = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "homeFrag");
            ft.commit();
        }
    }

    public void abrirFragmentoEnderecos() {
        fecharFragmentos();
        EnderecosFragment frag = (EnderecosFragment) getSupportFragmentManager().findFragmentByTag("enderecosFrag");
        if (frag == null) {
            frag = new EnderecosFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "enderecosFrag").addToBackStack(null);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "enderecosFrag").addToBackStack(null);
            ft.commit();

        }
    }

    public void abrirFragmentoUltimosPedidos() {
        fecharFragmentos();
        UltimosPedidosFragment frag = (UltimosPedidosFragment) getSupportFragmentManager().findFragmentByTag("ultimosPedidosFrag");
        if (frag == null) {
            frag = new UltimosPedidosFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "ultimosPedidosFrag").addToBackStack(null);
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "ultimosPedidosFrag").addToBackStack(null);
            ft.commit();
        }
    }

    public Pessoa getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Pessoa usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public void setCarrinhoCompras(ArrayList<ItemVenda> carrinhoCompras) {
        this.carrinhoCompras = carrinhoCompras;
    }


    //Venda Bean


    public void adicionarAoCarrinhoModoUsuario(Produto produto, Double quantidade, List<Ingrediente> ingredientes, List<Ingrediente> ingredientesAdicionais, Boolean metade, String tamanho) {
        itemVenda = new ItemVenda();
        this.itemVenda.setProdutoItemVenda(produto);
        this.itemVenda.setMetade(metade);
        this.itemVenda.setVendaItemVenda(vend.getIdVenda());
        this.itemVenda.setIngredientesAdicionais(ingredientesAdicionais);
        this.itemVenda.setIngredientesProduto(ingredientes);
        this.itemVenda.setQuantItemVenda(quantidade);
        this.itemVenda.setTamanho(tamanho);

        if (!this.carrinhoCompras.isEmpty()) {

            if (!produto.getEmpresaProduto().getIdPessoa().equals(carrinhoCompras.get(0).getProdutoItemVenda().getEmpresaProduto().getIdPessoa()) ) {
                Toast.makeText(MainActivity.this, "Escolha por favor, apenas produtos de uma única empresa por pedido", Toast.LENGTH_SHORT).show();


            } else {


                if(itemVenda.getMetade()){
                    itemVenda.setQuantItemVenda(0.5);
                    String aux = "";
                    for(int i = 0; i<itemVenda.getProdutoItemVenda().getTamanhos().size();i++){
                        if(itemVenda.getProdutoItemVenda().getTamanhos().get(i).getValorTamanho().equals(valor))
                            aux = itemVenda.getProdutoItemVenda().getTamanhos().get(i).getTamanho();
                    }
                    itemVenda.setTamanho(aux);
                }
                this.itemVenda.setVlrItemVenda(itemVenda.getQuantItemVenda() * valor);
                if (itemVenda.getIngredientesAdicionais() != null) {
                    for (Ingrediente i : itemVenda.getIngredientesAdicionais())
                        this.itemVenda.setVlrItemVenda(itemVenda.getVlrItemVenda() + (i.getValorAdicional() * itemVenda.getQuantItemVenda()));
                }
                ItemVenda itemremover = null;
                for (ItemVenda v : carrinhoCompras) {

                    if (v.getProdutoItemVenda().getIdProduto().equals(itemVenda.getProdutoItemVenda().getIdProduto())) {
                        if (v.getProdutoItemVenda().getCategoriaProduto().getTituloCategoria().equals("Bebida")) {
                            Double valor = v.getQuantItemVenda();
                            Double valornovo = itemVenda.getQuantItemVenda();
                            Double valorItem = v.getVlrItemVenda();
                            itemVenda.setVlrItemVenda(itemVenda.getVlrItemVenda() + valorItem);
                            itemVenda.setQuantItemVenda(valornovo + valor);
                            itemremover = v;
                        }
                        else{
                            Log.e("Log","Entou no else");
                            int quantA = itemVenda.getIngredientesAdicionais().size();
                            int qA = 0;
                            int q = 0;
                            int quant = itemVenda.getIngredientesProduto().size();
                            if(v.getIngredientesAdicionais().size()== quantA){
                                for(int i = 0; i<v.getIngredientesAdicionais().size();i++){
                                    if(v.getIngredientesAdicionais().get(i).getIdIngrediente().equals(itemVenda.getIngredientesAdicionais().get(i).getIdIngrediente())){
                                        qA++;
                                        Log.e("Log","Ingrediente Adicional");
                                    }
                                }
                            }
                            if(v.getIngredientesProduto().size()== quant){
                                for(int i = 0; i<v.getIngredientesProduto().size();i++){
                                    if(v.getIngredientesProduto().get(i).getIdIngrediente().equals(itemVenda.getIngredientesProduto().get(i).getIdIngrediente())){
                                        q++;
                                        Log.e("Log","Ingrediente Produto");
                                    }
                                }
                            }
                            if (q==quant && qA==quantA) {
                                Log.e("Log","Primeiro if");
                                if (v.getVlrItemVenda().equals(valor)) {
                                    Log.e("Log","processando");
                                    Double valori = v.getQuantItemVenda();
                                    Double valornovo = itemVenda.getQuantItemVenda();
                                    Double valorItem = v.getVlrItemVenda();
                                    itemVenda.setVlrItemVenda(itemVenda.getVlrItemVenda() + valorItem);
                                    itemVenda.setQuantItemVenda(valornovo + valori);
                                    itemremover = v;
                                }
                            }
                        }
                    }
                }

                itemVenda.setVlrUnitarioProduto(valor);
                carrinhoCompras.remove(itemremover);
                valor = null;
                this.carrinhoCompras.add(itemVenda);
                calcularCarrinho();
                this.itemVenda = new ItemVenda();
                Toast.makeText(getApplicationContext(), "Adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
                fecharEmpresa = true;
                navigationDrawerLeft.setSelection(4);

            }
        } else {

            if(itemVenda.getMetade()){
                itemVenda.setQuantItemVenda(0.5);
                String aux = "";
                for(int i = 0; i<itemVenda.getProdutoItemVenda().getTamanhos().size();i++){
                    if(itemVenda.getProdutoItemVenda().getTamanhos().get(i).getValorTamanho().equals(valor))
                        aux = itemVenda.getProdutoItemVenda().getTamanhos().get(i).getTamanho();
                }
                itemVenda.setTamanho(aux);
            }
            this.itemVenda.setVlrItemVenda(itemVenda.getQuantItemVenda() * valor);
            if (itemVenda.getIngredientesAdicionais() != null) {
                for (Ingrediente i : itemVenda.getIngredientesAdicionais())
                    this.itemVenda.setVlrItemVenda(itemVenda.getVlrItemVenda() + (i.getValorAdicional() * itemVenda.getQuantItemVenda()));
            }
           /* ItemVenda itemremover = null;
            for (ItemVenda v : carrinhoCompras) {

                if (v.getProdutoItemVenda().getIdProduto().equals(itemVenda.getProdutoItemVenda().getIdProduto())) {
                    if (v.getProdutoItemVenda().getCategoriaProduto().getTituloCategoria().equals("Bebida")) {
                        Double valor = v.getQuantItemVenda();
                        Double valornovo = itemVenda.getQuantItemVenda();
                        Double valorItem = v.getVlrItemVenda();
                        itemVenda.setVlrItemVenda(itemVenda.getVlrItemVenda() + valorItem);
                        itemVenda.setQuantItemVenda(valornovo + valor);
                        itemremover = v;
                    }
                    else{
                        if (v.getIngredientesAdicionais().equals(itemVenda.getIngredientesAdicionais()) && v.getIngredientesProduto().equals(itemVenda.getIngredientesProduto())) {
                            if (v.getVlrItemVenda().equals(valor)) {
                                Double valor = v.getQuantItemVenda();
                                Double valornovo = itemVenda.getQuantItemVenda();
                                Double valorItem = v.getVlrItemVenda();
                                itemVenda.setVlrItemVenda(itemVenda.getVlrItemVenda() + valorItem);
                                itemVenda.setQuantItemVenda(valornovo + valor);
                                itemremover = v;
                            }
                        }
                    }
                }
            }

            carrinhoCompras.remove(itemremover);*/
            itemVenda.setVlrUnitarioProduto(valor);
            valor = null;
            this.carrinhoCompras.add(itemVenda);
            calcularCarrinho();
            this.itemVenda = new ItemVenda();
            Toast.makeText(getApplicationContext(), "Adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
            fecharEmpresa = true;
            navigationDrawerLeft.setSelection(4);
        }
    }


    public List<Venda> listarUltimasVendas(Long id) {
        //Trazer Vendas Via Volley
        return null;
    }


    public void calcularCarrinho() {
        this.valorTotal = 0.00;
        quantMetadeGrande = 0;
        quantMetadeMedia = 0;
        quantMetadeBroto = 0;
        quantMetadeMini = 0;
        alcoolico = false;
        for (ItemVenda i : carrinhoCompras) {
            this.valorTotal += i.getVlrItemVenda();
            if (i.getProdutoItemVenda().getAlcoolico()) {
                alcoolico = true;
            }
            if(i.getMetade()){
                if(i.getTamanho().equals("Grande")){
                    quantMetadeGrande++;
                }
                if(i.getTamanho().equals("Media")){
                    quantMetadeMedia++;
                }
                if(i.getTamanho().equals("Broto")){
                    quantMetadeBroto++;
                }
                if(i.getTamanho().equals("Mini")){
                    quantMetadeMini++;
                }
            }
        }
    }


    public void limparCampos() {
        itemVenda = new ItemVenda();
        vend = new Venda();
        carrinhoCompras.clear();
        this.valorTotal = 0.00;
        fecharFragmentos();
    }


    public void removerCarrinho(ItemVenda itemVenda) {
        carrinhoCompras.remove(itemVenda);
        if(itemVenda.getMetade()){
            if(itemVenda.getTamanho().equals("Grande")){
                quantMetadeGrande--;
            }
            if(itemVenda.getTamanho().equals("Media")){
                quantMetadeMedia--;
            }
            if(itemVenda.getTamanho().equals("Broto")){
                quantMetadeBroto--;
            }
            if(itemVenda.getTamanho().equals("Mini")){
                quantMetadeMini--;
            }
        }
        calcularCarrinho();
    }
    public Integer getIdade(Date data) {
        Calendar dataNascimento = Calendar.getInstance();
        dataNascimento.setTime(data);
        Calendar dataAtual = Calendar.getInstance();
        Integer diferencaMes = dataAtual.get(Calendar.MONTH) - dataNascimento.get(Calendar.MONTH);
        Integer diferencaDia = dataAtual.get(Calendar.DAY_OF_MONTH) - dataNascimento.get(Calendar.DAY_OF_MONTH);
        Integer idade = (dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR));
        if (diferencaMes < 0 || (diferencaMes == 0 && diferencaDia < 0)) {
            idade--;
        }
        System.out.println(idade);
        return idade;
    }

    public void finalizarVenda(String formaPagamento, Double troco) {
        calcularCarrinho();
        if (endereco == null) {
            vend.setRetirada(Boolean.TRUE);
        } else {
            vend.setRetirada(Boolean.FALSE);
            vend.setEndereco(endereco);
            if (carrinhoCompras.get(0).getProdutoItemVenda().getEmpresaProduto().getValorEntrega() != null) {
                this.valorTotal += carrinhoCompras.get(0).getProdutoItemVenda().getEmpresaProduto().getValorEntrega();
            }
        }
        vend.setFormPagamento(formaPagamento);
        vend.setTrocoParaVenda(troco);
        if (getIdade(usuarioLogado.getDataNascPessoa()) < 18 && alcoolico) {
            Toast.makeText(getApplication(), "Menores de idade não podem efetuar compras de produtos alcoólicos", Toast.LENGTH_SHORT).show();
        }
        else {
            if(quantMetadeGrande%2!=0 || quantMetadeMedia%2!=0 || quantMetadeBroto%2!=0 || quantMetadeMini%2!=0){
                Toast.makeText(getApplicationContext(),"Ao adicionar metade de uma pizza é necessário adicionar a outra metade", Toast.LENGTH_SHORT).show();
            }
            else {
                vend.setDispositivo("Smartphone");
                vend.setCliente(usuarioLogado);
                vend.setVlrTotalVenda(valorTotal);
                vend.setCarrinho(carrinhoCompras);
                vend.setStatusVenda("Ativo");
                vend.setEmpresa(carrinhoCompras.get(0).getProdutoItemVenda().getEmpresaProduto());
                Date data = new Date();
                vend.setDataVenda(data);
            }
        }

    }


    public ItemVenda getItemVenda() {
        return itemVenda;
    }

    public void setItemVenda(ItemVenda itemVenda) {
        this.itemVenda = itemVenda;
    }

    public Venda getVend() {
        return vend;
    }

    public void setVend(Venda vend) {
        this.vend = vend;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public ArrayList<ItemVenda> getCarrinhoCompras() {
        return carrinhoCompras;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Produto getProd() {
        return prod;
    }

    public void setProd(Produto prod) {
        this.prod = prod;
    }

    public List<Venda> getVendas() {
        //trazer vendas
        return null;
    }

    public List<Ingrediente> getListaIngredientesAdicionais() {
        return listaIngredientesAdicionais;
    }

    public void setListaIngredientesAdicionais(List<Ingrediente> listaIngredientesAdicionais) {
        this.listaIngredientesAdicionais = listaIngredientesAdicionais;
    }

    public Venda getVendaSelecionada() {
        return vendaSelecionada;
    }

    public void setVendaSelecionada(Venda vendaSelecionada) {
        this.vendaSelecionada = vendaSelecionada;
    }

    public List<Configuracao> getMural() {
        return mural;
    }

    public void setMural(List<Configuracao> mural) {
        this.mural = mural;
    }

    public void exibeProgressBar(){
        pd.show();
        //progressBar.setVisibility(View.VISIBLE);
    }
    public void ocultaProgressBar(){
        pd.dismiss();
        //progressBar.setVisibility(View.GONE);
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }


    public void updateToken(Dispositivo d) throws JSONException {
        Gson gson = new Gson();
        d.setIdEmpresa(usuarioLogado.getIdPessoa());
        String s = gson.toJson(d);
        JsonObjectRequest request = new JsonObjectRequest("http://" + url + "/service/usuario/token", new JSONObject(s),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Log.e("Resposta", response.toString());
                        Gson gson = new Gson();
                        dispositivo = gson.fromJson(response.toString(), Dispositivo.class);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                ocultaProgressBar();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

    public void atualizarDispositivo() throws JSONException {
        exibeProgressBar();
        Gson gson = new Gson();
        String s = gson.toJson(dispositivo);
        JsonObjectRequest request = new JsonObjectRequest("http://" + url + "/service/usuario/token", new JSONObject(s),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ocultaProgressBar();
                        Log.e("Resposta", response.toString());
                        Gson gson = new Gson();
                        dispositivo = gson.fromJson(response.toString(), Dispositivo.class);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                ocultaProgressBar();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
        };


        request.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag("request");

        if (UtilTCM.verifyConnection(getApplicationContext())) {
            networkConnection.addRequestQueue(request);
        } else {
            exibirAlerta();
        }
    }

}
