
package scip.app;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
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
    private List<Participant> couple = null;
    private Participant participant = null;
    Participant male = null;
    Participant female = null;
    private long couple_id;
    private long participant_id;

    HashMap<Integer,String> monthMap = new HashMap<>();


    private void endSession() {
        Intent intent = new Intent(this, SessionSelectionActivity.class);
        startActivity(intent);
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
        CheckBox prepCheck = (CheckBox) findViewById(R.id.PrepCheck);
        CheckBox tempCheck = (CheckBox) findViewById(R.id.TempCheck);
        CheckBox cervicalCheck = (CheckBox) findViewById(R.id.CervicalCheck);
        CheckBox sexCheck = (CheckBox) findViewById(R.id.SexCheck);
        CheckBox opkCheck = (CheckBox) findViewById(R.id.OPKCheck);
        CheckBox sfluidCheck = (CheckBox) findViewById(R.id.CervicalCheck);

        couple_id = getIntent().getLongExtra("couple_id", 0);
        participant_id = getIntent().getLongExtra("participant_id", 0);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        if(couple_id !=0) {
            //Log.d("couple", "got couple");
            couple = db.getCoupleFromID(couple_id);
            if(couple.size() > 2) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.couple_num_error_message)
                        .setPositiveButton(R.string.couple_num_error_positive, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do nothing and let them continue
                            }
                        })
                        .setNegativeButton(R.string.couple_num_error_negative, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                endSession();
                            }
                        });
                // Create the AlertDialog object and return it
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        else {
            couple = null;
        }

        if(participant_id!=0) {
            participant = db.getParticipant(participant_id);
            if(participant.isFemale()) {
                female = participant;
            } else {
                male = participant;
                if(male!=null && !male.isIndex()) {
                    sexCheck.setVisibility(View.INVISIBLE);
                    tempCheck.setVisibility(View.INVISIBLE);
                    cervicalCheck.setVisibility(View.INVISIBLE);
                    opkCheck.setVisibility(View.INVISIBLE);
                }
            }
        }
        else {
            //Log.d("couple", "got participant");
            participant = null;
        }
        db.closeDB();

        TextView nextPeakFertilityTextView1 = (TextView) findViewById(R.id.peakFertilityValue1);
        TextView nextPeakFertilityTextView2 = (TextView) findViewById(R.id.peakFertilityValue2);
        TextView averageCycle = (TextView) findViewById(R.id.AvgCycleValue);

        if(couple!=null) {
            if(couple.get(0).isFemale()) {
                female = couple.get(0);
                male = couple.get(1);
            }
            else {
                female = couple.get(1);
                male = couple.get(0);
            }
        }


        if (female!=null && female.getPeakFertility() != null) {
            List<Date> fertilityWindow = female.getPeakFertility().getPeakFertilityWindow();
            if (fertilityWindow != null && fertilityWindow.size() == 8) {
                String fertility_range_1 = formatter.format(fertilityWindow.get(0)) + " - " + formatter.format(fertilityWindow.get(3));
                String fertility_range_2 = formatter.format(fertilityWindow.get(4)) + " - " + formatter.format(fertilityWindow.get(fertilityWindow.size()-1));
                nextPeakFertilityTextView1.setText(fertility_range_1);
                nextPeakFertilityTextView2.setText(fertility_range_2);
            }
            if (female.getPeakFertility().getAverageCycleLength() != -1) {
                String averageCycleValue = String.valueOf((int) female.getPeakFertility().getAverageCycleLength());
                averageCycle.setText(averageCycleValue);
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


        sexCheck.setOnClickListener(new OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             caldroidFragment.refreshView();
                                         }
                                         }

        );




        prepCheck.setOnClickListener(new OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             caldroidFragment.refreshView();
                                         }
                                     }
        );




        sfluidCheck.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               caldroidFragment.refreshView();
                                           }
                                       }
        );


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
                        participant = male;
                }

                if (female != null) {
                        participant = female;
                }

                if (participant != null) {
                    if (!participant.isIndex()) {
                        if (participant.getMemscaps() != null && participant.getMemscaps().size()>0) {
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
                    if (female.getSurveyResults() != null && female.getSurveyResults().size()>0) {
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
                                if (surveyResult.getTemperature() >= 36.6) {
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
               // String text = monthMap.get(month) + " " + year;
                //TextView monthText = (TextView) findViewById(R.id.monthLabel);
                //monthText.setText(text);
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