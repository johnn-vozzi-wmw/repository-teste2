package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClienteProduto;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class ClienteProdutoDbxDao extends AbstractProdutoTipoRelacaoBaseDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteProduto();
	}

    private static ClienteProdutoDbxDao instance;

    public ClienteProdutoDbxDao() {
        super(ClienteProduto.TABLE_NAME);
    }

    public static ClienteProdutoDbxDao getInstance() {
        if (instance == null) {
            instance = new ClienteProdutoDbxDao();
        }
        return instance;
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ClienteProduto clienteProduto = new ClienteProduto();
        clienteProduto.rowKey = rs.getString("rowkey");
        clienteProduto.cdEmpresa = rs.getString("cdEmpresa");
        clienteProduto.cdRepresentante = rs.getString("cdRepresentante");
        clienteProduto.cdCliente = rs.getString("cdCliente");
        clienteProduto.cdProduto = rs.getString("cdProduto");
        clienteProduto.flTipoRelacao = rs.getString("flTipoRelacao");
        clienteProduto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        clienteProduto.nuCarimbo = rs.getInt("nuCarimbo");
        clienteProduto.cdUsuario = rs.getString("cdUsuario");
        return clienteProduto;
    }

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return null;
	}

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPORELACAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { /**/ }

    @Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException { /**/ }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
        ClienteProduto clienteProduto = (ClienteProduto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", clienteProduto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", clienteProduto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", clienteProduto.cdProduto);
		addFlTipoRelacaoDomainCondition(clienteProduto, sqlWhereClause);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected String getAliasCteFlTipoRelacao() {
		return DaoUtil.ALIAS_CTECLIPROFLTIPORELACAO;
	}
	
	@Override
	protected String getAliasCTEDomain() {
		return DaoUtil.ALIAS_CTECLIENTEPRODUTO;
	}
    
}