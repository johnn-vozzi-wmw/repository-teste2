package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.NotaFiscal;
import totalcross.sql.ResultSet;

public class NotaFiscalDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new NotaFiscal();
	}

	private static NotaFiscalDao instance;

	public NotaFiscalDao() {
		super(NotaFiscal.TABLE_NAME);
	}
	
	public static NotaFiscalDao getInstance() {
        if (instance == null) {
            instance = new NotaFiscalDao();
        }
        return instance;
    }

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		NotaFiscal notaFiscal = new NotaFiscal();
		notaFiscal.rowKey = rs.getString("rowkey");
		notaFiscal.cdEmpresa = rs.getString("cdEmpresa");
		notaFiscal.nuNotaFiscal = rs.getString("nuNotaFiscal");
		notaFiscal.cdSerie = rs.getString("cdSerie");
		notaFiscal.nuPedido = rs.getString("nuPedido");
		notaFiscal.flOrigemPedido = rs.getString("flOrigemPedido");
		notaFiscal.cdRepresentante = rs.getString("cdRepresentante");
		notaFiscal.nmRepresentante = rs.getString("nmRepresentante");
		notaFiscal.cdCliente = rs.getString("cdCliente");
		notaFiscal.nmRazaoSocial = rs.getString("nmRazaoSocial");
		notaFiscal.vlNotaFiscal = rs.getDouble("vlNotaFiscal");
		notaFiscal.dtEmissao = rs.getDate("dtEmissao");
		notaFiscal.dtPrevisaoEntrega = rs.getDate("dtPrevisaoEntrega");
		notaFiscal.dtSaida = rs.getDate("dtSaida");
		notaFiscal. hrSaida = rs.getString("hrSaida");
		notaFiscal.cdTransportadora = rs.getString("cdTransportadora");
		notaFiscal.nmTransportadora = rs.getString("nmTransportadora");
		notaFiscal.dsObservacao = rs.getString("dsObservacao");
		notaFiscal.nuCarimbo = rs.getInt("nuCarimbo");
		notaFiscal.flTipoAlteracao = rs.getString("flTipoAlteracao");
		notaFiscal.cdUsuario = rs.getString("cdUsuario");
		return notaFiscal;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" NUNOTAFISCAL,");
		sql.append(" CDSERIE,");
		sql.append(" NUPEDIDO,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" NMREPRESENTANTE,");
		sql.append(" CDCLIENTE,");
		sql.append(" NMRAZAOSOCIAL,");
		sql.append(" VLNOTAFISCAL,");
		sql.append(" DTEMISSAO,");
		sql.append(" DTPREVISAOENTREGA,");
		sql.append(" DTSAIDA,");
		sql.append(" HRSAIDA,");
		sql.append(" CDTRANSPORTADORA,");
		sql.append(" NMTRANSPORTADORA,");
		sql.append(" DSOBSERVACAO,");
		sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO" );
		
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException { }

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		NotaFiscal notaFiscal = (NotaFiscal) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", notaFiscal.cdEmpresa);
		sqlWhereClause.addAndCondition("NUNOTAFISCAL = ", notaFiscal.nuNotaFiscal);
		sqlWhereClause.addAndCondition("CDSERIE = ", notaFiscal.cdSerie);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", notaFiscal.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", notaFiscal.nuPedido);
		sql.append(sqlWhereClause.getSql());
	}
	
}
