package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServ;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServResp;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RequisicaoServRespDbxDao extends CrudDbxDao  {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RequisicaoServResp();
	}

    private static RequisicaoServRespDbxDao instance;

	public RequisicaoServRespDbxDao() {
		super(RequisicaoServResp.TABLE_NAME);
	}
	
    public static RequisicaoServRespDbxDao getInstance() {
        if (instance == null) {
            instance = new RequisicaoServRespDbxDao();
        }
        return instance;
    }

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return null;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
	}
	
	public Vector findRespostasRequisicao(RequisicaoServ requisicaoServ) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDREQUISICAOSERV, tb.CDREQUISICAOSERVRESP, tb.DTREQUISICAOSERVRESP, tb.HRREQUISICAOSERVRESP,")
		.append(" tbmotivo.CDREQUISICAOSERVMOTIVO, tbmotivo.DSREQUISICAOSERVMOTIVO, tbmotivo.FLSTATUSREQUISICAO")
		.append(" FROM TBLVPREQUISICAOSERVRESP tb JOIN TBLVPREQUISICAOSERVMOTIVO tbmotivo ON tb.CDREQUISICAOSERVMOTIVORET = tbmotivo.CDREQUISICAOSERVMOTIVO")
		.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(requisicaoServ.cdEmpresa))
		.append(" AND tb.CDREPRESENTANTE = ").append(Sql.getValue(requisicaoServ.cdRepresentante))
		.append(" AND tb.CDREQUISICAOSERV = ").append(Sql.getValue(requisicaoServ.cdRequisicaoServ));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector requisicaoServRespList = new Vector();
			while (rs.next()) {
				RequisicaoServResp requisicaoServResp = new RequisicaoServResp();
				requisicaoServResp.cdEmpresa = rs.getString("cdEmpresa");
				requisicaoServResp.cdRepresentante = rs.getString("cdRepresentante");
				requisicaoServResp.cdRequisicaoServ = rs.getString("cdRequisicaoServ");
				requisicaoServResp.cdRequisicaoServResp = rs.getString("cdRequisicaoServResp");
				requisicaoServResp.dtRequisicaoServResp = rs.getDate("dtRequisicaoServResp");
				requisicaoServResp.hrRequisicaoServResp = rs.getString("hrRequisicaoServResp");
				requisicaoServResp.motivoServ.cdRequisicaoServMotivo = rs.getString("cdRequisicaoServMotivo");
				requisicaoServResp.motivoServ.dsRequisicaoServMotivo = rs.getString("dsRequisicaoServMotivo");
				requisicaoServResp.motivoServ.flStatusRequisicao = rs.getString("flStatusRequisicao");
				requisicaoServRespList.addElement(requisicaoServResp);
			}
			return requisicaoServRespList;
		}
	}
	
	@Override
	public BaseDomain findByRowKey(String rowKey) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT tb.CDEMPRESA, tb.CDREPRESENTANTE, tb.CDREQUISICAOSERV, tb.CDREQUISICAOSERVRESP, tb.DTREQUISICAOSERVRESP, tb.HRREQUISICAOSERVRESP, tb.DSOBSERVACAO, tb.CDUSUARIOCRIACAO, tb.NMUSUARIOCRIACAO,")
		.append(" tbmotivo.CDREQUISICAOSERVMOTIVO, tbmotivo.DSREQUISICAOSERVMOTIVO, tbmotivo.FLSTATUSREQUISICAO")
		.append(" FROM TBLVPREQUISICAOSERVRESP tb JOIN TBLVPREQUISICAOSERVMOTIVO tbmotivo ON tb.CDREQUISICAOSERVMOTIVORET = tbmotivo.CDREQUISICAOSERVMOTIVO")
		.append(" WHERE tb.rowKey = ").append(Sql.getValue(rowKey));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			RequisicaoServResp requisicaoServResp = new RequisicaoServResp();
			if (rs.next()) {
				requisicaoServResp.cdRequisicaoServResp = rs.getString("cdRequisicaoServResp");
				requisicaoServResp.dtRequisicaoServResp = rs.getDate("dtRequisicaoServResp");
				requisicaoServResp.hrRequisicaoServResp = rs.getString("hrRequisicaoServResp");
				requisicaoServResp.dsObservacao = rs.getString("dsObservacao");
				requisicaoServResp.cdUsuarioCriacao = rs.getString("cdUsuarioCriacao");
				requisicaoServResp.nmUsuarioCriacao = rs.getString("nmUsuarioCriacao");
				requisicaoServResp.motivoServ.cdRequisicaoServMotivo = rs.getString("cdRequisicaoServMotivo");
				requisicaoServResp.motivoServ.dsRequisicaoServMotivo = rs.getString("dsRequisicaoServMotivo");
				requisicaoServResp.motivoServ.flStatusRequisicao = rs.getString("flStatusRequisicao");
			}
			return requisicaoServResp;
		}
	}

}
