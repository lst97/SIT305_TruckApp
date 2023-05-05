package com.example.truckapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truckapp.R;
import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.utils.ImageUtil;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Truck> trucks;

    public HomeRecyclerViewAdapter(Context context, List<Truck> trucks) {
        this.context = context;
        this.trucks = trucks;
    }

    @NonNull
    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_avaliable_truck, parent, false);
        return new HomeRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.titleView.setText(trucks.get(position).getName());
        holder.descriptionView.setText(trucks.get(position).getDescription());
        Bitmap bm = ImageUtil.decodeImage(trucks.get(position).getImage());
        holder.imageView.setImageBitmap(bm);
        holder.shareBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Check out this truck: " + trucks.get(position).getName());
            context.startActivity(Intent.createChooser(intent, "Share with"));
        });
    }

    @Override
    public int getItemCount() {
        return trucks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleView;
        TextView descriptionView;
        Button shareBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recycler_truck_image);
            titleView = itemView.findViewById(R.id.recycler_truck_name_label);
            descriptionView = itemView.findViewById(R.id.recycler_truck_description_label);
            shareBtn = itemView.findViewById(R.id.recycler_truck_share);
        }
    }
}
