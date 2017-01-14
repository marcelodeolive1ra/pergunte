package mds.ufscar.pergunte;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marcelodeoliveiradasilva on 13/01/17.
 */

public class RequisicaoAssincrona extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... params) {
        Map<String, String> data = new HashMap<>();
        String json = "";
        HttpRequest request;

        switch (params[0]) {

            case "buscaraluno":

                data.put("email", params[1]);
                data.put("tipo", "aluno");

                request = HttpRequest.post("http://mds.secompufscar.com.br/buscaraluno/").form(data);
                json = request.body();

                break;

            case "buscarmaterias":
                data.put("email", params[1]);
                data.put("tipo", params[2]);

                request = HttpRequest.post("http://mds.secompufscar.com.br/buscarmaterias/").form(data);
                json = request.body();

                break;

            case "buscarmateriaporqr":
                data.put("codigo", params[1]);

                request = HttpRequest.post("http://mds.secompufscar.com.br/buscarmateriaporqr/").form(data);
                json = request.body();

                break;

            default:
                break;
        }

        return json;
    }

    protected void onPostExecute(String param) {
        //onPostExecute
    }
}