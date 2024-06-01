package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.RentabilidadeSaldo;
import totalcross.sql.ResultSet;

public class RentabilidadeSaldoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RentabilidadeSaldo();
	}

    private static RentabilidadeSaldoDbxDao instance;

    public RentabilidadeSaldoDbxDao() {
        super(RentabilidadeSaldo.TABLE_NAME);
    }

    public static RentabilidadeSaldoDbxDao getInstance() {
        if (instance == null) {
            instance = new RentabilidadeSaldoDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        RentabilidadeSaldo rentabilidadeSaldo = new RentabilidadeSaldo();
        rentabilidadeSaldo.rowKey = rs.getString("rowkey");
        rentabilidadeSaldo.cdEmpresa = rs.getString("cdEmpresa");
        rentabilidadeSaldo.cdRepresentante = rs.getString("cdRepresentante");
        rentabilidadeSaldo.flOrigemSaldo = rs.getString("flOrigemSaldo");
        rentabilidadeSaldo.vlTotalRentabilidade = ValueUtil.round(rs.getDouble("vlTotalRentabilidade"));
        rentabilidadeSaldo.vlTotalItens = ValueUtil.round(rs.getDouble("vlTotalItens"));
        rentabilidadeSaldo.nuCarimbo = rs.getInt("nuCarimbo");
        rentabilidadeSaldo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        rentabilidadeSaldo.cdUsuario = rs.getString("cdUsuario");
        return rentabilidadeSaldo;
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
        sql.append(" FLORIGEMSALDO,");
        sql.append(" VLTOTALRENTABILIDADE,");
        sql.append(" VLTOTALITENS,");
        sql.append(" NUCARIMBO,");
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
        sql.append(" FLORIGEMSALDO,");
        sql.append(" VLTOTALRENTABILIDADE,");
        sql.append(" VLTOTALITENS,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) domain;
        sql.append(Sql.getValue(rentabilidadeSaldo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(rentabilidadeSaldo.cdRepresentante)).append(",");
        sql.append(Sql.getValue(rentabilidadeSaldo.flOrigemSaldo)).append(",");
        sql.append(Sql.getValue(rentabilidadeSaldo.vlTotalRentabilidade)).append(",");
        sql.append(Sql.getValue(rentabilidadeSaldo.vlTotalItens)).append(",");
        sql.append(Sql.getValue(rentabilidadeSaldo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(rentabilidadeSaldo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(rentabilidadeSaldo.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) domain;
        sql.append(" VLTOTALRENTABILIDADE = ").append(Sql.getValue(rentabilidadeSaldo.vlTotalRentabilidade)).append(",");
        sql.append(" VLTOTALITENS = ").append(Sql.getValue(rentabilidadeSaldo.vlTotalItens));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", rentabilidadeSaldo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", rentabilidadeSaldo.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMSALDO = ", rentabilidadeSaldo.flOrigemSaldo);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}