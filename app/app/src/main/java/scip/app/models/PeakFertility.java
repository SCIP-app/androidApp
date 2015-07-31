package scip.app.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Allie on 6/29/2015.
 */
public class PeakFertility {
    List<SurveyResult> surveyResults;
    boolean dataProcessed;
    boolean resultsValid;
    List<Date> menses;
    List<Date> ovulation;
    Date lastMenses;
    Date lastOvulation;
    double averageCycleLength;
    double averageLuteralPhaseLength;
    Date nextMensesStart;
    Date nextOvulationStart;

    public PeakFertility(List<SurveyResult> surveyResults) {
        this.surveyResults = surveyResults;
        dataProcessed = false;
        this.menses = new ArrayList<>();
        this.ovulation = new ArrayList<>();
        averageCycleLength = 0;
        averageLuteralPhaseLength = 0;
        resultsValid = true;
    }

    public long getDayInCycle(Date today) {
        if(!dataProcessed)
            processData();
        if(resultsValid) {
            for(int i = menses.size()-1; i >= 0; i--) {
                long dateDiff = DateUtil.getDateDiff(menses.get(i), today, TimeUnit.DAYS);
                // if the date of cycle beginning is the closest one to today and is before, return the cycle day
                // otherwise, keep looking
                if(dateDiff >=  0 && dateDiff < 40)
                    return dateDiff + 1;
            }
            // it's in the future or the past where we don't have data. Only return if it's in the future
            long dateDifference = (long) (DateUtil.getDateDiff(nextMensesStart, today, TimeUnit.DAYS) % averageCycleLength + 1);
            if (dateDifference > 0)
                return dateDifference;
        }
        // return -1 is any errors occur
        return -1;
    }

    public List<Date> getPeakFertilityWindow() {
        if(!dataProcessed)
            processData();
        if(resultsValid) {
            List<Date> window = new ArrayList<>();
            // return a list of the 4 days before the next ovulation
            for(int i = -4; i < 0; i++)
                window.add(DateUtil.addDays(nextOvulationStart, i));
            return window;
        }
        return new ArrayList<>();
    }

    public double getAverageCycleLength() {
        if(!dataProcessed)
            processData();
        if(resultsValid)
            return averageCycleLength;
        return -1; 
    }

    private void processData() {
        if(dataProcessed)
            return;

        // do some stuff to calculate peak fertility information
        Collections.sort(surveyResults);
        menses.clear();
        ovulation.clear();
        lastMenses = DateUtil.addDays(surveyResults.get(0).getDate(), -30);
        lastOvulation = DateUtil.addDays(surveyResults.get(0).getDate(), -30);
        for(SurveyResult sr : surveyResults) {
            if(sr.isOnPeriod() && DateUtil.getDateDiff(lastMenses, sr.getDate(), TimeUnit.DAYS) > 10) {
                menses.add(sr.getDate());
                lastMenses = sr.getDate();
            }
            else if (sr.isOvulating() && DateUtil.getDateDiff(lastOvulation, sr.getDate(), TimeUnit.DAYS) > 10) {
                ovulation.add(DateUtil.addDays(sr.getDate(), 2));
                lastOvulation = sr.getDate();
            }
        }
        calculateAverageCycleLength();
        calculateAverageLuteralPhaseLength();

        nextMensesStart = DateUtil.addDays(menses.get(menses.size()-1), (int)averageCycleLength+1);
        nextOvulationStart = DateUtil.addDays(nextMensesStart, -1*(int)averageLuteralPhaseLength);

        Log.i("Next Menses", nextMensesStart.toString());
        Log.i("Next Ovulation", nextOvulationStart.toString());

        dataProcessed = true;
    }

    // TODO: add check to make sure cycle length is consistent
    private void calculateAverageCycleLength() {
        for (int i = 1; i < menses.size(); i++) {
            averageCycleLength += DateUtil.getDateDiff(menses.get(i - 1), menses.get(i), TimeUnit.DAYS);
        }
        averageCycleLength = averageCycleLength / (menses.size() - 1);
        Log.i("Average Cycle Length", String.valueOf(averageCycleLength));
    }

    // TODO: add check to revert to temperature data if something is weird
    private void calculateAverageLuteralPhaseLength() {
        if(menses.size() >= 3 && ovulation.size() >= 2) {
            averageLuteralPhaseLength = 0;
            int count = 0;
            if (menses.get(0).before(ovulation.get(0))) {
                for (int i = 1; i < menses.size(); i++) {
                    Log.i("Date difference", String.valueOf(DateUtil.getDateDiff(ovulation.get(i - 1), menses.get(i), TimeUnit.DAYS)));
                    if (i <= ovulation.size() && DateUtil.getDateDiff(ovulation.get(i - 1), menses.get(i), TimeUnit.DAYS) < 30) {
                        averageLuteralPhaseLength += DateUtil.getDateDiff(ovulation.get(i - 1), menses.get(i), TimeUnit.DAYS);
                        count++;
                    }
                }
            } else {
                for (int i = 0; i < menses.size(); i++) {
                    Log.i("Date difference", String.valueOf(DateUtil.getDateDiff(ovulation.get(i), menses.get(i), TimeUnit.DAYS)));
                    if (i <= ovulation.size() && DateUtil.getDateDiff(ovulation.get(i), menses.get(i), TimeUnit.DAYS) < 30) {
                        averageLuteralPhaseLength += DateUtil.getDateDiff(ovulation.get(i), menses.get(i), TimeUnit.DAYS);
                        count++;
                    }
                }
            }
            averageLuteralPhaseLength = averageLuteralPhaseLength / count;
            Log.i("Average Luteral Phase Length", String.valueOf(averageLuteralPhaseLength));
        }
        else if (menses.size() >= 3) {
            averageLuteralPhaseLength = 14;
        }
        else {
            resultsValid = false;
        }

    }
}
