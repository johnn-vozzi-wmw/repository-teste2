package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.dto.ItemNfeDTO;
import br.com.wmw.lavenderepda.business.service.ProdutoService;

public class ItemNfe extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPITEMNFE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String cdClassificFiscal;
    public String cdUnidade;
    public double qtItemFisico;
    public double vlBaseItemTabelaPreco;
    public double vlItemPedido;
    public double vlTotalIcmsItem;
    public double vlPctIcms;
    public double vlTotalItemPedido;
    public double vlTotalStItem;
    public double vlTotalBaseIcmsItem;
    public double vlTotalBaseStItem;
    public double vlDespesaAcessoria;
    public String flCigarro;
    public String dsNcmProduto;
    public String nuCfopProduto;
    public double qtPeso;
    public double vlPctPis;
    public double vlPctCofins;
    public String dsCestProduto;
    public double qtMultiploEmbalagem;
    public String cdOrigemMercadoria;
    public double vlPctReducaoBaseIcms;
    public double vlPctReducaoIcms;
    public double vlPctFecopRecolher;
    public String cdBeneficioFiscal;
    
    //Não Persistente
    public String dsProduto;
    private Produto produto;
	public boolean filtraPorNaoEnviadoServidor;
	public static String sortAttr;
	public boolean comJoinExampleRS = true;
    
    public ItemNfe() {
	}
    
    public ItemNfe(ItemNfeDTO itemNfeDTO) {
    	this();
    	try {
    		FieldMapper.copy(itemNfeDTO, this);
    	} catch (Throwable e) {
    		e.printStackTrace();
    	}
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemNfe) {
            ItemNfe itemNfe = (ItemNfe) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemNfe.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, itemNfe.cdRepresentante) && 
                ValueUtil.valueEquals(nuPedido, itemNfe.nuPedido) && 
                ValueUtil.valueEquals(flOrigemPedido, itemNfe.flOrigemPedido) && 
                ValueUtil.valueEquals(cdProduto, itemNfe.cdProduto) && 
                ValueUtil.valueEquals(flTipoItemPedido, itemNfe.flTipoItemPedido) && 
                ValueUtil.valueEquals(nuSeqProduto, itemNfe.nuSeqProduto) && 
                ValueUtil.valueEquals(cdItemGrade1, itemNfe.cdItemGrade1) && 
                ValueUtil.valueEquals(cdItemGrade2, itemNfe.cdItemGrade2) && 
                ValueUtil.valueEquals(cdItemGrade3, itemNfe.cdItemGrade3);
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
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(flTipoItemPedido);
        primaryKey.append(";");
        primaryKey.append(nuSeqProduto);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade1);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade2);
        primaryKey.append(";");
        primaryKey.append(cdItemGrade3);
        return primaryKey.toString();
    }
    
    public Produto getProduto() throws SQLException {
    	if ((!ValueUtil.isEmpty(cdProduto)) && ((produto == null) || (!cdProduto.equals(produto.cdProduto)))) {
    		produto = ProdutoService.getInstance().getProduto(cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto);
    	}
    	return produto;
    }
    
    public String getDsProduto() throws SQLException {
    	if ((!ValueUtil.isEmpty(cdProduto)) && ((produto == null) || (!cdProduto.equals(produto.cdProduto)))) {
    		produto = ProdutoService.getInstance().getProduto(cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto);
    	}
    	return ProdutoService.getInstance().getDsProduto(this.produto);
    }
    
    public String getDsProdutoSemCodigo() throws SQLException {
    	if ((!ValueUtil.isEmpty(cdProduto)) && ((produto == null) || (!cdProduto.equals(produto.cdProduto)))) {
    		produto = ProdutoService.getInstance().getProduto(cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto);
    	}
    	return produto.dsProduto; 	
    }

    public String getDsProdutoWithKey() throws SQLException {
    	return ProdutoService.getInstance().getDescriptionWithId(this.produto, this.cdProduto);
    }
    
	public String getDsItemNfe() throws SQLException {
		return ProdutoService.getInstance().getDescricaoProdutoReferenciaConsideraCodigo(this.getProduto());
	}
    
    public boolean isCigarro() {
    	return ValueUtil.getBooleanValue(flCigarro);
    }
    
    public String getSortStringValue() {
    	if ("DSPRODUTO".toUpperCase().equals(sortAttr)) {
			try {
				return getDsProdutoSemCodigo();
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
				return super.getSortStringValue();
			}
		}
		return super.getSortStringValue();
    }
	
    public int getSortIntValue() {
    	if (sortAttr.equals("NUSEQPRODUTO")) {
    		return ValueUtil.getIntegerValue(nuSeqProduto);
    	}
    	if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido() && produto != null) {
			return produto.getSortIntValue();
    	}
    	return ValueUtil.getIntegerValue(cdProduto);
    }

}