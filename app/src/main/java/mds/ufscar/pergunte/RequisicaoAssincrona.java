package mds.ufscar.pergunte;

import android.os.AsyncTask;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import mds.ufscar.pergunte.model.Materia;
import mds.ufscar.pergunte.model.Pergunta;

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
        static final String CODIGO_PERGUNTA = "codigo";

        static final String URL_SERVIDOR = "http://mds.secompufscar.com.br/";
    }

    static final String BUSCAR_ALUNO = "1";
    static final String BUSCAR_MATERIAS = "2";
    static final String BUSCAR_MATERIA_POR_QR_CODE = "3";
    static final String CADASTRAR_NOVA_MATERIA = "4";
    static final String INSCREVER_ALUNO_EM_MATERIA = "5";
    static final String CANCELAR_INSCRICAO_EM_MATERIA = "6";
    static final String BUSCAR_PERFIL_DO_USUARIO = "7";
    static final String DESATIVAR_MATERIA = "8";
    static final String CADASTRAR_NOVA_PERGUNTA = "9";
    static final String BUSCAR_PERGUNTAS_POR_MATERIA = "10";
    static final String BUSCAR_PERGUNTAS_POR_PROFESSOR = "11";
    static final String BUSCAR_ALTERNATIVAS_POR_PERGUNTA = "12";
    static final String BUSCAR_QUANTIDADE_DE_RESPOSTAS_POR_ALTERNATIVA_POR_PERGUNTA = "13";
    static final String BUSCAR_QUANTIDADE_DE_RESPOSTAS_TOTAIS_POR_PERGUNTA = "14";
    static final String ENVIAR_QR_CODE_POR_EMAIL = "15";
    static final String BUSCAR_PERGUNTAS_ATIVAS_POR_MATERIA = "16";
    static final String BUSCAR_PROXIMAS_PERGUNTAS_POR_MATERIA = "17";
    static final String BUSCAR_PERGUNTAS_RESPONDIDAS_POR_MATERIA = "18";

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
                    Materia materia = (Materia)this.objetoGenerico;
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

                case CADASTRAR_NOVA_PERGUNTA:
                    Pergunta pergunta = (Pergunta)this.objetoGenerico;
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.CODIGO_MATERIA, params[2]);
                    parametros.put("titulo", pergunta.getTitulo());
                    parametros.put("texto_pergunta", pergunta.getTextoPergunta());

                    int quantidadeAlternativas = pergunta.getAlternativas().size();
                    parametros.put("quantidade_alternativas", Integer.toString(quantidadeAlternativas));
                    parametros.put("data_aproximada", pergunta.getDataAproximadaString());

                    for (int i = 0; i < quantidadeAlternativas; i++) {
                        parametros.put("alternativa" + Integer.toString(i) + "_letra", pergunta.getAlternativas().get(i).getLetra());
                        parametros.put("alternativa" + Integer.toString(i) + "_texto_alternativa", pergunta.getAlternativas().get(i).getTextoAlternativa());
                        parametros.put("alternativa" + Integer.toString(i) + "_correta", (pergunta.getAlternativas().get(i).isCorreta() ? "true" : "false"));
                    }

                    System.out.println(parametros);

                    request = HttpRequest.post("http://mds.secompufscar.com.br/cadastrarpergunta/").form(parametros);
                    break;

                case BUSCAR_PERGUNTAS_POR_PROFESSOR:
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "buscarperguntasporprofessor/").form(parametros);
                    break;

                case BUSCAR_ALTERNATIVAS_POR_PERGUNTA:
                    parametros.put(Parametros.CODIGO_PERGUNTA, params[1]);
                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "buscaralternativasporpergunta/").form(parametros);
                    break;

                case BUSCAR_QUANTIDADE_DE_RESPOSTAS_POR_ALTERNATIVA_POR_PERGUNTA:
                    parametros.put(Parametros.CODIGO_PERGUNTA, params[1]);
                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "buscarquantidadederespostasporalternativaporpergunta/").form(parametros);
                    break;

                case BUSCAR_QUANTIDADE_DE_RESPOSTAS_TOTAIS_POR_PERGUNTA:
                    parametros.put(Parametros.CODIGO_PERGUNTA, params[1]);
                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "buscarquantidadetotalderespostasporpergunta/").form(parametros);
                    break;

                case ENVIAR_QR_CODE_POR_EMAIL:
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.CODIGO_MATERIA, params[2]);
                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "enviarqrcodeporemail/").form(parametros);
                    break;

                case BUSCAR_PERGUNTAS_ATIVAS_POR_MATERIA:
                    Materia m = (Materia)objetoGenerico;
                    parametros.put(Parametros.CODIGO_MATERIA, Integer.toString(m.getCodigo()));
                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "buscarperguntasativaspormateria/").form(parametros);
                    break;

                case BUSCAR_PROXIMAS_PERGUNTAS_POR_MATERIA:
                    Materia m2 = (Materia)objetoGenerico;
                    parametros.put(Parametros.CODIGO_MATERIA, Integer.toString(m2.getCodigo()));

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.CANADA);
                    Date date = new Date();
                    String hoje = dateFormat.format(date);

                    parametros.put("data", hoje);
                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "buscarproximasperguntaspormateria/").form(parametros);
                    break;

                case BUSCAR_PERGUNTAS_RESPONDIDAS_POR_MATERIA:
                    Materia m3 = (Materia)objetoGenerico;
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.CODIGO_MATERIA, Integer.toString(m3.getCodigo()));

                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "buscarperguntasrespondidaspormateria/").form(parametros);
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

