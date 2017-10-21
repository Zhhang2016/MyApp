package com.yan.pullrefreshlayout;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yan on 2017/7/7.
 * refresh show helper
 */
public class RefreshShowHelper {

    /**
     * @ShowState
     */
    @IntDef({STATE_FOLLOW, STATE_PLACEHOLDER_FOLLOW
            , STATE_PLACEHOLDER_CENTER, STATE_CENTER
            , STATE_CENTER_FOLLOW, STATE_FOLLOW_CENTER
            , STATE_PLACEHOLDER
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowState {
    }

    public static final int STATE_FOLLOW = 0;
    public static final int STATE_PLACEHOLDER_FOLLOW = 1;
    public static final int STATE_PLACEHOLDER_CENTER = 2;
    public static final int STATE_CENTER_FOLLOW = 3;
    public static final int STATE_FOLLOW_CENTER = 4;
    public static final int STATE_PLACEHOLDER = 5;
    public static final int STATE_CENTER = 6;

    private int headerShowState = STATE_FOLLOW;
    private int footerShowState = STATE_FOLLOW;

    private PullRefreshLayout pullRefreshLayout;

    RefreshShowHelper(PullRefreshLayout pullRefreshLayout) {
        this.pullRefreshLayout = pullRefreshLayout;
    }

    void setHeaderShowGravity(int headerShowGravity) {
        if (headerShowGravity != -1) {
            this.headerShowState = headerShowGravity;
        }
        if (headerShowState != RefreshShowHelper.STATE_FOLLOW) {
            targetBringToFront();
        }
    }

    void setFooterShowGravity(int footerShowGravity) {
        if (footerShowGravity != -1) {
            this.footerShowState = footerShowGravity;
        }
        if (footerShowState != RefreshShowHelper.STATE_FOLLOW) {
            targetBringToFront();
        }
    }

    private void targetBringToFront() {
        if (pullRefreshLayout.targetView != null && pullRefreshLayout.targetView == pullRefreshLayout.pullContentLayout) {
            pullRefreshLayout.targetView.bringToFront();
        }
    }

    void dellHeaderMoving(int moveDistance) {
        if (pullRefreshLayout.headerView != null && moveDistance >= 0) {
            switch (headerShowState) {
                case STATE_FOLLOW:
                    pullRefreshLayout.headerView.setTranslationY(moveDistance);
                    break;
                case STATE_CENTER:
                    pullRefreshLayout.headerView.setTranslationY(moveDistance / 2);
                    break;
                case STATE_FOLLOW_CENTER:
                    pullRefreshLayout.headerView.setTranslationY(moveDistance <= pullRefreshLayout
                            .refreshTriggerDistance ? moveDistance : pullRefreshLayout.refreshTriggerDistance
                            + (moveDistance - pullRefreshLayout.refreshTriggerDistance) / 2);
                    break;
                case STATE_CENTER_FOLLOW:
                    pullRefreshLayout.headerView.setTranslationY(moveDistance <= pullRefreshLayout
                            .refreshTriggerDistance ? moveDistance / 2 : moveDistance
                            - pullRefreshLayout.refreshTriggerDistance / 2);
                    break;
                case STATE_PLACEHOLDER_CENTER:
                    pullRefreshLayout.headerView.setTranslationY(moveDistance <=
                            pullRefreshLayout.refreshTriggerDistance ? 0 : (moveDistance
                            - pullRefreshLayout.refreshTriggerDistance) / 2);
                    break;
                case STATE_PLACEHOLDER_FOLLOW:
                    pullRefreshLayout.headerView.setTranslationY(moveDistance <= pullRefreshLayout
                            .refreshTriggerDistance ? 0 : moveDistance
                            - pullRefreshLayout.refreshTriggerDistance);
                    break;
            }
        }
    }

    void dellFooterMoving(int moveDistance) {
        if (pullRefreshLayout.footerView != null && moveDistance <= 0) {
            switch (footerShowState) {
                case STATE_FOLLOW:
                    pullRefreshLayout.footerView.setTranslationY(moveDistance);
                    break;
                case STATE_CENTER:
                    pullRefreshLayout.footerView.setTranslationY(moveDistance / 2);
                    break;
                case STATE_FOLLOW_CENTER:
                    pullRefreshLayout.footerView.setTranslationY(moveDistance <= -pullRefreshLayout
                            .loadTriggerDistance ? -pullRefreshLayout.loadTriggerDistance
                            + (pullRefreshLayout.loadTriggerDistance + moveDistance) / 2 : moveDistance);
                    break;
                case STATE_CENTER_FOLLOW:
                    pullRefreshLayout.footerView.setTranslationY(moveDistance <= -pullRefreshLayout
                            .loadTriggerDistance ? moveDistance + pullRefreshLayout
                            .loadTriggerDistance / 2 : moveDistance / 2);
                    break;
                case STATE_PLACEHOLDER_CENTER:
                    pullRefreshLayout.footerView.setTranslationY(moveDistance <= -pullRefreshLayout
                            .loadTriggerDistance ? (moveDistance + pullRefreshLayout
                            .loadTriggerDistance) / 2 : 0);
                    break;
                case STATE_PLACEHOLDER_FOLLOW:
                    pullRefreshLayout.footerView.setTranslationY(moveDistance <= -pullRefreshLayout
                            .loadTriggerDistance ? moveDistance + pullRefreshLayout
                            .loadTriggerDistance : 0);
                    break;
            }
        }
    }

    void layout(int left, int top, int right, int bottom) {
        if (pullRefreshLayout.headerView != null) {
            int paddingLeft = pullRefreshLayout.getPaddingLeft();
            int paddingTop = pullRefreshLayout.getPaddingTop();
            PullRefreshLayout.LayoutParams lp = (PullRefreshLayout.LayoutParams) pullRefreshLayout.headerView.getLayoutParams();
            switch (headerShowState) {
                case STATE_FOLLOW:
                case STATE_FOLLOW_CENTER:
                    pullRefreshLayout.headerView.layout(paddingLeft + lp.leftMargin
                            , top + lp.topMargin + paddingTop - pullRefreshLayout.headerView.getMeasuredHeight()
                            , paddingLeft + lp.leftMargin + pullRefreshLayout.headerView.getMeasuredWidth()
                            , top + lp.topMargin + paddingTop);
                    break;
                case STATE_PLACEHOLDER:
                case STATE_PLACEHOLDER_CENTER:
                case STATE_PLACEHOLDER_FOLLOW:
                    pullRefreshLayout.headerView.layout(paddingLeft + lp.leftMargin
                            , top + paddingTop + lp.topMargin
                            , paddingLeft + lp.leftMargin + pullRefreshLayout.headerView.getMeasuredWidth()
                            , top + paddingTop + lp.topMargin + pullRefreshLayout.headerView.getMeasuredHeight());
                    break;
                case STATE_CENTER:
                case STATE_CENTER_FOLLOW:
                    pullRefreshLayout.headerView.layout(paddingLeft + lp.leftMargin
                            , -pullRefreshLayout.headerView.getMeasuredHeight() / 2
                            , paddingLeft + lp.leftMargin + pullRefreshLayout.headerView.getMeasuredWidth()
                            , pullRefreshLayout.headerView.getMeasuredHeight() / 2);
                    break;
            }
        }
        if (pullRefreshLayout.footerView != null) {
            int paddingLeft = pullRefreshLayout.getPaddingLeft();
            int paddingTop = pullRefreshLayout.getPaddingTop();
            PullRefreshLayout.LayoutParams lp = (PullRefreshLayout.LayoutParams) pullRefreshLayout.footerView.getLayoutParams();
            switch (footerShowState) {
                case STATE_FOLLOW:
                case STATE_FOLLOW_CENTER:
                    pullRefreshLayout.footerView.layout(lp.leftMargin + paddingLeft
                            , bottom + lp.topMargin + paddingTop
                            , lp.leftMargin + paddingLeft + pullRefreshLayout.footerView.getMeasuredWidth()
                            , bottom + lp.topMargin + paddingTop + pullRefreshLayout.footerView.getMeasuredHeight());
                    break;
                case STATE_PLACEHOLDER:
                case STATE_PLACEHOLDER_CENTER:
                case STATE_PLACEHOLDER_FOLLOW:
                    pullRefreshLayout.footerView.layout(lp.leftMargin + paddingLeft
                            , bottom + lp.topMargin + paddingTop - pullRefreshLayout.footerView.getMeasuredHeight()
                            , lp.leftMargin + paddingLeft + pullRefreshLayout.footerView.getMeasuredWidth()
                            , bottom + lp.topMargin + paddingTop);
                    break;
                case STATE_CENTER:
                case STATE_CENTER_FOLLOW:
                    pullRefreshLayout.footerView.layout(lp.leftMargin + paddingLeft
                            , bottom - pullRefreshLayout.footerView.getMeasuredHeight() / 2
                            , lp.leftMargin + paddingLeft + pullRefreshLayout.footerView.getMeasuredWidth()
                            , bottom + pullRefreshLayout.footerView.getMeasuredHeight() / 2);
                    break;
            }
        }
    }
}
