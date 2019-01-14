package com.practice.workmanager

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * Created by fahad.waqar on 14-Jan-19.
 * Contact No : 0309-4101147
 */

class FirebaseWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    //Please add your google-services.json file to the App directory
    internal var firebaseRef = FirebaseDatabase.getInstance().getReference("testing")

    override fun doWork(): ListenableWorker.Result {
        firebaseRef.child(Calendar.getInstance().time.toString()).setValue("Testing Value")
        return Result.success()
    }
}
