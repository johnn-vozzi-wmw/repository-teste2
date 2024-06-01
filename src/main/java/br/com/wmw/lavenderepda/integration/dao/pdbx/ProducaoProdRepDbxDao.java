package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ProducaoProdRep;
import totalcross.sql.ResultSet;

public class ProducaoProdRepDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProducaoProdRep();
	}

    private static ProducaoProdRepDbxDao instance;
	

    public ProducaoProdRepDbxDao() {
        super(ProducaoProdRep.TABLE_NAME);
    }
    
    public static ProducaoProdRepDbxDao getInstance() {
        if (instance == null) {
            instance = new ProducaoProdRepDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ProducaoProdRep producaoProdRep = new ProducaoProdRep();
        producaoProdRep.rowKey = rs.getString("rowkey");
        producaoProdRep.cdEmpresa = rs.getString("cdEmpresa");
        producaoProdRep.cdRepresentante = rs.getString("cdRepresentante");
        producaoProdRep.cdProduto = rs.getString("cdProduto");
        producaoProdRep.dtInicial = rs.getDate("dtInicial");
        producaoProdRep.dtFinal = rs.getDate("dtFinal");
        producaoProdRep.qtdRateioProducao = ValueUtil.round(rs.getDouble("qtdRateioProducao"));
        producaoProdRep.nuCarimbo = rs.getInt("nuCarimbo");
        producaoProdRep.flTipoAlteracao = rs.getString("flTipoAlteracao");
        producaoProdRep.cdUsuario = rs.getString("cdUsuario");
        return producaoProdRep;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL,");
        sql.append(" QTDRATEIOPRODUCAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL,");
        sql.append(" QTDRATEIOPRODUCAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        ProducaoProdRep producaoProdRep = (ProducaoProdRep) domain;
        sql.append(Sql.getValue(producaoProdRep.cdEmpresa)).append(",");
        sql.append(Sql.getValue(producaoProdRep.cdRepresentante)).append(",");
        sql.append(Sql.getValue(producaoProdRep.cdProduto)).append(",");
        sql.append(Sql.getValue(producaoProdRep.dtInicial)).append(",");
        sql.append(Sql.getValue(producaoProdRep.dtFinal)).append(",");
        sql.append(Sql.getValue(producaoProdRep.qtdRateioProducao)).append(",");
        sql.append(Sql.getValue(producaoProdRep.nuCarimbo)).append(",");
        sql.append(Sql.getValue(producaoProdRep.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(producaoProdRep.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        ProducaoProdRep producaoProdRep = (ProducaoProdRep) domain;
        sql.append(" QTDRATEIOPRODUCAO = ").append(Sql.getValue(producaoProdRep.qtdRateioProducao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(producaoProdRep.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(producaoProdRep.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(producaoProdRep.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        ProducaoProdRep producaoProdRep = (ProducaoProdRep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", producaoProdRep.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", producaoProdRep.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", producaoProdRep.cdProduto);
		sqlWhereClause.addAndCondition("DTINICIAL = ", producaoProdRep.dtInicial);
		sqlWhereClause.addAndCondition("DTFINAL = ", producaoProdRep.dtFinal);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}