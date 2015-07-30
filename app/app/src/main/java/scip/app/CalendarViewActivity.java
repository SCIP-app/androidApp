package scip.app;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CheckBox;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import scip.app.models.Participant;

public class CalendarViewActivity extends AppCompatActivity {
    private CaldroidFragment caldroidFragment;
    private List<Participant> couple;
    private long couple_id;

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

        couple_id = getIntent().getLongExtra("couple_id", 0);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        couple = db.getCoupleFromID(couple_id);
        db.closeDB();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

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


       CheckBox sexCheckBox = (CheckBox)findViewById(R.id.SexCheck);
        sexCheckBox.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               CalendarCustomAdapterFragment calView = (CalendarCustomAdapterFragment) caldroidFragment;
                                               calView.getInstance().refresh();
                                           }
                                       }
        );




        CheckBox prepCheck = (CheckBox)findViewById(R.id.PrepCheck);
        prepCheck.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               CalendarCustomAdapterFragment calView = (CalendarCustomAdapterFragment)caldroidFragment;
                                               calView.getInstance().refresh();
                                           }
                                       }
        );



        CheckBox sfluidCheck = (CheckBox)findViewById(R.id.CervicalCheck);
        sfluidCheck.setOnClickListener(new OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             CalendarCustomAdapterFragment calView = (CalendarCustomAdapterFragment)caldroidFragment;
                                             calView.getInstance().refresh();
                                         }
                                     }
        );


        CheckBox opkCheck = (CheckBox)findViewById(R.id.OPKCheck);
        opkCheck.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               CalendarCustomAdapterFragment calView = (CalendarCustomAdapterFragment)caldroidFragment;
                                               calView.getInstance().refresh();
                                           }
                                       }
        );

        CheckBox htempCheck = (CheckBox)findViewById(R.id.TempCheck);
        htempCheck.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               CalendarCustomAdapterFragment calView = (CalendarCustomAdapterFragment)caldroidFragment;
                                               calView.getInstance().refresh();
                                           }
                                       }
        );


        // Attach to the activity


        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                LayoutInflater inflater = getLayoutInflater();

                View layout = inflater.inflate(R.layout.popup_detail,
                        (ViewGroup) findViewById(R.id.popUp));

                PopupWindow pw = new PopupWindow(layout,750,450,true);
                pw.setBackgroundDrawable(new ColorDrawable(1));
                TextView dateText = (TextView) pw.getContentView().findViewById(R.id.current_date_text);
                dateText.setText(formatter.format(date));
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
               // Toast.makeText(getApplicationContext(),
                 //       "Long click " + formatter.format(date),
                    //    Toast.LENGTH_SHORT).show();
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

    public List<Participant> getCouple() {
        return couple;
    }
}