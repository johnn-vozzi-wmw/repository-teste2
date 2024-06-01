package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.TransportadoraCep;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class TransportadoraCepDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TransportadoraCep();
	}

	private static TransportadoraCepDbxDao instance = null;
	
	   public TransportadoraCepDbxDao() {
	        super(TransportadoraCep.TABLE_NAME);
	    }
	   
	   public static TransportadoraCepDbxDao getInstance() {
	        if (instance == null) {
	            instance = new TransportadoraCepDbxDao();
	        }
	        return instance;
	   }

	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		TransportadoraCep transportadoraCep = new TransportadoraCep();
		transportadoraCep.cdEmpresa = rs.getString("cdEmpresa");
		transportadoraCep.cdTransportadora = rs.getString("cdTransportadora");
		transportadoraCep.cdTipoServicoFrete = rs.getString("cdTipoServicoFrete");
		transportadoraCep.nuFaixaCepInicial = rs.getString("nuFaixaCepInicial");
		transportadoraCep.nuFaixaCepFinal = rs.getString("nuFaixaCepFinal");
		transportadoraCep.nuDiasEntrega = rs.getInt("nuDiasEntrega");
		transportadoraCep.vlPctFrete = ValueUtil.round(rs.getDouble("vlPctFrete"));
		transportadoraCep.vlMinFrete = ValueUtil.round(rs.getDouble("vlMinFrete"));
		transportadoraCep.vlTaxaColeta = ValueUtil.round(rs.getDouble("vlTaxaColeta"));
		return transportadoraCep;
	}
	
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDTRANSPORTADORA,");
        sql.append(" tb.CDTIPOSERVICOFRETE,");
        sql.append(" tb.NUFAIXACEPINICIAL,");
        sql.append(" tb.NUFAIXACEPFINAL,");
        sql.append(" tb.NUDIASENTREGA,");
        sql.append(" tb.VLPCTFRETE,");
        sql.append(" tb.VLMINFRETE,");
        sql.append(" tb.VLTAXACOLETA,");
        sql.append(" tb.ROWKEY");  
	}
	
	protected void addInsertColumns(StringBuffer sql) throws SQLException {
	}
	
	protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}
	
	protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
	}
	
	protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
	}

	public Vector findTranspCepByCliente(Cliente cliente) throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append("select tp.NMTRANSPORTADORA, tc.CDEMPRESA, tc.CDTRANSPORTADORA, tc.CDTIPOSERVICOFRETE, tc.NUFAIXACEPINICIAL, tc.NUFAIXACEPFINAL, tc.NUDIASENTREGA, tc.VLPCTFRETE, tc.VLMINFRETE, tc.VLTAXACOLETA from ")
    		.append(tableName).append(" tc");
    	sql.append(" join TBLVPTRANSPORTADORA tp on tc.CDTRANSPORTADORA = tp.CDTRANSPORTADORA and tc.CDEMPRESA = tp.CDEMPRESA");
    	sql.append(" where ").append(Sql.getValue(cliente.dsCepComercial.replaceAll("[^0-9]", "")));
    	sql.append(" between tc.NUFAIXACEPINICIAL and tc.NUFAIXACEPFINAL");
    	sql.append(" and tc.CDEMPRESA = ").append(Sql.getValue(cliente.cdEmpresa));
    	sql.append(" order by tc.VLPCTFRETE, tp.NMTRANSPORTADORA");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			Vector transporteCepList = new Vector();
			while (rs.next()) {
				TransportadoraCep transportadoraCep = new TransportadoraCep();
				transportadoraCep.cdEmpresa = rs.getString("cdEmpresa");
				transportadoraCep.cdTransportadora = rs.getString("cdTransportadora");
				transportadoraCep.nmTransportadora = rs.getString("nmTransportadora");
				transportadoraCep.cdTipoServicoFrete = rs.getString("cdTipoServicoFrete");
				transportadoraCep.nuFaixaCepInicial = rs.getString("nuFaixaCepInicial");
				transportadoraCep.nuFaixaCepFinal = rs.getString("nuFaixaCepFinal");
				transportadoraCep.nuDiasEntrega = rs.getInt("nuDiasEntrega");
				transportadoraCep.vlPctFrete = ValueUtil.round(rs.getDouble("vlPctFrete"));
				transportadoraCep.vlMinFrete = ValueUtil.round(rs.getDouble("vlMinFrete"));
				transportadoraCep.vlTaxaColeta = ValueUtil.round(rs.getDouble("vlTaxaColeta"));
				transporteCepList.addElement(transportadoraCep);
			}
			return transporteCepList;
		}
	}

	public TransportadoraCep findTransportadoraCepByCdTransportadoraAndDsCepComercial(String cdTransportadora, String dsCepComercial) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("select * from ").append(tableName);
		sql.append(" where CDTRANSPORTADORA = ").append(cdTransportadora);
		sql.append(" and ").append(Sql.getValue(dsCepComercial == null ? dsCepComercial : dsCepComercial.replaceAll("[^0-9]", "")));
    	sql.append(" between NUFAIXACEPINICIAL and NUFAIXACEPFINAL");
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				TransportadoraCep transportadoraCep = new TransportadoraCep();
				transportadoraCep.cdEmpresa = rs.getString("cdEmpresa");
				transportadoraCep.cdTransportadora = rs.getString("cdTransportadora");
				transportadoraCep.cdTipoServicoFrete = rs.getString("cdTipoServicoFrete");
				transportadoraCep.nuFaixaCepInicial = rs.getString("nuFaixaCepInicial");
				transportadoraCep.nuFaixaCepFinal = rs.getString("nuFaixaCepFinal");
				transportadoraCep.nuDiasEntrega = rs.getInt("nuDiasEntrega");
				transportadoraCep.vlPctFrete = ValueUtil.round(rs.getDouble("vlPctFrete"));
				transportadoraCep.vlMinFrete = ValueUtil.round(rs.getDouble("vlMinFrete"));
				transportadoraCep.vlTaxaColeta = ValueUtil.round(rs.getDouble("vlTaxaColeta"));
				return transportadoraCep;
			}
			return null;
		}	
	}
	   
	   
	  
}
