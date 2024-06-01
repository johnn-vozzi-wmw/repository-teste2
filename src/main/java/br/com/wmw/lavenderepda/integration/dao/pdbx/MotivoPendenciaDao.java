package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MotivoPendencia;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class MotivoPendenciaDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new MotivoPendencia();
	}

    private static MotivoPendenciaDao instance;

    public MotivoPendenciaDao() {
        super(MotivoPendencia.TABLE_NAME);
    }

    public static MotivoPendenciaDao getInstance() {
        if (instance == null) {
            instance = new MotivoPendenciaDao();
        }
        return instance;
    }

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		MotivoPendencia motivoPendencia = new MotivoPendencia();
		motivoPendencia.rowKey = rs.getString("rowkey");
		motivoPendencia.cdEmpresa = rs.getString("cdEmpresa");
		motivoPendencia.cdRepresentante = rs.getString("cdRepresentante");
		motivoPendencia.cdMotivoPendencia = rs.getString("cdMotivoPendencia");
		motivoPendencia.dsMotivoPendencia = rs.getString("dsMotivoPendencia");
		motivoPendencia.flRegraLiberacao = rs.getString("flRegraLiberacao");
		motivoPendencia.nuPrioridadeJust = rs.getInt("nuPrioridadeJust");
		motivoPendencia.flTipoAlteracao = rs.getString("flTipoAlteracao");
		motivoPendencia.nuCarimbo = rs.getInt("nuCarimbo");
		motivoPendencia.cdUsuario = rs.getString("cdUsuario");
		return motivoPendencia;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDMOTIVOPENDENCIA,");
		sql.append(" tb.DSMOTIVOPENDENCIA,");
		sql.append(" tb.FLREGRALIBERACAO,");
		sql.append(" tb.NUPRIORIDADEJUST,");
		sql.append(" tb.FLTIPOALTERACAO,");
		sql.append(" tb.NUCARIMBO,");
		sql.append(" tb.CDUSUARIO");
		
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException { }

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException { }

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		MotivoPendencia motivoPendencia = (MotivoPendencia) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", motivoPendencia.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", motivoPendencia.cdRepresentante);
		sqlWhereClause.addAndCondition("CDMOTIVOPENDENCIA = ", motivoPendencia.cdMotivoPendencia);
		sql.append(sqlWhereClause.getSql());
		
	}

	public Vector findMotivoPendenciaPrincipalPedido(MotivoPendencia motivoPendencia, boolean isJoinComTabelasErp) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(motivoPendencia, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addJoinsMotivoPendeciaByPedido(sql, isJoinComTabelasErp);
		addWhereMotivoPendenciaByPedido(motivoPendencia, sql);
		addOrderByMotivoPendenciaByPedido(sql);
		addLimit(sql, motivoPendencia);
		return findAll(motivoPendencia, sql.toString());
	}
	
	public Vector findMotivoPendenciaPrincipalPedidoErp(MotivoPendencia motivoPendencia) throws SQLException {
		return VectorUtil.concatVectors(findMotivoPendenciaPrincipalPedido(motivoPendencia, false), findMotivoPendenciaPrincipalPedido(motivoPendencia, true));
	}

	private void addWhereMotivoPendenciaByPedido(MotivoPendencia motivoPendencia, StringBuffer sql) {
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition(" ped.CDEMPRESA = ", motivoPendencia.cdEmpresa);
		if (!SessionLavenderePda.isUsuarioSupervisor()) {
			sqlWhereClause.addAndCondition(" ped.CDREPRESENTANTE = ", motivoPendencia.cdRepresentante);
		}
		sqlWhereClause.addAndCondition(" ped.NUPEDIDO = ", motivoPendencia.nuPedido);
		sql.append(sqlWhereClause.getSql());
	}

	private void addJoinsMotivoPendeciaByPedido(StringBuffer sql, boolean isUsaTabelasErp) {
		sql.append(isUsaTabelasErp ? " JOIN TBLVPITEMPEDIDOERP itp ON " : " JOIN TBLVPITEMPEDIDO itp ON ");
		sql.append(" itp.CDEMPRESA = tb.CDEMPRESA AND");
		if (!SessionLavenderePda.isUsuarioSupervisor()) {
			sql.append(" itp.CDREPRESENTANTE = tb.CDREPRESENTANTE AND");
		}
		sql.append(" itp.CDMOTIVOPENDENCIA = tb.CDMOTIVOPENDENCIA");
		sql.append(isUsaTabelasErp ? " JOIN TBLVPPEDIDOERP ped ON " : " JOIN TBLVPPEDIDO ped ON ");
		sql.append(" itp.CDEMPRESA = ped.CDEMPRESA AND");
		sql.append(" itp.CDREPRESENTANTE = ped.CDREPRESENTANTE AND");
		sql.append(" itp.FLORIGEMPEDIDO = ped.FLORIGEMPEDIDO AND");
		sql.append(" itp.NUPEDIDO = ped.NUPEDIDO");
	}
	
	private void addOrderByMotivoPendenciaByPedido(StringBuffer sql) {
		sql.append(" ORDER BY itp.NUORDEMLIBERACAO DESC, tb.NUPRIORIDADEJUST ASC");
	}

}