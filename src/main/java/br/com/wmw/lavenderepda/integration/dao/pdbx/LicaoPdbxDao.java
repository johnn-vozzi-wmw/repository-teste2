package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Licao;
import totalcross.sql.ResultSet;
import totalcross.sql.sqlite4j.SQLite4JPreparedStatement;
import totalcross.util.Vector;

public class LicaoPdbxDao extends LavendereCrudPersonDbxDao {

	private static LicaoPdbxDao instance;
	
	public LicaoPdbxDao() {
		super(Licao.TABLE_NAME);
		// TODO Auto-generated constructor stub
	}
	
	public static LicaoPdbxDao getInstance() {
		if(instance == null) {
			instance = new LicaoPdbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		Licao licao = new Licao();
		licao.rowKey = rs.getString(1);
		licao.cdLicao = rs.getString(2);
		licao.titulo = rs.getString(3);
		licao.cdLicaoDependente = rs.getString(4);
		licao.conteudo = rs.getString(5);
		return licao;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		// TODO Auto-generated method stub
		sql.append(" ROWKEY");
		sql.append(", CDLICAO");
		sql.append(", TITULO");
		sql.append(", CDLICAODEPENDENTE");
		sql.append(", CONTEUDO");
	}


	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		// TODO Auto-generated method stub
		Licao licao = (Licao) domain;
		sql.append(" TITULO = ").append(Sql.getValue(licao.titulo));
		sql.append(", CDLICAODEPENDENTE = ").append(Sql.getValue(licao.cdLicaoDependente));
		sql.append(", CONTEUDO = ").append(Sql.getValue(licao.conteudo));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		// TODO Auto-generated method stub
		Licao licao = (Licao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndLikeCondition("CDLICAO", licao.cdLicao);
		sqlWhereClause.addOrLikeCondition("TITULO", licao.titulo);
		sqlWhereClause.addAndConditionEquals("CDLICAODEPENDENTE", licao.cdLicaoDependente);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
		// TODO Auto-generated method stub
		Licao licao = (Licao) domain;
		if("ROWKEY".equalsIgnoreCase(columnName)) {
			return Sql.getValue(licao.getRowKey());
		}
		if("CDLICAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(licao.cdLicao);
		}	
		if("TITULO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(licao.titulo);
		}
		if("CDLICAODEPENDENTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(licao.cdLicaoDependente);
		}
		if("CONTEUDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(licao.conteudo);
		}	
		if("FLTIPOALTERACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(BaseDomain.FLTIPOALTERACAO_INSERIDO);
		}
		if("CDUSUARIO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(licao.cdUsuario);
		}
		
		if("DTALTERACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(DateUtil.getCurrentDate());
		}
		
		if("HRALTERACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(TimeUtil.getCurrentTimeHHMMSS());
		}
		
		return super.getInsertValue(columnName, columnType, columnSize, domain);
	}

}
