package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeCli;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RelNovidadeCliDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RelNovidadeCli();
	}

    private static RelNovidadeCliDao instance;

    public RelNovidadeCliDao() {
        super(RelNovidadeCli.TABLE_NAME);
    }
    
    public static RelNovidadeCliDao getInstance() {
        if (instance == null) {
            instance = new RelNovidadeCliDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        RelNovidadeCli relNovidadeCli = new RelNovidadeCli();
        relNovidadeCli.rowKey = rs.getString("rowkey");
        relNovidadeCli.cdEmpresa = rs.getString("cdEmpresa");
        relNovidadeCli.cdRepresentante = rs.getString("cdRepresentante");
        relNovidadeCli.cdCliente = rs.getString("cdCliente");
        relNovidadeCli.cdTipoNovidade = rs.getString("cdTiponovidade");
        relNovidadeCli.nmRazaoSocial = rs.getString("nmRazaoSocial");
        relNovidadeCli.dsNovidadeCliente = rs.getString("dsNovidadeCliente");
        relNovidadeCli.dtEmissaoRelatorio = rs.getDate("dtEmissaoRelatorio");
        relNovidadeCli.nuCarimbo = rs.getInt("nuCarimbo");
        relNovidadeCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        relNovidadeCli.cdUsuario = rs.getString("cdUsuario");
        relNovidadeCli.dtAlteracao = rs.getDate("dtAlteracao");
        relNovidadeCli.hrAlteracao = rs.getString("hrAlteracao");
        return relNovidadeCli;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDTIPONOVIDADE,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" DSNOVIDADECLIENTE,");
        sql.append(" DTEMISSAORELATORIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}
    
    public Vector getRegistrosPorTipoNovidade(BaseDomain domain) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select count(*) as qtRegistro, cdtiponovidade  from ");
    	sql.append(tableName);
    	addWhereByExample(domain, sql);
    	sql.append(" group by cdtiponovidade;");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
            Vector vetor = new Vector();
	        while (rs.next()) {
	        	RelNovidadeCli relNovidadeCli = new RelNovidadeCli();
	        	relNovidadeCli.cdTipoNovidade = rs.getString("cdtiponovidade");
	        	relNovidadeCli.qtRegistrosTipoNovidade = rs.getInt("qtRegistro");
	            vetor.addElement(relNovidadeCli);
	        }
	        return vetor;
        }
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        RelNovidadeCli relNovidadeCli = (RelNovidadeCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", relNovidadeCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", relNovidadeCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTIPONOVIDADE = ", relNovidadeCli.cdTipoNovidade);
		sqlWhereClause.addAndCondition("DTEMISSAORELATORIO > ", relNovidadeCli.dtEmissaoRelatorio);
		sqlWhereClause.addStartAndMultipleCondition();
       	boolean adicionouInicioBloco = false;
       	adicionouInicioBloco |= sqlWhereClause.addAndLikeCondition("NMRAZAOSOCIAL", relNovidadeCli.dsFiltro, false);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("CDCLIENTE", relNovidadeCli.dsFiltro, false);
       	if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
		sql.append(sqlWhereClause.getSql());
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
	}

}