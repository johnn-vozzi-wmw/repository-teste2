package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ItemPedidoBonifCfg extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPITEMPEDIDOBONIFCFG";
	public static String FLTIPOREGISTRO_CREDITO = "C";
	public static String FLTIPOREGISTRO_DEBITO = "D";
	public static String FLTIPOREGISTRO_BRINDE = "B";
	public static final String NMCAMPOTIPOREGISTRO = "flTipoRegistro";
	public static final String NMCAMPOTIPOITEMPEDIDO = "flTipoItemPedido";
	
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
	
	//Não persistentes
	private Produto produto;
	private BonifCfg bonifCfg;
	private BonifCfgBrinde bonifCfgBrinde;
	public String[] inFlTipoRegistro;
	public String[] notInFlTipoRegistro;
	public boolean isCreateItemPedidoBonifCfg;
	public String cdTipoRegraBonificacao;
	
	public ItemPedidoBonifCfg() {
		super();
	}
	
	public ItemPedidoBonifCfg(ItemPedido itemPedido) {
		this.cdEmpresa = itemPedido.cdEmpresa;
		this.cdRepresentante = itemPedido.cdRepresentante;
		this.nuPedido = itemPedido.nuPedido;
		this.flOrigemPedido = itemPedido.flOrigemPedido;
		this.cdProduto = itemPedido.cdProduto;
		this.flTipoItemPedido = itemPedido.flTipoItemPedido;
		this.nuSeqProduto = itemPedido.nuSeqProduto;
	}

	public ItemPedidoBonifCfg(Pedido pedido) {
		this.cdEmpresa = pedido.cdEmpresa;
		this.cdRepresentante = pedido.cdRepresentante;
		this.nuPedido = pedido.nuPedido;
		this.flOrigemPedido = pedido.flOrigemPedido;
	}
	
	@Override
	public String getPrimaryKey() {
		return getPrimaryKeyItemPedido() + cdBonifCfg + ";" + flTipoRegistro;
	}

	public String getPrimaryKeyItemPedido() {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append(cdEmpresa);
		strBuffer.append(";");
		strBuffer.append(cdRepresentante);
		strBuffer.append(";");
		strBuffer.append(flOrigemPedido);
		strBuffer.append(";");
		strBuffer.append(nuPedido);
		strBuffer.append(";");
		strBuffer.append(cdProduto);
		strBuffer.append(";");
		strBuffer.append(flTipoItemPedido);
		strBuffer.append(";");
		strBuffer.append(nuSeqProduto);
		strBuffer.append(";");
		return strBuffer.toString();
	}
	
	public boolean isFlTipoRegistroCredito() {
		return ValueUtil.valueEquals(this.flTipoRegistro, FLTIPOREGISTRO_CREDITO);
	}
	
	public boolean isFlTipoRegistroDebito() {
		return ValueUtil.valueEquals(this.flTipoRegistro, FLTIPOREGISTRO_DEBITO);
	}
	
	public boolean isFlTipoRegistroBrinde() {
		return ValueUtil.valueEquals(this.flTipoRegistro, FLTIPOREGISTRO_BRINDE);
	}

	public Produto getProduto() {
		if (produto == null) {
			produto = new Produto();
		}
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public BonifCfg getBonifCfg() {
		if (bonifCfg == null) {
			bonifCfg = new BonifCfg(this.cdEmpresa, this.cdBonifCfg);
		}
		return bonifCfg;
	}
	
	public void setBonifCfg(BonifCfg bonifCfg) {
		this.bonifCfg = bonifCfg;
	}

	public BonifCfgBrinde getBonifCfgBrinde() {
		if (bonifCfgBrinde == null) {
			bonifCfgBrinde = new BonifCfgBrinde();
			bonifCfgBrinde.cdEmpresa = this.cdEmpresa;
			bonifCfgBrinde.cdBonifCfg = this.cdBonifCfg;
			bonifCfgBrinde.cdProduto = this.cdProduto;
		}
		return bonifCfgBrinde;
	}

	public void setBonifCfgBrinde(BonifCfgBrinde bonifCfgBrinde) {
		this.bonifCfgBrinde = bonifCfgBrinde;
	}
	
	public void setBonifCfgBrinde(Double qtBrinde, String flOpcional) {
		BonifCfgBrinde bonifCfgBrinde = getBonifCfgBrinde();
		bonifCfgBrinde.qtBrinde = qtBrinde;
		bonifCfgBrinde.flOpcional = flOpcional;
		setBonifCfgBrinde(bonifCfgBrinde);
	}

	public boolean isBonificacaoAutomatica() {
		return ValueUtil.VALOR_SIM.equalsIgnoreCase(flBonificacaoAutomatica);
	}

}
