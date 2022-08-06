package com.miniproject.pickupalarm.Adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.miniproject.pickupalarm.Interfaces.ContactSelect;
import com.miniproject.pickupalarm.Models.ContactModel;
import com.miniproject.pickupalarm.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private List<ContactModel> contactModelList;
    private ContactSelect contactSelect;
    private boolean globalSelected;

    public ContactAdapter(List<ContactModel> contactModelList, boolean globalSelected) {
        this.contactModelList = contactModelList;
        this.globalSelected = globalSelected;
    }

    public ContactAdapter(List<ContactModel> contactModelList, boolean globalSelected, ContactSelect contactSelect) {
        this.contactModelList = contactModelList;
        this.contactSelect = contactSelect;
        this.globalSelected = globalSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_number_item, parent, false);
        return new ViewHolder(root);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(contactModelList.get(position).getName());
        holder.number.setText(contactModelList.get(position).getMobileNumber());
        holder.photo.setText(contactModelList.get(position).getName().substring(0,1));
        holder.itemView.setSelected(contactModelList.get(position).isSelected());
        if (globalSelected)
            holder.itemView.setSelected(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {
                if (contactSelect != null)
                    contactSelect.onSelect(position, !contactModelList.get(position).isSelected());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView photo;
        private TextView name;
        private TextView number;

        private CardView contactCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            contactCard = itemView.findViewById(R.id.contact_card);
            contactCard.setBackgroundResource(R.drawable.contact_item_background);
        }
    }
}
