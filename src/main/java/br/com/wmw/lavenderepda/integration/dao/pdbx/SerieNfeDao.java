package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.SerieNfe;
import totalcross.sql.ResultSet;

public class SerieNfeDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SerieNfe();
	}

	private static SerieNfeDao instance;
	
	public static SerieNfeDao getInstance() {
		if (instance == null) {
			instance = new SerieNfeDao();
		}
		return instance;
	}

	public SerieNfeDao() {
		super(SerieNfe.TABLE_NAME);
	}

	@Override
	protected void addInsertColumns(StringBuffer arg0) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,")
		.append(" CDEMPRESA,")
		.append(" CDREPRESENTANTE,")
		.append(" NUSERIENFE,")
		.append(" NUPROXIMONUMERO,")
		.append(" NUPROXIMONUMEROORG,")
		.append(" CDTIPONOTA,")
		.append(" CDUSUARIOALTERACAO");
	}

	@Override
	protected void addUpdateValues(BaseDomain arg0, StringBuffer arg1) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		SerieNfe serieNfe = (SerieNfe)domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", serieNfe.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", serieNfe.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("NUSERIENFE", serieNfe.nuSerieNfe);
		sqlWhereClause.addAndConditionEquals("CDTIPONOTA", serieNfe.cdTipoNota);
		if (ValueUtil.isNotEmpty(serieNfe.flExclusivaCont)) {
			sqlWhereClause.addAndConditionEquals("FLEXCLUSIVACONT", serieNfe.flExclusivaCont);
		}
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		SerieNfe serieNfe = new SerieNfe();
		serieNfe.cdEmpresa = rs.getString("cdEmpresa");
		serieNfe.cdRepresentante = rs.getString("cdRepresentante");
		serieNfe.nuSerieNfe = rs.getInt("nuSerieNfe");
		serieNfe.nuProximoNumero = rs.getInt("nuProximoNumero");
		serieNfe.nuProximoNumeroOrg = rs.getInt("nuProximoNumeroOrg");
		serieNfe.cdUsuarioAlteracao = rs.getString("cdUsuarioAlteracao");
		serieNfe.cdTipoNota = rs.getString("cdTipoNota");
		return serieNfe;
	}

}
