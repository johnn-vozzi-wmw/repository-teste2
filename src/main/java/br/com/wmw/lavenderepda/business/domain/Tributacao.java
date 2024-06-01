package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Tributacao extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPTRIBUTACAO";

	public static int TRIBUTACAO_ISENTO = 0;
	public static int TRIBUTACAO_NORMAL = 1;
	public static int TRIBUTACAO_RETIDO = 2;
	public static int BASE_ICMS_RETIDO_DESCONTO = 1;
	public static int BASE_ICMS_RETIDO_ACRESCIMO = 2;
	public static int BASE_ICMS_RETIDO_DESCONTO_ACRESCIMO = 3;
	public static int BASE_IPI_DESCONTO = 1;
	public static int BASE_IPI_ACRESCIMO = 2;
	public static int BASE_IPI_DESCONTO_ACRESCIMO = 3;

	public static String CDTIPOPEDIDOVALORPADRAO = "0";
	public static String CDUFVALORPADRAO = "0";
	public static String CDTRIBUTACAOPRODUTOPADRAO = "0";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTributacaoCliente;
    public String cdTributacaoProduto;
    public String cdTipoPedido;
    public String cdUf;
    public int cdTipoPrecoBasetributacao;
    public double vlPctRepasseIcms;
    public double vlPctReducaoBaseIcms;
    public double vlPctIcms;
    public int cdTipoRecolhimento;
    public String flBaseIcmsRetidoComDesconto;
    public String flBaseIcmsRetidoComRepasse;
	public String flBaseIpiComDesconto;
    public double vlPctMargemAgregada;
    public double vlPctIcmsRetido;
    public double vlPctReducaoBaseIcmsRetido;
    public double vlPctReducaoIcms;
    public double vlPctMinSt;
    public String flAplicaIpiIcms;
    public String flAplicaIpiRetido;
    public double vlPctFecop;
    public double vlPctFecopRecolher;
    public double vlDespesaAcessoria;
    public String flAplicaFrete;
    public String flAplicaFecopST;
    public int cdTipoFecop;
    public double vlPctPis;
    public double vlPctCofins;
    public String flUsaIpiCalculado;
    public double vlMinPis;
    public double vlMinCofins;	
    public double vlPctIpi;
    public double vlMinIpi;
    public double vlPctOutorga;
    public double vlIcmsRetido;	
    public double vlIcms;	
    public String flMedicamento;
    public String cdBeneficioFiscal;
    public double vlPctDiferenca;
    
    //Não persistentes
    public String[] cdTributacaoClienteFilter;
    	

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Tributacao) {
            Tributacao tributacao = (Tributacao) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tributacao.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tributacao.cdRepresentante) &&
                ValueUtil.valueEquals(cdTributacaoCliente, tributacao.cdTributacaoCliente) &&
                ValueUtil.valueEquals(cdTributacaoProduto, tributacao.cdTributacaoProduto) &&
                ValueUtil.valueEquals(cdTipoPedido, tributacao.cdTipoPedido) &&
                ValueUtil.valueEquals(cdUf, tributacao.cdUf);
        }
        return false;
    }
    
    
    public boolean equalsSemUf(Tributacao tributacao) {
    	return
             ValueUtil.valueEquals(cdEmpresa, tributacao.cdEmpresa) &&
             ValueUtil.valueEquals(cdRepresentante, tributacao.cdRepresentante) &&
             ValueUtil.valueEquals(cdTributacaoCliente, tributacao.cdTributacaoCliente) &&
             ValueUtil.valueEquals(cdTributacaoProduto, tributacao.cdTributacaoProduto) &&
             ValueUtil.valueEquals(cdTipoPedido, tributacao.cdTipoPedido);
    }
    

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTributacaoCliente);
    	strBuffer.append(";");
    	strBuffer.append(cdTributacaoProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdTipoPedido);
    	strBuffer.append(";");
    	strBuffer.append(cdUf);
        return strBuffer.toString();
    }

    public boolean isBaseIcmsRetidoComDesconto() {
    	return ValueUtil.VALOR_SIM.equals(flBaseIcmsRetidoComDesconto) || BASE_ICMS_RETIDO_DESCONTO == ValueUtil.getIntegerValue(flBaseIcmsRetidoComDesconto);
    }
    
	public boolean isBaseIpiComDesconto() {
		return ValueUtil.VALOR_SIM.equals(flBaseIpiComDesconto) || BASE_IPI_DESCONTO == ValueUtil.getIntegerValue(flBaseIpiComDesconto);
	}

	public boolean isBaseIpiComAcrescimo() {
		return BASE_IPI_ACRESCIMO == ValueUtil.getIntegerValue(flBaseIpiComDesconto);
	}

	public boolean isBaseIpiComDescontoAcrescimo() {
		return BASE_IPI_DESCONTO_ACRESCIMO == ValueUtil.getIntegerValue(flBaseIpiComDesconto);
	}
    
    public boolean isBaseIcmsRetidoComAcrescimo() {
    	return BASE_ICMS_RETIDO_ACRESCIMO == ValueUtil.getIntegerValue(flBaseIcmsRetidoComDesconto);
    }
    
    public boolean isBaseIcmsRetidoComDescontoAcrescimo() {
    	return BASE_ICMS_RETIDO_DESCONTO_ACRESCIMO == ValueUtil.getIntegerValue(flBaseIcmsRetidoComDesconto);
    }
    
    public boolean isAplicaIpiNoIcmsRetido() {
		return ValueUtil.VALOR_SIM.equals(flAplicaIpiRetido);
	}    
    
    public boolean isAplicaIpiNoIcms() {
		return ValueUtil.VALOR_SIM.equals(flAplicaIpiIcms);
	}
    
    public boolean isPossuiFundoDePobreza() {
    	return ValueUtil.VALOR_SIM.equals(flAplicaFecopST);
    }

	public boolean isAplicaFrete() {
		return ValueUtil.VALOR_SIM.equals(flAplicaFrete);
	}
		
	public boolean isUsaIpiCalculado() {
		return ValueUtil.VALOR_SIM.equals(flUsaIpiCalculado);
	}
	
	public boolean isBaseIcmsRetidoComRepasse() {
		return ValueUtil.VALOR_SIM.equals(flBaseIcmsRetidoComRepasse);
	}
	
	public boolean isFlMedicamento() {
		return ValueUtil.VALOR_SIM.equals(flMedicamento);
	}

	@Override
	public String getCdDomain() {
		return cdTributacaoCliente;
	}

	@Override
	public String getDsDomain() {
		return cdTributacaoCliente;
	}

}