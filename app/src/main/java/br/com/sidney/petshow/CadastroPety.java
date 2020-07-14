package br.com.sidney.petshow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import br.com.sidney.petshow.entity.Pet;

/**
 * @author Sidney Toshidi Oku
 */
public class CadastroPety extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 105;
    private String currentPhotoPath;

    private Toolbar toolbar;
    private EditText edNome;
    private EditText edTipo;
    private EditText edPorte;
    private ImageView img1;

    private DatabaseReference databaseReference;
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pety);

        toolbar = (Toolbar) findViewById(R.id.toolbar_cadastropet_fragment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edNome = (EditText) findViewById(R.id.ed_cadastropet_nome);
        edTipo = (EditText) findViewById(R.id.ed_cadastropet_tipo);
        edPorte = (EditText) findViewById(R.id.ed_cadastropet_porte);
        img1 = (ImageView) findViewById(R.id.imageView1);

        pet = (Pet) getIntent().getSerializableExtra("cadastro");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (pet != null) {
            carregarDados();
        }
    }

    private void carregarDados() {
        edNome.setText(pet.getNome());
        edTipo.setText(pet.getTipo());
        edPorte.setText(pet.getPorte());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastrogeral_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, CadastroGeralActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.menu_cadastrogeral_form_salvar) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            pet.setNome(edNome.getText().toString());
            pet.setTipo(edTipo.getText().toString());
            pet.setPorte(edPorte.getText().toString());

            if (pet.getId() != null) {
                databaseReference.child(user.getUid()).child("memoria").child(pet.getId()).setValue(pet);
            } else {
                String chave = databaseReference.child(user.getUid()).child("memoria").push().getKey();
                pet.setId(chave);
                databaseReference.child(user.getUid()).child("memoria").child(chave).setValue(pet);
            }

            Toast.makeText(this, "A sua memória foi armazenada com sucesso!!", Toast.LENGTH_LONG).show();
            finish();

            Intent i = new Intent(this, CadastroGeralActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            setResult(RESULT_OK);
            startActivity(i);
            return true;
        } else if (item.getItemId() == R.id.menu_cadastrogeral_form_cancelar) {
            Intent i = new Intent(this, CadastroGeralActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            setResult(RESULT_CANCELED);
            startActivity(i);
            return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultado = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edNome.setText(resultado.get(0));
                    pet.setNome(edNome.getText().toString());
                }
                break;
            }
            case 101 : {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultado = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edTipo.setText(resultado.get(0));
                    pet.setTipo(edTipo.getText().toString());
                }
                break;
            }
            case 102 : {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> resultado = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edPorte.setText(resultado.get(0));
                    pet.setPorte(edPorte.getText().toString());
                }
                break;
            }
            case 105: {
                if (resultCode == RESULT_OK) {
                    try {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 3;
                        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, options);

                        //Converter o bitmap em Base64 (string), que é útil para mandar a foto para um WS.
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                        byte[] binario = outputStream.toByteArray();
                        String fotoString = Base64.encodeToString(binario, Base64.DEFAULT);

                        Log.i("CadastroPet",""+fotoString.length());
                        img1.setImageBitmap(bitmap);
                        img1.setBackground(null);
                        pet.setImagem1(fotoString);
                    } catch (Exception i) {
                        Log.e("CadastroPet","Deu erro!!!");
                    }
                }
                break;
            }
        }
    }

    public void vozTextoNome(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Qual o nome?");
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            Log.e("CadastroPety", "Activity não encontrada");
        }
    }

    public void vozTextoTipo(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Qual o tipo?");
        try {
            startActivityForResult(intent, 101);
        } catch (ActivityNotFoundException a) {
            Log.e("CadastroPet", "Activity não encontrada");
        }
    }

    public void vozTextoPorte(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Qual o porte?");
        try {
            startActivityForResult(intent, 102);
        } catch (ActivityNotFoundException a) {
            Log.e("CadastroPet", "Activity não encontrada");
        }
    }

    public void chamarCamera1(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "br.com.sidney.petshow",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}