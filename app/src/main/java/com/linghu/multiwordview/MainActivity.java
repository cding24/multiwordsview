package com.linghu.multiwordview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.linghu.multiwords.R;

/**
 * created by linghu on 2017/09/20
 * 多个词组文字控件demo
 *
 */
public class MainActivity extends AppCompatActivity {

    private MultiWordsView majorMTV;
    private String[] majorData = {"会计","计算机科学技术","计算机软件","计算机系统结构","通信工程","数学与信息技术"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.linghu.multiwords.R.layout.activity_main);
        majorMTV = (MultiWordsView) findViewById(R.id.major);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //下面是模拟数据加载的时间过程，所以延迟处理了，大家可以按照自己的来写
        majorMTV.postDelayed(new Runnable() {
            @Override
            public void run() {
                majorMTV.setContents(majorData);
            }
        }, 2000);
    }

}
