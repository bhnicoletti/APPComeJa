package br.com.nicoletti.comeja;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.robototextview.widget.RobotoTextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import br.com.nicoletti.comeja.model.Pessoa;


public class InformacoesEmpresa extends ActionBarActivity {
    private Toolbar mToolbar;
    private RobotoTextView telefoneEmpresa, tempoPreparo, valorEntrega, endereco, horarioFuncionamento, observacoes;
    private Pessoa empresa;
    private SimpleDraweeView imagem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(getApplicationContext());
        setContentView(R.layout.informacoes_empresa);

        empresa = MainActivity.empresaSelecionada;

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle(empresa.getNomeFantasiaPessoa());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagem = (SimpleDraweeView) findViewById(R.id.img);
        telefoneEmpresa = (RobotoTextView) findViewById(R.id.tv_telefone);
        tempoPreparo = (RobotoTextView) findViewById(R.id.tv_tempoPreparo);
        valorEntrega = (RobotoTextView) findViewById(R.id.tv_valorEntrega);
        endereco = (RobotoTextView) findViewById(R.id.tv_endereco);
        horarioFuncionamento = (RobotoTextView) findViewById(R.id.tv_horarioFuncionamento);
        observacoes = (RobotoTextView) findViewById(R.id.tv_observacoes);

        valorEntrega.setText("Valor Entrega: R$ " + empresa.getValorEntrega()+"0");
        tempoPreparo.setText("Tempo Preparo: " + empresa.getTempoPreparo());
        telefoneEmpresa.setText("Telefones: " + empresa.getCelularPessoa()
                + " | " + empresa.getTelefonePessoa());
        horarioFuncionamento.setText("Horário de Funcionamento: " + empresa.getHorarioFuncionamento());
        observacoes.setText("Observações: "+empresa.getObs());
        String enderecos = "Endereço: \n";
        for (int i = 0; i < empresa.getEnderecos().size(); i++) {
            enderecos += empresa.getEnderecos().get(i).getRuaEndereco()+", nº "+empresa.getEnderecos().get(i).getNumeroEndereco()+" - "
                    +empresa.getEnderecos().get(i).getBairroEndereco()+", "+empresa.getEnderecos().get(i).getCidadeEndereco()+" - "+empresa.getEnderecos().get(i).getEstadoEndereco()+"\n";

        }
        endereco.setText(enderecos);

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

        Uri uri = Uri.parse("http://www.comeja.com.br/imagens/" + empresa.getImagem());


        DraweeController dc = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setTapToRetryEnabled(true)
                .setControllerListener(listener)
                .setOldController(imagem.getController())
                .build();
        imagem.setController(dc);


    }

    @Override
    protected void onStop() {
        super.onStop();
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        return true;
    }
}
