package com.auribises.volleydemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ishantkumar on 11/11/17.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    Context context;
    int resource;
    ArrayList<User> userList;

    public UsersAdapter(Context context, int resource, ArrayList<User> userList){
        this.context = context;
        this.resource = resource;
        this.userList = userList;
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(resource,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UsersAdapter.ViewHolder holder, int position) {

        User user = userList.get(position);
        holder.imageView.setBackgroundResource(R.drawable.ic_boy);
        holder.txtName.setText(user.getName());
        holder.txtEmail.setText(user.getEmail());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    // To initialize the views
    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView txtName,txtEmail;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            txtName = itemView.findViewById(R.id.textViewName);
            txtEmail = itemView.findViewById(R.id.textViewEmail);
        }
    }
}
