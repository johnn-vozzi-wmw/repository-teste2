package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class RelPedidoDataEntregaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Pedido();
	}

    private static RelPedidoDataEntregaPdbxDao instance;

	public RelPedidoDataEntregaPdbxDao() {
		super("");
	}

    public static RelPedidoDataEntregaPdbxDao getInstance() {
        if (instance == null) {
            instance = new RelPedidoDataEntregaPdbxDao();
        }
        return instance;
    }

    //@Override
    public Vector findAllByExample(BaseDomain domain) throws java.sql.SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domain, sql);
        sql.append(" from ").append(Pedido.TABLE_NAME_PEDIDO).append(" p, ").append(ItemPedido.TABLE_NAME_ITEMPEDIDO).append(" i ");
        addWhereByExample(domain, sql);
        addOrderBy(sql);
        return findAll(domain, sql.toString());
    }

    public Vector findAllByExampleErp(BaseDomain domain) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domain, sql);
        sql.append(" from ").append(Pedido.TABLE_NAME_PEDIDOERP).append(" p, ").append(ItemPedido.TABLE_NAME_ITEMPEDIDOERP).append(" i ");
        addWhereByExample(domain, sql);
        addOrderBy(sql);
        return findAll(domain, sql.toString());
    }

    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" p.cdCliente,");
		sql.append(" p.nupedido,");
		sql.append(" p.dtEntrega,");
		sql.append(" p.rowkey,");
		sql.append(" p.vlTotalPedido,");
		sql.append(" sum (i.QTITEMFISICO) as qtItensTotalUnidade,");
		sql.append(" sum (i.QTITEMFATURAMENTO) as qtItensTotalCaixaTt");
	}

	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		Pedido pedido = (Pedido)domain;
    	sql.append(" where p.cdRepresentante = ").append(Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdRepresentante)).append(" and p.cdEmpresa = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa)).append(
    			" and i.cdRepresentante = p.cdRepresentante and i.cdEmpresa = p.cdEmpresa and i.nupedido = p.nupedido and i.flOrigemPedido = p.flOrigemPedido");
    	if (!ValueUtil.isEmpty(pedido.cdStatusPedido)) {
            sql.append(" and cdStatusPedido = ").append(Sql.getValue(pedido.cdStatusPedido));
    	}
	}

	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" group by p.nupedido, p.cdCliente, p.dtEntrega, p.rowkey, p.vlTotalPedido");
	}

	protected void addOrderBy(StringBuffer sql) {
		sql.append(" group by p.nupedido, p.cdCliente, p.dtEntrega, p.rowkey, p.vlTotalPedido");
	}

	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		Pedido pedido = new Pedido();
		pedido.cdCliente = rs.getString("cdCliente");
		pedido.dtEntrega = rs.getDate("dtEntrega");
		pedido.qtItensTotalUnidade = rs.getDouble("qtItensTotalUnidade");
		pedido.qtItensTotalCaixa = rs.getDouble("qtItensTotalCaixaTt");
		pedido.vlTotalPedido = rs.getDouble("vlTotalPedido");
		pedido.rowKey = rs.getString("rowKey");
		return pedido;
	}

	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
}