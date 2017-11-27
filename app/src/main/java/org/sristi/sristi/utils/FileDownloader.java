package org.sristi.sristi.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.UUID;

/**
 * Created by Ariyan Khan on 23-03-2016.
 */
public class FileDownloader {

    Context context;
    Uri targetUri;
    Uri destinationUri;

    public FileDownloader(Context context, Uri targetUri, Uri destinationUri) {
        this.context = context;
        this.targetUri = targetUri;
        this.destinationUri = destinationUri;
    }

    public long startDownload() {
        long download_id = 0;
        try {

            File downloadsDir = new File(Environment.getExternalStorageDirectory(), "Downloads");
            if (!downloadsDir.isDirectory())
                downloadsDir.mkdirs();
            File tempFile = new File(downloadsDir, UUID.randomUUID().toString() + ".tmp");
            tempFile.createNewFile();

            DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(targetUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(true);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setTitle(FileDownloader.class.getName());
            request.setDestinationUri(Uri.fromFile(tempFile));
            download_id = downloadManager.enqueue(request);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return download_id;
    }

}





















