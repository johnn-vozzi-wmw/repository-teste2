package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ResumoDia;
import totalcross.sql.ResultSet;

public class ResumoDiaDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ResumoDia();
	}

    private static ResumoDiaDbxDao instance;

    public ResumoDiaDbxDao() {
        super(ResumoDia.TABLE_NAME);
    }

    public static ResumoDiaDbxDao getInstance() {
        if (instance == null) {
            instance = new ResumoDiaDbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        ResumoDia resumoDia = new ResumoDia();
        resumoDia.rowKey = rs.getString("rowkey");
        resumoDia.cdEmpresa = rs.getString("cdEmpresa");
        resumoDia.cdRepresentante = rs.getString("cdRepresentante");
        resumoDia.dtResumo = rs.getDate("dtResumo");
        resumoDia.vlTotalVendido = ValueUtil.round(rs.getDouble("vlTotalVendido"));
        resumoDia.qtItensVendidos = rs.getInt("qtItensVendidos");
        resumoDia.qtPedidos = rs.getInt("qtPedidos");
        resumoDia.qtItensVendidosBonificados = rs.getInt("qtItensVendidosBonificados");
        resumoDia.vlVerbaConsumida = ValueUtil.round(rs.getDouble("vlVerbaConsumida"));
        resumoDia.nuCarimbo = rs.getInt("nuCarimbo");
        resumoDia.flTipoAlteracao = rs.getString("flTipoAlteracao");
        resumoDia.cdUsuario = rs.getString("cdUsuario");
        resumoDia.vlTotalBonificacao = ValueUtil.round(rs.getDouble("vlTotalBonificacao"));
        resumoDia.vlVerbaBonificacao = ValueUtil.round(rs.getDouble("vlVerbaBonificacao"));
        resumoDia.vlSaldoAnterior = ValueUtil.round(rs.getDouble("vlSaldoanterior"));
        resumoDia.vlTotalPagamento = ValueUtil.round(rs.getDouble("vlTotalpagamento"));
        resumoDia.vlTotalValorizacao = ValueUtil.round(rs.getDouble("vlTotalvalorizacao"));
        resumoDia.vlTotalPromissoria = ValueUtil.round(rs.getDouble("vlTotalpromissoria"));
        resumoDia.vlSaldoFinal = ValueUtil.round(rs.getDouble("vlSaldofinal"));
        resumoDia.vlTotalPontuacaoBase = ValueUtil.round(rs.getDouble("vlTotalPontuacaoBase"));
        resumoDia.vlTotalPontuacaoRealizado = ValueUtil.round(rs.getDouble("vlTotalPontuacaoRealizado"));
        resumoDia.dtUltimofechamento = rs.getDate("dtUltimofechamento");
        resumoDia.vlTotalPeso = ValueUtil.round(rs.getDouble("vlTotalPeso"));
        return resumoDia;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" DTRESUMO,");
        sql.append(" VLTOTALVENDIDO,");
        sql.append(" QTITENSVENDIDOS,");
        sql.append(" QTPEDIDOS,");
        sql.append(" QTITENSVENDIDOSBONIFICADOS,");
        sql.append(" VLVERBACONSUMIDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" VLTOTALBONIFICACAO,");
        sql.append(" VLVERBABONIFICACAO,");
        sql.append(" VLSALDOANTERIOR,");
        sql.append(" VLTOTALPAGAMENTO,");
        sql.append(" VLTOTALVALORIZACAO,");
        sql.append(" VLTOTALPROMISSORIA,");
        sql.append(" VLSALDOFINAL,");
        sql.append(" VLTOTALPONTUACAOBASE,");
        sql.append(" VLTOTALPONTUACAOREALIZADO,");
        sql.append(" VLTOTALPESO,");
        sql.append(" DTULTIMOFECHAMENTO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" DTRESUMO,");
        sql.append(" VLTOTALVENDIDO,");
        sql.append(" QTITENSVENDIDOS,");
        sql.append(" QTPEDIDOS,");
        sql.append(" QTITENSVENDIDOSBONIFICADOS,");
        sql.append(" VLVERBACONSUMIDA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" VLTOTALBONIFICACAO,");
        sql.append(" VLVERBABONIFICACAO,");
        sql.append(" VLSALDOANTERIOR,");
        sql.append(" VLTOTALPAGAMENTO,");
        sql.append(" VLTOTALVALORIZACAO,");
        sql.append(" VLTOTALPROMISSORIA,");
        sql.append(" VLSALDOFINAL,");
        sql.append(" VLTOTALPONTUACAOBASE,");
        sql.append(" VLTOTALPONTUACAOREALIZADO,");
	    sql.append(" VLTOTALPESO,");
        sql.append(" DTULTIMOFECHAMENTO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" VLTOTALVOLUME");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ResumoDia resumoDia = (ResumoDia) domain;
        sql.append(Sql.getValue(resumoDia.cdEmpresa)).append(",");
        sql.append(Sql.getValue(resumoDia.cdRepresentante)).append(",");
        sql.append(Sql.getValue(resumoDia.dtResumo)).append(",");
        sql.append(Sql.getValue(resumoDia.vlTotalVendido)).append(",");
        sql.append(Sql.getValue(resumoDia.qtItensVendidos)).append(",");
        sql.append(Sql.getValue(resumoDia.qtPedidos)).append(",");
        sql.append(Sql.getValue(resumoDia.qtItensVendidosBonificados)).append(",");
        sql.append(Sql.getValue(resumoDia.vlVerbaConsumida)).append(",");
        sql.append(Sql.getValue(resumoDia.nuCarimbo)).append(",");
        sql.append(Sql.getValue(resumoDia.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(resumoDia.cdUsuario)).append(",");
        sql.append(Sql.getValue(resumoDia.vlTotalBonificacao)).append(",");
        sql.append(Sql.getValue(resumoDia.vlVerbaBonificacao)).append(",");
        sql.append(Sql.getValue(resumoDia.vlSaldoAnterior)).append(",");
        sql.append(Sql.getValue(resumoDia.vlTotalPagamento)).append(",");
        sql.append(Sql.getValue(resumoDia.vlTotalValorizacao)).append(",");
        sql.append(Sql.getValue(resumoDia.vlTotalPromissoria)).append(",");
        sql.append(Sql.getValue(resumoDia.vlSaldoFinal)).append(",");
        sql.append(Sql.getValue(resumoDia.vlTotalPontuacaoBase)).append(",");
        sql.append(Sql.getValue(resumoDia.vlTotalPontuacaoRealizado)).append(",");
        sql.append(Sql.getValue(resumoDia.vlTotalPeso)).append(",");
        sql.append(Sql.getValue(resumoDia.dtUltimofechamento)).append(",");
        sql.append(Sql.getValue(resumoDia.dtAlteracao)).append(",");
        sql.append(Sql.getValue(resumoDia.hrAlteracao)).append(",");
        sql.append(Sql.getValue(resumoDia.vlTotalVolume));

    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ResumoDia resumoDia = (ResumoDia) domain;
        sql.append(" vlTotalVendido = ").append(Sql.getValue(resumoDia.vlTotalVendido)).append(",");
        sql.append(" qtItensVendidos = ").append(Sql.getValue(resumoDia.qtItensVendidos)).append(",");
        sql.append(" qtPedidos = ").append(Sql.getValue(resumoDia.qtPedidos)).append(",");
        sql.append(" qtItensVendidosBonificados = ").append(Sql.getValue(resumoDia.qtItensVendidosBonificados)).append(",");
        sql.append(" vlVerbaConsumida = ").append(Sql.getValue(resumoDia.vlVerbaConsumida)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(resumoDia.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(resumoDia.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(resumoDia.cdUsuario)).append(",");
        sql.append(" vlTotalBonificacao = ").append(Sql.getValue(resumoDia.vlTotalBonificacao)).append(",");
        sql.append(" vlVerbaBonificacao = ").append(Sql.getValue(resumoDia.vlVerbaBonificacao)).append(",");
        sql.append(" VLSALDOANTERIOR = ").append(Sql.getValue(resumoDia.vlSaldoAnterior)).append(",");
        sql.append(" VLTOTALPAGAMENTO = ").append(Sql.getValue(resumoDia.vlTotalPagamento)).append(",");
        sql.append(" VLTOTALVALORIZACAO = ").append(Sql.getValue(resumoDia.vlTotalValorizacao)).append(",");
        sql.append(" VLTOTALPROMISSORIA = ").append(Sql.getValue(resumoDia.vlTotalPromissoria)).append(",");
        sql.append(" VLSALDOFINAL = ").append(Sql.getValue(resumoDia.vlSaldoFinal)).append(",");
        sql.append(" VLTOTALPONTUACAOBASE = ").append(Sql.getValue(resumoDia.vlTotalPontuacaoBase)).append(",");
        sql.append(" VLTOTALPONTUACAOREALIZADO = ").append(Sql.getValue(resumoDia.vlTotalPontuacaoRealizado)).append(",");
        sql.append(" VLTOTALPESO = ").append(Sql.getValue(resumoDia.vlTotalPeso)).append(",");
        sql.append(" DTULTIMOFECHAMENTO = ").append(Sql.getValue(resumoDia.dtUltimofechamento)).append(",");
        sql.append(" VLTOTALVOLUME = ").append(Sql.getValue(resumoDia.vlTotalVolume));

    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        ResumoDia resumoDia = (ResumoDia) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", resumoDia.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", resumoDia.cdRepresentante);
		sqlWhereClause.addAndCondition("DTRESUMO = ", resumoDia.dtResumo);
		sqlWhereClause.addAndCondition("DTRESUMO <= ", resumoDia.dtResumoMenorIgualFilter);
		//--
		sql.append(sqlWhereClause.getSql());
    }

}