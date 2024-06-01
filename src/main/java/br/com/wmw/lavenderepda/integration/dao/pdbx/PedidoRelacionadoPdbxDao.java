package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoRelacionado;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PedidoRelacionadoPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PedidoRelacionado();
	}

	private static PedidoRelacionadoPdbxDao instance;
	
	public PedidoRelacionadoPdbxDao() {
		super(PedidoRelacionado.TABLE_NAME_PEDIDORELACIONADO);
	}
	
	public PedidoRelacionadoPdbxDao(String newTableName) {
		super(newTableName);
	}
	
	public static PedidoRelacionadoPdbxDao getInstance() {
		if (PedidoRelacionadoPdbxDao.instance == null) {
			PedidoRelacionadoPdbxDao.instance = new PedidoRelacionadoPdbxDao(PedidoRelacionado.TABLE_NAME_PEDIDORELACIONADO);
		}
		return PedidoRelacionadoPdbxDao.instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PedidoRelacionado pedidoRelacionado = new PedidoRelacionado();
		pedidoRelacionado.rowKey = rs.getString("rowKey");
		pedidoRelacionado.cdEmpresa = rs.getString("cdEmpresa");
		pedidoRelacionado.cdRepresentante = rs.getString("cdRepresentante");
		pedidoRelacionado.nuPedido = rs.getString("nuPedido");
		pedidoRelacionado.flOrigemPedido = rs.getString("flOrigemPedido");
		pedidoRelacionado.cdEmpresaRel = rs.getString("cdEmpresaRel");
		pedidoRelacionado.cdRepresentanteRel = rs.getString("cdRepresentanteRel");
		pedidoRelacionado.nuPedidoRel = rs.getString("nuPedidoRel");
		pedidoRelacionado.flOrigemPedidoRel = rs.getString("flOrigemPedidoRel");
		pedidoRelacionado.flTipoAlteracao = rs.getString("flTipoAlteracao");
		pedidoRelacionado.flTipoRelacionamento = rs.getString("flTipoRelacionamento");
		pedidoRelacionado.cdUsuario = rs.getString("cdUsuario");
		return pedidoRelacionado;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowKey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" NUPEDIDO,");
		sql.append(" FLORIGEMPEDIDO,");
		sql.append(" CDEMPRESAREL,");
		sql.append(" CDREPRESENTANTEREL,");
		sql.append(" NUPEDIDOREL,");
		sql.append(" FLORIGEMPEDIDOREL,");
		sql.append(" FLTIPOALTERACAO,");
		sql.append(" FLTIPORELACIONAMENTO,");
		sql.append(" CDUSUARIO");
	}

	@Override
	protected String getInsertValue(String columnName, int columnType, int columnSize, BaseDomain domain) {
		PedidoRelacionado pedidoRelacionado = (PedidoRelacionado) domain;
		if ("ROWKEY".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.getRowKey());
		}
		if ("CDEMPRESA".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.cdEmpresa);
		}
		if ("CDREPRESENTANTE".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.cdRepresentante);
		}
		if ("NUPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.nuPedido);
		}
		if ("FLORIGEMPEDIDO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.flOrigemPedido);
		}
		if ("CDEMPRESAREL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.cdEmpresaRel);
		}
		if ("CDREPRESENTANTEREL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.cdRepresentanteRel);
		}
		if ("NUPEDIDOREL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.nuPedidoRel);
		}
		if ("FLORIGEMPEDIDOREL".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.flOrigemPedidoRel);
		}
		if ("NUCARIMBO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT);
		}
		if ("FLTIPOALTERACAO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.flTipoAlteracao);
		}
		if ("FLTIPORELACIONAMENTO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.flTipoRelacionamento);
		}
		if ("CDUSUARIO".equalsIgnoreCase(columnName)) {
			return Sql.getValue(pedidoRelacionado.cdUsuario);
		}
		return super.getInsertValue(columnName, columnType, columnSize, domain);
	}
	
	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		PedidoRelacionado pedidoRelacionado = (PedidoRelacionado) domain;
		addUpdateValuesPerson(pedidoRelacionado, sql);
		//--
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(PedidoRelacionado.FLTIPOALTERACAO_ALTERADO)).append(",");
		sql.append(" CDUSUARIO = ").append(Sql.getValue(pedidoRelacionado.cdUsuario));
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		PedidoRelacionado pedidoRelacionado = (PedidoRelacionado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", pedidoRelacionado.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", pedidoRelacionado.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("NUPEDIDO", pedidoRelacionado.nuPedido);
		sqlWhereClause.addAndConditionEquals("FLORIGEMPEDIDO", pedidoRelacionado.flOrigemPedido);
		sqlWhereClause.addAndConditionEquals("CDEMPRESAREL", pedidoRelacionado.cdEmpresaRel);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTEREL", pedidoRelacionado.cdRepresentanteRel);
		sqlWhereClause.addAndConditionEquals("NUPEDIDOREL", pedidoRelacionado.nuPedidoRel);
		sqlWhereClause.addAndConditionEquals("FLORIGEMPEDIDOREL", pedidoRelacionado.flOrigemPedidoRel);
		sqlWhereClause.addAndConditionEquals("FLTIPORELACIONAMENTO", pedidoRelacionado.flTipoRelacionamento);
		sql.append(sqlWhereClause.getSql());
	}
	
	public Vector findPedidosRelacionadosByPedido(Pedido pedido) throws SQLException {
		StringBuffer sql = new StringBuffer();
		Vector pedidoRelList = new Vector();
		sql.append("SELECT CDEMPRESAREL, CDREPRESENTANTEREL, NUPEDIDOREL, FLORIGEMPEDIDOREL FROM TBLVPPEDIDORELACIONADO WHERE ");
		sql.append(" CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		sql.append(" AND NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {				
				Pedido pedidoRel = new Pedido();
				pedidoRel.cdEmpresa = rs.getString("cdEmpresaRel");
				pedidoRel.cdRepresentante = rs.getString("cdRepresentanteRel");
				pedidoRel.nuPedido = rs.getString("nuPedidoRel");
				pedidoRel.flOrigemPedido = rs.getString("flOrigemPedidoRel");
				pedidoRelList.addElement(pedidoRel);
			}
			return pedidoRelList;
		}
	}

	public Pedido findUnicoPedidoRelacionadoByPedido(Pedido pedido) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT CDEMPRESAREL, CDREPRESENTANTEREL, NUPEDIDOREL, FLORIGEMPEDIDOREL FROM TBLVPPEDIDORELACIONADO ");
		sql.append(" WHERE CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa));
		sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(pedido.cdRepresentante));
		sql.append(" AND FLORIGEMPEDIDO = ").append(Sql.getValue(pedido.flOrigemPedido));
		sql.append(" AND NUPEDIDO = ").append(Sql.getValue(pedido.nuPedido));
		sql.append(" AND FLTIPORELACIONAMENTO = ").append(Sql.getValue(PedidoRelacionado.FLTIPORELACIONAMENTO_PEDIDO_PRODUCAO));
		sql.append(" LIMIT 1");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Pedido pedidoRel = new Pedido();
			if (rs.next()) {
				pedidoRel.cdEmpresa = rs.getString("cdEmpresaRel");
				pedidoRel.cdRepresentante = rs.getString("cdRepresentanteRel");
				pedidoRel.nuPedido = rs.getString("nuPedidoRel");
				pedidoRel.flOrigemPedido = rs.getString("flOrigemPedidoRel");
			}
			return pedidoRel;
		}
	}

}
