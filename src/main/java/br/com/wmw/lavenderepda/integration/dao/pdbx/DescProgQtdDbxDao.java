package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescProgQtd;
import totalcross.sql.ResultSet;

public class DescProgQtdDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescProgQtd();
	}

    private static DescProgQtdDbxDao instance;

    public DescProgQtdDbxDao() {
        super(DescProgQtd.TABLE_NAME);
    }

    public static DescProgQtdDbxDao getInstance() {
        if (instance == null) {
            instance = new DescProgQtdDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        DescProgQtd descProgQtd = new DescProgQtd();
        descProgQtd.rowKey = rs.getString("rowkey");
        descProgQtd.cdEmpresa = rs.getString("cdEmpresa");
        descProgQtd.cdRepresentante = rs.getString("cdRepresentante");
        descProgQtd.cdUnidade = rs.getString("cdUnidade");
        descProgQtd.qtUnidade = rs.getInt("qtUnidade");
        descProgQtd.vlPctDesconto = ValueUtil.round(rs.getDouble("vlPctDesconto"));
        descProgQtd.nuCarimbo = rs.getInt("nuCarimbo");
        descProgQtd.flTipoAlteracao = rs.getString("flTipoAlteracao");
        descProgQtd.cdUsuario = rs.getString("cdUsuario");
        return descProgQtd;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDUNIDADE,");
        sql.append(" QTUNIDADE,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDUNIDADE,");
        sql.append(" QTUNIDADE,");
        sql.append(" VLPCTDESCONTO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescProgQtd descProgQtd = (DescProgQtd) domain;
        sql.append(Sql.getValue(descProgQtd.cdEmpresa)).append(",");
        sql.append(Sql.getValue(descProgQtd.cdRepresentante)).append(",");
        sql.append(Sql.getValue(descProgQtd.cdUnidade)).append(",");
        sql.append(Sql.getValue(descProgQtd.qtUnidade)).append(",");
        sql.append(Sql.getValue(descProgQtd.vlPctDesconto)).append(",");
        sql.append(Sql.getValue(descProgQtd.nuCarimbo)).append(",");
        sql.append(Sql.getValue(descProgQtd.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(descProgQtd.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescProgQtd descProgQtd = (DescProgQtd) domain;
        sql.append(" VLPCTDESCONTO = ").append(Sql.getValue(descProgQtd.vlPctDesconto)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(descProgQtd.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(descProgQtd.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(descProgQtd.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescProgQtd descProgQtd = (DescProgQtd) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descProgQtd.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", descProgQtd.cdRepresentante);
		sqlWhereClause.addAndCondition("CDUNIDADE = ", descProgQtd.cdUnidade);
		sqlWhereClause.addAndCondition("QTUNIDADE <= ", descProgQtd.qtUnidade);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}