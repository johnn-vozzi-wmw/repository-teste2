package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ContratoCliente;
import totalcross.sql.ResultSet;

public class ContratoClienteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ContratoCliente();
	}

    private static ContratoClienteDbxDao instance;

    public ContratoClienteDbxDao() {
        super(ContratoCliente.TABLE_NAME);
    }

    public static ContratoClienteDbxDao getInstance() {
        if (instance == null) {
            instance = new ContratoClienteDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ContratoCliente contratoCliente = new ContratoCliente();
        contratoCliente.rowKey = rs.getString("rowkey");
        contratoCliente.cdEmpresa = rs.getString("cdEmpresa");
        contratoCliente.cdRepresentante = rs.getString("cdRepresentante");
        contratoCliente.cdCliente = rs.getString("cdCliente");
        contratoCliente.flTipoContrato = rs.getString("flTipoContrato");
        contratoCliente.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
        contratoCliente.dtVigenciaFinal = rs.getDate("dtVigenciaFinal");
        contratoCliente.nuDiasPeriodicidade = rs.getInt("nuDiasPeriodicidade");
        contratoCliente.cdTabelaPreco = rs.getString("cdTabelaPreco");
        contratoCliente.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        contratoCliente.nuCarimbo = rs.getInt("nuCarimbo");
        contratoCliente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        contratoCliente.cdUsuario = rs.getString("cdUsuario");
        return contratoCliente;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLTIPOCONTRATO,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" NUDIASPERIODICIDADE,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" FLTIPOCONTRATO,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" NUDIASPERIODICIDADE,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" nuCarimbo,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContratoCliente contratoCliente = (ContratoCliente) domain;
        sql.append(Sql.getValue(contratoCliente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(contratoCliente.cdRepresentante)).append(",");
        sql.append(Sql.getValue(contratoCliente.cdCliente)).append(",");
        sql.append(Sql.getValue(contratoCliente.flTipoContrato)).append(",");
        sql.append(Sql.getValue(contratoCliente.dtVigenciaInicial)).append(",");
        sql.append(Sql.getValue(contratoCliente.dtVigenciaFinal)).append(",");
        sql.append(Sql.getValue(contratoCliente.nuDiasPeriodicidade)).append(",");
        sql.append(Sql.getValue(contratoCliente.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(contratoCliente.cdCondicaoPagamento)).append(",");
        sql.append(Sql.getValue(contratoCliente.nuCarimbo)).append(",");
        sql.append(Sql.getValue(contratoCliente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(contratoCliente.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContratoCliente contratoCliente = (ContratoCliente) domain;
        sql.append(" DTVIGENCIAFINAL = ").append(Sql.getValue(contratoCliente.dtVigenciaFinal)).append(",");
        sql.append(" NUDIASPERIODICIDADE = ").append(Sql.getValue(contratoCliente.nuDiasPeriodicidade)).append(",");
        sql.append(" CDTABELAPRECO = ").append(Sql.getValue(contratoCliente.cdTabelaPreco)).append(",");
        sql.append(" CDCONDICAOPAGAMENTO = ").append(Sql.getValue(contratoCliente.cdCondicaoPagamento)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(contratoCliente.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(contratoCliente.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(contratoCliente.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContratoCliente contratoCliente = (ContratoCliente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", contratoCliente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", contratoCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", contratoCliente.cdCliente);
		sqlWhereClause.addAndCondition("FLTIPOCONTRATO = ", contratoCliente.flTipoContrato);
		sqlWhereClause.addAndCondition("DTVIGENCIAINICIAL = ", contratoCliente.dtVigenciaInicial);
		sqlWhereClause.addAndCondition("DTVIGENCIAINICIAL <= ", contratoCliente.dtVigenciaFilter);
		sqlWhereClause.addAndCondition("DTVIGENCIAFINAL >= ", contratoCliente.dtVigenciaFilter);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}