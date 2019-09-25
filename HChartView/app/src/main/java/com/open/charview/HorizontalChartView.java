package com.open.charview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 横向坐标柱状图view
 */
public class HorizontalChartView extends View {
    private String TAG = this.getClass().getSimpleName();
    public static final int ROWSPACEMODE_SINGLE = 1;
    public static final int ROWSPACEMODE_MULTIPLE = 2;
    private Context mContext;
    //间隔线画笔
    private Paint mLinePaint;
    //间隔线粗细
    private int mLineSize;
    //间隔线颜色
    private int mLineColor;

    //水平比例柱画笔
    private Paint mColumnPaint;
    //水平比例柱粗细
    private int mColumnHeight;
    //水平比例柱状颜色
    private int mColumnColor;

    //文本画笔
    private Paint mLeftFontPaint;
    private Paint mRightFontPaint;
    private Paint mBottomFontPaint;
    //左文字大小
    private int mLeftFontSize;
    //左文字颜色
    private int mLeftFontColor;
    //右文字大小
    private int mRightFontSize;
    //右文字颜色
    private int mRightFontColor;
    //底部文字大小
    private int mBottomFontSize;
    //底部文字颜色
    private int mBottomFontColor;

    //间隔线条数
    private int mLineNum = 6;
    //控件整体高度,用于自适应高度绘制
    private int mChartViewHeight;

    //行间距
    private int mRowSpace = 25;
    //柱状图之间mRowSpace个数
    private int mRowSpaceMode;
    //柱状图和左侧文本间距
    private int mLeftFontSpace;
    //柱状图和右侧文本间距
    private int mRightFontSpace;
    //底部文本
    private String mBottemText = "0";

    //展示的数据集
    private List<Chart> mChartList;
    //最大分数值
    private float mMaxScore;

    public HorizontalChartView(Context context) {
        this(context, null);
    }

    public HorizontalChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        TypedArray attr = mContext.obtainStyledAttributes(attrs, R.styleable.HorizontalChartView, 0, 0);
        mLineSize = attr.getDimensionPixelSize(R.styleable.HorizontalChartView_lineSize, mLineSize);
        mLineColor = attr.getColor(R.styleable.HorizontalChartView_lineColor, 0xFF979797);
        mColumnHeight = attr.getDimensionPixelSize(R.styleable.HorizontalChartView_columnHeight, 20);
        mColumnColor = attr.getColor(R.styleable.HorizontalChartView_columnColor, 0xFF958FF4);
        mLeftFontSize = attr.getDimensionPixelSize(R.styleable.HorizontalChartView_leftFontSize, 44);
        mLeftFontColor = attr.getColor(R.styleable.HorizontalChartView_leftFontColor, 0xFF666666);
        mRightFontSize = attr.getDimensionPixelSize(R.styleable.HorizontalChartView_rightFontSize, 44);
        mRightFontColor = attr.getColor(R.styleable.HorizontalChartView_rightFontColor, 0xFF666666);
        mRightFontSpace = attr.getDimensionPixelSize(R.styleable.HorizontalChartView_rightFontSpace, 10);
        mLeftFontSpace = attr.getDimensionPixelSize(R.styleable.HorizontalChartView_rightFontSpace, 10);
        mBottomFontSize = attr.getDimensionPixelSize(R.styleable.HorizontalChartView_bottomFontSize, 41);
        mBottomFontColor = attr.getColor(R.styleable.HorizontalChartView_bottomFontColor, 0xFF666666);
        mLineNum = attr.getInteger(R.styleable.HorizontalChartView_lineNum, mLineNum);
        mRowSpace = attr.getDimensionPixelSize(R.styleable.HorizontalChartView_rowSpace, mRowSpace);
        mRowSpaceMode = attr.getInt(R.styleable.HorizontalChartView_rowSpaceMode, ROWSPACEMODE_MULTIPLE);

        mChartList = new ArrayList<>();

        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(DensityUtils.px2sp(context, mLineSize));
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setColor(mLineColor);

        mColumnPaint = new Paint();
        mColumnPaint.setColor(mColumnColor);

        mLeftFontPaint = new Paint();
        mLeftFontPaint.setTextSize(mLeftFontSize);
        mLeftFontPaint.setColor(mLeftFontColor);
        mLeftFontPaint.setAntiAlias(true);
        mLeftFontPaint.setTextAlign(Paint.Align.LEFT);

        mRightFontPaint = new Paint();
        mRightFontPaint.setTextSize(mRightFontSize);
        mRightFontPaint.setColor(mRightFontColor);
        mRightFontPaint.setAntiAlias(true);
        mRightFontPaint.setTextAlign(Paint.Align.LEFT);

        mBottomFontPaint = new Paint();
        mBottomFontPaint.setTextSize(mBottomFontSize);
        mBottomFontPaint.setColor(mBottomFontColor);
        mBottomFontPaint.setAntiAlias(true);
        mBottomFontPaint.setTextAlign(Paint.Align.CENTER);

        attr.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, mChartViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChartList != null && !mChartList.isEmpty()) {
            int width = getWidth();
            int height = getHeight();
            int leftWidth = (int) (width * 0.15);
            int lineWidth = (int) (width * 0.78);

            //底部文字信息
            Paint.FontMetrics fm = mBottomFontPaint.getFontMetrics();
            int lineLength = (int) (height - (fm.bottom - fm.top));
            int size = mChartList.size();
            //向上取整
            double maxBottomName = Math.ceil(mMaxScore);
            int midNum = mLineNum - 1;
            //绘制竖线和底部文本
            for (int i = 0; i < mLineNum; i++) {
                canvas.save();
                double midScore = maxBottomName / midNum * i;
                if (i == midNum) {
                    mBottemText = String.valueOf((int) maxBottomName);
                } else {
                    mBottemText = String.valueOf(midScore);
                    int lastLen = mBottemText.indexOf(".");
                    mBottemText = mBottemText.substring(0, lastLen + 2);
                }
                canvas.drawText(mBottemText, leftWidth + i * lineWidth / mLineNum, height, mBottomFontPaint);
                if (i != 0) {
                    canvas.drawLine(leftWidth + i * lineWidth / mLineNum, lineLength - 15,
                            leftWidth + i * lineWidth / mLineNum, lineLength, mLinePaint);
                } else {
                    canvas.drawLine(leftWidth + i * lineWidth / mLineNum, 0, leftWidth + i * lineWidth / mLineNum,
                            lineLength, mLinePaint);
                }
                canvas.restore();
            }
            //绘制底部横线
            int maxWidth = leftWidth + midNum * lineWidth / mLineNum;
            canvas.drawLine(leftWidth, lineLength, maxWidth, lineLength, mLinePaint);

            //计算最大分和最大分向上取整的比例
            double scoreRatio = mMaxScore / maxBottomName;
            double perScoreRatio = (double) Math.round(scoreRatio * 100) / 100;
            //柱状图最大宽度
            int maxColumnWidth = (int) ((midNum * lineWidth / mLineNum) * perScoreRatio);
            //获取两端文本信息
            Paint.FontMetrics leftFms = mLeftFontPaint.getFontMetrics();
            int leftFmsHeight = (int) (leftFms.bottom - leftFms.top);
            Paint.FontMetrics rightFms = mRightFontPaint.getFontMetrics();
            int rightFmsHeight = (int) (rightFms.bottom - rightFms.top);
            //绘制水平柱状图及左右两端文本
            int n = 0;
            for (int j = size; j > 0; j--) {
                canvas.save();
                String leftName = mChartList.get(j - 1).getLeftName();
                float rightName = mChartList.get(j - 1).getRightName();
                n++;
                int top;
                if (ROWSPACEMODE_MULTIPLE == mRowSpaceMode) {
                    top = mRowSpace * (2 * n - 1) + mColumnHeight * (n - 1);
                } else {
                    top = mRowSpace * n + mColumnHeight * (n - 1);
                }
                int bottom = top + mColumnHeight;
                //计算right,右侧分数和最大分数的比例
                double ratio = (double) rightName / (double) mMaxScore;
                double perRatio = (double) Math.round(ratio * 100) / 100;
                //因为从leftWidth长度位置开始绘制，所以加上一个leftWidth长度
                int right = (int) (maxColumnWidth * perRatio) + leftWidth;
                //绘制左侧文本
                int leftTop = (int) (top + mColumnHeight / 2 - leftFmsHeight / 2 - leftFms.top);
                canvas.drawText(leftName, leftWidth - getTextWidth(mLeftFontPaint, leftName) - mLeftFontSpace, leftTop, mLeftFontPaint);
                //绘制柱状图
                canvas.drawRect(leftWidth, top, right, bottom, mColumnPaint);
                //绘制右侧文本
                String rightText = String.valueOf(rightName);
                int rightTop = (int) (top + mColumnHeight / 2 - rightFmsHeight / 2 - rightFms.top);
                canvas.drawText(rightText
                        , right + mRightFontSpace, rightTop, mRightFontPaint);
                canvas.restore();
            }
        }
    }

    /**
     * 计算view所需绘制的高度
     *
     * @return
     */
    private int getFillerHeight() {
        if (mChartList != null && !mChartList.isEmpty()) {
            int size = mChartList.size();
            Paint.FontMetrics fontMetrics = mBottomFontPaint.getFontMetrics();
            int bottomTextHeight = (int) (fontMetrics.bottom - fontMetrics.top);
            int totalRowSpaceHeight = mRowSpaceMode == ROWSPACEMODE_MULTIPLE ? mRowSpace * 2 * size : mRowSpace * (size + 1);
            mChartViewHeight = mColumnHeight * size + totalRowSpaceHeight + bottomTextHeight;
        }
        return mChartViewHeight;
    }

    /**
     * 根据画笔获取文本高度
     *
     * @param paint
     * @return
     */
    private int getFontMetricsHeight(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return (int) (fontMetrics.bottom - fontMetrics.top);
    }

    /**
     * 计算文本宽度
     *
     * @param paint
     * @param text
     */
    private int getTextWidth(Paint paint, String text) {
        int textWidth = 0;
        if (!TextUtils.isEmpty(text)) {
            int len = text.length();
            float[] widths = new float[len];
            paint.getTextWidths(text, widths);
            for (int i = 0; i < len; i++) {
                textWidth += (int) Math.ceil(widths[i]);
            }
        }
        return textWidth;
    }

    /**
     * 计算文本高度
     *
     * @param paint
     * @param text
     */
    private int getTextHeight(Paint paint, String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    /**
     * 设置展示数据
     *
     * @param list
     */
    public void setData(List<Chart> list) {
        if (list != null) {
            this.mChartList = list;
        }
        mMaxScore = getMaxScore(list);
        setMaxScore(mMaxScore);
        getFillerHeight();
    }

    /**
     * 取出最大值,用于计算柱状图最大长度
     *
     * @param list
     * @return
     */
    private float getMaxScore(List<Chart> list) {
        float maxScroe = 0;
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Chart chart = list.get(i);
                if (i == 0) {
                    maxScroe = chart.getRightName();
                } else {
                    if (maxScroe < chart.getRightName()) {
                        maxScroe = chart.getRightName();
                    }
                }
            }
            return maxScroe;
        }
        return 1;
    }

    /**
     * 设置最大值
     *
     * @param score
     */
    public void setMaxScore(float score) {
        this.mMaxScore = score;
    }
}
