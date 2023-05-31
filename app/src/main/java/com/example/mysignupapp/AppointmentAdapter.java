package com.example.mysignupapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder>
{
    Context context;

    List<Appointment> appointments;

    FirebaseDatabase db;

    FirebaseAuth mAuth;

    String firebase_link;

    private AppointmentAdapter.AppointmentClickListener listener;

    public AppointmentAdapter(Context context, List<Appointment> appointments, AppointmentAdapter.AppointmentClickListener listener)
    {
        this.context = context;
        this.appointments = appointments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_item, parent, false);
        return new AppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        Appointment chosen_appointment = appointments.get(position);

        String current_id = mAuth.getCurrentUser().getUid();
        String sender_id = chosen_appointment.getSender_id();
        String receiver_id = chosen_appointment.getReceiver_id();

        DatabaseReference user_ref1 = db.getReference("Users/" + sender_id);
        DatabaseReference user_ref2 = db.getReference("Users/" + receiver_id);

        if(current_id.equals(sender_id))
        {
            user_ref2.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    User user_now = snapshot.getValue(User.class);

                    if(user_now.getProfile_picture() != null)
                    {
                        if(!user_now.getProfile_picture().isEmpty())
                        {
                            firebase_link = user_now.getProfile_picture();
                            Picasso.get().load(firebase_link).into(holder.profile_picture_switcher);
                        }
                    }

                    holder.switcher_name.setText(user_now.getUsername());
                    String date = chosen_appointment.getRequest().getWhen();
                    holder.when.setText(date);
                    String ad_of_interest = chosen_appointment.getRequest().getAbout().get("Title").toString();
                    holder.interested_for.setText(ad_of_interest);

                    if(chosen_appointment.getRequest().getPrice_offer() != null)
                    {
                        String price_trade = chosen_appointment.getRequest().getPrice_offer();
                        holder.trade_with.setText(price_trade);
                    }
                    else if(chosen_appointment.getRequest().getTrade() != null)
                    {
                        String ad_trade = chosen_appointment.getRequest().getTrade().get("Title").toString();
                        holder.trade_with.setText(ad_trade);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }
        else if(current_id.equals(receiver_id))
        {
            user_ref1.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    User user_now = snapshot.getValue(User.class);

                    if(user_now.getProfile_picture() != null)
                    {
                        if(!user_now.getProfile_picture().isEmpty())
                        {
                            firebase_link = user_now.getProfile_picture();
                            Picasso.get().load(firebase_link).into(holder.profile_picture_switcher);
                        }
                    }

                    holder.switcher_name.setText(user_now.getUsername());
                    String date = chosen_appointment.getRequest().getWhen();
                    holder.when.setText(date);
                    String ad_of_interest = chosen_appointment.getRequest().getAbout().get("Title").toString();
                    holder.trade_with.setText(ad_of_interest);

                    if(chosen_appointment.getRequest().getPrice_offer() != null)
                    {
                        String price_trade = chosen_appointment.getRequest().getPrice_offer();
                        holder.interested_for.setText(price_trade);
                    }
                    else if(chosen_appointment.getRequest().getTrade() != null)
                    {
                        String ad_trade = chosen_appointment.getRequest().getTrade().get("Title").toString();
                        holder.interested_for.setText(ad_trade);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {

                }
            });
        }

    }

    @Override
    public int getItemCount()
    {
        return appointments.size();
    }


    public interface AppointmentClickListener
    {
        void onClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        ImageView profile_picture_switcher;
        TextView when;
        TextView switcher_name;
        TextView interested_for;
        TextView trade_with;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            profile_picture_switcher = itemView.findViewById(R.id.switcher_profile_pic);
            when = itemView.findViewById(R.id.when_sent);
            switcher_name = itemView.findViewById(R.id.switcher_name);
            interested_for = itemView.findViewById(R.id.interested_for);
            trade_with = itemView.findViewById(R.id.trade_with);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            listener.onClick(v, getAdapterPosition());
        }
    }
}
