package br.com.sidney.petshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author Sidney Toshidi OKu
 */
public class CadastroLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private Toolbar toolbar;
    private EditText edNome;
    private EditText edEmail;
    private EditText edSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_login);

        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar_cadastrologin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edNome = (EditText) findViewById(R.id.cadastrologin_activity_txt_nome);
        edEmail = (EditText) findViewById(R.id.cadastrologin_activity_txt_email);
        edSenha = (EditText) findViewById(R.id.cadastrologin_activity_txt_senha);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            return true;
        }

        return false;
    }

    public void cadastrar(View view) {
        if(edSenha.getText().toString().length() > 5) {
            mAuth.createUserWithEmailAndPassword(edEmail.getText().toString(), edSenha.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(CadastroLogin.this, "Conta criada com sucesso!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CadastroLogin.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(CadastroLogin.this, "Deu erro!!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "A senha precisa ter 6 caracteres ou mais!!", Toast.LENGTH_SHORT).show();
        }
    }

//    private boolean validar() {
//        boolean valid = true;
//
//        String nome = edNome.getText().toString();
//        String email = edEmail.getText().toString();
//        String senha = edSenha.getText().toString();
//
//        if (nome.isEmpty()) {
//            edNome.setError("Informe o nome");
//            valid = false;
//        } else if (email.isEmpty()) {
//
//        }
//    }
}