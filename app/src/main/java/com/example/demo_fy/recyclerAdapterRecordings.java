package com.example.demo_fy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapterRecordings extends RecyclerView.Adapter<recyclerAdapterRecordings.MyViewHolder> {
    private ArrayList<Session> sessions; //temporarily holds strings for testing purposes

    public recyclerAdapterRecordings(ArrayList<Session> sessions){
        this.sessions = sessions;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView sessionTxt;

        public MyViewHolder(final View view) {
            super(view);
            sessionTxt = view.findViewById(R.id.sessionName);
        }
    }



    @NonNull
    @Override
    public recyclerAdapterRecordings.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapterRecordings.MyViewHolder holder, int position) {
        String session = sessions.get(position).getName();
        holder.sessionTxt.setText(session);
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }
}
