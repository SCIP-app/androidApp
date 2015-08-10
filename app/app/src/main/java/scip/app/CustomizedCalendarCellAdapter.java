package scip.app;

import android.app.Activity;
import android.widget.CheckBox;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton;

import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CellView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;
import java.util.List;

import hirondelle.date4j.DateTime;
import scip.app.models.MemsCap;
import scip.app.models.Participant;
import scip.app.models.PeakFertility;
import scip.app.models.SurveyResult;


public class CustomizedCalendarCellAdapter extends CaldroidGridAdapter {

    private List<Participant> couple;
    Activity activity;
    public View calendarCellView;

    public CustomizedCalendarCellAdapter(Context context, int month, int year,
                                         HashMap<String, Object> caldroidData,
                                         HashMap<String, Object> extraData,List<Participant> couple) {
        super(context, month, year, caldroidData, extraData);
        activity = (Activity) context;
        this.couple = couple;

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



        if (dateTime.equals(getToday())) {
            cellView.setBackgroundColor(R.color.material_blue_500);
        }



        // Just show them all for now. Will parse through data soon

        final ImageView unprotectedSex = (ImageView) cellView.findViewById(R.id.sex);
        unprotectedSex.setVisibility(View.INVISIBLE);
        final ImageView sfluid = (ImageView) cellView.findViewById(R.id.sfluid);
        sfluid.setVisibility(View.INVISIBLE);
        final ImageView htemp = (ImageView) cellView.findViewById(R.id.htemp);
        htemp.setVisibility(View.INVISIBLE);
        final ImageView opk = (ImageView) cellView.findViewById(R.id.opk);
        opk.setVisibility(View.INVISIBLE);
        final ImageView prep = (ImageView) cellView.findViewById(R.id.prep);
        prep.setVisibility(View.INVISIBLE);

        for (Participant participant: couple) {
            Calendar calendar = Calendar.getInstance();
            if(!participant.isIndex() && participant.getMemscaps()!=null) {
                List<MemsCap> memsCaps = participant.getMemscaps();
                for(MemsCap memsCap:memsCaps) {
                    Date memsCapDate = memsCap.getDate();
                    calendar.setTime(memsCapDate);

                    if((calendar.get(Calendar.MONTH) == (dateTime.getMonth()-1)) && (calendar.get(Calendar.YEAR) == dateTime.getYear()) && (calendar.get(Calendar.DAY_OF_MONTH) == dateTime.getDay())){
                        prep.setVisibility(View.VISIBLE);
                    }
                }
            }

            if(participant.isFemale()) {
                List<SurveyResult> surveyResults = participant.getSurveyResults();
                PeakFertility fertility = participant.getPeakFertility();
                if(fertility!=null) {
                    List<Date> fertilityWindow = participant.getPeakFertility().getPeakFertilityWindow();
                    if(fertilityWindow!=null && fertilityWindow.size()>0) {
                        for(Date fertilityDate:fertilityWindow) {
                            calendar.setTime(fertilityDate);
                            if((calendar.get(Calendar.MONTH) == dateTime.getMonth()-1) && (calendar.get(Calendar.YEAR) == dateTime.getYear()) && (calendar.get(Calendar.DAY_OF_MONTH) == dateTime.getDay())) {
                                cellView.setBackgroundResource(R.color.color_fertile);
                            }
                        }
                    }
                }

                for(SurveyResult surveyResult:surveyResults) {
                    calendar.setTime(surveyResult.getDate());
                    if((calendar.get(Calendar.MONTH) == dateTime.getMonth()-1) && (calendar.get(Calendar.YEAR) == dateTime.getYear()) && (calendar.get(Calendar.DAY_OF_MONTH) == dateTime.getDay())){
                        if(surveyResult!=null) {
                           if (surveyResult.isOvulating()) {
                               opk.setVisibility(View.VISIBLE);
                           }
                           if(surveyResult.isHadSex() && !surveyResult.isUsedCondom()) {
                               unprotectedSex.setVisibility(View.VISIBLE);
                           }
                           if(surveyResult.getTemperature()>=97.8) {
                              htemp.setVisibility(View.VISIBLE);
                           }
                           if(surveyResult.isVaginaMucusSticky()) {
                               sfluid.setVisibility(View.VISIBLE);
                           }
                            if(surveyResult.isOnPeriod()) {
                                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
                            }
                       }
                    }
                }
            }
        }



        /*

        if (dateTime.equals(getToday())) {
            cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
        } else {
            cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
        }
        */


        tv1.setText("" + dateTime.getDay());


        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        calendarCellView = cellView;
        return cellView;
    }


    public void refreshView() {

        final ImageView unprotectedSex = (ImageView) calendarCellView.findViewById(R.id.sex);
        unprotectedSex.setVisibility(View.INVISIBLE);
        final ImageView sfluid = (ImageView) calendarCellView.findViewById(R.id.sfluid);
        sfluid.setVisibility(View.INVISIBLE);
        final ImageView htemp = (ImageView) calendarCellView.findViewById(R.id.htemp);
        htemp.setVisibility(View.INVISIBLE);
        final ImageView opk = (ImageView) calendarCellView.findViewById(R.id.opk);
        opk.setVisibility(View.INVISIBLE);
        final ImageView prep = (ImageView) calendarCellView.findViewById(R.id.prep);
        prep.setVisibility(View.INVISIBLE);

        final CheckBox sexCheck = (CheckBox) calendarCellView.findViewById(R.id.SexCheck);
        if (sexCheck.isChecked()) {
            prep.setVisibility(View.INVISIBLE);
            htemp.setVisibility(View.INVISIBLE);
            opk.setVisibility(View.INVISIBLE);
            sfluid.setVisibility(View.INVISIBLE);
        }
    }


}
