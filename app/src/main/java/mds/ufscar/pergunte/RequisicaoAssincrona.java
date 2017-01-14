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
        Map<String, String> data = new HashMap<>();
        int response = -1;

        switch (params[0]) {

            case "buscaraluno":
                response = -1;

                try {
                    data.put("email", params[1]);
                    data.put("tipo", "aluno");

                    System.out.println(data);

                    HttpRequest request = HttpRequest.post("http://mds.secompufscar.com.br/buscaraluno/").form(data);
                    response = request.code();

                    System.out.println("REQUEST CODE = " + response);
                    System.out.println(request.contentType());
                    System.out.println(request.body());
                    System.out.println(request.getParams("status"));

                } catch (Exception exception) {
                    System.err.println("ERROR CODE = " + response);
                    return null;
                }

                break;

            case "buscarmateriaporqr":
                response = -1;

                try {
                    data.put("codigo", params[1]);

                    System.out.println(data);

                    HttpRequest request = HttpRequest.post("http://mds.secompufscar.com.br/buscarmateriaporqr/").form(data);
                    response = request.code();

                    System.out.println("REQUEST CODE = " + response);
                    System.out.println(request.contentType());
                    System.out.println(request.body());
                    System.out.println(request.getParams("status"));

                } catch (Exception exception) {
                    System.err.println("ERROR CODE = " + response);
                    return null;
                }

                break;
            default:
                break;
        }

        return "";
    }

    protected void onPostExecute(String param) {
        //onPostExecute
    }
}
