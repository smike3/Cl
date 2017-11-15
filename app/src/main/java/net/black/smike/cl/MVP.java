package net.black.smike.cl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Created by smike on 15.11.17.
 */

public class MVP extends AppCompatActivity implements View.OnClickListener {

    EditText et_server;
    SharedPreferences prf;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mvp);
        Intent intent = getIntent();
        String mvp_files = intent.getStringExtra("mvp_files");
        String files[]=mvp_files.split("!");
        lv=(ListView)findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, files);
        lv.setAdapter(adapter);
        System.out.println(files[1]);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {


        }
    }
}
