package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.util.Date;

public class PedidoConsignacao extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPPEDIDOCONSIGNACAO";
	public static String TIPO_REGISTRO_INCLUSAO = "I";
	public static String TIPO_REGISTRO_DEVOLUCAO = "D";
	public static String TIPO_REGISTRO_CANCELAMENTO = "C";
	public static String TIPO_REGISTRO_AGRUPADO = "A";
	public static String TIPO_REGISTRO_FECHAMENTO = "F";
	public static final String APPOBJ_CAMPOS_FILTRO_PEDIDO_CONSIGNACAO = "PedidoConsignacao";
	public static final String NMCOLUNA_NUSEQITEMPEDIDO = "NUSEQITEMPEDIDO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public String cdCliente;
    public int nuSeqConsignacao;
    public Date dtEmissao;
    public String hrEmissao;
    public int nuSeqItemPedido;
    public String cdTabelaPreco;
    public String cdCondicaoPagamento;
    public String cdTipoPedido;
    public String cdTipoPagamento;
    public String cdCondicaoComercial;
    public String cdUnidade;
    public double qtItemFisico;
    public double vlItemPedido;
    public double vlTotalItemPedido;
    public double vlPctDesconto;
    public double vlPctAcrescimo;
    public String flTipoRegistro;
    public String cdLoteProduto;
    
    //Não persistente
    public Produto produto;
    public Date dtEmissaoFilter;
    
    public void copyPropertiesfromPedido(Pedido pedido) {
    	if (ValueUtil.isNotEmpty(pedido.cdEmpresa)) {
    		cdEmpresa = pedido.cdEmpresa;
    	}
    	if (ValueUtil.isNotEmpty(pedido.cdRepresentante)) {
    		cdRepresentante = pedido.cdRepresentante;
    	}
		if (ValueUtil.isNotEmpty(pedido.nuPedido)) {
			nuPedido = pedido.nuPedido;
		}
		if (ValueUtil.isNotEmpty(pedido.flOrigemPedido)) {
			flOrigemPedido = pedido.flOrigemPedido;
		}
		if (ValueUtil.isNotEmpty(pedido.cdCliente)) {
			cdCliente = pedido.cdCliente;
		}
		if (ValueUtil.isNotEmpty(pedido.dtEmissao)) {
			dtEmissao = pedido.dtEmissao;
		}
		if (ValueUtil.isNotEmpty(pedido.hrEmissao)) {
			hrEmissao = pedido.hrEmissao;
		}
		if (ValueUtil.isNotEmpty(pedido.cdTabelaPreco)) {
			cdTabelaPreco = pedido.cdTabelaPreco;
		}
		if (ValueUtil.isNotEmpty(pedido.cdCondicaoPagamento)) {
			cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		}
		if (ValueUtil.isNotEmpty(pedido.cdTipoPedido)) {
			cdTipoPedido = pedido.cdTipoPedido;
		}
		if (ValueUtil.isNotEmpty(pedido.cdTipoPagamento)) {
			cdTipoPagamento = pedido.cdTipoPagamento;
		}
		if (ValueUtil.isNotEmpty(pedido.cdCondicaoComercial)) {
			cdCondicaoComercial = pedido.cdCondicaoComercial;
		}
		if (ValueUtil.isNotEmpty(pedido.cdUnidade)) {
			cdUnidade = pedido.cdUnidade;
		}
    }
    
    public void copyPropertiesfromItem(ItemPedido itemPedido) {
		if (ValueUtil.isNotEmpty(itemPedido.cdEmpresa)) {
			cdEmpresa = itemPedido.cdEmpresa;
		}
		if (ValueUtil.isNotEmpty(itemPedido.cdRepresentante)) {
			cdRepresentante = itemPedido.cdRepresentante;
		}
		if (ValueUtil.isNotEmpty(itemPedido.nuPedido)) {
			nuPedido = itemPedido.nuPedido;
		}
		if (ValueUtil.isNotEmpty(itemPedido.flOrigemPedido)) {
			flOrigemPedido = itemPedido.flOrigemPedido;
		}
		if (ValueUtil.isNotEmpty(itemPedido.cdProduto)) {
			cdProduto = itemPedido.cdProduto;
		}
		if (ValueUtil.isNotEmpty(itemPedido.flTipoItemPedido)) {
			flTipoItemPedido = itemPedido.flTipoItemPedido;
		}
		nuSeqProduto = itemPedido.nuSeqProduto;
		if (ValueUtil.isNotEmpty(itemPedido.cdCliente)) {
			cdCliente = itemPedido.cdCliente;
		}
		nuSeqItemPedido = itemPedido.nuSeqItemPedido;
		if (ValueUtil.isNotEmpty(itemPedido.cdTabelaPreco)) {
			cdTabelaPreco = itemPedido.cdTabelaPreco;
		}
		if (ValueUtil.isNotEmpty(itemPedido.cdCondicaoComercial)) {
			cdCondicaoComercial = itemPedido.cdCondicaoComercial;
		}
		if (ValueUtil.isNotEmpty(itemPedido.cdUnidade)) {
			cdUnidade = itemPedido.cdUnidade;
		}
		qtItemFisico = itemPedido.qtItemFisico;
		vlItemPedido = itemPedido.vlItemPedido;
		vlTotalItemPedido = itemPedido.vlTotalItemPedido;
		vlPctDesconto = itemPedido.vlPctDesconto;
		vlPctAcrescimo = itemPedido.vlPctAcrescimo;
		if (ValueUtil.isNotEmpty(itemPedido.cdLoteProduto)) {
			cdLoteProduto = itemPedido.cdLoteProduto;
		}
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PedidoConsignacao) {
            PedidoConsignacao pedidoconsignacao = (PedidoConsignacao) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pedidoconsignacao.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, pedidoconsignacao.cdRepresentante) && 
                ValueUtil.valueEquals(flOrigemPedido, pedidoconsignacao.flOrigemPedido) && 
                ValueUtil.valueEquals(nuPedido, pedidoconsignacao.nuPedido) && 
                ValueUtil.valueEquals(cdProduto, pedidoconsignacao.cdProduto) && 
                ValueUtil.valueEquals(flTipoItemPedido, pedidoconsignacao.flTipoItemPedido) && 
                ValueUtil.valueEquals(nuSeqProduto, pedidoconsignacao.nuSeqProduto) && 
                ValueUtil.valueEquals(cdCliente, pedidoconsignacao.cdCliente) &&
                ValueUtil.valueEquals(nuSeqConsignacao, pedidoconsignacao.nuSeqConsignacao);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(flTipoItemPedido);
        primaryKey.append(";");
        primaryKey.append(nuSeqProduto);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(nuSeqConsignacao);
        return primaryKey.toString();
    }
    
    @Override
    public boolean isEnviadoServidor() {
    	return ValueUtil.isEmpty(flTipoAlteracao);
	}
    
    public boolean isPedidoConsignacaoIniciadoProcessoEnvio() {
		return Pedido.FLTIPOALTERACAO_PROCESSANDO_ENVIO.equals(flTipoAlteracao);
	}
    
    public Produto getProduto() throws SQLException {
    	if (ValueUtil.isNotEmpty(cdProduto) && (produto == null || !cdProduto.equals(produto.cdProduto))) {
    		produto = ProdutoService.getInstance().getProduto(cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto);
    	}
    	return produto;
    }
    
    public String getDsProduto() throws SQLException {
    	if (ValueUtil.isNotEmpty(cdProduto) && (produto == null || !cdProduto.equals(produto.cdProduto))) {
    		produto = ProdutoService.getInstance().getProduto(cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto);
    	}
    	return ProdutoService.getInstance().getDsProduto(this.produto);
    }

    public String getDsProdutoWithKey() throws SQLException {
    	return ProdutoService.getInstance().getDescriptionWithId(this.produto, this.cdProduto);
    }
    
}