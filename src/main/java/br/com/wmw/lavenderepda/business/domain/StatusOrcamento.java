package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class StatusOrcamento extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPSTATUSORCAMENTO";
	public static final String NMCOLUNA_CDSTATUSORCAMENTO = "CDSTATUSORCAMENTO";
	public static final String NMCOLUNA_FLPERMITEFECHARPEDIDO = "FLPERMITEFECHARPEDIDO";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String cdStatusOrcamento;
	public String dsStatusOrcamento;
	public String flStatusCancelamento;
	public String flStatusInicial;
	public String flPermiteFecharPedido;
	public String flStatusPreOrcamento;

	public String flPermiteAlterarCondComercial;
	
	//Não Persistentes
	public String notFlStatusPreOrcamento;
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StatusOrcamento) {
			StatusOrcamento status = (StatusOrcamento) obj;
			return ValueUtil.valueEquals(cdEmpresa, status.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, status.cdRepresentante) &&
					ValueUtil.valueEquals(cdStatusOrcamento, status.cdStatusOrcamento);
		}
		return false;
	}

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + cdStatusOrcamento;
	}
	
	public boolean isStatusCancelamento() {
		return ValueUtil.getBooleanValue(flStatusCancelamento);
	}

	@Override
	public String getCdDomain() {
		return cdStatusOrcamento;
	}

	@Override
	public String getDsDomain() {
		return dsStatusOrcamento;
	}
	
	public boolean isStatusInicial() {
		return ValueUtil.getBooleanValue(flStatusInicial);
	}

	public boolean permiteFecharPedido() {
		return ValueUtil.getBooleanValue(flPermiteFecharPedido);
	}
	
	public boolean isStatusPreOrcamento() {
		return ValueUtil.getBooleanValue(flStatusPreOrcamento);
	}
	
	public boolean isPermiteAlterarCondComercialPedido() {
		return ValueUtil.getBooleanValue(flPermiteAlterarCondComercial);
	}

}
