package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudPersonDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServTipMot;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServTipo;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class RequisicaoServTipMotDbxDao extends CrudPersonDbxDao  {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RequisicaoServTipMot();
	}

    private static RequisicaoServTipMotDbxDao instance;

	public RequisicaoServTipMotDbxDao() {
		super(RequisicaoServTipMot.TABLE_NAME);
	}
	
    public static RequisicaoServTipMotDbxDao getInstance() {
        if (instance == null) {
            instance = new RequisicaoServTipMotDbxDao();
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
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {		
	}

	@Override
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
	}
	
	public Vector findAllTipo(RequisicaoServTipMot requisicaoServTipMotFilter) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT tipo.CDREQUISICAOSERVTIPO, tipo.DSREQUISICAOSERVTIPO, tipo.FLOBRIGACLIENTE, tipo.FLFILTROSTATUSCLIENTE, tipo.DSSTATUSCLIENTELIST, tipo.FLOBRIGAPEDIDO FROM TBLVPREQUISICAOSERVTIPMOT tb")
		.append(" JOIN TBLVPREQUISICAOSERVTIPO tipo ON tb.CDREQUISICAOSERVTIPO = tipo.CDREQUISICAOSERVTIPO")
		.append(" WHERE tb.CDEMPRESA = ").append(Sql.getValue(requisicaoServTipMotFilter.cdEmpresa))
		.append(" AND (tb.CDREPRESENTANTE = ").append(Sql.getValue(requisicaoServTipMotFilter.cdRepresentante)).append(" OR tb.CDREPRESENTANTE = ").append(Sql.getValue(ValueUtil.VALOR_ZERO)).append(")");
		if (requisicaoServTipMotFilter.obrigaCliente) {
			sql.append(" AND tipo.FLOBRIGACLIENTE = 'S'");
		}
		if (requisicaoServTipMotFilter.obrigaPedido) {
			sql.append(" AND tipo.FLOBRIGAPEDIDO = 'S'");	
		}
		sql.append(" ORDER BY tipo.DSREQUISICAOSERVTIPO ASC");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector requisicaoServTipoList = new Vector();
			while (rs.next()) {
				RequisicaoServTipo requisicaoServTipo = new RequisicaoServTipo();
				requisicaoServTipo.cdRequisicaoServTipo = rs.getString("cdRequisicaoServTipo");
				requisicaoServTipo.dsRequisicaoServTipo = rs.getString("dsRequisicaoServTipo");
				requisicaoServTipo.flFiltroStatusCliente = rs.getString("flFiltroStatusCliente");
				requisicaoServTipo.dsStatusClienteList = rs.getString("dsStatusClienteList");
				requisicaoServTipo.flObrigaCliente = rs.getString("flObrigaCliente");
				requisicaoServTipo.flObrigaPedido = rs.getString("flObrigaPedido");
				requisicaoServTipoList.addElement(requisicaoServTipo);
			}
			return requisicaoServTipoList;
		}
	}

}
