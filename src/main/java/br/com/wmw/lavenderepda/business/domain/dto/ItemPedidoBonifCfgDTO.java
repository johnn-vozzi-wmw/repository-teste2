package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;

public class ItemPedidoBonifCfgDTO {
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String flOrigemPedido;
	public String nuPedido;
	public String cdProduto;
	public String flTipoItemPedido;
	public int nuSeqProduto;
	public String cdBonifCfg;
	public String flTipoRegistro;
	public double vlBonificacao;
	public double qtBonificacao;
	public String flBonificacaoAutomatica;
	
	public ItemPedidoBonifCfgDTO() {
	}
	
	public ItemPedidoBonifCfgDTO(ItemPedidoBonifCfg itemPedidoBonifCfg) {
		try {
			FieldMapper.copy(itemPedidoBonifCfg, this);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public String getCdEmpresa() {
		return cdEmpresa;
	}
	
	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}
	
	public String getCdRepresentante() {
		return cdRepresentante;
	}
	
	public void setCdRepresentante(String cdRepresentante) {
		this.cdRepresentante = cdRepresentante;
	}
	
	public String getFlOrigemPedido() {
		return flOrigemPedido;
	}
	
	public void setFlOrigemPedido(String flOrigemPedido) {
		this.flOrigemPedido = flOrigemPedido;
	}
	
	public String getNuPedido() {
		return nuPedido;
	}
	
	public void setNuPedido(String nuPedido) {
		this.nuPedido = nuPedido;
	}
	
	public String getCdProduto() {
		return cdProduto;
	}
	
	public void setCdProduto(String cdProduto) {
		this.cdProduto = cdProduto;
	}
	
	public String getFlTipoItemPedido() {
		return flTipoItemPedido;
	}
	
	public void setFlTipoItemPedido(String flTipoItemPedido) {
		this.flTipoItemPedido = flTipoItemPedido;
	}
	
	public int getNuSeqProduto() {
		return nuSeqProduto;
	}
	
	public void setNuSeqProduto(int nuSeqProduto) {
		this.nuSeqProduto = nuSeqProduto;
	}
	
	public String getCdBonifCfg() {
		return cdBonifCfg;
	}
	
	public void setCdBonifCfg(String cdBonifCfg) {
		this.cdBonifCfg = cdBonifCfg;
	}
	
	public String getFlTipoRegistro() {
		return flTipoRegistro;
	}
	
	public void setFlTipoRegistro(String flTipoRegistro) {
		this.flTipoRegistro = flTipoRegistro;
	}
	
	public double getVlBonificacao() {
		return vlBonificacao;
	}
	
	public void setVlBonificacao(double vlBonificacao) {
		this.vlBonificacao = vlBonificacao;
	}
	
	public double getQtBonificacao() {
		return qtBonificacao;
	}
	
	public void setQtBonificacao(double qtBonificacao) {
		this.qtBonificacao = qtBonificacao;
	}
	
	public String getFlBonificacaoAutomatica() {
		return flBonificacaoAutomatica;
	}
	
	public void setFlBonificacaoAutomatica(String flBonificacaoAutomatica) {
		this.flBonificacaoAutomatica = flBonificacaoAutomatica;
	}
	
}
