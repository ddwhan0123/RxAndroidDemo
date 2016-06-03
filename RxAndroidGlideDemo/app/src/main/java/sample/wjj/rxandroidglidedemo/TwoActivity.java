package sample.wjj.rxandroidglidedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sample.wjj.rxandroidglidedemo.OBJ.SampleModel;
import sample.wjj.rxandroidglidedemo.Tools.RandomTools;
import sample.wjj.rxandroidglidedemo.Widget.StatusBarCompat;

public class TwoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button testMap;
    private ImageView tiffanyImg;
    private RandomTools randomTools;
    private ArrayList<SampleModel> data;
    private TextView tiffanyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        StatusBarCompat.compat(this, getResources().getColor(R.color.status_bar_color));
        StatusBarCompat.compat(this);

        findId();
        init();
        setListener();
        LogUtils.d("--->Main Thread  id " + Thread.currentThread().getId());
    }

    private void findId() {
        testMap = (Button) findViewById(R.id.testMap);
        tiffanyImg = (ImageView) findViewById(R.id.tiffanyImg);
        tiffanyText = (TextView) findViewById(R.id.tiffanyText);
    }

    private void init() {
        randomTools = RandomTools.getInstance();
        //填充数据
        makeSampleModelData();
    }

    private void setListener() {
        testMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testMap:
                //setPhotoForOne(data);
                setPhotoForTwo(data);
                break;
        }

    }

    private void setPhotoForOne(ArrayList<SampleModel> data) {
        Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<ArrayList<SampleModel>, SampleModel>() {
                    @Override
                    public SampleModel call(ArrayList<SampleModel> list) {
                        LogUtils.d("第一个call线程id " + Thread.currentThread().getId());
                        //模拟返回集合的某个元素
                        int randInt = randomTools.getRandom(4);
                        return list.get(randInt);
                    }
                }).map(new Func1<SampleModel, String>() {
            @Override
            public String call(SampleModel sampleModel) {
                LogUtils.d("第二个call线程id " + Thread.currentThread().getId());
                return sampleModel.getContent();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        LogUtils.d("第三个call线程id " + Thread.currentThread().getId());
                        LogUtils.d("--->开始加载 " + url);
                        Glide.with(TwoActivity.this).load(url).into(tiffanyImg);
                    }
                });

    }

    private void setPhotoForTwo(ArrayList<SampleModel> data) {
        Observable.just(data).subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<ArrayList<SampleModel>, Observable<SampleModel>>() {
                    @Override
                    public Observable<SampleModel> call(ArrayList<SampleModel> sampleModels) {
                        return Observable.from(sampleModels);
                    }
                }).filter(new Func1<SampleModel, Boolean>() {
            @Override
            public Boolean call(SampleModel sampleModel) {
                return !sampleModel.getName().equals("");
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<SampleModel>() {
            @Override
            public void onCompleted() {
                LogUtils.d("--->onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e);
            }

            @Override
            public void onNext(SampleModel sampleModel) {
                LogUtils.d("--->onNext");
                Glide.with(TwoActivity.this).load(sampleModel.getContent()).into(tiffanyImg);
                tiffanyText.setText(sampleModel.getName());
            }
        });
    }

    //制造数据源
    private void makeSampleModelData() {
        data = new ArrayList<>();
        data.add(new SampleModel("Tiffany one", "http://hiphotos.baidu.com/zhixin/abpic/item/4651a712c8fcc3cea97dbce49045d688d53f206c.jpg"));
        data.add(new SampleModel("Tiffany two", "http://pic.5442.com/2014/0930/06/5442.jpg"));
        data.add(new SampleModel("", "http://img5q.duitang.com/uploads/item/201410/22/20141022214043_5EEKH.thumb.224_0.jpeg"));
        data.add(new SampleModel("Tiffany four", "http://img5.duitang.com/uploads/item/201512/08/20151208163159_HGEM2.thumb.224_0.png"));
        data.add(new SampleModel("Tiffany five", "http://img4.duitang.com/uploads/item/201510/29/20151029224537_ijEKF.thumb.224_0.jpeg"));
    }

}
