package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class PlataformaVenda extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPPLATAFORMAVENDA";

    public String cdEmpresa;
    public String cdPlataformaVenda;
    public String dsPlataformaVenda;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlataformaVenda) {
            PlataformaVenda plataformavenda = (PlataformaVenda) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, plataformavenda.cdEmpresa) && 
                ValueUtil.valueEquals(cdPlataformaVenda, plataformavenda.cdPlataformaVenda);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdPlataformaVenda);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdPlataformaVenda;
	}

	@Override
	public String getDsDomain() {
		return dsPlataformaVenda;
	}
    
}