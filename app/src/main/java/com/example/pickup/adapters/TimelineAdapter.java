package com.example.pickup.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickup.R;
import com.example.pickup.enums.Availability;
import com.example.pickup.models.ParseEvent;
import com.example.pickup.models.ParseUserToEvent;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder>{

    private Context context;
    private List<Pair<ParseUserToEvent, Integer>> userToEvents;

    private static final String TAG = "TimelineAdapter";
    public static final String AVAILABILITY_GOING = "Going";
    public static final String AVAILABILITY_MAYBE = "Maybe";
    public static final String AVAILABILITY_NO = "No";
    public static final String AVAILABILITY_NOT_SPECIFIED = "Not Specified";

    public TimelineAdapter(Context context, List<Pair<ParseUserToEvent, Integer>> userToEvents) {
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
        Pair<ParseUserToEvent, Integer> userToEvent = userToEvents.get(position);
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
    public void addAll(List<Pair<ParseUserToEvent, Integer>> list) {
        userToEvents.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvDate;
        private TextView tvTime;
        private TextView tvLocation;
        private TextView tvDistance;
        private TextView tvEventSport;
        private ImageButton btnAvailability;
        private Availability currentAvailability;
        private Availability nextAvailability;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvEventSport = itemView.findViewById(R.id.tvEventSport);
            btnAvailability = itemView.findViewById(R.id.btnAvailability);
            tvDistance = itemView.findViewById(R.id.tvDistance);

            itemView.setOnClickListener(this);
        }

        public void bind(final Pair<ParseUserToEvent, Integer> userToEventDistancePair) {

            final ParseUserToEvent userToEvent = userToEventDistancePair.first;
            ParseEvent event = userToEvent.getEvent();

            // Bind the post data to the view elements
            tvTime.setText(event.getTime());
            tvEventSport.setText(event.getSport());
            tvDistance.setText(userToEventDistancePair.second.toString() + " miles away");

            currentAvailability = getCurrentAvailability(userToEvent.getAvailability());

            setAvailability(btnAvailability, currentAvailability);

            btnAvailability.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View view) {
                    nextAvailability = getNextAvailability(currentAvailability);
                    setAvailability(btnAvailability, nextAvailability);
                    saveAvailability(userToEvent, currentAvailability.text());
                    updateAvailability();
                }
            });
        }

        private Availability getCurrentAvailability(String availability) {
            if (availability.equals("Going")) {
                return Availability.GOING;
            }
            else if (availability.equals("Maybe")) {
                return Availability.MAYBE;
            }
            else if (availability.equals("No")){
                return Availability.NO;
            }
            else {
                return Availability.NA;
            }
        }

        private Availability getNextAvailability(Availability availability) {
            if (availability == Availability.GOING) {
                return Availability.MAYBE;
            }
            else if (availability == Availability.MAYBE) {
                return Availability.NO;
            }
            else if (availability == Availability.NO){
                return Availability.GOING;
            }
            else {
                return Availability.GOING;
            }
        }

        private void setAvailability(ImageButton btnAvailability, Availability nextAvailability) {
            if (nextAvailability == Availability.NA) {
                btnAvailability.setBackground(context.getDrawable(R.drawable.button_not_specified));
                currentAvailability = Availability.NA;
            }
            else if (nextAvailability == Availability.GOING) {
                btnAvailability.setBackground(context.getDrawable(R.drawable.button_going));
                currentAvailability = Availability.GOING;
            }
            else if (nextAvailability == Availability.MAYBE){
                btnAvailability.setBackground(context.getDrawable(R.drawable.button_maybe));
                currentAvailability = Availability.MAYBE;
            }
            else {
                btnAvailability.setBackground(context.getDrawable(R.drawable.button_no));
                currentAvailability = Availability.NO;
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
            int adapterPosition = getAdapterPosition();
            userToEvents.remove(adapterPosition);
            notifyItemRemoved(adapterPosition);
        }

        @Override
        public void onClick(View view) {
            // make sure the position is valid, i.e. actually exists in the view
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                // get the post at the position, this won't work if the class is static
                Log.i(TAG, "onClick: " + getAdapterPosition());
            }
        }
    }
}
