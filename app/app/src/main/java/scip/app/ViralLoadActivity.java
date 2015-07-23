package scip.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.BubbleChartView;
import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.Participant;
import scip.app.models.ViralLoad;

import java.util.Calendar;
import java.util.Date;

public class ViralLoadActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viral_load);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    /**
     * A fragment containing a bubble chart.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final int BUBBLES_NUM = 8;

        private BubbleChartView chart;
        private BubbleChartData data;
        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private ValueShape shape = ValueShape.CIRCLE;
        private boolean hasLabels = true;
        private boolean hasLabelForSelected = false;
        private long couple_id;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_viral_load, container, false);

            chart = (BubbleChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            generateData();

            return rootView;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_viral_load, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            return super.onOptionsItemSelected(item);
        }


        private void generateData() {
            couple_id =  getActivity().getIntent().getLongExtra("couple_id", 0);
            chart.getBubbleChartData().setBubbleScale((float)0.1);
            //chart.getBubbleChartData().setBubbleScale((float)(10));
            DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
            List<Participant> couple = db.getCoupleFromID(couple_id);
            db.closeDB();

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(2015,7,1);
            //int month = cal.get(Calendar.MONTH);

            List<BubbleValue> bubbleValues = new ArrayList<BubbleValue>();
            List<ViralLoad> viralLoadList = new ArrayList<ViralLoad>();
            List<BubbleValue> values = new ArrayList<BubbleValue>();

            for(Participant participant:couple) {
                if(participant.isIndex()) {
                    viralLoadList = participant.getViralLoads();

                        for (ViralLoad viralLoad:viralLoadList) {
                            float num = (float)viralLoad.getNumber();
                            Calendar today = Calendar.getInstance();
                            today.setTime(viralLoad.getDate());
                            Date date  = viralLoad.getDate();

                            int changeInMonth = today.get(Calendar.MONTH) - cal.get(Calendar.MONTH);
                            while(changeInMonth <= 0)
                                changeInMonth += 12;
                            BubbleValue value = new BubbleValue((float) changeInMonth, num, num);
                            value.setColor(ChartUtils.pickColor());
                            value.setLabel("aaa");
                            value.setShape(shape);
                            values.add(value);
                        }




                }
            }


            data = new BubbleChartData(values);
            data.setHasLabels(true);
            data.setHasLabelsOnlyForSelected(hasLabelForSelected);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("NUMBER OF MONTHS");
                    axisY.setName("RNA VALUE");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setBubbleChartData(data);

        }



        private class ValueTouchListener implements BubbleChartOnValueSelectListener {

            @Override
            public void onValueSelected(int bubbleIndex, BubbleValue value) {
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }
        }
    }
}


/*
    private void generateData() {
        couple_id =  getActivity().getIntent().getLongExtra("couple_id", 0);
        DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
        List<Participant> couple = db.getCoupleFromID(couple_id);
        db.closeDB();
        List<BubbleValue> bubbleValues = new ArrayList<BubbleValue>();
        List<ViralLoad> viralLoadList = new ArrayList<ViralLoad>();

        for(int i=0;i<couple.size();i++) {
            Participant participant = couple.get(i);
            viralLoadList =  participant.getViralLoads();
            int bubbleNum = viralLoadList.size();
            for(int j = 0;j<viralLoadList.size();j++) {
                ViralLoad viralLoad = viralLoadList.get(j);
                for (int m = 0; m < bubbleNum; ++m) {
                    BubbleValue value = new BubbleValue(m, (float) Math.random() * 100, (float) Math.random() * 1000);
                    value.setColor(R.color.material_blue_500);
                    value.setLabel(String.valueOf(viralLoad.getNumber()));
                    value.setShape(shape);
                    bubbleValues.add(value);
                }

                data = new BubbleChartData(bubbleValues);
                data.setHasLabels(hasLabels);
                data.setHasLabelsOnlyForSelected(true);

                if (hasAxes) {
                    Axis axisX = new Axis();
                    Axis axisY = new Axis().setHasLines(true);
                    if (hasAxesNames) {
                        axisX.setName("Axis X");
                        axisY.setName("Axis Y");
                    }
                    data.setAxisXBottom(axisX);
                    data.setAxisYLeft(axisY);
                } else {
                    data.setAxisXBottom(null);
                    data.setAxisYLeft(null);
                }

                chart.setBubbleChartData(data);

            }

        }
    }
    */
