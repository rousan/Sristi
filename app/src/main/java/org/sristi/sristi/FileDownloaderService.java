package org.sristi.sristi;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcelable;

import org.sristi.sristi.utils.EventUpdatesChecker;
import org.sristi.sristi.utils.FileDownloader;
import org.sristi.sristi.utils.FilesData;

import java.io.File;
import java.util.UUID;

public class FileDownloaderService extends Service {

    public FileDownloaderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int  onStartCommand(final Intent i, int flags, final int startId) {

        try {



        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    public long startDownload(Uri targetUri) {
        long download_id = 0;
        try {

            File downloadsDir = new File(Environment.getExternalStorageDirectory(), "Downloads");
            if (!downloadsDir.isDirectory())
                downloadsDir.mkdirs();
            File tempFile = new File(downloadsDir, UUID.randomUUID().toString() + ".tmp");
            tempFile.createNewFile();

            DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(targetUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setTitle(FileDownloaderService.class.getName());
            request.setDestinationUri(Uri.fromFile(tempFile));
            download_id = downloadManager.enqueue(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return download_id;
    }



}
