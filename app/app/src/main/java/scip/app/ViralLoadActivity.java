package scip.app;

import android.graphics.Color;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.BubbleChartView;
import lecho.lib.hellocharts.view.Chart;

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

        private static final int BUBBLES_NUM = 5;

        private BubbleChartView chart;
        private BubbleChartData data;
        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private ValueShape shape = ValueShape.CIRCLE;
        private boolean hasLabels = false;
        private boolean hasLabelForSelected = false;

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
            inflater.inflate(R.menu.dashboard, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            /*
            int id = item.getItemId();
            if (id == R.id.action_reset) {
                reset();
                generateData();
                return true;
            }
            if (id == R.id.action_shape_circles) {
                setCircles();
                return true;
            }
            if (id == R.id.action_shape_square) {
                setSquares();
                return true;
            }
            if (id == R.id.action_toggle_labels) {
                toggleLabels();
                return true;
            }
            if (id == R.id.action_toggle_axes) {
                toggleAxes();
                return true;
            }
            if (id == R.id.action_toggle_axes_names) {
                toggleAxesNames();
                return true;
            }
            if (id == R.id.action_animate) {
                prepareDataAnimation();
                chart.startDataAnimation();
                return true;
            }
            if (id == R.id.action_toggle_selection_mode) {
                toggleLabelForSelected();
                Toast.makeText(getActivity(),
                        "Selection mode set to " + chart.isValueSelectionEnabled() + " select any point.",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.action_toggle_touch_zoom) {
                chart.setZoomEnabled(!chart.isZoomEnabled());
                Toast.makeText(getActivity(), "IsZoomEnabled " + chart.isZoomEnabled(), Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.action_zoom_both) {
                chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
                return true;
            }
            if (id == R.id.action_zoom_horizontal) {
                chart.setZoomType(ZoomType.HORIZONTAL);
                return true;
            }
            if (id == R.id.action_zoom_vertical) {
                chart.setZoomType(ZoomType.VERTICAL);
                return true;
            }
            */
            return super.onOptionsItemSelected(item);
        }

        private void reset() {
            hasAxes = true;
            hasAxesNames = true;
            shape = ValueShape.CIRCLE;
            hasLabels = false;
            hasLabelForSelected = false;

            chart.setValueSelectionEnabled(hasLabelForSelected);
        }

        private void generateData() {

            List<BubbleValue> values = new ArrayList<BubbleValue>();
            for (int i = 0; i < 4; ++i) {
                BubbleValue value = new BubbleValue((float) Math.random() * 100, (float) Math.random() * 100,(float)0.01);
                value.setColor(Color.BLUE);

                //Viral Load text
                value.setLabel("2/22");
                value.setShape(shape);
                values.add(value);
            }

            data = new BubbleChartData(values);
            data.setHasLabels(true);
            data.setHasLabelsOnlyForSelected(hasLabelForSelected);

            List<AxisValue> xAxisValues = new ArrayList<AxisValue>();
            for (int i = 0; i < 25; i += 1) {
                xAxisValues.add(new AxisValue(i));
            }

            List<AxisValue> yAxisValues = new ArrayList<AxisValue>();

            AxisValue y1 = new AxisValue(10);
            y1.setLabel("10^2");
            yAxisValues.add(y1);

            AxisValue y2 = new AxisValue(20);
            y2.setLabel("10^3");
            yAxisValues.add(y2);

            AxisValue y3 = new AxisValue(30);
            y3.setLabel("10^4");
            yAxisValues.add(y3);

            AxisValue y4 = new AxisValue(40);
            y4.setLabel("10^5");
            yAxisValues.add(y4);

            AxisValue y5 = new AxisValue(50);
            y5.setLabel("10^6");
            yAxisValues.add(y5);

            AxisValue y6 = new AxisValue(60);
            y6.setLabel("10^7");
            yAxisValues.add(y6);

            AxisValue y7 = new AxisValue(70);
            y7.setLabel("10^8");
            yAxisValues.add(y7);

            AxisValue y8 = new AxisValue(80);
            y8.setLabel("10^9");
            yAxisValues.add(y8);

            AxisValue y9 = new AxisValue(90);
            y9.setLabel("10^10");
            yAxisValues.add(y9);


            if (hasAxes) {
                Axis axisX = new Axis();
                axisX.setValues(xAxisValues);


                Axis axisY = new Axis().setHasLines(true);
                axisY.setValues(yAxisValues);
                if (hasAxesNames) {
                    axisX.setName("NUMBER OF MONTHS");
                    axisY.setName("HIV RNA COPIES PER ML PLASMA");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setBubbleChartData(data);

        }

        private void setCircles() {
            shape = ValueShape.CIRCLE;
            generateData();
        }

        private void setSquares() {
            shape = ValueShape.SQUARE;
            generateData();
        }

        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }

            generateData();
        }

        private void toggleLabelForSelected() {
            hasLabelForSelected = !hasLabelForSelected;

            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelForSelected) {
                hasLabels = false;
            }

            generateData();
        }

        private void toggleAxes() {
            hasAxes = !hasAxes;

            generateData();
        }

        private void toggleAxesNames() {
            hasAxesNames = !hasAxesNames;

            generateData();
        }

        /**
         * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
         * method(don't confuse with View.animate()).
         */
        private void prepareDataAnimation() {
            for (BubbleValue value : data.getValues()) {
                value.setTarget(value.getX() + (float) Math.random() * 4 * getSign(), (float) Math.random() * 100,
                        (float) Math.random() * 1000);
            }
        }

        private int getSign() {
            int[] sign = new int[]{-1, 1};
            return sign[Math.round((float) Math.random())];
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