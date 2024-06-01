package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class TipoFretePdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoFrete();
	}

    private static TipoFretePdbxDao instance;

    public TipoFretePdbxDao() {
        super(TipoFrete.TABLE_NAME);
    }

    public static TipoFretePdbxDao getInstance() {
        if (instance == null) {
            instance = new TipoFretePdbxDao();
        }
        return instance;
    }

    @Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException { /**/ }
    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }
    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { /**/ }
    @Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
    @Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { /**/ }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        TipoFrete tipofrete = new TipoFrete();
        tipofrete.rowKey = rs.getString("rowkey");
        tipofrete.cdEmpresa = rs.getString("cdEmpresa");
        tipofrete.cdRepresentante = rs.getString("cdRepresentante");
        tipofrete.cdTipoFrete = rs.getString("cdTipofrete");
        tipofrete.cdUf = rs.getString("cdUf");
        tipofrete.dsTipoFrete = rs.getString("dsTipofrete");
        tipofrete.flFreteFob = rs.getString("flFretefob");
        tipofrete.vlPctFrete = rs.getDouble("vlPctFrete");
        tipofrete.vlPesoMinimo = rs.getDouble("vlPesoMinimo");
        tipofrete.flDefault = rs.getString("flDefault");
        tipofrete.flUsaInfoAdicional = rs.getString("flUsaInfoAdicional");
        tipofrete.flTipoFreteCif = rs.getString("flTipoFreteCif");
        tipofrete.flCalculaFrete = rs.getString("flCalculaFrete");
        tipofrete.vlPctMaxDesconto = rs.getDouble("vlPctMaxDesconto");
        tipofrete.vlMinimo = rs.getDouble("vlMinimo");
        tipofrete.flSemFrete = rs.getString("flSemFrete");
        tipofrete.flCalculaFreteItem = rs.getString("flCalculaFreteItem");
        tipofrete.flSugereSeguro = rs.getString("flSugereSeguro");
        if (LavenderePdaConfig.isUsaMotivoPendencia()) {
            tipofrete.cdMotivoPendencia = rs.getString("cdMotivoPendencia");
            tipofrete.nuOrdemLiberacao = rs.getInt("nuOrdemLiberacao");
        }
        return tipofrete;
    }

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
    	TipoFrete tipoFrete = (TipoFrete) domainFilter;
    	sql.append(" tb.rowkey,");
    	sql.append(" tb.CDEMPRESA,");
    	sql.append(" tb.CDREPRESENTANTE,");
    	sql.append(" tb.CDTIPOFRETE,");
    	sql.append(" tb.CDUF,");
    	sql.append(" tb.DSTIPOFRETE,");
    	sql.append(" tb.FLFRETEFOB,");
    	sql.append(" tb.VLPCTFRETE,");
    	sql.append(" tb.VLPESOMINIMO,");
    	sql.append(" tb.FLUSAINFOADICIONAL,");
    	sql.append(" tb.FLCALCULAFRETE,");
    	sql.append(" tb.FLTIPOFRETECIF,");
    	sql.append(" tb.VLPCTMAXDESCONTO,");
    	sql.append(" tb.VLMINIMO,");
    	sql.append(" tb.FLSEMFRETE,");
    	sql.append(" tb.FLCALCULAFRETEITEM,");
    	if (LavenderePdaConfig.usaTipoFretePorCliente && tipoFrete.tipoFreteCliFilter != null) {
    		sql.append(" COALESCE(").append(DaoUtil.ALIAS_TIPOFRETECLI).append(".FLDEFAULT, tb.FLDEFAULT) FLDEFAULT,");
    	} else {
    		sql.append(" tb.FLDEFAULT,");
    	}
    	sql.append(" tb.FLSUGERESEGURO");
        if (LavenderePdaConfig.isUsaMotivoPendencia()) {
        	sql.append(", CDMOTIVOPENDENCIA");
        	sql.append(", NUORDEMLIBERACAO");
        }
    }

   @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
        TipoFrete tipoFrete = (TipoFrete) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", tipoFrete.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", tipoFrete.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDTIPOFRETE = ", tipoFrete.cdTipoFrete);
		sqlWhereClause.addAndCondition("tb.CDUF = ", tipoFrete.cdUf);
		sqlWhereClause.addAndCondition("tb.FLUSAINFOADICIONAL = ", tipoFrete.flUsaInfoAdicional);
		sqlWhereClause.addAndCondition("tb.FLTIPOFRETECIF = ", tipoFrete.flTipoFreteCif);
		if (LavenderePdaConfig.usaTipoFretePorCliente && tipoFrete.tipoFreteCliFilter != null) {
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addAndCondition(DaoUtil.ALIAS_TIPOFRETECLI.concat(".CDEMPRESA IS NOT NULL"));
			sqlWhereClause.addOrCondition(DaoUtil.getNotExistsTipoFreteCliCondition(tipoFrete.tipoFreteCliFilter));
			sqlWhereClause.addEndMultipleCondition();
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }

	@Override
	protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
		TipoFrete tipoFrete = (TipoFrete) domainFilter;
		if (LavenderePdaConfig.usaTipoFretePorCliente && tipoFrete.tipoFreteCliFilter != null) {
			DaoUtil.addJoinTipoFreteCli(sql, tipoFrete.tipoFreteCliFilter);
		}
	}

}