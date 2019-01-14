package com.practice.workmanager

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import androidx.work.*
import com.google.firebase.FirebaseApp
import java.util.concurrent.TimeUnit

/**
 * Created by fahad.waqar on 14-Jan-19.
 * Contact No : 0309-4101147
 */
class MainActivity : AppCompatActivity() {

    lateinit var constraint: Constraints

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this@MainActivity)

        // these are the constraints for running the background task
        // You can add multiple constraints
        constraint = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

        doOneTimeWorkRequest()

        // Periodic Work Request Runs Multiple Time According to the Constrainsts Added
        doPeriodicWorkRequest()
    }

    private fun doPeriodicWorkRequest() {
        val recurringWork: PeriodicWorkRequest = PeriodicWorkRequest.Builder(FirebaseWorker::class.java, 2,
                TimeUnit.MINUTES).setConstraints(constraint).build()
        WorkManager.getInstance().enqueue(recurringWork)

        WorkManager.getInstance().getWorkInfoByIdLiveData(recurringWork.id)
                .observe(this@MainActivity, Observer {
                    it?.let {
                        if (it.state.isFinished) {
                            Toast.makeText(this@MainActivity, "State finished of Periodic Work Request", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
    }

    private fun doOneTimeWorkRequest() {
        val oneTimeWork: OneTimeWorkRequest = OneTimeWorkRequest.Builder(FirebaseWorker::class.java)
                .setConstraints(constraint).build()
        WorkManager.getInstance().enqueue(oneTimeWork)

        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWork.id)
                .observe(this@MainActivity, Observer {
                    it?.let {
                        if (it.state.isFinished) {
                            Toast.makeText(this@MainActivity, "State finished of OneTime Work Request", Toast.LENGTH_SHORT).show()
                        }
                    }
                })

//         only oneTime work can be added in then()
//         you can add multiple oneTime Work Request in then()
        WorkManager.getInstance().beginWith(oneTimeWork)
                .then(oneTimeWork)
                .enqueue()
    }
}