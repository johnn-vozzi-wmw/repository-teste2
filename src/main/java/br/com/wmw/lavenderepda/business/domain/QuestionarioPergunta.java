package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class QuestionarioPergunta extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPQUESTIONARIOPERGUNTA";

    public String cdEmpresa;
    public String cdQuestionario;
    public String cdPergunta;
    public Date dtCadastro;
    public int nuSequencia;
    public String flObrigatorio;
    public String flExibeObservacao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof QuestionarioPergunta) {
            QuestionarioPergunta questionarioPergunta = (QuestionarioPergunta) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, questionarioPergunta.cdEmpresa) && 
                ValueUtil.valueEquals(cdQuestionario, questionarioPergunta.cdQuestionario) && 
                ValueUtil.valueEquals(cdPergunta, questionarioPergunta.cdPergunta);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdQuestionario);
        primaryKey.append(";");
        primaryKey.append(cdPergunta);
        return primaryKey.toString();
    }
    
	public boolean exibeObservacao() {
		return ValueUtil.valueEquals(flExibeObservacao, "S");
	}
}