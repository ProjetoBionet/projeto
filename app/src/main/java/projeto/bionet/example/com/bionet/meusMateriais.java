package projeto.bionet.example.com.bionet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class meusMateriais extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;
    DocumentReference coletaRef;
    ArrayList<Coleta> coletaArray;
    String status;
    private ProgressDialog mProgressDialog;
    private StorageReference storageRef;
    RadioGroup RGrupo;
    RadioButton rButton;
    Intent getIntent;
    int listIndex = -1;
    private RadioButton listRadioButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_materiais);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        status = "Ativo";
        mProgressDialog = new ProgressDialog(this);
        RGrupo = (RadioGroup) findViewById(R.id.radiogroup);
        getIntent = getIntent();

        if (getIntent.getStringExtra("status").equalsIgnoreCase("Ativos")) {
            status = "Ativo";
            rButton = (RadioButton) findViewById(R.id.rbAtivos);
            rButton.setChecked(true);
        }else if (getIntent.getStringExtra("status").equalsIgnoreCase("Inativos")){
            status = "Inativo";
            rButton = (RadioButton) findViewById(R.id.rbInativos);
            rButton.setChecked(true);
        }else if (getIntent.getStringExtra("status").equalsIgnoreCase("Finalizados")) {
            status = "Finalizado";
            rButton = (RadioButton) findViewById(R.id.rbFinalizados);
            rButton.setChecked(true);
        }

        RGrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rbAtivos) {
                    Intent intent = new Intent(meusMateriais.this, meusMateriais.class);
                    intent.putExtra("status", "Ativos");
                    finish();
                    startActivity(intent);


                }else if (checkedId == R.id.rbInativos){
                    Intent intent = new Intent(meusMateriais.this, meusMateriais.class);
                    intent.putExtra("status", "Inativos");
                    finish();
                    startActivity(intent);


                }else if (checkedId == R.id.rbFinalizados){
                    Intent intent = new Intent(meusMateriais.this, meusMateriais.class);
                    intent.putExtra("status", "Finalizados");
                    finish();
                    startActivity(intent);


                }

            }
        });


    }

    @Override
    protected void onStart()
    {
        super.onStart();

        final ArrayList<Coleta> coletaArray = new ArrayList<>();
        mProgressDialog.setMessage("Consultando dados...");
        mProgressDialog.show();

        db.collection("Coleta")
                .whereEqualTo("proprietario", user.getUid()).whereEqualTo("status", status)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = new HashMap<>();
                                doc = document.getData();

                                Gson gson = new Gson();
                                JsonElement jsonElement = gson.toJsonTree(doc);
                                Coleta coleta = gson.fromJson(jsonElement, Coleta.class);
                                coletaArray.add(coleta);
                            }
                            gerarLista(coletaArray);
                            mProgressDialog.dismiss();
                        } else {

                        }
                    }
                });
    }

    private void gerarLista(ArrayList array) {

        if (array.size() == 0) {
            View view = findViewById(R.id.activity_principal);
            Snackbar.make(view, "Você não tem nenhum material "+status+" cadastrado!", Snackbar.LENGTH_INDEFINITE).show();
        } else {

        ListView lista = (ListView) findViewById(R.id.listaColetas);
        AdapterLista adapter = new AdapterLista(array, this);
        lista.setAdapter(adapter);
    }

    }

    public void onClickRadioButton(View v) {
        View vMain = ((View) v.getParent());
        // getParent() must be added 'n' times,
        // where 'n' is the number of RadioButtons' nested parents
        // in your case is one.

        // uncheck previous checked button.
        if (listRadioButton != null) listRadioButton.setChecked(false);
        // assign to the variable the new one
        listRadioButton = (RadioButton) v;
        // find if the new one is checked or not, and set "listIndex"
        if (listRadioButton.isChecked()) {
            listIndex = ((ViewGroup) vMain.getParent()).indexOfChild(vMain);
        } else {
            listRadioButton = null;
            listIndex = -1;
        }
    }

}