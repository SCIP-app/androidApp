package scip.app;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hirondelle.date4j.DateTime;
import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.Participant;
import scip.app.models.SurveyResult;
import java.util.Date;

public class CustomizedCalendarCellAdapter extends CaldroidGridAdapter {

    private List<Participant> couple;
    Intent main_intent;
    private long couple_id;
    private Context main_activity;

    public CustomizedCalendarCellAdapter(Activity context,Intent intent,int month, int year,
                                       HashMap<String, Object> caldroidData,
                                       HashMap<String, Object> extraData) {

        super(context.getApplicationContext(),month, year, caldroidData, extraData);
        main_intent = intent;
        main_activity = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;


        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_calendar_cell, null);
        }

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
       // TextView tv2 = (TextView) cellView.findViewById(R.id.tv2);

        tv1.setTextColor(Color.BLACK);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        String dates = dateTime.getRawDateString();
        Resources resources = context.getResources();

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources
                    .getColor(com.caldroid.R.color.caldroid_darker_gray));
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;


        if (dateTime.equals(getToday())) {
            cellView.setBackgroundColor(R.color.material_blue_500);
        }

        // Just show them all for now. Will parse through data soon
        
        ImageView unprotectedSex = (ImageView) cellView.findViewById(R.id.sex);
        unprotectedSex.setVisibility(View.VISIBLE);
        ImageView sfluid = (ImageView) cellView.findViewById(R.id.sfluid);
        sfluid.setVisibility(View.VISIBLE);
        ImageView htemp = (ImageView) cellView.findViewById(R.id.htemp);
        htemp.setVisibility(View.VISIBLE);
        ImageView opk = (ImageView) cellView.findViewById(R.id.opk);
        opk.setVisibility(View.VISIBLE);
        ImageView prep = (ImageView) cellView.findViewById(R.id.prep);
        prep.setVisibility(View.VISIBLE);

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setBackgroundColor(resources
                    .getColor(com.caldroid.R.color.caldroid_sky_blue));

            tv1.setTextColor(Color.BLACK);

        }


        if (dateTime.equals(getToday())) {
            cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
        } else {
            cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
        }


        tv1.setText("" + dateTime.getDay());
        

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        return cellView;
    }
}