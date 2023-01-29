package com.itsolution.car_owner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class adapter_to_get_data_OwnerDashboard extends RecyclerView.Adapter<adapter_to_get_data_OwnerDashboard.ViewHolder> {

    Context context;
    List<model_for_owner_dash> modellist;

    public adapter_to_get_data_OwnerDashboard(Context context, List<model_for_owner_dash> modellist) {
        this.context = context;
        this.modellist = modellist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        model_for_owner_dash model_for_owner_dash_variable=modellist.get(position);
        holder.model_.setText(model_for_owner_dash_variable.getCar_model());
        holder.location_.setText(model_for_owner_dash_variable.getLocation());
        holder.price_.setText(model_for_owner_dash_variable.getPrice()+" Taka");
        Picasso.get().load(model_for_owner_dash_variable.getImg_1()).into(holder.img);


    }

    @Override
    public int getItemCount() {
        return modellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView model_,price_,location_;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            model_=itemView.findViewById(R.id.car_model);
            price_=itemView.findViewById(R.id.car_price);
            location_=itemView.findViewById(R.id.car_location);
            img=itemView.findViewById(R.id.car_img);
        }
    }
}
