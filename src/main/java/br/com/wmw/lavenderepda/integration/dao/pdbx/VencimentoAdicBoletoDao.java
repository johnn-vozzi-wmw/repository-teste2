package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.VencimentoAdicBoleto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class VencimentoAdicBoletoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VencimentoAdicBoleto();
	}

    private static VencimentoAdicBoletoDao instance;
	

    public VencimentoAdicBoletoDao() {
        super(VencimentoAdicBoleto.TABLE_NAME);
    }
    
    public static VencimentoAdicBoletoDao getInstance() {
        if (instance == null) {
            instance = new VencimentoAdicBoletoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        VencimentoAdicBoleto vencimentoAdicBoleto = new VencimentoAdicBoleto();
        vencimentoAdicBoleto.rowKey = rs.getString("rowkey");
        vencimentoAdicBoleto.cdEmpresa = rs.getString("cdEmpresa");
        vencimentoAdicBoleto.cdRepresentante = rs.getString("cdRepresentante");
        vencimentoAdicBoleto.cdVencimentoAdicBoleto = rs.getString("cdVencimentoAdicBoleto");
        vencimentoAdicBoleto.dsVencimentoAdicBoleto = rs.getString("dsVencimentoAdicBoleto");
        vencimentoAdicBoleto.nuMaxVencimentos = rs.getInt("nuMaxVencimentos");
        vencimentoAdicBoleto.nuIntervaloVencimentos = rs.getInt("nuIntervaloVencimentos");
        vencimentoAdicBoleto.nuCarimbo = rs.getInt("nuCarimbo");
        vencimentoAdicBoleto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        vencimentoAdicBoleto.cdUsuario = rs.getString("cdUsuario");
        return vencimentoAdicBoleto;
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
        sql.append(" CDVENCIMENTOADICBOLETO,");
        sql.append(" CDVENCIMENTOADICBOLETO,");
        sql.append(" NUMAXVENCIMENTOS,");
        sql.append(" NUINTERVALOVENCIMENTOS,");
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
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDVENCIMENTOADICBOLETO,");
        sql.append(" CDVENCIMENTOADICBOLETO,");
        sql.append(" NUMAXVENCIMENTOS,");
        sql.append(" NUINTERVALOVENCIMENTOS,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	VencimentoAdicBoleto vencimentoAdicBoleto = (VencimentoAdicBoleto) domain;
        sql.append(Sql.getValue(vencimentoAdicBoleto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(vencimentoAdicBoleto.cdRepresentante)).append(",");
        sql.append(Sql.getValue(vencimentoAdicBoleto.cdVencimentoAdicBoleto)).append(",");
        sql.append(Sql.getValue(vencimentoAdicBoleto.dsVencimentoAdicBoleto)).append(",");
        sql.append(Sql.getValue(vencimentoAdicBoleto.nuMaxVencimentos)).append(",");
        sql.append(Sql.getValue(vencimentoAdicBoleto.nuIntervaloVencimentos)).append(",");
        sql.append(Sql.getValue(vencimentoAdicBoleto.nuCarimbo)).append(",");
        sql.append(Sql.getValue(vencimentoAdicBoleto.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(vencimentoAdicBoleto.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	VencimentoAdicBoleto vencimentoAdicBoleto = (VencimentoAdicBoleto) domain;
        sql.append(" DSVENCIMENTOADICBOLETO = ").append(Sql.getValue(vencimentoAdicBoleto.dsVencimentoAdicBoleto)).append(",");
        sql.append(" NUMAXVENCIMENTOS = ").append(Sql.getValue(vencimentoAdicBoleto.nuMaxVencimentos)).append(",");
        sql.append(" NUINTERVALOVENCIMENTOS = ").append(Sql.getValue(vencimentoAdicBoleto.nuIntervaloVencimentos)).append(",");
        sql.append(" cdUsuario = ").append(Sql.getValue(vencimentoAdicBoleto.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	VencimentoAdicBoleto vencimentoAdicBoleto = (VencimentoAdicBoleto) domain;
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("CDEMPRESA = ", vencimentoAdicBoleto.cdEmpresa);
    	sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", vencimentoAdicBoleto.cdRepresentante);
    	sqlWhereClause.addAndCondition("CDVENCIMENTOADICBOLETO = ", vencimentoAdicBoleto.cdVencimentoAdicBoleto);
    	sqlWhereClause.addAndCondition("DSVENCIMENTOADICBOLETO = ", vencimentoAdicBoleto.dsVencimentoAdicBoleto);
    	sqlWhereClause.addAndCondition("NUMAXVENCIMENTOS = ", vencimentoAdicBoleto.nuMaxVencimentos);
    	sqlWhereClause.addAndCondition("NUINTERVALOVENCIMENTOS = ", vencimentoAdicBoleto.nuIntervaloVencimentos);
    	//--
    	sql.append(sqlWhereClause.getSql());
    }
 
    public Vector findVencimentos(String cdEmpresa, String cdRepresentante, String cdVencimentoAdicBoleto) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select * from ");
    	sql.append(tableName);
    	sql.append(" where cdEmpresa = ").append(Sql.getValue(cdEmpresa));
    	sql.append(" and cdRepresentante = ").append(Sql.getValue(cdRepresentante));
    	if (ValueUtil.isNotEmpty(cdVencimentoAdicBoleto)) {
    		sql.append(" and NUMAXVENCIMENTOS <= (select NUMAXVENCIMENTOS from TBLVPVENCIMENTOADICBOLETO ") 
    		.append("		 where cdEmpresa = ").append(Sql.getValue(cdEmpresa))
    		.append("		 and cdRepresentante = ").append(Sql.getValue(cdRepresentante))
    		.append("		 and cdVencimentoAdicBoleto = ").append(Sql.getValue(cdVencimentoAdicBoleto)).append(" ) ");
    	}
    	sql.append(" order by NUMAXVENCIMENTOS DESC");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector vector = new Vector();
    		while (rs.next()) {
    			vector.addElement(populate(getBaseDomainDefault(), rs));
    		}
    		return vector;
    	}
    }

}