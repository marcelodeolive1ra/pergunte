package mds.ufscar.pergunte;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import mds.ufscar.pergunte.model.Materia;

/**
 * Created by marcelodeoliveiradasilva on 13/01/17.
 */

public class RequisicaoAssincrona extends AsyncTask<String, Void, String> {

    private Object objetoGenerico;

    public void setObject(Object o) {
        this.objetoGenerico = o;
    }

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

            case "cadastrarmateria":
                Materia materia = (Materia)objetoGenerico;

                // Não é necessário passar o código, pois este será gerado automaticamente no banco
                data.put("email", params[1]);
                data.put("turma", materia.getTurma());
                data.put("ano", Integer.toString(materia.getAno()));
                data.put("semestre", Integer.toString(materia.getSemestre()));
                data.put("nome_disciplina", materia.getNomeDisciplina());
                data.put("codigo_inscricao", materia.getCodigoInscricao());

                request = HttpRequest.post("http://mds.secompufscar.com.br/cadastrarmateria/").form(data);
                json = request.body();

                break;

            case "cancelarinscricaoemmateria":
                data.put("email", params[1]);
                data.put("codigo", params[2]);

                System.out.println(data);

                request = HttpRequest.post("http://mds.secompufscar.com.br/cancelarinscricaoemmateria/").form(data);
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

