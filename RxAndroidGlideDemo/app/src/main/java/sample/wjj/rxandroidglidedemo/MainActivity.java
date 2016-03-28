package sample.wjj.rxandroidglidedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import sample.wjj.rxandroidglidedemo.OBJ.TestOBJ;


public class MainActivity extends AppCompatActivity {
    Toolbar toolBar;
    RecyclerView recycleView;
    DataAdapter dataAdapter;
    String data[] = {"http://hiphotos.baidu.com/zhixin/abpic/item/4651a712c8fcc3cea97dbce49045d688d53f206c.jpg"
            , "http://pic.5442.com/2014/0930/06/5442.jpg",
            "http://img5q.duitang.com/uploads/item/201410/22/20141022214043_5EEKH.thumb.224_0.jpeg",
            "http://img5.duitang.com/uploads/item/201512/08/20151208163159_HGEM2.thumb.224_0.png",
            "http://img4.duitang.com/uploads/item/201510/29/20151029224537_ijEKF.thumb.224_0.jpeg",
            "http://img5.imgtn.bdimg.com/it/u=1230273521,1023320328&fm=21&gp=0.jpg"
    };

    //被观察者
    Observable observable = Observable.create(new Observable.OnSubscribe<List<TestOBJ>>() {
        @Override
        public void call(Subscriber<? super List<TestOBJ>> subscriber) {
            subscriber.onNext(makeData());
            subscriber.onCompleted();
        }
    });

    //观察者
    Observer<List<TestOBJ>> observer = new Observer<List<TestOBJ>>() {

        @Override
        public void onCompleted() {
            LogUtils.d("--->onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<TestOBJ> testOBJs) {
            testOBJs.size();
            LogUtils.d("--->onNext  testOBJs.size() " + testOBJs.size());
            //刷新数据
            dataAdapter.updateData(testOBJs);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        logic();
        setSupportActionBar(toolBar);
        StatusBarCompat.compat(this, getResources().getColor(R.color.status_bar_color));
        StatusBarCompat.compat(this);
    }

    private void init() {
        LogUtils.d("--->init");
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    private void logic() {
        LogUtils.d("--->logic");
        dataAdapter = new DataAdapter(MainActivity.this);
        recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recycleView.setAdapter(dataAdapter);

        LogUtils.d("--->Glide.getPhotoCacheDir " + Glide.getPhotoCacheDir(MainActivity.this, "MY_CACHE_LOCATION"));

    }

    //制造数据
    private List<TestOBJ> makeData() {
        List<TestOBJ> list = new ArrayList<>();
        TestOBJ testOBJ;
        for (int k = 0; k < 5; k++) {
            testOBJ = new TestOBJ();
            testOBJ.content = "标题啊,你服不服 " + k + " 个 ";
            testOBJ.imageUrl = data[k];
            list.add(testOBJ);
        }
        LogUtils.d("--->list的长度等于 " + list.size());
        return list;
    }

}
