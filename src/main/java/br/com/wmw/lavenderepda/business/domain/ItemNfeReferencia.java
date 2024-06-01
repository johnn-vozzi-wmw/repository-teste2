package br.com.wmw.lavenderepda.business.domain;


import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.dto.ItemNfeReferenciaDTO;

public class ItemNfeReferencia extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPITEMNFEREFERENCIA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public String cdTributacaoConfig;
    public String cdReferencia;
    public String dsReferencia;
    public String cdCfop;
    public String cdClassificFiscal;
    public String cdUnidade;
    public double qtItemFisico;
    public double vlBaseItemTabelaPreco;
    public double vlItemPedido;
    public double vlTotalIcmsItem;
    public double vlPctIcms;
    public double vlTotalItemPedido;
    //Não Persistente
	public static String sortAttr;
    
    public ItemNfeReferencia() {}
    
    public ItemNfeReferencia(ItemNfeReferenciaDTO itemReferenciaDto) {
    	try {
    		FieldMapper.copy(itemReferenciaDto, this);
    	} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
	//Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemNfeReferencia) {
            ItemNfeReferencia itemNfeReferencia = (ItemNfeReferencia) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemNfeReferencia.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, itemNfeReferencia.cdRepresentante) && 
                ValueUtil.valueEquals(nuPedido, itemNfeReferencia.nuPedido) && 
                ValueUtil.valueEquals(flOrigemPedido, itemNfeReferencia.flOrigemPedido) && 
                ValueUtil.valueEquals(flTipoItemPedido, itemNfeReferencia.flTipoItemPedido) && 
                ValueUtil.valueEquals(nuSeqProduto, itemNfeReferencia.nuSeqProduto) && 
                ValueUtil.valueEquals(cdTributacaoConfig, itemNfeReferencia.cdTributacaoConfig) &&
                ValueUtil.valueEquals(cdReferencia, itemNfeReferencia.cdReferencia);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(flTipoItemPedido);
        primaryKey.append(";");
        primaryKey.append(nuSeqProduto);
        primaryKey.append(";");
        primaryKey.append(cdTributacaoConfig);
        primaryKey.append(";");
        primaryKey.append(cdReferencia);
        return primaryKey.toString();
    }
    
    
    public String getSortStringValue() {
    	if ("DSREFERENCIA".toUpperCase().equals(sortAttr)) {
				return dsReferencia;
		} 
		return super.getSortStringValue();
    }
	
	public int getSortIntValue() {
    	if (sortAttr.equals("NUSEQPRODUTO")) {
    		return ValueUtil.getIntegerValue(nuSeqProduto);
    	}
    	return ValueUtil.getIntegerValue(cdReferencia);
    }
}