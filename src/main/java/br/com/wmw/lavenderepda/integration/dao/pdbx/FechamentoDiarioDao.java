package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import totalcross.sql.ResultSet;
import totalcross.util.Date;
import totalcross.util.Vector;


public class FechamentoDiarioDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new FechamentoDiario();
	}

    private static FechamentoDiarioDao instance;

    public FechamentoDiarioDao() {
        super(FechamentoDiario.TABLE_NAME); 
    }
    
    public static FechamentoDiarioDao getInstance() {
        if (instance == null) {
            instance = new FechamentoDiarioDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        FechamentoDiario fechamentoDiario = new FechamentoDiario();
        fechamentoDiario.rowKey = rs.getString("rowkey");
        fechamentoDiario.cdEmpresa = rs.getString("cdEmpresa");
        fechamentoDiario.cdRepresentante = rs.getString("cdRepresentante");
        fechamentoDiario.dtFechamentoDiario = rs.getDate("dtFechamentoDiario");
        fechamentoDiario.hrFechamentoDiario = rs.getString("hrFechamentoDiario");
        fechamentoDiario.vlTotalDepositoBancario = ValueUtil.round(rs.getDouble("vlTotalDepositoBancario"));
        fechamentoDiario.dtFinalizacao = rs.getDate("dtFinalizacao");
        fechamentoDiario.hrFinalizacao = rs.getString("hrFinalizacao");
        fechamentoDiario.vlTotalPedidos = ValueUtil.round(rs.getDouble("vlTotalPedidos"));
        fechamentoDiario.vlTotalReceitas = ValueUtil.round(rs.getDouble("vlTotalReceitas"));
        fechamentoDiario.vlTotalVendas = ValueUtil.round(rs.getDouble("vlTotalVendas"));
        fechamentoDiario.vlTotalVendasDinheiro = ValueUtil.round(rs.getDouble("vlTotalVendasDinheiro"));
        fechamentoDiario.vlTotalVendasCheque = ValueUtil.round(rs.getDouble("vlTotalVendasCheque"));
        fechamentoDiario.vlTotalVendasBoleto = ValueUtil.round(rs.getDouble("vlTotalVendasBoleto"));
        fechamentoDiario.vlTotalPagamentos = ValueUtil.round(rs.getDouble("vlTotalPagamentos"));
        fechamentoDiario.vlTotalPagamentosDinheiro = ValueUtil.round(rs.getDouble("vlTotalPagamentosDinheiro"));
        fechamentoDiario.vlTotalPagamentosCheque = ValueUtil.round(rs.getDouble("vlTotalPagamentosCheque"));
        fechamentoDiario.vlTotalPagamentos = ValueUtil.round(rs.getDouble("vlTotalPagamentos"));
        fechamentoDiario.vlTotalPagamentosDinheiro = ValueUtil.round(rs.getDouble("vlTotalPagamentosDinheiro"));
        fechamentoDiario.vlTotalPagamentosCheque = ValueUtil.round(rs.getDouble("vlTotalPagamentosCheque"));
        fechamentoDiario.vlTotalPagamentosBoleto = ValueUtil.round(rs.getDouble("vlTotalPagamentosBoleto"));
        fechamentoDiario.vlTotalBonificacao = ValueUtil.round(rs.getDouble("vlTotalBonificacao"));
        fechamentoDiario.vlTotalCreditoCliente = ValueUtil.round(rs.getDouble("vlTotalCreditoCliente"));
        fechamentoDiario.vlTotalDesconto = ValueUtil.round(rs.getDouble("vlTotalDesconto"));
        fechamentoDiario.nuImpressao = rs.getInt("nuImpressao");
        fechamentoDiario.flLiberadoSenha = rs.getString("flLiberadoSenha");
        fechamentoDiario.flAgrupado = rs.getString("flAgrupado");
        fechamentoDiario.vlTotalVendasOutros = ValueUtil.round(rs.getDouble("vlTotalVendasOutros"));
        fechamentoDiario.vlTotalPagamentosOutros = ValueUtil.round(rs.getDouble("vlTotalPagamentosOutros"));
        fechamentoDiario.dsPlacaVeiculo = rs.getString("dsPlacaVeiculo");
        fechamentoDiario.kmInicialVeiculo = rs.getDouble("kmInicialVeiculo");
	    fechamentoDiario.kmFinalVeiculo = rs.getDouble("kmFinalVeiculo");
        fechamentoDiario.cdUsuario = rs.getString("cdUsuario");
        fechamentoDiario.nuCarimbo = rs.getInt("nuCarimbo");
        fechamentoDiario.flTipoAlteracao = rs.getString("flTipoAlteracao");
        fechamentoDiario.hrAlteracao = rs.getString("hrAlteracao");
        fechamentoDiario.dtAlteracao = rs.getDate("dtAlteracao");
        return fechamentoDiario;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" DTFECHAMENTODIARIO,");
        sql.append(" HRFECHAMENTODIARIO,");
        sql.append(" VLTOTALDEPOSITOBANCARIO,");
        sql.append(" DTFINALIZACAO,");
        sql.append(" HRFINALIZACAO,");
        sql.append(" VLTOTALPEDIDOS,");
        sql.append(" VLTOTALRECEITAS,");
        sql.append(" VLTOTALVENDAS,");
        sql.append(" VLTOTALVENDASDINHEIRO,");
        sql.append(" VLTOTALVENDASCHEQUE,");
        sql.append(" VLTOTALVENDASBOLETO,");
        sql.append(" VLTOTALPAGAMENTOS,");
        sql.append(" VLTOTALPAGAMENTOSDINHEIRO,");
        sql.append(" VLTOTALPAGAMENTOSCHEQUE,");
        sql.append(" VLTOTALPAGAMENTOSBOLETO,");
        sql.append(" VLTOTALBONIFICACAO,");
        sql.append(" VLTOTALCREDITOCLIENTE,");
        sql.append(" VLTOTALDESCONTO,");
        sql.append(" NUIMPRESSAO,");
        sql.append(" FLLIBERADOSENHA,");
        sql.append(" FLAGRUPADO,");
        sql.append(" VLTOTALVENDASOUTROS,");
        sql.append(" VLTOTALPAGAMENTOSOUTROS,");
        sql.append(" DSPLACAVEICULO,");
	    sql.append(" KMINICIALVEICULO,");
	    sql.append(" KMFINALVEICULO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" DTALTERACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" DTFECHAMENTODIARIO,");
        sql.append(" HRFECHAMENTODIARIO,");
        sql.append(" VLTOTALDEPOSITOBANCARIO,");
        sql.append(" DTFINALIZACAO,");
        sql.append(" HRFINALIZACAO,");
        sql.append(" VLTOTALPEDIDOS,");
        sql.append(" VLTOTALRECEITAS,");
        sql.append(" VLTOTALVENDAS,");
        sql.append(" VLTOTALVENDASDINHEIRO,");
        sql.append(" VLTOTALVENDASCHEQUE,");
        sql.append(" VLTOTALVENDASBOLETO,");
        sql.append(" VLTOTALPAGAMENTOS,");
        sql.append(" VLTOTALPAGAMENTOSDINHEIRO,");
        sql.append(" VLTOTALPAGAMENTOSCHEQUE,");
        sql.append(" VLTOTALPAGAMENTOSBOLETO,");
        sql.append(" VLTOTALBONIFICACAO,");
        sql.append(" VLTOTALCREDITOCLIENTE,");
        sql.append(" VLTOTALDESCONTO,");
        sql.append(" NUIMPRESSAO,");
        sql.append(" FLLIBERADOSENHA,");
        sql.append(" FLAGRUPADO,");
        sql.append(" VLTOTALVENDASOUTROS,");
        sql.append(" VLTOTALPAGAMENTOSOUTROS,");
	    sql.append(" DSPLACAVEICULO,");
	    sql.append(" KMINICIALVEICULO,");
	    sql.append(" KMFINALVEICULO,");
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" DTALTERACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        FechamentoDiario fechamentoDiario = (FechamentoDiario) domain;
        sql.append(Sql.getValue(fechamentoDiario.cdEmpresa)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.cdRepresentante)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.dtFechamentoDiario)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.hrFechamentoDiario)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalDepositoBancario)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.dtFinalizacao)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.hrFinalizacao)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalPedidos)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalReceitas)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalVendas)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalVendasDinheiro)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalVendasCheque)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalVendasBoleto)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalPagamentos)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalPagamentosDinheiro)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalPagamentosCheque)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalPagamentosBoleto)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalBonificacao)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalCreditoCliente)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalDesconto)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.nuImpressao)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.flLiberadoSenha)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.flAgrupado)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalVendasOutros)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.vlTotalPagamentosOutros)).append(",");
	    sql.append(Sql.getValue(fechamentoDiario.dsPlacaVeiculo)).append(",");
	    sql.append(Sql.getValue(fechamentoDiario.kmInicialVeiculo)).append(",");
	    sql.append(Sql.getValue(fechamentoDiario.kmFinalVeiculo)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.cdUsuario)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.nuCarimbo)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.hrAlteracao)).append(",");
        sql.append(Sql.getValue(fechamentoDiario.dtAlteracao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        FechamentoDiario fechamentoDiario = (FechamentoDiario) domain;
        sql.append(" HRFECHAMENTODIARIO = ").append(Sql.getValue(fechamentoDiario.hrFechamentoDiario)).append(",");
        sql.append(" VLTOTALDEPOSITOBANCARIO = ").append(Sql.getValue(fechamentoDiario.vlTotalDepositoBancario)).append(",");
        sql.append(" DTFINALIZACAO = ").append(Sql.getValue(fechamentoDiario.dtFinalizacao)).append(",");
        sql.append(" HRFINALIZACAO = ").append(Sql.getValue(fechamentoDiario.hrFinalizacao)).append(",");
        sql.append(" VLTOTALPEDIDOS = ").append(Sql.getValue(fechamentoDiario.vlTotalPedidos)).append(",");
        sql.append(" VLTOTALRECEITAS = ").append(Sql.getValue(fechamentoDiario.vlTotalReceitas)).append(",");
        sql.append(" VLTOTALVENDAS = ").append(Sql.getValue(fechamentoDiario.vlTotalVendas)).append(",");
        sql.append(" VLTOTALVENDASDINHEIRO = ").append(Sql.getValue(fechamentoDiario.vlTotalVendasDinheiro)).append(",");
        sql.append(" VLTOTALVENDASCHEQUE = ").append(Sql.getValue(fechamentoDiario.vlTotalVendasCheque)).append(",");
        sql.append(" VLTOTALVENDASBOLETO = ").append(Sql.getValue(fechamentoDiario.vlTotalVendasBoleto)).append(",");
        sql.append(" VLTOTALPAGAMENTOS = ").append(Sql.getValue(fechamentoDiario.vlTotalPagamentos)).append(",");
        sql.append(" VLTOTALPAGAMENTOSDINHEIRO = ").append(Sql.getValue(fechamentoDiario.vlTotalPagamentosDinheiro)).append(",");
        sql.append(" VLTOTALPAGAMENTOSCHEQUE = ").append(Sql.getValue(fechamentoDiario.vlTotalPagamentosCheque)).append(",");
        sql.append(" VLTOTALPAGAMENTOSBOLETO = ").append(Sql.getValue(fechamentoDiario.vlTotalPagamentosBoleto)).append(",");
        sql.append(" VLTOTALBONIFICACAO = ").append(Sql.getValue(fechamentoDiario.vlTotalBonificacao)).append(",");
        sql.append(" VLTOTALCREDITOCLIENTE = ").append(Sql.getValue(fechamentoDiario.vlTotalCreditoCliente)).append(",");
        sql.append(" VLTOTALDESCONTO = ").append(Sql.getValue(fechamentoDiario.vlTotalDesconto)).append(",");
        sql.append(" NUIMPRESSAO = ").append(Sql.getValue(fechamentoDiario.nuImpressao)).append(",");
        sql.append(" VLTOTALVENDASOUTROS = ").append(Sql.getValue(fechamentoDiario.vlTotalVendasOutros)).append(",");
        sql.append(" VLTOTALPAGAMENTOSOUTROS = ").append(Sql.getValue(fechamentoDiario.vlTotalPagamentosOutros)).append(",");
	    sql.append(" DSPLACAVEICULO = ").append(Sql.getValue(fechamentoDiario.dsPlacaVeiculo)).append(",");
	    sql.append(" KMINICIALVEICULO = ").append(Sql.getValue(fechamentoDiario.kmInicialVeiculo)).append(",");
	    sql.append(" KMFINALVEICULO = ").append(Sql.getValue(fechamentoDiario.kmFinalVeiculo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(fechamentoDiario.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(fechamentoDiario.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(fechamentoDiario.flTipoAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(fechamentoDiario.hrAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(fechamentoDiario.dtAlteracao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        FechamentoDiario fechamentoDiario = (FechamentoDiario) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ",  fechamentoDiario.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", fechamentoDiario.cdRepresentante);
		sqlWhereClause.addAndCondition("DTFECHAMENTODIARIO = ", fechamentoDiario.dtFechamentoDiario);
		sqlWhereClause.addAndCondition("DTFECHAMENTODIARIO <= ", fechamentoDiario.dtFechamentoDiarioFilter);
		sqlWhereClause.addAndCondition("FLLIBERADOSENHA = ", fechamentoDiario.flLiberadoSenha);
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	FechamentoDiario fechamentoDiario = (FechamentoDiario) domain;
    	if (ValueUtil.isNotEmpty(fechamentoDiario.dtFechamentoDiarioFilter)) {
    		sql.append(" ORDER BY DTFECHAMENTODIARIO DESC");
    	} else {
    		super.addOrderBy(sql, domain);
    	}
    }
    
    public Date findDataPosteriorADataExclusaoFechamentoDiario(FechamentoDiario fechamentoDiarioFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" SELECT MAX(DTFECHAMENTODIARIO) as DTFECHAMENTODIARIO FROM  ");
    	sql.append(tableName);
    	addWhereByExample(fechamentoDiarioFilter, sql);
    	return getDate(sql.toString());
    }
    
    public Date findDataMinimaFechamentoDiario(FechamentoDiario fechamentoDiarioFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" SELECT MIN(DTFECHAMENTODIARIO) as DTFECHAMENTODIARIO FROM  ");
    	sql.append(tableName);
    	addWhereByExample(fechamentoDiarioFilter, sql);
    	return getDate(sql.toString());
    }
    
    public FechamentoDiario findUltimoFechamentoDiario(FechamentoDiario domain) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(domain, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addWhereByExample(domain, sql);
		sql.append(" ORDER BY DTFECHAMENTODIARIO DESC");
		Vector results = findAll(domain, sql.toString());
		if (ValueUtil.isEmpty(results)) return null;
		return (FechamentoDiario) results.items[0];
    }
	
    
}