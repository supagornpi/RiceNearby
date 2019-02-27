package com.warunya.ricenearby.onesignal;

import android.os.AsyncTask;
import android.util.Log;

import com.onesignal.OneSignal;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class OnesignalManager {

    public static void sendNotification(final String title, final String message) {
        sendNotification(title, message, null);
    }

    public static void sendNotification(final String title, final String message, final Date date) {
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.e("player id", ": " + userId);
                sendWithPlayerId(userId, title, message, date);
            }
        });
    }

    private static void sendWithPlayerId(final String playerId, final String title, final String message, final Date date) {
        new AsyncTask<String, Void, Object>() {
            @Override
            protected Object doInBackground(String... strings) {
                try {
                    String jsonResponse;

                    URL url = new URL("https://onesignal.com/api/v1/notifications");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setUseCaches(false);
                    con.setDoOutput(true);
                    con.setDoInput(true);

                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    con.setRequestMethod("POST");
                    String strJsonBody;

                    if (date != null) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.MINUTE, 2);
                        strJsonBody = "{"
                                + "\"app_id\": \"0d940ebc-1264-457e-9571-29e420bf535e\","
                                + "\"include_player_ids\": [\"" + playerId + "\"],"
                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"send_after\": \"" + calendar.getTime() + "\","
                                + "\"headings\": {\"en\": \"" + title + "\"},"
                                + "\"contents\": {\"en\": \"" + message + "\"}"
                                + "}";

                    } else {
                        strJsonBody = "{"
                                + "\"app_id\": \"0d940ebc-1264-457e-9571-29e420bf535e\","
                                + "\"include_player_ids\": [\"" + playerId + "\"],"
                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"headings\": {\"en\": \"" + title + "\"},"
                                + "\"contents\": {\"en\": \"" + message + "\"}"
                                + "}";
                    }

                    System.out.println("strJsonBody:\n" + strJsonBody);

                    byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                    con.setFixedLengthStreamingMode(sendBytes.length);

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(sendBytes);

                    int httpResponse = con.getResponseCode();
                    System.out.println("httpResponse: " + httpResponse);

                    if (httpResponse >= HttpURLConnection.HTTP_OK
                            && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                        Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                        scanner.close();
                    } else {
                        Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                        scanner.close();
                    }
                    System.out.println("jsonResponse:\n" + jsonResponse);

                } catch (Throwable t) {
                    t.printStackTrace();
                }
                return null;
            }

        }.execute();
    }


}
