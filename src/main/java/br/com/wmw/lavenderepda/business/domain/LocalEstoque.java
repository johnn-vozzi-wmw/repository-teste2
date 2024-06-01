package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class LocalEstoque extends LavendereBaseDomain{
	
	public static final String TABLE_NAME = "TBLVPLOCALESTOQUE";
	
	public String cdEmpresa;
	public String cdRepresentante;
    public String cdLocalEstoque;
    public String dsLocalEstoque;
    public Integer nuOrdem;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    public LocalEstoque() { }
    
    public LocalEstoque(String cdEmpresa, String cdRepresentante, String cdLocalEstoque) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdRepresentante = cdRepresentante;
    	this.cdLocalEstoque = cdLocalEstoque;
    }
    
	@Override
	public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdLocalEstoque);
        return primaryKey.toString();
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocalEstoque) {
        	LocalEstoque localEstoque = (LocalEstoque) obj;
            return
            	ValueUtil.valueEquals(cdEmpresa, localEstoque.cdEmpresa) &&
            	ValueUtil.valueEquals(cdRepresentante, localEstoque.cdRepresentante) &&
                ValueUtil.valueEquals(cdLocalEstoque, localEstoque.cdLocalEstoque);
        }
        return false;
    }	
    
	@Override
	public String getCdDomain() {
		return cdLocalEstoque;
	}

	@Override
	public String getDsDomain() {
		return dsLocalEstoque;
	}
}
