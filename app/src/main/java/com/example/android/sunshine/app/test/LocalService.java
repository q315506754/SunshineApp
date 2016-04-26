package com.example.android.sunshine.app.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.android.sunshine.app.R;
import com.example.android.sunshine.app.TestPageActityFragment;

/**
 * This is an example of implementing an application service that runs locally
 * in the same process as the application. The {@link LocalServiceController}
 * and {@link LocalServiceBinding} classes show how to interact with the
 * service.
 * <p/>
 * <p>Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service. This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */
public class LocalService extends Service {
    private static final String TAG = LocalService.class.getSimpleName();
    private NotificationManager mNM;

    /**
     * Class for clients to access. Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        public LocalService getService() {
            return LocalService.this;
        }


    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

// Display a notification about us starting. We put an icon in the status bar.
        showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
// We want this service to continue running until it is explicitly
// stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
// Cancel the persistent notification.
        mNM.cancel(R.string.local_service_started);

// Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mBinder;
    }

    // This is the object that receives interactions from clients. See
// RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
// In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.local_service_started);

// Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.ic_snow, text,
                System.currentTimeMillis());

// The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TestPageActityFragment.class), 0);

// Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.local_service_label),
                text, contentIntent);

// Send the notification.
// We use a layout id because it is a unique number. We use it later to cancel.
        mNM.notify(R.string.local_service_started, notification);
    }
}