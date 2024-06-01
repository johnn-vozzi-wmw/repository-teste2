package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ContratoProdEst;
import totalcross.sql.ResultSet;

public class ContratoProdEstDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ContratoProdEst();
	}

    private static ContratoProdEstDbxDao instance;

    public ContratoProdEstDbxDao() {
        super(ContratoProdEst.TABLE_NAME);
    }

    public static ContratoProdEstDbxDao getInstance() {
        if (instance == null) {
            instance = new ContratoProdEstDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ContratoProdEst contratoProdEst = new ContratoProdEst();
        contratoProdEst.rowKey = rs.getString("rowkey");
        contratoProdEst.cdEmpresa = rs.getString("cdEmpresa");
        contratoProdEst.cdRepresentante = rs.getString("cdRepresentante");
        contratoProdEst.cdCliente = rs.getString("cdCliente");
        contratoProdEst.flTipoContrato = rs.getString("flTipoContrato");
        contratoProdEst.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
        contratoProdEst.cdProduto = rs.getString("cdProduto");
        contratoProdEst.dtCadastro = rs.getDate("dtCadastro");
        contratoProdEst.qtEstoqueAtual = ValueUtil.round(rs.getDouble("qtEstoqueAtual"));
        contratoProdEst.flTipoAlteracao = rs.getString("flTipoAlteracao");
        contratoProdEst.nuCarimbo = rs.getInt("nuCarimbo");
        contratoProdEst.cdUsuario = rs.getString("cdUsuario");
        return contratoProdEst;
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
        sql.append(" CDPRODUTO,");
        sql.append(" DTCADASTRO,");
        sql.append(" QTESTOQUEATUAL,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" nuCarimbo,");
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
        sql.append(" CDPRODUTO,");
        sql.append(" DTCADASTRO,");
        sql.append(" QTESTOQUEATUAL,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" nuCarimbo,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContratoProdEst contratoProdEst = (ContratoProdEst) domain;
        sql.append(Sql.getValue(contratoProdEst.cdEmpresa)).append(",");
        sql.append(Sql.getValue(contratoProdEst.cdRepresentante)).append(",");
        sql.append(Sql.getValue(contratoProdEst.cdCliente)).append(",");
        sql.append(Sql.getValue(contratoProdEst.flTipoContrato)).append(",");
        sql.append(Sql.getValue(contratoProdEst.dtVigenciaInicial)).append(",");
        sql.append(Sql.getValue(contratoProdEst.cdProduto)).append(",");
        sql.append(Sql.getValue(contratoProdEst.dtCadastro)).append(",");
        sql.append(Sql.getValue(contratoProdEst.qtEstoqueAtual)).append(",");
        sql.append(Sql.getValue(contratoProdEst.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(contratoProdEst.nuCarimbo)).append(",");
        sql.append(Sql.getValue(contratoProdEst.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContratoProdEst contratoProdEst = (ContratoProdEst) domain;
        sql.append(" QTESTOQUEATUAL = ").append(Sql.getValue(contratoProdEst.qtEstoqueAtual)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(contratoProdEst.flTipoAlteracao)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(contratoProdEst.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(contratoProdEst.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ContratoProdEst contratoProdEst = (ContratoProdEst) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", contratoProdEst.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", contratoProdEst.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", contratoProdEst.cdCliente);
		sqlWhereClause.addAndCondition("FLTIPOCONTRATO = ", contratoProdEst.flTipoContrato);
		sqlWhereClause.addAndCondition("DTVIGENCIAINICIAL = ", contratoProdEst.dtVigenciaInicial);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", contratoProdEst.cdProduto);
		sqlWhereClause.addAndCondition("DTCADASTRO = ", contratoProdEst.dtCadastro);
		//--
		sql.append(sqlWhereClause.getSql());
    }
}