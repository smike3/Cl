package net.black.smike.cl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int n_port = 6778;
    TaskConn tk;
    TextView tv;
    TextView tv_vol;
    Aud_Data aud_data=new Aud_Data();
    SharedPreferences prf;
    Pref pp=new Pref();

//
    Button bt_st,bt_ps,bt_adv,bt_rev,bt_volup,bt_voldown,bt_mvp,bt_mpvstop;
    ImageButton bt_pl;
    Toolbar tlb;
    Handler handler;
        //String str_info = "", ipAddr,path_mvp;
    String str_info = "",mvp_files="";
    ServerSocket servers = null;
    Socket fromclient = null;
    private static final String TAG = "LogCL";

    private  static final int    serverPort = 3379;
    private  static final String localhost  = "127.0.0.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_pl=(ImageButton)findViewById(R.id.bt_pl);
        bt_st=(Button)findViewById(R.id.bt_st);
        bt_ps=(Button)findViewById(R.id.bt_ps);
        bt_adv=(Button)findViewById(R.id.bt_adv);
        bt_rev=(Button)findViewById(R.id.bt_rev);
        bt_volup=(Button)findViewById(R.id.bt_volup);
        bt_voldown=(Button)findViewById(R.id.bt_voldown);
        bt_mvp=(Button)findViewById(R.id.bt_mvp);
        bt_mpvstop=(Button)findViewById(R.id.bt_mpvstop);
        tv = (TextView)findViewById(R.id.tv);
        tv_vol = (TextView)findViewById(R.id.tv_vol);
        tlb=(Toolbar)findViewById(R.id.tlb);
        bt_pl.setOnClickListener(this);
        bt_st.setOnClickListener(this);
        bt_ps.setOnClickListener(this);
        bt_adv.setOnClickListener(this);
        bt_rev.setOnClickListener(this);
        bt_volup.setOnClickListener(this);
        bt_voldown.setOnClickListener(this);
        bt_mvp.setOnClickListener(this);
        bt_mpvstop.setOnClickListener(this);
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

                        break;
                    case R.id.mpv:
                        Intent intent = new Intent(getBaseContext(), Settings.class);
                        startActivity(intent);
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
        // Inflate the menu; this adds items to the action bar if it is present.
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
    @Override
    public void onClick(View v) {
        tk=new TaskConn();
        switch (v.getId()) {
            case R.id.bt_adv:
                tk.execute("0playlist-advance",pp.ipAddr);
                break;
            case R.id.bt_pl:
                tk.execute("0playback-play",pp.ipAddr);
                break;
            case R.id.bt_st:
                tk.execute("0playback-stop",pp.ipAddr);
                break;
            case R.id.bt_ps:
                tk.execute("0playback-pause",pp.ipAddr);
                break;
            case R.id.bt_rev:
                tk.execute("0playlist-reverse",pp.ipAddr);
                break;
            case R.id.bt_volup:
                aud_data.vol=aud_data.vol+5;
                aud_data.norm();
                tk.execute("0set-volume "+aud_data.vol,pp.ipAddr);
                break;
            case R.id.bt_voldown:
                aud_data.vol=aud_data.vol-5;
                aud_data.norm();
                tk.execute("0set-volume "+aud_data.vol,pp.ipAddr);
                break;
            case R.id.bt_mvp:
                Intent intent_mvp = new Intent(this, MVP_list.class);
                intent_mvp.putExtra("mvp_files",mvp_files);
                startActivityForResult(intent_mvp,1);
                break;
            case R.id.bt_mpvstop:
                tk.execute("1stp",pp.ipAddr);
                break;
            default:
                tv.setText("22");
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        String name=data.getStringExtra("name_mvp");
        System.out.println("1mvp"+pp.path_mvp+name);
        tk=new TaskConn();
        tk.execute("1mvp"+pp.path_mvp+name,pp.ipAddr);
    }

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

    class MVP_Date{


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
    Pref loadPref() {
        Pref ppp=new Pref();
        prf = getSharedPreferences("CL_settings",MODE_PRIVATE);
        //String ip_s=prf.getString("server_name", "");
        //System.out.println("\n!"+ip_s);
        //return ip_s;
        ppp.set_ipAddr(prf.getString("server_name", ""));
        ppp.set_path_mvp(prf.getString("mvp_dir", ""));
        return ppp;
    }

    class TaskConn extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... line) {
            Socket socket = null;
            String line2 = "";
            int c;
            try {
                try {
                    //byte[] ipAddr = new byte[]{10, 6, (byte)133, 66};

                    System.out.println("\n!!!!" + line[1]);
                    //InetAddress ipAddress = InetAddress.getByAddress(ipAddr);
                    //InetAddress ipAddress = InetAddress.getByName("10.6.133.66");
                    InetAddress ipAddress = InetAddress.getByName(line[1]);

                    System.out.println("Welcome to Client side\n" +
                            "Connecting to the server\n\t" +
                            "(IP address " + localhost +
                            ", port " + serverPort + ")");
                    // InetAddress ipAddress = InetAddress.getByName(localhost);
                    //socket = new Socket(ipAddress, serverPort);
                    socket = new Socket();
                    //socket.setSoTimeout(1000);
                    socket.connect(new InetSocketAddress(line[1], serverPort), 1000);

                    System.out.println("The connection is established.");

                    System.out.println(
                            "\tLocalPort = " +
                                    socket.getLocalPort() +
                                    "\n\tInetAddress.HostAddress = " +
                                    socket.getInetAddress().getHostAddress() +
                                    "\n\tReceiveBufferSize (SO_RCVBUF) = " +
                                    socket.getReceiveBufferSize());

                    // Получаем входной и выходной потоки сокета для обмена
                    // сообщениями с сервером
                    InputStream sin = socket.getInputStream();
                    OutputStream sout = socket.getOutputStream();

                    DataInputStream in = new DataInputStream(sin);
                    DataOutputStream out = new DataOutputStream(sout);


                    //    String line=et.getText().toString();
                    //      tv.append(line);
                    //   String line="sdfffd";
                    //   out.writeUTF(line);     // Отсылаем строку серверу
                    byte[] bb = line[0].getBytes();
                    //   String comm="playback-play";
                    //byte[] bb=line[0].getBytes();
                    out.write(bb, 0, bb.length);
                    out.flush();            // Завершаем поток
                    //line2 = in.readUTF();    // Ждем ответа от сервера
                    byte[] bb2 = new byte[20480];
                    // Log.d(TAG,bb2.toString());
                    c = in.read(bb2);
                    //bb2[c]='\0';
                    line2 = new String(bb2);
                    line2 = line2.substring(0, c);
                    //  Log.d(TAG,"!"+bb2.toString()+"!"+c);
                    //  tv.append('\n'+line2);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "Exce 1");
                    line2="0[000]none";
                   // return "0[000]none";

                }
            } finally {
                try {
                    if (socket != null)
                        socket.close();
                    Log.d(TAG, "Exce 2");

                } catch (IOException e) {
                    //e.printStackTrace();
                    //                   return line2;
                    Log.d(TAG, "Exce 3");
                }
            }
            Log.d(TAG, "Exce 111");
            return line2;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "123"+result);
            // if(aud_data.vol==-1) aud_data.vol=Integer.parseInt(result.substring(1,4));
            switch (Integer.parseInt(result.substring(0,1))) {
                case 0:
                    aud_data.vol = Integer.parseInt(result.substring(2, 5));
                    tv.setText(result.substring(6));
                    tv_vol.setText(result.substring(2, 5));
                    break;
                case 1:
                  //  String files[]=result.split("!");
                //    Log.d(TAG, files[0]);
                 //   System.out.println(files.length);
                    mvp_files=result.substring(5);
                    break;
            }
           // tv.setText(result.substring(0,1));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        pp=loadPref();
        tk=new TaskConn();
        tk.execute("0current-song",pp.ipAddr);
        tk=new TaskConn();
        tk.execute("1dir"+pp.path_mvp,pp.ipAddr);
        Log.d(TAG, "MainActivity: onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity: onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity: onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity: onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity: onDestroy()");
    }

}