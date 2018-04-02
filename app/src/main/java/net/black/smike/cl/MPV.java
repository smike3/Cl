package net.black.smike.cl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

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

public class MPV extends Fragment implements View.OnClickListener{

    Button bt_mpv;
    ImageButton bt_mpvstop,bt_mpv_up,bt_mpv_down,bt_mpv_quit,bt_mpv_adv,bt_mpv_advadv,bt_mpv_rev,bt_mpv_revrev;
    TaskConn tk;
    Pref pp=new Pref();
    String mvp_files="";
    private static final String TAG = "LogCL";
    ServerSocket servers = null;
    Socket fromclient = null;
    private  static final int    serverPort = 3379;
    private  static final String localhost  = "127.0.0.1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mpv, null);
        bt_mpv=(Button)v.findViewById(R.id.bt_mvp2);
        bt_mpv_up=(ImageButton)v.findViewById(R.id.bt_mpv_up);
        bt_mpv_adv=(ImageButton)v.findViewById(R.id.bt_mpv_adv);
        bt_mpv_advadv=(ImageButton)v.findViewById(R.id.bt_mpv_advadv);
        bt_mpv_rev=(ImageButton)v.findViewById(R.id.bt_mpv_rev);
        bt_mpv_revrev=(ImageButton)v.findViewById(R.id.bt_mpv_revrev);
        bt_mpv_down=(ImageButton)v.findViewById(R.id.bt_mpv_down);
        bt_mpv_quit=(ImageButton)v.findViewById(R.id.bt_mpv_quit);
        bt_mpvstop=(ImageButton)v.findViewById(R.id.bt_mpvstop2);
        bt_mpv.setOnClickListener(this);
        bt_mpv_quit.setOnClickListener(this);
        bt_mpv_up.setOnClickListener(this);
        bt_mpv_down.setOnClickListener(this);
        bt_mpv_adv.setOnClickListener(this);
        bt_mpv_advadv.setOnClickListener(this);
        bt_mpv_rev.setOnClickListener(this);
        bt_mpv_revrev.setOnClickListener(this);
        bt_mpvstop.setOnClickListener(this);
        SharedPreferences prf;
        Pref ppp=new Pref();
        prf = this.getActivity().getSharedPreferences("CL_settings", Context.MODE_PRIVATE);
        //String ip_s=prf.getString("server_name", "");
        //System.out.println("\n!"+ip_s);
        //return ip_s;
        ppp.set_ipAddr(prf.getString("server_name", ""));
        ppp.set_path_mvp(prf.getString("mvp_dir", ""));
        pp=ppp;
  //      tk=new TaskConn();
    //    tk.execute("1dir"+pp.path_mvp,pp.ipAddr);
        return v;
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
                    //if(aud_data.vol<=0) {
              //      aud_data.vol = Integer.parseInt(result.substring(2, 5));
             //       skb.setProgress(aud_data.vol);
                    //}
                    //else aud_data.vol = Integer.parseInt(result.substring(2, 5));
               //     tv.setText(result.substring(6));
              //      tv_vol.setText(Integer.toString(aud_data.vol));
               //     System.out.println("!!!!!!!"+skb.getProgress());
                    //tv_vol.setText(skb.getProgress());
                    break;
                case 1:
                    //  String files[]=result.split("!");
                    //    Log.d(TAG, files[0]);
                    System.out.println("-->>"+result);

                    if(result.substring(1,4).equals("dir")) {mvp_files = result.substring(5);
                    System.out.println("--->>"+mvp_files);
                        Intent intent_mvp = new Intent(getActivity().getApplication(), MVP_list.class);
                        intent_mvp.putExtra("mvp_files",mvp_files);
                        startActivityForResult(intent_mvp,1);
                        }
                    break;
            }
            // tv.setText(result.substring(0,1));
        }
    }

    public void onClick(View view) {
            tk=new TaskConn();
            switch(view.getId()) {
                case R.id.bt_mpv_quit:
                    tk.execute("1qui", pp.ipAddr);
                    break;
                case R.id.bt_mpv_up:
                    tk.execute("1vup", pp.ipAddr);
                    break;
                case R.id.bt_mpv_down:
                    tk.execute("1vdn", pp.ipAddr);
                    break;
                case R.id.bt_mvp2:
                    //System.out.println(pp.);
                    tk.execute("1dir"+pp.path_mvp,pp.ipAddr);
                  //  Intent intent_mvp = new Intent(getActivity().getApplication(), MVP_list.class);
                //    intent_mvp.putExtra("mvp_files",mvp_files);
               //     startActivityForResult(intent_mvp,1);
                //    tk=new TaskConn();
               //     tk.execute("1mvp"+pp.path_mvp+name,pp.ipAddr);
                    break;
                case R.id.bt_mpvstop2:
                    System.out.println("########"+pp.ipAddr);
                    tk.execute("1stp", pp.ipAddr);
                    break;
                case R.id.bt_mpv_adv:
                    tk.execute("1adv", pp.ipAddr);
                    break;
                case R.id.bt_mpv_advadv:
                    tk.execute("1aad", pp.ipAddr);
                    break;
                case R.id.bt_mpv_rev:
                    tk.execute("1rev", pp.ipAddr);
                    break;
                case R.id.bt_mpv_revrev:
                    tk.execute("1rre", pp.ipAddr);
                    break;
                default:
                    //       tv.setText("22");
                    break;
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        String name=data.getStringExtra("name_mvp");
        System.out.println("VVVVVVV1mvp"+name);
        tk=new TaskConn();
        tk.execute("1mvp"+pp.path_mvp+name,pp.ipAddr);
        //  System.out.println("1mvp"+pp.path_mvp+name);
        //   tk=new MPV.TaskConn();
        //    tk.execute("1mvp"+pp.path_mvp+name,pp.ipAddr);
    }

}
