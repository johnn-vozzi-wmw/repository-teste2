package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.StatusPedidoPda;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class StatusPedidoPdaPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new StatusPedidoPda();
	}

    private static StatusPedidoPdaPdbxDao instance;

	private Vector cache = null;

    public StatusPedidoPdaPdbxDao() {
        super(StatusPedidoPda.TABLE_NAME);
    }

    public static StatusPedidoPdaPdbxDao getInstance() {
        if (instance == null) {
            instance = new StatusPedidoPdaPdbxDao();
        }
        return instance;
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null; 
	}

	//@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
		sql.append(" rowKey,");
		sql.append(" CDSTATUSPEDIDO,");
		sql.append(" DSSTATUSPEDIDO,");
		if (LavenderePdaConfig.usaWorkflowStatusPedido) {
			sql.append(" FLUSAWORKFLOW,");
			sql.append(" NUORDEMWORKFLOW,");
			sql.append(" IMICONEWORKFLOW,");
		}
		sql.append(" FLCREDITOCLIENTEABERTO,");
		sql.append(" FLRELACIONAPEDIDO,");
		sql.append(" FLCOMPLEMENTAVEL,");
		sql.append(" FLCONVERTETIPOPEDIDOREPLICACAO,");
		sql.append(" FLRELACIONAPEDIDOPDA,");
		sql.append(" FLCONSIDERADESCPROGVLMIN,");
		sql.append(" FLCONSIDERADESCPROGVLMAX,");
		sql.append(" FLIGNORAHISTORICOITEM,");
		sql.append(" FLOCULTAVALORESCOMISSAO,");
		sql.append(" FLCONSIDERAFAMILIA");
	}

	//@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
	    StatusPedidoPda statusPedidoPda = new StatusPedidoPda();
	    statusPedidoPda.rowKey = rs.getString("rowkey");
	    statusPedidoPda.cdStatusPedido = rs.getString("cdStatusPedido");
	    statusPedidoPda.dsStatusPedido = rs.getString("dsStatusPedido");
	    statusPedidoPda.flCreditoClienteAberto = rs.getString("flCreditoClienteAberto");
	    if (LavenderePdaConfig.usaWorkflowStatusPedido) {
	    	statusPedidoPda.flUsaWorkflow = rs.getString("flUsaWorkflow");
	    	statusPedidoPda.nuOrdemWorkflow = rs.getInt("nuOrdemWorkflow");
	    	statusPedidoPda.imIconeWorkflow = rs.getBytes("imIconeWorkflow");
	    }
	    statusPedidoPda.flRelacionaPedido = rs.getString("flRelacionaPedido");
	    statusPedidoPda.flComplementavel = rs.getString("flComplementavel");
	    statusPedidoPda.flRelacionaPedidoPda = rs.getString("flRelacionaPedidoPda");
	    statusPedidoPda.flIgnoraHistoricoItem = rs.getString("flIgnoraHistoricoItem");
		statusPedidoPda.flConsideraDescProgVlMin = rs.getString("flConsideraDescProgVlMin");
		statusPedidoPda.flConsideraDescProgVlMax = rs.getString("flConsideraDescProgVlMax");
		statusPedidoPda.flConverteTipoPedidoReplicacao = rs.getString("flConverteTipoPedidoReplicacao");
		statusPedidoPda.flOcultaValoresComissao = rs.getString("flOcultaValoresComissao");
		statusPedidoPda.flConsideraFamilia = rs.getString("flConsideraFamilia");
	    return statusPedidoPda;
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        StatusPedidoPda statusPedido = (StatusPedidoPda) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDSTATUSPEDIDO = ", statusPedido.cdStatusPedido);
        sqlWhereClause.addAndCondition("FLUSAWORKFLOW = ", statusPedido.flUsaWorkflow);
        sqlWhereClause.addAndCondition("FLRELACIONAPEDIDO = ", statusPedido.flRelacionaPedido);
        sqlWhereClause.addAndCondition("FLCOMPLEMENTAVEL = ", statusPedido.flComplementavel);
        sql.append(sqlWhereClause.getSql());
    }

	public Vector findIdStatusRelacionaPedidoPda() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT CDSTATUSPEDIDO, FLRELACIONAPEDIDOPDA ");
		sql.append(" FROM ").append(tableName);
		sql.append(" WHERE FLRELACIONAPEDIDOPDA = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector statusPedidoPdaList = new Vector();
			while (rs.next()) {
				String cdStatusPedido = rs.getString("cdStatusPedido");
				statusPedidoPdaList.addElement(cdStatusPedido);
			}
			return statusPedidoPdaList;
		}
	}

	//@Override
	public void clearCache() {
		super.clearCache();
		cache = null;
	}

	//@Override
	public Vector findAllInCache() throws SQLException {
		return getCacheVector();
	}

	//@Override
	public Vector findAllByExampleInCache(BaseDomain domain) throws java.sql.SQLException {
		return null;
	}

	private Vector getCacheVector() throws SQLException {
		if (cache == null) {
			cache = findAll();
		}
		return cache;
	}

	//@Override
	public BaseDomain findByRowKeyInCache(BaseDomain domain) throws SQLException {
		int index = getCacheVector().indexOf(domain);
		if (index != -1) {
			return (BaseDomain) getCacheVector().items[index];
		}
		return null;
	}
}
