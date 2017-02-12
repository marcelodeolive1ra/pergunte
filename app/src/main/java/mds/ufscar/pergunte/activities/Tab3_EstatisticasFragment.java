package mds.ufscar.pergunte.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import mds.ufscar.pergunte.helpers.CircleTransform;
import mds.ufscar.pergunte.R;
import mds.ufscar.pergunte.helpers.RequisicaoAssincrona;

import static android.content.Context.LOCATION_SERVICE;

public class Tab3_EstatisticasFragment extends Fragment {
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
        View rootView = inflater.inflate(R.layout.tab3_estatisticas_fragment, container, false);

        user_name = (TextView) rootView.findViewById(R.id.user_profile_name);
        user_email = (TextView) rootView.findViewById(R.id.user_profile_email);
        user_foto = (ImageView) rootView.findViewById(R.id.user_profile_photo);
        header_foto = (ImageView) rootView.findViewById(R.id.header_cover_image);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        int quantidade_materias_inscritas = 0;
        int quantidade_perguntas_respondidas = 0;
        int quantidade_perguntas_respondidas_corretamente = 0;

        RequisicaoAssincrona requisicao = new RequisicaoAssincrona();

        try {
            JSONObject resultado_requisicao = requisicao.execute(RequisicaoAssincrona.BUSCAR_ESTATISTICAS,
                    MainScreenActivity.getEmailDoUsuarioAtual()).get();

            if (resultado_requisicao != null) {
                if (resultado_requisicao.getString("status").equals("ok")) {
                    quantidade_materias_inscritas = resultado_requisicao.getInt("quantidade_materias_inscritas");
                    quantidade_perguntas_respondidas = resultado_requisicao.getInt("quantidade_perguntas_respondidas");
                    quantidade_perguntas_respondidas_corretamente = resultado_requisicao.getInt("quantidade_perguntas_respondidas_corretamente");
                } else {
                    Log.w("REQUISICAO", resultado_requisicao.toString());
                    Toast.makeText(Tab3_EstatisticasFragment.this.getActivity(),
                            resultado_requisicao.getString("descricao"), Toast.LENGTH_LONG).show();
                }
            } else {
                AlertDialog.Builder adb = new AlertDialog.Builder(Tab3_EstatisticasFragment.this.getActivity());
                adb.setTitle("Erro");
                adb.setMessage("Não foi possível conectar à Internet.\n\nVerifique sua conexão e tente novamente.");
                adb.setPositiveButton("Tentar novamente", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                });
                adb.show();
            }

        } catch (InterruptedException | ExecutionException | JSONException e) {
            e.printStackTrace();
        }

        // Thiago, dados das estatísticas aqui:
        System.out.println(quantidade_materias_inscritas);
        System.out.println(quantidade_perguntas_respondidas);
        System.out.println(quantidade_perguntas_respondidas_corretamente);


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
//                    .load("http://files.vividscreen.info/soft/cee54d62fbd8200f5da70bad775a7c22/Google-Abstract-wide-l.jpg")
                    .load("http://mds.secompufscar.com.br/static/admin/img/fundo_perfil.png")
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
