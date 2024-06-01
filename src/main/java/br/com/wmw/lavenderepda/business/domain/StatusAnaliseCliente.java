package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class StatusAnaliseCliente extends LavendereBaseDomain {

    public String cdStatusAnalise;
    public String dsStatusAnalise;
    public String flReprovacao;
    public String flConclusao;
    public Date dtAlteracao;
    public String hrAlteracao;
    public String flPermiteEdicao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof StatusAnaliseCliente) {
            StatusAnaliseCliente statusAnaliseCliente = (StatusAnaliseCliente) obj;
            return
                ValueUtil.valueEquals(cdStatusAnalise, statusAnaliseCliente.cdStatusAnalise);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdStatusAnalise);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdStatusAnalise;
	}

	@Override
	public String getDsDomain() {
		return dsStatusAnalise;
	}

}