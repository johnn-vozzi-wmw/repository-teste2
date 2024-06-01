package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class GiroProduto extends BaseDomain {

    public static String TABLE_NAME = "TBLVPGIROPRODUTO";

    public static String NM_COLUNA_CDPRODUTO = "cdProduto";
    public static String CD_ITEMGRADE_DEFAULT = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public double qtMediasemanal;
    public double qtMaiorcompra;
    public String dsDia;
    public double qtCompra;
    public double vlUnitario;
    public String dsObservacao;
    public int nuRelevancia;
    public String cdTabelaPreco;
    public double vlUltPreco;

    //-- Não Persistente
    public String dsProduto;
    public Produto produto;
    public ProdutoGrade produtoGrade;
    public ItemGrade itemGrade1;
    public ItemGrade itemGrade2;
    public ItemGrade itemGrade3;
    public Pedido pedidoAtual;
    public ItemPedido itemPedidoUltPreco;
    public boolean ultPrecoCarregado;
    public ItemPedido itemPedidoFilter;
    public String[] cdTabelaPrecoList;
    public Produto produtoFilter;
    public ProdutoCliente produtoClienteFilter;
    public ClienteProduto clienteProdutoFilter;
    public ProdutoCondPagto produtoCondPagtoFilter;
    public ProdutoTipoPed produtoTipoPedFilter;

    public GiroProduto() {}
    
    public GiroProduto(String cdEmpresa, String cdRepresentante, String cdCliente) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdCliente = cdCliente;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj instanceof GiroProduto) {
            GiroProduto giroproduto = (GiroProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, giroproduto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, giroproduto.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, giroproduto.cdCliente) &&
                ValueUtil.valueEquals(cdProduto, giroproduto.cdProduto) &&
                ValueUtil.valueEquals(cdItemGrade1, giroproduto.cdItemGrade1) &&
                ValueUtil.valueEquals(cdItemGrade2, giroproduto.cdItemGrade2) &&
                ValueUtil.valueEquals(cdItemGrade3, giroproduto.cdItemGrade3);
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
    	strBuffer.append(cdCliente);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade1);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade2);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade3);
        return strBuffer.toString();
    }
    
    @Override
    public int getSortIntValue() {
    	return ValueUtil.getIntegerValue(nuRelevancia);
    }

}