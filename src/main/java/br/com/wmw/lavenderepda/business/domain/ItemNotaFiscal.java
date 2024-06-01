package br.com.wmw.lavenderepda.business.domain;


import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class ItemNotaFiscal extends BasePersonDomain {
	
	public static String TABLE_NAME = "TBLVPITEMNOTAFISCAL";
	
	public String cdEmpresa;
	public String nuNotaFiscal;
	public String cdSerie;
	public String cdProduto;
	public String nuPedido;
	public String flOrigemPedido;
	public String cdRepresentante;
	public String dsProduto;
	public int nuSequenciaItem;
	public String dsUnidade;
	public double qtItem;
	public Double vlUnitario;
	public Double vlTotalItem;
	
	public ItemNotaFiscal() {
		super(TABLE_NAME);
	}

	public ItemNotaFiscal(String cdEmpresa, String cdSerie, String nuNotaFiscal) {
		super(TABLE_NAME);
		this.cdEmpresa = cdEmpresa;
		this.cdSerie = cdSerie;
		this.nuNotaFiscal = nuNotaFiscal;
	}

	@Override
	public String getPrimaryKey() {
		StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(nuNotaFiscal);
    	strBuffer.append(";");
    	strBuffer.append(cdSerie);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
        return strBuffer.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ItemNotaFiscal) {
			ItemNotaFiscal notaFiscal = (ItemNotaFiscal) obj;
			return 
			ValueUtil.valueEquals(cdEmpresa, notaFiscal.cdEmpresa) &&
			ValueUtil.valueEquals(nuNotaFiscal, notaFiscal.nuNotaFiscal) &&
			ValueUtil.valueEquals(cdSerie, notaFiscal.cdSerie) && 
			ValueUtil.valueEquals(cdProduto, notaFiscal.cdProduto);
	    }
	    return false;
	}
	
	public String getQtItem() {
		return StringUtil.getStringValueToInterface(this.qtItem, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0
				: ValueUtil.doublePrecisionInterface);
	}

}
