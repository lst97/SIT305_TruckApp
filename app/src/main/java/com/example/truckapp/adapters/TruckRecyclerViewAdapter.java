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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truckapp.R;
import com.example.truckapp.activities.OrderViewerActivity;
import com.example.truckapp.controllers.OrderController;
import com.example.truckapp.controllers.ServicesController;
import com.example.truckapp.models.order.Order;
import com.example.truckapp.models.truck.Truck;
import com.example.truckapp.services.cookie.CookieService;
import com.example.truckapp.utils.ImageUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TruckRecyclerViewAdapter extends RecyclerView.Adapter<TruckRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Truck> trucks;
    ServicesController servicesController;

    public TruckRecyclerViewAdapter(Context context, List<Truck> trucks) {
        this.context = context;
        this.trucks = trucks;
        servicesController = ServicesController.getInstance();
    }

    @NonNull
    @Override
    public TruckRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_avaliable_truck, parent, false);
        return new TruckRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TruckRecyclerViewAdapter.ViewHolder holder, int position) {
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

        // set card on click listener
        holder.itemView.setOnClickListener(v -> {
            OrderController orderController = OrderController.getInstance();
            CookieService cookieService = (CookieService) servicesController.getService("CookieService");
            List<Order> orders = orderController.getOrders(cookieService.getUserSession());

            // check if the truck is not available/booked by the user
            for (Order order : orders) {
                if (order.getVehicleId() == trucks.get(position).getId()) {
                    Intent intent = new Intent(context, OrderViewerActivity.class);
                    intent.putExtra("truckId", trucks.get(position).getId());
                    context.startActivity(intent);
                    return;
                }
            }

            // little message
            CharSequence truckName = trucks.get(position).getName();
            CharSequence text = truckName + " is available, book by clicking the '+' button";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
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
