package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class DescPromocional extends LavendereBaseDomain {

	public static final String CD_CHAVE_VAZIO = "0";  
	public static final String NOME_COLUNA_QTITEM = "QTITEM";
	public static final String TABLE_NAME = "TBLVPDESCPROMOCIONAL";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdProduto;
    public String cdGrupoDescCli;
    public String cdGrupoDescProd;
    public int qtItem;
    public String cdTabelaPreco;
    public String cdCondicaoComercial;
    public String dsDescPromocional;
    public double vlProdutoFinal;
    public double vlDescontoProduto;
    public double vlPctDescontoProduto;
    public double vlPctComissao;
    public double vlPctRentabilidade;
    public Date dtInicial;
    public Date dtFinal;
    public int nuPromocao; //coluna faz parte da chave, porém, pda não deve entende-la como sendo, para não estragar a busca dos descontos
    public double qtMinVenda;
    public double qtMinGrade1;
    public double qtMinGrade2;
    public String cdLocal;
    public String cdTipoPedido;
    public String cdUnidade;
    public String cdCondicaoPagamento;
    public String cdTipoFrete;
    public String cdLocalEstoque;
    public String cdUf;
    public String cdRegrasAdicionaisDescPromo; //coluna chave que somente agrupa os códigos adicionais (cdTipoPedido;cdCondicaoPagamento;cdLocalEstoque;cdTipoFrete;cdUf), mas não é utilizada para realizar os filtros
    
    //Nao persistente
    public String flValidaQtMinDescPromocional;
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DescPromocional) {
            DescPromocional descPromocional = (DescPromocional) obj;
            return ValueUtil.valueEquals(cdEmpresa, descPromocional.cdEmpresa)
					&& ValueUtil.valueEquals(cdRepresentante, descPromocional.cdRepresentante)
					&& ValueUtil.valueEquals(cdCliente, descPromocional.cdCliente)
					&& ValueUtil.valueEquals(cdProduto, descPromocional.cdProduto)
					&& ValueUtil.valueEquals(cdGrupoDescCli, descPromocional.cdGrupoDescCli)
					&& ValueUtil.valueEquals(cdGrupoDescProd, descPromocional.cdGrupoDescProd)
					&& ValueUtil.valueEquals(qtItem, descPromocional.qtItem)
					&& ValueUtil.valueEquals(cdTabelaPreco, descPromocional.cdTabelaPreco)
					&& ValueUtil.valueEquals(cdCondicaoComercial, descPromocional.cdCondicaoComercial)
					&& ValueUtil.valueEquals(cdLocal, descPromocional.cdLocal)
					&& ValueUtil.valueEquals(cdCondicaoPagamento, descPromocional.cdCondicaoPagamento)
					&& ValueUtil.valueEquals(cdTipoFrete, descPromocional.cdTipoFrete)
					&& ValueUtil.valueEquals(cdLocalEstoque, descPromocional.cdLocalEstoque)
					&& ValueUtil.valueEquals(cdUf, descPromocional.cdUf)
					&& ValueUtil.valueEquals(cdTipoPedido, descPromocional.cdTipoPedido);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdGrupoDescCli);
        primaryKey.append(";");
        primaryKey.append(cdGrupoDescProd);
        primaryKey.append(";");
        primaryKey.append(qtItem);
        primaryKey.append(";");
        primaryKey.append(cdTabelaPreco);
        primaryKey.append(";");
        primaryKey.append(cdCondicaoComercial);
        primaryKey.append(";");
        primaryKey.append(cdLocal);
        primaryKey.append(";");
        primaryKey.append(cdTipoPedido);
        primaryKey.append(";");
        primaryKey.append(cdCondicaoPagamento);
        primaryKey.append(";");
        primaryKey.append(cdLocalEstoque);
        primaryKey.append(";");
        primaryKey.append(cdTipoFrete);
        primaryKey.append(";");
        primaryKey.append(cdUf);
        return primaryKey.toString();
    }

    public void populatePrimaryKeyFromRowKey() {
    	String[] rowKeyArray = rowKey.split(";");
    	int size = rowKeyArray.length;
		if (size == 15) {
			cdEmpresa = rowKeyArray[0];
			cdRepresentante = rowKeyArray[1];
			cdCliente = rowKeyArray[2];
			cdProduto = rowKeyArray[3];
			cdGrupoDescCli = rowKeyArray[4];
			cdGrupoDescProd = rowKeyArray[5];
			qtItem = ValueUtil.getIntegerValue(rowKeyArray[6]);
			cdTabelaPreco = rowKeyArray[7];
			cdCondicaoComercial = rowKeyArray[8];
			cdLocal = rowKeyArray[9];
			cdTipoPedido = rowKeyArray[10];
			cdCondicaoPagamento = rowKeyArray[11];
			cdLocalEstoque = rowKeyArray[12];
			cdTipoFrete = rowKeyArray[13];
			cdUf = rowKeyArray[14];
		}
    }
    
    public double getVlDescVlBaseItemPedido(double vlBaseItemPedido) {
		double vlItemPedidoMaxPermitido = 0;
		if (vlPctDescontoProduto > 0) {
			vlItemPedidoMaxPermitido = vlBaseItemPedido * (1 - (vlPctDescontoProduto / 100));
		} else if (vlDescontoProduto > 0) {
			vlItemPedidoMaxPermitido = vlBaseItemPedido - vlDescontoProduto;
		}
		return ValueUtil.round(vlItemPedidoMaxPermitido);
	}
    
    @Override
    public int getSortIntValue() {
    	return ValueUtil.getIntegerValue(qtItem);
    }

	@Override
	public String getCdDomain() {
		return StringUtil.getStringValue(nuPromocao);
	}

	@Override
	public String getDsDomain() {
		return dsDescPromocional;
	}
    
}
