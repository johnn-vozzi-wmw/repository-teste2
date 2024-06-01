package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ModoFaturamento;
import totalcross.sql.ResultSet;

public class ModoFaturamentoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ModoFaturamento();
	}

    private static ModoFaturamentoDbxDao instance;
	
	public ModoFaturamentoDbxDao() {
		super(ModoFaturamento.TABLE_NAME);
	}

    public static ModoFaturamentoDbxDao getInstance() {
        if (instance == null) {
            instance = new ModoFaturamentoDbxDao();
        }
        return instance;
    }
	
	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ModoFaturamento modoFaturamento = new ModoFaturamento();
		modoFaturamento.rowKey = rs.getString("rowkey");
		modoFaturamento.cdEmpresa = rs.getString("cdEmpresa");
		modoFaturamento.cdRepresentante = rs.getString("cdRepresentante");
		modoFaturamento.cdModoFaturamento = rs.getString("cdModoFaturamento");
		modoFaturamento.dsModoFaturamento = rs.getString("dsModoFaturamento");
		modoFaturamento.flExigeObservacao = rs.getString("flExigeObservacao");
		modoFaturamento.flTipoAlteracao = rs.getString("flTipoAlteracao");
		modoFaturamento.cdUsuario = rs.getString("cdUsuario");
		return modoFaturamento;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDMODOFATURAMENTO,");
        sql.append(" tb.DSMODOFATURAMENTO,");
        sql.append(" tb.FLEXIGEOBSERVACAO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.CDUSUARIO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ModoFaturamento modoFaturamento = (ModoFaturamento) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", modoFaturamento.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", modoFaturamento.cdRepresentante);
		sqlWhereClause.addAndCondition("CDMODOFATURAMENTO = ", modoFaturamento.cdModoFaturamento);
		sql.append(sqlWhereClause.getSql());
	}

}
