package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class CondicaoComercialPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondicaoComercial();
	}

    private static CondicaoComercialPdbxDao instance;

    public CondicaoComercialPdbxDao() {
        super(CondicaoComercial.TABLE_NAME);
    }

    public static CondicaoComercialPdbxDao getInstance() {
        if (instance == null) {
            instance = new CondicaoComercialPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondicaoComercial condicaoComercial = new CondicaoComercial();
        condicaoComercial.rowKey = rs.getString("rowkey");
        condicaoComercial.cdEmpresa = rs.getString("cdEmpresa");
        condicaoComercial.cdRepresentante = rs.getString("cdRepresentante");
        condicaoComercial.cdCondicaoComercial = rs.getString("cdCondicaoComercial");
        condicaoComercial.dsCondicaoComercial = rs.getString("dsCondicaoComercial");
        condicaoComercial.vlIndiceFinanceiro = rs.getDouble("vlIndiceFinanceiro");
        condicaoComercial.vlIndiceVerba = rs.getDouble("vlIndiceVerba");
        condicaoComercial.flIndiceVerbaSequencial = rs.getString("flIndiceVerbaSequencial");
        condicaoComercial.flAcessivelOutrasCond = rs.getString("flAcessivelOutrasCond");
        condicaoComercial.flAcessaOutrasCond = rs.getString("flAcessaOutrasCond");
        condicaoComercial.nuCarimbo = rs.getInt("nuCarimbo");
        if (LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial) {
        	condicaoComercial.cdTabelaPreco = rs.getString("cdTabelaPreco");
        }
        if (isFazJoinComCondComCliente(condicaoComercial) || isFazJoinCondComSegCli(condicaoComercial)) {
        	condicaoComercial.flDefault = rs.getString("flDefault");
        }
        condicaoComercial.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condicaoComercial.cdUsuario = rs.getString("cdUsuario");
        return condicaoComercial;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCONDICAOCOMERCIAL,");
        sql.append(" tb.DSCONDICAOCOMERCIAL,");
        sql.append(" tb.VLINDICEFINANCEIRO,");
        sql.append(" tb.VLINDICEVERBA,");
        sql.append(" tb.FLINDICEVERBASEQUENCIAL,");
        sql.append(" tb.FLACESSIVELOUTRASCOND,");
        sql.append(" tb.FLACESSAOUTRASCOND,");
        sql.append(" tb.NUCARIMBO,");
        if (LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial) {
        	sql.append(" tb.CDTABELAPRECO,");
        }
        sql.append(" tb.FLTIPOALTERACAO,");
        if (isFazJoinCondComSegCli((CondicaoComercial)domainFilter)) {
        	sql.append(" CONDCOMSEGCLI.FLDEFAULT FLDEFAULT,");
        } else if (isFazJoinComCondComCliente((CondicaoComercial)domainFilter)) {
        	sql.append(" CONDCOMCLI.FLDEFAULT FLDEFAULT,");
        }
        sql.append(" tb.CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" DSCONDICAOCOMERCIAL,");
        sql.append(" VLINDICEFINANCEIRO,");
        sql.append(" VLINDICEVERBA,");
        sql.append(" FLINDICEVERBASEQUENCIAL,");
        sql.append(" FLACESSIVELOUTRASCOND,");
        sql.append(" FLACESSAOUTRASCOND,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondicaoComercial condicaoComercial = (CondicaoComercial) domain;
        sql.append(Sql.getValue(condicaoComercial.cdEmpresa)).append(",");
        sql.append(Sql.getValue(condicaoComercial.cdRepresentante)).append(",");
        sql.append(Sql.getValue(condicaoComercial.cdCondicaoComercial)).append(",");
        sql.append(Sql.getValue(condicaoComercial.dsCondicaoComercial)).append(",");
        sql.append(Sql.getValue(condicaoComercial.vlIndiceFinanceiro)).append(",");
        sql.append(Sql.getValue(condicaoComercial.vlIndiceVerba)).append(",");
        sql.append(Sql.getValue(condicaoComercial.flIndiceVerbaSequencial)).append(",");
        sql.append(Sql.getValue(condicaoComercial.flAcessivelOutrasCond)).append(",");
        sql.append(Sql.getValue(condicaoComercial.flAcessaOutrasCond)).append(",");
        sql.append(Sql.getValue(condicaoComercial.nuCarimbo)).append(",");
        sql.append(Sql.getValue(condicaoComercial.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(condicaoComercial.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondicaoComercial condicaoComercial = (CondicaoComercial) domain;
        sql.append(" DSCONDICAOCOMERCIAL = ").append(Sql.getValue(condicaoComercial.dsCondicaoComercial)).append(",");
        sql.append(" FLACESSIVELOUTRASCOND = ").append(Sql.getValue(condicaoComercial.flAcessivelOutrasCond)).append(",");
        sql.append(" FLACESSAOUTRASCOND = ").append(Sql.getValue(condicaoComercial.flAcessaOutrasCond)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(condicaoComercial.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(condicaoComercial.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(condicaoComercial.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondicaoComercial condicaoComercial = (CondicaoComercial) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", condicaoComercial.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", condicaoComercial.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCONDICAOCOMERCIAL = ", condicaoComercial.cdCondicaoComercial);
		if (condicaoComercial.flCondicaoComercialParaItensPedido && ValueUtil.isNotEmpty(condicaoComercial.cdCondicaoComercialPedido)) {
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addAndCondition("COALESCE(tb.FLACESSIVELOUTRASCOND," + Sql.getValue(ValueUtil.VALOR_SIM) + ") != " + Sql.getValue(ValueUtil.VALOR_NAO));
			sqlWhereClause.addOrCondition("tb.CDCONDICAOCOMERCIAL = " + Sql.getValue(condicaoComercial.cdCondicaoComercialPedido));
			sqlWhereClause.addEndMultipleCondition();
		}
		//--
		sql.append(sqlWhereClause.getSql());
		if (LavenderePdaConfig.usaCondComercialPorCondPagto && condicaoComercial.condComCondPagtoFilter != null) {
			DaoUtil.addExistsCondComCondPagto(sql, condicaoComercial.condComCondPagtoFilter, false);
		}
		if (LavenderePdaConfig.isUsaCondicaoComercialPorCliente() && condicaoComercial.condComClienteFilter != null && condicaoComercial.condComClienteFilter.comparaClientesRepresentante) {
			DaoUtil.addExistsClienteCondComCli(sql, condicaoComercial.condComClienteFilter);
		}
    }
    
    public double findIndiceCondicaoComercial(CondicaoComercial filter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
        sql.append(" Select VLINDICEFINANCEIRO from TBLVPCONDICAOCOMERCIAL com ");
		sql.append(" inner join TBLVPCONDCOMCLIENTE comcli on (comcli.cdEmpresa = com.cdEmpresa ");
		sql.append("			AND comcli.cdCliente = ").append(Sql.getValue(filter.cdCliente));
		sql.append("			AND comcli.cdRepresentante = com.cdRepresentante) ");
		sql.append(" WHERE com.cdEmpresa = ").append(Sql.getValue(filter.cdEmpresa));
		sql.append(" AND com.cdCondicaoComercial = ").append(Sql.getValue(filter.cdCondicaoComercial));
		sql.append(" AND comcli.cdCondicaoComercial = ").append(Sql.getValue(filter.cdCondicaoComercial));
		sql.append(" AND com.cdRepresentante = ").append(Sql.getValue(filter.cdRepresentante));
        
		return getDouble(sql.toString());
	}
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	CondicaoComercial condComercial = (CondicaoComercial) domainFilter; 
    	if (isFazJoinComCondComCliente(condComercial)) {
    		DaoUtil.addJoinCondComCliente(sql, condComercial.condComClienteFilter);
    	}
    	if (isFazJoinCondComSegCli(condComercial)) {
    		DaoUtil.addJoinCondComSegCli(sql, condComercial.condComSegCliFilter);
    	}
    }
    
    protected boolean isFazJoinComCondComCliente(CondicaoComercial condComercial) {
    	return LavenderePdaConfig.isUsaCondicaoComercialPorCliente() && condComercial.condComClienteFilter != null && !condComercial.condComClienteFilter.comparaClientesRepresentante;
    }
    
    protected boolean isFazJoinCondComSegCli(CondicaoComercial condComercial) {
    	return LavenderePdaConfig.usaCondicaoComercialPorSegmentoECliente && condComercial.condComSegCliFilter != null;
    }

}