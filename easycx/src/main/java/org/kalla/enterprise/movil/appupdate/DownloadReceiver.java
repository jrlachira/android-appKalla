package org.kalla.enterprise.movil.appupdate;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class DownloadReceiver  extends BroadcastReceiver {

    private long enqueue;
    private DownloadManager dm;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        long downloadId = intent.getLongExtra(
                DownloadManager.EXTRA_DOWNLOAD_ID, 0);



//          if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
//              String downloadPath = intent.getStringExtra(DownloadManager.COLUMN_URI);
//              Query query = new Query();
//              query.setFilterById(downloadId);
//              dm = (DownloadManager) context.getSystemService(Service.DOWNLOAD_SERVICE);
//              Cursor c = dm.query(query);
//              if (c.moveToFirst()) {
//            	  String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//            	  uriString=c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
//            	  Intent intent2 = new Intent(Intent.ACTION_VIEW);
//            	  intent2 .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    			 intent.setDataAndType(Uri.fromFile(new File(uriString)), "application/vnd.android.package-archive");
//    				context.startActivity(intent2);
//              }
//          }

//

//              int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
//              if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
//
//              	String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//
////              	if (extras != null) {
////  					Messenger messenger = (Messenger) extras.get("MESSENGER");
////  					Message msg = Message.obtain();
////  					msg.arg1 = result;
////  					Bundle bundle = new Bundle();
////  					bundle.putString("absolutePath", uriString);
////  					msg.setData(bundle);
////  					try {
////  						messenger.send(msg);
////  					} catch (android.os.RemoteException e1) {
////  						Log.w(getClass().getName(), "Exception sending message", e1);
////  					}
////
////  				}
//
//                  Toast.makeText(context, "Termino de DEscarga"+uriString, Toast.LENGTH_LONG).show();
//              }
        // }

    }


}
