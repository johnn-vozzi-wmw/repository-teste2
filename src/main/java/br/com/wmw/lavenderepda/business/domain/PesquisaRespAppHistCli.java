package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class PesquisaRespAppHistCli extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPESQUISARESPAPPHISTCLI";

    public String cdEmpresa;
    public String cdCliente;
    public String cdQuestionario;
    public String cdPergunta;
    public String cdResposta;
    public String dsResposta;
    public String dsObservacao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PesquisaRespAppHistCli) {
            PesquisaRespAppHistCli pesquisaRespAppHistCli = (PesquisaRespAppHistCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pesquisaRespAppHistCli.cdEmpresa) && 
                ValueUtil.valueEquals(cdCliente, pesquisaRespAppHistCli.cdCliente) && 
                ValueUtil.valueEquals(cdQuestionario, pesquisaRespAppHistCli.cdQuestionario) && 
                ValueUtil.valueEquals(cdPergunta, pesquisaRespAppHistCli.cdPergunta) && 
                ValueUtil.valueEquals(cdResposta, pesquisaRespAppHistCli.cdResposta);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdQuestionario);
        primaryKey.append(";");
        primaryKey.append(cdPergunta);
        return primaryKey.toString();
    }

}