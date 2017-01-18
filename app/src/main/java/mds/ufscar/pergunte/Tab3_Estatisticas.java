package mds.ufscar.pergunte;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import static android.content.Context.LOCATION_SERVICE;

public class Tab3_Estatisticas extends Fragment {
    TextView user_name;
    TextView user_email;
    ImageView user_foto;
    ImageView header_foto;

    /* GPS */
    private TextView Coordinates;
    private LocationManager locationManager;
    private LocationListener listener;
    /* GPS */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3_estatisticas, container, false);

        user_name = (TextView) rootView.findViewById(R.id.user_profile_name);
        user_email = (TextView) rootView.findViewById(R.id.user_profile_email);
        user_foto = (ImageView) rootView.findViewById(R.id.user_profile_photo);
        header_foto = (ImageView) rootView.findViewById(R.id.header_cover_image);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            user_name.setText(name);
            String email = user.getEmail();
            user_email.setText(email);
            Uri photoUrl = user.getPhotoUrl();
            Picasso.with(this.getContext())
                    .load(photoUrl)
                    .transform(new CircleTransform())
                    .into(user_foto);

            Picasso.with(this.getContext())
                    .load("http://files.vividscreen.info/soft/cee54d62fbd8200f5da70bad775a7c22/Google-Abstract-wide-l.jpg")
                    .into(header_foto);

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
           // String uid = user.getUid();

            Coordinates = (TextView) rootView.findViewById(R.id.text_Coordinates);
            Coordinates.setText("Localização: atualizando");
            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Coordinates.setText("Localização: " + location.getLatitude() + " " + location.getLongitude());
                    //historico
                    //Coordinates.append("\n " + location.getLatitude() + " " + location.getLongitude());
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            };

            //noinspection MissingPermission
            locationManager.requestLocationUpdates("gps", 5000, 1, listener);

            //atualiza de 5s caso anda 1m


        }

        return rootView;
    }

/* acho q sei como resolver o problema de permissão do danilo.
 @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                    configure_button();
                break;
            default:
                break;
        }
    }

*/

}
