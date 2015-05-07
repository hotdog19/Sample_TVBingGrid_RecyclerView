package jp.co.fenrir.tvbing_sample;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * A {@link android.support.v7.widget.RecyclerView.LayoutManager} implementation
 * that places children in a two-dimensional grid, sized to a fixed column count
 * value. User scrolling is possible in both horizontal and vertical directions
 * to view the data set.
 *
 * <p>The column count is controllable via {@link #setTotalColumnCount(int)}. The layout manager
 * will generate the number of rows necessary to accommodate the data set based on
 * the fixed column count.
 *
 * <p>This manager does make some assumptions to simplify the implementation:
 * <ul>
 *     <li>All child views are assumed to be the same size</li>
 *     <li>The window of visible views is a constant</li>
 * </ul>
 */
public class ZYFixedGridLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = ZYFixedGridLayoutManager.class.getSimpleName();

    /* Fill Direction Constants */
    private static final int DIRECTION_NONE = -1;
    private static final int DIRECTION_START = 0;
    private static final int DIRECTION_END = 1;
    private static final int DIRECTION_UP = 2;
    private static final int DIRECTION_DOWN = 3;

    private static final int DEFAULT_COUNT = 1;



    /* Fill Direction Constants */

    /* First (top-left) position visible at any point */
    private int mFirstVisiblePosition;
    /* Consistent size applied to all child views */
    private int mDecoratedChildWidth;
    private int mDecoratedChildHeight = DataManager.ITEM_HEIGHT;
    /* Number of columns that exist in the grid */
    private int mTotalColumnCount = DEFAULT_COUNT;
    /* Metrics for the visible window of our data */
    private int mVisibleColumnCount;
    private int mVisibleRowCount;
    /* Flag to force current scroll offsets to be ignored on re-layout */
    private boolean mForceClearOffsets;

    /**
     * Set the number of columns the layout manager will use. This will
     * trigger a layout update.
     * @param count Number of columns.
     */
    public void setTotalColumnCount(int count) {
        mTotalColumnCount = count;
        requestLayout();
    }

    /*
     * You must return true from this method if you want your
     * LayoutManager to support anything beyond "simple" item
     * animations. Enabling this causes onLayoutChildren() to
     * be called twice on each animated change; once for a
     * pre-layout, and again for the real layout.
     */
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }



    /*
     * This method is your initial call from the framework. You will receive it when you
     * need to start laying out the initial set of views. This method will not be called
     * repeatedly, so don't rely on it to continually process changes during user
     * interaction.
     *
     * This method will be called when the data set in the adapter changes, so it can be
     * used to update a layout based on a new item count.
     *
     * If predictive animations are enabled, you will see this called twice. First, with
     * state.isPreLayout() returning true to lay out children in their initial conditions.
     * Then again to lay out children in their final locations.
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //We have nothing to show for an empty data set but clear any existing views
        //得到adapter中所有的count
        Log.d("MSG", "onLayoutChildren RecyclerView.State:" + state);
        Log.d("MSG", "onLayoutChildren mDecoratedChildHeight:" + mDecoratedChildHeight);
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }

        //当前view是否为空
        if (getChildCount() == 0) { //First or empty layout
            //Scrap measure one child
            View scrap = recycler.getViewForPosition(0);
            addView(scrap);
            //得到第一个view高度与宽度
            measureChildWithMargins(scrap, 0, 0);

            /*
             * We make some assumptions in this code based on every child
             * view being the same size (i.e. a uniform grid). This allows
             * us to compute the following values up front because they
             * won't change.
             */
            mDecoratedChildWidth = getDecoratedMeasuredWidth(scrap);
            mDecoratedChildHeight = DataManager.ITEM_HEIGHT;
            Log.d("MSG", "onLayoutChildren mDecoratedChildHeight:" + mDecoratedChildHeight);

            //这个地方需要计算真实的高度
            detachAndScrapView(scrap, recycler);
        }


        //Always update the visible startRow/column counts
        updateWindowSizing();

        Log.d("MSG", "onLayoutChildren after updateWindowSizing() mDecoratedChildHeight:" + mDecoratedChildHeight);


        SparseIntArray removedCache = null;

        int childLeft;
        int childTop;
        if (getChildCount() == 0) {
            //第一次进行layout
            //First or empty layout
            //Reset the visible and scroll positions
            mFirstVisiblePosition = 0;
            childLeft = childTop = 0;
        } else if (getVisibleChildCount() >= getItemCount()) {
            //这个地方需要判断是否不需要scroll 如果一个节目把所有的元素都能显示完成的话。
            //Data set is too small to scroll fully, just reset position
            mFirstVisiblePosition = 0;
            childLeft = childTop = 0;
        } else {
            //如果Adapter data change 走下面的方法

            //Adapter data set changes
            /*
             * Keep the existing initial position, and save off
             * the current scrolled offset.
             */
            final View topChild = getChildAt(0);
            if (mForceClearOffsets) {
                childLeft = childTop = 0;
                mForceClearOffsets = false;
            } else {
                childLeft = getDecoratedLeft(topChild);
                childTop = getDecoratedTop(topChild);
            }

            /*
             * When data set is too small to scroll vertically, adjust vertical offset
             * and shift position to the first startRow, preserving current column
             */
//            if (!state.isPreLayout() && getVerticalSpace() > (getTotalRowCount() * mDecoratedChildHeight)) {
//                mFirstVisiblePosition = mFirstVisiblePosition % getTotalColumnCount();
//                childTop = 0;
//
//                //If the shift overscrolls the column max, back it off
//                if ((mFirstVisiblePosition + mVisibleColumnCount) > getItemCount()) {
//                    mFirstVisiblePosition = Math.max(getItemCount() - mVisibleColumnCount, 0);
//                    childLeft = 0;
//                }
//            }

            /*
             * Adjust the visible position if out of bounds in the
             * new layout. This occurs when the new item count in an adapter
             * is much smaller than it was before, and you are scrolled to
             * a location where no items would exist.
             */
//            int maxFirstRow = getTotalRowCount() - (mVisibleRowCount-1);
//            int maxFirstCol = getTotalColumnCount() - (mVisibleColumnCount-1);
//            boolean isOutOfRowBounds = getFirstVisibleRow() > maxFirstRow;
//            boolean isOutOfColBounds =  getFirstVisibleColumn() > maxFirstCol;
//            if (isOutOfRowBounds || isOutOfColBounds) {
//                int firstRow;
//                if (isOutOfRowBounds) {
//                    firstRow = maxFirstRow;
//                } else {
//                    firstRow = getFirstVisibleRow();
//                }
//                int firstCol;
//                if (isOutOfColBounds) {
//                    firstCol = maxFirstCol;
//                } else {
//                    firstCol = getFirstVisibleColumn();
//                }
//                mFirstVisiblePosition = firstRow * DataManager.COLUMES + firstCol;
//                Log.d("MSG", "mFirstVisiblePosition set 2:"+ mFirstVisiblePosition);
//
//
//                childLeft = getHorizontalSpace() - (mDecoratedChildWidth * mVisibleColumnCount);
//                childTop = getVerticalSpace() - (mDecoratedChildHeight * mVisibleRowCount);
//
//                //Correct cases where shifting to the bottom-right overscrolls the top-left
//                // This happens on data sets too small to scroll in a direction.
//                if (getFirstVisibleRow() == 0) {
//                    childTop = Math.min(childTop, 0);
//                }
//                if (getFirstVisibleColumn() == 0) {
//                    childLeft = Math.min(childLeft, 0);
//                }
//            }
        }

        //Clear all attached views into the recycle bin
        //删除所有的recycle bin的东东。
        detachAndScrapAttachedViews(recycler);

        //Fill the grid for the initial layout of views
        fillGrid(DIRECTION_NONE, 0, 0, recycler);
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        //Completely scrap the existing layout
        removeAllViews();

    }

    /*
     * Rather than continuously checking how many views we can fit
     * based on scroll offsets, we simplify the math by computing the
     * visible grid as what will initially fit on screen, plus one.
     */
    private void updateWindowSizing() {
        //计算出有多少列，没有除尽就加1
        mVisibleColumnCount = (getHorizontalSpace() / mDecoratedChildWidth) + 1;
        if (getHorizontalSpace() % mDecoratedChildWidth > 0) {
            //如果没有除尽就加一
            mVisibleColumnCount++;
        }

        //Allow minimum value for small data sets
        if (mVisibleColumnCount > DataManager.COLUMES) {
            //如果超过了总的列数，则直接返回
            mVisibleColumnCount = DataManager.COLUMES;
        }


        mVisibleRowCount = (getVerticalSpace()/ mDecoratedChildHeight) + 1;
        if (getVerticalSpace() % mDecoratedChildHeight > 0) {
            mVisibleRowCount++;
        }

        if (mVisibleRowCount > DataManager.ROWS) {
            mVisibleRowCount = DataManager.ROWS;
        }
    }

    private void fillGrid(int direction, RecyclerView.Recycler recycler) {
        fillGrid(direction, 0, 0, recycler);
    }

    private void fillGrid(int direction, int emptyLeft, int emptyTop, RecyclerView.Recycler recycler) {

        Log.d("MSG", "fillGrid start");

        if (mFirstVisiblePosition < 0) mFirstVisiblePosition = 0;
        if (mFirstVisiblePosition >= DataManager.COLUMES * DataManager.ROWS)  {
            mFirstVisiblePosition = (DataManager.COLUMES * DataManager.ROWS - 1);
            Log.d("MSG", "mFirstVisiblePosition:" + mFirstVisiblePosition);
        }


        /*
         * First, we will detach all existing views from the layout.
         * detachView() is a lightweight operation that we can use to
         * quickly reorder views without a full add/remove.
         */
        SparseArray<View> viewCache = new SparseArray<View>(getChildCount());
        int startLeftOffset = getPaddingLeft() + emptyLeft;
        int startTopOffset = getPaddingTop() + emptyTop;
        int firstVisibleRow = (int)Math.floor(mFirstVisiblePosition / DataManager.COLUMES);
        if (getChildCount() != 0) {
            final View topView = getChildAt(0);
            startLeftOffset = getDecoratedLeft(topView);
            int topviewTop =  getDecoratedTop(topView);
            LayoutParams lps = (LayoutParams)topView.getLayoutParams();
            int visibleRowOffset = firstVisibleRow - lps.startRow;
            startTopOffset =  topviewTop + visibleRowOffset * mDecoratedChildHeight;

            switch (direction) {
                case DIRECTION_START:
                    startLeftOffset -= mDecoratedChildWidth;
                    break;
                case DIRECTION_END:
                    startLeftOffset += mDecoratedChildWidth;
                    break;
                case DIRECTION_UP:
                    startTopOffset -= mDecoratedChildHeight;
                    break;
                case DIRECTION_DOWN:
                    startTopOffset += mDecoratedChildHeight;
                    break;
            }

            //Cache all views by their existing position, before updating counts
            for (int i=0; i < getChildCount(); i++) {
               // int position = positionOfIndex(i);
                final View child = getChildAt(i);
                LayoutParams lp = (LayoutParams)child.getLayoutParams();
                viewCache.put(lp.position, child);
            }

            //Temporarily detach all views.
            // Views we still need will be added back at the proper index.
            for (int i=0; i < viewCache.size(); i++) {
                detachView(viewCache.valueAt(i));
            }
        }

        /*
         * Next, we advance the visible position based on the fill direction.
         * DIRECTION_NONE doesn't advance the position in any direction.
         */
        switch (direction) {
            case DIRECTION_START:
                mFirstVisiblePosition--;
                break;
            case DIRECTION_END:
                mFirstVisiblePosition++;
                break;
            case DIRECTION_UP:
                mFirstVisiblePosition -= DataManager.COLUMES;
                break;
            case DIRECTION_DOWN:
                mFirstVisiblePosition += DataManager.COLUMES;
                break;
        }

        /*
         * Next, we supply the grid of items that are deemed visible.
         * If these items were previously there, they will simply be
         * re-attached. New views that must be created are obtained
         * from the Recycler and added.
         */
        int leftOffset = startLeftOffset;
        int topOffset = startTopOffset;


        this.fillChildView(leftOffset,topOffset,recycler,viewCache);

//        /*
//         * Finally, we ask the Recycler to scrap and store any views
//         * that we did not re-attach. These are views that are not currently
//         * necessary because they are no longer visible.
//         */
        for (int i=0; i < viewCache.size(); i++) {
            final View removingView = viewCache.valueAt(i);
            recycler.recycleView(removingView);
        }
    }

    private void fillChildView(int leftOffset, int topOffset, RecyclerView.Recycler recycler, SparseArray<View> viewCache) {
        List<Integer> visiblePositonList = DataManager.getSharedInstance().getVisibleData(mFirstVisiblePosition,mVisibleColumnCount,mVisibleRowCount);
        int firstVisibleRow = (int)Math.floor(mFirstVisiblePosition / DataManager.COLUMES);

        for (int position  : visiblePositonList) {
            View view = viewCache.get(position);
            TVProgram p = DataManager.getSharedInstance().getLists().get(position);
            if (view == null) {
                Log.d("MSG","view == null");
                /*
                 * The Recycler will give us either a newly constructed view,
                 * or a recycled view it has on-hand. In either case, the
                 * view will already be fully bound to the data by the
                 * adapter for us.
                 */

                view = recycler.getViewForPosition(position);
                addView(view);
                Log.d("MSG","after addView(view)");
                /*

                /*
                 * It is prudent to measure/layout each new view we
                 * receive from the Recycler. We don't have to do
                 * this for views we are just re-arranging.
                 */
                Log.d("MSG","measureChildWithMargins before");
                measureChildWithMargins(view, 0, 0);
                Log.d("MSG","measureChildWithMargins after");
                int left = leftOffset + (p.leftOffset() - (mFirstVisiblePosition % DataManager.COLUMES)) * mDecoratedChildWidth;
                //layout这边有问题。

                int top = topOffset + (p.getMatrixStartRow() - firstVisibleRow) * mDecoratedChildHeight;

                layoutDecorated(view, left, top,
                        left + mDecoratedChildWidth,
                        top + view.getLayoutParams().height);
                Log.d("MSG","layoutDecorated after leftOffset :" + leftOffset + "topOffset :" + topOffset);

            } else {
                //Re-attach the cached view at its new index
                Log.d("MSG","attachView");


                /*
                 * It is prudent to measure/layout each new view we
                 * receive from the Recycler. We don't have to do
                 * this for views we are just re-arranging.
                    viewCache.remove(position);
                 */

                    attachView(view);
                    viewCache.remove(position);


            }
        }
    }

    /*
     * You must override this method if you would like to support external calls
     * to shift the view to a given adapter position. In our implementation, this
     * is the same as doing a fresh layout with the given position as the top-left
     * (or first visible), so we simply set that value and trigger onLayoutChildren()
     */
    @Override
    public void scrollToPosition(int position) {
        if (position >= getItemCount()) {
            Log.e(TAG, "Cannot scroll to "+position+", item count is "+getItemCount());
            return;
        }

        //Ignore current scroll offset, snap to top-left
        mForceClearOffsets = true;
        //Set requested position as first visible
        mFirstVisiblePosition = position;
        //Trigger a new view layout
        requestLayout();
    }

    /*
     * You must override this method if you would like to support external calls
     * to animate a change to a new adapter position. The framework provides a
     * helper scroller implementation (LinearSmoothScroller), which we leverage
     * to do the animation calculations.
     */
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {
        if (position >= getItemCount()) {
            Log.e(TAG, "Cannot scroll to "+position+", item count is "+getItemCount());
            return;
        }

        /*
         * LinearSmoothScroller's default behavior is to scroll the contents until
         * the child is fully visible. It will snap to the top-left or bottom-right
         * of the parent depending on whether the direction of travel was positive
         * or negative.
         */
        LinearSmoothScroller scroller = new LinearSmoothScroller(recyclerView.getContext()) {
            /*
             * LinearSmoothScroller, at a minimum, just need to know the vector
             * (x/y distance) to travel in order to get from the current positioning
             * to the target.
             */
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                final int rowOffset = getGlobalRowOfPosition(targetPosition)
                        - getGlobalRowOfPosition(mFirstVisiblePosition);
                final int columnOffset = getGlobalColumnOfPosition(targetPosition)
                        - getGlobalColumnOfPosition(mFirstVisiblePosition);

                return new PointF(columnOffset * mDecoratedChildWidth, rowOffset * mDecoratedChildHeight);
            }
        };
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    /*
     * Use this method to tell the RecyclerView if scrolling is even possible
     * in the horizontal direction.
     */
    @Override
    public boolean canScrollHorizontally() {
        //We do allow scrolling
        return true;
    }

    /*
     * This method describes how far RecyclerView thinks the contents should scroll horizontally.
     * You are responsible for verifying edge boundaries, and determining if this scroll
     * event somehow requires that new views be added or old views get recycled.
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.e("MSG","scrollHorizontallyBy start");
        if (getChildCount() == 0) {
            return 0;
        }

        Log.e("TT","dx is :" + dx);


        //Take leftmost measurements from the top-left child
        final View topView = getChildAt(0);
        final View bottomView = getChildAt(getChildCount()-1);

        //Optimize the case where the entire data set is too small to scroll
        int viewSpan = getDecoratedRight(bottomView) - getDecoratedLeft(topView);
        if (viewSpan < getHorizontalSpace()) {
            //We cannot scroll in either direction
            //这个地方不能横滚
            return 0;
        }

        int delta;

        boolean leftBoundReached = getFirstVisibleColumn() == 0;
        boolean rightBoundReached = getLastVisibleColumn() >= DataManager.COLUMES;



        if (dx > 0) { // Contents are scrolling left
            //Check right bound
            if (rightBoundReached) {
                //If we've reached the last column, enforce limits
                int rightOffset = getHorizontalSpace() - getDecoratedRight(bottomView) + getPaddingRight();
                delta = Math.max(-dx, rightOffset);
            } else {
                //No limits while the last column isn't visible
                delta = -dx;
            }
        } else { // Contents are scrolling right
            //Check left bound
            if (leftBoundReached) {
                int leftOffset = -getDecoratedLeft(topView) + getPaddingLeft();
                delta = Math.min(-dx, leftOffset);
            } else {
                delta = -dx;
            }
        }

        offsetChildrenHorizontal(delta);

        if (dx > 0) {
            if (getDecoratedRight(topView) < 0 && !rightBoundReached) {
                fillGrid(DIRECTION_END, recycler);
            } else if (!rightBoundReached) {
                fillGrid(DIRECTION_NONE, recycler);
            }
        } else {
            if (getDecoratedLeft(topView) > 0 && !leftBoundReached) {
                fillGrid(DIRECTION_START, recycler);
            } else if (!leftBoundReached) {
                fillGrid(DIRECTION_NONE, recycler);
            }
        }

        /*
         * Return value determines if a boundary has been reached
         * (for edge effects and flings). If returned value does not
         * match original delta (passed in), RecyclerView will draw
         * an edge effect.
         */
        return -delta;
    }

    /*
     * Use this method to tell the RecyclerView if scrolling is even possible
     * in the vertical direction.
     */
    @Override
    public boolean canScrollVertically() {
        //We do allow scrolling
        return true;
    }

    /*
     * This method describes how far RecyclerView thinks the contents should scroll vertically.
     * You are responsible for verifying edge boundaries, and determining if this scroll
     * event somehow requires that new views be added or old views get recycled.
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }

        Log.e("TT","dy is :" + dy);

        //if dy is grater than twice of mDecoratedChildHeight, set dy to yOffset
        int yOffset = Math.abs(dy) > 2*mDecoratedChildHeight ? 2*mDecoratedChildHeight * (dy/ Math.abs(dy)) : dy;

//        int yOffset = dy;

        Log.d("MSG", "firstVisiblePosition childCount :" + getChildCount());


        //这里可以通过 mFirstVisiblePosition计算出高度，假定没有间距。
        //TODO 可以写成方法
        int firstVisibleRow = (int)Math.floor(mFirstVisiblePosition / DataManager.COLUMES);

        //Take top measurements from the top-left child
        final View topView = getChildAt(0);
        int topviewTop =  getDecoratedTop(topView);

        //Take bottom measurements from the bottom-right child.
        //final View bottomView = getChildAt(getChildCount()-1);

        //Optimize the case where the entire data set is too small to scroll
        //int viewSpan = getDecoratedBottom(bottomView) - getDecoratedTop(topView);
        LayoutParams lp = (LayoutParams)topView.getLayoutParams();
        int visibleRowOffset = 0;
        //if (yOffset >=  0 ) {
            visibleRowOffset =  firstVisibleRow - lp.startRow;
        //} else {
        //    visibleRowOffset = lp.endRow - firstVisibleRow;
        //}
        int top =  topviewTop + visibleRowOffset * mDecoratedChildHeight;
        int bottom = top + mVisibleRowCount  * mDecoratedChildHeight;


        int viewSpan =  bottom - top;
        if (viewSpan < getVerticalSpace()) {
            //We cannot scroll in either direction
            return 0;
        }

        int delta;
        int maxRowCount = DataManager.ROWS;
        boolean topBoundReached = getFirstVisibleRow() == 0;
        boolean bottomBoundReached = getLastVisibleRow() >= maxRowCount;
        if (yOffset > 0) { // Contents are scrolling up
            //Check against bottom bound
            if (bottomBoundReached) {
                //If we've reached the last startRow, enforce limits
                int bottomOffset;

                //假设 都是到一样的底部
                final View bottomView = getChildAt(getChildCount() - 1);
                LayoutParams lpb = (LayoutParams)bottomView.getLayoutParams();
                TVProgram pb = DataManager.getSharedInstance().getLists().get(lpb.position);
                //一个是减法
                int bottomRow =  pb.getMatrixEndRow();


                if ( bottomRow >= (maxRowCount - 1)) {
                    //We are truly at the bottom, determine how far
                    bottomOffset = getVerticalSpace() - bottom
                            + getPaddingBottom();
                } else {
                    /*
                     * Extra space added to account for allowing bottom space in the grid.
                     * This occurs when the overlap in the last startRow is not large enough to
                     * ensure that at least one element in that startRow isn't fully recycled.
                     */
                    bottomOffset = getVerticalSpace() - (bottom
                            + mDecoratedChildHeight) + getPaddingBottom();
                }

                delta = Math.max(-yOffset, bottomOffset);
            } else {
                //No limits while the last startRow isn't visible
                delta = -yOffset;
            }
        } else { // Contents are scrolling down
            //Check against top bound
            if (topBoundReached) {
                int topOffset = -top + getPaddingTop();

                delta = Math.min(-yOffset, topOffset);
            } else {
                delta = -yOffset;
            }
        }

        offsetChildrenVertical(delta);

        if (yOffset > 0) {
         //这个地方错了，应该重新计算

            //Log.d("MSG", "firstVisiblePosition getDecoratedBottom(topView) :" + getDecoratedBottom(topView));
            //Log.d("MSG", "firstVisiblePosition visibleRowOffset :" + visibleRowOffset);
            //Log.d("MSG", "firstVisiblePosition botttom:" + (getDecoratedBottom(topView)- visibleRowOffset * mDecoratedChildHeight));
            if ( getDecoratedTop(topView) + (visibleRowOffset+1) * mDecoratedChildHeight < 0 && !bottomBoundReached) {
                fillGrid(DIRECTION_DOWN, recycler);
            } else if (!bottomBoundReached) {
                fillGrid(DIRECTION_NONE, recycler);
            }
        } else {
            //Log.d("MSG", "firstVisiblePosition -1 :"+ yOffset);
//            Log.d("MSG","firstVisiblePosition mDecoratedChildHeight:" + mDecoratedChildHeight);
//            Log.d("MSG","firstVisiblePosition cur : " + firstVisibleRow + " lp.startRow: " + lp.startRow + " lp.endRow: " + lp.endRow);
//            Log.d("MSG", "firstVisiblePosition getDecoratedTop(topView) :" + getDecoratedTop(topView));
//            Log.d("MSG", "firstVisiblePosition getDecoratedBottom(topView) :" + getDecoratedBottom(topView));
//            //Log.d("MSG", "firstVisiblePosition visibleRowOffset :" + visibleRowOffset);
            //topTotalOffset -
            //int height = getDecoratedTop(topView) - (topTotalOffset - visibleRowOffset - 1) * mDecoratedChildHeight;
            //Log.d("MSG", "firstVisiblePosition botttom:" + height);
            int height = getDecoratedBottom(topView) - (lp.endRow - firstVisibleRow + 1) * mDecoratedChildHeight;

//            Log.d("MSG", "firstVisiblePosition height :" + height);


            if (getDecoratedBottom(topView) - (lp.endRow - firstVisibleRow + 1 -1) * mDecoratedChildHeight   > 0 && !topBoundReached) {
                fillGrid(DIRECTION_UP, recycler);
            } else if (!topBoundReached) {
                fillGrid(DIRECTION_NONE, recycler);
            }
        }

        /*
         * Return value determines if a boundary has been reached
         * (for edge effects and flings). If returned value does not
         * match original delta (passed in), RecyclerView will draw
         * an edge effect.
         */
        return -delta;
    }

    /*
     * This is a helper method used by RecyclerView to determine
     * if a specific child view can be returned.
     */
    @Override
    public View findViewByPosition(int position) {
        //TODO
        for (int i=0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            LayoutParams lp = (LayoutParams)child.getLayoutParams();
            if (lp.position == position) {
                return getChildAt(i);
            }
        }

        return null;
    }

    /** Boilerplate to extend LayoutParams for tracking startRow/column of attached views */

    /*
     * Even without extending LayoutParams, we must override this method
     * to provide the default layout parameters that each child view
     * will receive when added.
     */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }
    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
        } else {
            return new LayoutParams(lp);
        }
    }
    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    public static class LayoutParams extends RecyclerView.LayoutParams {

        //Current row in the grid
        public int startRow;
        //Current column in the grid
        //由于是固定的所以只需要做这样的判断
        public int column;
        public int endRow;//都是以开始时间来弄
        public int position;
        public int visibleRow;


        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }
        public LayoutParams(int width, int height) {
            super(width, height);
        }
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }
    }

    /** Private Helpers and Metrics Accessors */

    /* Return the overall column index of this position in the global layout */
    private int getGlobalColumnOfPosition(int position) {
        return position % mTotalColumnCount;
    }
    /* Return the overall startRow index of this position in the global layout */
    private int getGlobalRowOfPosition(int position) {
        return position / mTotalColumnCount;
    }

    /*
     * Mapping between child view indices and adapter data
     * positions helps fill the proper views during scrolling.
     */
//    private int positionOfIndex(int childIndex) {
//        int startRow = childIndex / mVisibleColumnCount;
//        int column = childIndex % mVisibleColumnCount;
//
//        return mFirstVisiblePosition + (startRow * DataManager.COLUMES) + column;
//    }

//    private int rowOfIndex(int childIndex) {
//        int position = positionOfIndex(childIndex);
//
//        return position / DataManager.COLUMES;
//    }

    private int getFirstVisibleColumn() {
        return (mFirstVisiblePosition % DataManager.COLUMES);
    }

    private int getLastVisibleColumn() {
        return getFirstVisibleColumn() + mVisibleColumnCount;
    }

    private int getFirstVisibleRow() {
        return (mFirstVisiblePosition / DataManager.COLUMES);
    }

    private int getLastVisibleRow() {
        return getFirstVisibleRow() + mVisibleRowCount;
    }

    private int getVisibleChildCount() {
        return mVisibleColumnCount * mVisibleRowCount;
    }

    private int getTotalColumnCount() {
        if (getItemCount() < mTotalColumnCount) {
            return getItemCount();
        }

        return mTotalColumnCount;
    }

    private int getTotalRowCount() {
        if (getItemCount() == 0 || mTotalColumnCount == 0) {
            return 0;
        }
        int maxRow = getItemCount() / mTotalColumnCount;
        //Bump the startRow count if it's not exactly even
        if (getItemCount() % mTotalColumnCount != 0) {
            maxRow++;
        }

        return maxRow;
    }

    private int getHorizontalSpace() {
        Log.d("Msg","getWidth() :" + getWidth());
        Log.d("Msg","getPaddingRight() :" + getPaddingRight());
        Log.d("Msg","getPaddingLeft() :" + getPaddingLeft());
        return getWidth() - getPaddingRight() - getPaddingLeft();
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }
}
