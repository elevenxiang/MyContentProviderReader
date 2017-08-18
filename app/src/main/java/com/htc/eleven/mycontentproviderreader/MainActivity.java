package com.htc.eleven.mycontentproviderreader;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Uri URI = Uri.parse("content://com.htc.eleven_xiang");

    //获取系统换行符, windows/linux 各不一样,需要透过接口获取, 这个跟文件路径分隔符 从 File.separator 获取一样 !
    private String lineSperator = System.getProperty("line.separator"," ");

    private TextView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.readSQL).setOnClickListener(this);
        content = (TextView) findViewById(R.id.content);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.readSQL:
                Cursor c = getContentResolver().query(URI, null,null,null,null);

                // initialize Cursor c position !!!
                c.moveToFirst();

                System.out.println("content has " + c.getCount() + " lines");
                for (int i=0; i<c.getCount(); i++) {
                    String data = c.getString(c.getColumnIndex("name"));

                    if(i==0){
                        //overwrite default string content, and change line.
                        content.setText(data+lineSperator);
                    } else
                        //append subsequent strings.
                        content.append(data+lineSperator);

                    // move to next !!!
                    c.moveToNext();
                }
                break;
        }
    }
}
