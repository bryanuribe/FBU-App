package com.example.pickup.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickup.R;
import com.example.pickup.models.ParseEvent;
import com.example.pickup.models.ParseUserToEvent;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder>{

    private Context context;
    private List<ParseUserToEvent> userToEvents;

    private static final String TAG = "TimelineAdapter";
    public static final String AVAILABILITY_GOING = "Going";
    public static final String AVAILABILITY_MAYBE = "Maybe";
    public static final String AVAILABILITY_NO = "No";
    public static final String AVAILABILITY_NOT_SPECIFIED = "Not Specified";

    public TimelineAdapter(Context context, List<ParseUserToEvent> userToEvents) {
        this.context = context;
        this.userToEvents = userToEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseUserToEvent userToEvent = userToEvents.get(position);
        holder.bind(userToEvent);
    }

    @Override
    public int getItemCount() {
        return userToEvents.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        userToEvents.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<ParseUserToEvent> list) {
        userToEvents.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTime;
        private TextView tvDate;
        private TextView tvEventSport;
        private ImageButton btnAvailability;
        private String currentAvailability;
        private String nextAvailability;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvEventTime);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvEventSport = itemView.findViewById(R.id.tvEventSport);
            btnAvailability = itemView.findViewById(R.id.btnAvailability);

            itemView.setOnClickListener(this);
        }

        public void bind(final ParseUserToEvent userToEvent) {

            ParseEvent event = userToEvent.getEvent();

            // Bind the post data to the view elements
            tvDate.setText(event.getDate());
            tvTime.setText(event.getTime());
            tvEventSport.setText(event.getSport());

            currentAvailability = userToEvent.getAvailability();

            setAvailability(btnAvailability, currentAvailability);

            btnAvailability.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    nextAvailability = getNextAvailability(currentAvailability);
                    setAvailability(btnAvailability, nextAvailability);
                    saveAvailability(userToEvent, currentAvailability);
                }
            });
        }

        private String getNextAvailability(String currentAvailability) {
            if (currentAvailability.equals(AVAILABILITY_NOT_SPECIFIED)) {
                return AVAILABILITY_GOING;
            }
            else if (currentAvailability.equals(AVAILABILITY_GOING)) {
                return AVAILABILITY_MAYBE;
            }
            else if (currentAvailability.equals(AVAILABILITY_MAYBE)){
                return AVAILABILITY_NO;
            }
            else {
                return AVAILABILITY_GOING;
            }
        }

        private void setAvailability(ImageButton btnAvailability, String nextAvailability) {
            if (nextAvailability.equals(AVAILABILITY_NOT_SPECIFIED)) {
                btnAvailability.setBackground(context.getDrawable(R.drawable.button_not_specified));
                currentAvailability = AVAILABILITY_NOT_SPECIFIED;
            }
            else if (nextAvailability.equals(AVAILABILITY_GOING)) {
                btnAvailability.setBackground(context.getDrawable(R.drawable.button_going));
                currentAvailability = AVAILABILITY_GOING;
            }
            else if (nextAvailability.equals(AVAILABILITY_MAYBE)){
                btnAvailability.setBackground(context.getDrawable(R.drawable.button_maybe));
                currentAvailability = AVAILABILITY_MAYBE;
            }
            else {
                btnAvailability.setBackground(context.getDrawable(R.drawable.button_no));
                currentAvailability = AVAILABILITY_NO;
            }
        }

        private void saveAvailability(ParseUserToEvent userToEvent, String currentAvailability) {
            userToEvent.setAvailability(currentAvailability);
            userToEvent.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "done: Error while saving", e);
                    }
                    Log.i(TAG, "done: Save userToEvent successful!");
                }
            });
        }

        public void updateAvailability() {
            // TODO: Only if not your events or all events
            // TODO: Animate leaving item
            userToEvents.remove(getAdapterPosition());
            notifyDataSetChanged();
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
