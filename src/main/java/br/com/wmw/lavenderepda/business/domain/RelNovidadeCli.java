package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.TipoNovidadeService;
import totalcross.util.Date;

public class RelNovidadeCli extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPRELNOVIDADECLI";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdTipoNovidade;
    public String nmRazaoSocial;
    public String dsNovidadeCliente;
    public Date dtEmissaoRelatorio;
    public Date dtAlteracao;
    public String hrAlteracao;
    
	// Não Persistente
	public String dsFiltro;
    public int qtRegistrosTipoNovidade;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof RelNovidadeCli) {
            RelNovidadeCli relnovidadecli = (RelNovidadeCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, relnovidadecli.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, relnovidadecli.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, relnovidadecli.cdCliente) && 
                ValueUtil.valueEquals(cdTipoNovidade, relnovidadecli.cdTipoNovidade);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdTipoNovidade);
        return primaryKey.toString();
    }
    
    @Override
    public int getSortIntValue() {
    	if (RelNovidadeProd.SORT_COLUMN_CDPRODUTO.equals(RelNovidadeProd.sortAttr)) {
    		return ValueUtil.getIntegerValue(cdCliente);
    	}
    	if (RelNovidadeProd.SORT_COLUMN_DTEMISSAORELATORIO.equals(RelNovidadeProd.sortAttr) && ValueUtil.isNotEmpty(dtEmissaoRelatorio)) {
    		return dtEmissaoRelatorio.getDateInt();
    	} 
    	return super.getSortIntValue();
    }
    
    @Override
    public String getSortStringValue() {
    	if (RelNovidadeProd.SORT_COLUMN_DSPRODUTO.equals(RelNovidadeProd.sortAttr)) {
    		return nmRazaoSocial;
    	}
    	if (RelNovidadeProd.SORT_COLUMN_DSNOVIDADEPRODUTO.equals(RelNovidadeProd.sortAttr)) {
    		try {
				return TipoNovidadeService.getInstance().getDsTipoNovidade(cdTipoNovidade);
			} catch (SQLException ex) {
				return dsNovidadeCliente;
			}
    	}
    	return super.getSortStringValue();
    }

}