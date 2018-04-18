package de.brettin.leon.travelfriend.schedule;

import android.app.job.JobParameters;
import android.app.job.JobService;

import de.brettin.leon.travelfriend.resources.TfDatabase;

/**
 * Jobervice to update the position of the user
 */
public class TfUpdatePositionIntentService extends JobService{

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return this.updatePosition();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    /**
     * Updates the position in the database
     * @return True to show that the job is done.
     */
    private boolean updatePosition() {
        final TfDatabase database = new TfDatabase(this);
        database.updateOwnPosition(this);
        return true;
    }
}
