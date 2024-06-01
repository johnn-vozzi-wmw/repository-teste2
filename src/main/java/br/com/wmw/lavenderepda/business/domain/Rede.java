package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Rede extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPREDE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdRede;
    public String dsRede;
    public double vlSaldo;
    public double vlLimiteCredito;
    
    public Rede() {
	
    }
    
    public Rede(String cdRede, String dsRede) {
		super();
		this.cdRede = cdRede;
		this.dsRede = dsRede;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rede) {
            Rede rede = (Rede) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, rede.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, rede.cdRepresentante) &&
                ValueUtil.valueEquals(cdRede, rede.cdRede);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRede);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdRede;
	}

	@Override
	public String getDsDomain() {
		return dsRede;
	}
    
}