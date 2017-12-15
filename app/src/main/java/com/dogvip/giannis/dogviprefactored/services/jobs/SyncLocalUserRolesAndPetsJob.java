package com.dogvip.giannis.dogviprefactored.services.jobs;

import android.util.Log;

import com.dogvip.giannis.dogviprefactored.config.AppConfig;
import com.dogvip.giannis.dogviprefactored.pojo.BaseRequest;
import com.dogvip.giannis.dogviprefactored.pojo.sync.SyncUserRolesAndPetsResponse;
import com.dogvip.giannis.dogviprefactored.retrofit.ServiceAPI;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.DogVipRoomDatabase;
import com.dogvip.giannis.dogviprefactored.roompersistencedata.entities.UserRole;
import com.dogvip.giannis.dogviprefactored.utilities.errorhandling.RetryWithDelay;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by giannis on 7/12/2017.
 */

public class SyncLocalUserRolesAndPetsJob extends JobService {
    private static final String debugTag = SyncLocalUserRolesAndPetsJob.class.getSimpleName();
    @Inject
    ServiceAPI mServiceAPI;
    @Inject
    BaseRequest mBaseRequest;
    @Inject
    DogVipRoomDatabase dogVipRoomDatabase;
    @Inject
    RetryWithDelay retryWithDelay;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.e(debugTag, "SyncLocalUserRolesAndPetsJob onStartJob");
        mBaseRequest.setAction("sync_user_roles_pets");
        mBaseRequest.setAuthtoken(job.getExtras().getString("auth_token"));
        mServiceAPI
                .syncUserRolesAndPets(mBaseRequest)
                .subscribeOn(Schedulers.io())
                .retryWhen(configureRetryWithDelayParams(3, 2000))
                .subscribe(response -> {
                    if (response.getCode() == AppConfig.STATUS_OK) {
                        syncLocalUserRolesAndPets(response.getUserRolesPets())
                                .subscribeOn(Schedulers.io())
                                .subscribe(() -> {
                                            Log.e(debugTag, "SyncLocalUserRolesAndPetsJob successfully finished");
                                            jobFinished(job, false);
//                                            dogVipRoomDatabase.stateEntityDao().get().subscribe(stateEntities -> {
//                                                for (UserData stateEntity: stateEntities) {
//                                                    Log.e(debugTag, "ID: "+stateEntity.getId() + " UPDATE: "+stateEntity.getUpdated_at());
//                                                }
//                                            });
//                                            dogVipRoomDatabase.userRoleDao().getUserRoles().subscribe(userRoles -> {
//                                                for (UserRole userRole: userRoles) {
//                                                    Log.e(debugTag, "ID: "+userRole.getId() + " NAME: "+userRole.getName() +  " SURNAME: "+userRole.getSurname());
//                                                }
//                                            });
//                                            dogVipRoomDatabase.petDao().getAllPets().subscribe(pets -> {
//                                                for (Pet pet: pets) {
//                                                    Log.e(debugTag, "ID: "+pet.getP_id() + " NAME: "+pet.getP_name() +  " race: "+pet.getRace());
//                                                }
//                                            });
                                        },
                                        onError -> {
                                            Log.e(debugTag, "job finished with error, reschedule it");
                                            jobFinished(job, true);
                                        });
                    } else {
                        jobFinished(job, true);
                    }
                }, onError -> jobFinished(job, true));


        jobFinished(job, false);
        return true;
    }
    /* Called when the scheduling engine has decided to interrupt the execution of a running job, most
     * likely because the runtime constraints associated with the job are no longer satisfied. The job
     * must stop execution
     * */
    @Override
    public boolean onStopJob(JobParameters job) {
        Log.e(debugTag, "onEndJob");
        return true;
    }

    private Completable syncLocalUserRolesAndPets(SyncUserRolesAndPetsResponse userRolesAndPets) {
        return Completable.fromAction(() -> {
//            Log.e(debugTag, dogVipRoomDatabase + " ");
                dogVipRoomDatabase.stateEntityDao().updateUserStateEntity(System.currentTimeMillis()/1000L, new int[]{1,2,3});
                dogVipRoomDatabase.userRoleDao().deleteAllUserRoles();
                if (!userRolesAndPets.getUserRoles().isEmpty()) {
                    List<Long> rows = dogVipRoomDatabase
                                                .userRoleDao()
                                                .insertUserRoles(userRolesAndPets.getUserRoles());
                    if (!rows.isEmpty()) {
                        dogVipRoomDatabase
                                    .petDao()
                                    .insertPet(userRolesAndPets.getOwnerPets());
                }
            }
        });
    }

    private RetryWithDelay configureRetryWithDelayParams(int maxRetries, int retryDelayMillis) {
        retryWithDelay.setMaxRetries(maxRetries);
        retryWithDelay.setRetryDelayMillis(retryDelayMillis);
        return retryWithDelay;
    }
}
