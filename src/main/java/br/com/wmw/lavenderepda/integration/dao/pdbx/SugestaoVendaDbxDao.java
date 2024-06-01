package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import totalcross.sql.ResultSet;

public class SugestaoVendaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SugestaoVenda();
	}

    private static SugestaoVendaDbxDao instance;

    public SugestaoVendaDbxDao() {
        super(SugestaoVenda.TABLE_NAME);
    }

    public static SugestaoVendaDbxDao getInstance() {
        if (instance == null) {
            instance = new SugestaoVendaDbxDao();
        }
        return instance;
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        SugestaoVenda sugestaoVenda = new SugestaoVenda();
        sugestaoVenda.rowKey = rs.getString("rowkey");
        sugestaoVenda.cdEmpresa = rs.getString("cdEmpresa");
        sugestaoVenda.cdSugestaoVenda = rs.getString("cdSugestaoVenda");
        sugestaoVenda.dsSugestaoVenda = rs.getString("dsSugestaoVenda");
        sugestaoVenda.flTipoSugestaoVenda = rs.getString("flTipoSugestaoVenda");
        sugestaoVenda.cdRamoAtividade = rs.getString("cdRamoAtividade");
        sugestaoVenda.dtFinal = rs.getDate("dtFinal");
        sugestaoVenda.dtInicial = rs.getDate("dtInicial");
        sugestaoVenda.flPossuiVigencia = rs.getString("flPossuiVigencia");
        sugestaoVenda.flObrigaVenda = rs.getString("flObrigaVenda");
        sugestaoVenda.flTipoFrequencia = rs.getString("flTipoFrequencia");
        sugestaoVenda.flFechamento = rs.getString("flFechamento");
        sugestaoVenda.nuCarimbo = rs.getInt("nuCarimbo");
        sugestaoVenda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        sugestaoVenda.cdCanal = rs.getString("cdCanal");
        sugestaoVenda.cdSegmento = rs.getString("cdSegmento");
        sugestaoVenda.flIndustria = rs.getString("flIndustria");
        sugestaoVenda.cdUsuario = rs.getString("cdUsuario");
		if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			sugestaoVenda.cdClassificacao = rs.getString("cdClassificacao");
		}
        return sugestaoVenda;
    }

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDSUGESTAOVENDA,");
        sql.append(" DSSUGESTAOVENDA,");
        sql.append(" FLTIPOSUGESTAOVENDA,");
        sql.append(" CDRAMOATIVIDADE,");
        sql.append(" DTFINAL,");
        sql.append(" DTINICIAL,");
        sql.append(" FLPOSSUIVIGENCIA,");
        sql.append(" FLOBRIGAVENDA,");
        sql.append(" FLTIPOFREQUENCIA,");
        sql.append(" FLFECHAMENTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDCANAL,");
        sql.append(" CDSEGMENTO,");
        sql.append(" FLINDUSTRIA,");
        sql.append(" CDUSUARIO");
		if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			sql.append(", CDCLASSIFICACAO");
		}
    }

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDSUGESTAOVENDA,");
        sql.append(" DSSUGESTAOVENDA,");
        sql.append(" FLTIPOSUGESTAOVENDA,");
        sql.append(" CDRAMOATIVIDADE,");
        sql.append(" DTFINAL,");
        sql.append(" DTINICIAL,");
        sql.append(" FLPOSSUIVIGENCIA,");
        sql.append(" FLOBRIGAVENDA,");
        sql.append(" FLTIPOFREQUENCIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDCANAL,");
        sql.append(" CDSEGMENTO,");
        sql.append(" FLINDUSTRIA,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVenda sugestaoVenda = (SugestaoVenda) domain;
        sql.append(Sql.getValue(sugestaoVenda.cdEmpresa)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.cdSugestaoVenda)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.dsSugestaoVenda)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.flTipoSugestaoVenda)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.cdRamoAtividade)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.dtFinal)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.dtInicial)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.flPossuiVigencia)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.flObrigaVenda)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.flTipoFrequencia)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.nuCarimbo)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.cdCanal)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.cdSegmento)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.flIndustria)).append(",");
        sql.append(Sql.getValue(sugestaoVenda.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVenda sugestaoVenda = (SugestaoVenda) domain;
        sql.append(" DSSUGESTAOVENDA = ").append(Sql.getValue(sugestaoVenda.dsSugestaoVenda)).append(",");
        sql.append(" DTFINAL = ").append(Sql.getValue(sugestaoVenda.dtFinal)).append(",");
        sql.append(" DTINICIAL = ").append(Sql.getValue(sugestaoVenda.dtInicial)).append(",");
        sql.append(" FLPOSSUIVIGENCIA = ").append(Sql.getValue(sugestaoVenda.flPossuiVigencia)).append(",");
        sql.append(" FLOBRIGAVENDA = ").append(Sql.getValue(sugestaoVenda.flObrigaVenda)).append(",");
        sql.append(" FLTIPOFREQUENCIA = ").append(Sql.getValue(sugestaoVenda.flTipoFrequencia)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(sugestaoVenda.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(sugestaoVenda.flTipoAlteracao)).append(",");
        sql.append(" CDCANAL = ").append(Sql.getValue(sugestaoVenda.cdCanal)).append(",");
        sql.append(" CDSEGMENTO = ").append(Sql.getValue(sugestaoVenda.cdSegmento)).append(",");
        sql.append(" FLINDUSTRIA = ").append(Sql.getValue(sugestaoVenda.flIndustria)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(sugestaoVenda.cdUsuario));
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        SugestaoVenda sugestaoVenda = (SugestaoVenda) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDEMPRESA = ", sugestaoVenda.cdEmpresa);
		sqlWhereClause.addAndCondition("CDSUGESTAOVENDA = ", sugestaoVenda.cdSugestaoVenda);
		sqlWhereClause.addAndCondition("FLTIPOSUGESTAOVENDA = ", sugestaoVenda.flTipoSugestaoVenda);
	    if (LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) {
		    sqlWhereClause.addAndCondition("((FLINDUSTRIA <> " + Sql.getValue(ValueUtil.VALOR_SIM) + " AND CDRAMOATIVIDADE = " + Sql.getValue(sugestaoVenda.cdRamoAtividade) + ") OR FLINDUSTRIA = " + Sql.getValue(ValueUtil.VALOR_SIM) + ")");
	    } else {
		    sqlWhereClause.addAndCondition("CDRAMOATIVIDADE = ", sugestaoVenda.cdRamoAtividade);
		    sqlWhereClause.addAndCondition("(FLINDUSTRIA <> " + Sql.getValue(ValueUtil.VALOR_SIM) + " OR FLINDUSTRIA IS NULL)");
	    }
		sqlWhereClause.addAndCondition("DTINICIAL <= ", sugestaoVenda.dtInicial);
		sqlWhereClause.addAndCondition("DTFINAL >= ", sugestaoVenda.dtFinal);
		sqlWhereClause.addAndCondition("FLPOSSUIVIGENCIA = ", sugestaoVenda.flPossuiVigencia);
		sqlWhereClause.addAndCondition("FLFECHAMENTO = ", sugestaoVenda.flFechamento);

	    if (LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) {
			if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
				if (ValueUtil.isEmpty(sugestaoVenda.cdClassificacao)) {
					sqlWhereClause.addAndCondition("CDCLASSIFICACAO IS NULL");
				} else {
					sqlWhereClause.addAndConditionEquals("CDCLASSIFICACAO", sugestaoVenda.cdClassificacao);
				}
			}
		    if (sugestaoVenda.cdCanal == null) {
			    sqlWhereClause.addAndCondition("((FLINDUSTRIA = " + Sql.getValue(ValueUtil.VALOR_SIM) + " AND (CDCANAL IS NULL OR CDCANAL = '' OR CDCANAL = 0)) OR FLINDUSTRIA <> " + Sql.getValue(ValueUtil.VALOR_SIM) + ")");
		    } else {
			    sqlWhereClause.addAndCondition("((FLINDUSTRIA = " + Sql.getValue(ValueUtil.VALOR_SIM) + " AND (CDCANAL = " + Sql.getValue(sugestaoVenda.cdCanal) + " OR CDCANAL IS NULL OR CDCANAL = '' OR CDCANAL = 0)) OR FLINDUSTRIA <> " + Sql.getValue(ValueUtil.VALOR_SIM) + ")");
		    }
		    if (sugestaoVenda.cdSegmento == null) {
			    sqlWhereClause.addAndCondition("((FLINDUSTRIA = " + Sql.getValue(ValueUtil.VALOR_SIM) + " AND (CDSEGMENTO IS NULL OR CDSEGMENTO = '' OR CDSEGMENTO = 0)) OR FLINDUSTRIA <> " + Sql.getValue(ValueUtil.VALOR_SIM) + ")");
		    } else {
			    sqlWhereClause.addAndCondition("((FLINDUSTRIA = " + Sql.getValue(ValueUtil.VALOR_SIM) + " AND (CDSEGMENTO = " + Sql.getValue(sugestaoVenda.cdSegmento) + " OR CDSEGMENTO IS NULL OR CDSEGMENTO = '' OR CDSEGMENTO = 0)) OR FLINDUSTRIA <> " + Sql.getValue(ValueUtil.VALOR_SIM) + ")");
		    }
			sqlWhereClause.addAndCondition("FLINDUSTRIA = ", sugestaoVenda.flIndustria);
		}

		if (ValueUtil.isNotEmpty(sugestaoVenda.flFechamentoDif)) {
			sqlWhereClause.addAndCondition("(FLFECHAMENTO IS NULL OR FLFECHAMENTO <> ", sugestaoVenda.flFechamentoDif);
			sqlWhereClause.addEndMultipleCondition();
		}
		sql.append(sqlWhereClause.getSql());
    }

}