package com.example.myapplication.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Category;
import com.example.myapplication.Interface.IonclickCTGR;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {

    List<Category> list = new ArrayList<>();
    IonclickCTGR ionclickCTGR;

    public CategoryAdapter(List<Category> list) {
        this.list = list;
    }

    void setonClickCTGR(IonclickCTGR ionclickCTGR){
        this.ionclickCTGR = ionclickCTGR;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.category_item,parent,false);
        Viewholder viewholder = new Viewholder(view);


        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        final Category category = list.get(position);
        switch (position){
            case 0 :
                Picasso.get().load(category.getThumb()).fit().into(holder.img);
                break;
            case 1 :
                Picasso.get().load(R.drawable.ctgr_1).fit().into(holder.img);
                break;
            case 2 :
                Picasso.get().load(R.drawable.ctgr_2).fit().into(holder.img);
                break;
            case 3 :
                Picasso.get().load(category.getThumb()).fit().into(holder.img);
                break;
            case 4 :
                Picasso.get().load(R.drawable.ctgr_4).fit().into(holder.img);
                break;
            case 5 :
                Picasso.get().load(R.drawable.ctgr_5).fit().into(holder.img);
                break;
            case 6 :
                Picasso.get().load(R.drawable.ctgr_6).fit().into(holder.img);
                break;
            case 7 :
                Picasso.get().load(R.drawable.ctgr_7).fit().into(holder.img);
//                holder.img.setImageResource(R.drawable.ctgr_7);
                break;
            case 8 :
                Picasso.get().load(category.getThumb()).fit().into(holder.img);
                break;
            case 9 :
                Picasso.get().load(category.getThumb()).fit().into(holder.img);
                break;


        }

        holder.tv.setText(category.getCategory());

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionclickCTGR.onClickCTGR(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    class Viewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

             img = itemView.findViewById(R.id.img_ctgr);
             tv = itemView.findViewById(R.id.tv_ctgr_name);
        }
    }
}