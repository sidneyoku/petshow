package br.com.sidney.petshow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.sidney.petshow.adapter.CadastroPetAdapter;
import br.com.sidney.petshow.entity.Pet;
import br.com.sidney.petshow.utils.UIUtils;

/**
 * @author Sidney Toshidi Oku
 */
public class CadastroGeralActivity extends AppCompatActivity implements ActionMode.Callback, AbsListView.MultiChoiceModeListener {
    private static final int DADOS_REQUEST_CODE = 100;

    private DatabaseReference databaseReference;

    private boolean trocaActionModeActive;
    private Menu mMenu;

    private ListView listView;
    private Toolbar mToolbar;
    private ActionBar actionBar;
    private CadastroPetAdapter adapter;
    private List<Pet> listPet = new ArrayList<>();
    private List<Pet> listSelectedPet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_geral);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_cadastrogeral_activity);
        setSupportActionBar(mToolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        listView = (ListView) findViewById(R.id.list_cadastro_geral_activity);
        adapter = new CadastroPetAdapter(this);
        listView.setAdapter(adapter);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);
        listView.setSelector(R.drawable.my_selector);

        carregarDados();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DADOS_REQUEST_CODE && resultCode == RESULT_OK) {
            carregarDados();
            Toast.makeText(CadastroGeralActivity.this, R.string.msg_gravacao_sucesso, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_global_pesquisar_e_salvar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_item_global_ps_pesquisar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (listPet != null || listPet.size() > 0) {
                    adapter.clear();
                    for (Pet pet : listPet) {
                        if ((pet.getNome() != null) && (pet.getNome().equals(query)))
                            adapter.add(pet);
                    }
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (listPet != null || listPet.size() > 0) {
//                    adapter.clear();
//                    for (Pet pet : listPet) {
//                        if ((pet.getNome() != null) && (pet.getNome().equals(newText)))
//                            adapter.add(pet);
//                    }
//                }

                return true;
            }
        };

        searchView.setOnQueryTextListener(textChangeListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;

        } else if (item.getItemId() == R.id.menu_item_global_ps_novo) {
            Intent i = new Intent(this, CadastroPety.class);
            i.putExtra("cadastro", new Pet());
            startActivityForResult(i, DADOS_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        Pet pet = adapter.getItem(position);

        if (checked) {
            listSelectedPet.add(pet);
        } else {
            listSelectedPet.remove(pet);
        }

        if (listSelectedPet.size() > 1) {
            trocaActionModeActive = true;
        } else if (listSelectedPet.size() == 1) {
            trocaActionModeActive = false;
        }

        if (trocaActionModeActive == true) {
            mode.getMenu().clear();
            mode.getMenuInflater().inflate(R.menu.action_global_delete, mMenu);

        } else if (trocaActionModeActive == false) {
            mode.getMenu().clear();
            mode.getMenuInflater().inflate(R.menu.action_global_edit, mMenu);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        actionBar.hide();
        trocaActionModeActive = false;
        mode.getMenuInflater().inflate(R.menu.action_global_edit, menu);
        mMenu = mode.getMenu();
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

    @Override
    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.act_global_edit) {
            Pet pet = null;

            for (Pet p : listSelectedPet) {
                pet = p;
            }

            Intent i = new Intent(getApplicationContext(), CadastroPety.class);
            i.putExtra("cadastro", pet);
            startActivityForResult(i, DADOS_REQUEST_CODE);

            return true;

        } else if (id == R.id.act_global_delete) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    for (Pet p : listSelectedPet) {
                        databaseReference.child(user.getUid()).child("memoria").child(p.getId()).removeValue();
                    }

                    carregarDados();

                    int count = listView.getChildCount();

                    for (int i = 0; i < count; i++) {
                        View view = listView.getChildAt(i);
                        view.setBackgroundColor(Color.TRANSPARENT);
                    }

                    mode.finish();
                }
            };

            DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            AlertDialog dialog = UIUtils.createDialog(this,
                    R.string.txt_excluir, R.string.msg_excluir,
                    R.string.txt_sim, positiveListener, R.string.txt_nao,
                    negativeListener);
            dialog.show();

            return true;
        }

        int count = listView.getChildCount();

        for (int i = 0; i < count; i++) {
            View view = listView.getChildAt(i);
            view.setBackgroundColor(Color.TRANSPARENT);
        }

        mode.finish();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        trocaActionModeActive = false;
        mMenu = null;
        mode.getMenu().clear();
        listSelectedPet = new ArrayList<>();
        actionBar.show();
    }

    private void carregarDados() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(user.getUid()).child("memoria").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPet = new ArrayList<>();
                adapter.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Pet pet = postSnapshot.getValue(Pet.class);
                    listPet.add(pet);
                    adapter.add(pet);
                }
//                for (int i = 0; i < listPet.size(); i++) {
//                    Log.e("Name", listPet.get(i).getNome());
//                    Log.e("Tipo", listPet.get(i).getTipo());
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pesquisar(String str) {

    }
}