package com.htc.eleven.mycontentproviderreader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     *  user customized database from another application.
     * */
    private Uri URI = Uri.parse("content://com.htc.eleven_xiang");

    /**
     * obtain \n or \r\n line break char from different system
     *
     * 获取系统换行符, windows/linux 各不一样,需要透过接口获取, 这个跟文件路径分隔符 从 File.separator 获取一样 !
     * */
    private String lineBreak = System.getProperty("line.separator", "###");
    /**
     * Uri for access media database.
     * */
    private Uri mMusicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    /**
     * String[] for query column.
     * */
    private String[] mColumns = new String[] {MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION};

    /**
     * String[] permissions
     * */
    private String[] mPermissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};

    /**
     * request code for read/write external storage.
     * */
    private int mRquestCode = 1;

    /**
     * TextView to show the query results.
     * */
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.readSQL).setOnClickListener(this);
        content = (TextView) findViewById(R.id.content);

        findViewById(R.id.scanMusic).setOnClickListener(this);
        requestPermissions(mPermissions,mRquestCode);
    }

    /**
     *  obtain request permission result from user, and if no permission, it will exit Activity UI directly.
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == mRquestCode) {
            for(int i=0; i<permissions.length; i++) {
                if(grantResults[i] != 0)
                    finish();
            }
        }
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
                        content.setText(data+lineBreak);
                    } else
                        //append subsequent strings.
                        content.append(data+lineBreak);

                    // move to next !!!
                    c.moveToNext();
                }
                break;

            case R.id.scanMusic:
                Cursor list = getContentResolver().query(mMusicUri,mColumns,"duration>10000", null, null);

                Toast.makeText(MainActivity.this, "There are " + list.getCount()+ " records", Toast.LENGTH_SHORT).show();
                if(list.moveToFirst()) {
                    int index;
                    String data;

                    content.setText("");
                    do {
                        index = list.getColumnIndex(MediaStore.Audio.Media.DATA);
                        data = list.getString(index);

                        content.append(data + lineBreak);
                    } while (list.moveToNext());
                }
                break;
        }
    }
}
