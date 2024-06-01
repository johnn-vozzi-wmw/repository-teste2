package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.RentabilidadeFaixa;
import totalcross.sql.ResultSet;

public class RentabilidadeFaixaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RentabilidadeFaixa();
	}

    private static RentabilidadeFaixaDbxDao instance;
	
    public RentabilidadeFaixaDbxDao() {
        super(RentabilidadeFaixa.TABLE_NAME);
    }
    
    public static RentabilidadeFaixaDbxDao getInstance() {
        if (instance == null) {
            instance = new RentabilidadeFaixaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        RentabilidadeFaixa rentabilidadeFaixa = new RentabilidadeFaixa();
        rentabilidadeFaixa.rowKey = rs.getString("rowkey");
        rentabilidadeFaixa.cdEmpresa = rs.getString("cdEmpresa");
        rentabilidadeFaixa.vlPctRentabilidade = ValueUtil.round(rs.getDouble("vlPctRentabilidade"));
        rentabilidadeFaixa.dsFaixa = rs.getString("dsFaixa");
        rentabilidadeFaixa.flFaixaMinima = rs.getString("flFaixaMinima");
        rentabilidadeFaixa.flFaixaIdeal = rs.getString("flFaixaIdeal");
        rentabilidadeFaixa.vlR = rs.getInt("vlR");
        rentabilidadeFaixa.vlG = rs.getInt("vlG");
        rentabilidadeFaixa.vlB = rs.getInt("vlB");
        rentabilidadeFaixa.flTipoAlteracao = rs.getString("flTipoAlteracao");
        rentabilidadeFaixa.nuCarimbo = rs.getInt("nuCarimbo");
        rentabilidadeFaixa.cdUsuario = rs.getString("cdUsuario");
        return rentabilidadeFaixa;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" VLPCTRENTABILIDADE,");
        sql.append(" DSFAIXA,");
        sql.append(" FLFAIXAMINIMA,");
        sql.append(" FLFAIXAIDEAL,");
        sql.append(" VLR,");
        sql.append(" VLG,");
        sql.append(" VLB,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" VLPCTRENTABILIDADE,");
        sql.append(" DSFAIXA,");
        sql.append(" FLFAIXAMINIMA,");
        sql.append(" FLFAIXAIDEAL,");
        sql.append(" VLR,");
        sql.append(" VLG,");
        sql.append(" VLB,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RentabilidadeFaixa rentabilidadeFaixa = (RentabilidadeFaixa) domain;
        sql.append(Sql.getValue(rentabilidadeFaixa.cdEmpresa)).append(",");
        sql.append(Sql.getValue(rentabilidadeFaixa.vlPctRentabilidade)).append(",");
        sql.append(Sql.getValue(rentabilidadeFaixa.dsFaixa)).append(",");
        sql.append(Sql.getValue(rentabilidadeFaixa.flFaixaMinima)).append(",");
        sql.append(Sql.getValue(rentabilidadeFaixa.flFaixaIdeal)).append(",");
        sql.append(Sql.getValue(rentabilidadeFaixa.vlR)).append(",");
        sql.append(Sql.getValue(rentabilidadeFaixa.vlG)).append(",");
        sql.append(Sql.getValue(rentabilidadeFaixa.vlB)).append(",");
        sql.append(Sql.getValue(rentabilidadeFaixa.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(rentabilidadeFaixa.nuCarimbo)).append(",");
        sql.append(Sql.getValue(rentabilidadeFaixa.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RentabilidadeFaixa rentabilidadeFaixa = (RentabilidadeFaixa) domain;
        sql.append(" DSFAIXA = ").append(Sql.getValue(rentabilidadeFaixa.dsFaixa)).append(",");
        sql.append(" FLFAIXAMINIMA = ").append(Sql.getValue(rentabilidadeFaixa.flFaixaMinima)).append(",");
        sql.append(" FLFAIXAIDEAL = ").append(Sql.getValue(rentabilidadeFaixa.flFaixaIdeal)).append(",");
        sql.append(" VLR = ").append(Sql.getValue(rentabilidadeFaixa.vlR)).append(",");
        sql.append(" VLG = ").append(Sql.getValue(rentabilidadeFaixa.vlG)).append(",");
        sql.append(" VLB = ").append(Sql.getValue(rentabilidadeFaixa.vlB)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(rentabilidadeFaixa.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(rentabilidadeFaixa.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(rentabilidadeFaixa.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        RentabilidadeFaixa rentabilidadeFaixa = (RentabilidadeFaixa) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", rentabilidadeFaixa.cdEmpresa);
		sqlWhereClause.addAndCondition("VLPCTRENTABILIDADE = ", rentabilidadeFaixa.vlPctRentabilidade);
		sqlWhereClause.addAndCondition("FLFAIXAMINIMA = ", rentabilidadeFaixa.flFaixaMinima);
		sqlWhereClause.addAndCondition("FLFAIXAIDEAL = ", rentabilidadeFaixa.flFaixaIdeal);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}