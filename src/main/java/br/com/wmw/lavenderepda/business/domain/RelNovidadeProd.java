package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.TipoNovidadeService;
import totalcross.util.Date;

public class RelNovidadeProd extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPRELNOVIDADEPROD";
    
    public static final String SORT_COLUMN_CDPRODUTO = "CDPRODUTO";
	public static final String SORT_COLUMN_DSPRODUTO = "DSPRODUTO";
	public static final String SORT_COLUMN_DSNOVIDADEPRODUTO = "DSNOVIDADEPRODUTO";
	public static final String SORT_COLUMN_DTEMISSAORELATORIO = "DTEMISSAORELATORIO";
	public static final String SORT_COLUMN_DSMARCA = "DSMARCA";

	public static final String VALOR_PADRAO = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdTipoNovidade;
    public String cdItemGrade1;
    public String cdItemGrade2;
    public String cdItemGrade3;
    public String cdTabelaPreco;
    public String cdUf;
    public String cdUnidade;
	public int qtItem;
	public int cdPrazoPagtoPreco;
	public String cdLocalEstoque;
	public String flOrigemEstoque;
    public String dsProduto;
    public String dsNovidadeProduto;
    public Date dtEmissaoRelatorio;
    public double vlPrecoAntigo;
    public static String sortAttr;

    //Não Persistente
    public String cdProdutoExato;
    public int qtRegistrosTipoNovidade;
    public String dsReferenciaProduto;
    public String dsMarcaProduto;
    public int indexItem;
    public boolean filterGrade;
    
    public TabelaPreco tabelaPreco;
    public LocalEstoque localEstoque;
    
    public ProdutoGrade produtoGrade;
    public TipoItemGrade tipoItemGrade1;
    public ItemGrade itemGrade1;
    public TipoItemGrade tipoItemGrade2;
    public ItemGrade itemGrade2;
    public TipoItemGrade tipoItemGrade3;
    public ItemGrade itemGrade3;

	public boolean addWhereTabPreco;
    
    //Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RelNovidadeProd)) return false;
        RelNovidadeProd relNovidadeProduto = (RelNovidadeProd) obj;
        return ValueUtil.valueEquals(cdEmpresa, relNovidadeProduto.cdEmpresa) &&
        	   ValueUtil.valueEquals(cdRepresentante, relNovidadeProduto.cdRepresentante) &&
        	   ValueUtil.valueEquals(cdProduto, relNovidadeProduto.cdProduto) &&
        	   ValueUtil.valueEquals(cdTipoNovidade, relNovidadeProduto.cdTipoNovidade) &&
        	   ValueUtil.valueEquals(cdItemGrade1, relNovidadeProduto.cdItemGrade1) &&
        	   ValueUtil.valueEquals(cdItemGrade2, relNovidadeProduto.cdItemGrade2) &&
        	   ValueUtil.valueEquals(cdItemGrade3, relNovidadeProduto.cdItemGrade3) &&
        	   ValueUtil.valueEquals(cdTabelaPreco, relNovidadeProduto.cdTabelaPreco) &&
        	   ValueUtil.valueEquals(cdUf, relNovidadeProduto.cdUf) &&
        	   ValueUtil.valueEquals(cdUnidade, relNovidadeProduto.cdUnidade) &&
        	   ValueUtil.valueEquals(qtItem, relNovidadeProduto.qtItem) &&
        	   ValueUtil.valueEquals(cdPrazoPagtoPreco, relNovidadeProduto.cdPrazoPagtoPreco) &&
        	   ValueUtil.valueEquals(cdLocalEstoque, relNovidadeProduto.cdLocalEstoque) &&
        	   ValueUtil.valueEquals(flOrigemEstoque, relNovidadeProduto.flOrigemEstoque);
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdTipoNovidade);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade1);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade2);
    	strBuffer.append(";");
    	strBuffer.append(cdItemGrade3);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
    	strBuffer.append(";");
    	strBuffer.append(cdUf);
    	strBuffer.append(";");
    	strBuffer.append(cdUnidade);
    	strBuffer.append(";");
    	strBuffer.append(qtItem);
    	strBuffer.append(";");
    	strBuffer.append(cdPrazoPagtoPreco);
    	strBuffer.append(";");
    	strBuffer.append(cdLocalEstoque);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemEstoque);
        return strBuffer.toString();
    }
    
    @Override
    public int getSortIntValue() {
    	if (SORT_COLUMN_CDPRODUTO.equals(sortAttr)) {
    		return ValueUtil.getIntegerValue(cdProduto);
    	}
    	if (SORT_COLUMN_DTEMISSAORELATORIO.equals(sortAttr) && ValueUtil.isNotEmpty(dtEmissaoRelatorio)) {
    		return dtEmissaoRelatorio.getDateInt();
    	} 
    	return super.getSortIntValue();
    }
    
    @Override
    public String getSortStringValue() {
    	if (SORT_COLUMN_DSPRODUTO.equals(sortAttr) && ValueUtil.isNotEmpty(cdProduto)) {
    		if (cdProduto.equalsIgnoreCase(dsProduto)) {
    			try {
					return ProdutoService.getInstance().getDsProduto(cdProduto);
				} catch (SQLException ex) {
					return dsProduto;
				}
    		}
    		return ValueUtil.isNotEmpty(dsProduto) ? dsProduto : "";
    	}
    	if (SORT_COLUMN_DSNOVIDADEPRODUTO.equals(sortAttr)) {
    		try {
				return TipoNovidadeService.getInstance().getDsTipoNovidade(cdTipoNovidade);
			} catch (SQLException ex) {
				return dsNovidadeProduto;
			}
    	}
    	if (SORT_COLUMN_DSMARCA.equals(sortAttr)) {
    		if (LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos()) {
    			return dsMarcaProduto;
    		} else {
    			return dsNovidadeProduto;
    		}
		}
    	return super.getSortStringValue();
    }
    
    public boolean possuiTabelaPreco() {
    	return !ValueUtil.valueEquals(cdTabelaPreco, RelNovidadeProd.VALOR_PADRAO);
    }
    
    public boolean possuiLocalEstoque() {
    	return !ValueUtil.valueEquals(cdLocalEstoque, RelNovidadeProd.VALOR_PADRAO);
    }
    
    public boolean possuiGrades() {
    	return possuiGrade1() || possuiGrade2() || possuiGrade3();
    }
    
    public boolean possuiGrade1() {
    	return !ValueUtil.valueEquals(cdItemGrade1, RelNovidadeProd.VALOR_PADRAO);
    }
    
    public boolean possuiGrade2() {
    	return !ValueUtil.valueEquals(cdItemGrade2, RelNovidadeProd.VALOR_PADRAO);
    }
    
    public boolean possuiGrade3() {
    	return !ValueUtil.valueEquals(cdItemGrade3, RelNovidadeProd.VALOR_PADRAO);
    }
    
    public boolean possuiUF() {
    	return !ValueUtil.valueEquals(cdUf, RelNovidadeProd.VALOR_PADRAO);
    }
    
    public boolean possuiUnidade() {
    	return !ValueUtil.valueEquals(cdUnidade, RelNovidadeProd.VALOR_PADRAO);
    }
    
    public boolean possuiQuantidade() {
    	return qtItem > 0;
    }  
    
    public boolean possuiPrazoPagto() {
    	return cdPrazoPagtoPreco > 0;
    }
    
    public boolean possuiOrigemEstoque() {
    	return !ValueUtil.valueEquals(flOrigemEstoque, RelNovidadeProd.VALOR_PADRAO);
    }

	public boolean isNovidadeEntradaEstoque() {
		return ValueUtil.valueEquals(TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE, cdTipoNovidade);
	}

	public boolean mostraValorDaUnidadePrecoPorEmbalagemNovidade() {
		return LavenderePdaConfig.isMostraValorDaUnidadePrecoPorEmbalagem() && ((ValueUtil.valueEquals(TipoNovidade.TIPONOVIDADEPRODUTO_AUMENTO_PRECO, cdTipoNovidade)) || ValueUtil.valueEquals(TipoNovidade.TIPONOVIDADEPRODUTO_QUEDA_PRECO, cdTipoNovidade));
		
	}
}