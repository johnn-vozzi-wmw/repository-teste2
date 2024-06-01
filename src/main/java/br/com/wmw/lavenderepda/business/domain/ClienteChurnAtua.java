package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ClienteChurnAtua extends BasePersonDomain {
	
	public static String TABLE_NAME = "TBLVPCLIENTECHURNATUA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdChurn;
    public String cdChurnAtua;
    public String cdMotivoChurn;
    public String dsObsChurn;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    public ClienteChurnAtua() {
		super(TABLE_NAME);
	}
    
    public ClienteChurnAtua(ClienteChurn clienteChurn) {
		this();
		this.cdEmpresa = clienteChurn.cdEmpresa;
		this.cdRepresentante = clienteChurn.cdRepresentante;
		this.cdChurn = clienteChurn.cdChurn;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof ClienteChurnAtua) {
            ClienteChurnAtua clientechurnatua = (ClienteChurnAtua) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, clientechurnatua.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, clientechurnatua.cdRepresentante) && 
                ValueUtil.valueEquals(cdChurn, clientechurnatua.cdChurn) && 
                ValueUtil.valueEquals(cdChurnAtua, clientechurnatua.cdChurnAtua);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdChurn);
        primaryKey.append(";");
        primaryKey.append(cdChurnAtua);
        return primaryKey.toString();
    }

}