package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Fragment.VideoHotAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterComment  extends RecyclerView.Adapter<AdapterComment.Viewholder> {

    List<String> list;

    public AdapterComment(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.comment_item,parent,false);

        AdapterComment.Viewholder viewholder = new AdapterComment.Viewholder(view);

        return  viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        final  String a ;
        a = list.get(position);
        holder.tvcomment.setText(a);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        TextView tvcomment;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvcomment = itemView.findViewById(R.id.tvcomment);
            tvcomment.setSelected(true);


        }
    }
}
