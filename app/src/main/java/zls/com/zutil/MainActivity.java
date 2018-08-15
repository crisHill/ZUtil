package zls.com.zutil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import zls.com.zlibrary.StringUtil;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((TextView)v).getText().toString().trim();
                s += StringUtil.isEmpty(s);
                ((TextView)v).setText(s);
            }
        });
    }
}
