package projeto.bionet.example.com.bionet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static projeto.bionet.example.com.bionet.R.id.parent;

public class PesquisaMaterial extends AppCompatActivity {

    SearchView searchView;
    ListView lista;
    FirebaseFirestore db;
    private ProgressDialog mProgressDialog;
    ArrayList<Coleta> coletaArrayRef;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_material);

        searchView = (SearchView) findViewById(R.id.searchView);
        lista = (ListView) findViewById(R.id.listaResultados);

        searchView.setQueryHint("Digite o material.");
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                lista.setAdapter(null);
                pesquisar(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });


    }


    public void pesquisar(final String query){

        final ArrayList<Coleta> coletaArray = new ArrayList<>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Consultando dados...");
        mProgressDialog.show();

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("Coleta")
                .whereGreaterThan("proprietario", user.getUid())
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

                        }
                    }
                });


        db.collection("Coleta")
                .whereLessThan("proprietario", user.getUid())
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
                            filtarLista(coletaArray,query);
                            mProgressDialog.dismiss();
                        }
                    }
                });
    }

    private void gerarLista(ArrayList array) {


        if (array.size() == 0) {
            Toast.makeText(PesquisaMaterial.this, "Sua pesquisa retornou 0 resultados!",
                    Toast.LENGTH_LONG).show();
        } else {

            coletaArrayRef = new ArrayList<>();
            coletaArrayRef = (ArrayList<Coleta>) array.clone();
            final AdapterListaResultado adapter = new AdapterListaResultado(array, this);
            lista.setAdapter(adapter);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int posicao, long arg3)
                {
                    Coleta item = (Coleta) adapter.getItem(posicao);
                    Intent intent = new Intent(PesquisaMaterial.this, infoMaterial.class);
                    intent.putExtra("item", item);
                    startActivity(intent);

                    // Get data from your adapter,   the above code of line give the custom adapter's object of   current position of selected list item
                }
            });

        }



    }

    public void filtarLista(ArrayList<Coleta> array, String query){

        ArrayList<Coleta> filtro = new ArrayList<>();

        for (Coleta a : array){
            if (a.getStatus() != null) {
                if (a.getMaterial().toLowerCase().contains(query.toLowerCase()) && a.getStatus().equalsIgnoreCase("Ativo")) {
                    filtro.add(a);
                }
            }
        }

        gerarLista(filtro);
    }




}
