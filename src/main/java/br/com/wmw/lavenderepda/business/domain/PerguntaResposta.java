package br.com.wmw.lavenderepda.business.domain;

import java.util.HashMap;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PerguntaResposta extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPPERGUNTARESPOSTA";

	public static final String CD_RESPOSTALIVRE_PADRAO = "1";
	public static final String CD_RESPOSTAMULT_PADRAO = "0";

	public String cdEmpresa;
	public String cdPergunta;
	public String cdResposta;
	public String dsResposta;
	public Date dtCadastro;
	public Integer nuSequencia;
	public String cdPerguntasSecundarias;
	public String dsExpressao;
	public Vector listPerguntasSecundarias;
	public HashMap<String,Pergunta> hashPerguntasSecundarias;
	public String flObrigaFoto;
	public PesquisaRespAppFoto pesquisaRespAppFoto;
	
	public PerguntaResposta(String dsTabela) {
		super(dsTabela);
	}

	public PerguntaResposta() {
		super(TABLE_NAME);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PerguntaResposta) {
			PerguntaResposta perguntaResposta = (PerguntaResposta) obj;
			return ValueUtil.valueEquals(cdEmpresa, perguntaResposta.cdEmpresa)
					&& ValueUtil.valueEquals(cdPergunta, perguntaResposta.cdPergunta) && ValueUtil.valueEquals(cdResposta, perguntaResposta.cdResposta);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return dsResposta;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdPergunta);
		primaryKey.append(";");
		primaryKey.append(cdResposta);
		return primaryKey.toString();
	}
	
	public boolean isObrigaFoto() {
    	return ValueUtil.VALOR_SIM.equals(flObrigaFoto);
    }
}
