package net.black.smike.cl;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by smike on 15.11.17.
 */

public class MVP_list extends AppCompatActivity implements View.OnClickListener {

    EditText et_server;
    SharedPreferences prf;
    ListView lv;

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mvp_list, null);
      //  Intent intent = getIntent();
     //   String mvp_files = intent.getStringExtra("mvp_files");
        final String files[]={"12334"};//mvp_files.split("!");
        lv=(ListView)v.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, files);
        lv.setAdapter(adapter);
//        System.out.println(files[1]);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               // Intent ii=new Intent();
                //ii.putExtra("name_mvp", files[position]);
                System.out.println(files[position]);
                //setResult(RESULT_OK, ii);
              //  finish();
            }
        });
        return v;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mvp_list);
        Intent intent = getIntent();
        JSONObject comm_j;
        JSONArray files_name;
        final String files[];
        String mvp_files = intent.getStringExtra("mvp_files");
       // final String files[]=mvp_files.split("!");
        try {
            comm_j = new JSONObject(mvp_files);
            files_name=comm_j.getJSONArray("file_list");
            files=new String[files_name.length()];
            for(int c=0;c < files_name.length();c++)
                {
                    files[c]=files_name.getString(c);
                    System.out.println("   > "+files[c]);
                }

        lv=(ListView)findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, files);
        lv.setAdapter(adapter);
//        System.out.println(files[1]);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent ii=new Intent();
                ii.putExtra("name_mvp", files[position]);
                System.out.println(files[position]);
                setResult(RESULT_OK, ii);
                finish();
            }
        });
        } catch (JSONException e)
        {
            System.out.println(" JSON!!! ");
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {


        }
    }
}
