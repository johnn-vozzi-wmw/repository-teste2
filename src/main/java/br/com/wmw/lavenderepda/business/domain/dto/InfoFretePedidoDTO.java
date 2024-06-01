package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.InfoFretePedido;

public class InfoFretePedidoDTO {

	public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String flTaxaEntrega;
    public double vlTaxaEntrega;
    public String flAjudante;
    public int qtAjudante;
    public String flAntecipaEntrega;
    public String flAgendamento;
    public String cdTipoVeiculo;

    public InfoFretePedidoDTO() {
		super();
	}

    public InfoFretePedidoDTO copy(final InfoFretePedido infoFretePedido) {
		try {
			FieldMapper.copy(infoFretePedido, this);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return this;
	}
    
	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public String getCdRepresentante() {
		return cdRepresentante;
	}

	public String getNuPedido() {
		return nuPedido;
	}

	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}

	public String getFlTaxaEntrega() {
		return flTaxaEntrega;
	}

	public double getVlTaxaEntrega() {
		return vlTaxaEntrega;
	}

	public String getFlAjudante() {
		return flAjudante;
	}

	public int getQtAjudante() {
		return qtAjudante;
	}

	public String getFlAntecipaEntrega() {
		return flAntecipaEntrega;
	}

	public String getFlAgendamento() {
		return flAgendamento;
	}

	public String getCdTipoVeiculo() {
		return cdTipoVeiculo;
	}
    
	
}
