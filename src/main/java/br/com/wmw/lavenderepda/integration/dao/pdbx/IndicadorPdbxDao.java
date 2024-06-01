package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.lavenderepda.business.domain.Indicador;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;

public class IndicadorPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Indicador();
	}

    private static IndicadorPdbxDao instance;

    public IndicadorPdbxDao() {
        super(Indicador.TABLE_NAME);
    }

    public static IndicadorPdbxDao getInstance() {
        if (instance == null) {
            instance = new IndicadorPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDINDICADOR,");
        sql.append(" DSINDICADOR,");
        sql.append(" DSVLPADRAOINDICADOR,");
        sql.append(" NUSEQUENCIA");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Indicador indicador = new Indicador();
        indicador.rowKey = rs.getString("rowkey");
        indicador.cdIndicador = rs.getString("cdIndicador");
        indicador.dsIndicador = rs.getString("dsIndicador");
        indicador.dsVlPadraoIndicador = rs.getString("dsVlPadraoIndicador");
        indicador.nuSequencia = rs.getInt("nuSequencia");
        return indicador;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Indicador indicador = (Indicador) domain;
        sql.append(" where CDINDICADOR = ").append(Sql.getValue(indicador.cdIndicador));
    }
 
    public List<ValorIndicador> buscaIndicadoresPor(String cdEmpresa, String cdRepresentante, Date dtReferencia) throws java.sql.SQLException {
  		StringBuffer sql = getSqlBuffer();
  		sql.append(" SELECT vl.cdindicador, dsindicador, coalesce(dsvlindicador, COALESCE(dsVlPadraoIndicador, ' ')) as dsvlindicador, vl.dsMascaraFormato ");
  		sql.append(" FROM ").append(tableName).append(" vl");
  		sql.append(" LEFT JOIN tblvpvalorindicador li on (li.cdindicador = vl.cdindicador");
  		sql.append(" and cdempresa = '").append(cdEmpresa);
  		sql.append("' and cdrepresentante = '").append(cdRepresentante);
  		sql.append("' and dtreferencia = ").append(Sql.getValue(dtReferencia));
  		sql.append(") ORDER BY vl.nusequencia");
  		try (Statement st = getCurrentDriver().getStatement();
  				ResultSet rs = st.executeQuery(sql.toString())) {
  			List<ValorIndicador> list = new ArrayList<>();
  			ValorIndicador domain = null;
  			while (rs.next()) {
  				domain = new ValorIndicador();
  				domain.cdIndicador = rs.getString("cdindicador");
  				domain.dsIndicador = rs.getString("dsindicador");
  				domain.dsVlIndicador = rs.getString("dsvlindicador");
  				domain.dsMascaraFormato = rs.getString("dsMascaraFormato");
  				list.add(domain);
  			}
  			return list;
  		}
  	}
}