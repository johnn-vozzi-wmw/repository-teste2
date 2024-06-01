package br.com.wmw.lavenderepda.business.domain;

import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class NotaCredito extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPNOTACREDITO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdNotaCredito;
    public String cdCliente;
    public double vlNotaCredito;
    public String flUtilizada;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    //Não persistente
    public List<String> cdNotaCreditoFilter;

    public NotaCredito() { 
    	
    }
    
    public NotaCredito(String cdEmpresa, String cdRepresentante, String cdCliente) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdRepresentante = cdRepresentante;
    	this.cdCliente = cdCliente;
    	cdNotaCreditoFilter = new ArrayList<>();
    	
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof NotaCredito) {
            NotaCredito notaCredito = (NotaCredito) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, notaCredito.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, notaCredito.cdRepresentante) && 
                ValueUtil.valueEquals(cdNotaCredito, notaCredito.cdNotaCredito);
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
        primaryKey.append(cdNotaCredito);
        return primaryKey.toString();
    }
    
    public String[] cdNotaCreditoFilterToArray() {
    	return (String[]) cdNotaCreditoFilter.toArray (new String[cdNotaCreditoFilter.size()]);
    }

}