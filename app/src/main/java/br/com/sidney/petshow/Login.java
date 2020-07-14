package br.com.sidney.petshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * @author Sidney Toshidi Oku
 */
public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText edEmail;
    private EditText edSenha;
    private Button btnLogin;
    private TextView txtCadastrarConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edEmail = (EditText) findViewById(R.id.login_activity_email);
        edSenha = (EditText) findViewById(R.id.login_activity_senha);
        btnLogin = (Button) findViewById(R.id.btn_login);
        txtCadastrarConta = (TextView) findViewById(R.id.txt_cadastrarconta);
    }

    public void logar(View view) {
        mAuth.signInWithEmailAndPassword(edEmail.getText().toString(), edSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            entrarApp(user);
                        } else {
                            Log.i("Login", task.getResult().toString());
                            Toast.makeText(Login.this, "Deu erro. Tente novamente!!",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void entrarApp(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void cadastrar(View view) {
        Intent intent = new Intent(this, CadastroLogin.class);
        startActivity(intent);
    }

    public void recuperarSenha(View view) {
        Intent intent = new Intent(this, RecuperarSenha.class);
        startActivity(intent);
    }
}