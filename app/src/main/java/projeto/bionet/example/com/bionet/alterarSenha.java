package projeto.bionet.example.com.bionet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class alterarSenha extends AppCompatActivity {
    EditText e1;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);
        e1 = (EditText)findViewById(R.id.password);
        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
    }

    public void change(View v){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            dialog.setMessage("mudar senha");
            dialog.show();
            user.updatePassword(e1.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "sua senha foi alterada",Toast.LENGTH_LONG);

                    }else{
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"senha não pode ser alterada",Toast.LENGTH_LONG);
                    }
                }
            });

        }

    }
}
