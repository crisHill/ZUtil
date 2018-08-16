package zls.com.zutil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import zls.com.zlibrary.StringUtil;
import zls.com.zlibrary.view.DownloadProgress;
import zls.com.zlibrary.view.Voicer;

public class MainActivity extends Activity {

    DownloadProgress dp;
    RelativeLayout root;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        dp = findViewById(R.id.dp);
        root = findViewById(R.id.root);

        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dp.setInfo(67f, false);
                root.addView(Voicer.create(context, 300));
            }
        });

    }
}
