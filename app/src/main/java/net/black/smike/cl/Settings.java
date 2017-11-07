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
    EditText et_ip1,et_ip2,et_ip3,et_ip4;
    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        bt_save=(Button)findViewById(R.id.bt_save);
        et_ip1=(EditText)findViewById(R.id.et_ip1);
        et_ip2=(EditText)findViewById(R.id.et_ip2);
        et_ip3=(EditText)findViewById(R.id.et_ip3);
        et_ip4=(EditText)findViewById(R.id.et_ip4);
        bt_save.setOnClickListener(this);
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
        prf=getPreferences(MODE_PRIVATE);

    }

}
