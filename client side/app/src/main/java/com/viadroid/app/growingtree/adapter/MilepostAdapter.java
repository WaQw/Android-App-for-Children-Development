package com.viadroid.app.growingtree.adapter;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.viadroid.app.growingtree.App;
import com.viadroid.app.growingtree.R;
import com.viadroid.app.growingtree.base.BaseRvAdapter;
import com.viadroid.app.growingtree.base.BaseRvHolder;
import com.viadroid.app.growingtree.entries.Milestone;
import com.viadroid.app.growingtree.util.DateUtils;

import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AlertDialog;

/**
 * 里程碑Adapter
 */
public class MilepostAdapter extends BaseRvAdapter<Milestone> {

    private int[] iv_mark_res_ids = {
            R.drawable.ic_smile,
            R.drawable.ic_eat,
            R.drawable.ic_sit,
            R.drawable.ic_stand_up,
            R.drawable.ic_crawl,
            R.drawable.ic_walk,
            R.drawable.ic_call_parents,
            R.drawable.ic_call_parents,
            R.drawable.ic_stand_up,
            R.drawable.ic_walk,
    };

    public MilepostAdapter(List<Milestone> list) {
        super(list);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_milepost_layout;
    }

    @Override
    public void onBindViewHolder(BaseRvHolder holder, Milestone item, int position) {

        holder.getImageView(R.id.iv_mark).setImageResource(iv_mark_res_ids[item.getType()]);

        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_up_time, item.getCreatedTime());
        holder.setText(R.id.tv_des, item.getDes());
        holder.setText(R.id.tv_assessment, getAssessment(item.getType(), item.getCreatedTime()));

        ImageView imageView = holder.getImageView(R.id.iv_photo);
        Glide.with(getContext()).load(item.getPhotoUrl()).centerCrop().into(imageView);

        float month = calcMonth(item.getCreatedTime());
        holder.itemView.setOnClickListener(v -> {
            int arrayId = getRecommendationStringArray(month);
            String[] array = getContext().getResources().getStringArray(arrayId);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    Random random = new Random();
                    int i = random.nextInt(3);
                    Log.d(TAG, "onBindViewHolder: Random:" + i);
                    String msg = array[i];

                    builder.setTitle("Tips for care:")
                            .setMessage(msg);

                    builder.show();
                }
        );

        setNormal(holder, item);
        holder.setText(R.id.tv_actually, "Actually: " + String.format("%.1f",month) + " month");
    }

    private String getAssessment(int type, String date) {
        float months = calcMonth(date);

        return getAssessment(type, months);
    }

    private float calcMonth(String date) {
        int days = DateUtils.daysBetween(
                DateUtils.string2Date(App.getApp().getBaby().getBirthday(), DateUtils.PATTEN_YMDSF),
                DateUtils.string2Date(date, DateUtils.PATTEN_YMDSF));
        return days / 30.0f;
    }

    private String getAssessment(int type, float months) {
        //笑3月 自己吃饼干9月 自己坐9月 扶着站11月 爬13月 扶着走13月 叫妈妈/爸爸15月 自己站16月 自己走17月

        int[] arrays = {
                R.array.first_smile,
                R.array.first_eat_biscuits,
                R.array.first_sit_self,
                R.array.first_stand_up,
                R.array.first_crawl,
                R.array.first_walk,
                R.array.first_call_parents,
                R.array.first_call_parents,
                R.array.first_stand_self,
                R.array.first_walk_self
        };

        int[] b = {3, 9, 9, 11, 13, 13, 15, 15, 16, 17};

        String[] stringArray = getContext().getResources().getStringArray(arrays[type]);

        if (months > b[type]) {
            return stringArray[1];
        }
        return stringArray[0];

    }

    private void setNormal(BaseRvHolder holder, Milestone item) {

        String[] array = getContext().getResources().getStringArray(R.array.milestones_standard_times);

        holder.setText(R.id.tv_normal, "Normal: " + array[item.getType()]);
    }

    private int getRecommendationStringArray(float month) {
        if (month <= 1) {
            return R.array.recommend_1_month;
        } else if (month <= 2) {
            return R.array.recommend_2_month;
        } else if (month <= 3) {
            return R.array.recommend_3_month;
        } else if (month <= 4) {
            return R.array.recommend_4_month;
        } else if (month <= 5) {
            return R.array.recommend_5_month;
        } else if (month <= 6) {
            return R.array.recommend_6_month;
        } else if (month <= 7) {
            return R.array.recommend_7_month;
        } else if (month <= 8) {
            return R.array.recommend_8_month;
        } else if (month <= 9) {
            return R.array.recommend_9_month;
        } else if (month <= 10) {
            return R.array.recommend_10_month;
        } else if (month <= 11) {
            return R.array.recommend_11_month;
        } else if (month <= 12) {
            return R.array.recommend_12_month;
        } else if (month <= 15) {
            return R.array.recommend_15_month;
        } else if (month <= 18) {
            return R.array.recommend_18_month;
        } else {
            return R.array.recommend_24_month;
        }
    }
}
