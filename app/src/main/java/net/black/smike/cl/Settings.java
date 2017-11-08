package net.black.smike.cl;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by smike on 07.11.17.
 */

public class Settings extends AppCompatActivity implements View.OnClickListener {

    Button bt_save;
    EditText et_server;
    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        prf=getSharedPreferences("CL_settings",MODE_PRIVATE);
        bt_save=(Button)findViewById(R.id.bt_save);
        et_server=(EditText)findViewById(R.id.et_server);
        bt_save.setOnClickListener(this);
        et_server.setText(prf.getString("server_name", ""));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.bt_save: saveIP();
                finish();
                break;

        }
    }

    void saveIP()
    {
        SharedPreferences.Editor ed = prf.edit();
        ed.putString("server_name", et_server.getText().toString());
        System.out.println("\n333"+et_server.getText().toString()+"333\n");
        ed.commit();
    }

}
