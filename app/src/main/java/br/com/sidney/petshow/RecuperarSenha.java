package br.com.sidney.petshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenha extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private Toolbar toolbar;
    private EditText edRecuperarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);
        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar_recuperarsenha);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edRecuperarSenha = (EditText) findViewById(R.id.input_emaill);
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

    public void recuperar(View view) {
        mAuth.sendPasswordResetEmail(edRecuperarSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RecuperarSenha.this, R.string.txt_email_enviado, Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(RecuperarSenha.this, R.string.txt_erro_email, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}