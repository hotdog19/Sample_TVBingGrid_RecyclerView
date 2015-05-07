package jp.co.fenrir.tvbing_sample;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.Random;

/**
 * Created by benson on 15/04/22.
 */
public class MyLayoutManager extends RecyclerView.LayoutManager {

    private int stations =50;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {

        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
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
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        detachAndScrapAttachedViews(recycler);

        // layout child views.
        // from start to end,top to bottom

        int leftOffset = getPaddingLeft();

        for (int i = 0; i < getItemCount() / stations; i++) {
            // i - rows
            for (int j = 0; j < stations; j++) {
                // j - columns
                View view = recycler.getViewForPosition(i * stations + j);

                addView(view);

                measureChildWithMargins(view,0,0);

                int topOffset = getTopChildViewTopOffset(recycler,i * stations + j) + getPaddingTop();

                layoutDecorated(view,leftOffset,topOffset,leftOffset + getDecoratedMeasuredWidth(view),topOffset + getDecoratedMeasuredHeight(view));

                leftOffset += getDecoratedMeasuredWidth(view);
            }

            // reset leftOffset to 0.
            leftOffset = getPaddingLeft();

        }

    }

    private int getTopChildViewTopOffset(RecyclerView.Recycler recycler,int index) {

        if (index < stations)
            return 0;

        View topView = getChildAt(index -stations);
        return getDecoratedTop(topView) + getDecoratedMeasuredHeight(topView);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {

        offsetChildrenVertical(-dy);
        return dy;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {

        offsetChildrenHorizontal(-dx);
        return dx;
    }

}
