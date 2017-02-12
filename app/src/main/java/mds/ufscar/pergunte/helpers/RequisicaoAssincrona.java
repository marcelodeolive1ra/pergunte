package mds.ufscar.pergunte.helpers;

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

import mds.ufscar.pergunte.models.Materia;
import mds.ufscar.pergunte.models.Pergunta;

/**
 * Created by marcelodeoliveiradasilva on 13/01/17.
 */

public class RequisicaoAssincrona extends AsyncTask<String, Void, JSONObject> {

    public class Parametros {
        public static final String EMAIL_USUARIO = "email";
        public static final String TIPO_USUARIO = "tipo";
        public static final String PERFIL_ALUNO = "aluno";
        public static final String PERFIL_PROFESSOR = "professor";
        public static final String STATUS_MATERIA = "status_materia";
        public static final String STATUS_MATERIA_ATIVA = "ativa";
        public static final String STATUS_MATERIA_INATIVA = "inativa";
        public static final String CODIGO_MATERIA = "codigo";
        public static final String NOME_MATERIA = "nome_materia";
        public static final String TURMA_MATERIA = "turma";
        public static final String CODIGO_INSCRICAO_MATERIA = "codigo_inscricao";
        public static final String ANO_MATERIA = "ano";
        public static final String SEMESTRE_MATERIA = "semestre";
        public static final String CODIGO_PERGUNTA = "codigo";

        static final String URL_SERVIDOR = "http://mds.secompufscar.com.br/";
    }

    public static final String BUSCAR_ALUNO = "1";
    public static final String BUSCAR_MATERIAS = "2";
    public static final String BUSCAR_MATERIA_POR_QR_CODE = "3";
    public static final String CADASTRAR_NOVA_MATERIA = "4";
    public static final String INSCREVER_ALUNO_EM_MATERIA = "5";
    public static final String CANCELAR_INSCRICAO_EM_MATERIA = "6";
    public static final String BUSCAR_PERFIL_DO_USUARIO = "7";
    public static final String DESATIVAR_MATERIA = "8";
    public static final String CADASTRAR_NOVA_PERGUNTA = "9";
    public static final String BUSCAR_PERGUNTAS_POR_MATERIA = "10";
    public static final String BUSCAR_PERGUNTAS_POR_PROFESSOR = "11";
    public static final String BUSCAR_ALTERNATIVAS_POR_PERGUNTA = "12";
    public static final String BUSCAR_QUANTIDADE_DE_RESPOSTAS_POR_ALTERNATIVA_POR_PERGUNTA = "13";
    public static final String BUSCAR_QUANTIDADE_DE_RESPOSTAS_TOTAIS_POR_PERGUNTA = "14";
    public static final String ENVIAR_QR_CODE_POR_EMAIL = "15";
    public static final String BUSCAR_PERGUNTAS_ATIVAS_POR_MATERIA = "16";
    public static final String BUSCAR_PROXIMAS_PERGUNTAS_POR_MATERIA = "17";
    public static final String BUSCAR_PERGUNTAS_RESPONDIDAS_POR_MATERIA = "18";
    public static final String DISPONIBILIZAR_PERGUNTA = "19";
    public static final String FINALIZAR_PERGUNTA = "20";
    public static final String REGISTRAR_RESPOSTA = "21";
    public static final String BUSCAR_RESPOSTAS_POR_PERGUNTA = "22";
    public static final String BUSCAR_ESTATISTICAS = "23";

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
                        parametros.put("cadastrar_alternativa_dialog" + Integer.toString(i) + "_letra", pergunta.getAlternativas().get(i).getLetra());
                        parametros.put("cadastrar_alternativa_dialog" + Integer.toString(i) + "_texto_alternativa", pergunta.getAlternativas().get(i).getTextoAlternativa());
                        parametros.put("cadastrar_alternativa_dialog" + Integer.toString(i) + "_correta", (pergunta.getAlternativas().get(i).isCorreta() ? "true" : "false"));
                    }

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

                case DISPONIBILIZAR_PERGUNTA:
                    parametros.put(Parametros.CODIGO_PERGUNTA, params[1]);

                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "disponibilizarpergunta/").form(parametros);
                    break;

                case FINALIZAR_PERGUNTA:
                    parametros.put(Parametros.CODIGO_PERGUNTA, params[1]);

                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "finalizarpergunta/").form(parametros);
                    break;

                case REGISTRAR_RESPOSTA:
                    String[] alternativasEscolhidas = (String[])objetoGenerico;

                    for (int i = 0; i < alternativasEscolhidas.length; i++) {
                        System.out.println(alternativasEscolhidas[i]);
                    }

                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    parametros.put(Parametros.CODIGO_PERGUNTA, params[2]);
                    parametros.put("quantidade_alternativas", Integer.toString(alternativasEscolhidas.length));

                    for (int i = 0; i < alternativasEscolhidas.length; i++) {
                        parametros.put("cadastrar_alternativa_dialog" + Integer.toString(i) + "_codigo", alternativasEscolhidas[i]);
                    }

                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "registrarresposta/").form(parametros);
                    break;

                case BUSCAR_RESPOSTAS_POR_PERGUNTA:
                    parametros.put("codigo", params[1]);
                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "buscarrespostasporpergunta/").form(parametros);
                    break;

                case BUSCAR_ESTATISTICAS:
                    parametros.put(Parametros.EMAIL_USUARIO, params[1]);
                    request = HttpRequest.post(Parametros.URL_SERVIDOR + "buscarestatisticas/").form(parametros);
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

