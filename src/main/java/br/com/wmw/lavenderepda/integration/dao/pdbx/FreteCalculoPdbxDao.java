package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.FreteBaseCalculo;
import br.com.wmw.lavenderepda.business.domain.FreteCalculo;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import br.com.wmw.lavenderepda.business.domain.FreteEvento;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class FreteCalculoPdbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FreteCalculo();
	}

    private static FreteCalculoPdbxDao instance;

    public FreteCalculoPdbxDao() {
        super(FreteCalculo.TABLE_NAME);
    }

    public static FreteCalculoPdbxDao getInstance() {
        return (instance == null) ? instance = new FreteCalculoPdbxDao() : instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	FreteCalculo freteCalculo = new FreteCalculo();
        freteCalculo.rowKey = rs.getString("rowkey");
        freteCalculo.cdEmpresa = rs.getString("cdEmpresa");
        freteCalculo.cdRepresentante = rs.getString("cdRepresentante");
        freteCalculo.cdTransportadora = rs.getString("cdTransportadora");
        freteCalculo.cdFreteConfig = rs.getString("cdFreteConfig");
        freteCalculo.cdFreteCalculo = rs.getInt("cdFreteCalculo");
        freteCalculo.nuOrdemCalculo = rs.getInt("nuOrdemCalculo");
        freteCalculo.cdFreteEvento = rs.getString("cdFreteEvento");
        freteCalculo.flTipoTaxa = rs.getString("flTipoTaxa");
        freteCalculo.vlTaxa = rs.getDouble("vlTaxa");
        freteCalculo.flFormaPrecoFrete = rs.getString("flFormaPrecoFrete");
        freteCalculo.vlMinimo = rs.getDouble("vlMinimo");
        freteCalculo.vlFaixaBC = rs.getDouble("vlFaixaBC");
        freteCalculo.flTipoTaxaExcedendeFaixaBC = rs.getString("flTipoTaxaExcedendeFaixaBC");
        freteCalculo.vlTaxaExcedenteFaixaBC = rs.getDouble("vlTaxaExcedenteFaixaBC");
        return freteCalculo;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDTRANSPORTADORA,");
        sql.append(" CDFRETECONFIG,");
        sql.append(" CDFRETECALCULO,");
        sql.append(" NUORDEMCALCULO,");
        sql.append(" CDFRETEEVENTO,");
        sql.append(" FLTIPOTAXA,");
        sql.append(" VLTAXA,");
        sql.append(" FLFORMAPRECOFRETE,");
        sql.append(" VLMINIMO,");
        sql.append(" VLFAIXABC,");
        sql.append(" FLTIPOTAXAEXCEDENDEFAIXABC,");
        sql.append(" VLTAXAEXCEDENTEFAIXABC");
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        FreteCalculo freteCalculoFilter = (FreteCalculo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", freteCalculoFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", freteCalculoFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("CDTRANSPORTADORA = ", freteCalculoFilter.cdTransportadora);
		sqlWhereClause.addAndCondition("CDFRETECONFIG = ", freteCalculoFilter.cdFreteConfig);
		sqlWhereClause.addAndCondition("CDFRETECALCULO = ", freteCalculoFilter.cdFreteCalculo);
		sqlWhereClause.addAndCondition("NUORDEMCALCULO = ", freteCalculoFilter.nuOrdemCalculo);
		sqlWhereClause.addAndCondition("CDFRETEEVENTO = ", freteCalculoFilter.cdFreteEvento);
		sqlWhereClause.addAndCondition("FLTIPOTAXA = ", freteCalculoFilter.flTipoTaxa);
		sqlWhereClause.addAndCondition("VLTAXA = ", freteCalculoFilter.vlTaxa);
		sqlWhereClause.addAndCondition("FLFORMAPRECOFRETE = ", freteCalculoFilter.flFormaPrecoFrete);
		sqlWhereClause.addAndCondition("VLMINIMO = ", freteCalculoFilter.vlMinimo);
		sqlWhereClause.addAndCondition("FLTIPOTAXAEXCEDENDEFAIXABC = ", freteCalculoFilter.flTipoTaxaExcedendeFaixaBC);
		sqlWhereClause.addAndCondition("VLFAIXABC = ", freteCalculoFilter.vlFaixaBC);
		sqlWhereClause.addAndCondition("VLTAXAEXCEDENTEFAIXABC = ", freteCalculoFilter.vlTaxaExcedenteFaixaBC);
		sql.append(sqlWhereClause.getSql());
    }

	public Vector findAllFreteCalculoByFreteConfig(FreteConfig freteConfigFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT TB.CDFRETECALCULO, ") 
				 .append(" TB.CDFRETEEVENTO, ")
				 .append(" TB.NUORDEMCALCULO, ")
				 .append(" TB.FLTIPOTAXA, ")
				 .append(" TB.VLTAXA, ")
				 .append(" TB.FLFORMAPRECOFRETE, ")  
				 .append(" TB.VLMINIMO, ")
				 .append(" TB.VLFAIXABC, ")
				 .append(" TB.FLTIPOTAXAEXCEDENDEFAIXABC, ")
				 .append(" TB.VLTAXAEXCEDENTEFAIXABC, ")
				 .append(" FRETEEVENTO.DSFRETEEVENTO, ")
				 .append(" FRETEEVENTO.NUTIPOBASECALCULO, ")
				 .append(" FRETEBASECALCULO.CDFRETECALCULO CDFRETECALCULOBASE, ")
				 .append(" FRETEBASECALCULO.CDFRETECALCULOBC CDFRETECALCULOBC ") 
		.append(" FROM TBLVPFRETECALCULO TB ")
		.append(" JOIN TBLVPFRETEEVENTO FRETEEVENTO ON ")	
			.append(" FRETEEVENTO.CDEMPRESA = TB.CDEMPRESA ")
		.append(" AND FRETEEVENTO.CDREPRESENTANTE = TB.CDREPRESENTANTE ")
		.append(" AND FRETEEVENTO.CDFRETEEVENTO = TB.CDFRETEEVENTO")
		.append(" LEFT JOIN TBLVPFRETEBASECALCULO FRETEBASECALCULO ON ")
		.append(" FRETEBASECALCULO.CDEMPRESA = TB.CDEMPRESA ")
		.append(" AND FRETEBASECALCULO.CDREPRESENTANTE = TB.CDREPRESENTANTE ")
		.append(" AND FRETEBASECALCULO.CDFRETECONFIG = TB.CDFRETECONFIG ")
		.append(" AND FRETEBASECALCULO.CDFRETECALCULO  = TB.CDFRETECALCULO ")
		.append(" AND FRETEEVENTO.NUTIPOBASECALCULO IN (").append(Sql.getValue(FreteEvento.TIPO_BASE_CALCULO_CALCULADO_MAIOR_VALOR)).append(", ").append(Sql.getValue(FreteEvento.TIPO_BASE_CALCULO_CALCULADO_SOMATORIO)).append(")");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("TB.CDEMPRESA = ", freteConfigFilter.cdEmpresa);
		sqlWhereClause.addAndCondition("TB.CDREPRESENTANTE = ", freteConfigFilter.cdRepresentante);
		sqlWhereClause.addAndCondition("TB.CDTRANSPORTADORA = ", freteConfigFilter.cdTransportadora);
		sqlWhereClause.addAndCondition("TB.CDFRETECONFIG = ", freteConfigFilter.cdFreteConfig);
		sql.append(sqlWhereClause.getSql())
		.append(" ORDER BY TB.NUORDEMCALCULO ASC, TB.VLFAIXABC ASC");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector result = new Vector(50);
			Vector listFreteBaseCalculo = new Vector();
			int lastNuSeq = 0;
			while (rs.next()) {
				FreteCalculo freteCalculo = new FreteCalculo();
				freteCalculo.freteEvento = new FreteEvento();
				FreteBaseCalculo freteBaseCalculo = new FreteBaseCalculo();
				int columnResultIndex = 1;
				populateFreteCalculoByFreteCalculoResult(freteConfigFilter, freteCalculo, rs, columnResultIndex);
				populateFreteEventoByFreteCalculoResult(freteCalculo, rs, columnResultIndex);				
				freteCalculo.listFreteBaseCalculo = listFreteBaseCalculo =  lastNuSeq == freteCalculo.nuOrdemCalculo ? listFreteBaseCalculo : new Vector();				
				populateFreteBaseCalculoByFreteCalculoResult(listFreteBaseCalculo, freteCalculo, freteBaseCalculo, rs, columnResultIndex);
				lastNuSeq = freteCalculo.nuOrdemCalculo;
				// Remove e adiciona novamente o novo objeto se ele existir, pois há uma nova interação no freteBaseCalculo;
				if (result.contains(freteCalculo)) {
					result.removeElement(freteCalculo);
				}
				result.addElement(freteCalculo);
			}	
			return result;
		}
	}

	private void populateFreteCalculoByFreteCalculoResult(final FreteConfig freteConfigFilter, FreteCalculo freteCalculo, final ResultSet rs, int columnResultIndex) throws SQLException {
		freteCalculo.cdEmpresa = freteConfigFilter.cdEmpresa;
		freteCalculo.cdRepresentante = freteConfigFilter.cdRepresentante;
		freteCalculo.cdTransportadora = freteConfigFilter.cdTransportadora;
		freteCalculo.cdFreteConfig = freteConfigFilter.cdFreteConfig;
		freteCalculo.cdFreteCalculo = rs.getInt("cdFreteCalculo");;
		freteCalculo.nuOrdemCalculo = rs.getInt("nuOrdemCalculo");
		freteCalculo.cdFreteEvento = rs.getString("cdFreteEvento");
		freteCalculo.flTipoTaxa = rs.getString("flTipoTaxa");
		freteCalculo.vlTaxa = rs.getDouble("vlTaxa");
		freteCalculo.flFormaPrecoFrete = rs.getString("flFormaPrecoFrete");
		freteCalculo.vlMinimo = rs.getDouble("vlMinimo");
		freteCalculo.vlFaixaBC = rs.getDouble("vlFaixaBC");
		freteCalculo.flTipoTaxaExcedendeFaixaBC = rs.getString("flTipoTaxaExcedendeFaixaBC");
		freteCalculo.vlTaxaExcedenteFaixaBC = rs.getDouble("vlTaxaExcedenteFaixaBC");
	}

	private void populateFreteEventoByFreteCalculoResult(FreteCalculo freteCalculo, final ResultSet rs, int columnResultIndex) throws SQLException {
		freteCalculo.freteEvento.cdEmpresa = freteCalculo.cdEmpresa;
		freteCalculo.freteEvento.cdRepresentante = freteCalculo.cdRepresentante;
		freteCalculo.freteEvento.cdFreteEvento = freteCalculo.cdFreteEvento;
		freteCalculo.freteEvento.dsFreteEvento = rs.getString("dsFreteEvento");
		freteCalculo.freteEvento.nuTipoBaseCalculo = rs.getInt("nuTipoBaseCalculo");
	}
	
	private void populateFreteBaseCalculoByFreteCalculoResult(Vector listFreteBaseCalculo, FreteCalculo freteCalculo, FreteBaseCalculo freteBaseCalculo, ResultSet rs, int columnResultIndex) throws SQLException {
		freteBaseCalculo.cdEmpresa = freteCalculo.cdEmpresa;
		freteBaseCalculo.cdRepresentante = freteCalculo.cdRepresentante;
		freteBaseCalculo.cdTransportadora = freteCalculo.cdTransportadora;
		freteBaseCalculo.cdFreteConfig = freteCalculo.cdFreteConfig;
		freteBaseCalculo.cdFreteCalculo = rs.getInt("CDFRETECALCULOBASE");
		freteBaseCalculo.cdFreteCalculoBC = rs.getInt("CDFRETECALCULOBC");
		if (freteBaseCalculo.cdFreteCalculo == 0 || freteCalculo.cdFreteCalculo != freteBaseCalculo.cdFreteCalculo) return;
		listFreteBaseCalculo.addElement(freteBaseCalculo);
	}

}