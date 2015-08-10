

package scip.app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.filter.Approximator;
import com.github.mikephil.charting.data.filter.Approximator.ApproximatorType;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.Participant;
import scip.app.models.ViralLoad;

public class ViralLoadActivity extends Activity implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private BubbleChart mChart;
    XAxis xl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_viral_load);



        mChart = (BubbleChart) findViewById(R.id.chart1);
        mChart.setDescription("");


        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawGridBackground(false);
        mChart.setTouchEnabled(true);
        mChart.setHighlightEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        //mChart.setMaxVisibleValueCount(200);
        mChart.setPinchZoom(true);

        mChart.getAxisLeft().setStartAtZero(true);
        mChart.getAxisRight().setStartAtZero(false);

        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);

        YAxis yl = mChart.getAxisLeft();
        yl.setSpaceTop(30f);
        yl.setStartAtZero(false);
        yl.setSpaceBottom(30f);

        mChart.getAxisRight().setEnabled(false);

        xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.mLabelWidth = 10;
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    /*
        int count = mSeekBarX.getProgress() + 1;
        int range = mSeekBarY.getProgress();

        tvX.setText("" + count);
        tvY.setText("" + range);

        mChart.invalidate();
        */
    }

    public void loadData() {


        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Participant> couple = db.getCoupleFromID(getIntent().getLongExtra("couple_id", 0));
        db.closeDB();

        HashMap<Integer,String> monthMap = new HashMap<Integer,String>();
        monthMap.put(1,"Jan");
        monthMap.put(2,"Feb");
        monthMap.put(3,"Mar");
        monthMap.put(4,"Apr");
        monthMap.put(5,"May");
        monthMap.put(6,"Jun");
        monthMap.put(7,"Jul");
        monthMap.put(8,"Aug");
        monthMap.put(9,"Sep");
        monthMap.put(10,"Oct");
        monthMap.put(11,"Nov");
        monthMap.put(12,"Dec");

        ArrayList<BubbleEntry> yVals1 = new ArrayList<BubbleEntry>();

        for(Participant participant:couple) {

            int index = 0;
            if (participant.isIndex()) {
                List<ViralLoad> viralLoadList = participant.getViralLoads();
                for (ViralLoad viralLoad : viralLoadList) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(viralLoad.getDate());
                    BubbleEntry bubbleEntry = new BubbleEntry(index, viralLoad.getNumber(), viralLoad.getNumber());
                    bubbleEntry.setSize(5f);
                    yVals1.add(bubbleEntry);
                    index++;
                }
            }
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for(Participant participant:couple) {

            int index = 0;
            if (participant.isIndex()) {
                List<ViralLoad> viralLoadList = participant.getViralLoads();
                for (ViralLoad viralLoad : viralLoadList) {
                    xVals.add(String.valueOf(viralLoad.getDate()));
                }
            }
        }

            // create a dataset and give it a type
            BubbleDataSet set1 = new BubbleDataSet(yVals1, "DS 1");
            set1.setColor(ColorTemplate.COLORFUL_COLORS[0], 130);
            set1.setDrawValues(true);


            BubbleDataSet dataSet = set1;

            // create a data object with the datasets
            BubbleData data = new BubbleData(xVals, dataSet);
            data.setValueTextSize(8f);
            data.setValueTextColor(Color.BLACK);
            data.setHighlightCircleWidth(1.5f);

            mChart.setData(data);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }
}