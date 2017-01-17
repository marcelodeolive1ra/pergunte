package mds.ufscar.pergunte;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Tab3_Estatisticas extends Fragment {
    TextView user_name;
    TextView user_email;
    ImageView user_foto;
    ImageView header_foto;
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
        }

        return rootView;
    }


}
