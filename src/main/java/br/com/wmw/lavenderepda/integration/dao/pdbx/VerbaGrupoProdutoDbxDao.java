package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoProduto;
import totalcross.sql.ResultSet;

public class VerbaGrupoProdutoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaGrupoProduto();
	}

    private static VerbaGrupoProdutoDbxDao instance;

    public VerbaGrupoProdutoDbxDao() {
        super(VerbaGrupoProduto.TABLE_NAME); 
    }
    
    public static VerbaGrupoProdutoDbxDao getInstance() {
        if (instance == null) {
            instance = new VerbaGrupoProdutoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        VerbaGrupoProduto verbaGrupoProduto = new VerbaGrupoProduto();
        verbaGrupoProduto.rowKey = rs.getString("rowkey");
        verbaGrupoProduto.cdEmpresa = rs.getString("cdEmpresa");
        verbaGrupoProduto.cdRepresentante = rs.getString("cdRepresentante");
        verbaGrupoProduto.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        verbaGrupoProduto.cdTabelaPreco = rs.getString("cdTabelaPreco");
        verbaGrupoProduto.vlPctVerbaPositiva = ValueUtil.round(rs.getDouble("vlPctVerbaPositiva"));
        verbaGrupoProduto.cdUsuario = rs.getString("cdUsuario");
        verbaGrupoProduto.nuCarimbo = rs.getInt("nuCarimbo");
        verbaGrupoProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return verbaGrupoProduto;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" VLPCTVERBAPOSITIVA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" VLPCTVERBAPOSITIVA,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        VerbaGrupoProduto verbaGrupoProduto = (VerbaGrupoProduto) domain;
        sql.append(Sql.getValue(verbaGrupoProduto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(verbaGrupoProduto.cdRepresentante)).append(",");
        sql.append(Sql.getValue(verbaGrupoProduto.cdGrupoProduto1)).append(",");
        sql.append(Sql.getValue(verbaGrupoProduto.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(verbaGrupoProduto.vlPctVerbaPositiva)).append(",");
        sql.append(Sql.getValue(verbaGrupoProduto.cdUsuario)).append(",");
        sql.append(Sql.getValue(verbaGrupoProduto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(verbaGrupoProduto.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        VerbaGrupoProduto verbaGrupoProduto = (VerbaGrupoProduto) domain;
        sql.append(" CDTABELAPRECO = ").append(Sql.getValue(verbaGrupoProduto.cdTabelaPreco)).append(",");
        sql.append(" VLPCTVERBAPOSITIVA = ").append(Sql.getValue(verbaGrupoProduto.vlPctVerbaPositiva)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(verbaGrupoProduto.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(verbaGrupoProduto.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(verbaGrupoProduto.flTipoAlteracao)).append(",");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        VerbaGrupoProduto verbaGrupoProduto = (VerbaGrupoProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", verbaGrupoProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", verbaGrupoProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", verbaGrupoProduto.cdGrupoProduto1);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}