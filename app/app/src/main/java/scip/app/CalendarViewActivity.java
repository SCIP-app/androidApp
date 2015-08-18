
package scip.app;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CheckBox;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.util.AttributeSet;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.Gravity;

import scip.app.databasehelper.DatabaseHelper;
import scip.app.models.MemsCap;
import scip.app.models.Participant;
import scip.app.models.SurveyResult;

public class CalendarViewActivity extends ActionBarActivity {
    private CaldroidFragment caldroidFragment;
    private List<Participant> couple;
    private Participant participant;
    Participant male;
    Participant female;
    private long couple_id;
    private long participant_id;

    HashMap<Integer,String> monthMap = new HashMap<>();


    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            caldroidFragment.setBackgroundResourceForDate(R.color.material_blue_500,
                    blueDate);
            caldroidFragment.setBackgroundResourceForDate(R.color.material_deep_teal_200,
                    greenDate);
            caldroidFragment.setTextColorForDate(R.color.abc_secondary_text_material_light, blueDate);
            caldroidFragment.setTextColorForDate(R.color.abc_primary_text_disable_only_material_light, greenDate);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
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
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        couple_id = getIntent().getLongExtra("couple_id", 0);
        participant_id = getIntent().getLongExtra("participant_id", 0);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        if(couple_id !=0) {
            couple = db.getCoupleFromID(couple_id);
            db.closeDB();
        }

        if(participant_id!=0) {
            participant = db.getParticipant(participant_id);
            if(participant.isFemale()) {
                female = participant;
            } else {
                male = participant;
            }
            db.closeDB();
        }

        TextView nextPeakFertilityTextView = (TextView) findViewById(R.id.peakFertilityValue);
        TextView averageCycle = (TextView) findViewById(R.id.AvgCycleValue);


        if(couple!=null && couple.size()==2) {
            female = couple.get(0);
            male = couple.get(1);
        }

        if(female!=null) {
            if (female.getPeakFertility() != null) {
                List<Date> fertilityWindow = female.getPeakFertility().getPeakFertilityWindow();
                if (fertilityWindow != null && fertilityWindow.size() == 4) {
                    String fertility = formatter.format(fertilityWindow.get(0)) + " - " + formatter.format(fertilityWindow.get(fertilityWindow.size() - 1));
                    nextPeakFertilityTextView.setText(fertility);
                }
                if (female.getPeakFertility().getAverageCycleLength() != -1) {
                    String averageCycleValue = String.valueOf((int) female.getPeakFertility().getAverageCycleLength());
                    averageCycle.setText(averageCycleValue);
                }
            }
        }


        caldroidFragment = new CalendarCustomAdapterFragment();


        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);
        }

        final FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        final CheckBox sexCheck = (CheckBox)findViewById(R.id.SexCheck);
        sexCheck.setOnClickListener(new OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             caldroidFragment.refreshView();
                                         }
                                         }

        );



        final CheckBox prepCheck = (CheckBox)findViewById(R.id.PrepCheck);
        prepCheck.setOnClickListener(new OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             caldroidFragment.refreshView();
                                         }
                                     }
        );



        final CheckBox sfluidCheck = (CheckBox)findViewById(R.id.CervicalCheck);
        sfluidCheck.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               caldroidFragment.refreshView();
                                           }
                                       }
        );


        final CheckBox opkCheck = (CheckBox)findViewById(R.id.OPKCheck);
        opkCheck.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                                caldroidFragment.refreshView();
                                        }

                                    }
        );

        final CheckBox htempCheck = (CheckBox)findViewById(R.id.TempCheck);
        htempCheck.setOnClickListener(new OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                                 caldroidFragment.refreshView();
                                          }
                                      }
        );





        // Attach to the activity


        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(final Date date, View view) {
                LayoutInflater inflater = getLayoutInflater();

                View layout = inflater.inflate(R.layout.popup_detail,
                        (ViewGroup) findViewById(R.id.popUp));

                PopupWindow pw = new PopupWindow(layout, 750, 450, true);
                TextView dateText = (TextView) pw.getContentView().findViewById(R.id.current_date_text);
                ImageView prepPopupImage = (ImageView) pw.getContentView().findViewById(R.id.prepicon);
                ImageView sexPopupImage = (ImageView) pw.getContentView().findViewById(R.id.sexIcon);
                ImageView positivePopupImage = (ImageView) pw.getContentView().findViewById(R.id.positiveicon);
                ImageView stickyPopupImage = (ImageView) pw.getContentView().findViewById(R.id.stickyicon);
                ImageView htempPopUpIcon = (ImageView) pw.getContentView().findViewById(R.id.htempicon);
                ImageView cyclePopUpIcon = (ImageView) pw.getContentView().findViewById(R.id.cycleicon);

                TextView prepPopupText = (TextView) pw.getContentView().findViewById(R.id.preplabel);
                TextView sexPopupText = (TextView) pw.getContentView().findViewById(R.id.sexlabel);
                TextView positivePopupText = (TextView) pw.getContentView().findViewById(R.id.positivelabel);
                TextView stickyPopupText = (TextView) pw.getContentView().findViewById(R.id.stickylabel);
                TextView htempPopUpText = (TextView) pw.getContentView().findViewById(R.id.htemplabel);
                TextView cycleLabelText = (TextView) pw.getContentView().findViewById(R.id.cyclelabel);
                TextView cycleDayInCycleText = (TextView) pw.getContentView().findViewById(R.id.dayInCycle);

                Participant participant = null;
                if (male != null) {
                    if (male.isIndex()) {
                        participant = male;
                    }
                }

                if (female != null) {
                    if (female.isIndex()) {
                        participant = female;
                    }
                }
                if (participant != null) {
                    if (!participant.isIndex()) {
                        if (participant.getMemscaps() != null) {
                            for (MemsCap memsCap : participant.getMemscaps()) {
                                if (date.compareTo(memsCap.getDate()) == 0) {
                                    prepPopupImage.setVisibility(View.VISIBLE);
                                    prepPopupText.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }

                if (female != null) {

                    cycleLabelText.setVisibility(View.VISIBLE);
                    cyclePopUpIcon.setVisibility(View.VISIBLE);
                    cycleDayInCycleText.setVisibility(View.VISIBLE);

                    if (female.getPeakFertility() != null) {
                        long dayInCycle = female.getPeakFertility().getDayInCycle(date);
                        cycleDayInCycleText.setText(String.valueOf(dayInCycle));

                    }
                    if (female.getSurveyResults() != null) {
                        for (SurveyResult surveyResult : female.getSurveyResults()) {
                            if (date.compareTo(surveyResult.getDate()) == 0) {

                                if (surveyResult.isOvulating()) {
                                    positivePopupImage.setVisibility(View.VISIBLE);
                                    positivePopupText.setVisibility(View.VISIBLE);
                                }
                                if (surveyResult.isHadSex() && !surveyResult.isUsedCondom()) {
                                    sexPopupImage.setVisibility(View.VISIBLE);
                                    sexPopupText.setVisibility(View.VISIBLE);
                                }
                                if (surveyResult.getTemperature() >= 97.8) {
                                    htempPopUpIcon.setVisibility(View.VISIBLE);
                                    htempPopUpText.setVisibility(View.VISIBLE);
                                }
                                if (surveyResult.isVaginaMucusSticky()) {
                                    stickyPopupImage.setVisibility(View.VISIBLE);
                                    stickyPopupText.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                }

                dateText.setText(formatter.format(date));
                pw.setBackgroundDrawable(new ColorDrawable(1));
                pw.dismiss();
                pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }


            @Override
            public void onChangeMonth(int month, int year) {
                String text = monthMap.get(month) + " " + year;
                TextView monthText = (TextView) findViewById(R.id.monthLabel);
                monthText.setText(text);
            }

            @Override
            public void onLongClickDate(Date date, View view) {

            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null && caldroidFragment.getRightArrowButton()!=null) {
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

    }

    public Participant getParticipant() {
        return participant;
    }

    public List<Participant> getCouple() {
        return couple;
    }
}