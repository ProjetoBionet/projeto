package projeto.bionet.example.com.bionet;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Resultado_Pesquisa extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado__pesquisa);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btResultadoVoltar) {
            Intent i = new Intent(this, PesquisaMaterial.class);
            startActivity(i);
        }
    }
}
