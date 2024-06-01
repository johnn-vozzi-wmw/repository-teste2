package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaGrupoService;

public class SugestaoVendaGrupo extends BaseDomain {

	public static String TABLE_NAME = "TBLVPSUGESTAOVENDAGRUPO";
    public static final String SORT_DSGRUPO = "DSGRUPO";

    public String cdEmpresa;
    public String cdSugestaoVenda;
    public String cdGrupoProduto1;
    public String cdGrupoProduto2;
    public String cdGrupoProduto3;
    public int qtMixProdutosVenda;
    public int qtUnidadesVenda;

    //Não Persistente
    private String dsSugestaoVendaGrupo;
    public int qtMixVendida;
    public int qtVendida;
    public String[] cdsSugestao;
    public static String sortAttr;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof SugestaoVendaGrupo) {
            SugestaoVendaGrupo sugestaoVendaGrupo = (SugestaoVendaGrupo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, sugestaoVendaGrupo.cdEmpresa) &&
                ValueUtil.valueEquals(cdSugestaoVenda, sugestaoVendaGrupo.cdSugestaoVenda) &&
                ValueUtil.valueEquals(cdGrupoProduto1, sugestaoVendaGrupo.cdGrupoProduto1) &&
                ValueUtil.valueEquals(cdGrupoProduto2, sugestaoVendaGrupo.cdGrupoProduto2) &&
                ValueUtil.valueEquals(cdGrupoProduto3, sugestaoVendaGrupo.cdGrupoProduto3);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdSugestaoVenda);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto1);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto2);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto3);
        return primaryKey.toString();
    }

    public String getDsSugestaoVendaGrupo() throws SQLException {
    	if (dsSugestaoVendaGrupo == null) {
    		dsSugestaoVendaGrupo = SugestaoVendaGrupoService.getInstance().getDsSugestaoVenda(this);
    	}
    	return dsSugestaoVendaGrupo;
    }
    
    @Override
    public String toString() {
    	if (SORT_DSGRUPO.equals(sortAttr)) {
    		try {
    			dsSugestaoVendaGrupo = getDsSugestaoVendaGrupo();
    		} catch (Throwable t) {ExceptionUtil.handle(t);}
			return dsSugestaoVendaGrupo != null ? dsSugestaoVendaGrupo.toUpperCase() : "";
		}
    	return super.toString();
    }

}