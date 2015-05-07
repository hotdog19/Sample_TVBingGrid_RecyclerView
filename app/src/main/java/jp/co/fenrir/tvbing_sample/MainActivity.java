package jp.co.fenrir.tvbing_sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private RecyclerView recyclerView;
//    private VariableGridLayoutManager gridLayoutManager;
    //private _FixedGridLayoutManager gridLayoutManager;
    private ZYFixedGridLayoutManager gridLayoutManager;
    private SimpleAdapter simpleAdapter;

    // //// header
    private HorizontalScrollView hsl;
    private LinearLayout headerLL;

    // //// time
    private ScrollView vsl;
    private LinearLayout timelineLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hsl = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        headerLL = (LinearLayout)findViewById(R.id.headerLL);

        vsl = (ScrollView)findViewById(R.id.verticalScrollView);
        timelineLL = (LinearLayout)findViewById(R.id.timelineLL);
        DataManager.ITEM_HEIGHT = dip2px(this.getApplicationContext(), DataManager.ITEM_HEIGHT_DP) ;

        // populate header and timeline data
        for (int i = 0; i < DataManager.COLUMES; i++) {

            TextView tv = new TextView(this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(240, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setText("hecheng:" + i);
            tv.setTextColor(Color.WHITE);

            headerLL.addView(tv);
        }

        int timeHight = DataManager.ROWS * DataManager.ITEM_HEIGHT / 24;
        for (int i = 0; i < 24; i++) {

            TextView tv = new TextView(this);
            tv.setLayoutParams(new ViewGroup.LayoutParams(25, timeHight));
            tv.setText(String.valueOf(i));
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(12);
            tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            timelineLL.addView(tv);

            View v = new View(this);
            v.setLayoutParams(new ViewGroup.LayoutParams(25,1));
            v.setBackgroundColor(Color.WHITE);

            timelineLL.addView(v);

        }

        hsl.setBackgroundColor(Color.RED);

        vsl.setBackgroundColor(Color.BLUE);
        vsl.setAlpha(.8f);

        timelineLL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Log.e("TT","hechengmenhaha");
                return false;
            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.view);
//        gridLayoutManager = new VariableGridLayoutManager();
        //gridLayoutManager = new _FixedGridLayoutManager();
        gridLayoutManager = new ZYFixedGridLayoutManager();
        gridLayoutManager.setTotalColumnCount(DataManager.COLUMES);

        InsetDecoration insetDecoration = new InsetDecoration(this);
        recyclerView.addItemDecoration(insetDecoration);

        recyclerView.setLayoutManager(gridLayoutManager);

        simpleAdapter = new SimpleAdapter();
        simpleAdapter.setItemCount();
        recyclerView.setAdapter(simpleAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        //Log.e("TT","hehe you have clicked:" + position);
                        view.setBackgroundColor(Color.CYAN);
                    }
                })
        );

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                hsl.scrollBy(dx, 0);
                vsl.scrollBy(0, dy);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }
    }


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


}
