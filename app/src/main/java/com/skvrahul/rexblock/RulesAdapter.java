package com.skvrahul.rexblock;

import android.content.Context;
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
    public interface DeleteClickListener{
        void onItemClick(Rule rule);
    }
    public RulesAdapter(List<Rule> rules, DeleteClickListener deleteClickListener){
        this.rules = rules;
        this.deleteClickListener=deleteClickListener;
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
        public TextView activeTV;
        public TextView nameTV;
        public ImageView deleteIV;
        public ViewHolder(View view ){
            super(view);
            initializeViews(view);
        }
        public void onBind(final Rule rule){
            ruleTV.setText(rule.getRegex());
            if(rule.isActive()){
                activeTV.setText("Active");
                activeTV.setTextColor(context.getResources().getColor(R.color.green));
            }else{
                activeTV.setText("Active");
                activeTV.setTextColor(context.getResources().getColor(R.color.red));
            }
            deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(deleteClickListener!=null){
                        deleteClickListener.onItemClick(rule);
                    }
                }
            });
            if(rule.getName().length()>0){
                nameTV.setText(rule.getName());
            }
        }
        public void initializeViews(View view){
            ruleTV = (TextView)view.findViewById(R.id.regex_rule_text_view);
            activeTV = (TextView)view.findViewById(R.id.regex_rule_active_text_view);
            deleteIV = (ImageView)view.findViewById(R.id.regex_rule_delete_image_view);
            nameTV = (TextView)view.findViewById(R.id.regex_name_text_view);
        }
    }

}
