package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;

import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.CatalogoItemPedLog;
import totalcross.sql.ResultSet;

import java.sql.SQLException;

public class CatalogoItemPedLogPdbxDao extends LavendereCrudDbxDao {

	private static CatalogoItemPedLogPdbxDao instance;

	public CatalogoItemPedLogPdbxDao() {
		super(CatalogoItemPedLog.TABLE_NAME);
	}

	public static CatalogoItemPedLogPdbxDao getInstance() {
		if (instance == null) instance = new CatalogoItemPedLogPdbxDao();
		return instance;
	}

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CatalogoItemPedLog();
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		CatalogoItemPedLog catalogoItemPedLog = new CatalogoItemPedLog();
		catalogoItemPedLog.rowKey = rs.getString("rowkey");
		catalogoItemPedLog.cdEmpresa = rs.getString("cdEmpresa");
		catalogoItemPedLog.cdRepresentante = rs.getString("cdRepresentante");
		catalogoItemPedLog.cdCatalogoItemPedidoLog = rs.getString("cdCatalogoItemPedidoLog");
		catalogoItemPedLog.nuSeqCatalogoItemPedidoLog = rs.getInt("nuSeqCatalogoItemPedidoLog");
		catalogoItemPedLog.flOrigemPedido = rs.getString("flOrigemPedido");
		catalogoItemPedLog.nuPedido = rs.getString("nuPedido");
		catalogoItemPedLog.cdProduto = rs.getString("cdProduto");
		catalogoItemPedLog.flTipoItemPedido = rs.getString("flTipoItemPedido");
		catalogoItemPedLog.nuSeqProduto = rs.getInt("nuSeqProduto");
		catalogoItemPedLog.vlItemPedido = rs.getDouble("vlItemPedido");
		catalogoItemPedLog.dtGeracaoCatalogo = rs.getDate("dtGeracaoCatalogo");
		catalogoItemPedLog.hrGeracaoCatalogo = rs.getString("hrGeracaoCatalogo");
		catalogoItemPedLog.flTipoAlteracao = rs.getString("flTipoAlteracao");
		catalogoItemPedLog.dtAlteracao = rs.getDate("dtAlteracao");
		catalogoItemPedLog.hrAlteracao = rs.getString("hrAlteracao");
		return catalogoItemPedLog;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowKey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDCATALOGOITEMPEDIDOLOG,");
		sql.append(" NUSEQCATALOGOITEMPEDIDOLOG,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" NUPEDIDO,");
		sql.append(" CDPRODUTO,");
		sql.append(" DTGERACAOCATALOGO,");
		sql.append(" HRGERACAOCATALOGO,");
		sql.append(" FLTIPOITEMPEDIDO,");
		sql.append(" NUSEQPRODUTO,");
		sql.append(" VLITEMPEDIDO");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDCATALOGOITEMPEDIDOLOG,");
		sql.append(" NUSEQCATALOGOITEMPEDIDOLOG,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" NUPEDIDO,");
		sql.append(" CDPRODUTO,");
		sql.append(" FLTIPOITEMPEDIDO,");
		sql.append(" NUSEQPRODUTO,");
		sql.append(" VLITEMPEDIDO,");
		sql.append(" CDUSUARIO,");
		sql.append(" DTGERACAOCATALOGO,");
		sql.append(" HRGERACAOCATALOGO,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" DTALTERACAO,");
		sql.append(" HRALTERACAO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		CatalogoItemPedLog catalogoItemPedLog = (CatalogoItemPedLog) domain;
		sql.append(Sql.getValue(catalogoItemPedLog.cdEmpresa)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.cdRepresentante)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.cdCatalogoItemPedidoLog)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.nuSeqCatalogoItemPedidoLog)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.flOrigemPedido)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.nuPedido)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.cdProduto)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.flTipoItemPedido)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.nuSeqProduto)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.vlItemPedido)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.cdUsuario)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.dtGeracaoCatalogo)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.hrGeracaoCatalogo)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.flTipoAlteracao)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.dtAlteracao)).append(",");
		sql.append(Sql.getValue(catalogoItemPedLog.hrAlteracao));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		CatalogoItemPedLog catalogoItemPedLog = new CatalogoItemPedLog();
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", catalogoItemPedLog.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", catalogoItemPedLog.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCATALOGOITEMPEDIDOLOG = ", catalogoItemPedLog.cdCatalogoItemPedidoLog);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", catalogoItemPedLog.nuPedido);
	}

}
