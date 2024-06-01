package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.NotificacaoPda;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class NotificacaoPdaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new NotificacaoPda();
	}

    private static NotificacaoPdaDbxDao instance;
	public static String erroOcorridoAtualizacao;
	public static boolean atualizandoCache;
	private Vector cacheNotificacaoOnLine = null;

    public NotificacaoPdaDbxDao() {
        super(NotificacaoPda.TABLE_NAME);
    }

    public static NotificacaoPdaDbxDao getInstance() {
        if (instance == null) {
            instance = new NotificacaoPdaDbxDao();
        }
        return instance;
    }

    public void putNotificacaoOnCache(String rowKey) {
    	if (cacheNotificacaoOnLine == null) {
    		cacheNotificacaoOnLine = new Vector(0);
    	}
    	cacheNotificacaoOnLine.addElement(rowKey);
    }

    public String getNotificacaoOnCache(int index) {
    	if (cacheNotificacaoOnLine == null) {
    		return null;
    	}
    	return (String)cacheNotificacaoOnLine.items[index];
    }

    public Vector getCacheNotificacaoOnLine() {
    	if (cacheNotificacaoOnLine == null) {
    		cacheNotificacaoOnLine = new Vector(0);
    	}
    	return cacheNotificacaoOnLine;
    }

    public void limpeCacheNotificacaoOnLine() {
    	cacheNotificacaoOnLine = null;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        NotificacaoPda notificacaoPda = new NotificacaoPda();
        notificacaoPda.rowKey = rs.getString("rowkey");
        notificacaoPda.cdNotificacao = rs.getString("cdNotificacao");
        notificacaoPda.cdChave = rs.getString("cdChave");
        notificacaoPda.cdRepresentante = rs.getString("cdRepresentante");
        notificacaoPda.dtNotificacao = rs.getDate("dtNotificacao");
        notificacaoPda.hrNotificacao = rs.getString("hrNotificacao");
        notificacaoPda.dsNotificacao = rs.getString("dsNotificacao");
        notificacaoPda.nuSequencia = rs.getInt("nuSequencia");
        notificacaoPda.nuCarimbo = rs.getInt("nuCarimbo");
        notificacaoPda.flTipoAlteracao = rs.getString("flTipoAlteracao");
        notificacaoPda.cdUsuario = rs.getString("cdUsuario");
        return notificacaoPda;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDNOTIFICACAO,");
        sql.append(" CDCHAVE,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" DTNOTIFICACAO,");
        sql.append(" HRNOTIFICACAO,");
        sql.append(" DSNOTIFICACAO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDNOTIFICACAO,");
        sql.append(" CDCHAVE,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" DTNOTIFICACAO,");
        sql.append(" HRNOTIFICACAO,");
        sql.append(" DSNOTIFICACAO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        NotificacaoPda notificacaoPda = (NotificacaoPda) domain;
        sql.append(Sql.getValue(notificacaoPda.cdNotificacao)).append(",");
        sql.append(Sql.getValue(notificacaoPda.cdChave)).append(",");
        sql.append(Sql.getValue(notificacaoPda.cdRepresentante)).append(",");
        sql.append(Sql.getValue(notificacaoPda.dtNotificacao)).append(",");
        sql.append(Sql.getValue(notificacaoPda.hrNotificacao)).append(",");
        sql.append(Sql.getValue(notificacaoPda.dsNotificacao)).append(",");
        sql.append(Sql.getValue(notificacaoPda.nuSequencia)).append(",");
        sql.append(Sql.getValue(notificacaoPda.nuCarimbo)).append(",");
        sql.append(Sql.getValue(notificacaoPda.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(notificacaoPda.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        NotificacaoPda notificacaoPda = (NotificacaoPda) domain;
        sql.append(" dtNotificacao = ").append(Sql.getValue(notificacaoPda.dtNotificacao)).append(",");
        sql.append(" hrNotificacao = ").append(Sql.getValue(notificacaoPda.hrNotificacao)).append(",");
        sql.append(" dsNotificacao = ").append(Sql.getValue(notificacaoPda.dsNotificacao)).append(",");
        sql.append(" nuSequencia = ").append(Sql.getValue(notificacaoPda.nuSequencia)).append(",");
        sql.append(" nuCarimbo = ").append(Sql.getValue(notificacaoPda.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(notificacaoPda.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(notificacaoPda.cdUsuario));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        NotificacaoPda notificacaoPda = (NotificacaoPda) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDNOTIFICACAO = ", notificacaoPda.cdNotificacao);
		sqlWhereClause.addAndCondition("CDCHAVE = ", notificacaoPda.cdChave);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", notificacaoPda.cdRepresentante);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public void updateNotificacaoAfterSync(String rowKey) throws SQLException {
		executeUpdate("update " + tableName + " set " + BaseDomain.NMCAMPOTIPOALTERACAO + " = " + Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL) + " where rowKey = '" + rowKey + "'");
    }
}