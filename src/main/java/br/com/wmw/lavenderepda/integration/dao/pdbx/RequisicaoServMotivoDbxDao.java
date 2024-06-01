package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServMotivo;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RequisicaoServMotivoDbxDao extends CrudDbxDao  {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RequisicaoServMotivo();
	}
	
    private static RequisicaoServMotivoDbxDao instance;
    
    public RequisicaoServMotivoDbxDao() {
        super(RequisicaoServMotivo.TABLE_NAME);
    }

    public static RequisicaoServMotivoDbxDao getInstance() {
        if (instance == null) {
            instance = new RequisicaoServMotivoDbxDao();
        }
        return instance;
    }

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		RequisicaoServMotivo requisicaoMotivoServ = new RequisicaoServMotivo();
		requisicaoMotivoServ.cdRequisicaoServMotivo = rs.getString("cdRequisicaoServMotivo");
		requisicaoMotivoServ.dsRequisicaoServMotivo = rs.getString("dsRequisicaoServMotivo");
		requisicaoMotivoServ.flUsoMotivo = rs.getString("flUsoMotivo");
		requisicaoMotivoServ.flObrigaObservacao = rs.getString("flObrigaObservacao");
		requisicaoMotivoServ.flStatusRequisicao = rs.getString("flStatusRequisicao");
		return requisicaoMotivoServ;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" CDREQUISICAOSERVMOTIVO,");
		sql.append(" DSREQUISICAOSERVMOTIVO,");
		sql.append(" FLUSOMOTIVO,");
		sql.append(" FLOBRIGAOBSERVACAO,");
		sql.append(" FLSTATUSREQUISICAO");
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
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
	
	public Vector findAllByUsoMotivo(String cdTipo) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT DISTINCT tb.CDREQUISICAOSERVMOTIVO, tb.DSREQUISICAOSERVMOTIVO, tb.FLUSOMOTIVO, tb.FLOBRIGAOBSERVACAO, tb.FLSTATUSREQUISICAO FROM TBLVPREQUISICAOSERVMOTIVO tb")
		.append(" JOIN TBLVPREQUISICAOSERVTIPMOT tipmot ON tb.CDREQUISICAOSERVMOTIVO = tipmot.CDREQUISICAOSERVMOTIVO")
		.append(" WHERE tipmot.CDREQUISICAOSERVTIPO = ").append(Sql.getValue(cdTipo))
		.append(" AND (FLUSOMOTIVO = '0' OR FLUSOMOTIVO = '1')")
		.append(" ORDER BY tb.DSREQUISICAOSERVMOTIVO ASC");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector requisicaoServMotivoList = new Vector();	
			while (rs.next()) {
				RequisicaoServMotivo requisicaoServMotivo = new RequisicaoServMotivo();
				requisicaoServMotivo.cdRequisicaoServMotivo = rs.getString("cdRequisicaoServMotivo");
				requisicaoServMotivo.dsRequisicaoServMotivo = rs.getString("dsRequisicaoServMotivo");
				requisicaoServMotivo.flUsoMotivo = rs.getString("flUsoMotivo");
				requisicaoServMotivo.flObrigaObservacao = rs.getString("flObrigaObservacao");
				requisicaoServMotivo.flStatusRequisicao = rs.getString("flStatusRequisicao");
				requisicaoServMotivoList.addElement(requisicaoServMotivo);
			}
			return requisicaoServMotivoList;
		}
	}

}
