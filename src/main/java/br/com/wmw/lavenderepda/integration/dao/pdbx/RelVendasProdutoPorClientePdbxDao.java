package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.RelVendasProdutoPorCliente;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class RelVendasProdutoPorClientePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RelVendasProdutoPorCliente();
	}

    private static RelVendasProdutoPorClientePdbxDao instance;

    public RelVendasProdutoPorClientePdbxDao() {
		super("");
	}

    public static RelVendasProdutoPorClientePdbxDao getInstance() {
        if (instance == null) {
            instance = new RelVendasProdutoPorClientePdbxDao();
        }
        return instance;
    }

    //@Override
    public Vector findAllByExample(BaseDomain domain) throws java.sql.SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domain, sql);
        sql.append(" from ").append(ItemPedido.TABLE_NAME_ITEMPEDIDO).append(" i, ").append(Pedido.TABLE_NAME_PEDIDO).append(" p, ").append(Cliente.TABLE_NAME).append(" c ");
        addWhereByExample(domain, sql);
        addOrderBy(sql);
        return findAll(domain, sql.toString());
    }

    public Vector findAllByExampleErp(BaseDomain domain) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectColumns(domain, sql);
        sql.append(" from ").append(ItemPedido.TABLE_NAME_ITEMPEDIDOERP).append(" i, ").append(Pedido.TABLE_NAME_PEDIDOERP).append(" p, ").append(Cliente.TABLE_NAME).append(" c ");
        addWhereByExample(domain, sql);
        addOrderBy(sql);
        return findAll(domain, sql.toString());
    }

    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" c.NMRAZAOSOCIAL,");
		sql.append(" p.DTEMISSAO,");
		sql.append(" i.QTITEMFISICO ");
	}

	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RelVendasProdutoPorCliente relVendasProdutoPorCliente = (RelVendasProdutoPorCliente) domain;
    	sql.append(" where p.cdcliente = c.cdCliente and i.cdProduto = '").append(relVendasProdutoPorCliente.cdProduto).append("' and i.cdEmpresa = ").append(Sql.getValue(SessionLavenderePda.cdEmpresa)).append(" and");
    	sql.append(" i.cdRepresentante = p.cdRepresentante and i.cdEmpresa = p.cdEmpresa and i.nupedido = p.nupedido and i.cdRepresentante=c.cdRepresentante and i.cdEmpresa = c.cdEmpresa");

	}

	protected void addOrderBy(StringBuffer sql) {
    	sql.append(" order by p.dtEmissao");
	}

	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		sql.append(" order by p.dtEmissao");
	}


	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		RelVendasProdutoPorCliente relVendasProdutoPorCliente = new RelVendasProdutoPorCliente();
		relVendasProdutoPorCliente.nmRazaoSocial = rs.getString("nmrazaosocial");
		relVendasProdutoPorCliente.dtEmissao = rs.getDate("dtemissao");
		relVendasProdutoPorCliente.qtItemFisico = rs.getDouble("qtitemFisico");
		return relVendasProdutoPorCliente;
	}

	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

}
