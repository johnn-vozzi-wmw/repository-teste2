package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Notificacao extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPNOTIFICACAO";
	public static final String NMCOLUNA_FLLIDO = "FLLIDO";
	public static final String NMCOLUNA_CDINSTANTE = "CDINSTANTE";
	public static final String NMCOLUNA_DSTIPONOTIFICACAO = "DSTIPONOTIFICACAO";
	
	public String cdNotificacao;
	public String cdInstante;
	public String dsTipoNotificacao;
	public String vlChave;
	public String dsMensagem;
	public String cdUsuarioDestino;
	public String flLido;
    public Date dtAlteracao;
    public String hrAlteracao;
	public Date dtLimpeza;


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Notificacao) {
			Notificacao notificacao = (Notificacao) obj;
			return ValueUtil.valueEquals(cdNotificacao, notificacao.cdNotificacao) && ValueUtil.valueEquals(cdInstante, notificacao.cdInstante) && ValueUtil.valueEquals(cdUsuarioDestino, notificacao.cdUsuarioDestino);
		}
		return false;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdNotificacao);
        primaryKey.append(";");
		primaryKey.append(cdInstante);
		primaryKey.append(";");
        primaryKey.append(cdUsuarioDestino);
		return primaryKey.toString();
	}

}