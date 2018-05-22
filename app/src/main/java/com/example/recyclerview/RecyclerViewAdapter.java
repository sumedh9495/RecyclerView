package com.example.recyclerview;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private final ArrayList<NewContact> mContacts;

    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<NewContact> contacts) {
        mContacts = contacts;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "-----> onBindViewHolder: called");

        //  Glide.with(mContext)
        //         .asBitmap()
        //          .load(mImages.get(position))
        //           .into(holder.image);

        final NewContact contact = mContacts.get(position);

        holder.firstname.setText(contact.getName());
        //holder.lastname.setText(contact.getLastName());
        holder.number.setText(contact.getNumber());


        holder.image.setImageURI(Uri.parse(contact.getPhoto()));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "---->" + contact.getName());

                Intent intent = new Intent(mContext, ContactDetails.class);
                intent.putExtra("firstName", contact.getName());
                intent.putExtra("lastName", contact.getEmail());
                intent.putExtra("Mobile", contact.getNumber());
                intent.putExtra("image", contact.getPhoto());
                intent.putExtra("contactid",contact.getContactid());
                mContext.startActivity(intent);

            }
        });


        holder.Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = contact.getNumber();
                 Intent i=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + number));
                mContext.startActivity(i);



            }
        });

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        CircleImageView image;
        TextView firstname;
        TextView lastname;
        TextView number;
        ImageView Call;

        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            firstname = itemView.findViewById(R.id.firstName);
         //   lastname = itemView.findViewById(R.id.lastName);
            number = itemView.findViewById(R.id.number);
            Call = itemView.findViewById(R.id.imageView_call);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
