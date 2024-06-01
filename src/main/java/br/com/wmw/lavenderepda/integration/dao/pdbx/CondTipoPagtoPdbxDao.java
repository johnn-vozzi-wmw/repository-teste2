package br.com.wmw.lavenderepda.integration.dao.pdbx;



import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondTipoPagto;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class CondTipoPagtoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new CondTipoPagto();
	}

    private static CondTipoPagtoPdbxDao instance;

    public CondTipoPagtoPdbxDao() {
        super(CondTipoPagto.TABLE_NAME);
    }

    public static CondTipoPagtoPdbxDao getInstance() {
        if (instance == null) {
            instance = new CondTipoPagtoPdbxDao();
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
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDCONDICAOPAGAMENTO,");
        sql.append(" tb.CDTIPOPAGAMENTO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.QTMINVALORPARCELA");
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        CondTipoPagto condpagtotipopagto = new CondTipoPagto();
        condpagtotipopagto.rowKey = rs.getString("rowkey");
        condpagtotipopagto.cdEmpresa = rs.getString("cdEmpresa");
        condpagtotipopagto.cdRepresentante = rs.getString("cdRepresentante");
        condpagtotipopagto.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        condpagtotipopagto.cdTipoPagamento = rs.getString("cdTipoPagamento");
        condpagtotipopagto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        condpagtotipopagto.qtMinValorParcela = rs.getDouble("qtMinValorParcela");
        return condpagtotipopagto;
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        CondTipoPagto condpagtotipopagto = (CondTipoPagto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", condpagtotipopagto.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", condpagtotipopagto.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCONDICAOPAGAMENTO = ", condpagtotipopagto.cdCondicaoPagamento);
		sqlWhereClause.addAndCondition("tb.CDTIPOPAGAMENTO = ", condpagtotipopagto.cdTipoPagamento);
		//--
		sql.append(sqlWhereClause.getSql());
    }
}