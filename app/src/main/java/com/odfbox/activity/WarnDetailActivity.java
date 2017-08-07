package com.odfbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.adapter.EventListAdapter;
import com.odfbox.entity.EventList;
import com.odfbox.entity.Odfbox;
import com.odfbox.entity.Warns;
import com.odfbox.handle.BoxDetailHandler;
import com.odfbox.handle.EventHandler;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.odfbox.R.id.text_title;

public class WarnDetailActivity extends BaseActivity {
    Warns warns;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.code)
    TextView code;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.box_news)
    RelativeLayout boxNews;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.line1)
    ImageView line1;
    @Bind(R.id.checkbox)
    CheckBox checkbox;
    @Bind(R.id.warn_first)
    TextView warnFirst;
    @Bind(R.id.line2)
    ImageView line2;
    @Bind(text_title)
    TextView textTitle;
    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.activity_warn_detail)
    RelativeLayout activityWarnDetail;
    Odfbox boxDetail;
    EventListAdapter adapter;

    public boolean isWarns = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_detail);
        ButterKnife.bind(this);
        OdfboxApplication.addActivity(this);
        setTitleTextView("事件告警列表", null);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            warns = (Warns) bundle.getSerializable("info");
        }

        initData();
        getBoxDetail();
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("info", boxDetail);
                intent.putExtras(bundle1);
                intent.setClass(mContext, BoxDetailActivity.class);
                startActivity(intent);
            }
        });


        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isWarns = true;
                } else {
                    isWarns = false;
                }
                getList();
            }
        });


    }

    public void initData() {
        if (warns != null) {
            if (warns.position != null) {
                address.setText("地址：" + warns.position.address);
            } else {
                address.setText("地址：--------");
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    public void getList() {
        showProgress(0, true);
        client.getEventList(mContext, warns.odf_box, isWarns, new EventHandler() {
            @Override
            public void onSuccess(EventList commen) {
                super.onSuccess(commen);
                cancelmDialog();
                textTitle.setText("事件和告警记录，共计" + commen.count + "条");
                adapter = new EventListAdapter(mContext, commen.results);
                listview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                cancelmDialog();
            }

            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }
        });
    }

    public void getBoxDetail() {
        client.getBoxDetail(mContext, warns.odf_box, new BoxDetailHandler() {
            @Override
            public void onSuccess(final Odfbox odfbox) {
                super.onSuccess(odfbox);
                if (odfbox != null) {
                    if (odfbox.serial_text != null) {
                        code.setText("光交箱编号：" + odfbox.serial_text);
                    }

                    if (odfbox.name != null) {
                        name.setText("光交箱名字：" + odfbox.name);
                    }

                }

                if (odfbox.picture.thumbnail_url != null) {
                    ImageLoader.getInstance().displayImage("http:" + odfbox.picture.thumbnail_url, image);
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, SpaceImageDetailActivity.class);
                            if (odfbox.picture != null) {
                                intent.putExtra("images", "http:" + odfbox.picture.url);
                            }
                            int[] location = new int[2];
                            image.getLocationOnScreen(location);
                            intent.putExtra("locationX", location[0]);
                            intent.putExtra("locationY", location[1]);
                            intent.putExtra("width", image.getWidth());
                            intent.putExtra("height", image.getHeight());
                            mContext.startActivity(intent);
                        }
                    });
                }
                if (odfbox != null) {
                    boxDetail = odfbox;
                } else {
                    boxDetail = new Odfbox();
                }

                address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.putExtra("type", "box");
                        intent.putExtra("lat", odfbox.latitude_baidu + "");
                        intent.putExtra("lon", odfbox.longitude_baidu + "");
                        intent.setClass(mContext, OdfboxLocationActivity.class);
                        mContext.startActivity(intent);
                    }
                });


            }
        });
    }

}
