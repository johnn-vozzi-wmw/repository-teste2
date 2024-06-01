package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Vector;

public class ItemKit extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPITEMKIT";

	public static final String CD_TABELA_PRECO_ZERO = "0";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdKit;
	public String cdProduto;
	public String cdTabelaPreco;
	public double qtMinItem;
	public double qtMaxItem;
	public double vlPctDesconto;
	public double vlUnitarioKit;
	public int qtItemKit;
	public String flAgrupadorSimilaridade;
	public String cdUnidade = "0";
	public String flBonificado;

	//Não Pesistente
	public String dsProduto;
	public String cdTabelaPrecoFilter;
	public double nuConversaoUnidadesMedida;
	public boolean semEstoque;
	public Vector similaresList;
	public String cdAgrupadorSimilaridade;
	public double qtItemFisicoItemPedido;
	public double qtItemFaturamentoItemPedido;
	public String nuPedido;
	public Kit kit;
	public String cdCliente;
	public double vlUnitario;

	//Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemKit) {
			ItemKit itemKit = (ItemKit) obj;
			return
			ValueUtil.valueEquals(cdEmpresa, itemKit.cdEmpresa) &&
			ValueUtil.valueEquals(cdRepresentante, itemKit.cdRepresentante) &&
			ValueUtil.valueEquals(cdKit, itemKit.cdKit) &&
			ValueUtil.valueEquals(cdProduto, itemKit.cdProduto) &&
			ValueUtil.valueEquals(cdTabelaPreco, itemKit.cdTabelaPreco) &&
			ValueUtil.valueEquals(flBonificado, itemKit.flBonificado);
		}
		return false;
	}

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdKit);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
	    strBuffer.append(cdTabelaPreco);
	    strBuffer.append(";");
	    strBuffer.append(cdUnidade);
	    strBuffer.append(";");
	    strBuffer.append(flBonificado);
        return strBuffer.toString();
    }

	@Override
	public String getCdDomain() {
		return cdProduto;
	}

	@Override
	public String getDsDomain() {
		return dsProduto;
	}

	public boolean hasSimilares() {
		return ValueUtil.isNotEmpty(similaresList);
	}

	public boolean hasSimilaresPreenchidos() {
		if (hasSimilares()) {
			int qt = 0 ;
			int size = similaresList.size();
			for (int i = 0; i < size; i++) {
				qt += ((Produto) similaresList.items[i]).qtItemPedido;
			}
			return qt == qtItemKit;
		}
		return false;
	}
	
	public boolean isBuscandoSimilar() {
		return ValueUtil.isNotEmpty(cdAgrupadorSimilaridade);
	}
	
	public boolean isBonificado() {
		return ValueUtil.getBooleanValue(flBonificado);
	}
}