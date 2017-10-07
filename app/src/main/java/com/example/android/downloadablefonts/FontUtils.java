package com.example.android.downloadablefonts;

import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by rahuljanagouda on 16/09/17.
 */

public class FontUtils {
    private static Handler mHandler = null;
    static List<String> safeList = Collections.synchronizedList(new ArrayList<String>());


    public static void requestDownloadRandom(final TextView a) {
        if (safeList.size() < 1)
            safeList.addAll(Arrays.asList(a.getContext().getResources().getStringArray(R.array.family_names)));
        String familyName = safeList.get(new Random().nextInt(safeList.size()));


        QueryBuilder queryBuilder = new QueryBuilder(familyName)
                .withWidth(1)
                .withWeight(1)
                .withItalic(0)
                .withBestEffort(true);
        String query = queryBuilder.build();

        FontRequest request = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                query,
                R.array.com_google_android_gms_fonts_certs);

        FontsContractCompat.FontRequestCallback callback = new FontsContractCompat
                .FontRequestCallback() {
            @Override
            public void onTypefaceRetrieved(Typeface typeface) {
                if (typeface != null && a != null)
                    a.setTypeface(typeface);
            }

            @Override
            public void onTypefaceRequestFailed(int reason) {
            }
        };
        FontsContractCompat
                .requestFont(a.getContext(), request, callback,
                        getHandlerThreadHandler());
    }

    private static Handler getHandlerThreadHandler() {

        if (mHandler == null) {
            HandlerThread handlerThread = new HandlerThread("fonts");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
        }
        return mHandler;
    }

}
