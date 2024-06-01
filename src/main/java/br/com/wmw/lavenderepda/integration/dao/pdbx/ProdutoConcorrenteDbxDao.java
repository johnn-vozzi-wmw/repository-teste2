package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.ProdutoConcorrente;
import totalcross.sql.ResultSet;

public class ProdutoConcorrenteDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ProdutoConcorrente();
	}

    private static ProdutoConcorrenteDbxDao instance;

    public ProdutoConcorrenteDbxDao() {
        super(ProdutoConcorrente.TABLE_NAME);
    }
    
    public static ProdutoConcorrenteDbxDao getInstance() {
        if (instance == null) {
            instance = new ProdutoConcorrenteDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ProdutoConcorrente produtoConcorrente = new ProdutoConcorrente();
        produtoConcorrente.rowKey = rs.getString("rowkey");
        produtoConcorrente.cdEmpresa = rs.getString("cdEmpresa");
        produtoConcorrente.cdRepresentante = rs.getString("cdRepresentante");
        produtoConcorrente.cdProdutoConcorrente = rs.getString("cdProdutoConcorrente");
        produtoConcorrente.dsProdutoConcorrente = rs.getString("dsProdutoConcorrente");
        produtoConcorrente.cdConcorrente = rs.getString("cdConcorrente");
        produtoConcorrente.cdUsuario = rs.getString("cdUsuario");
        produtoConcorrente.flTipoAlteracao = rs.getString("flTipoAlteracao");
        produtoConcorrente.nuCarimbo = rs.getInt("nuCarimbo");
        return produtoConcorrente;
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
        sql.append(" CDPRODUTOCONCORRENTE,");
        sql.append(" DSPRODUTOCONCORRENTE,");
        sql.append(" CDCONCORRENTE,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTOCONCORRENTE,");
        sql.append(" DSPRODUTOCONCORRENTE,");
        sql.append(" CDCONCORRENTE,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoConcorrente produtoConcorrente = (ProdutoConcorrente) domain;
        sql.append(Sql.getValue(produtoConcorrente.cdEmpresa)).append(",");
        sql.append(Sql.getValue(produtoConcorrente.cdRepresentante)).append(",");
        sql.append(Sql.getValue(produtoConcorrente.cdProdutoConcorrente)).append(",");
        sql.append(Sql.getValue(produtoConcorrente.dsProdutoConcorrente)).append(",");
        sql.append(Sql.getValue(produtoConcorrente.cdConcorrente)).append(",");
        sql.append(Sql.getValue(produtoConcorrente.cdUsuario)).append(",");
        sql.append(Sql.getValue(produtoConcorrente.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(produtoConcorrente.nuCarimbo));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoConcorrente produtoConcorrente = (ProdutoConcorrente) domain;
        sql.append(" DSPRODUTOCONCORRENTE = ").append(Sql.getValue(produtoConcorrente.dsProdutoConcorrente)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(produtoConcorrente.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(produtoConcorrente.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(produtoConcorrente.nuCarimbo));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ProdutoConcorrente produtoConcorrente = (ProdutoConcorrente) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", produtoConcorrente.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", produtoConcorrente.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCONCORRENTE = ", produtoConcorrente.cdConcorrente);
		//--
		sqlWhereClause.addStartAndMultipleCondition();
		boolean adicionouInicioBloco = false;
       	adicionouInicioBloco |= sqlWhereClause.addAndLikeCondition("DSPRODUTOCONCORRENTE", produtoConcorrente.dsProdutoConcorrente, true);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("CDPRODUTOCONCORRENTE", produtoConcorrente.cdProdutoConcorrente, true);
       	if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}