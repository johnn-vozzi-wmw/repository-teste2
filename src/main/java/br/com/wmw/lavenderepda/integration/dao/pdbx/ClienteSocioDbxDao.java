package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClienteSocio;
import totalcross.sql.ResultSet;

public class ClienteSocioDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteSocio();
	}

	public ClienteSocioDbxDao() {
		super(ClienteSocio.TABLE_NAME);
	}
	
	private static ClienteSocioDbxDao instance;
	
	public static ClienteSocioDbxDao getInstance() {
		if (instance == null) {
			instance = new ClienteSocioDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		ClienteSocio clienteSocio = new ClienteSocio();
		clienteSocio.cdEmpresa = rs.getString("cdEmpresa");
		clienteSocio.cdRepresentante = rs.getString("cdRepresentante");
		clienteSocio.cdCliente = rs.getString("cdCliente");
		clienteSocio.cdSocio = rs.getString("cdSocio");
		clienteSocio.nuCnpj = rs.getString("nuCnpj");
		clienteSocio.nmRazaoSocial = rs.getString("nmRazaoSocial");
		clienteSocio.dtEntradaSocio = rs.getDate("dtEntradaSocio");
		clienteSocio.vlPctParticipacao = rs.getDouble("vlPctParticipacao");
		return clienteSocio;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append("CDEMPRESA, CDREPRESENTANTE, CDCLIENTE, CDSOCIO, NUCNPJ, NMRAZAOSOCIAL, DTENTRADASOCIO, VLPCTPARTICIPACAO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		ClienteSocio clienteSocio = (ClienteSocio) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", clienteSocio.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", clienteSocio.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("CDCLIENTE", clienteSocio.cdCliente);
		sql.append(sqlWhereClause.getSql());
	}

}
