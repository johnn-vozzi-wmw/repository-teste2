package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FaixaBoleto;
import totalcross.sql.ResultSet;
import totalcross.sys.InvalidNumberException;
import totalcross.util.BigDecimal;

public class FaixaBoletoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FaixaBoleto();
	}

    private static FaixaBoletoDbxDao instance = null;
	

    public FaixaBoletoDbxDao() {
        super(FaixaBoleto.TABLE_NAME);
    }
    
    public static FaixaBoletoDbxDao getInstance() {
        if (instance == null) {
            instance = new FaixaBoletoDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        FaixaBoleto faixaBoleto = new FaixaBoleto();
        faixaBoleto.rowKey = rs.getString("rowkey");
        faixaBoleto.cdEmpresa = rs.getString("cdEmpresa");
        faixaBoleto.cdRepresentante = rs.getString("cdRepresentante");
        faixaBoleto.cdBoletoConfig = rs.getString("cdBoletoConfig");
        try {
        	faixaBoleto.nuBoletoInicio = new BigDecimal(rs.getString("nuBoletoInicio"));
        	faixaBoleto.nuBoletoFim = new BigDecimal(rs.getString("nuBoletoFim"));
        	String nuUltimoBoleto = rs.getString("nuUltimoBoleto");
        	faixaBoleto.nuUltimoBoleto =  ValueUtil.isNotEmpty(nuUltimoBoleto) ? new BigDecimal(nuUltimoBoleto) : null;
        } catch (InvalidNumberException e) {
        }
        faixaBoleto.cdUsuario = rs.getString("cdUsuario");
        faixaBoleto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        faixaBoleto.nuCarimbo = rs.getInt("nuCarimbo");
        return faixaBoleto;
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
        sql.append(" CDBOLETOCONFIG,");
        sql.append(" NUBOLETOINICIO,");
        sql.append(" NUBOLETOFIM,");
        sql.append(" NUULTIMOBOLETO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDBOLETOCONFIG,");
        sql.append(" NUBOLETOINICIO,");
        sql.append(" NUBOLETOFIM,");
        sql.append(" NUULTIMOBOLETO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        FaixaBoleto faixaBoleto = (FaixaBoleto) domain;
        sql.append(Sql.getValue(faixaBoleto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(faixaBoleto.cdRepresentante)).append(",");
        sql.append(Sql.getValue(faixaBoleto.cdBoletoConfig)).append(",");
        sql.append(Sql.getValue(faixaBoleto.nuBoletoInicio)).append(",");
        sql.append(Sql.getValue(faixaBoleto.nuBoletoFim)).append(",");
        sql.append(Sql.getValue(faixaBoleto.nuUltimoBoleto)).append(",");
        sql.append(Sql.getValue(faixaBoleto.cdUsuario)).append(",");
        sql.append(Sql.getValue(faixaBoleto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(faixaBoleto.nuCarimbo));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        FaixaBoleto faixaBoleto = (FaixaBoleto) domain;
        sql.append(" CDBOLETOCONFIG = ").append(Sql.getValue(faixaBoleto.cdBoletoConfig)).append(",");
        sql.append(" NUULTIMOBOLETO = ").append(Sql.getValue(faixaBoleto.nuUltimoBoleto)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(faixaBoleto.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(faixaBoleto.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(faixaBoleto.nuCarimbo));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        FaixaBoleto faixaBoleto = (FaixaBoleto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", faixaBoleto.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", faixaBoleto.cdRepresentante);
		sqlWhereClause.addAndCondition("CDBOLETOCONFIG = ", faixaBoleto.cdBoletoConfig);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	public void updateLastFaixaBoleto(FaixaBoleto faixaBoletoFilter) {
		try {
	        StringBuffer sql = getSqlBuffer();
	        sql.append("UPDATE TBLVPFAIXABOLETO SET NUULTIMOBOLETO = ");
			sql.append(Sql.getValue(faixaBoletoFilter.nuUltimoBoleto));
	        sql.append(" WHERE ");
	        sql.append(" CDEMPRESA = ").append(Sql.getValue(faixaBoletoFilter.cdEmpresa));
	        sql.append(" AND CDREPRESENTANTE = ").append(Sql.getValue(faixaBoletoFilter.cdRepresentante));
	        sql.append(" AND CDBOLETOCONFIG = ").append(Sql.getValue(faixaBoletoFilter.cdBoletoConfig));
	        sql.append(" AND NUULTIMOBOLETO < ").append(Sql.getValue(faixaBoletoFilter.nuUltimoBoleto));
	        sql.append(" AND ").append(Sql.getValue(faixaBoletoFilter.nuUltimoBoleto)).append(" BETWEEN NUBOLETOINICIO AND NUBOLETOFIM");
			executeUpdate(sql.toString());
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}  
	}
    
}