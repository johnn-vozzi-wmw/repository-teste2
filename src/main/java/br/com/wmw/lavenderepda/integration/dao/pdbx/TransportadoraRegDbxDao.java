package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Regiao;
import br.com.wmw.lavenderepda.business.domain.TipoFrete;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.business.domain.TransportadoraReg;
import br.com.wmw.lavenderepda.util.DaoUtil;
import totalcross.sql.ResultSet;

public class TransportadoraRegDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TransportadoraReg();
	}

    private static TransportadoraRegDbxDao instance = null;

    public TransportadoraRegDbxDao() {
        super(TransportadoraReg.TABLE_NAME);
    }
    
    public static TransportadoraRegDbxDao getInstance() {
        if (instance == null) {
            instance = new TransportadoraRegDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		TransportadoraReg transpRegFilter = (TransportadoraReg) domainFilter;
        TransportadoraReg transportadoraReg = new TransportadoraReg();
        transportadoraReg.rowKey = rs.getString("rowkey");
        transportadoraReg.cdEmpresa = rs.getString("cdEmpresa");
        transportadoraReg.cdRepresentante = rs.getString("cdRepresentante");
        transportadoraReg.cdTransportadora = rs.getString("cdTransportadora");
        transportadoraReg.cdRegiao = rs.getString("cdRegiao");
        transportadoraReg.cdTipoFrete = rs.getString("cdTipoFrete");
        transportadoraReg.vlMinPedido = ValueUtil.round(rs.getDouble("vlMinPedido"));
        transportadoraReg.flPossuiContrato = rs.getString("flPossuiContrato");
        transportadoraReg.vlPctFrete = ValueUtil.round(rs.getDouble("vlPctFrete"));
        transportadoraReg.vlMinFrete = ValueUtil.round(rs.getDouble("vlMinFrete"));
        transportadoraReg.vlMaxPedido = ValueUtil.round(rs.getDouble("vlMaxPedido"));
        transportadoraReg.cdUsuario = rs.getString("cdUsuario");
        transportadoraReg.flTipoAlteracao = rs.getString("flTipoAlteracao");
        transportadoraReg.nuCarimbo = rs.getInt("nuCarimbo");
        transportadoraReg.transportadora = new Transportadora();
        transportadoraReg.transportadora.cdEmpresa = transportadoraReg.cdEmpresa;
        transportadoraReg.transportadora.cdTransportadora = transportadoraReg.cdTransportadora;
        transportadoraReg.transportadora.nmTransportadora = rs.getString("nmTransportadora");
        transportadoraReg.transportadora.flSomaFrete = rs.getString("flSomaFrete");
        transportadoraReg.transportadora.flMostraFrete = rs.getString("flMostraFrete");
        transportadoraReg.regiao = new Regiao();
        transportadoraReg.regiao.cdRegiao = transportadoraReg.cdRegiao;
        transportadoraReg.regiao.nmRegiao = rs.getString("nmRegiao");
        transportadoraReg.tipoFrete = new TipoFrete();
        if (transpRegFilter.tipoFreteFilter != null) {
        	transportadoraReg.tipoFrete.cdTipoFrete = transportadoraReg.cdTipoFrete; 
        	transportadoraReg.tipoFrete.dsTipoFrete = rs.getString("dsTipoFrete");
        	transportadoraReg.tipoFrete.flTipoFreteCif = rs.getString("flTipoFreteCif");
        	transportadoraReg.tipoFrete.flCalculaFrete = rs.getString("flCalculaFrete");
        	transportadoraReg.vlFrete = ValueUtil.round(rs.getDouble("vlFrete"));
        }
        return transportadoraReg;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
    	TransportadoraReg transRegFilter = (TransportadoraReg) domainFilter;
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDTRANSPORTADORA,");
        sql.append(" tb.CDREGIAO,");
        sql.append(" tb.CDTIPOFRETE,");
        sql.append(" tb.VLMINPEDIDO,");
        sql.append(" tb.VLMAXPEDIDO,");
        sql.append(" tb.FLPOSSUICONTRATO,");
        sql.append(" tb.VLPCTFRETE,");
        sql.append(" tb.VLMINFRETE,");
        sql.append(" tb.CDUSUARIO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.NUCARIMBO,");
        sql.append(" tr.NMTRANSPORTADORA,");
        sql.append(" tr.FLMOSTRAFRETE,");
        sql.append(" tr.FLSOMAFRETE,");
        if (transRegFilter.tipoFreteFilter != null) {
        	sql.append(" tfrete.DSTIPOFRETE,");
        	sql.append(" tfrete.FLTIPOFRETECIF,");
        	sql.append(" tfrete.FLCALCULAFRETE,");
        	sql.append(getVlFreteColumnSentence(transRegFilter)).append(" AS VLFRETE,");
        }
        sql.append(" REG.NMREGIAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		/**/
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.CDTRANSPORTADORA,");
        sql.append(" tb.CDREGIAO,");
        sql.append(" tb.CDTIPOFRETE,");
        sql.append(" tb.VLMINPEDIDO,");
        sql.append(" tb.VLMAXPEDIDO,");
        sql.append(" tb.FLPOSSUICONTRATO,");
        sql.append(" tb.VLPCTFRETE,");
        sql.append(" tb.VLMINFRETE,");
        sql.append(" tb.CDUSUARIO,");
        sql.append(" tb.FLTIPOALTERACAO,");
        sql.append(" tb.NUCARIMBO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        TransportadoraReg transportadoraReg = (TransportadoraReg) domain;
        sql.append(Sql.getValue(transportadoraReg.cdEmpresa)).append(",");
        sql.append(Sql.getValue(transportadoraReg.cdRepresentante)).append(",");
        sql.append(Sql.getValue(transportadoraReg.cdTransportadora)).append(",");
        sql.append(Sql.getValue(transportadoraReg.cdRegiao)).append(",");
        sql.append(Sql.getValue(transportadoraReg.cdTipoFrete)).append(",");
        sql.append(Sql.getValue(transportadoraReg.vlMinPedido)).append(",");
        sql.append(Sql.getValue(transportadoraReg.vlMaxPedido)).append(",");
        sql.append(Sql.getValue(transportadoraReg.flPossuiContrato)).append(",");
        sql.append(Sql.getValue(transportadoraReg.vlPctFrete)).append(",");
        sql.append(Sql.getValue(transportadoraReg.vlMinFrete)).append(",");
        sql.append(Sql.getValue(transportadoraReg.cdUsuario)).append(",");
        sql.append(Sql.getValue(transportadoraReg.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(transportadoraReg.nuCarimbo));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        TransportadoraReg transportadoraReg = (TransportadoraReg) domain;
        sql.append(" tb.VLMINPEDIDO = ").append(Sql.getValue(transportadoraReg.vlMinPedido)).append(",");
        sql.append(" tb.FLPOSSUICONTRATO = ").append(Sql.getValue(transportadoraReg.flPossuiContrato)).append(",");
        sql.append(" tb.VLPCTFRETE = ").append(Sql.getValue(transportadoraReg.vlPctFrete)).append(",");
        sql.append(" tb.VLMINFRETE = ").append(Sql.getValue(transportadoraReg.vlMinFrete)).append(",");
        sql.append(" tb.CDUSUARIO = ").append(Sql.getValue(transportadoraReg.cdUsuario)).append(",");
        sql.append(" tb.FLTIPOALTERACAO = ").append(Sql.getValue(transportadoraReg.flTipoAlteracao)).append(",");
        sql.append(" tb.NUCARIMBO = ").append(Sql.getValue(transportadoraReg.nuCarimbo));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        TransportadoraReg transportadoraReg = (TransportadoraReg) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", transportadoraReg.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", transportadoraReg.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDTRANSPORTADORA = ", transportadoraReg.cdTransportadora);
		sqlWhereClause.addAndCondition("tb.CDREGIAO = ", transportadoraReg.cdRegiao);
		sqlWhereClause.addAndCondition("tb.CDTIPOFRETE = ", transportadoraReg.cdTipoFrete);
		if (!transportadoraReg.ignoraVlMinMaxFrete) {
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addAndCondition("tb.VLMINPEDIDO <= ", transportadoraReg.vlMinPedido);
			sqlWhereClause.addOrConditionForced("tb.VLMINPEDIDO = ", 0);
			sqlWhereClause.addEndMultipleCondition();
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addAndCondition("tb.VLMAXPEDIDO >= ", transportadoraReg.vlMaxPedido);
			sqlWhereClause.addOrConditionForced("tb.VLMAXPEDIDO = ", 0);
			sqlWhereClause.addOrCondition("tb.VLMAXPEDIDO IS NULL");
			sqlWhereClause.addEndMultipleCondition();
		}
		if (transportadoraReg.regiaoFilter != null) {
			sqlWhereClause.addAndCondition(DaoUtil.getRegiaoJoinConditions(transportadoraReg.regiaoFilter));
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
 
    @Override
	protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		TransportadoraReg transpRegFilter = ((TransportadoraReg)domain);
    	if (transpRegFilter.ordenaPorFreteCifVlFrete && transpRegFilter.tipoFreteFilter != null) {
    		sql.append(" order by ").append(getOrderByColumnsFreteCifVlFrete(transpRegFilter));
    	} else if ("NMTRANSPORTADORA".equals(domain.sortAtributte)) {
			sql.append(" order by tr.NMTRANSPORTADORA");
		} else {
			super.addOrderBy(sql, domain);
		}
	}
    
    @Override
    protected void addJoin(BaseDomain domainFilter, StringBuffer sql) {
    	TransportadoraReg transpRegFilter = (TransportadoraReg) domainFilter;
    	DaoUtil.addJoinTransportadora(sql);
    	DaoUtil.addJoinRegiao(sql);
    	if (transpRegFilter.tipoFreteFilter != null) {
    		DaoUtil.addJoinTipoFrete(sql, transpRegFilter.tipoFreteFilter);
    	}
    }
    
    protected String getOrderByColumnsFreteCifVlFrete(TransportadoraReg transpRegFilter) {
    	StringBuffer sql = new StringBuffer();
    	sql.append("CASE WHEN TFRETE.FLTIPOFRETECIF = 'S' THEN 1 ELSE 2 END,");
    	sql.append(getVlFreteColumnSentence(transpRegFilter)).append(",");
    	sql.append(" tr.NMTRANSPORTADORA");
    	return sql.toString();
    }
    
    protected String getVlFreteColumnSentence(TransportadoraReg transpRegFilter) {
    	StringBuffer sql = new StringBuffer();
    	sql.append(" CASE WHEN TFRETE.FLCALCULAFRETE = 'S' THEN MAX(")
	    	.append(Sql.getValue(transpRegFilter.vlTotalPedidoFilter))
	    	.append(" * TB.VLPCTFRETE / 100, TB.VLMINFRETE) ELSE 0 END ");
    	return sql.toString();
    }
}