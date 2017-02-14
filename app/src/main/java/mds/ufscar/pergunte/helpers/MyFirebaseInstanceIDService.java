package mds.ufscar.pergunte.helpers;

/**
 * Created by thyonamine on 13/02/17.
 */
/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.firebase.iid.FirebaseInstanceIdService;
import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;



public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    //cZEJr62M0eo:APA91bGFTSIfR_Q9OsBOC-eNO26R4FacCxC2nZnUvGLSyLFfgP-JOrF-N7r_Py2aDO_kTNoJXR1SLiQ5BGlZs-ouryqU4SvIKlpFZDAiIn0H6BiA1r15MqWY4OZu7QDrYB3zYVlqfeU7

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "*******************************************:");
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        //TODO MARCELOOOOO help akiii, cada user tem um Token de registro FCM, para mandar notificaçao precisa usar esse.

    }

    //para mandar uma notificação usar
    /* http post request
    https://https://fcm.googleapis.com/fcm/send
    Content-Type:application/json
    Authorization:key=AAAA8nobT9Y:APA91bG-7smDqfZlFdYJDGdY_-1XEX3RNHKnkjSsnQVg_wz2bXMyHvjL4g5ojemjaTJGdUD352BG6fWPSNZ

     {
     "to" : "cZEJr62M0eo:APA91bGFTSIfR_Q9OsBOC-eNO26R4FacCxC2nZnUvGLSyLFfgP-JOrF-N7r_Py2aDO_kTNoJXR1SLiQ5BGlZs-ouryqU4SvIKlpFZDAiIn0H6BiA1r15MqWY4OZu7QDrYB3zYVlqfeU7",
     "notification" : {
     "title" : "Mensagem para o Firebase",
     "body" : "Teste firebase"
     }
     }
     */
}