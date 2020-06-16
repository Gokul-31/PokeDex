package com.example.android.pokedexproper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder> implements Filterable {
    private List<PokeName> pNames;
    private List<PokeName> pNamesFull;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public static class RecycleViewHolder extends RecyclerView.ViewHolder {

        public TextView nameView;
        public RecycleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            nameView=itemView.findViewById(R.id.pokeName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecycleAdapter(ArrayList<PokeName> pNamesP){
        pNames=pNamesP;
        pNamesFull=new ArrayList<>(pNamesP);
    }

    @NonNull
    @Override
    public RecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_lay,parent,false);
        RecycleViewHolder rvh= new RecycleViewHolder(v,mListener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewHolder holder, int position) {
        PokeName currItem = pNames.get(position);
        holder.nameView.setText(currItem.name);
    }

    @Override
    public int getItemCount() {
        return pNames.size();
    }

    @Override
    public Filter getFilter() {
        return exFilter;
    }

    private Filter exFilter = new Filter() {
        //background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PokeName> filteredList=new ArrayList<>();

            if(constraint==null||constraint.length()==0){
                filteredList.addAll(pNamesFull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PokeName p : pNamesFull) {
                    if (p.name.toLowerCase().contains(filterPattern)){
                        filteredList.add(p);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pNames.clear();
            pNames.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
