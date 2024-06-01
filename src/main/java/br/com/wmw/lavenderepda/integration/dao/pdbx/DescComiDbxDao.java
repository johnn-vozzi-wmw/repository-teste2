package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.DescComi;
import totalcross.sql.ResultSet;

public class DescComiDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DescComi();
	}

    private static DescComiDbxDao instance;

    public DescComiDbxDao() {
        super(DescComi.TABLE_NAME);
    }

    public static DescComiDbxDao getInstance() {
        if (instance == null) {
            instance = new DescComiDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        DescComi descComi = new DescComi();
        descComi.rowKey = rs.getString("rowkey");
        descComi.cdEmpresa = rs.getString("cdEmpresa");
        descComi.cdDescComi = rs.getString("cdDescComi");
        descComi.cdProduto = rs.getString("cdProduto");
        descComi.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        descComi.cdGrupoProduto2 = rs.getString("cdGrupoProduto2");
        descComi.cdGrupoProduto3 = rs.getString("cdGrupoProduto3");
        descComi.cdCliente = rs.getString("cdCliente");
        descComi.cdRepresentante = rs.getString("cdRepresentante");
        descComi.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        descComi.cdRamoAtividade = rs.getString("cdRamoAtividade");
        descComi.cdCidade = rs.getString("cdCidade");
        descComi.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
        descComi.dtVigenciaFinal = rs.getDate("dtVigenciaFinal");
        descComi.cdTipoDesCcomi = rs.getString("cdTipoDesCcomi");
        descComi.nuCarimbo = rs.getInt("nuCarimbo");
        descComi.flTipoAlteracao = rs.getString("flTipoAlteracao");
        descComi.cdUsuario = rs.getString("cdUsuario");
        return descComi;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDDESCCOMI,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDGRUPOPRODUTO3,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDRAMOATIVIDADE,");
        sql.append(" CDCIDADE,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" CDTIPODESCCOMI,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDDESCCOMI,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" CDGRUPOPRODUTO2,");
        sql.append(" CDGRUPOPRODUTO3,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" CDRAMOATIVIDADE,");
        sql.append(" CDCIDADE,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" CDTIPODESCCOMI,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescComi descComi = (DescComi) domain;
        sql.append(Sql.getValue(descComi.cdEmpresa)).append(",");
        sql.append(Sql.getValue(descComi.cdDescComi)).append(",");
        sql.append(Sql.getValue(descComi.cdProduto)).append(",");
        sql.append(Sql.getValue(descComi.cdGrupoProduto1)).append(",");
        sql.append(Sql.getValue(descComi.cdGrupoProduto2)).append(",");
        sql.append(Sql.getValue(descComi.cdGrupoProduto3)).append(",");
        sql.append(Sql.getValue(descComi.cdCliente)).append(",");
        sql.append(Sql.getValue(descComi.cdRepresentante)).append(",");
        sql.append(Sql.getValue(descComi.cdCondicaoPagamento)).append(",");
        sql.append(Sql.getValue(descComi.cdRamoAtividade)).append(",");
        sql.append(Sql.getValue(descComi.cdCidade)).append(",");
        sql.append(Sql.getValue(descComi.dtVigenciaInicial)).append(",");
        sql.append(Sql.getValue(descComi.dtVigenciaFinal)).append(",");
        sql.append(Sql.getValue(descComi.cdTipoDesCcomi)).append(",");
        sql.append(Sql.getValue(descComi.nuCarimbo)).append(",");
        sql.append(Sql.getValue(descComi.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(descComi.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescComi descComi = (DescComi) domain;
        sql.append(" CDPRODUTO = ").append(Sql.getValue(descComi.cdProduto)).append(",");
        sql.append(" CDGRUPOPRODUTO1 = ").append(Sql.getValue(descComi.cdGrupoProduto1)).append(",");
        sql.append(" CDGRUPOPRODUTO2 = ").append(Sql.getValue(descComi.cdGrupoProduto2)).append(",");
        sql.append(" CDGRUPOPRODUTO3 = ").append(Sql.getValue(descComi.cdGrupoProduto3)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(descComi.cdCliente)).append(",");
        sql.append(" CDREPRESENTANTE = ").append(Sql.getValue(descComi.cdRepresentante)).append(",");
        sql.append(" CDCONDICAOPAGAMENTO = ").append(Sql.getValue(descComi.cdCondicaoPagamento)).append(",");
        sql.append(" CDRAMOATIVIDADE = ").append(Sql.getValue(descComi.cdRamoAtividade)).append(",");
        sql.append(" CDCIDADE = ").append(Sql.getValue(descComi.cdCidade)).append(",");
        sql.append(" DTVIGENCIAINICIAL = ").append(Sql.getValue(descComi.dtVigenciaInicial)).append(",");
        sql.append(" DTVIGENCIAFINAL = ").append(Sql.getValue(descComi.dtVigenciaFinal)).append(",");
        sql.append(" CDTIPODESCCOMI = ").append(Sql.getValue(descComi.cdTipoDesCcomi)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(descComi.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(descComi.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(descComi.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        DescComi descComi = (DescComi) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", descComi.cdEmpresa);
		sqlWhereClause.addAndCondition("CDDESCCOMI = ", descComi.cdDescComi);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", descComi.cdProduto);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", descComi.cdGrupoProduto1);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ", descComi.cdGrupoProduto2);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO3 = ", descComi.cdGrupoProduto3);
		sqlWhereClause.addAndCondition("CDTIPODESCCOMI = ", descComi.cdTipoDesCcomi);
   		sqlWhereClause.addAndCondition("DTVIGENCIAINICIAL <= ", descComi.dtVigenciaInicial);
   		sqlWhereClause.addAndCondition("DTVIGENCIAFINAL >= ", descComi.dtVigenciaFinal);
   		if (descComi.filterCdGrupoProduto2Null) {
   			sqlWhereClause.addAndCondition("CDGRUPOPRODUTO2 = ''");
   		}
   		if (descComi.filterCdGrupoProduto3Null) {
   			sqlWhereClause.addAndCondition("CDGRUPOPRODUTO3 = ''");
   		}
		//--
		sql.append(sqlWhereClause.getSql());
    }

}