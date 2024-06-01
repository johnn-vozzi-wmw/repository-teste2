package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class TipoPagamento extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPTIPOPAGAMENTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoPagamento;
    public String dsTipoPagamento;
    public int nuNivel;
    public double qtMinValor;
    public String flEspecial;
    public String flPagamentoAVista;
    public String flCheque;
    public String flUsaVencimento;
    public String cdBoletoConfig;
    public String flBoleto;
    public String flPermiteMultiplosPagamentos;
    public String flRestrito;
    public double vlIndicePagamento;
    public double vlPctJuros;
    public double vlPctMultaDiario;
    public double vlPctMulta;
    public int nuDiasProtesto;
    public int nuDiasMulta;
    public int nuDiasMaxPagamento;
    public String flPermiteUsoModuloPagamento;
    public String flIgnoraLimiteCredito;
    public String flOcultoParaNovoCliente;
    
    //Não persistente
    public CondTipoPagto condTipoPagtoFilter;
    public TipoCondPagtoCli tipoCondPagtoCliFilter;
    public TipoPagtoCli tipoPagtoCliFilter;
    public TipoPagtoTabPreco tipoPagtoTabPrecoFilter;
    public CondicaoPagamento condicaoPagamentoFilter;
    public String notFlRestrito;
    public String notFlPermiteUsoModuloPagamento;
    public String notFlIgnoraLimiteCredito;
    public String notFlOcultoParaNovoCliente;
    public boolean filtraQuandoPossuiCondPagto;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoPagamento) {
            TipoPagamento tipoPagamento = (TipoPagamento) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoPagamento.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tipoPagamento.cdRepresentante) &&
                ValueUtil.valueEquals(cdTipoPagamento, tipoPagamento.cdTipoPagamento);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTipoPagamento);
        return strBuffer.toString();
    }

    public boolean isEspecial() {
    	return ValueUtil.VALOR_SIM.equals(flEspecial);
    }

	public String getCdDomain() {
		return cdTipoPagamento;
	}

	public String getDsDomain() {
		return dsTipoPagamento;
	}
	
	public boolean isTipoPagamentoAVista() {
		if (LavenderePdaConfig.isTipoPagamentoOcultoAndNaoSetaPadrao()) {
			return true;
		}
		return ValueUtil.VALOR_SIM.equals(flPagamentoAVista);
	}
	
	public boolean isCheque() {
		return ValueUtil.getBooleanValue(flCheque);
	}
	
	public boolean isUsaVencimento() {
		return ValueUtil.getBooleanValue(flUsaVencimento);
	}

	public boolean isBoleto() {
		return ValueUtil.getBooleanValue(flBoleto);
	}
	
	public boolean isPermiteMultiplosPagamentos() {
		return ValueUtil.VALOR_SIM.equals(flPermiteMultiplosPagamentos);
	}
    
    public boolean isRestrito() {
    	return ValueUtil.VALOR_SIM.equals(flRestrito);
    }
    
    public boolean isIgnoraLimiteCredito() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flIgnoraLimiteCredito);
	}
    
    public boolean isOcultoParaNovoCliente() {
    	return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flOcultoParaNovoCliente);
    }

}