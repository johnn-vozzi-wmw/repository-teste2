package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.service.CestaService;

public class Cesta extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPCESTA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCesta;
    public String dsCesta;
    //
    public String cdClienteFilter;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Cesta) {
            Cesta cesta = (Cesta) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, cesta.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, cesta.cdRepresentante) &&
                ValueUtil.valueEquals(cdCesta, cesta.cdCesta);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCesta);
        return strBuffer.toString();
    }

    public static String getDsCesta(String cdCesta) throws SQLException {
    	Cesta cesta = new Cesta();
    	cesta.cdCesta = cdCesta;
    	cesta.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	cesta.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	String dsCategoria = CestaService.getInstance().findColumnByRowKey(cesta.getRowKey(), "DSCESTA");
    	cesta = null;
    	if (!ValueUtil.isEmpty(dsCategoria)) {
    		return dsCategoria;
    	} else {
    		return StringUtil.getStringValue(cdCesta);
    	}
    }

	public String getCdDomain() {
		return cdCesta;
	}

	public String getDsDomain() {
		return dsCesta;
	}
}