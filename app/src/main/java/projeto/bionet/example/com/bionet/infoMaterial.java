package projeto.bionet.example.com.bionet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class infoMaterial extends AppCompatActivity {

    private Coleta coletaAlt;
    private EditText etQuantidade, etValor, etTelefone, etCep, etRua, etNum, etComplemento, etBairro, etCidade, etEstado;
    private Spinner spMaterial, spMedida, spModalidade, spEntrega;
    private CheckBox cbDinheiro, cbCredito, cbDebito, cbMercadoPago;
    private String material, medida, modalidade, quantidade, entrega, telefone, valor, cep, rua, num, complemento, bairro, cidade, estado, teste;
    private Boolean dinheiro, debito, credito, mercadoPago;
    private Intent intent;
    private StorageReference storageRef, imageRef;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_material);


        intent = getIntent();
        coletaAlt = (Coleta) intent.getSerializableExtra("item");
        getSupportActionBar().setTitle("Informação Material");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imageRef = storageRef.child("coleta/"+coletaAlt.getId());

        ImageView imagem = (ImageView) findViewById(R.id.imagemColeta);

   /*     Glide.with(view.getContext())
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .into(imagem);
*/

        cbDinheiro = (CheckBox) findViewById(R.id.checkboxDinheiro);
        cbCredito = (CheckBox) findViewById(R.id.checkboxCredito);
        cbDebito = (CheckBox) findViewById(R.id.checkboxDebito);
        cbMercadoPago = (CheckBox) findViewById(R.id.checkboxMercadoPago);


        if(coletaAlt.getModalidade().equalsIgnoreCase("Venda")){
           // etValor.setText(coletaAlt.getValor().toString());

            if (coletaAlt.getDinheiro() == true){
                cbDinheiro.setChecked(true);
            }
            if (coletaAlt.getDebito() == true){
                cbDebito.setChecked(true);
            }
            if (coletaAlt.getCredito() == true){
                cbCredito.setChecked(true);
            }
            if (coletaAlt.getMercadopago() == true){
                cbMercadoPago.setChecked(true);
            }

        }

    }
}
