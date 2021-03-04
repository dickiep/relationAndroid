package com.dickies.android.relationbn.productdisplay;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dickies.android.relationbn.R;
import com.dickies.android.relationbn.utils.Category;

import java.util.ArrayList;

/**
 * Created by Phil on 28/07/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    ArrayList<Category> categoryList = new ArrayList<>();
    Context ctx;

    public CategoryAdapter(ArrayList<Category> categoryList, Context ctx) {
        this.categoryList = categoryList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_row_layout, parent, false);
        CategoryAdapter.CategoryViewHolder categoryViewHolder = new CategoryAdapter.CategoryViewHolder(view, ctx, categoryList);

        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.title.setText(category.getName());
        holder.description.setText(category.getDescription());

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    /**
     *
     */
    static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //ImageView imageView;
        TextView title, description;
        ArrayList<Category> categories = new ArrayList<>();
        Context ctx;

        public CategoryViewHolder(View view, Context ctx, ArrayList<Category> categories) {
            super(view);
            this.categories = categories;
            this.ctx = ctx;
            view.setOnClickListener(this);
            //imageView = view.findViewById(R.id.imageView);
            title = view.findViewById(R.id.categoryTitle);
            description = view.findViewById(R.id.categoryDescription);

        }

        @Override
        public void onClick(View view) {

            final String TAG = "CategoryAdapter";
            int position = getAdapterPosition();
            Category category = this.categories.get(position);
            int id = category.getId();
            String title = category.getName();
            Intent intent  = new Intent(ctx, CategoryProductsDisplayActivity.class);
            intent.putExtra("categoryID", id);
            intent.putExtra("categoryTitle",title);
            ctx.startActivity(intent);

        }


    }

}

