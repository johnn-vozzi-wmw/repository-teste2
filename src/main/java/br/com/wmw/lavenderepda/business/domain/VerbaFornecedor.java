package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.FornecedorService;

public class VerbaFornecedor extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPVERBAFORNECEDOR";
    
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdFornecedor;
    public String flOrigemVerba;
    public double vlSaldo;
    public double vlSaldoInicial;
    
    //-- Não Persistente
    private Fornecedor fornecedor;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaFornecedor) {
            VerbaFornecedor verbaFornecedor = (VerbaFornecedor) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaFornecedor.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, verbaFornecedor.cdRepresentante) && 
                ValueUtil.valueEquals(cdFornecedor, verbaFornecedor.cdFornecedor) &&
            	ValueUtil.valueEquals(flOrigemVerba, verbaFornecedor.flOrigemVerba);
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
        primaryKey.append(cdFornecedor);
        primaryKey.append(";");
        primaryKey.append(flOrigemVerba);
        return primaryKey.toString();
    }

	public Fornecedor getFornecedor() throws SQLException {
		if (fornecedor == null) {
			Fornecedor filter = new Fornecedor();
			filter.cdEmpresa = cdEmpresa;
			filter.cdFornecedor = cdFornecedor;
			fornecedor = (Fornecedor) FornecedorService.getInstance().findByRowKey(filter.getRowKey());
		}
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
    

}