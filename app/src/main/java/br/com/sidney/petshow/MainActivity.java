package br.com.sidney.petshow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.sidney.petshow.utils.UIUtils;

/**
 * @author Sidney Toshidi Oku
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String INTENT_MSG_INICIO = null;
    private static String INTENT_SUPORTE = null;

    private FirebaseAuth mAuth;

    private TextView txtNome;
    private TextView txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_app_bar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_main_activity);
        navigationView.setNavigationItemSelectedListener(this);

        View viewHeader = navigationView.getHeaderView(0);
        txtNome = (TextView) viewHeader.findViewById(R.id.txt_main_nav_header_nome);
        txtEmail = (TextView) viewHeader.findViewById(R.id.txt_main_nav_header_email);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        txtNome.setText(user.getDisplayName());
        txtEmail.setText(user.getEmail());

        INTENT_MSG_INICIO = getIntent().getStringExtra("inicio");

        if (INTENT_MSG_INICIO != null) {
            selectView(R.id.nav_principal);
        } else {
            selectView(R.id.nav_principal);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main_activity);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return  true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        selectView(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main_activity);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.submenu_item_main_suporte) {
            final CharSequence[] opcaoPedido = {"E-mail", "Mensagem (SMS)"};

            DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    if (UIUtils.getEmail() == true) {


                    } else if (UIUtils.getSms() == true) {
                        Intent intent = new Intent(MainActivity.this, SuporteSMSActivity.class);

                        if (INTENT_SUPORTE.equals("0")) {
                            intent.putExtra("pagina", "0");
                        }

                        startActivity(intent);

                    } else if (UIUtils.getEmail() == false && UIUtils.getSms() == false) {
                        Toast.makeText(MainActivity.this, R.string.msg_valida_escolha_opcao, Toast.LENGTH_SHORT).show();
                    }

                }
            };

            DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            AlertDialog dialog = UIUtils.createDialogSuporte(this, R.string.dlg_title_suporte,
                    R.string.txt_sim, positiveListener, R.string.txt_nao, negativeListener, opcaoPedido);
            dialog.show();
            return true;

        } else if (id == R.id.submenu_item_main_versao) {
            AlertDialog dialog = UIUtils.createDialog(this, R.string.sub_title_action_versao, R.string.txt_versao);
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void selectView(int id) {
        FragmentManager manager = getSupportFragmentManager();
        Intent intent;

        switch (id) {
            case R.id.nav_principal:
                INTENT_SUPORTE = "0";
                InicioFragment inicioFragment = new InicioFragment();
                manager.beginTransaction().replace(R.id.relativelayout_for_fragment, inicioFragment, inicioFragment.getTag()).commit();
                getSupportActionBar().setTitle(R.string.nav_item_inicio);
                break;
            case R.id.nav_sair:
                DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mAuth.signOut();
                        finish();
                    }
                };

                DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                AlertDialog dialog = UIUtils.createDialog(this, R.string.dlg_title_sair, R.string.msg_sair,
                        R.string.txt_sim, positiveListener, R.string.txt_nao, negativeListener);
                dialog.show();
                break;
        }

    }
}