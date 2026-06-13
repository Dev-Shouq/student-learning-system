package com.example.systemnews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

/**
 * ResourceAdapter: Bridge between the data (Resource list) and the UI (RecyclerView).
 * Responsible for creating view holders and binding data to individual list items.
 */
public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ViewHolder> {

    // List of resources to be displayed
    private final List<Resource> resourceList;

    public ResourceAdapter(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single item in the list
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data for the current position
        Resource resource = resourceList.get(position);
        
        // Populate the views with data
        holder.tvTitle.setText(resource.getTitle());
        holder.tvCategory.setText(resource.getCategory());
        holder.tvUrl.setText(resource.getUrl());
        holder.tvDescription.setText(resource.getDescription());
        
        // Update the rating and comments display for this specific resource
        updateUI(holder, resource); 

        // Set up the click listener for the feedback submission button
        holder.btnSubmit.setOnClickListener(v -> {
            String commentText = holder.etComment.getText().toString().trim();
            float userRating = holder.ratingBarInput.getRating();

            // Ensure the user has selected a rating before submitting
            if (userRating == 0) {
                Toast.makeText(v.getContext(), "Please select stars to rate!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the data model with new feedback
            resource.addUserFeedback(commentText, userRating);

            // Reset the input fields
            holder.etComment.setText("");
            holder.ratingBarInput.setRating(0);

            // Refresh the UI to reflect the new average rating and comment
            updateUI(holder, resource); 
            Toast.makeText(v.getContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * updateUI: Utility method to refresh the rating indicators and review list for an item.
     */
    private void updateUI(ViewHolder holder, Resource resource) {
        // Display the current average rating as stars
        holder.ratingBarDisplay.setRating(resource.getAverageRating());
        
        // Compile and display the list of all user reviews
        List<Resource.UserFeedback> feedbacks = resource.getFeedbacks();
        if (feedbacks.isEmpty()) {
            holder.tvCommentsDisplay.setText("No comments yet. Be the first to rate!");
        } else {
            StringBuilder sb = new StringBuilder("User Reviews:\n");
            for (Resource.UserFeedback feedback : feedbacks) {
                // Represent the rating using star emojis
                for (int i = 0; i < (int)feedback.getRating(); i++) sb.append("⭐");
                if (!feedback.getComment().isEmpty()) sb.append(" - ").append(feedback.getComment());
                sb.append("\n");
            }
            holder.tvCommentsDisplay.setText(sb.toString().trim());
        }
    }

    @Override
    public int getItemCount() {
        // Total number of items in the list
        return resourceList.size();
    }

    /**
     * ViewHolder: Inner class that holds references to all views within a single list item layout.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvUrl, tvDescription, tvCommentsDisplay;
        RatingBar ratingBarDisplay, ratingBarInput;
        EditText etComment;
        Button btnSubmit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Locate each UI component by its ID
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvUrl = itemView.findViewById(R.id.tv_url);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvCommentsDisplay = itemView.findViewById(R.id.tv_comments_display);
            ratingBarDisplay = itemView.findViewById(R.id.rating_bar);
            ratingBarInput = itemView.findViewById(R.id.rating_bar_input);
            etComment = itemView.findViewById(R.id.et_comment);
            btnSubmit = itemView.findViewById(R.id.btn_submit);
        }
    }
}
