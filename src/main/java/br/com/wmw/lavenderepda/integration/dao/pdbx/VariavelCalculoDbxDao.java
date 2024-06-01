package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.VariavelCalculo;
import totalcross.sql.ResultSet;

public class VariavelCalculoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VariavelCalculo();
	}

    private static VariavelCalculoDbxDao instance;

    public VariavelCalculoDbxDao() {
        super(VariavelCalculo.TABLE_NAME); 
    }
    
    public static VariavelCalculoDbxDao getInstance() {
        if (instance == null) {
            instance = new VariavelCalculoDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        VariavelCalculo variavelCalculo = new VariavelCalculo();
        variavelCalculo.rowKey = rs.getString("rowkey");
        variavelCalculo.cdEmpresa = rs.getString("cdEmpresa");
        variavelCalculo.cdRepresentante = rs.getString("cdRepresentante");
        variavelCalculo.cdEntidade = rs.getString("cdEntidade");
        variavelCalculo.pkEntidade = rs.getString("pkEntidade");
        variavelCalculo.cdVariavel = rs.getString("cdVariavel");
        variavelCalculo.dsVariavel = rs.getString("dsVariavel");
        variavelCalculo.dsValorVariavel = rs.getString("dsValorVariavel");
        variavelCalculo.cdTipoValorVariavel = rs.getString("cdTipoValorVariavel");
        variavelCalculo.cdCliente = rs.getString("cdCliente");
        variavelCalculo.cdProduto = rs.getString("cdProduto");
        variavelCalculo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        variavelCalculo.nuCarimbo = rs.getInt("nuCarimbo");
        variavelCalculo.cdUsuario = rs.getString("cdUsuario");
        return variavelCalculo;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDENTIDADE,");
        sql.append(" PKENTIDADE,");
        sql.append(" CDVARIAVEL,");
        sql.append(" DSVARIAVEL,");
        sql.append(" DSVALORVARIAVEL,");
        sql.append(" CDTIPOVALORVARIAVEL,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDENTIDADE,");
        sql.append(" PKENTIDADE,");
        sql.append(" CDVARIAVEL,");
        sql.append(" DSVARIAVEL,");
        sql.append(" DSVALORVARIAVEL,");
        sql.append(" CDTIPOVALORVARIAVEL,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        VariavelCalculo variavelCalculo = (VariavelCalculo) domain;
        sql.append(Sql.getValue(variavelCalculo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(variavelCalculo.cdRepresentante)).append(",");
        sql.append(Sql.getValue(variavelCalculo.cdEntidade)).append(",");
        sql.append(Sql.getValue(variavelCalculo.pkEntidade)).append(",");
        sql.append(Sql.getValue(variavelCalculo.cdVariavel)).append(",");
        sql.append(Sql.getValue(variavelCalculo.dsVariavel)).append(",");
        sql.append(Sql.getValue(variavelCalculo.dsValorVariavel)).append(",");
        sql.append(Sql.getValue(variavelCalculo.cdTipoValorVariavel)).append(",");
        sql.append(Sql.getValue(variavelCalculo.cdCliente)).append(",");
        sql.append(Sql.getValue(variavelCalculo.cdProduto)).append(",");
        sql.append(Sql.getValue(variavelCalculo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(variavelCalculo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(variavelCalculo.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        VariavelCalculo variavelCalculo = (VariavelCalculo) domain;
        sql.append(" DSVARIAVEL = ").append(Sql.getValue(variavelCalculo.dsVariavel)).append(",");
        sql.append(" DSVALORVARIAVEL = ").append(Sql.getValue(variavelCalculo.dsValorVariavel)).append(",");
        sql.append(" CDTIPOVALORVARIAVEL = ").append(Sql.getValue(variavelCalculo.cdTipoValorVariavel)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(variavelCalculo.cdCliente)).append(",");
        sql.append(" CDPRODUTO = ").append(Sql.getValue(variavelCalculo.cdProduto)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(variavelCalculo.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(variavelCalculo.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(variavelCalculo.cdUsuario));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        VariavelCalculo variavelCalculo = (VariavelCalculo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", variavelCalculo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", variavelCalculo.cdRepresentante);
		sqlWhereClause.addAndCondition("CDENTIDADE = ", variavelCalculo.cdEntidade);
		sqlWhereClause.addAndCondition("PKENTIDADE = ", variavelCalculo.pkEntidade);
		sqlWhereClause.addAndCondition("CDVARIAVEL = ", variavelCalculo.cdVariavel);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", variavelCalculo.cdCliente);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", variavelCalculo.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}