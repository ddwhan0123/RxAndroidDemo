package sample.wjj.rxandroidglidedemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;

import java.util.List;

import javax.crypto.Mac;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import sample.wjj.rxandroidglidedemo.R;
import sample.wjj.rxandroidglidedemo.bean.ItemBean;
import sample.wjj.rxandroidglidedemo.tools.SharePreferencesTools;


public class MainActivity extends AppCompatActivity {
    Toolbar toolBar;
    EditText edit;
    TextView result;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        logic();
    }

    private void init() {
        setSupportActionBar(toolBar);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        result = (TextView) findViewById(R.id.result);
        edit = (EditText) findViewById(R.id.edit);
        btn = (Button) findViewById(R.id.btn);
    }

    private void logic() {
        toolBar.setTitle("RxAndroidDemo");

        saveText();
        reFreshText();
    }

    private void reFreshText() {
        RxTextView.afterTextChangeEvents(edit).subscribe(textViewAfterTextChangeEvent -> {
            result.setText(textViewAfterTextChangeEvent.editable().toString());
        });
    }


    private void saveText() {
        RxView.clicks(btn)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        result.setText(SharePreferencesTools.getString(MainActivity.this, "user") + " now ");
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {
                        SharePreferencesTools.putString(MainActivity.this, "user", edit.getText().toString().trim());
                        onCompleted();
                    }
                });
    }
}
