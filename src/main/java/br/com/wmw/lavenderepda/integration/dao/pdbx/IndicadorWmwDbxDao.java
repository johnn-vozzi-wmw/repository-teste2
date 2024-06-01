package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.IndicadorWmw;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;

public class IndicadorWmwDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new IndicadorWmw();
	}

    private static IndicadorWmwDbxDao instance;

    public IndicadorWmwDbxDao() {
        super(IndicadorWmw.TABLE_NAME); 
    }
    
    public static IndicadorWmwDbxDao getInstance() {
        if (instance == null) {
            instance = new IndicadorWmwDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs)  throws SQLException {
        IndicadorWmw indicadorWmw = new IndicadorWmw();
        indicadorWmw.rowKey = rs.getString("rowkey");
        indicadorWmw.cdIndicador = rs.getString("cdIndicador");
        indicadorWmw.dsIndicador = rs.getString("dsIndicador");
        indicadorWmw.dsVlPadraoIndicador = rs.getString("dsVlPadraoIndicador");
        indicadorWmw.cdTipoApuracao = rs.getString("cdTipoApuracao");
        indicadorWmw.nuSequencia = rs.getInt("nuSequencia");
        indicadorWmw.dsMascaraFormato = rs.getString("dsMascaraFormato");
        indicadorWmw.nuCarimbo = rs.getInt("nuCarimbo");
        indicadorWmw.cdUsuario = rs.getString("cdUsuario");
        indicadorWmw.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return indicadorWmw;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDINDICADOR,");
        sql.append(" DSINDICADOR,");
        sql.append(" DSVLPADRAOINDICADOR,");
        sql.append(" CDTIPOAPURACAO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" DSMASCARAFORMATO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDINDICADOR,");
        sql.append(" DSINDICADOR,");
        sql.append(" DSVLPADRAOINDICADOR,");
        sql.append(" CDTIPOAPURACAO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" DSMASCARAFORMATO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        IndicadorWmw indicadorWmw = (IndicadorWmw) domain;
        sql.append(Sql.getValue(indicadorWmw.cdIndicador)).append(",");
        sql.append(Sql.getValue(indicadorWmw.dsIndicador)).append(",");
        sql.append(Sql.getValue(indicadorWmw.dsVlPadraoIndicador)).append(",");
        sql.append(Sql.getValue(indicadorWmw.cdTipoApuracao)).append(",");
        sql.append(Sql.getValue(indicadorWmw.nuSequencia)).append(",");
        sql.append(Sql.getValue(indicadorWmw.dsMascaraFormato)).append(",");
        sql.append(Sql.getValue(indicadorWmw.nuCarimbo)).append(",");
        sql.append(Sql.getValue(indicadorWmw.cdUsuario)).append(",");
        sql.append(Sql.getValue(indicadorWmw.flTipoAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        IndicadorWmw indicadorWmw = (IndicadorWmw) domain;
        sql.append(" DSINDICADOR = ").append(Sql.getValue(indicadorWmw.dsIndicador)).append(",");
        sql.append(" DSVLPADRAOINDICADOR = ").append(Sql.getValue(indicadorWmw.dsVlPadraoIndicador)).append(",");
        sql.append(" CDTIPOAPURACAO = ").append(Sql.getValue(indicadorWmw.cdTipoApuracao)).append(",");
        sql.append(" NUSEQUENCIA = ").append(Sql.getValue(indicadorWmw.nuSequencia)).append(",");
        sql.append(" DSMASCARAFORMATO = ").append(Sql.getValue(indicadorWmw.dsMascaraFormato)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(indicadorWmw.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(indicadorWmw.cdUsuario)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(indicadorWmw.flTipoAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        IndicadorWmw indicadorWmw = (IndicadorWmw) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDINDICADOR = ", indicadorWmw.cdIndicador);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	public List<ValorIndicador> buscaIndicadoresPor(String cdEmpresa, String cdRepresentante, Date dtReferencia) throws SQLException {
		StringBuffer sql = getSqlBuffer();
  		sql.append(" SELECT TB.CDINDICADOR, DSINDICADOR, DSAPURACAO, COALESCE(VLINDICADOR, COALESCE(DSVLPADRAOINDICADOR, ' ')) as VLINDICADOR, TB.DSMASCARAFORMATO ");
  		sql.append(" FROM ").append(tableName).append(" TB");
  		sql.append(" LEFT JOIN TBLVPVALORINDICADORWMW VI ON (VI.CDINDICADOR = TB.CDINDICADOR");
  		sql.append(" AND CDEMPRESA = '").append(cdEmpresa);
  		sql.append("' AND CDREPRESENTANTE = '").append(cdRepresentante);
  		sql.append("' AND DTAPURACAO = ").append(Sql.getValue(dtReferencia));
  		sql.append(") ORDER BY TB.NUSEQUENCIA");
  		try (Statement st = getCurrentDriver().getStatement();
  				ResultSet rs = st.executeQuery(sql.toString())) {
  			List<ValorIndicador> list = new ArrayList<>();
  			//--
  			ValorIndicador domain = null;
  			StringBuilder s = new StringBuilder();
  			while (rs.next()) {
  				s.setLength(0);
  				domain = new ValorIndicador();
  				domain.cdIndicador = rs.getString("CDINDICADOR");
  				domain.dsIndicador = rs.getString("DSINDICADOR");
  				domain.dsVlIndicador = rs.getString("VLINDICADOR");
  				domain.dsMascaraFormato = rs.getString("DSMASCARAFORMATO");
  				String dsApuracao = rs.getString("DSAPURACAO");
  				if (ValueUtil.isNotEmpty(dsApuracao)) {
  					s.append(domain.dsIndicador).append(" - ").append(dsApuracao);
  					domain.dsIndicador = s.toString();
  				}
  				list.add(domain);
  			}
  			return list;
  		}
	}
    
}