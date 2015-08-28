package scip.app;

import android.app.Activity;
import android.util.Log;
import android.widget.CheckBox;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidGridAdapter;

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

    private List<Participant> couple = null;
    Participant male = null;
    Participant female = null;
    Participant participant = null;
    Activity activity;
    public View calendarCellView;
    View cellView;
    ImageView unprotectedSex;
    ImageView sfluid;
    ImageView htemp;
    ImageView opk;
    ImageView prep;

    public CustomizedCalendarCellAdapter(Context context, int month, int year,
                                         HashMap<String, Object> caldroidData,
                                         HashMap<String, Object> extraData,List<Participant> couple,Participant participant) {
        super(context, month, year, caldroidData, extraData);
        activity = (Activity) context;
        if(couple!=null) {
            if(couple.get(0).isFemale()) {
                //Log.d("in couple", "female is first");
                female = couple.get(0);
                male = couple.get(1);
            }
            else {
                //Log.d("in couple", "female is second");
                male = couple.get(0);
                female = couple.get(1);
            }
        }
        else {
            //Log.d("couple frag", "couple is null");
        }

        if(participant!=null) {
            if(participant.isFemale()) {
                female = participant;
                male = null;
            } else {
                male = participant;
                female = null;
            }
        }
        else {
            //Log.d("couple frag", "participant is null");
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cellView = convertView;


        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.custom_calendar_cell, null);
        }

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);

        tv1.setTextColor(Color.BLACK);

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        cellView.setBackgroundResource(R.drawable.cell_blank);


        if (dateTime.equals(getToday())) {
            cellView.setBackgroundResource(R.drawable.cell_today);
        }



        // Just show them all for now. Will parse through data soon

        unprotectedSex = (ImageView) cellView.findViewById(R.id.sex);
        unprotectedSex.setVisibility(View.INVISIBLE);
        sfluid = (ImageView) cellView.findViewById(R.id.sfluid);
        sfluid.setVisibility(View.INVISIBLE);
        htemp = (ImageView) cellView.findViewById(R.id.htemp);
        htemp.setVisibility(View.INVISIBLE);
        opk = (ImageView) cellView.findViewById(R.id.opk);
        opk.setVisibility(View.INVISIBLE);
        prep = (ImageView) cellView.findViewById(R.id.prep);
        prep.setVisibility(View.INVISIBLE);


        CheckBox opkCheck = (CheckBox) activity.findViewById(R.id.OPKCheck);
        CheckBox prepCheck = (CheckBox) activity.findViewById(R.id.PrepCheck);
        CheckBox sexCheck = (CheckBox) activity.findViewById(R.id.SexCheck);
        CheckBox sfluidCheck = (CheckBox) activity.findViewById(R.id.CervicalCheck);
        CheckBox htempCheck = (CheckBox) activity.findViewById(R.id.TempCheck);

        Participant participant = null;
        if(male!=null) {
                participant = male;
        }

        if(female!=null) {
                participant = female;
        }
        Calendar calendar = Calendar.getInstance();

        if(participant!=null) {
            if (!participant.isIndex() && participant.getMemscaps() != null) {
                List<MemsCap> memsCaps = participant.getMemscaps();
                for (MemsCap memsCap : memsCaps) {
                    Date memsCapDate = memsCap.getDate();
                    calendar.setTime(memsCapDate);

                    if ((calendar.get(Calendar.MONTH) == (dateTime.getMonth() - 1)) && (calendar.get(Calendar.YEAR) == dateTime.getYear()) && (calendar.get(Calendar.DAY_OF_MONTH) == dateTime.getDay())) {
                        prep.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        if(female!=null) {
            List<SurveyResult> surveyResults = female.getSurveyResults();
            PeakFertility fertility = female.getPeakFertility();
            if(fertility!=null) {
                List<Date> fertilityWindow = fertility.getPeakFertilityWindow();
                if(fertilityWindow.size()>0) {
                    for (Date fertilityVal : fertilityWindow) {
                        Calendar fertilityCalendar = Calendar.getInstance();
                        fertilityCalendar.setTime(fertilityVal);

                        if ((fertilityCalendar.get(Calendar.MONTH) == (dateTime.getMonth() - 1)) && (fertilityCalendar.get(Calendar.YEAR) == dateTime.getYear()) && (fertilityCalendar.get(Calendar.DAY_OF_MONTH) == dateTime.getDay())) {
                            cellView.setBackgroundResource(R.drawable.cellborder);
                        }

                    }
                }
            }

            if(surveyResults != null && surveyResults.size()>0)  {
                for (SurveyResult surveyResult : surveyResults) {
                    calendar.setTime(surveyResult.getDate());
                    if ((calendar.get(Calendar.MONTH) == dateTime.getMonth() - 1) && (calendar.get(Calendar.YEAR) == dateTime.getYear()) && (calendar.get(Calendar.DAY_OF_MONTH) == dateTime.getDay())) {
                        if (surveyResult != null) {
                            if (surveyResult.isOvulating() && opkCheck.isChecked()) {
                                opk.setVisibility(View.VISIBLE);
                            }
                            if (surveyResult.isHadSex() && !surveyResult.isUsedCondom() && sexCheck.isChecked()) {
                                unprotectedSex.setVisibility(View.VISIBLE);
                            }
                            if ((surveyResult.getTemperature() >= 36.6) && htempCheck.isChecked()) {
                                htemp.setVisibility(View.VISIBLE);
                            }
                            if (surveyResult.isVaginaMucusSticky() && sfluidCheck.isChecked()) {
                                sfluid.setVisibility(View.VISIBLE);
                            }
                            if (surveyResult.isOnPeriod()) {
                                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
                            }

                        }
                    }
                }
            }
        }

        if(!sexCheck.isChecked()) {
            unprotectedSex.setVisibility(View.INVISIBLE);
        }

        if(!htempCheck.isChecked()) {
            htemp.setVisibility(View.INVISIBLE);
        }

        if(!opkCheck.isChecked()) {
            opk.setVisibility(View.INVISIBLE);
        }

        if(!sfluidCheck.isChecked()) {
            sfluid.setVisibility(View.INVISIBLE);
        }

        if(!prepCheck.isChecked()) {
            prep.setVisibility(View.INVISIBLE);
        }


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







}
