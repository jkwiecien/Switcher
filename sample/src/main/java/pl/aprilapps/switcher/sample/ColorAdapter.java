package pl.aprilapps.switcher.sample;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Jacek Kwiecień on 08.10.2015.
 */
public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    private enum Case {
        PROGRESS, ERROR, EMPTY
    }

    private MainActivity activity;

    public ColorAdapter(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.view_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (position) {
            case 0:
                holder.sampleCase = Case.PROGRESS;
                holder.itemView.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_green_light));
                holder.label.setText("TEST PROGRESS");
                break;
            case 1:
                holder.sampleCase = Case.ERROR;
                holder.itemView.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_red_light));
                holder.label.setText("TEST ERROR");
                break;
            default:
                holder.sampleCase = Case.EMPTY;
                holder.itemView.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_orange_light));
                holder.label.setText("TEST EMPTY PLACEHOLDER");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Case sampleCase;
        protected TextView label;

        public ViewHolder(View itemView) {
            super(itemView);

            label = itemView.findViewById(R.id.label);

            itemView.findViewById(R.id.cell).setOnClickListener(view -> {
                switch (sampleCase) {
                    case PROGRESS:
                        activity.onProgress();
                        break;
                    case ERROR:
                        activity.onError();
                        break;
                    default:
                        activity.onEmpty();
                        break;
                }
            });
        }
    }
}
