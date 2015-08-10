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
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
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


        private BubbleChartView chart;
        private BubbleChartData data;
        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private ValueShape shape = ValueShape.CIRCLE;
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
            DatabaseHelper db = new DatabaseHelper(getActivity().getApplicationContext());
            List<Participant> couple = db.getCoupleFromID(couple_id);
            db.closeDB();

            List<ViralLoad> viralLoadList = new ArrayList<ViralLoad>();
            List<BubbleValue> values = new ArrayList<BubbleValue>();
            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<AxisValue> axisYValues = new ArrayList<AxisValue>();

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



            for(Participant participant:couple) {
                if(participant.isIndex()) {
                    viralLoadList = participant.getViralLoads();
                        for (ViralLoad viralLoad:viralLoadList) {
                            int year = 2015;
                            if(viralLoad!=null) {
                                Calendar calendar = Calendar.getInstance();
                                Date date = viralLoad.getDate();

                                float num = (float) viralLoad.getNumber();
                                calendar.setTime(date);
                                int month = calendar.get(Calendar.MONTH);
                                if(calendar.get(Calendar.YEAR)>year) {
                                    int diff = calendar.get(Calendar.YEAR) - year;
                                    month = month + (12*diff);
                                }
                                if(calendar.get(Calendar.YEAR)<year){
                                    int diff = year - calendar.get(Calendar.YEAR);
                                    month = month*diff;
                                }

                                float bubbleSize = (float) viralLoad.getNumber();
                                if(bubbleSize<=1000) {
                                    bubbleSize = 15;

                                }

                                if(bubbleSize>1000 && bubbleSize<=10000) {
                                    bubbleSize = 30;
                                }

                                if(bubbleSize>10000) {
                                    bubbleSize = 45;
                                }

                                BubbleValue value = new BubbleValue((float) month, num, bubbleSize);

                                if(num <1000) {
                                    value.setColor(getResources().getColor(R.color.material_green_800));
                                } else {
                                    value.setColor(getResources().getColor(R.color.material_orange_800));
                                }

                                AxisValue axisValue = new AxisValue(month);
                                AxisValue axisYValue = new AxisValue(num);
                                axisValue.setLabel(monthMap.get(calendar.get(Calendar.MONTH)+1)+" "+calendar.get(Calendar.YEAR));
                                axisYValue.setLabel(String.valueOf(num));
                                axisValues.add(axisValue);
                                axisYValues.add(axisYValue);

                                value.setLabel(String.valueOf(viralLoad.getNumber()));
                                value.setShape(shape);
                                values.add(value);

                            }

                        }




                }
            }


            data = new BubbleChartData(values);
            data.setHasLabels(true);
            data.setHasLabelsOnlyForSelected(hasLabelForSelected);

            if (hasAxes) {
                Axis axisX = new Axis();
                axisX.setValues(axisValues);
                Axis axisY = new Axis().setHasLines(false);
                axisY.setValues(axisYValues);

                if (hasAxesNames) {
                    axisX.setName("MONTHS");
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

