package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ClienteSetorOrigem;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class ClienteSetorOrigemPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteSetorOrigem();
	}

    private static ClienteSetorOrigemPdbxDao instance;

    public ClienteSetorOrigemPdbxDao() {
        super(ClienteSetorOrigem.TABLE_NAME);
    }

    public static ClienteSetorOrigemPdbxDao getInstance() {
        if (instance == null) {
            instance = new ClienteSetorOrigemPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ClienteSetorOrigem clientesetororigem = new ClienteSetorOrigem();
        clientesetororigem.rowKey = rs.getString("rowKey");
        clientesetororigem.cdEmpresa = rs.getString("cdEmpresa");
        clientesetororigem.cdRepresentante = rs.getString("cdRepresentante");
        clientesetororigem.cdTipoCliRede = rs.getString("cdTipoCliRede");
        clientesetororigem.cdCliRede = rs.getString("cdCliRede");
        clientesetororigem.cdSetor = rs.getString("cdSetor");
        clientesetororigem.cdOrigemSetor = rs.getString("cdOrigemSetor");
        clientesetororigem.dsSetor = rs.getString("dsSetor");
        clientesetororigem.dsOrigemSetor = rs.getString("dsOrigemSetor");
        clientesetororigem.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        clientesetororigem.nuCarimbo = rs.getInt("nuCarimbo");
        clientesetororigem.flTipoAlteracao = rs.getString("flTipoAlteracao");
        clientesetororigem.cdUsuario = rs.getString("cdUsuario");
        clientesetororigem.isSetorOrigem = true;
        return clientesetororigem;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" ROWKEY,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTIPOCLIREDE,");
        sql.append(" CDCLIREDE,");
        sql.append(" CDSETOR,");
        sql.append(" CDORIGEMSETOR,");
        sql.append(" DSSETOR,");
        sql.append(" DSORIGEMSETOR,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	ClienteSetorOrigem clientesetororigem = (ClienteSetorOrigem) domain;
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("CDEMPRESA = ", clientesetororigem.cdEmpresa);
    	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", clientesetororigem.cdRepresentante);
    	sqlWhereClause.addAndCondition("CDTIPOCLIREDE = ", clientesetororigem.cdTipoCliRede);
    	sqlWhereClause.addAndCondition("CDCLIREDE = ", clientesetororigem.cdCliRede);
    	sqlWhereClause.addAndCondition("CDSETOR = ", clientesetororigem.cdSetor);
    	sqlWhereClause.addAndCondition("CDORIGEMSETOR = ", clientesetororigem.cdOrigemSetor);
    	//--
    	sql.append(sqlWhereClause.getSql());
    }

    public Vector findAllByExampleSummary(BaseDomain domain) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectSummaryColumns(domain, sql);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addWhereByExampleSummary(domain, sql);
        addOrderBy(sql, domain);
        return findAllSummary(domain, sql.toString());
    }

    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ClienteSetorOrigem clientesetororigem = new ClienteSetorOrigem();
        clientesetororigem.rowKey = rs.getString("rowKey");
        clientesetororigem.cdEmpresa = rs.getString("cdEmpresa");
        clientesetororigem.cdRepresentante = rs.getString("cdRepresentante");
        clientesetororigem.cdTipoCliRede = rs.getString("cdTipoCliRede");
        clientesetororigem.cdCliRede = rs.getString("cdCliRede");
        clientesetororigem.cdSetor = rs.getString("cdSetor");
        clientesetororigem.cdOrigemSetor = rs.getString("cdOrigemSetor");
        clientesetororigem.dsSetor = rs.getString("dsSetor");
        clientesetororigem.dsOrigemSetor = rs.getString("dsOrigemSetor");
        clientesetororigem.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        clientesetororigem.nuCarimbo = rs.getInt("nuCarimbo");
        clientesetororigem.flTipoAlteracao = rs.getString("flTipoAlteracao");
        clientesetororigem.cdUsuario = rs.getString("cdUsuario");
        return clientesetororigem;
    }

    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
        sql.append(" ROWKEY,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTIPOCLIREDE,");
        sql.append(" CDCLIREDE,");
        sql.append(" CDSETOR,");
        sql.append(" CDORIGEMSETOR,");
        sql.append(" DSSETOR,");
        sql.append(" DSORIGEMSETOR,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }


    protected void addWhereByExampleSummary(BaseDomain domain, StringBuffer sql) {
    	ClienteSetorOrigem clientesetororigem = (ClienteSetorOrigem) domain;
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("CDEMPRESA = ", clientesetororigem.cdEmpresa);
    	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", clientesetororigem.cdRepresentante);
    	sqlWhereClause.addAndCondition("CDTIPOCLIREDE = ", clientesetororigem.cdTipoCliRede);
    	sqlWhereClause.addAndCondition("CDCLIREDE = ", clientesetororigem.cdCliRede);
    	sqlWhereClause.addAndCondition("CDSETOR = ", clientesetororigem.cdSetor);
    	sqlWhereClause.addAndCondition("CDSETOR != ", ClienteSetorOrigem.CLIENTE_SETOR_CDSETOR_VAZIO);
    	sqlWhereClause.addAndCondition("CDORIGEMSETOR = ", clientesetororigem.cdOrigemSetor);
    	//--
    	sql.append(sqlWhereClause.getSql());
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

}