package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SugVendaPerson;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class SugVendaPersonDbxDao extends CrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new SugVendaPerson();
	}
	
	private static SugVendaPersonDbxDao instance;
	
	public static SugVendaPersonDbxDao getInstance() {
		if (instance == null) {
			instance = new SugVendaPersonDbxDao();
		}
		return instance;
	}

	public SugVendaPersonDbxDao() {
		super(SugVendaPerson.TABLE_NAME);
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		SugVendaPerson sugVendaPerson = new SugVendaPerson();
		sugVendaPerson.cdSugVendaPerson = rs.getString(2);
		sugVendaPerson.dsSugVendaPerson = rs.getString(3);
		sugVendaPerson.dsSugVendaPersonAbrev = rs.getString(4);
		sugVendaPerson.dtVigenciaInicial = rs.getDate(5);
		sugVendaPerson.dtVigenciaFinal = rs.getDate(6);
		sugVendaPerson.nuRelevancia = rs.getInt(7);
		sugVendaPerson.imSugVendaPerson = rs.getBytes(8);
		sugVendaPerson.dsComandoCliente = rs.getString(9);
		sugVendaPerson.dsComandoProduto = rs.getString(10);
		return sugVendaPerson;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,")
		.append(" CDSUGVENDAPERSON,")
		.append(" DSSUGVENDAPERSON,")
		.append(" DSSUGVENDAPERSONABREV,")
		.append(" DTVIGENCIAINICIAL,")
		.append(" DTVIGENCIAFINAL,")
		.append(" NURELEVANCIA,")
		.append(" IMSUGVENDAPERSON,")
		.append(" DSCOMANDOCLIENTE,")
		.append(" DSCOMANDOPRODUTO");
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
		SugVendaPerson sugVendaPerson = (SugVendaPerson)domain;
		addWhereInCdsSugs(sugVendaPerson.cdSugsFilter, sql);
	}
	
	public void addWhereInCdsSugs(List<String> cdSugs, StringBuffer sql) {
		sql.append(" WHERE CDSUGVENDAPERSON IN (");
		int size = cdSugs.size() - 1;
		String [] sugestoes = cdSugs.toArray(new String[size + 1]);
		for (int i = 0; i < size; i++) {
			sql.append(Sql.getValue(sugestoes[i])).append(",");
		}
		sql.append(Sql.getValue(sugestoes[size])).append(")")
		.append(" ORDER BY NURELEVANCIA DESC");
	}
	
	public ArrayList<String> getCdsSugPedido(Pedido pedido, String cdProduto) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		boolean useCdProduto = ValueUtil.isNotEmpty(cdProduto);
		sql.append("SELECT scli.CDSUGVENDAPERSON FROM TBLVPSUGVENDAPERSONCLI scli JOIN TBLVPSUGVENDAPERSON sug ")
		.append(" ON scli.CDSUGVENDAPERSON = sug.CDSUGVENDAPERSON");
		if (useCdProduto) {
			sql.append(" JOIN TBLVPSUGVENDAPERSONPROD sprod on sprod.CDSUGVENDAPERSON = scli.CDSUGVENDAPERSON")
			.append(" sprod.CDEMPRESA = scli.CDEMPRESA");
		}
		sql.append(" WHERE scli.CDEMPRESA = ").append(Sql.getValue(pedido.cdEmpresa))
		.append(" AND scli.CDCLIENTE = ").append(Sql.getValue(pedido.cdCliente));
		if (ValueUtil.isNotEmpty(cdProduto)) {
			sql.append(" AND sprod.CDPRODUTO = ").append(Sql.getValue(cdProduto));
		}
		sql.append(" ORDER BY sug.NURELEVANCIA DESC");
		ArrayList<String> cdsSug = new ArrayList<>();
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				cdsSug.add(rs.getString(1));
			}
		}
		return cdsSug;
	}
	
}
