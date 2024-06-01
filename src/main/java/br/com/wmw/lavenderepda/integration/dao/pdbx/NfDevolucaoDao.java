package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import totalcross.sql.ResultSet;

public class NfDevolucaoDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new NfDevolucao();
	}

	private static NfDevolucaoDao instance;

	public NfDevolucaoDao() {
		super(NfDevolucao.TABLE_NAME);
	}
	
	public static NfDevolucaoDao getInstance() {
        if (instance == null) {
            instance = new NfDevolucaoDao();
        }
        return instance;
    }

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		NfDevolucao nfDevolucao = new NfDevolucao();
		nfDevolucao.rowKey = rs.getString("rowkey");
		nfDevolucao.cdEmpresa = rs.getString("cdEmpresa");
		nfDevolucao.cdRepresentante = rs.getString("cdRepresentante");
		nfDevolucao.cdSerie = rs.getString("cdSerie");
		nfDevolucao.nuNfDevolucao = rs.getString("nuNfDevolucao");
		nfDevolucao.nuPedido = rs.getString("nuPedido");
		nfDevolucao.flOrigemPedido = rs.getString("flOrigemPedido");
		nfDevolucao.cdCliente = rs.getString("cdCliente");
		nfDevolucao.vlNfDevolucao = rs.getDouble("vlNfDevolucao");
		nfDevolucao.dtEmissao = rs.getDate("dtEmissao");
		nfDevolucao.cdTransportadora = rs.getString("cdTransportadora");
		nfDevolucao.dsObservacao = rs.getString("dsObservacao");
		nfDevolucao.flTipoAlteracao = rs.getString("flTipoAlteracao");
		nfDevolucao.flAprovacao = rs.getString("FLAPROVACAO");
		return nfDevolucao;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" NUNFDEVOLUCAO,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDSERIE,");
		sql.append(" NUPEDIDO,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" CDCLIENTE,");
		sql.append(" VLNFDEVOLUCAO,");
		sql.append(" DTEMISSAO,");
		sql.append(" CDTRANSPORTADORA,");
		sql.append(" DSOBSERVACAO,");
        sql.append(" FLTIPOALTERACAO, ");
        sql.append(" FLAPROVACAO");
		
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException { 
		
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { 
		
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		NfDevolucao nfDevolucao = (NfDevolucao) domain;
		sql.append(" FLAPROVACAO = ").append(Sql.getValue(nfDevolucao.flAprovacao)).append(",");
		sql.append(" DSOBSERVACAO = ").append(Sql.getValue(nfDevolucao.dsObservacao)).append(",");
		sql.append(" CDUSUARIO = ").append(Sql.getValue(nfDevolucao.cdUsuario)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(nfDevolucao.flTipoAlteracao)).append(",");
		sql.append(" HRALTERACAO = ").append(Sql.getValue(nfDevolucao.hrAlteracao)).append(",");
		sql.append(" DTALTERACAO = ").append(Sql.getValue(nfDevolucao.dtAlteracao));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		NfDevolucao notaDevolucao = (NfDevolucao) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", notaDevolucao.cdEmpresa);
		sqlWhereClause.addAndCondition("NUNFDEVOLUCAO = ", notaDevolucao.nuNfDevolucao);
		sqlWhereClause.addAndCondition("CDSERIE = ", notaDevolucao.cdSerie);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", notaDevolucao.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", notaDevolucao.nuPedido);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", notaDevolucao.cdCliente);
		sqlWhereClause.addAndCondition("FLAPROVACAO = ", notaDevolucao.flAprovacao);
		sql.append(sqlWhereClause.getSql());
	}
	
}
