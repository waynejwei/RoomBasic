package com.example.roombasic;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private List<Word> AllWords = new ArrayList<>();
    private boolean useCardView;//判断是否适用卡片布局
    private WordViewModel wordViewModel;
    private static final String TAG = "MyAdapter";

    MyAdapter(boolean useCardView, WordViewModel wordViewModel) {
        this.useCardView = useCardView;
        this.wordViewModel = wordViewModel;
    }

    /*内部类，用于写部件的id*/
    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView_number,textView_english,textView_chinese;
        Switch switchInvisible;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_number = itemView.findViewById(R.id.textView_number);
            textView_english = itemView.findViewById(R.id.textView_english);
            textView_chinese = itemView.findViewById(R.id.textView_chinese);
            switchInvisible = itemView.findViewById(R.id.switch_invisible);
        }
    }

    void setAllWords(List<Word> allWords) {
        AllWords = allWords;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if(this.useCardView){
            view = layoutInflater.inflate(R.layout.ceil_card_2,parent,false);
        }else {
            view = layoutInflater.inflate(R.layout.ceil_normal_2,parent,false);
        }

        /*以下两个监听事件因为都是用new创建的监听器，所以不建议写在onBindViewHolder里(频繁创建)*/
        final MyViewHolder holder = new MyViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//频幕的点击事件，在viewholder创建时就建立监听器
                Log.d(TAG, "go to word");
                Uri uri = Uri.parse("http://www.youdao.com/w/"+holder.textView_english.getText()+"/#keyfrom=dict2.top");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.switchInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {//按钮监听事件
                Word word = (Word)holder.itemView.getTag(R.id.id_for_chinese_invisible);//getTag返回的是Object对象，要强转
                if(isChecked){
                    holder.textView_chinese.setVisibility(View.GONE);
                    word.setChineseInvisible(true);
                    wordViewModel.update_interface(word);
                }else{
                    holder.textView_chinese.setVisibility(View.VISIBLE);
                    word.setChineseInvisible(false);
                    wordViewModel.update_interface(word);
                }
            }
        });
        return holder;
    }

    /*数据的绑定*/
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Word word = AllWords.get(position);
        holder.itemView.setTag(R.id.id_for_chinese_invisible,word);//获取当前位置的word，这样在onBindViewHolder方法外面也可以通过getTag方法和id_for_chinese_invisible获取。
        holder.textView_number.setText(String.valueOf(position+1));
        holder.textView_english.setText(word.getWord());
        holder.textView_chinese.setText(word.getChineseMeaning());
        if(word.isChineseInvisible()){//设置中文可见性
            holder.textView_chinese.setVisibility(View.GONE);//消失
            holder.switchInvisible.setChecked(true);
        }else{
            holder.textView_chinese.setVisibility(View.VISIBLE);//可见
            holder.switchInvisible.setChecked(false);
        }
    }



    @Override
    public int getItemCount() {
        return AllWords.size();
    }
}
