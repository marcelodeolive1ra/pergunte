package mds.ufscar.pergunte;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mds.ufscar.pergunte.model.Materia;

/**
 * Created by marcelodeoliveiradasilva on 13/01/17.
 */

public class RequisicaoAssincrona extends AsyncTask<String, Void, JSONObject> {

    private Object objetoGenerico;

    public void setObject(Object o) {
        this.objetoGenerico = o;
    }

    protected JSONObject doInBackground(String... params) {
        Map<String, String> data = new HashMap<>();
        JSONObject retorno_requisicao = null;
        HttpRequest request = null;

        switch (params[0]) {
            case "buscaraluno":
                data.put("email", params[1]);
                data.put("tipo", "aluno");
                request = HttpRequest.post("http://mds.secompufscar.com.br/buscaraluno/").form(data);
                break;

            case "buscarmaterias":
                data.put("email", params[1]);
                data.put("tipo", params[2]);
                data.put("status_materia", params[3]);
                request = HttpRequest.post("http://mds.secompufscar.com.br/buscarmaterias/").form(data);
                break;

            case "buscarmateriaporqr":
                data.put("codigo", params[1]);
                request = HttpRequest.post("http://mds.secompufscar.com.br/buscarmateriaporqr/").form(data);
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
                break;

            case "inscreveralunoemmateria":
                data.put("email", params[1]);
                data.put("codigo", params[2]);
                request = HttpRequest.post("http://mds.secompufscar.com.br/inscreveralunoemmateria/").form(data);
                break;

            case "cancelarinscricaoemmateria":
                data.put("email", params[1]);
                data.put("codigo", params[2]);
                request = HttpRequest.post("http://mds.secompufscar.com.br/cancelarinscricaoemmateria/").form(data);
                break;

            case "buscarperfildousuario":
                data.put("email", params[1]);
                request = HttpRequest.post("http://mds.secompufscar.com.br/buscarperfilusuario/").form(data);
                break;

            default:
                break;
        }

        try {
            retorno_requisicao = (request != null) ? new JSONObject(request.body()) : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retorno_requisicao;
    }

    protected void onPostExecute(String param) {
        //onPostExecute
    }

}

