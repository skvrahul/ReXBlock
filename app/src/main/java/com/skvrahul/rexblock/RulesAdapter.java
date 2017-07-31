package com.skvrahul.rexblock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skvrahul on 29/7/17.
 */

public class RulesAdapter extends RecyclerView.Adapter<RulesAdapter.ViewHolder> {
    private List<Rule> rules = new ArrayList<>();
    private Context context;
    private DeleteClickListener deleteClickListener;
    private ActiveClickListener activeClickListener;
    public interface DeleteClickListener{
        void onItemClick(Rule rule);
    }
    public interface ActiveClickListener{
        void onItemClick(Rule rule);
    }
    public RulesAdapter(List<Rule> rules, DeleteClickListener deleteClickListener, ActiveClickListener activeClickListener){
        this.rules = rules;
        this.deleteClickListener=deleteClickListener;
        this.activeClickListener =activeClickListener;
    }

    @Override
    public RulesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rule, parent, false);
        context = parent.getContext();
        return new  ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Rule rule = rules.get(position);
        holder.onBind(rule);
    }

    @Override
    public int getItemCount() {
        return rules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView ruleTV;
        public ImageView activeIV;
        public TextView nameTV;
        public ImageView deleteIV;
        public ViewHolder(View view ){
            super(view);
            initializeViews(view);
        }
        public void onBind(final Rule rule){
            ruleTV.setText(rule.getRegex());
            if(rule.isActive()){
                activeIV.setImageResource(R.drawable.ic_check_green_24dp);
                activeIV.setTag("ACTIVE");

            }else{
                activeIV.setImageResource(R.drawable.ic_cross_red_24dp);
                activeIV.setTag("INACTIVE");


            }
            deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteClickListener!=null){
                        deleteClickListener.onItemClick(rule);
                    }
                }
            });
            activeIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(activeClickListener!=null){
                        activeClickListener.onItemClick(rule);
                    }
                    if(activeIV.getTag()=="ACTIVE"){
                        activeIV.setImageResource(R.drawable.ic_cross_red_24dp);
                        activeIV.setTag("INACTIVE");
                    }else{
                        activeIV.setImageResource(R.drawable.ic_check_green_24dp);
                        activeIV.setTag("ACTIVE");
                    }
                }
            });
            if(rule.getName().length()>0){
                nameTV.setText(rule.getName());
            }
        }
        public void initializeViews(View view){
            ruleTV = (TextView)view.findViewById(R.id.regex_rule_text_view);
            activeIV = (ImageView) view.findViewById(R.id.regex_rule_active_image_view);
            deleteIV = (ImageView)view.findViewById(R.id.regex_rule_delete_image_view);
            nameTV = (TextView)view.findViewById(R.id.regex_name_text_view);
        }
    }

}
