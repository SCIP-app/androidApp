package scip.app.models;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import scip.app.databasehelper.DatabaseHelper;

/**
 * Created by Allie on 6/22/2015.
 *
 * This is the model for the Participant data structure.
 */
public class Participant {
    long id;
    long participant_id;
    Context context;
    boolean isFemale;
    List<SurveyResult> surveyResults;
    List<ViralLoad> viralLoads;
    List<MemsCap> memscaps;
    List<PeakFertility> peakFertilities;

    // Constructors

    // this constructor only to be used for creating database entries
    public Participant(long participant_id, boolean isFemale) {
        this.participant_id = participant_id;
        this.isFemale = isFemale;
    }

    public Participant(Context context, long participant_id) {
        this.context =  context;
        this.participant_id = participant_id;
        loadData();
    }

    public Participant(Context context, long id, long participant_id, int isFemale) {
        this.context = context;
        this.id = id;
        this.participant_id = participant_id;
        if(isFemale == 1) {
            this.isFemale = true;
            Log.d("PC", "Is Female");
        }
        else
            this.isFemale = false;
        loadData();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParticipantId() {
        return participant_id;
    }

    public long getCoupleId() {
        return participant_id/100;
    }

    public List<SurveyResult> getSurveyResults() {
        if(isFemale)
            return surveyResults;
        return null;
    }

    public List<ViralLoad> getViralLoads() {
        if(isIndex())
            return viralLoads;
        return null;
    }

    public List<PeakFertility> getPeakFertilities() {
        if (isFemale)
            return peakFertilities;
        return null;
    }

    public List<MemsCap> getMemscaps() {
        if (!isIndex())
            return memscaps;
        return null;
    }

    public boolean isIndex() {
        long tmp = (participant_id/10) % 2;
        if(tmp == 0)
            return true;
        return false;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public Participant getPartner() {
        DatabaseHelper db = new DatabaseHelper(context);
        List<Participant> couple = db.getCoupleFromID(getCoupleId());
        db.closeDB();
        for(Participant p : couple) {
            if(p.getParticipantId() != participant_id)
                return p;
        }
        return null;
    }

    public void reCalculateFertilityData() {
        if(isFemale) {
            Log.d("New Participant", String.valueOf(participant_id));
            List<SurveyResult> srs = surveyResults;
            Collections.sort(srs);
            List<Date> menses = new ArrayList<>();
            List<Date> ovulation = new ArrayList<>();
            Date lastMenses = DateUtil.addDays(srs.get(0).getDate(), -30);
            Date lastOvulation = DateUtil.addDays(srs.get(0).getDate(), -30);
            for(SurveyResult sr : srs) {
                if(sr.isOnPeriod()) {
                    Log.d("Period Date", sr.getDate().toString());
                }
                else if (sr.isOvulating()) {
                    Log.d("Ovulation Date", sr.getDate().toString());
                }
                if(sr.isOnPeriod() && DateUtil.getDateDiff(lastMenses, sr.getDate(), TimeUnit.DAYS) > 10) {
                    menses.add(sr.getDate());
                    lastMenses = sr.getDate();
                }
                else if (sr.isOvulating() && DateUtil.getDateDiff(lastOvulation, sr.getDate(), TimeUnit.DAYS) > 10) {
                    ovulation.add(DateUtil.addDays(sr.getDate(), 2));
                    lastOvulation = sr.getDate();
                }
            }
            if(menses.size() >= 3 && ovulation.size() >= 2) {
                Log.d("Mensus dates", menses.toString());
                Log.d("Ovulation dates", ovulation.toString());

                double averageCycleLength = 0;
                for(int i = 1; i < menses.size(); i++) {
                    averageCycleLength += DateUtil.getDateDiff(menses.get(i-1), menses.get(i), TimeUnit.DAYS);
                }
                averageCycleLength = averageCycleLength / (menses.size()-1);
                Log.i("Average Cycle Length", String.valueOf(averageCycleLength));

                double averageLuteralPhaseLength = 0;
                int count = 0;
                if(menses.get(0).before(ovulation.get(0))) {
                    for(int i = 1; i < menses.size(); i++) {
                        Log.i("Date difference", String.valueOf(DateUtil.getDateDiff(ovulation.get(i-1), menses.get(i),TimeUnit.DAYS)));
                        if(i <= ovulation.size() && DateUtil.getDateDiff(ovulation.get(i-1), menses.get(i),TimeUnit.DAYS) < 30) {
                            averageLuteralPhaseLength += DateUtil.getDateDiff(ovulation.get(i-1), menses.get(i), TimeUnit.DAYS);
                            count++;
                        }
                    }
                }
                else {
                    for(int i = 0; i < menses.size(); i++) {
                        Log.i("Date difference", String.valueOf(DateUtil.getDateDiff(ovulation.get(i), menses.get(i),TimeUnit.DAYS)));
                        if(i <= ovulation.size() && DateUtil.getDateDiff(ovulation.get(i), menses.get(i),TimeUnit.DAYS) < 30) {
                            averageLuteralPhaseLength += DateUtil.getDateDiff(ovulation.get(i), menses.get(i), TimeUnit.DAYS);
                            count++;
                        }
                    }
                }
                averageLuteralPhaseLength = averageLuteralPhaseLength / count;
                Log.i("Average Luteral Phase Length", String.valueOf(averageLuteralPhaseLength));

                Date nextMenses = DateUtil.addDays(menses.get(menses.size()-1), (int)averageCycleLength+1);
                Date nextOvulation = DateUtil.addDays(nextMenses, -1*(int)averageLuteralPhaseLength);

                Log.i("Next Menses", nextMenses.toString());
                Log.i("Next Ovulation", nextOvulation.toString());
            }
            else if (menses.size() >= 3) {
                Log.d("Mensus dates", menses.toString());

                double averageCycleLength = 0;
                for (int i = 1; i < menses.size(); i++) {
                    averageCycleLength += DateUtil.getDateDiff(menses.get(i - 1), menses.get(i), TimeUnit.DAYS);
                }
                averageCycleLength = averageCycleLength / (menses.size() - 1);
                Log.i("Average Cycle Length", String.valueOf(averageCycleLength));

                double averageLuteralPhaseLength = 14;
                Log.i("Average Luteral Phase Length", String.valueOf(averageLuteralPhaseLength));

                Date nextMenses = DateUtil.addDays(menses.get(menses.size()-1), (int)averageCycleLength+1);
                Date nextOvulation = DateUtil.addDays(nextMenses, -1*(int)averageLuteralPhaseLength);

                Log.i("Next Menses", nextMenses.toString());
                Log.i("Next Ovulation", nextOvulation.toString());
            }
        }
    }


    private void loadData() {
        DatabaseHelper db = new DatabaseHelper(context);
        if(isFemale) {
            this.peakFertilities = db.getAllPeakFertilityById(participant_id);
            this.surveyResults = db.getAllSurveyResultsById(participant_id);
        }
        if(isIndex()) {
            this.viralLoads = db.getAllViralLoadsById(participant_id);
        }
        else {
            this.memscaps = db.getAllMemsCapById(participant_id);
        }
        db.closeDB();
    }



}
