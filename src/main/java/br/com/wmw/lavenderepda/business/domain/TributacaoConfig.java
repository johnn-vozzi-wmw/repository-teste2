package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;

public class TributacaoConfig extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPTRIBUTACAOCONFIG";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTributacaoConfig;
    public String cdTributacaoCliente;
    public String cdTributacaoProduto;
    public String cdTipoPedido;
    public String cdCliente;
    public String cdProduto;
    public String cdUf;
    public String flCalculaIpi;
    public String flFreteBaseIpi;
    public String flTipoCalculoPisCofins;
    public String flFreteBasePisCofins;
    public String flCalculaIcms;
    public String flFreteBaseIcms;
    public String flIpiBaseIcms;
    public String flCalculaSt;
    public String flFreteBaseSt;
    public String flIpiBaseSt;
    public String flVerificaValorItem;
    public String flAplicaReducaoBaseIcmsRetido;
    public String flUtilizaValorFixoImpostos;
	public String cdClassificFiscal;
    public String flReduzIcmsPisCofins;
        
    public TributacaoConfig() {
	}
    
    public TributacaoConfig(String cdTributacaoClienteFilter, String cdTributacaoProdutoFilter, String cdTipoPedidoFilter, String cdClienteFilter, String cdProdutoFilter, String cdUFFilter) {
		super();
		cdEmpresa = SessionLavenderePda.cdEmpresa;
		cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TributacaoConfig.class);
		cdTributacaoCliente = cdTributacaoClienteFilter;
		cdTributacaoProduto = cdTributacaoProdutoFilter;
		cdTipoPedido = cdTipoPedidoFilter;
		cdCliente = cdClienteFilter;
		cdProduto = cdProdutoFilter;
		cdUf = cdUFFilter;
	}

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TributacaoConfig) {
            TributacaoConfig tributacaoconfig = (TributacaoConfig) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tributacaoconfig.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, tributacaoconfig.cdRepresentante) && 
                ValueUtil.valueEquals(cdTributacaoCliente, tributacaoconfig.cdTributacaoCliente) && 
                ValueUtil.valueEquals(cdTributacaoProduto, tributacaoconfig.cdTributacaoProduto) && 
                ValueUtil.valueEquals(cdTipoPedido, tributacaoconfig.cdTipoPedido) && 
                ValueUtil.valueEquals(cdCliente, tributacaoconfig.cdCliente) && 
                ValueUtil.valueEquals(cdProduto, tributacaoconfig.cdProduto) && 
                ValueUtil.valueEquals(cdUf, tributacaoconfig.cdUf);
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
        primaryKey.append(cdTributacaoCliente);
        primaryKey.append(";");
        primaryKey.append(cdTributacaoProduto);
        primaryKey.append(";");
        primaryKey.append(cdTipoPedido);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdUf);
        return primaryKey.toString();
    }
    
    public boolean isCalculaPisCofins() {
    	return "1".equals(flTipoCalculoPisCofins);
    }
    
    public boolean isCalculaEDebitaPisCofins() {
    	return "2".equals(flTipoCalculoPisCofins);
    }
    
    public boolean isAplicaValorFreteNaBasePisCofins() {
    	return ValueUtil.VALOR_SIM.equals(flFreteBasePisCofins);
    }
    
    public boolean isCalculaIpi() {
    	return ValueUtil.VALOR_SIM.equals(flCalculaIpi);
    }
    
    public boolean isAplicaValorFreteNaBaseIpi() {
    	return ValueUtil.VALOR_SIM.equals(flFreteBaseIpi);
    }
    
    public boolean isCalculaIcms() {
    	return ValueUtil.VALOR_SIM.equals(flCalculaIcms);
    }
   
    public boolean isAplicaValorFreteNaBaseIcms() {
    	return ValueUtil.VALOR_SIM.equals(flFreteBaseIcms); 
    }
    
    public boolean isAplicaValorIpiNaBaseIcms() {
    	return ValueUtil.VALOR_SIM.equals(flIpiBaseIcms); 
    }
    
    public boolean isCalculaSt() {
    	return ValueUtil.VALOR_SIM.equals(flCalculaSt);
    }
    
    public boolean isCalculaSt2() {
        return "2".equals(flCalculaSt);
    }
    
    public boolean isAplicaValorFreteNaBaseSt() {
    	return ValueUtil.VALOR_SIM.equals(flFreteBaseSt); 
    }
    
    public boolean isAplicaValorIpiNaBaseSt() {
    	return ValueUtil.VALOR_SIM.equals(flIpiBaseSt); 
    }
    
	public boolean isVerificaValorItem() {
		return ValueUtil.VALOR_SIM.equals(flVerificaValorItem);
	}
	
	public boolean isIndEscala() {
		return ValueUtil.VALOR_SIM.equals(flUtilizaValorFixoImpostos);
	}

	public boolean isAplicaReducaoIcmsBaseCalculoPisCofins() {
    	return ValueUtil.VALOR_SIM.equals(flReduzIcmsPisCofins);
	}

}
