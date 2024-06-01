package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.InfoEntregaPed;
import totalcross.sql.ResultSet;

public class InfoEntregaPedPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new InfoEntregaPed();
	}

    private static InfoEntregaPedPdbxDao instance;

    public InfoEntregaPedPdbxDao() {
        super(InfoEntregaPed.TABLE_NAME);
    }

    public static InfoEntregaPedPdbxDao getInstance() {
        if (instance == null) {
            instance = new InfoEntregaPedPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        InfoEntregaPed infoEntregaPed = new InfoEntregaPed();
        infoEntregaPed.rowKey = rs.getString("rowkey");
        infoEntregaPed.cdEmpresa = rs.getString("cdEmpresa");
        infoEntregaPed.cdRepresentante = rs.getString("cdRepresentante");
        infoEntregaPed.flOrigemPedido = rs.getString("flOrigemPedido");
        infoEntregaPed.nuPedido = rs.getString("nuPedido");
        infoEntregaPed.dtEmissao = rs.getDate("dtEmissao");
        infoEntregaPed.cdCliente = rs.getString("cdCliente");
        infoEntregaPed.dsInfoEntrega = rs.getString("dsInfoEntrega");
        infoEntregaPed.nuCarimbo = rs.getInt("nuCarimbo");
        infoEntregaPed.flTipoAlteracao = rs.getString("flTipoAlteracao");
        infoEntregaPed.cdUsuario = rs.getString("cdUsuario");
        infoEntregaPed.cliente = new Cliente();
        infoEntregaPed.cliente.cdCliente = rs.getString("cdCliente");
        infoEntregaPed.cliente.nmRazaoSocial = rs.getString("nmRazaoSocial");
        infoEntregaPed.cliente.nuCnpj = rs.getString("nuCnpj");
        return infoEntregaPed;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.FLORIGEMPEDIDO,");
        sql.append(" tb.NUPEDIDO,");
        sql.append(" tb.DTEMISSAO,");
        sql.append(" tb.CDCLIENTE,");
        sql.append(" tb.DSINFOENTREGA,");
        sql.append(" tb.NUCARIMBO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.CDUSUARIO,");
        sql.append(" cli.NMRAZAOSOCIAL,");
        sql.append(" cli.NUCNPJ");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}
	
	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		super.addJoin(domainFilter, sql);
    	sql.append(" LEFT JOIN TBLVPCLIENTE cli ON ");
		sql.append(" cli.CDEMPRESA = tb.CDEMPRESA");
		sql.append(" AND cli.CDREPRESENTANTE = tb.CDREPRESENTANTE");
		sql.append(" AND cli.CDCLIENTE = tb.CDCLIENTE ");
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" DTEMISSAO,");
        sql.append(" CDCLIENTE,");
        sql.append(" DSINFOENTREGA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	InfoEntregaPed infoEntregaPed = (InfoEntregaPed) domain;
        sql.append(Sql.getValue(infoEntregaPed.cdEmpresa)).append(",");
        sql.append(Sql.getValue(infoEntregaPed.cdRepresentante)).append(",");
        sql.append(Sql.getValue(infoEntregaPed.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(infoEntregaPed.nuPedido)).append(",");
        sql.append(Sql.getValue(infoEntregaPed.dtEmissao)).append(",");
        sql.append(Sql.getValue(infoEntregaPed.cdCliente)).append(",");
        sql.append(Sql.getValue(infoEntregaPed.dsInfoEntrega)).append(",");
        sql.append(Sql.getValue(infoEntregaPed.nuCarimbo)).append(",");
        sql.append(Sql.getValue(infoEntregaPed.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(infoEntregaPed.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	InfoEntregaPed infoEntregaPed = (InfoEntregaPed) domain;
    	sql.append(" DTEMISSAO = ").append(Sql.getValue(infoEntregaPed.dtEmissao)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(infoEntregaPed.cdCliente)).append(",");
        sql.append(" DSINFOENTREGA = ").append(Sql.getValue(infoEntregaPed.dsInfoEntrega)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(infoEntregaPed.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(infoEntregaPed.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(infoEntregaPed.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	InfoEntregaPed infoentregaped = (InfoEntregaPed) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("tb.CDEMPRESA", infoentregaped.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("tb.CDREPRESENTANTE", infoentregaped.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("tb.FLORIGEMPEDIDO", infoentregaped.flOrigemPedido);
		sqlWhereClause.addAndConditionEquals("tb.NUPEDIDO", infoentregaped.nuPedido);
		sqlWhereClause.addAndConditionEquals("tb.CDCLIENTE", infoentregaped.cdCliente);
		sqlWhereClause.addAndLikeCondition("tb.DSINFOENTREGA", infoentregaped.dsInfoEntrega);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}