package mds.ufscar.pergunte;

import android.os.AsyncTask;
import java.util.HashMap;
import java.util.Map;

import mds.ufscar.pergunte.model.Materia;
import mds.ufscar.pergunte.model.Professor;

/**
 * Created by marcelodeoliveiradasilva on 13/01/17.
 */

public class RequisicaoAssincrona extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... params) {
        int response = -1;

        try {
            Map<String, String> data = new HashMap<String, String>();
            data.put("email", "marcelodeoliveira@outlook.com");
            data.put("tipo", "aluno");

            System.out.println(data);

            HttpRequest request = HttpRequest.post("http://mds.secompufscar.com.br/buscarmaterias/").form(data);
//            HttpRequest request = HttpRequest.post("http://mds.secompufscar.com.br/buscaraluno/").
//                    send("email=marcelodeoliveira@outlook.com").code();

            response = request.code();

            System.out.println("REQUEST CODE = " + response);
            System.out.println(request.contentType());
            System.out.println(request.body());
            System.out.println(request.getParams("status"));
            return null;

        } catch (Exception exception) {
            System.err.println("ERROR CODE = " + response);
            return null;
        }
    }

    protected void onPostExecute(String param) {
        //onPostExecute
    }
}
