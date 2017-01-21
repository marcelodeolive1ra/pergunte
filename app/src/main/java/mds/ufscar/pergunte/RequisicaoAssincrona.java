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
    static final String DESATIVAR_MATERIA = "8";

    private Object objetoGenerico;

    public void setObject(Object o) {
        this.objetoGenerico = o;
    }

    protected JSONObject doInBackground(String... params) {
        Map<String, String> parametros = new HashMap<>();
        JSONObject retorno_requisicao = null;
        HttpRequest request = null;

        try {
            switch (params[0]) {
                case BUSCAR_ALUNO:
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.TIPO_USUARIO, Parametros.PERFIL_ALUNO);
                    request = HttpRequest.post("http://mds.secompufscar.com.br/buscaraluno/").form(parametros);
                    break;

                case BUSCAR_MATERIAS:
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.TIPO_USUARIO, params[2]);
                    parametros.put(Parametros.STATUS_MATERIA, params[3]);
                    request = HttpRequest.post("http://mds.secompufscar.com.br/buscarmaterias/").form(parametros);
                    break;

                case BUSCAR_MATERIA_POR_QR_CODE:
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.CODIGO_MATERIA, params[2]);
                    request = HttpRequest.post("http://mds.secompufscar.com.br/buscarmateriaporqr/").form(parametros);
                    break;

                case CADASTRAR_NOVA_MATERIA:
                    Materia materia = (Materia) objetoGenerico;
                    // Não é necessário passar o código, pois este será gerado automaticamente no banco
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.TURMA_MATERIA, materia.getTurma());
                    parametros.put(Parametros.ANO_MATERIA, Integer.toString(materia.getAno()));
                    parametros.put(Parametros.SEMESTRE_MATERIA, Integer.toString(materia.getSemestre()));
                    parametros.put(Parametros.NOME_MATERIA, materia.getNomeDisciplina());
                    parametros.put(Parametros.CODIGO_INSCRICAO_MATERIA, materia.getCodigoInscricao());
                    request = HttpRequest.post("http://mds.secompufscar.com.br/cadastrarmateria/").form(parametros);
                    break;

                case INSCREVER_ALUNO_EM_MATERIA:
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.CODIGO_MATERIA, params[2]);
                    request = HttpRequest.post("http://mds.secompufscar.com.br/inscreveralunoemmateria/").form(parametros);
                    break;

                case CANCELAR_INSCRICAO_EM_MATERIA:
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.CODIGO_MATERIA, params[2]);
                    request = HttpRequest.post("http://mds.secompufscar.com.br/cancelarinscricaoemmateria/").form(parametros);
                    break;

                case BUSCAR_PERFIL_DO_USUARIO:
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    request = HttpRequest.post("http://mds.secompufscar.com.br/buscarperfilusuario/").form(parametros);
                    break;

                case DESATIVAR_MATERIA:
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.CODIGO_MATERIA, params[2]);
                    request = HttpRequest.post("http://mds.secompufscar.com.br/desativarmateria/").form(parametros);
                    break;

                default:
                    break;
            }

            try {
                retorno_requisicao = (request != null) ? new JSONObject(request.body()) : null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (HttpRequest.HttpRequestException e) {
            return null;
        }

        return retorno_requisicao;
    }

    protected void onPostExecute(String param) {
        //onPostExecute
    }

}

