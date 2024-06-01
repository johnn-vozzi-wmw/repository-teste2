package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoReg;
import totalcross.sql.ResultSet;

public class PesquisaMercadoRegDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PesquisaMercadoReg();
	}

	private static PesquisaMercadoRegDbxDao instance;

	public PesquisaMercadoRegDbxDao() {
		super(PesquisaMercadoReg.TABLE_NAME);
	}

	public static PesquisaMercadoRegDbxDao getInstance() {
		if (instance == null) {
			instance = new PesquisaMercadoRegDbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PesquisaMercadoReg pesquisaMercadoReg = new PesquisaMercadoReg();
		pesquisaMercadoReg.rowKey = rs.getString("rowkey");
		pesquisaMercadoReg.cdEmpresa = rs.getString("cdEmpresa");
		pesquisaMercadoReg.cdRepresentante = rs.getString("cdRepresentante");
		pesquisaMercadoReg.cdPesquisaMercadoConfig = rs.getString("cdPesquisaMercadoConfig");
		pesquisaMercadoReg.cdCliente = rs.getString("cdCliente");
		pesquisaMercadoReg.cdProduto = rs.getString("cdProduto");
		pesquisaMercadoReg.cdConcorrente = rs.getString("cdConcorrente");
		pesquisaMercadoReg.dtEmissao = rs.getDate("dtEmissao");
		pesquisaMercadoReg.dtInsercaoProduto = rs.getDate("dtInsercaoProduto");
		pesquisaMercadoReg.hrInsercaoProduto = rs.getString("hrInsercaoProduto");
		pesquisaMercadoReg.vlUnitario = rs.getDouble("vlUnitario");
		pesquisaMercadoReg.flControleWmw = rs.getString("flControleWmw");
		pesquisaMercadoReg.dsMensagemControleWmw = rs.getString("dsMensagemControleWmw");
		pesquisaMercadoReg.flControleErp = rs.getString("flControleErp");
		pesquisaMercadoReg.dsMensagemControleErp = rs.getString("dsMensagemControleErp");
		pesquisaMercadoReg.dtRecebimento = rs.getDate("dtRecebimento");
		pesquisaMercadoReg.hrRecebimento = rs.getString("hrRecebimento");
		pesquisaMercadoReg.dtEnvioErp = rs.getDate("dtEnvioErp");
		pesquisaMercadoReg.hrEnvioErp = rs.getString("hrEnvioErp");
		pesquisaMercadoReg.cdUsuarioEmissao = rs.getString("cdUsuarioEmissao");
		pesquisaMercadoReg.nuPedido = rs.getString("nuPedido");
		pesquisaMercadoReg.flOrigemPedido = rs.getString("flOrigemPedido");
		pesquisaMercadoReg.cdLatitude = rs.getDouble("cdLatitude");
		pesquisaMercadoReg.cdLongitude = rs.getDouble("cdLongitude");
		pesquisaMercadoReg.flFinalizada = rs.getString("flFinalizada");
		pesquisaMercadoReg.nuCarimbo = rs.getInt("nuCarimbo");
		return pesquisaMercadoReg;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
		sql.append(" rowkey,");
		getColumns(sql);
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) {
		getColumns(sql);
	}

	private void getColumns(StringBuffer sql) {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDPESQUISAMERCADOCONFIG,");
		sql.append(" CDCLIENTE,");
		sql.append(" CDPRODUTO,");
		sql.append(" CDCONCORRENTE,");
		sql.append(" DTEMISSAO,");
		sql.append(" DTINSERCAOPRODUTO,");
		sql.append(" HRINSERCAOPRODUTO,");
		sql.append(" VLUNITARIO,");
		sql.append(" FLCONTROLEWMW,");
		sql.append(" DSMENSAGEMCONTROLEWMW,");
		sql.append(" FLCONTROLEERP,");
		sql.append(" DSMENSAGEMCONTROLEERP,");
		sql.append(" DTRECEBIMENTO,");
		sql.append(" HRRECEBIMENTO,");
		sql.append(" DTENVIOERP,");
		sql.append(" HRENVIOERP,");
		sql.append(" CDUSUARIOEMISSAO,");
		sql.append(" NUPEDIDO,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" CDLATITUDE,");
		sql.append(" CDLONGITUDE,");
		sql.append(" FLFINALIZADA,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" NUCARIMBO,");
		sql.append(" CDUSUARIO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoReg pesquisaMercadoReg = (PesquisaMercadoReg) domain;
		sql.append(Sql.getValue(pesquisaMercadoReg.cdEmpresa)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.cdRepresentante)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.cdPesquisaMercadoConfig)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.cdCliente)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.cdProduto)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.cdConcorrente)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.dtEmissao)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.dtInsercaoProduto)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.hrInsercaoProduto)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.vlUnitario)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.flControleWmw)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.dsMensagemControleWmw)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.flControleErp)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.dsMensagemControleErp)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.dtRecebimento)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.hrRecebimento)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.dtEnvioErp)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.hrEnvioErp)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.cdUsuarioEmissao)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.nuPedido)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.flOrigemPedido)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.cdLatitude)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.cdLatitude)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.flFinalizada)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.nuCarimbo)).append(",");
		sql.append(Sql.getValue(pesquisaMercadoReg.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoReg pesquisaMercadoReg = (PesquisaMercadoReg) domain;
		sql.append(" DTINSERCAOPRODUTO = ").append(Sql.getValue(pesquisaMercadoReg.dtInsercaoProduto)).append(",");
		sql.append(" HRINSERCAOPRODUTO = ").append(Sql.getValue(pesquisaMercadoReg.hrInsercaoProduto)).append(",");
		sql.append(" VLUNITARIO = ").append(Sql.getValue(pesquisaMercadoReg.vlUnitario)).append(",");
		sql.append(" FLCONTROLEWMW = ").append(Sql.getValue(pesquisaMercadoReg.flControleWmw)).append(",");
		sql.append(" DSMENSAGEMCONTROLEWMW = ").append(Sql.getValue(pesquisaMercadoReg.dsMensagemControleWmw)).append(",");
		sql.append(" FLCONTROLEERP = ").append(Sql.getValue(pesquisaMercadoReg.flControleErp)).append(",");
		sql.append(" DSMENSAGEMCONTROLEERP = ").append(Sql.getValue(pesquisaMercadoReg.dsMensagemControleErp)).append(",");
		sql.append(" DTRECEBIMENTO = ").append(Sql.getValue(pesquisaMercadoReg.dtRecebimento)).append(",");
		sql.append(" HRRECEBIMENTO = ").append(Sql.getValue(pesquisaMercadoReg.hrRecebimento)).append(",");
		sql.append(" DTENVIOERP = ").append(Sql.getValue(pesquisaMercadoReg.dtEnvioErp)).append(",");
		sql.append(" HRENVIOERP = ").append(Sql.getValue(pesquisaMercadoReg.hrEnvioErp)).append(",");
		sql.append(" CDUSUARIOEMISSAO = ").append(Sql.getValue(pesquisaMercadoReg.cdUsuarioEmissao)).append(",");
		sql.append(" NUPEDIDO = ").append(Sql.getValue(pesquisaMercadoReg.nuPedido)).append(",");
		sql.append(" FLORIGEMPEDIDO = ").append(Sql.getValue(pesquisaMercadoReg.flOrigemPedido)).append(",");
		sql.append(" CDLATITUDE = ").append(Sql.getValue(pesquisaMercadoReg.cdLatitude)).append(",");
		sql.append(" CDLONGITUDE = ").append(Sql.getValue(pesquisaMercadoReg.cdLongitude)).append(",");
		sql.append(" FLFINALIZADA = ").append(Sql.getValue(pesquisaMercadoReg.flFinalizada));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoReg pesquisaMercadoReg = (PesquisaMercadoReg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" tb.CDEMPRESA = ", pesquisaMercadoReg.cdEmpresa);
		sqlWhereClause.addAndCondition(" tb.CDREPRESENTANTE = ", pesquisaMercadoReg.cdRepresentante);
		sqlWhereClause.addAndCondition(" tb.CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoReg.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition(" tb.CDCLIENTE = ", pesquisaMercadoReg.cdCliente);
		sqlWhereClause.addAndCondition(" tb.CDPRODUTO = ", pesquisaMercadoReg.cdProduto);
		sqlWhereClause.addAndCondition(" tb.FLCONTROLEWMW = ", pesquisaMercadoReg.flControleWmw);
		sqlWhereClause.addAndCondition(" tb.DSMENSAGEMCONTROLEWMW = ", pesquisaMercadoReg.dsMensagemControleWmw);
		sqlWhereClause.addAndCondition(" tb.FLCONTROLEERP = ", pesquisaMercadoReg.flControleErp);
		sqlWhereClause.addAndCondition(" tb.DSMENSAGEMCONTROLEERP = ", pesquisaMercadoReg.dsMensagemControleErp);
		sqlWhereClause.addAndCondition(" tb.CDUSUARIOEMISSAO = ", pesquisaMercadoReg.cdUsuarioEmissao);
		sqlWhereClause.addAndCondition(" tb.NUPEDIDO = ", pesquisaMercadoReg.nuPedido);
		sqlWhereClause.addAndCondition(" tb.FLORIGEMPEDIDO = ", pesquisaMercadoReg.flOrigemPedido);
		sqlWhereClause.addAndCondition(" tb.FLTIPOALTERACAO = ", pesquisaMercadoReg.flTipoAlteracao);
		sqlWhereClause.addAndCondition(" tb.FLFINALIZADA = ", pesquisaMercadoReg.flFinalizada);
		sql.append(sqlWhereClause.getSql());
	}

	@Override
	public void deleteAllByExample(BaseDomain domain) throws SQLException {
		super.deleteAllByExample(domain);
	}

	@Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) {
		PesquisaMercadoReg pesquisaMercadoReg = (PesquisaMercadoReg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", pesquisaMercadoReg.cdEmpresa);
		sqlWhereClause.addAndCondition(" CDREPRESENTANTE = ", pesquisaMercadoReg.cdRepresentante);
		sqlWhereClause.addAndCondition(" CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoReg.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition(" CDCLIENTE = ", pesquisaMercadoReg.cdCliente);
		sqlWhereClause.addAndCondition(" CDPRODUTO = ", pesquisaMercadoReg.cdProduto);
		sqlWhereClause.addAndCondition(" CDCONCORRENTE = ", pesquisaMercadoReg.cdConcorrente);
		sqlWhereClause.addAndCondition(" NUPEDIDO = ", pesquisaMercadoReg.nuPedido);
		sqlWhereClause.addAndCondition(" FLORIGEMPEDIDO = ", pesquisaMercadoReg.flOrigemPedido);
		sql.append(sqlWhereClause.getSql());
	}

	public void updateCoordenadasByExample(PesquisaMercadoReg pesquisaMercadoReg) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("update ");
		sql.append(PesquisaMercadoReg.TABLE_NAME);
		sql.append(" set ");
		sql.append(" CDLATITUDE = ").append(Sql.getValue(pesquisaMercadoReg.cdLatitude)).append(",");
		sql.append(" CDLONGITUDE = ").append(Sql.getValue(pesquisaMercadoReg.cdLongitude));
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDEMPRESA = ", pesquisaMercadoReg.cdEmpresa);
		sqlWhereClause.addAndCondition(" CDREPRESENTANTE = ", pesquisaMercadoReg.cdRepresentante);
		sqlWhereClause.addAndCondition(" CDPESQUISAMERCADOCONFIG = ", pesquisaMercadoReg.cdPesquisaMercadoConfig);
		sqlWhereClause.addAndCondition(" CDCLIENTE = ", pesquisaMercadoReg.cdCliente);
		sqlWhereClause.addAndCondition(" ((CDLATITUDE is null OR CDLATITUDE = '') OR (CDLONGITUDE is null or CDLONGITUDE = ''))");
		sql.append(sqlWhereClause.getSql());
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException e) {
			//nenhum registro afetado.
		}
	}

	public void clearNuPedidoAfterDeletePedido(PesquisaMercadoReg pesquisaMercadoReg) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("update ");
		sql.append(PesquisaMercadoReg.TABLE_NAME);
		sql.append(" set NUPEDIDO = NULL, FLORIGEMPEDIDO = NULL ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" CDREPRESENTANTE = ", pesquisaMercadoReg.cdRepresentante);
		sqlWhereClause.addAndCondition(" NUPEDIDO = ", pesquisaMercadoReg.nuPedido);
		sqlWhereClause.addAndCondition(" FLORIGEMPEDIDO = ", pesquisaMercadoReg.flOrigemPedido);
		sql.append(sqlWhereClause.getSql());
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException e) {
			//nenhum registro afetado
		}
	}

	@Override
	public void delete(BaseDomain domain) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" delete from ");
		sql.append(tableName);
		sql.append(" where rowkey = ");
		sql.append(Sql.getValue(((PesquisaMercadoReg) domain).rowKey));
		executeUpdate(sql.toString());
	}

	public void updateFlFinalizada(PesquisaMercadoReg pesquisaMercadoReg, String flFinalizada) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("update ");
		sql.append(PesquisaMercadoReg.TABLE_NAME);
		sql.append(" set FLFINALIZADA = ").append(Sql.getValue(flFinalizada)).append(",");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ALTERADO));
		addWhereDeleteByExample(pesquisaMercadoReg, sql);
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException e) {
			//nenhum registro afetado
		}
	}

}
