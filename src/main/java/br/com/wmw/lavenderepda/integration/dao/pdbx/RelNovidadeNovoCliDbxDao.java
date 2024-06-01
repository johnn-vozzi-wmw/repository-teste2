package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeNovoCli;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RelNovidadeNovoCliDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RelNovidadeNovoCli();
	}

    private static RelNovidadeNovoCliDbxDao instance;

    public RelNovidadeNovoCliDbxDao() {
        super(RelNovidadeNovoCli.TABLE_NAME); 
    }
    
    public static RelNovidadeNovoCliDbxDao getInstance() {
        if (instance == null) {
            instance = new RelNovidadeNovoCliDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        RelNovidadeNovoCli relNovidadeNovoCli = new RelNovidadeNovoCli();
        relNovidadeNovoCli.rowKey = rs.getString("rowkey");
        relNovidadeNovoCli.cdEmpresa = rs.getString("cdEmpresa");
        relNovidadeNovoCli.cdRepresentante = rs.getString("cdRepresentante");
        relNovidadeNovoCli.cdNovoCliente = rs.getString("cdNovoCliente");
        relNovidadeNovoCli.cdTipoNovidade = rs.getString("cdTiponovidade");
        relNovidadeNovoCli.nmRazaoSocial = rs.getString("nmRazaoSocial");
        relNovidadeNovoCli.nuCnpj = rs.getString("nuCnpj");
        relNovidadeNovoCli.dsMensagem = rs.getString("dsMensagem");
        relNovidadeNovoCli.dtGeracao = rs.getDate("dtGeracao");
        relNovidadeNovoCli.cdUsuario = rs.getString("cdUsuario");
        relNovidadeNovoCli.nuCarimbo = rs.getInt("nuCarimbo");
        relNovidadeNovoCli.flTipoAlteracao = rs.getString("flTipoAlteracao");
        relNovidadeNovoCli.dtAlteracao = rs.getDate("dtAlteracao");
        relNovidadeNovoCli.hrAlteracao = rs.getString("hrAlteracao");
        return relNovidadeNovoCli;
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
        sql.append(" CDNOVOCLIENTE,");
        sql.append(" CDTIPONOVIDADE,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" NUCNPJ,");
        sql.append(" DSMENSAGEM,");
        sql.append(" DTGERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDNOVOCLIENTE,");
        sql.append(" CDTIPONOVIDADE,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" NUCNPJ,");
        sql.append(" DSMENSAGEM,");
        sql.append(" DTGERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO" );
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        RelNovidadeNovoCli relNovidadeNovoCli = (RelNovidadeNovoCli) domain;
        sql.append(Sql.getValue(relNovidadeNovoCli.cdEmpresa)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.cdRepresentante)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.cdNovoCliente)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.cdTipoNovidade)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.nmRazaoSocial)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.nuCnpj)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.dsMensagem)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.dtGeracao)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.cdUsuario)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.nuCarimbo)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.dtAlteracao)).append(",");
        sql.append(Sql.getValue(relNovidadeNovoCli.hrAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        RelNovidadeNovoCli relNovidadeNovoCli = (RelNovidadeNovoCli) domain;
        sql.append(" NMRAZAOSOCIAL = ").append(Sql.getValue(relNovidadeNovoCli.nmRazaoSocial)).append(",");
        sql.append(" NUCNPJ = ").append(Sql.getValue(relNovidadeNovoCli.nuCnpj)).append(",");
        sql.append(" DSMENSAGEM = ").append(Sql.getValue(relNovidadeNovoCli.dsMensagem)).append(",");
        sql.append(" DTGERACAO = ").append(Sql.getValue(relNovidadeNovoCli.dtGeracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(relNovidadeNovoCli.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(relNovidadeNovoCli.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(relNovidadeNovoCli.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(relNovidadeNovoCli.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(relNovidadeNovoCli.hrAlteracao));
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
	        	RelNovidadeNovoCli relNovidadeNovoCli = new RelNovidadeNovoCli();
	        	relNovidadeNovoCli.cdTipoNovidade = rs.getString("cdtiponovidade");
	        	relNovidadeNovoCli.qtRegistrosTipoNovidade = rs.getInt("qtRegistro");
	            vetor.addElement(relNovidadeNovoCli);
	        }
	        return vetor;
        }
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        RelNovidadeNovoCli relNovidadeNovoCli = (RelNovidadeNovoCli) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", relNovidadeNovoCli.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", relNovidadeNovoCli.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTIPONOVIDADE = ", relNovidadeNovoCli.cdTipoNovidade);
		sqlWhereClause.addAndCondition("DTGERACAO > ", relNovidadeNovoCli.dtGeracao);
		
		sqlWhereClause.addStartAndMultipleCondition();
       	boolean adicionouInicioBloco = false;
       	adicionouInicioBloco |= sqlWhereClause.addAndLikeCondition("NMRAZAOSOCIAL", relNovidadeNovoCli.nmRazaoSocial, false);
       	adicionouInicioBloco |= sqlWhereClause.addOrLikeCondition("CDNOVOCLIENTE", relNovidadeNovoCli.cdNovoCliente, false);
       	if (adicionouInicioBloco) {
   			sqlWhereClause.addEndMultipleCondition();
   		} else {
   			sqlWhereClause.removeStartMultipleCondition();
   		}
		sql.append(sqlWhereClause.getSql());
    }
    
}