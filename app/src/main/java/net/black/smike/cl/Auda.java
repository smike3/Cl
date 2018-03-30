package net.black.smike.cl;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by smike on 28.11.17.
 */


public class Auda extends Fragment implements View.OnClickListener {

    Button bt_mpv, bt_mpvstop;
    ImageButton bt_st,bt_ps,bt_adv,bt_rev,bt_pl,bt_vol_up,bt_vol_down;
    TextView tv;
    TextView tv_vol;
    TaskConn tk;
    Pref pp=new Pref();
    String mvp_files="";
    SeekBar skb;
    Toolbar tlb;
    Aud_Data aud_data=new Aud_Data();
    private static final String TAG = "LogCL";
    ServerSocket servers = null;
    Socket fromclient = null;
    private  static final int    serverPort = 3379;
    private  static final String localhost  = "127.0.0.1";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.auda, null);
        bt_pl=(ImageButton)v.findViewById(R.id.bt_pl);
        bt_st=(ImageButton)v.findViewById(R.id.bt_st);
        bt_ps=(ImageButton)v.findViewById(R.id.bt_ps);
        bt_adv=(ImageButton)v.findViewById(R.id.bt_adv);
        bt_rev=(ImageButton)v.findViewById(R.id.bt_rev);
        bt_vol_up=(ImageButton)v.findViewById(R.id.bt_vol_up);
        bt_vol_down=(ImageButton)v.findViewById(R.id.bt_vol_down);
        tv = (TextView)v.findViewById(R.id.tv);
        tv_vol = (TextView)v.findViewById(R.id.tv_vol);
        tlb=(Toolbar)v.findViewById(R.id.tlb);
        skb=(SeekBar) v.findViewById(R.id.skb);
        bt_pl.setOnClickListener(this);
        bt_st.setOnClickListener(this);
        bt_ps.setOnClickListener(this);
        bt_adv.setOnClickListener(this);
        bt_rev.setOnClickListener(this);
        bt_vol_up.setOnClickListener(this);
        bt_vol_down.setOnClickListener(this);
        skb.setOnSeekBarChangeListener(seekBarChangeListener);

        return v;
    }


    public void onClick(View view) {
        tk=new TaskConn();
        switch(view.getId()) {
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
            case R.id.bt_vol_up:
                aud_data.vol+=3;
                aud_data.norm();
                tk.execute("0set-volume "+aud_data.vol,pp.ipAddr);
                break;
            case R.id.bt_vol_down:
                aud_data.vol-=3;
                aud_data.norm();
                tk.execute("0set-volume "+aud_data.vol,pp.ipAddr);
                break;
            default:
                tv.setText("22");
                break;
        }
    }


    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            aud_data.vol=skb.getProgress();
            tv_vol.setText(Integer.toString(aud_data.vol));

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            tk=new TaskConn();
            tk.execute("0set-volume "+aud_data.vol,pp.ipAddr);
        }
    };


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
                    //if(aud_data.vol<=0) {
                          aud_data.vol = Integer.parseInt(result.substring(2, 5));
                           skb.setProgress(aud_data.vol);
                    //}
                    //else aud_data.vol = Integer.parseInt(result.substring(2, 5));
                        tv.setText(result.substring(6).replaceAll(" - ","\n"));
                          tv_vol.setText(Integer.toString(aud_data.vol));
                         System.out.println("!!!!!!!"+skb.getProgress());
                    //tv_vol.setText(skb.getProgress());
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
    public void onStart() {
        super.onStart();
        SharedPreferences prf;
        Pref ppp=new Pref();
        prf = this.getActivity().getSharedPreferences("CL_settings", Context.MODE_PRIVATE);
        //String ip_s=prf.getString("server_name", "");
        //System.out.println("\n!"+ip_s);
        //return ip_s;
        ppp.set_ipAddr(prf.getString("server_name", ""));
        ppp.set_path_mvp(prf.getString("mvp_dir", ""));
        pp=ppp;
        tk=new TaskConn();
        tk.execute("0current-song",pp.ipAddr);
       // pp=loadPref();
    //    tk=new TaskConn();
      //  tk.execute("0current-song",pp.ipAddr);
    }
}
