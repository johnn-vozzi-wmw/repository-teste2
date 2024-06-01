package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServ;
import br.com.wmw.lavenderepda.business.domain.StatusRequisicaoServ;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RequisicaoServDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RequisicaoServ();
	}

    private static RequisicaoServDbxDao instance;
    
    public RequisicaoServDbxDao() {
        super(RequisicaoServ.TABLE_NAME);
    }

    public static RequisicaoServDbxDao getInstance() {
        if (instance == null) {
            instance = new RequisicaoServDbxDao();
        }
        return instance;
    }

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		RequisicaoServ requisicaoServ = new RequisicaoServ();
		requisicaoServ.cdEmpresa = rs.getString("cdEmpresa");
		requisicaoServ.cdRepresentante = rs.getString("cdRepresentante");
		requisicaoServ.cdRequisicaoServ = rs.getString("cdRequisicaoServ");
		requisicaoServ.motivoServ.cdRequisicaoServMotivo = rs.getString("cdRequisicaoServMotivo");
		requisicaoServ.motivoServ.dsRequisicaoServMotivo = rs.getString("dsRequisicaoServMotivo");
		requisicaoServ.motivoServ.flStatusRequisicao = rs.getString("flStatusAtual");
		requisicaoServ.tipoServ.cdRequisicaoServTipo = rs.getString("cdRequisicaoServTipo");
		requisicaoServ.tipoServ.dsRequisicaoServTipo = rs.getString("dsRequisicaoServTipo");
		requisicaoServ.tipoServ.flObrigaCliente = rs.getString("flObrigaCliente");
		requisicaoServ.tipoServ.flObrigaPedido = rs.getString("flObrigaPedido");
		requisicaoServ.cliente.cdCliente = rs.getString("cdCliente");
		requisicaoServ.cliente.nmRazaoSocial = rs.getString("nmRazaoSocial");
		requisicaoServ.nuPedido = rs.getString("nuPedido");
		requisicaoServ.dsObservacao = rs.getString("dsObservacao");
		requisicaoServ.dtRequisicaoServ = rs.getDate("dtRequisicaoServ");
		requisicaoServ.hrRequisicaoServ = rs.getString("hrRequisicaoServ");
		requisicaoServ.flTipoAlteracao = rs.getString("flTipoAlteracao");
		return requisicaoServ;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
	}

	private void addSubqueryFlStatusAtual(StringBuffer sql) {
		sql.append(" COALESCE((SELECT motivoResp.flstatusrequisicao FROM TBLVPREQUISICAOSERVRESP tbresp JOIN TBLVPREQUISICAOSERVMOTIVO motivoResp ON tbresp.CDREQUISICAOSERVMOTIVORET = motivoResp.CDREQUISICAOSERVMOTIVO")
		.append(" WHERE tbresp.cdempresa = tb.cdempresa AND tbresp.cdrepresentante = tb.cdrepresentante AND tbresp.cdrequisicaoserv = tb.cdrequisicaoserv ORDER BY dtrequisicaoservresp DESC, hrrequisicaoservresp DESC")
		.append(" LIMIT 1), motivo.flStatusRequisicao) AS flStatusAtual ");
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
		sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDREQUISICAOSERV,");
        sql.append(" CDREQUISICAOSERVTIPO,");
        sql.append(" CDREQUISICAOSERVMOTIVO,");
        sql.append(" CDCLIENTE,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" DTREQUISICAOSERV,");
        sql.append(" HRREQUISICAOSERV,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIOCRIACAO,");
        sql.append(" CDUSUARIO");
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
		RequisicaoServ requisicaoServ = (RequisicaoServ) domain; 
    	sql.append(Sql.getValue(requisicaoServ.cdEmpresa)).append(",");
        sql.append(Sql.getValue(requisicaoServ.cdRepresentante)).append(",");
        sql.append(Sql.getValue(requisicaoServ.cdRequisicaoServ)).append(",");
        sql.append(Sql.getValue(requisicaoServ.tipoServ.cdRequisicaoServTipo)).append(",");
        sql.append(Sql.getValue(requisicaoServ.motivoServ.cdRequisicaoServMotivo)).append(",");
        sql.append(Sql.getValue(requisicaoServ.cdCliente)).append(",");
        sql.append(Sql.getValue(requisicaoServ.nuPedido)).append(",");
        sql.append(Sql.getValue(requisicaoServ.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(requisicaoServ.dtRequisicaoServ)).append(",");
        sql.append(Sql.getValue(requisicaoServ.hrRequisicaoServ)).append(",");
        sql.append(Sql.getValue(requisicaoServ.dsObservacao)).append(",");
        sql.append(Sql.getValue(requisicaoServ.nuCarimbo)).append(",");
        sql.append(Sql.getValue(requisicaoServ.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(requisicaoServ.cdUsuarioCriacao)).append(",");
        sql.append(Sql.getValue(requisicaoServ.cdUsuario));
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		RequisicaoServ requisicaoServ = (RequisicaoServ) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(" CDEMPRESA", requisicaoServ.cdEmpresa != null ? requisicaoServ.cdEmpresa : SessionLavenderePda.cdEmpresa);
		if (!SessionLavenderePda.isUsuarioSupervisor()) {
			sqlWhereClause.addAndConditionEquals(" CDREPRESENTANTE", requisicaoServ.cdRepresentante != null ? requisicaoServ.cdRepresentante : SessionLavenderePda.getRepresentante().cdRepresentante);
		}
		sqlWhereClause.addAndConditionEquals(" CDREQUISICAOSERVTIPO", requisicaoServ.tipoServ.cdRequisicaoServTipo);
		sqlWhereClause.addAndConditionEquals(" FLSTATUSATUAL", requisicaoServ.motivoServ.flStatusRequisicao);
		sqlWhereClause.addAndCondition(" DTREQUISICAOSERV <= ", requisicaoServ.dtFimFilter);
		sqlWhereClause.addAndCondition(" DTREQUISICAOSERV >= ", requisicaoServ.dtInicioFilter);
		sql.append(sqlWhereClause.getSql());
	}

	protected void addJoin(StringBuffer sql) {
		sql.append(" LEFT OUTER JOIN TBLVPCLIENTE cliente ON");
		sql.append(" tb.CDEMPRESA = cliente.CDEMPRESA and tb.CDREPRESENTANTE = cliente.CDREPRESENTANTE and tb.CDCLIENTE = cliente.CDCLIENTE");
		sql.append(" JOIN TBLVPREQUISICAOSERVTIPO tipo ON");
		sql.append(" tb.CDREQUISICAOSERVTIPO = tipo.CDREQUISICAOSERVTIPO");
		sql.append(" JOIN TBLVPREQUISICAOSERVMOTIVO motivo ON");
		sql.append(" tb.CDREQUISICAOSERVMOTIVO = motivo.CDREQUISICAOSERVMOTIVO");
	}
	
	@Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if ((domain != null) && !ValueUtil.isEmpty(domain.sortAtributte)) {
    		sql.append(" order by ");
    		String[] sortAtributtes = StringUtil.split(domain.sortAtributte, ',');
    		String[] sortAsc = StringUtil.split(domain.sortAsc, ',');
    		for (int i = 0; i < sortAtributtes.length; i++) {
    			sql.append(sortAtributtes[i]).append(ValueUtil.VALOR_SIM.equals(sortAsc[i]) ? " ASC" : " DESC");
    			if (!(i == (sortAtributtes.length - 1))) {
    				sql.append(" , ");
    			}
			}
    	}
	}
	
	@Override
	public Vector findAllByExample(BaseDomain domain) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select * from (select  tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDREQUISICAOSERV, tb.DTREQUISICAOSERV, tb.HRREQUISICAOSERV, tb.NUPEDIDO,")
		.append("tipo.CDREQUISICAOSERVTIPO, tipo.DSREQUISICAOSERVTIPO, tipo.FLOBRIGACLIENTE, tipo.FLOBRIGAPEDIDO, cliente.CDCLIENTE, cliente.NMRAZAOSOCIAL, ")
		.append("motivo.CDREQUISICAOSERVMOTIVO, motivo.DSREQUISICAOSERVMOTIVO, tb.DSOBSERVACAO, tb.FLTIPOALTERACAO,");
		addSubqueryFlStatusAtual(sql);
		sql.append(" from TBLVPREQUISICAOSERV tb ");
		addJoin(sql);
		sql.append(") Requisicao");
		addWhereByExample(domain, sql);
		addOrderBy(sql, domain);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector requisicaoServList = new Vector();
			while (rs.next()) {
				requisicaoServList.addElement(populate(domain, rs));
			}
			return requisicaoServList;
		}
	}
	
	@Override
	public BaseDomain findByRowKey(String rowKey) throws SQLException {
		BaseDomain domainFilter = getBaseDomainDefault();
		StringBuffer sql = getSqlBuffer();
        sql.append(" select tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDREQUISICAOSERV, tb.FLTIPOALTERACAO, motivo.CDREQUISICAOSERVMOTIVO, tb.NUPEDIDO,")
        .append(" motivo.DSREQUISICAOSERVMOTIVO, tipo.CDREQUISICAOSERVTIPO,  tipo.DSREQUISICAOSERVTIPO, tipo.FLOBRIGACLIENTE, tipo.FLOBRIGAPEDIDO, cliente.CDCLIENTE, cliente.NMRAZAOSOCIAL, tb.DSOBSERVACAO, tb.DTREQUISICAOSERV, tb.HRREQUISICAOSERV,");
        addSubqueryFlStatusAtual(sql);
        sql.append("from TBLVPREQUISICAOSERV tb ");
        addJoin(sql);
        addJoin(domainFilter, sql);
        sql.append(" where tb.rowKey =").append(Sql.getValue(rowKey));
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return populate(domainFilter, rs);
			}
		}
        return null;
	}

	public int findByNewRequisicao(RequisicaoServ requisicaoServFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select count(1) from (select  ");
		addSubqueryFlStatusAtual(sql);
		sql.append(" from  TBLVPREQUISICAOSERV tb JOIN TBLVPREQUISICAOSERVMOTIVO motivo ON tb.CDREQUISICAOSERVMOTIVO = motivo.CDREQUISICAOSERVMOTIVO");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals(" tb.CDEMPRESA ", requisicaoServFilter.cdEmpresa);
		sqlWhereClause.addAndConditionEquals(" tb.CDCLIENTE ", requisicaoServFilter.cdCliente);
		sqlWhereClause.addAndConditionEquals(" tb.CDREQUISICAOSERVTIPO ", requisicaoServFilter.tipoServ.cdRequisicaoServTipo);
		sqlWhereClause.addStartAndMultipleCondition();
		sqlWhereClause.addAndConditionEquals(" FLSTATUSATUAL ", StatusRequisicaoServ.FLSTATUSANDAMENTO);
		sqlWhereClause.addOrCondition(" FLSTATUSATUAL = ", StatusRequisicaoServ.FLSTATUSPENDENTE);
		sqlWhereClause.addEndMultipleCondition();
		sql.append(sqlWhereClause.getSql());
		sql.append(")");
		return getInt(sql.toString());
	}
	
}
