package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Segmento;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class SegmentoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Segmento();
	}

    private static SegmentoDbxDao instance;

    public SegmentoDbxDao() {
        super(Segmento.TABLE_NAME);
    }

    public static SegmentoDbxDao getInstance() {
        if (instance == null) {
            instance = new SegmentoDbxDao();
        }
        return instance;
    }

    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Segmento segmento = new Segmento();
        segmento.rowKey = rs.getString("rowkey");
        segmento.cdEmpresa = rs.getString("cdEmpresa");
        segmento.cdRepresentante = rs.getString("cdRepresentante");
        segmento.cdSegmento = rs.getString("cdSegmento");
        segmento.dsSegmento = rs.getString("dsSegmento");
        segmento.nuCarimbo = rs.getInt("nuCarimbo");
        segmento.flTipoAlteracao = rs.getString("flTipoAlteracao");
        if (isFazJoinComClienteSeg((Segmento)domainFilter)) {
        	segmento.flDefault = rs.getString("flDefault");
        }
        segmento.cdUsuario = rs.getString("cdUsuario");
        return segmento;
    }

	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDSEGMENTO,");
        sql.append(" tb.DSSEGMENTO,");
        sql.append(" tb.NUCARIMBO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        if (isFazJoinComClienteSeg((Segmento)domainFilter)) {
        	sql.append(" CLISEG.FLDEFAULT,");
        }
        sql.append(" tb.CDUSUARIO");
    }

	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDSEGMENTO,");
        sql.append(" DSSEGMENTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Segmento segmento = (Segmento) domain;
        sql.append(Sql.getValue(segmento.cdEmpresa)).append(",");
        sql.append(Sql.getValue(segmento.cdRepresentante)).append(",");
        sql.append(Sql.getValue(segmento.cdSegmento)).append(",");
        sql.append(Sql.getValue(segmento.dsSegmento)).append(",");
        sql.append(Sql.getValue(segmento.nuCarimbo)).append(",");
        sql.append(Sql.getValue(segmento.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(segmento.cdUsuario));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Segmento segmento = (Segmento) domain;
        sql.append(" DSSEGMENTO = ").append(Sql.getValue(segmento.dsSegmento)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(segmento.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(segmento.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(segmento.cdUsuario));
    }

    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Segmento segmento = (Segmento) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", segmento.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", segmento.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDSEGMENTO = ", segmento.cdSegmento);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	Segmento segmento = (Segmento) domainFilter;
    	if (isFazJoinComClienteSeg(segmento)) {
    		DaoUtil.addJoinClienteSeg(sql, segmento.clienteSegFilter);
    	}
    }

	private boolean isFazJoinComClienteSeg(Segmento segmento) {
		return LavenderePdaConfig.usaSegmentoPorCliente && segmento.clienteSegFilter != null;
	}

}