package projeto.bionet.example.com.bionet;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PesquisaMaterial extends ActionBarActivity implements View.OnClickListener {

    Spinner materiais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_material);

        materiais = (Spinner) findViewById(R.id.spMaterial);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Material, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        materiais.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btPesquisar){
            Intent i = new Intent(this, Resultado_Pesquisa.class);
            startActivity(i);
        }else if(v.getId() == R.id.btVoltar)
        {
            Intent i = new Intent(this, LobbyActivity.class);
            startActivityForResult(i, 1);
        }
    }
}
