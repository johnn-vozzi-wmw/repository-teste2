package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Pergunta extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPERGUNTA";
	
	public static final String TIPO_PERGUNTA_MULTIPLA = "1";
	public static final String TIPO_PERGUNTA_TEXTO = "2";
	public static final String TIPO_PERGUNTA_NUMERICO = "3";
	public static final String TIPO_PERGUNTA_DATE = "4";
	public static final String TIPO_PERGUNTA_MULTI_CHECK = "5";
	public static final String FLRESPOSTAAUTOMATICA_N = "N";
	public static final String FLRESPOSTAAUTOMATICA_PELO_HISTORICO = "1";
	public static final String FLRESPOSTAAUTOMATICA_PELA_ENTIDADE_E_CAMPO = "2";

	public String cdEmpresa;
	public String cdPergunta;
	public String dsPergunta;
	public Date dtCadastro;
	public String flExibeObservacao;
	public String cdTipoPergunta;
	public String flRespostaAutomatica;
	public String nmEntidade;
	public String nmCampo;
	public String flSomenteLeitura;

	//não persistente
	public boolean isObrigatoria;
	public boolean flQuestionarioPerguntaExibeObservacao;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pergunta) {
			Pergunta pergunta = (Pergunta) obj;
			return ValueUtil.valueEquals(cdEmpresa, pergunta.cdEmpresa) && ValueUtil.valueEquals(cdPergunta, pergunta.cdPergunta);
		}
		return false;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdPergunta);
		return primaryKey.toString();
	}
	
	public boolean exibeObservacao() {
		return ValueUtil.valueEquals(flExibeObservacao, "S");
	}
}
