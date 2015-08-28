package scip.app;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;

import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.charts.ScatterChart.ScatterShape;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;

import java.util.HashMap;
import java.util.List;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.Participant;
import scip.app.models.ViralLoad;

import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.highlight.Highlight;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;

public class ViralLoadActivity extends AppCompatActivity implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private ScatterChart mChart;
    private long couple_id;
    private long participant_id;
    private DatabaseHelper db;
    private List<Participant> couple;
    private Participant indexParticipant;
    private Participant participant;
    HashMap<Integer,String> monthMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_viral_load);
        couple_id =  getIntent().getLongExtra("couple_id", 0);
        participant_id =  getIntent().getLongExtra("participant_id", 0);

        if(participant_id!=0) {
            db = new DatabaseHelper(getApplicationContext());
            participant = db.getParticipant(participant_id);
        }

        if(couple_id!= 0) {
            db = new DatabaseHelper(getApplicationContext());
            couple = db.getCoupleFromID(couple_id);
        }

        db.closeDB();
        monthMap = new HashMap<Integer,String>();
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

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        if(participant!=null) {
           if(participant.isIndex()) {
               indexParticipant = participant;
           }
        }

        if(couple!=null) {
            for (Participant participant : couple) {
                if (participant.isIndex()) {
                    indexParticipant = participant;
                }
            }
        }



        if(indexParticipant!=null) {
                    List<ViralLoad> viralLoadList = indexParticipant.getViralLoads();
                    if(viralLoadList!=null && viralLoadList.size()>0) {
                        int count1 = 0;
                        int count2 = 0;
                        for(ViralLoad viralLoad:viralLoadList) {
                            if(viralLoad.getNumber()<400) {
                                yVals2.add(new Entry(viralLoad.getNumber(),count1));
                                count1++;
                            } else {
                                yVals1.add(new Entry(viralLoad.getNumber(),count2));
                                count2++;
                            }

                            Calendar calendar = Calendar.getInstance();
                            Date date = viralLoad.getDate();
                            calendar.setTime(date);
                            int year = 2015;
                            int month = calendar.get(Calendar.MONTH);
                            if(calendar.get(Calendar.YEAR)>year) {
                                int diff = calendar.get(Calendar.YEAR) - year;
                                month = month + (12*diff);
                            }
                            if(calendar.get(Calendar.YEAR)<year){
                                int diff = year - calendar.get(Calendar.YEAR);
                                month = month*diff;
                            }
                            String xLabel = monthMap.get(calendar.get(Calendar.MONTH)+1)+" "+calendar.get(Calendar.YEAR);
                            xVals.add(xLabel);
                        }
                    }

        }



        mChart = (ScatterChart) findViewById(R.id.chart1);
        mChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
        mChart.setDescription("");


        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawGridBackground(true);

        mChart.setTouchEnabled(true);
        mChart.setHighlightEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart.setMaxVisibleValueCount(200);
        mChart.setPinchZoom(true);

        // create a dataset and give it a type
        ScatterDataSet set1 = new ScatterDataSet(yVals1, "Viral load >1000");
        set1.setScatterShape(ScatterShape.CIRCLE);
        set1.setColor(getResources().getColor(R.color.material_orange_800));
        set1.setScatterShapeSize(40f);

        ScatterDataSet set2 = new ScatterDataSet(yVals2, "Viral load < 400");
        set2.setScatterShape(ScatterShape.CIRCLE);
        set2.setColor(getResources().getColor(R.color.material_green_800));
        set2.setScatterShapeSize(40f);


        ArrayList<ScatterDataSet> dataSets = new ArrayList<ScatterDataSet>();
        dataSets.add(set1); // add the datasets
        dataSets.add(set2); // add the datasets


        // create a data object with the datasets
        ScatterData data = new ScatterData(xVals, dataSets);

        mChart.setData(data);
        mChart.invalidate();


        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.RIGHT_OF_CHART);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawGridLines(false);

        mChart.getAxisRight().setEnabled(false);

        XAxis xl = mChart.getXAxis();
        xl.setDrawGridLines(false);
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

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
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