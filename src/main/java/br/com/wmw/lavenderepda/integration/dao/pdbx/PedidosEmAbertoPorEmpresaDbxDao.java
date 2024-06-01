package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class PedidosEmAbertoPorEmpresaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Pedido();
	}

    private static PedidosEmAbertoPorEmpresaDbxDao instance;

    public PedidosEmAbertoPorEmpresaDbxDao() {
    	super(Pedido.TABLE_NAME_PEDIDO);
    }
    
    public static PedidosEmAbertoPorEmpresaDbxDao getInstance() {
        if (instance == null) {
            instance = new PedidosEmAbertoPorEmpresaDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        Pedido pedido = new Pedido();
        pedido.rowKey = rs.getString("rowkey");
        pedido.cdEmpresa = rs.getString("cdEmpresa");
        pedido.cdRepresentante = rs.getString("cdRepresentante");
        pedido.cdCliente = rs.getString("cdCliente");
        pedido.nuPedido = rs.getString("nuPedido");
        pedido.cdStatusPedido = rs.getString("cdStatusPedido");
        pedido.dtEmissao = rs.getDate("dtEmissao");
        pedido.vlTotalPedido = ValueUtil.round(rs.getDouble("vlTotalPedido"));
        pedido.flOrigemPedido = rs.getString("florigempedido");

        Cliente cliente = new Cliente();
        cliente.cdEmpresa = pedido.cdEmpresa;
        cliente.cdRepresentante = pedido.cdRepresentante;
        cliente.cdCliente = pedido.cdCliente;
        cliente.nmRazaoSocial = rs.getString("nmrazaosocial");
        cliente.nuCnpj = rs.getString("nucnpj");
        pedido.setCliente(cliente);
        
        Empresa empresa = new Empresa();
        empresa.cdEmpresa = pedido.cdEmpresa;
        empresa.nmEmpresa = rs.getString("nmempresa");
        empresa.nmEmpresaCurto = rs.getString("nmEmpresaCurto");
        pedido.setEmpresa(empresa);
        
        return pedido;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.NUPEDIDO,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.CDSTATUSPEDIDO,");
        sql.append(" tb.DTEMISSAO,");
        sql.append(" tb.VLTOTALPEDIDO ,");
        sql.append(" tb.FLORIGEMPEDIDO, ");
        sql.append(" cli.NMRAZAOSOCIAL, ");
        sql.append(" cli.NUCNPJ, ");
        sql.append(" emp.NMEMPRESA, ");
        sql.append(" emp.NMEMPRESACURTO");
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
    	Pedido pedido = (Pedido) domain;
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("tb.CDSTATUSPEDIDO = ", pedido.cdStatusPedido);
    	sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", pedido.cdEmpresa);
    	sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", pedido.cdRepresentante);
    	sqlWhereClause.addAndCondition("DTEMISSAO >=", pedido.dtEmissaoInicialFilter);
    	sqlWhereClause.addAndCondition("DTEMISSAO <=", pedido.dtEmissaoFinalFilter);
    	sqlWhereClause.addAndConditionsLikeOr(new String[] {DaoUtil.ALIAS_CLIENTE+".CDCLIENTE", DaoUtil.ALIAS_CLIENTE+".NMRAZAOSOCIAL", DaoUtil.ALIAS_CLIENTE+".NUCNPJ"}, pedido.dsClienteFilter);
    	//--
    	sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	DaoUtil.addJoinCliente(sql);
    	DaoUtil.addJoinEmpresa(sql, DaoUtil.ALIAS_EMPRESA);
    }

    @Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	super.addOrderByWithoutAlias(sql, domain);
    }
    
}