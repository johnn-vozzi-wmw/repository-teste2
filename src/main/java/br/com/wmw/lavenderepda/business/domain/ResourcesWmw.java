package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ResourcesWmw extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPRESOURCESWMW";
	
	public static final String CHAVE_RELATORIO_PDF = "RELATORIO_PDF";

    public String cdChave;
    public byte[] baConteudo;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    public ResourcesWmw() {
    	super();
    }
    
    public ResourcesWmw(String cdChave) {
    	this();
    	this.cdChave = cdChave;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ResourcesWmw) {
            ResourcesWmw resourcesWmw = (ResourcesWmw) obj;
            return
                ValueUtil.valueEquals(cdChave, resourcesWmw.cdChave);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdChave);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdChave;
	}

	@Override
	public String getDsDomain() {
		return cdChave;
	}

}