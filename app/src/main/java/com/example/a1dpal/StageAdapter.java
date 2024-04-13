package com.example.a1dpal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StageAdapter extends RecyclerView.Adapter<StageAdapter.MyViewHolder> {

    Context context;
    ArrayList<LocalStorage> localStorage;
    public StageAdapter(Context context, ArrayList<LocalStorage> localStorage){
        this.context = context;
        this.localStorage = localStorage;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // layout of each Card is inflated
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview, viewGroup, false);

        return new StageAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StageAdapter.MyViewHolder holder, int position) {
        // assign values to the view in recyclerview layout
        // based on position of recycler view
        holder.textView.setText(localStorage.get(position).getLocation());
        holder.imageView.setImageResource(localStorage.get(position).getImage());
        holder.imageView.setTag(localStorage.get(position).getImage());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.imageView.getContext(), NPCActivity.class);
                i.putExtra("image", holder.imageView.getTag().toString());
                holder.imageView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // show items displayed
        return localStorage.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{
        // take view from recyclerview layout

        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cardViewImage);
            textView = itemView.findViewById(R.id.cardViewText);
        }
    }


}