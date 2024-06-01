package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;

public class MargemRentab extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPMARGEMRENTAB";
	public static String NMCOLUNA_DTINIVIGENCIA = "dtIniVigencia";
	public static String NMCOLUNA_DTFIMVIGENCIA = "dtFimVigencia";

    public String cdEmpresa;
    public String cdMargemRentab;
    public Date dtIniVigencia;
    public Date dtFimVigencia;
    public String cdPlataformaVenda;
    public String cdCentroCusto;
    public String cdGrupoDescCli;
    public String cdCliente;
    public String cdLinha;
    public String cdStatusColecao;
    public String cdGrupoProduto1;
    public String cdProduto;
    public String flMargemRentabPedido;
    public String cdTabelaPreco;
    public double vlPctMargemRentabMin;
    public double vlPctMargemRentabMax;
    
    public Date dtAlteracao;
    public String hrAlteracao;

    public MargemRentab() {
	
    }
    
    
    public MargemRentab(ItemPedido itemPedido) throws SQLException {
    	Produto produto = itemPedido.getProduto();
    	Pedido pedido = itemPedido.pedido;
        this.cdEmpresa = itemPedido.cdEmpresa;
        this.cdMargemRentab = itemPedido.cdMargemRentab;
        this.dtIniVigencia = DateUtil.getCurrentDate();
        this.dtFimVigencia = DateUtil.getCurrentDate();
		if (pedido == null) return;
		this.cdPlataformaVenda = pedido.cdPlataformaVenda;
		this.cdCentroCusto = pedido.cdCentroCusto;
		this.cdGrupoDescCli = pedido.getCliente().cdGrupoDescCli;
		this.cdCliente = pedido.cdCliente;
		if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && ValueUtil.isNotEmpty(itemPedido.cdTabelaPreco)) {
			this.cdTabelaPreco = itemPedido.cdTabelaPreco;
		} else {
			this.cdTabelaPreco = pedido.cdTabelaPreco;
		}
		if (produto == null) return;
		this.cdLinha = produto.cdLinha;
		this.cdStatusColecao = produto.cdStatusColecao;
		this.cdGrupoProduto1 = produto.cdGrupoProduto1;
		this.cdProduto = produto.cdProduto;
	}
    
    public MargemRentab(Pedido pedido) throws SQLException {
    	if (pedido == null) return;
		this.cdEmpresa = pedido.cdEmpresa;
        this.cdMargemRentab = pedido.cdMargemRentab;
		this.dtIniVigencia = DateUtil.getCurrentDate();
		this.dtFimVigencia = DateUtil.getCurrentDate();
		this.cdPlataformaVenda = pedido.cdPlataformaVenda;
		this.cdCentroCusto = pedido.cdCentroCusto;
		this.cdGrupoDescCli = pedido.getCliente().cdGrupoDescCli;
		this.cdCliente = pedido.cdCliente;
		this.flMargemRentabPedido = ValueUtil.VALOR_SIM;
        this.cdTabelaPreco = pedido.cdTabelaPreco;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof MargemRentab) {
            MargemRentab margemrentab = (MargemRentab) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, margemrentab.cdEmpresa) && 
                ValueUtil.valueEquals(cdMargemRentab, margemrentab.cdMargemRentab);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdMargemRentab);
        return primaryKey.toString();
    }

}
