package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.sync.HttpConnection;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;

public class ConexaoPda extends LavendereBaseDomain implements HttpConnection {

    public static String TABLE_NAME = "TBLVPCONEXAOPDA";

	public String cdConexao;
    public String dsConexao;
    public String dsUrlWebService;
    public String flConexaoDiscada;
    public String flDefault;
    public String flConexaoSecundaria;

    public boolean isConexaoDiscada() {
    	return ValueUtil.VALOR_SIM.equals(flConexaoDiscada);
    }

    public boolean isDefault() {
    	return ValueUtil.VALOR_SIM.equals(flDefault);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConexaoPda) {
            ConexaoPda conexaoPda = (ConexaoPda) obj;
            return
                ValueUtil.valueEquals(cdConexao, conexaoPda.cdConexao);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	return cdConexao;
    }

	public String getCdDomain() {
		return cdConexao;
	}

	public String getDsDomain() {
		return dsConexao;
	}
	
	@Override
	public String getSortStringValue() {
		return flDefault;
	}

	@Override
	public String getBaseUrl() {
		return dsUrlWebService;
	}

	@Override
	public String getId() {
		return toString();
	}

}
