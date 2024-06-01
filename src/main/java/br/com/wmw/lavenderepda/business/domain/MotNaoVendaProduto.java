package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class MotNaoVendaProduto extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPMOTNAOVENDAPRODUTO";

    public String cdMotivo;
    public String dsMotivo;
    public String flExigeJustificativa;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MotNaoVendaProduto) {
            MotNaoVendaProduto motNaoVendaProduto = (MotNaoVendaProduto) obj;
            return ValueUtil.valueEquals(cdMotivo, motNaoVendaProduto.cdMotivo);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdMotivo);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdMotivo;
	}

	@Override
	public String getDsDomain() {
		return dsMotivo;
	}

	public boolean isExigeJustificativa() {
		return ValueUtil.getBooleanValue(flExigeJustificativa);
	}

}