package com.meuorcamento.app.mainmenu;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.meuorcamento.FirebaseUtil;
import com.meuorcamento.app.BaseActivity;
import com.meuorcamento.R;
import com.meuorcamento.app.meusorcamentos.MeusOrcamentosAprovadosPrestadorFragment;
import com.meuorcamento.app.meusorcamentos.MeusOrcamentosFragment;
import com.meuorcamento.app.login.LoginActivity;
import com.meuorcamento.app.meusorcamentos.MeusOrcamentosPrestadorFragment;
import com.meuorcamento.app.perfil.UsuarioPerfilVisualizacaoFragment;
import com.meuorcamento.model.UsuarioPerfil;
import com.meuorcamento.utils.AndroidHelper;
import com.meuorcamento.utils.progressHelper.ITransacao;
import com.meuorcamento.utils.progressHelper.TransacaoTask;

import java.util.ArrayList;
import java.util.List;

public class MenuSliderActivity extends BaseActivity {

    private static MenuSliderActivity instance;

    private static final String KEY = "MenuSliderActivity";

    protected DrawerLayout drawerLayout;
    protected FrameLayout frameLayout;
    protected ListView listViewMenu;
    protected ActionBar actionBar;
    protected TextView tvNomeUsuario;
    protected TextView tvEmail;


    private FragmentManager fragmentManager;

    private List<MenuItem> menuItemList = new ArrayList<>();


    public static void startActivity(Context context) {
        Intent openMainActivity = new Intent(context, MenuSliderActivity.class);
        //openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openMainActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!FirebaseUtil.userAuthenticated()) {
            Toast.makeText(this, "Você não está autenticado, faça login para prosseguir", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public static MenuSliderActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        setContentView(R.layout.activity_main_menu_slider);

        drawerLayout = findViewById(R.id.drawerLayout);

        frameLayout = findViewById(R.id.drawer_drawer);
        listViewMenu = findViewById(R.id.listViewMenu);
        tvNomeUsuario = findViewById(R.id.tvNomeUsuario);
        tvEmail = findViewById(R.id.tvEmail);


        actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        fragmentManager = getFragmentManager();
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(fragmentManager.getBackStackEntryCount() > 0) {

                }
            }
        });




        loadUsuarioPerfil();

    }

    private UsuarioPerfil usuarioPerfil;
    private void loadUsuarioPerfil() {
        openProgressDialog();
        FirebaseUtil.getUsuariosPerfisReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarioPerfil = dataSnapshot.getValue(UsuarioPerfil.class);
                closeProgressDialog();
                updateView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MenuSliderActivity.this, "Falha para carregar o perfil do usuário", Toast.LENGTH_SHORT).show();
                closeProgressDialog();
            }
        });
    }
    private void updateView() {
        if(usuarioPerfil == null)
            return;

        tvNomeUsuario.setText("Olá " + usuarioPerfil.getNome());
        tvEmail.setText(usuarioPerfil.getEmail());

        menuItemList.add(new MenuItem(new MeusOrcamentosFragment(), "Meus orçamentos", "", R.drawable.ic_art_track_black_24dp, EnumView.VIEW_MEUS_ORCAMENTOS));
        menuItemList.add(new MenuItem(new UsuarioPerfilVisualizacaoFragment(), "Perfil", "", R.drawable.ic_assignment_ind_black_24dp, EnumView.VIEW_VISUALIZAR_PERFIL));

        if(usuarioPerfil.getTipoUsuario() == 1) {
            menuItemList.add(new MenuItem(new MeusOrcamentosPrestadorFragment(), "Orçamentos solicitados", "", R.drawable.ic_chrome_reader_mode_black_48dp, EnumView.VIEW_ORCAMENTOS_SOLICITADOS));
            menuItemList.add(new MenuItem(new MeusOrcamentosAprovadosPrestadorFragment(), "Orçamentos aprovados", "", R.drawable.ic_shopping_cart_orc_black_48dp, EnumView.VIEW_ORCAMENTOS_SOLICITADOS));
        }

        listViewMenu.setAdapter(new MenuItemListAdapter(this, menuItemList));
        listViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuItem menuItem = (MenuItem) parent.getItemAtPosition(position);
                openFragmentt(menuItem);
                openOrCloseMenu();
            }
        });
        openFragmentt((MenuItem)listViewMenu.getAdapter().getItem(0));
        openOrCloseMenu();
    }

    private boolean changing = false;
    private void openFragmentt(MenuItem menuItem) {

        if (changing)
            return;

        if(menuItem == null)
            return;

        fragmentManager.executePendingTransactions();

        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        //fTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        Fragment fragmentExistente = fragmentManager.findFragmentByTag(menuItem.getTag());

        for (MenuItem item : menuItemList) {
            fTransaction.hide(item.getFragment());
        }

        //fTransaction.commit();
        //fragmentManager.executePendingTransactions();

        //fTransaction = fragmentManager.beginTransaction();

        if(fragmentExistente == null) {
            fTransaction.add(R.id.drawer_drawer, menuItem.getFragment(), menuItem.getTag());
            fTransaction.show(menuItem.getFragment());
        } else {
            fTransaction.show(menuItem.getFragment());
            //fTransaction.replace(R.id.drawer_drawer, fragmentExistente, menuItem.getTag());
        }

        //if(fragmentManager.getBackStackEntryCount() == 0) {
            fTransaction.addToBackStack(null);
        //}

        fTransaction.commit();
        try {
            fragmentManager.executePendingTransactions();
        }catch (Exception ex) {
            Log.e(KEY, ex.getMessage(), ex);
            changing = false;
        }



        actionBar.setTitle(menuItem.getTitle());
        actionBar.setIcon(menuItem.getIcon());

        changing = false;

    }

    public void btnEfetuarLogoffOnClick(View view) {
        AndroidHelper.alertDialogYesNo(this, "Você deseja sair do sistema?", AndroidHelper.QUESTION, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    signOut();
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            }
        });
    }

    @Override
    public boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
        //

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home: {
                openOrCloseMenu();
                return true;
            }
            default: {
                return super.onMenuItemSelected(featureId, item);
            }
        }

    }

    private void openOrCloseMenu() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {

        /*
        if (fragmentManager.getBackStackEntryCount() <= 1) {
            openOrCloseMenu();
            return;
        }
        */

        openOrCloseMenu();
        //super.onBackPressed();
    }
}



