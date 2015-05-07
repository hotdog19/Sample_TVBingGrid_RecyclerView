package jp.co.fenrir.tvbing_sample;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VerticalItemHolder> {

    private ArrayList<TVProgram> mItems;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public SimpleAdapter() {
        mItems = new ArrayList<>();
    }

    /*
     * A common adapter modification or reset mechanism. As with ListAdapter,
     * calling notifyDataSetChanged() will trigger the RecyclerView to update
     * the view. However, this method will not trigger any of the RecyclerView
     * animation features.
     */
    public void setItemCount() {
        mItems.clear();
        mItems.addAll(DataManager.getSharedInstance().getLists());

        notifyDataSetChanged();
    }

    /*
     * Inserting a new item at the head of the list. This uses a specialized
     * RecyclerView method, notifyItemInserted(), to trigger any enabled item
     * animations in addition to updating the view.
     */
    public void addItem(int position) {
        if (position >= mItems.size()) return;
        
//        mItems.add(position, generateDummyItem());
        notifyItemInserted(position);
    }

    /*
     * Inserting a new item at the head of the list. This uses a specialized
     * RecyclerView method, notifyItemRemoved(), to trigger any enabled item
     * animations in addition to updating the view.
     */
    public void removeItem(int position) {
        if (position >= mItems.size()) return;

        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {

        //Log.e("TTT","onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.view_tv_program, container, false);

        return new VerticalItemHolder(root, this);
    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {

        //Log.e("TTT","onBindViewHolder");

        TVProgram item = mItems.get(position);

        itemHolder.setTextId(item.getId());
        itemHolder.setTitle(String.valueOf(item.getTitle()));
        itemHolder.setSummary(String.valueOf(item.getSummary()));

        ZYFixedGridLayoutManager.LayoutParams lp = (ZYFixedGridLayoutManager.LayoutParams)itemHolder.itemView.getLayoutParams();




        lp.height = DataManager.ITEM_HEIGHT * item.getTimeOffset();
        lp.startRow = item.getMatrixStartRow();
        lp.endRow = item.getMatrixEndRow();
        lp.column = item.getMatrixColumn();
        lp.position = item.getTrueDataPosition();
        //lp.height = 20;
        itemHolder.itemView.setLayoutParams(lp);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class VerticalItemHolder extends RecyclerView.ViewHolder {
        private TextView textId;
        private TextView textTitle;
        private TextView textSummary;

        public VerticalItemHolder(View itemView, SimpleAdapter adapter) {
            super(itemView);

            textId = (TextView) itemView.findViewById(R.id.text_id);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            textSummary = (TextView) itemView.findViewById(R.id.text_summary);

        }

        public void setTextId(String id) {
            this.textId.setText(id);
        }

        public void setTitle(String title){
            textTitle.setText(title);
        }

        public void setSummary(String summary){
            textSummary.setText(summary);
        }
    }

//    public static TVProgram generateDummyItem() {
//        Random random = new Random();
//        return new TVProgram(String.valueOf(random.nextInt()),"TVBing", "Bing",1,5);
//    }




}


