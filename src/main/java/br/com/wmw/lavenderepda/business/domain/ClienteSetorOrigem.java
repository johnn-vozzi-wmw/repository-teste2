package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ClienteSetorOrigem extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPCLIENTESETORORIGEM";

	public static final String CLIENTE_SETOR_REDE = "RED";
	public static final String CLIENTE_SETOR_CONTRATO = "CLI";

	public static final String CLIENTE_SETOR_ORIGEM_SEM_CONTRATO = "--- Sem Contrato ---";
	public static final String CLIENTE_SETOR_CDSETOR_VAZIO = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoCliRede;
    public String cdCliRede;
    public String cdSetor;
    public String cdOrigemSetor;
    public String dsSetor;
    public String dsOrigemSetor;
    public String cdCondicaoPagamento;

    // Não Persistente
    public boolean isSetorOrigem;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ClienteSetorOrigem) {
            ClienteSetorOrigem clienteSetorOrigem = (ClienteSetorOrigem) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, clienteSetorOrigem.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, clienteSetorOrigem.cdRepresentante) &&
                ValueUtil.valueEquals(cdTipoCliRede, clienteSetorOrigem.cdTipoCliRede) &&
                ValueUtil.valueEquals(cdCliRede, clienteSetorOrigem.cdCliRede) &&
                ValueUtil.valueEquals(cdOrigemSetor, clienteSetorOrigem.cdOrigemSetor) &&
                (clienteSetorOrigem.cdSetor != null ? ValueUtil.valueEquals(cdSetor, clienteSetorOrigem.cdSetor) : true);
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
        primaryKey.append(cdTipoCliRede);
        primaryKey.append(";");
        primaryKey.append(cdCliRede);
        primaryKey.append(";");
        primaryKey.append(cdSetor);
        primaryKey.append(";");
        primaryKey.append(cdOrigemSetor);
        return primaryKey.toString();
    }

    public String toString() {
    	if (isSetorOrigem) {
    		return dsOrigemSetor;
    	} else {
    		return dsSetor;
    	}
    }

	public String getCdDomain() {
		if (isSetorOrigem) {
    		return cdOrigemSetor;
    	} else {
    		return cdSetor;
    	}
	}

	public String getDsDomain() {
		if (isSetorOrigem) {
    		return dsOrigemSetor;
    	} else {
    		return dsSetor;
    	}
	}

}