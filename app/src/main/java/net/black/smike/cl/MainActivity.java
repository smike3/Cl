package net.black.smike.cl;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

class Aud_Data{
    int vol;
    void norm()
    {
        if(vol<0) vol=0;
        if(vol>100) vol=100;
    }
    Aud_Data()
    {
        vol=-1;
    }
}

class MPV_Data{
    String path,name;
    MPV_Data()
    {

        path="";
        name="";
    }
}

class Pref{
    String ipAddr,path_mvp;
    public void set_ipAddr(String s)
    {
        ipAddr=s;
    }
    public void set_path_mvp(String s)
    {
        path_mvp=s+"/";
    }
}

public class MainActivity extends AppCompatActivity{
    MPV frag_mpv;
    Auda frag_auda;
    FragmentTransaction ft;
    FragmentManager fm;
    Toolbar tlb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tlb=(Toolbar)findViewById(R.id.tlb);
        frag_mpv = new MPV();
        frag_auda= new Auda();
        fm=getFragmentManager();
        ft=fm.beginTransaction();
        ft.add(R.id.laym, frag_auda);
        ft.commit();
        setSupportActionBar(tlb);
        DrawerLayout dr_lay = (DrawerLayout) findViewById(R.id.dr_lay);
        ActionBarDrawerToggle tog = new ActionBarDrawerToggle(this, dr_lay, tlb,R.string.dr_open,R.string.dr_close);
        dr_lay.addDrawerListener(tog);
        tog.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(
               new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.auda:
                        ft=fm.beginTransaction();
                        ft.replace(R.id.laym, frag_auda);
                        ft.commit();
                        break;
                    case R.id.mpv:
                        ft=fm.beginTransaction();
                        ft.replace(R.id.laym, frag_mpv);
                        ft.commit();
                        break;
                    default:
                        return false;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dr_lay);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        }
        );
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tlb_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dr_lay);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    Pref loadPref() {
        SharedPreferences prf;
        Pref ppp=new Pref();
        prf = getSharedPreferences("CL_settings", Context.MODE_PRIVATE);
        ppp.set_ipAddr(prf.getString("server_name", ""));
        ppp.set_path_mvp(prf.getString("mvp_dir", ""));
        return ppp;
    }


}