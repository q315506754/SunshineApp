package com.example.android.sunshine.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.sunshine.app.test.LocalService;
import com.example.android.sunshine.app.test.MyService;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestPageActityFragment extends Fragment {
    private final String LOG_TAG = TestPageActityFragment.class.getSimpleName();


    public TestPageActityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final FragmentActivity activity = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_test_page_actity, container, false);

        bindActionOnBtn(rootView, R.id.testActionType, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);// 设置Intent Action属性
                intent.setType("vnd.android.cursor.item/phone");// 设置Intent Type 属性
                //主要是获取通讯录的内容
                startActivity(intent); // 启动Activity
            }
        });

        bindActionOnBtn(rootView, R.id.testCategoryType, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);// 添加Action属性
                intent.addCategory(Intent.CATEGORY_HOME);// 添加Category属性
//                intent.put
                //主要是获取通讯录的内容
                startActivity(intent); // 启动Activity
            }
        });


        bindActionOnBtn(rootView, R.id.testStartService, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("com.sunshine.CUSTOM_SERVICE");
////                intent.setAction("com.sunshine.CUSTOM_SERVICE");// 添加Action属性
//                activity.startService(intent); // 启动Activity
                Intent intent = new Intent(activity, MyService.class);
//                intent.setAction("com.sunshine.CUSTOM_SERVICE");// 添加Action属性
                activity.startService(intent); // 启动Activity
            }
        });
        bindActionOnBtn(rootView, R.id.testStopService, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("com.sunshine.CUSTOM_SERVICE");
////                intent.setAction("com.sunshine.CUSTOM_SERVICE");// 添加Action属性
//                activity.stopService(intent); // 启动Activity
                Intent intent = new Intent(activity, MyService.class);
//                intent.setAction("com.sunshine.CUSTOM_SERVICE");// 添加Action属性
                activity.stopService(intent); // 启动Activity
            }
        });

        bindActionOnBtn(rootView, R.id.testStartInterService, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.bindService(new Intent(activity,
                        LocalService.class), mConnection, Context.BIND_AUTO_CREATE);
                mIsBound = true;
            }
        });
        bindActionOnBtn(rootView, R.id.testStopInterService, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBound) {
                    activity.unbindService(mConnection);
                    mIsBound = false;
                }
            }
        });

        final Vibrator  vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        bindActionOnBtn(rootView, R.id.testVibrate, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
                long [] pattern = {100,800,100,800};   // 停止 开启 停止 开启
//                long [] pattern = {100,800,100,800};   // 停止 开启 停止 开启
                vibrator.vibrate(pattern,2);           //重复两次上面的pattern 如果只想震动一次，index设为-1
//                vibrator.
//                vibrator.cancel();
            }
        });
        bindActionOnBtn(rootView, R.id.testCancelVibrate, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.cancel();
            }
        });

        smsFunc(activity, rootView);


        final String phoneNumber="10086";
        bindActionOnBtn(rootView, R.id.testDial, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                1）直接拨打
//                Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
//                startActivity(intentPhone);

//                跳转到拨号界面
//                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneNumber));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);

//                2、跳转到联系人页面，使用一下代码：
                Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(intentPhone);
            }
        });


        bindActionOnBtn(rootView, R.id.testChooseContact, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPhone = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                startActivity(intentPhone);
                startActivityForResult(intentPhone,1);
            }
        });


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
//                if (resultCode == RESULT_OK) {
//                    Uri contactData = data.getData();
//                    Cursor cursor = managedQuery(contactData, null, null, null,
//                            null);
//                    cursor.moveToFirst();
//                    String num = this.getContactPhone(cursor);
//                    show.setText("所选手机号为：" + num);
//                }
                Log.d(LOG_TAG, "resultCode:"+resultCode);

                Uri contactData = data.getData();
                Toast.makeText(getActivity(), contactData.toString(), Toast.LENGTH_LONG).show();

                Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null,
                        null);
                cursor.moveToFirst();
                displayCursor(cursor);

                int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
                String contactId = cursor.getString(idColumn);

                Log.d(LOG_TAG, "query mobile:"+ContactsContract.CommonDataKinds.Phone.CONTENT_URI.toString());

                // 获得联系人电话的cursor
                Cursor phone = getActivity().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                                + contactId, null, null);
                phone.moveToFirst();
                displayCursor(phone);

                int index = phone
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int typeindex = phone
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                int phone_type = phone.getInt(typeindex);
                String phoneNumber = phone.getString(index);

                EditText viewById = (EditText) getActivity().findViewById(R.id.testChooseContactTxt);
                viewById.setText(phoneNumber);
                break;

            default:
                break;
        }
    }

    private void displayCursor(Cursor cursor) {
        String[] columnNames = cursor.getColumnNames();

        int i =0;
        for (String col : columnNames) {
            int columnIndex = cursor.getColumnIndex(col);
//                    Log.d(LOG_TAG, "col:"+col);
            i++;
            try {
                String colVal = cursor.getString(columnIndex);
                Log.d(LOG_TAG, i+":"+col+"==>"+colVal);
            } catch (Exception e) {
//                e.printStackTrace();
                Log.e(LOG_TAG, i+":"+" col:"+col+" is not string !");
            }

        }
    }

    private void smsFunc(final FragmentActivity activity, View rootView) {
        bindActionOnBtn(rootView, R.id.testSendSMS, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String SENT = "sms_sent";
                final String DELIVERED = "sms_delivered";
                final String msgContent = "query 10086";

                activity.registerReceiver(new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Log.i("====>", SENT+"onReceive");
                        switch(getResultCode())
                        {
                            case Activity.RESULT_OK:
                                Log.i("====>", "Activity.RESULT_OK");
                                break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                Log.i("====>", "RESULT_ERROR_GENERIC_FAILURE");
                                break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE:
                                Log.i("====>", "RESULT_ERROR_NO_SERVICE");
                                break;
                            case SmsManager.RESULT_ERROR_NULL_PDU:
                                Log.i("====>", "RESULT_ERROR_NULL_PDU");
                                break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF:
                                Log.i("====>", "RESULT_ERROR_RADIO_OFF");
                                break;
                        }
                    }
                }, new IntentFilter(SENT));

                activity.registerReceiver(new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context context, Intent intent){
                        Log.i("====>", DELIVERED+"onReceive");
                        switch(getResultCode())
                        {
                            case Activity.RESULT_OK:
                                Log.i("====>", "RESULT_OK");
                                break;
                            case Activity.RESULT_CANCELED:
                                Log.i("=====>", "RESULT_CANCELED");
                                break;
                        }
                    }
                }, new IntentFilter(DELIVERED));

//                PendingIntent pi = PendingIntent.getActivity(getActivity(), 0, new Intent(getActivity(), this.getClass()), 0);

                PendingIntent sentPI = PendingIntent.getActivity(getActivity(), 0, new Intent(SENT), 0);
                PendingIntent deliveredPI = PendingIntent.getActivity(getActivity(), 0, new Intent(DELIVERED), 0);

                SmsManager sms = SmsManager.getDefault();


                //直接发送
//                sms.sendTextMessage("10086", null, msgContent, sentPI, deliveredPI);
//                sms.sendDataMessage("10086", null, port, message.getBytes(), sentPI, deliveredPI);

                //用系统的短信界面，这个方法需要用户自己输入接收方的电话号码
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body", msgContent);
                sendIntent.setType("vnd.android-dir/mms-sms");
                activity.startActivity(sendIntent);


//                这个方法自动设置接收方的号码
//                Uri uri = Uri.parse("smsto:" + 10086);
//                Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
//                sendIntent.putExtra("sms_body", msgContent);
//                startActivity(sendIntent);

            }
        });
    }

    private void bindActionOnBtn(View rootView, int actionType, View.OnClickListener listener) {
        Button viewById = (Button) rootView.findViewById(actionType);
        viewById.setOnClickListener(listener);
    }


    private LocalService mBoundService;
    private boolean mIsBound;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
// This is called when the connection with the service has been
// established, giving us the service object we can use to
// interact with the service. Because we have bound to a explicit
// service that we know is running in our own process, we can
// cast its IBinder to a concrete class and directly access it.
            mBoundService = ((LocalService.LocalBinder) service).getService();

// Tell the user about this for our demo.
            Toast.makeText( getActivity(), getActivity().getString(R.string.local_service_connected),
                    Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
// This is called when the connection with the service has been
// unexpectedly disconnected -- that is, its process crashed.
// Because it is running in our same process, we should never
// see this happen.
            mBoundService = null;
            Toast.makeText(getActivity(), R.string.local_service_disconnected,
                    Toast.LENGTH_SHORT).show();
        }
    };
}
