package com.anonlim.yolo5v;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anonlim.yolo5v.databinding.OptionsItemBinding;

import java.util.ArrayList;
import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder> {

    public SparseBooleanArray checkBoxStatus = new SparseBooleanArray();
    public List<String> mItems;
    public OptionsAdapter(List<String> items){
        this.mItems = items;
    }

    @NonNull
    @Override
    public OptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        OptionsItemBinding binding = OptionsItemBinding.inflate(inflater, parent, false);
        return new OptionsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public List<String> getCheckedItems(){
        List<String> result = new ArrayList<>();
        for (int i=0; i<checkBoxStatus.size(); i++){
            boolean isChecked = checkBoxStatus.valueAt(i);
            if (isChecked){
                String checkedItem = mItems.get(checkBoxStatus.keyAt(i));
                result.add(checkedItem);
            }
        }

        return result;
    }

    public class OptionsViewHolder extends RecyclerView.ViewHolder {
        private final OptionsItemBinding mBinding;
        public OptionsViewHolder(OptionsItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v;
                    checkBoxStatus.put(getAdapterPosition(), checkBox.isChecked());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

        public void bind(String item){
            mBinding.setText(item);
            mBinding.itemButton.setChecked(checkBoxStatus.get(getAdapterPosition()));
        }
    }

}
