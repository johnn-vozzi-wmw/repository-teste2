package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DapMatricula;
import totalcross.sql.ResultSet;

public class DapMatriculaDbxDao extends CrudDbxDao {
	
	private static DapMatriculaDbxDao instance;
	
	public static DapMatriculaDbxDao getInstance() {
		if (instance == null) {
			instance = new DapMatriculaDbxDao();
		}
		return instance;
	}

	public DapMatriculaDbxDao() {
		super(DapMatricula.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		DapMatricula dapMatricula = new DapMatricula();
		dapMatricula.rowKey = rs.getString("rowKey");
		dapMatricula.cdEmpresa = rs.getString("cdEmpresa");
		dapMatricula.cdCliente = rs.getString("cdCliente");
		dapMatricula.cdSafra = rs.getString("cdSafra");
		dapMatricula.cdDapMatricula = rs.getString("cdDapMatricula");
		dapMatricula.dtEmissao = rs.getDate("dtEmissao");
		dapMatricula.dtValidade = rs.getDate("dtValidade");
		dapMatricula.dsCidade = rs.getString("dsCidade");
		dapMatricula.dsLocalidade = rs.getString("dsLocalidade");
		dapMatricula.cdUf = rs.getString("cdUf");
		dapMatricula.nuMaxSeqLaudo = rs.getInt("nuMaxSeqLaudo");
		dapMatricula.dsSafra = rs.getString("dsSafra");
		return dapMatricula;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowKey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDCLIENTE,");
		sql.append(" tb.CDSAFRA,");
		sql.append(" CDDAPMATRICULA,");
		sql.append(" DTEMISSAO,");
		sql.append(" DTVALIDADE,");
		sql.append(" DSCIDADE,");
		sql.append(" DSLOCALIDADE,");
		sql.append(" CDUF,");
		sql.append(" NUMAXSEQLAUDO,");
		sql.append(" saf.DSSAFRA");
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" JOIN TBLVPSAFRA saf ON tb.CDSAFRA = saf.CDSAFRA and tb.CDUSUARIO = saf.CDUSUARIO");
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DapMatricula();
	}

	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if ((domain != null) && !ValueUtil.isEmpty(domain.sortAtributte)) {
    		sql.append(" order by ");
    		String[] sortAtributtes = StringUtil.split(domain.sortAtributte, ',');
    		String[] sortAsc = StringUtil.split(domain.sortAsc, ',');
    		for (int i = 0; i < sortAtributtes.length; i++) {
    			sql.append(sortAtributtes[i]).append(ValueUtil.VALOR_SIM.equals(sortAsc[i]) ? " ASC" : " DESC");
    			if (!(i == (sortAtributtes.length - 1))) {
    				sql.append(" , ");
    			}
			}
    	}
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		DapMatricula dapMatricula = (DapMatricula) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(" CDEMPRESA ", dapMatricula.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(" CDCLIENTE ", dapMatricula.cdCliente);
		sql.append(sqlWhereClause.getSql());
	}

}
