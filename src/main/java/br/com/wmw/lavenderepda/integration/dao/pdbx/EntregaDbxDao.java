package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Entrega;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;
import totalcross.util.Vector;

public class EntregaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Entrega();
	}

    private static EntregaDbxDao instance = null;
	public static String TABLE_NAME = "TBLVPENTREGA";

    public EntregaDbxDao() {
        super(TABLE_NAME);
    }
    
    public static EntregaDbxDao getInstance() {
        if (instance == null) {
            instance = new EntregaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Entrega entrega = new Entrega();
        entrega.rowKey = rs.getString("rowkey");
        entrega.cdEmpresa = rs.getString("cdEmpresa");
        entrega.cdRepresentante = rs.getString("cdRepresentante");
        entrega.cdEntrega = rs.getString("cdEntrega");
        entrega.dsEntrega = rs.getString("dsEntrega");
        entrega.cdTabelaPreco = rs.getString("cdTabelaPreco");
        entrega.vlMinPedido = ValueUtil.round(rs.getDouble("vlMinPedido"));
        entrega.qtMinProduto = rs.getInt("qtMinProduto");
        entrega.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
        entrega.dtVigenciaFinal = rs.getDate("dtVigenciaFinal");
        entrega.flTipoAlteracao = rs.getString("flTipoAlteracao");
        entrega.cdUsuario = rs.getString("cdUsuario");
        entrega.nuCarimbo = rs.getInt("nuCarimbo");
        entrega.dtAlteracao = rs.getDate("dtAlteracao");
        entrega.hrAlteracao = rs.getString("hrAlteracao");
        return entrega;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDENTREGA,");
        sql.append(" DSENTREGA,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" VLMINPEDIDO,");
        sql.append(" QTMINPRODUTO,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDENTREGA,");
        sql.append(" DSENTREGA,");
        sql.append(" CDTABELAPRECO,");
        sql.append(" VLMINPEDIDO,");
        sql.append(" QTMINPRODUTO,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        Entrega entrega = (Entrega) domain;
        sql.append(Sql.getValue(entrega.cdEmpresa)).append(",");
        sql.append(Sql.getValue(entrega.cdRepresentante)).append(",");
        sql.append(Sql.getValue(entrega.cdEntrega)).append(",");
        sql.append(Sql.getValue(entrega.dsEntrega)).append(",");
        sql.append(Sql.getValue(entrega.cdTabelaPreco)).append(",");
        sql.append(Sql.getValue(entrega.vlMinPedido)).append(",");
        sql.append(Sql.getValue(entrega.qtMinProduto)).append(",");
        sql.append(Sql.getValue(entrega.dtVigenciaInicial)).append(",");
        sql.append(Sql.getValue(entrega.dtVigenciaFinal)).append(",");
        sql.append(Sql.getValue(entrega.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(entrega.cdUsuario)).append(",");
        sql.append(Sql.getValue(entrega.nuCarimbo)).append(",");
        sql.append(Sql.getValue(entrega.dtAlteracao)).append(",");
        sql.append(Sql.getValue(entrega.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        Entrega entrega = (Entrega) domain;
        sql.append(" DSENTREGA = ").append(Sql.getValue(entrega.dsEntrega)).append(",");
        sql.append(" CDTABELAPRECO = ").append(Sql.getValue(entrega.cdTabelaPreco)).append(",");
        sql.append(" VLMINPEDIDO = ").append(Sql.getValue(entrega.vlMinPedido)).append(",");
        sql.append(" QTMINPRODUTO = ").append(Sql.getValue(entrega.qtMinProduto)).append(",");
        sql.append(" DTVIGENCIAINICIAL = ").append(Sql.getValue(entrega.dtVigenciaInicial)).append(",");
        sql.append(" DTVIGENCIAFINAL = ").append(Sql.getValue(entrega.dtVigenciaFinal)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(entrega.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(entrega.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(entrega.nuCarimbo)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(entrega.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(entrega.hrAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        Entrega entrega = (Entrega) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", entrega.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", entrega.cdRepresentante);
		sqlWhereClause.addAndCondition("CDENTREGA = ", entrega.cdEntrega);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    public Vector findAllByExample(BaseDomain domain) throws SQLException {
    	Date dataAtual = DateUtil.getCurrentDate();
    	Entrega entrega = (Entrega) domain;
		StringBuffer sql = getSqlBuffer();
        sql.append(" select * from ");
        sql.append(tableName);
        sql.append(" where CDEMPRESA = ").append(Sql.getValue(entrega.cdEmpresa)).append(" and CDREPRESENTANTE = ").append(Sql.getValue(entrega.cdRepresentante));
        sql.append(" and DTVIGENCIAINICIAL <= ").append(Sql.getValue(dataAtual)).append(" and DTVIGENCIAFINAL >= ").append(Sql.getValue(dataAtual));
        if (!entrega.ignoraValidacao) {
        	sql.append(" and (QTMINPRODUTO <= ").append(Sql.getValue(entrega.qtMinProduto)).append(" or QTMINPRODUTO = 0 or QTMINPRODUTO IS NULL)");
        	sql.append(" and (VLMINPEDIDO <= ").append(Sql.getValue(entrega.vlMinPedido)).append(" or VLMINPEDIDO = 0 OR VLMINPEDIDO IS NULL)");
        }
        if (ValueUtil.isNotEmpty(entrega.cdTabelaPreco)) {
        	sql.append(" and (CDTABELAPRECO = ").append(Sql.getValue(entrega.cdTabelaPreco)).append(" or CDTABELAPRECO is null or CDTABELAPRECO = '')");
        }
        try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
			Vector listEntrega = new Vector();
			while (rs.next()) {
				listEntrega.addElement(populate(domain, rs));
			}
			return listEntrega;
		}
    }

    public Vector findEntregaFechamentoPedido(BaseDomain domain) throws SQLException {
    	Date dataAtual = DateUtil.getCurrentDate();
    	Entrega entrega = (Entrega) domain;
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select * from ");
    	sql.append(tableName);
    	sql.append(" where CDEMPRESA = ").append(Sql.getValue(entrega.cdEmpresa)).append(" and CDREPRESENTANTE = ").append(Sql.getValue(entrega.cdRepresentante)).append(" and CDENTREGA = ").append(Sql.getValue(entrega.cdEntrega));
    	sql.append(" and DTVIGENCIAINICIAL <= ").append(Sql.getValue(dataAtual)).append(" and DTVIGENCIAFINAL >= ").append(Sql.getValue(dataAtual));
    	try (Statement st = getCurrentDriver().getStatement();
 				ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector listEntrega = new Vector();
    		while (rs.next()) {
    			listEntrega.addElement(populate(domain, rs));
    		}
    		return listEntrega;
    	}
    }
}