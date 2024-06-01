package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.TipoNovidadeService;
import totalcross.util.Date;

public class RelNovidadeNovoCli extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPRELNOVIDADENOVOCLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdNovoCliente;
    public String cdTipoNovidade;
    public String nmRazaoSocial;
    public String nuCnpj;
    public String dsMensagem;
    public Date dtGeracao;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    //não persite
    public int qtRegistrosTipoNovidade;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RelNovidadeNovoCli) {
            RelNovidadeNovoCli relnovidadenovocli = (RelNovidadeNovoCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, relnovidadenovocli.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, relnovidadenovocli.cdRepresentante) && 
                ValueUtil.valueEquals(cdNovoCliente, relnovidadenovocli.cdNovoCliente) && 
                ValueUtil.valueEquals(cdTipoNovidade, relnovidadenovocli.cdTipoNovidade);
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
        primaryKey.append(cdNovoCliente);
        primaryKey.append(";");
        primaryKey.append(cdTipoNovidade);
        return primaryKey.toString();
    }

    @Override
    public int getSortIntValue() {
        if (RelNovidadeProd.SORT_COLUMN_CDPRODUTO.equals(RelNovidadeProd.sortAttr)) {
            return ValueUtil.getIntegerValue(cdNovoCliente);
        }
        if (RelNovidadeProd.SORT_COLUMN_DTEMISSAORELATORIO.equals(RelNovidadeProd.sortAttr) && ValueUtil.isNotEmpty(dtGeracao)) {
            return dtGeracao.getDateInt();
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
                return dsMensagem;
            }
        }
        return super.getSortStringValue();
    }


}