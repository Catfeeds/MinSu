package com.minsu.minsu.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.adapter.CityHistoryItemAdapter;
import com.minsu.minsu.common.adapter.CityItemAdapter;
import com.minsu.minsu.common.adapter.ListSortAdapter;
import com.minsu.minsu.common.bean.CityBean;
import com.minsu.minsu.common.bean.SortModel;
import com.minsu.minsu.utils.CharacterParser;
import com.minsu.minsu.utils.PinyinComparator;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.widget.SideBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CitySelectActivity extends BaseActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.country_lvcountry)
    ListView sortListView;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidrbar)
    SideBar sideBar;
    private String tokenId;
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    /**
     * 汉字转成拼音的类
     */
    private PinyinComparator pinyinComparator;
    private ListSortAdapter adapter;
    private String[] strings;
    private String[] city;
    private String location_city;//当前城市
    private TextView tv_history;
    private RecyclerView recyclerView_history;
    private RecyclerView recyclerView_hot;
    private TextView tv_hot;


    @Override
    protected void processLogic() {
        MinSuApi.citySelect(this, 0x001, tokenId, callBack);
    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("选择城市");

        location_city = getIntent().getStringExtra("location_city");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        tokenId = StorageUtil.getTokenId(this);
        initViews();
    }

    private void initViews() {
        //实例化汉字转拼音
//        获取characterParser的实例

        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        //设置右触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });
        View headerView = View.inflate(this, R.layout.address_header, null);
        TextView tv_now = (TextView) headerView.findViewById(R.id.tv_now);

//        tv_history = (TextView) headerView.findViewById(R.id.tv_history);
        recyclerView_history = (RecyclerView) headerView.findViewById(R.id.recyclerView_history);
        recyclerView_hot = (RecyclerView) headerView.findViewById(R.id.recyclerView_hot);
        recyclerView_history.setLayoutManager(new GridLayoutManager(CitySelectActivity.this, 5));
        recyclerView_hot.setLayoutManager(new GridLayoutManager(CitySelectActivity.this, 5));
        tv_now.setText(location_city);
        tv_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CitySelectActivity.this, "当前位置", Toast.LENGTH_SHORT).show();

            }
        });

//        tv_hot = (TextView) headerView.findViewById(R.id.tv_hot);
//        tv_hot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                Toast.makeText(CitySelectActivity.this, "热门位置", Toast.LENGTH_SHORT).show();
//            }
//        });
        sortListView.addHeaderView(headerView);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position > 0) {
//                    Toast.makeText(getApplication(), ((SortModel) adapter.getItem(position - 1)).getName(), Toast.LENGTH_SHORT).show();
//                    SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
//                    sp.edit().putString("Location", ((SortModel) adapter.getItem(position - 1)).getName()).apply();
                    Intent intent = getIntent();
                    intent.putExtra("address", ((SortModel) adapter.getItem(position - 1)).getName());
                    CitySelectActivity.this.setResult(0, intent);
                    CitySelectActivity.this.finish();
                }
            }
        });

        SourceDateList = filledData(getResources().getStringArray(R.array.date));
//       根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
//        创建adapter
        adapter = new ListSortAdapter(this, SourceDateList);
//        给listView设置数据
        sortListView.setAdapter(adapter);
    }

    /**
     * ÎªListViewÌî³äÊý¾Ý
     * 填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
//            汉字转拼音
            String pinyin = characterParser.getSelling(date[i]);
//            转换为大写
            String sortString = pinyin.substring(0, 1).toUpperCase();

//            正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_city_select);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    private CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            CityBean cityBean = new Gson().fromJson(result.body(), CityBean.class);
                            List<CityBean.Data2> data2 = cityBean.data2;
                            List<CityBean.Data3> data3 = cityBean.data3;
                            CityItemAdapter hotCityItemAdapter = new CityItemAdapter(R.layout.item_city, data2);
                            CityHistoryItemAdapter historyCityItemAdapter = new CityHistoryItemAdapter(R.layout.item_city, data3);
                            recyclerView_history.setAdapter(historyCityItemAdapter);
                            recyclerView_hot.setAdapter(hotCityItemAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }

        }

        @Override
        public void onFail(int what, Response<String> result) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
