package mds.ufscar.pergunte;

import android.os.AsyncTask;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mds.ufscar.pergunte.model.Materia;

/**
 * Created by marcelodeoliveiradasilva on 13/01/17.
 */

public class RequisicaoAssincrona extends AsyncTask<String, Void, JSONObject> {

    public class Parametros {
        static final String EMAIL_USUARIO = "email";
        static final String TIPO_USUARIO = "tipo";
        static final String PERFIL_ALUNO = "aluno";
        static final String PERFIL_PROFESSOR = "professor";
        static final String STATUS_MATERIA = "status_materia";
        static final String STATUS_MATERIA_ATIVA = "ativa";
        static final String STATUS_MATERIA_INATIVA = "inativa";
        static final String CODIGO_MATERIA = "codigo";
        static final String NOME_MATERIA = "nome_materia";
        static final String TURMA_MATERIA = "turma";
        static final String CODIGO_INSCRICAO_MATERIA = "codigo_inscricao";
        static final String ANO_MATERIA = "ano";
        static final String SEMESTRE_MATERIA = "semestre";
    }

    static final String BUSCAR_ALUNO = "1";
    static final String BUSCAR_MATERIAS = "2";
    static final String BUSCAR_MATERIA_POR_QR_CODE = "3";
    static final String CADASTRAR_NOVA_MATERIA = "4";
    static final String INSCREVER_ALUNO_EM_MATERIA = "5";
    static final String CANCELAR_INSCRICAO_EM_MATERIA = "6";
    static final String BUSCAR_PERFIL_DO_USUARIO = "7";

    private Object objetoGenerico;

    public void setObject(Object o) {
        this.objetoGenerico = o;
    }

    protected JSONObject doInBackground(String... params) {
        Map<String, String> data = new HashMap<>();
        JSONObject retorno_requisicao = null;
        HttpRequest request = null;

        switch (params[0]) {
            case BUSCAR_ALUNO:
                data.put(Parametros.EMAIL_USUARIO, params[1]);
                data.put(Parametros.TIPO_USUARIO, Parametros.PERFIL_ALUNO);
                request = HttpRequest.post("http://mds.secompufscar.com.br/buscaraluno/").form(data);
                break;

            case BUSCAR_MATERIAS:
                data.put(Parametros.EMAIL_USUARIO, params[1]);
                data.put(Parametros.TIPO_USUARIO, params[2]);
                data.put(Parametros.STATUS_MATERIA, params[3]);
                request = HttpRequest.post("http://mds.secompufscar.com.br/buscarmaterias/").form(data);
                break;

            case BUSCAR_MATERIA_POR_QR_CODE:
                data.put(Parametros.EMAIL_USUARIO, params[1]);
                data.put(Parametros.CODIGO_MATERIA, params[2]);
                request = HttpRequest.post("http://mds.secompufscar.com.br/buscarmateriaporqr/").form(data);
                break;

            case CADASTRAR_NOVA_MATERIA:
                Materia materia = (Materia)objetoGenerico;
                // Não é necessário passar o código, pois este será gerado automaticamente no banco
                data.put(Parametros.EMAIL_USUARIO, params[1]);
                data.put(Parametros.TURMA_MATERIA, materia.getTurma());
                data.put(Parametros.ANO_MATERIA, Integer.toString(materia.getAno()));
                data.put(Parametros.SEMESTRE_MATERIA, Integer.toString(materia.getSemestre()));
                data.put(Parametros.NOME_MATERIA, materia.getNomeDisciplina());
                data.put(Parametros.CODIGO_INSCRICAO_MATERIA, materia.getCodigoInscricao());
                request = HttpRequest.post("http://mds.secompufscar.com.br/cadastrarmateria/").form(data);
                break;

            case INSCREVER_ALUNO_EM_MATERIA:
                data.put(Parametros.EMAIL_USUARIO, params[1]);
                data.put(Parametros.CODIGO_MATERIA, params[2]);
                request = HttpRequest.post("http://mds.secompufscar.com.br/inscreveralunoemmateria/").form(data);
                break;

            case CANCELAR_INSCRICAO_EM_MATERIA:
                data.put(Parametros.EMAIL_USUARIO, params[1]);
                data.put(Parametros.CODIGO_MATERIA, params[2]);
                request = HttpRequest.post("http://mds.secompufscar.com.br/cancelarinscricaoemmateria/").form(data);
                break;

            case BUSCAR_PERFIL_DO_USUARIO:
                data.put(Parametros.EMAIL_USUARIO, params[1]);
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

