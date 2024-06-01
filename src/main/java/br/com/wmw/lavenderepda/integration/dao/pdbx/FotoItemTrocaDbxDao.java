package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.FotoItemTroca;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class FotoItemTrocaDbxDao extends LavendereCrudDbxDao {

	private static FotoItemTrocaDbxDao instance;
	
	public FotoItemTrocaDbxDao() {
		super(FotoItemTroca.TABLE_NAME);
	}
	
	public static FotoItemTrocaDbxDao getInstance() {
		if (instance == null) {
			instance = new FotoItemTrocaDbxDao();
		}
		return instance;
	}
	
	public FotoItemTrocaDbxDao(String newTableName) {
		super(newTableName);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		FotoItemTroca fotoItemTroca = new FotoItemTroca();
		fotoItemTroca.cdEmpresa = rs.getString(1);
		fotoItemTroca.cdRepresentante = rs.getString(2);
		fotoItemTroca.flOrigemPedido = rs.getString(3);
		fotoItemTroca.nuPedido = rs.getString(4);
		fotoItemTroca.cdProduto = rs.getString(5);
		fotoItemTroca.flTipoItemPedido = rs.getString(6);
		fotoItemTroca.nuSeqProduto = rs.getInt(7);
		fotoItemTroca.cdFotoItemTroca = rs.getInt(8);
		fotoItemTroca.cdUsuarioCriacao = rs.getString(9);
		fotoItemTroca.nmFoto = rs.getString(10);
		fotoItemTroca.nuTamanho = rs.getInt(11);
		fotoItemTroca.dtModificacao = rs.getDate(12);
		fotoItemTroca.flEnviadoServidor = rs.getString(13);
		fotoItemTroca.flTipoAlteracao = rs.getString(14);
		fotoItemTroca.cdUsuario = rs.getString(15);
		fotoItemTroca.nuCarimbo = rs.getInt(16);
		fotoItemTroca.rowKey = rs.getString(17);
		return fotoItemTroca;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" NUPEDIDO,");
		sql.append(" CDPRODUTO,");
		sql.append(" FLTIPOITEMPEDIDO,");
		sql.append(" NUSEQPRODUTO,");
		sql.append(" CDFOTOITEMTROCA,");
		sql.append(" CDUSUARIOCRIACAO,");
		sql.append(" NMFOTO,");
		sql.append(" NUTAMANHO,");
		sql.append(" DTMODIFICACAO,");
		sql.append(" FLENVIADOSERVIDOR,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" CDUSUARIO,");
		sql.append(" NUCARIMBO,");
		sql.append(" rowkey ");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" NUPEDIDO,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" CDPRODUTO,");
		sql.append(" FLTIPOITEMPEDIDO,");
		sql.append(" NUSEQPRODUTO,");
		sql.append(" CDFOTOITEMTROCA,");
		sql.append(" CDUSUARIOCRIACAO,");
		sql.append(" NMFOTO,");
		sql.append(" CDUSUARIO,");
		sql.append(" FLENVIADOSERVIDOR,");
		sql.append(" NUTAMANHO,");
		sql.append(" DTMODIFICACAO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" NUCARIMBO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoItemTroca fotoItemTroca = (FotoItemTroca) domain;
		sql.append(Sql.getValue(fotoItemTroca.cdEmpresa)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.cdRepresentante)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.nuPedido)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.flOrigemPedido)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.cdProduto)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.flTipoItemPedido)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.nuSeqProduto)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.cdFotoItemTroca)).append(",");
		sql.append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdUsuario)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.nmFoto)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.cdUsuario)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.flEnviadoServidor)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.nuTamanho)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.dtModificacao)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(fotoItemTroca.nuCarimbo));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoItemTroca fotoItemTroca = (FotoItemTroca) domain;
		sql.append(" FLENVIADOSERVIDOR = ").append(Sql.getValue(fotoItemTroca.flEnviadoServidor)).append(",");
		sql.append(" NUTAMANHO = ").append(Sql.getValue(fotoItemTroca.nuTamanho)).append(",");
		sql.append(" DTMODIFICACAO = ").append(Sql.getValue(fotoItemTroca.dtModificacao)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fotoItemTroca.flTipoAlteracao)).append(",");
		sql.append(" NUCARIMBO = ").append(Sql.getValue(fotoItemTroca.nuCarimbo));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		FotoItemTroca fotoItemTroca = (FotoItemTroca) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", fotoItemTroca.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fotoItemTroca.cdRepresentante);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", fotoItemTroca.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", fotoItemTroca.flOrigemPedido);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", fotoItemTroca.cdProduto);
		sqlWhereClause.addAndCondition("FLTIPOITEMPEDIDO = ", fotoItemTroca.flTipoItemPedido);
		sqlWhereClause.addAndCondition("NUSEQPRODUTO = ", fotoItemTroca.nuSeqProduto);
		sqlWhereClause.addAndCondition("CDFOTOITEMTROCA = ", fotoItemTroca.cdFotoItemTroca);
		sqlWhereClause.addAndCondition("CDUSUARIOCRIACAO = ", fotoItemTroca.cdUsuarioCriacao);
		sqlWhereClause.addAndCondition("NMFOTO = ", fotoItemTroca.nmFoto);
		//--
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FotoItemTroca();
	}
}
