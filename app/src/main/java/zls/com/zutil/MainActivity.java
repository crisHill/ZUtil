package zls.com.zutil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import zls.com.zlibrary.StringUtil;
import zls.com.zlibrary.view.DownloadProgress;

public class MainActivity extends Activity {

    DownloadProgress dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dp = findViewById(R.id.dp);

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp.setInfo(67f, false);
            }
        });
    }
}
