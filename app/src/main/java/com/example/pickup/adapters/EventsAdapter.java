package com.example.pickup.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickup.R;
import com.example.pickup.models.EventParse;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

    private Context context;
    private List<EventParse> events;

    private static final String TAG = "EventsAdapter";

    public EventsAdapter(Context context, List<EventParse> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventParse event = events.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        events.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<EventParse> list) {
        events.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvEventTime);

            itemView.setOnClickListener(this);
        }

        public void bind(EventParse event) {
            // Bind the post data to the view elements
            tvTime.setText(event.getTime());
        }

        @Override
        public void onClick(View view) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the post at the position, this won't work if the class is static
                Log.i(TAG, "onClick: " + position);
            }
        }
    }
}
