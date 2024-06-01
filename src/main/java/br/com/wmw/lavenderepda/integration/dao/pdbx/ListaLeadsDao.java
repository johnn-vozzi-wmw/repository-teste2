package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ListaLeads;
import totalcross.sql.ResultSet;

public class ListaLeadsDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ListaLeads();
	}

    private static ListaLeadsDao instance;
	
    public ListaLeadsDao() {
        super(ListaLeads.TABLE_NAME);
    }
    
    public static ListaLeadsDao getInstance() {
        if (instance == null) {
            instance = new ListaLeadsDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ListaLeads listaLeads = new ListaLeads();
        listaLeads.rowKey = rs.getString("rowkey");
        listaLeads.cdEmpresa = rs.getString("cdEmpresa");
        listaLeads.cdRepresentante = rs.getString("cdRepresentante");
        listaLeads.cdLista = rs.getString("cdLista");
        listaLeads.nmLista = rs.getString("nmLista");
        listaLeads.flTpCadastro = rs.getString("flTipoCadastro");
        listaLeads.dtCriacao = rs.getDate("dtCriacao");
        listaLeads.flTipoAlteracao = rs.getString("flTipoAlteracao");
        listaLeads.nuCarimbo = rs.getInt("nuCarimbo");
        return listaLeads;
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
        sql.append(" CDLISTA,");
        sql.append(" NMLISTA,");
        sql.append(" FLTIPOCADASTRO,");
        sql.append(" DTCRIACAO,");
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
        sql.append(" CDLISTA,");
        sql.append(" NMLISTA,");
        sql.append(" FLTIPOCADASTRO,");
        sql.append(" DTCRIACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ListaLeads listaLeads = (ListaLeads) domain;
        sql.append(Sql.getValue(listaLeads.cdEmpresa)).append(",");
        sql.append(Sql.getValue(listaLeads.cdRepresentante)).append(",");
        sql.append(Sql.getValue(listaLeads.cdLista)).append(",");
        sql.append(Sql.getValue(listaLeads.nmLista)).append(",");
        sql.append(Sql.getValue(listaLeads.flTpCadastro)).append(",");
        sql.append(Sql.getValue(listaLeads.dtCriacao)).append(",");
        sql.append(Sql.getValue(listaLeads.cdUsuario)).append(",");
        sql.append(Sql.getValue(listaLeads.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ListaLeads listaLeads = (ListaLeads) domain;
        sql.append(" CDREPRESENTANTE = ").append(Sql.getValue(listaLeads.cdRepresentante)).append(",");
        sql.append(" CDLISTA = ").append(Sql.getValue(listaLeads.cdLista)).append(",");
        sql.append(" NMLISTA = ").append(Sql.getValue(listaLeads.nmLista)).append(",");
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ListaLeads listaLeads = (ListaLeads) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", listaLeads.cdEmpresa);
		sqlWhereClause.addAndCondition("CDLISTA = ", listaLeads.cdLista);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", listaLeads.cdRepresentante);
		sqlWhereClause.addAndCondition("NMLISTA = ", listaLeads.nmLista);
		sqlWhereClause.addAndCondition("FLTIPOCADASTRO = ", listaLeads.flTpCadastro);
		sqlWhereClause.addAndBetweenDateCondition("DTCRIACAO ", listaLeads.dtInicioFiltro, listaLeads.dtFimFiltro);
		sql.append(sqlWhereClause.getSql());
    	
		String dsFiltro = getDsFiltro(listaLeads.dsFiltro);
		if (ValueUtil.isNotEmpty(dsFiltro)) {
    		sql.append(" AND (");
    		sql.append("CDLISTA LIKE ").append(Sql.getValue(dsFiltro));
    		sql.append(" OR NMLISTA LIKE ").append(Sql.getValue(dsFiltro));
   			sql.append(")");
   		}
    }
    
    private String getDsFiltro(String dsFiltro) {
    	if (ValueUtil.isEmpty(dsFiltro)) {
    		return "";
    	}
    	boolean onlyStartString = false;
    	if (LavenderePdaConfig.usaPesquisaInicioString) {
    		if (dsFiltro.startsWith("*")) {
    			dsFiltro = dsFiltro.substring(1);
    		} else {
    			onlyStartString = true;
    		}
    	}
    	return onlyStartString ? dsFiltro + "%" : "%" + dsFiltro + "%";
    }
    
    
}