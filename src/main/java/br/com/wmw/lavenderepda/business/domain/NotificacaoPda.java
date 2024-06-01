package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import totalcross.util.Date;

public class NotificacaoPda extends BaseDomain {

    public static String TABLE_NAME = "TBLVPNOTIFICACAOPDA";

	public static final String CD_NOTIFICACAOPDA_VISITA_EM_ANDAMENTO = "VISITA_EM_ANDAMENTO";
	public static final String CD_NOTIFICACAOPDA_ALTERACAO_RECEBIMENTO_EMAIL = "ALTERA_RECEBE_EMAIL";
	public static final String CD_NOTIFICACAOPDA_ALTERACAO_RECEBIMENTO_SMS = "ALTERA_RECEBE_SMS";

	public String cdRepresentante;
    public String cdNotificacao;
    public String cdChave;
    public Date dtNotificacao;
    public String hrNotificacao;
    public String dsNotificacao;
    public int nuSequencia;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof NotificacaoPda) {
            NotificacaoPda notificacaoPda = (NotificacaoPda) obj;
            return
                ValueUtil.valueEquals(cdNotificacao, notificacaoPda.cdNotificacao) &&
                ValueUtil.valueEquals(cdChave, notificacaoPda.cdChave) &&
                ValueUtil.valueEquals(cdRepresentante, notificacaoPda.cdRepresentante);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdNotificacao);
        primaryKey.append(";");
        primaryKey.append(cdChave);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        return primaryKey.toString();
    }

    public String getChave() {
    	StringBuilder valorChave = new StringBuilder();
    	valorChave.append("[").append(SessionLavenderePda.usuarioPdaRep.cdRepresentante).append("]");
    	return valorChave.toString();
    }

    public String getChaveFlagsCLiente(String cdEmpresa, String cdCliente, String value) {
    	StringBuilder valorChave = new StringBuilder();
    	valorChave.append("[").append(cdEmpresa).append(";").append(cdCliente).append(";").append(value).append("]");
    	return valorChave.toString();
    }

}