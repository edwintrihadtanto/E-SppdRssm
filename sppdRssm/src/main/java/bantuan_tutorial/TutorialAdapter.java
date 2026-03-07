package bantuan_tutorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.SliderViewHolder> {

    private final int[] layouts;

    public TutorialAdapter(int[] layouts) {
        this.layouts = layouts;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layouts[viewType], parent, false);

        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return layouts.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {

        public SliderViewHolder(View itemView) {
            super(itemView);
        }
    }
}